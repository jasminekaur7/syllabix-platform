package com.syllabix.service;

import com.syllabix.dto.SubjectDto;
import com.syllabix.entity.Marks;
import com.syllabix.entity.Student;
import com.syllabix.entity.StudySession;
import com.syllabix.entity.Subject;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.StudySessionRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final MarksRepository marksRepository;
    private final StudySessionRepository studySessionRepository;
    private final SubjectService subjectService;

    public AnalyticsService(SubjectRepository subjectRepository,
                            TopicRepository topicRepository,
                            MarksRepository marksRepository,
                            StudySessionRepository studySessionRepository,
                            SubjectService subjectService) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.marksRepository = marksRepository;
        this.studySessionRepository = studySessionRepository;
        this.subjectService = subjectService;
    }

    public Map<String, Object> getComprehensiveAnalytics(Student student) {
        Map<String, Object> response = new HashMap<>();

        List<SubjectDto> subjects = subjectService.getAllSubjects(student);
        List<Marks> allMarks = marksRepository.findByStudentIdOrderByAssessmentDateDesc(student.getId());

        // 1. Subject-wise performance bar chart data
        List<String> subjectLabels = new ArrayList<>();
        List<Double> subjectScores = new ArrayList<>();
        List<String> subjectColors = new ArrayList<>();

        for (SubjectDto sub : subjects) {
            subjectLabels.add(sub.getName() + " (" + sub.getCode() + ")");
            subjectScores.add(sub.getAverageMarksPercentage() != null ? sub.getAverageMarksPercentage() : 0.0);
            subjectColors.add(sub.getColorHex() != null ? sub.getColorHex() : "#4f46e5");
        }

        Map<String, Object> subjectPerformanceChart = new HashMap<>();
        subjectPerformanceChart.put("labels", subjectLabels);
        subjectPerformanceChart.put("data", subjectScores);
        subjectPerformanceChart.put("colors", subjectColors);
        response.put("subjectPerformanceChart", subjectPerformanceChart);

        // 2. Performance trend line chart data (over chronological assessments)
        List<Marks> sortedAsc = new ArrayList<>(allMarks);
        Collections.reverse(sortedAsc); // Oldest to newest
        List<String> trendDates = new ArrayList<>();
        List<Double> trendScores = new ArrayList<>();
        List<String> trendLabels = new ArrayList<>();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
        for (Marks m : sortedAsc) {
            trendDates.add(m.getAssessmentDate().format(dtf));
            trendScores.add(Math.round(m.getPercentage() * 10.0) / 10.0);
            trendLabels.add(m.getSubject().getCode() + " - " + m.getAssessmentType());
        }

        Map<String, Object> performanceTrendChart = new HashMap<>();
        performanceTrendChart.put("labels", trendDates);
        performanceTrendChart.put("data", trendScores);
        performanceTrendChart.put("tooltips", trendLabels);
        response.put("performanceTrendChart", performanceTrendChart);

        // 3. Overall syllabus completion breakdown
        long totalTopics = topicRepository.countByStudentId(student.getId());
        long completedTopics = topicRepository.countByStudentIdAndCompletionStatus(student.getId(), "COMPLETED");
        long inProgressTopics = topicRepository.countByStudentIdAndCompletionStatus(student.getId(), "IN_PROGRESS");
        long notStartedTopics = Math.max(0, totalTopics - completedTopics - inProgressTopics);

        Map<String, Object> syllabusChart = new HashMap<>();
        syllabusChart.put("labels", Arrays.asList("Completed", "In Progress", "Not Started"));
        syllabusChart.put("data", Arrays.asList(completedTopics, inProgressTopics, notStartedTopics));
        syllabusChart.put("colors", Arrays.asList("#10b981", "#3b82f6", "#cbd5e1"));
        response.put("syllabusBreakdownChart", syllabusChart);

        // 4. Weekly Study Hours by day of week
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        List<StudySession> recentSessions = studySessionRepository.findRecentSessions(student.getId(), weekStart.atStartOfDay());

        Map<LocalDate, Double> dailyMinutesMap = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            dailyMinutesMap.put(weekStart.plusDays(i), 0.0);
        }

        for (StudySession s : recentSessions) {
            LocalDate d = s.getStartTime().toLocalDate();
            if (dailyMinutesMap.containsKey(d)) {
                dailyMinutesMap.put(d, dailyMinutesMap.get(d) + s.getDurationMinutes());
            }
        }

        List<String> weekDayLabels = new ArrayList<>();
        List<Double> weekDayHours = new ArrayList<>();
        DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("EEE dd");

        for (int i = 0; i < 7; i++) {
            LocalDate d = weekStart.plusDays(i);
            weekDayLabels.add(d.format(dayFmt));
            double hours = Math.round((dailyMinutesMap.get(d) / 60.0) * 10.0) / 10.0;
            weekDayHours.add(hours);
        }

        Map<String, Object> weeklyStudyChart = new HashMap<>();
        weeklyStudyChart.put("labels", weekDayLabels);
        weeklyStudyChart.put("data", weekDayHours);
        response.put("weeklyStudyChart", weeklyStudyChart);

        // 5. Academic Strengths & Weaknesses
        List<Map<String, Object>> strengths = new ArrayList<>();
        List<Map<String, Object>> weaknesses = new ArrayList<>();

        for (SubjectDto sub : subjects) {
            if (sub.getAverageMarksPercentage() != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("subjectName", sub.getName());
                item.put("subjectCode", sub.getCode());
                item.put("averageMarks", sub.getAverageMarksPercentage());
                item.put("completion", sub.getCompletionPercentage());

                if (sub.getAverageMarksPercentage() >= 75.0 && sub.getCompletionPercentage() >= 50.0) {
                    item.put("status", "Strong Academic Mastery");
                    strengths.add(item);
                } else if (sub.getAverageMarksPercentage() < 60.0 || (sub.getCompletionPercentage() < 40.0 && sub.getNextAssessmentDaysRemaining() != null && sub.getNextAssessmentDaysRemaining() <= 7)) {
                    item.put("status", "Attention Required");
                    weaknesses.add(item);
                }
            }
        }

        response.put("strengths", strengths);
        response.put("weaknesses", weaknesses);

        // Overall metrics
        Double overallAvg = marksRepository.calculateOverallAveragePercentage(student.getId());
        response.put("overallAveragePercentage", overallAvg != null ? Math.round(overallAvg * 10.0) / 10.0 : null);
        response.put("totalAssessmentsRecorded", allMarks.size());

        return response;
    }
}
