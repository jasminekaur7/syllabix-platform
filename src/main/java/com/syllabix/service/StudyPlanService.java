package com.syllabix.service;

import com.syllabix.dto.RecommendationDto;
import com.syllabix.dto.StudyPlanRequest;
import com.syllabix.dto.StudyTaskDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Student;
import com.syllabix.entity.StudyPlan;
import com.syllabix.entity.StudyTask;
import com.syllabix.entity.Topic;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.StudyPlanRepository;
import com.syllabix.repository.StudyTaskRepository;
import com.syllabix.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyPlanService {

    private final StudyPlanRepository studyPlanRepository;
    private final StudyTaskRepository studyTaskRepository;
    private final TopicRepository topicRepository;
    private final AssessmentRepository assessmentRepository;
    private final RecommendationEngineService recommendationEngineService;

    public StudyPlanService(StudyPlanRepository studyPlanRepository,
                            StudyTaskRepository studyTaskRepository,
                            TopicRepository topicRepository,
                            AssessmentRepository assessmentRepository,
                            RecommendationEngineService recommendationEngineService) {
        this.studyPlanRepository = studyPlanRepository;
        this.studyTaskRepository = studyTaskRepository;
        this.topicRepository = topicRepository;
        this.assessmentRepository = assessmentRepository;
        this.recommendationEngineService = recommendationEngineService;
    }

    public List<StudyTaskDto> getTasksForDate(Student student, LocalDate date) {
        List<StudyTask> tasks = studyTaskRepository.findByStudentIdAndPlannedDate(student.getId(), date);
        return tasks.stream().map(this::toTaskDto).collect(Collectors.toList());
    }

    public List<StudyTaskDto> getTasksForWeek(Student student, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<StudyTask> tasks = studyTaskRepository.findByStudentIdAndPlannedDateBetweenOrderByPlannedDateAscStartTimeAsc(student.getId(), startDate, endDate);
        return tasks.stream().map(this::toTaskDto).collect(Collectors.toList());
    }

    public List<StudyTaskDto> getPendingTasks(Student student) {
        List<StudyTask> tasks = studyTaskRepository.findByStudentIdAndIsCompletedFalseOrderByPlannedDateAscPriorityScoreDesc(student.getId());
        return tasks.stream().map(this::toTaskDto).collect(Collectors.toList());
    }

    /**
     * Automatic Study Timetable Generator:
     * Disperses high-priority pending topics over the plan window, respecting student daily study hours,
     * preferred study timings, spacing out revision, and never scheduling a topic after its relevant exam!
     */
    @Transactional
    public List<StudyTaskDto> generateStudyPlan(Student student, StudyPlanRequest request) {
        LocalDate start = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate end = request.getEndDate() != null ? request.getEndDate() : start.plusDays(13); // Default 2-week plan
        double dailyHours = request.getDailyStudyHours() != null ? request.getDailyStudyHours() : student.getDailyStudyHours();
        if (dailyHours <= 0) dailyHours = 3.0;

        int dailyBudgetMinutes = (int) (dailyHours * 60);

        // Delete any future uncompleted tasks to regenerate fresh plan
        studyTaskRepository.deleteByStudentIdAndPlannedDateGreaterThanEqual(student.getId(), start);

        // 1. Run Recommendation Engine to rank all pending topics by priority
        List<RecommendationDto> ranked = recommendationEngineService.generateRecommendations(
                student,
                request.getExamUrgencyWeight(),
                request.getSyllabusIncompletionWeight(),
                request.getTopicDifficultyWeight(),
                request.getPreviousPerformanceWeight()
        );

        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            ranked = ranked.stream()
                    .filter(r -> request.getSubjectIds().contains(r.getSubjectId()))
                    .collect(Collectors.toList());
        }

        // Determine study time slot according to preferred study timings
        String timing = student.getPreferredStudyTimings() != null ? student.getPreferredStudyTimings().toUpperCase() : "EVENING";
        LocalTime baseStartTime;
        switch (timing) {
            case "MORNING":
                baseStartTime = LocalTime.of(8, 0);
                break;
            case "AFTERNOON":
                baseStartTime = LocalTime.of(14, 0);
                break;
            case "NIGHT":
                baseStartTime = LocalTime.of(21, 0);
                break;
            case "EVENING":
            default:
                baseStartTime = LocalTime.of(18, 0);
                break;
        }

        List<StudyTask> createdTasks = new ArrayList<>();
        LocalDate currentDate = start;
        int currentDayMinutesUsed = 0;
        LocalTime currentSlotTime = baseStartTime;

        int topicIndex = 0;
        int totalRanked = ranked.size();

        while (!currentDate.isAfter(end) && topicIndex < totalRanked) {
            RecommendationDto rec = ranked.get(topicIndex);
            Topic topic = topicRepository.findById(rec.getTopicId()).orElse(null);

            if (topic != null) {
                // Check if an upcoming exam for this subject occurs BEFORE this currentDate
                List<Assessment> nextExams = assessmentRepository.findNextUpcomingAssessmentForSubject(rec.getSubjectId(), LocalDateTime.now());
                boolean examAlreadyPassed = false;
                if (!nextExams.isEmpty()) {
                    LocalDate examDate = nextExams.get(0).getAssessmentDateTime().toLocalDate();
                    if (currentDate.isAfter(examDate)) {
                        examAlreadyPassed = true;
                    }
                }

                if (!examAlreadyPassed) {
                    int sessionDuration = topic.getEstimatedDurationMinutes() != null ? topic.getEstimatedDurationMinutes() : 45;
                    // Cap individual session to 60 mins to encourage healthy Pomodoro intervals
                    if (sessionDuration > 60) sessionDuration = 60;

                    // If adding this exceeds daily budget, advance to next day
                    if (currentDayMinutesUsed + sessionDuration > dailyBudgetMinutes && currentDayMinutesUsed > 0) {
                        currentDate = currentDate.plusDays(1);
                        currentDayMinutesUsed = 0;
                        currentSlotTime = baseStartTime;
                        continue;
                    }

                    LocalTime slotEndTime = currentSlotTime.plusMinutes(sessionDuration);

                    StudyTask task = new StudyTask(
                            student,
                            topic,
                            topic.getSubject(),
                            currentDate,
                            currentSlotTime,
                            slotEndTime,
                            sessionDuration,
                            "NEW_LEARNING",
                            rec.getPriorityLabel(),
                            rec.getPriorityScore(),
                            rec.getReason()
                    );

                    createdTasks.add(task);
                    currentDayMinutesUsed += sessionDuration;

                    // Add 10-minute break interval
                    currentSlotTime = slotEndTime.plusMinutes(10);
                }
            }

            topicIndex++;
        }

        studyTaskRepository.saveAll(createdTasks);

        // Record plan summary in study_plans table
        StudyPlan plan = new StudyPlan(
                student,
                request.getPlanName() != null ? request.getPlanName() : ("Adaptive Plan: " + start + " to " + end),
                start,
                end,
                dailyHours,
                createdTasks.size(),
                String.format("{\"urgency\":%.2f,\"incompletion\":%.2f,\"difficulty\":%.2f,\"performance\":%.2f}",
                        request.getExamUrgencyWeight() != null ? request.getExamUrgencyWeight() : 0.35,
                        request.getSyllabusIncompletionWeight() != null ? request.getSyllabusIncompletionWeight() : 0.30,
                        request.getTopicDifficultyWeight() != null ? request.getTopicDifficultyWeight() : 0.20,
                        request.getPreviousPerformanceWeight() != null ? request.getPreviousPerformanceWeight() : 0.15)
        );
        studyPlanRepository.save(plan);

        return createdTasks.stream().map(this::toTaskDto).collect(Collectors.toList());
    }

    @Transactional
    public StudyTaskDto toggleTaskCompletion(Long taskId, Student student) {
        StudyTask task = studyTaskRepository.findByIdAndStudentId(taskId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Study task not found: " + taskId));

        boolean nowCompleted = !Boolean.TRUE.equals(task.getIsCompleted());
        task.setIsCompleted(nowCompleted);
        task.setCompletedAt(nowCompleted ? LocalDateTime.now() : null);

        if (nowCompleted) {
            Topic topic = task.getTopic();
            if (topic != null) {
                topic.setLastStudiedDate(LocalDate.now());
                if (!"COMPLETED".equalsIgnoreCase(topic.getCompletionStatus())) {
                    topic.setCompletionStatus("IN_PROGRESS");
                }
                topicRepository.save(topic);
            }
        }

        studyTaskRepository.save(task);
        return toTaskDto(task);
    }

    @Transactional
    public StudyTaskDto rescheduleTask(Long taskId, LocalDate newDate, Student student) {
        StudyTask task = studyTaskRepository.findByIdAndStudentId(taskId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Study task not found: " + taskId));

        task.setPlannedDate(newDate);
        task.setRationale(task.getRationale() + " (Rescheduled from " + task.getPlannedDate() + ")");
        studyTaskRepository.save(task);
        return toTaskDto(task);
    }

    @Transactional
    public void deleteTask(Long taskId, Student student) {
        StudyTask task = studyTaskRepository.findByIdAndStudentId(taskId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Study task not found: " + taskId));

        studyTaskRepository.delete(task);
    }

    public StudyTaskDto toTaskDto(StudyTask task) {
        StudyTaskDto dto = new StudyTaskDto();
        dto.setId(task.getId());
        dto.setTopicId(task.getTopic().getId());
        dto.setTopicName(task.getTopic().getName());
        dto.setTopicDifficulty(task.getTopic().getDifficulty());
        dto.setSubjectId(task.getSubject().getId());
        dto.setSubjectName(task.getSubject().getName());
        dto.setSubjectCode(task.getSubject().getCode());
        dto.setSubjectColor(task.getSubject().getColorHex());
        dto.setPlannedDate(task.getPlannedDate());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setDurationMinutes(task.getDurationMinutes());
        dto.setTaskType(task.getTaskType());
        dto.setPriority(task.getPriority());
        dto.setPriorityScore(task.getPriorityScore());
        dto.setRationale(task.getRationale());
        dto.setIsCompleted(task.getIsCompleted());
        dto.setCompletedAt(task.getCompletedAt());
        return dto;
    }
}
