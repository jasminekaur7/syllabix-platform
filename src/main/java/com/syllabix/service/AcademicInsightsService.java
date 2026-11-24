package com.syllabix.service;

import com.syllabix.dto.ExamReadinessDto;
import com.syllabix.dto.RecommendationDto;
import com.syllabix.dto.WhatIfRequestDto;
import com.syllabix.dto.WhatIfResultDto;
import com.syllabix.entity.AcademicInsight;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Marks;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.repository.AcademicInsightRepository;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicInsightsService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final AssessmentRepository assessmentRepository;
    private final MarksRepository marksRepository;
    private final AcademicInsightRepository academicInsightRepository;
    private final MarksService marksService;

    public AcademicInsightsService(SubjectRepository subjectRepository,
                                   TopicRepository topicRepository,
                                   AssessmentRepository assessmentRepository,
                                   MarksRepository marksRepository,
                                   AcademicInsightRepository academicInsightRepository,
                                   MarksService marksService) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.assessmentRepository = assessmentRepository;
        this.marksRepository = marksRepository;
        this.academicInsightRepository = academicInsightRepository;
        this.marksService = marksService;
    }

    /**
     * Differentiator B: Calculates Exam Readiness Score for all upcoming assessments
     */
    public List<ExamReadinessDto> calculateExamReadinessScores(Student student) {
        LocalDateTime now = LocalDateTime.now();
        List<Assessment> upcoming = assessmentRepository.findUpcomingAssessments(student.getId(), now);
        List<ExamReadinessDto> list = new ArrayList<>();

        for (Assessment assessment : upcoming) {
            Subject subject = assessment.getSubject();
            long daysRemaining = Math.max(0, Duration.between(now, assessment.getAssessmentDateTime()).toDays());

            // 1. Syllabus Completion Rate (0 to 100)
            long totalTopics = topicRepository.countBySubjectId(subject.getId());
            long completedTopics = topicRepository.countBySubjectIdAndCompletionStatus(subject.getId(), "COMPLETED");
            double syllabusRate = totalTopics > 0 ? (completedTopics * 100.0 / totalTopics) : 50.0;

            // 2. Topic Difficulty Mastery: ratio of completed hard/medium topics
            List<Topic> subjectTopics = topicRepository.findBySubjectId(subject.getId());
            long totalHardOrMedium = subjectTopics.stream()
                    .filter(t -> "HARD".equalsIgnoreCase(t.getDifficulty()) || "MEDIUM".equalsIgnoreCase(t.getDifficulty()))
                    .count();
            long completedHardOrMedium = subjectTopics.stream()
                    .filter(t -> ("HARD".equalsIgnoreCase(t.getDifficulty()) || "MEDIUM".equalsIgnoreCase(t.getDifficulty()))
                            && "COMPLETED".equalsIgnoreCase(t.getCompletionStatus()))
                    .count();
            double difficultyMastery = totalHardOrMedium > 0 ? (completedHardOrMedium * 100.0 / totalHardOrMedium) : 70.0;

            // 3. Previous Academic Performance Score (0 to 100)
            Double avgMarks = marksRepository.calculateSubjectAveragePercentage(subject.getId());
            double performanceScore = avgMarks != null ? avgMarks : 65.0; // Neutral baseline if no marks yet

            // 4. Revision Coverage: proportion of topics studied or with confidence >= 3
            long confidentTopics = subjectTopics.stream()
                    .filter(t -> t.getConfidenceLevel() != null && t.getConfidenceLevel() >= 3)
                    .count();
            double revisionRate = totalTopics > 0 ? (confidentTopics * 100.0 / totalTopics) : 50.0;

            // 5. Time Sufficiency: pending hours required vs available preparation window
            double totalPendingHours = subjectTopics.stream()
                    .filter(t -> !"COMPLETED".equalsIgnoreCase(t.getCompletionStatus()))
                    .mapToInt(t -> t.getEstimatedDurationMinutes() != null ? t.getEstimatedDurationMinutes() : 45)
                    .sum() / 60.0;

            double availableHoursBeforeExam = daysRemaining * student.getDailyStudyHours();
            double timeSufficiency = availableHoursBeforeExam > 0
                    ? Math.min(100.0, (availableHoursBeforeExam / Math.max(1.0, totalPendingHours)) * 100.0)
                    : (totalPendingHours == 0 ? 100.0 : 20.0);

            // Composite Exam Readiness Score formula:
            // 30% Syllabus Completion + 20% Difficulty Mastery + 20% Performance + 15% Revision + 15% Time Sufficiency
            double compositeScore = (syllabusRate * 0.30)
                    + (difficultyMastery * 0.20)
                    + (performanceScore * 0.20)
                    + (revisionRate * 0.15)
                    + (timeSufficiency * 0.15);

            compositeScore = Math.max(0.0, Math.min(100.0, Math.round(compositeScore * 10.0) / 10.0));

            ExamReadinessDto dto = new ExamReadinessDto();
            dto.setAssessmentId(assessment.getId());
            dto.setAssessmentTitle(assessment.getTitle() + " (" + assessment.getType() + ")");
            dto.setSubjectId(subject.getId());
            dto.setSubjectName(subject.getName());
            dto.setSubjectCode(subject.getCode());
            dto.setAssessmentDateTime(assessment.getAssessmentDateTime());
            dto.setDaysRemaining(daysRemaining);
            dto.setReadinessScore(compositeScore);

            dto.setSyllabusCompletionRate(Math.round(syllabusRate * 10.0) / 10.0);
            dto.setTopicDifficultyMastery(Math.round(difficultyMastery * 10.0) / 10.0);
            dto.setPreviousPerformanceScore(Math.round(performanceScore * 10.0) / 10.0);
            dto.setRevisionCoverageRate(Math.round(revisionRate * 10.0) / 10.0);
            dto.setTimeSufficiencyScore(Math.round(timeSufficiency * 10.0) / 10.0);

            if (compositeScore >= 80.0) {
                dto.setReadinessGrade("HIGH_READINESS");
                dto.setExplanation(String.format("Strong preparation indicators across syllabus completion (%.0f%%) and historical scores (%.0f%%). Maintain retention with periodic light revision.", syllabusRate, performanceScore));
            } else if (compositeScore >= 60.0) {
                dto.setReadinessGrade("MODERATE_READINESS");
                dto.setExplanation(String.format("On track, but %.0f%% of syllabus remains pending. Dedicate focused sessions over the remaining %d days.", 100.0 - syllabusRate, daysRemaining));
            } else if (compositeScore >= 40.0) {
                dto.setReadinessGrade("AT_RISK");
                dto.setExplanation(String.format("Elevated risk: %d days remaining with syllabus completion at %.0f%%. Priority topics should be scheduled immediately.", daysRemaining, syllabusRate));
            } else {
                dto.setReadinessGrade("CRITICAL");
                dto.setExplanation(String.format("Urgent intervention needed: examination in %d days while syllabus readiness is only %.0f%%. Accelerate preparation hours.", daysRemaining, compositeScore));
            }

            list.add(dto);
        }

        return list;
    }

    /**
     * Differentiator C: What-If Academic Scenario Simulator
     */
    public WhatIfResultDto simulateAcademicScenario(Student student, WhatIfRequestDto req) {
        WhatIfResultDto res = new WhatIfResultDto();

        Double currentOverallAvg = marksRepository.calculateOverallAveragePercentage(student.getId());
        double currAvg = currentOverallAvg != null ? currentOverallAvg : 70.0;
        res.setCurrentOverallAveragePercentage(Math.round(currAvg * 10.0) / 10.0);
        res.setCurrentEstimatedCgpa(marksService.calculateEstimatedCgpa(currAvg));

        Subject targetSubject = null;
        double currSubAvg = 0.0;
        if (req.getTargetSubjectId() != null) {
            targetSubject = subjectRepository.findByIdAndStudentId(req.getTargetSubjectId(), student.getId()).orElse(null);
            if (targetSubject != null) {
                Double sAvg = marksRepository.calculateSubjectAveragePercentage(targetSubject.getId());
                currSubAvg = sAvg != null ? sAvg : currAvg;
                res.setCurrentSubjectAveragePercentage(Math.round(currSubAvg * 10.0) / 10.0);
            }
        }

        long totalStudentTopics = topicRepository.countByStudentId(student.getId());
        long completedStudentTopics = topicRepository.countByStudentIdAndCompletionStatus(student.getId(), "COMPLETED");
        double currSyllabus = totalStudentTopics > 0 ? (completedStudentTopics * 100.0 / totalStudentTopics) : 0.0;
        res.setCurrentSyllabusCompletionPercentage(Math.round(currSyllabus * 10.0) / 10.0);

        // Simulation calculations
        double projectedSubAvg = currSubAvg;
        if (targetSubject != null && req.getHypotheticalAssessmentScore() != null) {
            double maxMarks = req.getHypotheticalAssessmentMaxMarks() != null && req.getHypotheticalAssessmentMaxMarks() > 0
                    ? req.getHypotheticalAssessmentMaxMarks() : 100.0;
            double simPercentage = (req.getHypotheticalAssessmentScore() / maxMarks) * 100.0;

            List<Marks> subjectMarks = marksRepository.findByStudentIdAndSubjectId(student.getId(), targetSubject.getId());
            int count = subjectMarks.size();
            projectedSubAvg = ((currSubAvg * count) + simPercentage) / (count + 1);
            res.setProjectedSubjectAveragePercentage(Math.round(projectedSubAvg * 10.0) / 10.0);
            res.setSubjectPercentageDelta(Math.round((projectedSubAvg - currSubAvg) * 10.0) / 10.0);
        }

        // Projected Overall Average
        List<Marks> allMarks = marksRepository.findByStudentId(student.getId());
        int totalMarksCount = allMarks.size();
        double projectedOverall;
        if (req.getHypotheticalAssessmentScore() != null) {
            double maxMarks = req.getHypotheticalAssessmentMaxMarks() != null ? req.getHypotheticalAssessmentMaxMarks() : 100.0;
            double simScorePct = (req.getHypotheticalAssessmentScore() / maxMarks) * 100.0;
            projectedOverall = ((currAvg * totalMarksCount) + simScorePct) / (totalMarksCount + 1);
        } else {
            projectedOverall = currAvg;
        }

        res.setProjectedOverallAveragePercentage(Math.round(projectedOverall * 10.0) / 10.0);
        res.setPercentageDelta(Math.round((projectedOverall - currAvg) * 10.0) / 10.0);
        res.setProjectedEstimatedCgpa(marksService.calculateEstimatedCgpa(projectedOverall));

        // Projected Syllabus Completion
        int addTopics = req.getAdditionalTopicsCompleted() != null ? req.getAdditionalTopicsCompleted() : 0;
        long projectedCompletedTopics = Math.min(totalStudentTopics, completedStudentTopics + addTopics);
        double projSyllabus = totalStudentTopics > 0 ? (projectedCompletedTopics * 100.0 / totalStudentTopics) : 0.0;
        res.setProjectedSyllabusCompletionPercentage(Math.round(projSyllabus * 10.0) / 10.0);

        // Human narrative analysis
        StringBuilder summary = new StringBuilder("Simulation scenario: ");
        if (targetSubject != null && req.getHypotheticalAssessmentScore() != null) {
            summary.append(String.format("Scoring %.0f in %s increases subject average from %.1f%% to %.1f%% (%+.1f%%). ",
                    req.getHypotheticalAssessmentScore(), targetSubject.getCode(), currSubAvg, projectedSubAvg, (projectedSubAvg - currSubAvg)));
        }
        if (addTopics > 0) {
            summary.append(String.format("Completing %d additional topics elevates overall syllabus completion from %.1f%% to %.1f%%. ",
                    addTopics, currSyllabus, projSyllabus));
        }
        if (req.getAdditionalDailyStudyHours() != null && req.getAdditionalDailyStudyHours() > 0) {
            summary.append(String.format("Studying +%.1f hours/day adds %.1f hours of preparation per week, accelerating topic completion pace by ~%.0f%%.",
                    req.getAdditionalDailyStudyHours(), req.getAdditionalDailyStudyHours() * 7, (req.getAdditionalDailyStudyHours() / Math.max(1.0, student.getDailyStudyHours())) * 100));
        }

        res.setScenarioSummary(summary.toString());
        res.setImpactAnalysis(String.format("Estimated CGPA shifts from %s to %s based on simulated grade impact.",
                res.getCurrentEstimatedCgpa(), res.getProjectedEstimatedCgpa()));

        return res;
    }

    /**
     * Differentiator D: Smart Revision Scheduler based on spaced repetition
     * Returns topics that were studied in the past or have low confidence
     */
    public List<RecommendationDto> getSmartRevisionOpportunities(Student student) {
        List<Topic> pendingOrLowConfidence = topicRepository.findByStudentId(student.getId()).stream()
                .filter(t -> (t.getConfidenceLevel() != null && t.getConfidenceLevel() <= 3)
                        || (t.getLastStudiedDate() != null && t.getLastStudiedDate().isBefore(LocalDate.now().minusDays(5))))
                .collect(Collectors.toList());

        List<RecommendationDto> revisionList = new ArrayList<>();
        for (Topic topic : pendingOrLowConfidence) {
            RecommendationDto dto = new RecommendationDto();
            dto.setTopicId(topic.getId());
            dto.setTopicName(topic.getName());
            dto.setSubjectId(topic.getSubject().getId());
            dto.setSubjectName(topic.getSubject().getName());
            dto.setSubjectCode(topic.getSubject().getCode());
            dto.setSubjectColor(topic.getSubject().getColorHex());
            dto.setDifficulty(topic.getDifficulty());
            dto.setEstimatedDurationMinutes(topic.getEstimatedDurationMinutes() != null ? topic.getEstimatedDurationMinutes() : 30);

            String reason;
            if (topic.getLastStudiedDate() != null) {
                long daysAgo = Duration.between(topic.getLastStudiedDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
                reason = "Last reviewed " + daysAgo + " days ago. Spaced repetition interval reached to maintain memory retention.";
            } else {
                reason = "Reported confidence is low (" + topic.getConfidenceLevel() + "/5). Recommended for focused reinforcement.";
            }

            dto.setReason(reason);
            dto.setPriorityScore(topic.getPriorityScore() != null ? topic.getPriorityScore() : 50.0);
            dto.setPriorityLabel("REVISION");
            revisionList.add(dto);
        }

        return revisionList;
    }

    /**
     * Differentiator E: Generate Academic Risk Alerts
     */
    @Transactional
    public List<AcademicInsight> detectAcademicRiskAlerts(Student student) {
        academicInsightRepository.deleteByStudentId(student.getId());
        List<AcademicInsight> insights = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        List<Assessment> upcoming = assessmentRepository.findUpcomingAssessments(student.getId(), now);

        // Risk 1: Assessment in <= 5 days with low syllabus completion (<45%)
        for (Assessment a : upcoming) {
            long days = Math.max(0, Duration.between(now, a.getAssessmentDateTime()).toDays());
            if (days <= 5) {
                Subject sub = a.getSubject();
                long totalTopics = topicRepository.countBySubjectId(sub.getId());
                long completedTopics = topicRepository.countBySubjectIdAndCompletionStatus(sub.getId(), "COMPLETED");
                double pct = totalTopics > 0 ? (completedTopics * 100.0 / totalTopics) : 0.0;

                if (pct < 45.0) {
                    insights.add(new AcademicInsight(
                            student,
                            "RISK_ALERT",
                            "Urgent Exam Risk: " + sub.getName() + " (" + sub.getCode() + ")",
                            String.format("Your %s is in %d day%s, but syllabus completion is only %.0f%% (%d of %d topics completed).",
                                    a.getTitle(), days, days == 1 ? "" : "s", pct, completedTopics, totalTopics),
                            "CRITICAL",
                            "Use the Study Planner to generate an emergency revision schedule focusing exclusively on high-priority topics."
                    ));
                }
            }
        }

        // Risk 2: Multiple exams packed closely together (<= 2 days apart)
        for (int i = 0; i < upcoming.size() - 1; i++) {
            Assessment a1 = upcoming.get(i);
            Assessment a2 = upcoming.get(i + 1);
            long daysApart = Math.abs(Duration.between(a1.getAssessmentDateTime(), a2.getAssessmentDateTime()).toDays());
            if (daysApart <= 2) {
                insights.add(new AcademicInsight(
                        student,
                        "RISK_ALERT",
                        "High Assessment Density Detected",
                        String.format("%s (%s) and %s (%s) are scheduled only %d day%s apart.",
                                a1.getTitle(), a1.getSubject().getCode(), a2.getTitle(), a2.getSubject().getCode(), daysApart, daysApart == 1 ? "" : "s"),
                        "WARNING",
                        "Pre-load preparation for the second exam this week to avoid last-minute cramming."
                ));
                break;
            }
        }

        // Risk 3: Subject with average score significantly below cohort target
        List<Subject> subjects = subjectRepository.findByStudentId(student.getId());
        for (Subject s : subjects) {
            Double avg = marksRepository.calculateSubjectAveragePercentage(s.getId());
            if (avg != null && avg < 55.0) {
                insights.add(new AcademicInsight(
                        student,
                        "STRENGTH_WEAKNESS",
                        "Academic Weakness in " + s.getName(),
                        String.format("Historical performance in %s averages %.1f%%, noticeably lower than your target grade %s.",
                                s.getCode(), avg, s.getTargetGrade()),
                        "WARNING",
                        "Prioritize problem-solving and lab exercises for pending units before the next test."
                ));
            }
        }

        // Success Insight if student has high completion or good average
        Double overallAvg = marksRepository.calculateOverallAveragePercentage(student.getId());
        if (overallAvg != null && overallAvg >= 80.0) {
            insights.add(new AcademicInsight(
                    student,
                    "STRENGTH_WEAKNESS",
                    "Strong Academic Momentum",
                    String.format("Overall marks average is outstanding at %.1f%% (estimated CGPA: %s).",
                            overallAvg, marksService.calculateEstimatedCgpa(overallAvg)),
                    "SUCCESS",
                    "Continue consistent study habits and Pomodoro sessions to sustain high performance."
            ));
        }

        academicInsightRepository.saveAll(insights);
        return insights;
    }
}
