package com.syllabix.service;

import com.syllabix.dto.RecommendationDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationEngineService {

    public static final double DEFAULT_EXAM_URGENCY_WEIGHT = 0.35;
    public static final double DEFAULT_SYLLABUS_INCOMPLETION_WEIGHT = 0.30;
    public static final double DEFAULT_TOPIC_DIFFICULTY_WEIGHT = 0.20;
    public static final double DEFAULT_PERFORMANCE_WEAKNESS_WEIGHT = 0.15;

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final AssessmentRepository assessmentRepository;
    private final MarksRepository marksRepository;

    public RecommendationEngineService(SubjectRepository subjectRepository,
                                         TopicRepository topicRepository,
                                         AssessmentRepository assessmentRepository,
                                         MarksRepository marksRepository) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.assessmentRepository = assessmentRepository;
        this.marksRepository = marksRepository;
    }

    /**
     * Recomputes and updates priority scores on all pending topics for the student,
     * using the transparent weighted algorithm.
     */
    @Transactional
    public List<RecommendationDto> generateRecommendations(Student student,
                                                           Double weightUrgency,
                                                           Double weightIncompletion,
                                                           Double weightDifficulty,
                                                           Double weightWeakness) {
        double w1 = weightUrgency != null ? weightUrgency : DEFAULT_EXAM_URGENCY_WEIGHT;
        double w2 = weightIncompletion != null ? weightIncompletion : DEFAULT_SYLLABUS_INCOMPLETION_WEIGHT;
        double w3 = weightDifficulty != null ? weightDifficulty : DEFAULT_TOPIC_DIFFICULTY_WEIGHT;
        double w4 = weightWeakness != null ? weightWeakness : DEFAULT_PERFORMANCE_WEAKNESS_WEIGHT;

        // Normalize weights to sum to 1.0
        double totalWeight = w1 + w2 + w3 + w4;
        if (totalWeight <= 0) {
            w1 = DEFAULT_EXAM_URGENCY_WEIGHT;
            w2 = DEFAULT_SYLLABUS_INCOMPLETION_WEIGHT;
            w3 = DEFAULT_TOPIC_DIFFICULTY_WEIGHT;
            w4 = DEFAULT_PERFORMANCE_WEAKNESS_WEIGHT;
            totalWeight = 1.0;
        }
        w1 /= totalWeight;
        w2 /= totalWeight;
        w3 /= totalWeight;
        w4 /= totalWeight;

        List<Subject> subjects = subjectRepository.findByStudentId(student.getId());
        List<RecommendationDto> recommendations = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Subject subject : subjects) {
            // 1. Calculate Syllabus Incompletion factor (0 - 100)
            long totalTopics = topicRepository.countBySubjectId(subject.getId());
            long completedTopics = topicRepository.countBySubjectIdAndCompletionStatus(subject.getId(), "COMPLETED");
            double completionRate = totalTopics > 0 ? (completedTopics * 100.0 / totalTopics) : 0.0;
            double syllabusIncompletionFactor = Math.max(0.0, Math.min(100.0, 100.0 - completionRate));

            // 2. Calculate Exam Urgency factor (0 - 100)
            List<Assessment> upcoming = assessmentRepository.findNextUpcomingAssessmentForSubject(subject.getId(), now);
            double examUrgencyFactor = 10.0; // Default when no upcoming exam
            String nextExamInfo = null;
            Long daysUntilExam = null;

            if (!upcoming.isEmpty()) {
                Assessment nextExam = upcoming.get(0);
                daysUntilExam = Math.max(0, Duration.between(now, nextExam.getAssessmentDateTime()).toDays());
                nextExamInfo = nextExam.getTitle() + " in " + daysUntilExam + " day" + (daysUntilExam == 1 ? "" : "s");

                if (daysUntilExam <= 2) {
                    examUrgencyFactor = 100.0;
                } else if (daysUntilExam <= 5) {
                    examUrgencyFactor = 85.0;
                } else if (daysUntilExam <= 10) {
                    examUrgencyFactor = 65.0;
                } else if (daysUntilExam <= 20) {
                    examUrgencyFactor = 40.0;
                } else {
                    examUrgencyFactor = 25.0;
                }
            }

            // 3. Calculate Academic Weakness factor (0 - 100)
            Double avgMarks = marksRepository.calculateSubjectAveragePercentage(subject.getId());
            double performanceWeaknessFactor = (avgMarks != null) ? Math.max(0.0, Math.min(100.0, 100.0 - avgMarks)) : 50.0;

            // Fetch pending topics for this subject
            List<Topic> pendingTopics = topicRepository.findPendingTopicsBySubjectOrderByPriority(subject.getId());

            for (Topic topic : pendingTopics) {
                // 4. Calculate Topic Difficulty & Confidence factor (0 - 100)
                double baseDifficulty;
                String diff = topic.getDifficulty() != null ? topic.getDifficulty().toUpperCase() : "MEDIUM";
                switch (diff) {
                    case "HARD":
                        baseDifficulty = 90.0;
                        break;
                    case "EASY":
                        baseDifficulty = 30.0;
                        break;
                    case "MEDIUM":
                    default:
                        baseDifficulty = 60.0;
                        break;
                }

                // Confidence adjustment: 1 (lowest confidence) adds +20; 5 (highest confidence) subtracts -20
                int confidence = topic.getConfidenceLevel() != null ? topic.getConfidenceLevel() : 3;
                double adjustedDifficulty = baseDifficulty + ((3 - confidence) * 10.0);
                double topicDifficultyFactor = Math.max(10.0, Math.min(100.0, adjustedDifficulty));

                // Final Weighted Priority Score
                double priorityScore = (examUrgencyFactor * w1)
                        + (syllabusIncompletionFactor * w2)
                        + (topicDifficultyFactor * w3)
                        + (performanceWeaknessFactor * w4);

                priorityScore = Math.round(priorityScore * 10.0) / 10.0;
                topic.setPriorityScore(priorityScore);
                topicRepository.save(topic);

                // Build Human-Readable Rationale
                StringBuilder reason = new StringBuilder();
                if (daysUntilExam != null && daysUntilExam <= 5) {
                    reason.append(subject.getName()).append(" has ").append(nextExamInfo).append(". ");
                } else if (daysUntilExam != null && daysUntilExam <= 12) {
                    reason.append("Approaching assessment for ").append(subject.getName()).append(" (").append(nextExamInfo).append("). ");
                }

                if (syllabusIncompletionFactor >= 60.0) {
                    reason.append(String.format("%.0f%% of syllabus is still pending. ", syllabusIncompletionFactor));
                }

                if ("HARD".equalsIgnoreCase(diff)) {
                    reason.append("Categorized as Hard topic requiring deeper focus");
                    if (confidence <= 2) reason.append(" (low confidence level)");
                    reason.append(". ");
                } else if (confidence <= 2) {
                    reason.append("Low self-reported confidence (").append(confidence).append("/5) warrants targeted revision. ");
                }

                if (avgMarks != null && avgMarks < 65.0) {
                    reason.append(String.format("Subject average is currently %.1f%%; extra practice recommended.", avgMarks));
                }

                if (reason.length() == 0) {
                    reason.append("Recommended to maintain balanced progression across the academic curriculum.");
                }

                RecommendationDto dto = new RecommendationDto();
                dto.setTopicId(topic.getId());
                dto.setTopicName(topic.getName());
                dto.setSubjectId(subject.getId());
                dto.setSubjectName(subject.getName());
                dto.setSubjectCode(subject.getCode());
                dto.setSubjectColor(subject.getColorHex());
                dto.setDifficulty(topic.getDifficulty());
                dto.setEstimatedDurationMinutes(topic.getEstimatedDurationMinutes());
                dto.setPriorityScore(priorityScore);
                dto.setReason(reason.toString().trim());

                if (priorityScore >= 70.0) {
                    dto.setPriorityLabel("CRITICAL");
                } else if (priorityScore >= 50.0) {
                    dto.setPriorityLabel("HIGH");
                } else if (priorityScore >= 30.0) {
                    dto.setPriorityLabel("MEDIUM");
                } else {
                    dto.setPriorityLabel("LOW");
                }

                dto.setExamUrgencyFactor(Math.round(examUrgencyFactor * 10.0) / 10.0);
                dto.setSyllabusIncompletionFactor(Math.round(syllabusIncompletionFactor * 10.0) / 10.0);
                dto.setTopicDifficultyFactor(Math.round(topicDifficultyFactor * 10.0) / 10.0);
                dto.setPreviousPerformanceFactor(Math.round(performanceWeaknessFactor * 10.0) / 10.0);

                recommendations.add(dto);
            }
        }

        recommendations.sort(Comparator.comparing(RecommendationDto::getPriorityScore).reversed());
        return recommendations;
    }

    /**
     * Differentiator A: "What Should I Study Today?"
     * Returns top 3 most critical topics to study right now.
     */
    public List<RecommendationDto> getTopRecommendationsToday(Student student) {
        List<RecommendationDto> all = generateRecommendations(student, null, null, null, null);
        return all.stream().limit(3).collect(Collectors.toList());
    }
}
