package com.syllabix.controller;

import com.syllabix.dto.AssessmentDto;
import com.syllabix.dto.DashboardStatsDto;
import com.syllabix.dto.RecommendationDto;
import com.syllabix.dto.StudyTaskDto;
import com.syllabix.dto.SubjectDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AssessmentService;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.MarksService;
import com.syllabix.service.RecommendationEngineService;
import com.syllabix.service.StudyPlanService;
import com.syllabix.service.StudySessionService;
import com.syllabix.service.SubjectService;
import com.syllabix.repository.TopicRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final AuthenticationService authenticationService;
    private final SubjectService subjectService;
    private final AssessmentService assessmentService;
    private final StudyPlanService studyPlanService;
    private final StudySessionService studySessionService;
    private final RecommendationEngineService recommendationEngineService;
    private final MarksService marksService;
    private final TopicRepository topicRepository;

    public DashboardApiController(AuthenticationService authenticationService,
                                  SubjectService subjectService,
                                  AssessmentService assessmentService,
                                  StudyPlanService studyPlanService,
                                  StudySessionService studySessionService,
                                  RecommendationEngineService recommendationEngineService,
                                  MarksService marksService,
                                  TopicRepository topicRepository) {
        this.authenticationService = authenticationService;
        this.subjectService = subjectService;
        this.assessmentService = assessmentService;
        this.studyPlanService = studyPlanService;
        this.studySessionService = studySessionService;
        this.recommendationEngineService = recommendationEngineService;
        this.marksService = marksService;
        this.topicRepository = topicRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        Student student = authenticationService.getAuthenticatedStudent();
        DashboardStatsDto stats = new DashboardStatsDto();

        stats.setStudentName(student.getFullName());

        List<SubjectDto> subjects = subjectService.getAllSubjects(student);
        stats.setTotalEnrolledSubjects((long) subjects.size());
        stats.setSubjects(subjects);

        // Calculate overall syllabus progress
        long totalTopics = topicRepository.countByStudentId(student.getId());
        long completedTopics = topicRepository.countByStudentIdAndCompletionStatus(student.getId(), "COMPLETED");
        long inProgressTopics = topicRepository.countByStudentIdAndCompletionStatus(student.getId(), "IN_PROGRESS");
        long notStarted = Math.max(0, totalTopics - completedTopics - inProgressTopics);

        stats.setTotalTopics(totalTopics);
        stats.setCompletedTopics(completedTopics);
        stats.setInProgressTopics(inProgressTopics);
        stats.setNotStartedTopics(notStarted);

        double overallCompletion = totalTopics > 0 ? (completedTopics * 100.0 / totalTopics) : 0.0;
        stats.setOverallSyllabusCompletionPercentage(Math.round(overallCompletion * 10.0) / 10.0);

        // Upcoming assessments
        List<AssessmentDto> upcoming = assessmentService.getUpcomingAssessments(student);
        stats.setUpcomingAssessmentsCount((long) upcoming.size());
        stats.setUpcomingAssessments(upcoming.stream().limit(5).collect(Collectors.toList()));

        // Study tasks today
        List<StudyTaskDto> todayTasks = studyPlanService.getTasksForDate(student, LocalDate.now());
        stats.setTodayTasks(todayTasks);
        stats.setPendingTasksCount(todayTasks.stream().filter(t -> !Boolean.TRUE.equals(t.getIsCompleted())).count());
        stats.setCompletedTasksCount(todayTasks.stream().filter(t -> Boolean.TRUE.equals(t.getIsCompleted())).count());

        double todayPlannedMins = todayTasks.stream().mapToInt(t -> t.getDurationMinutes() != null ? t.getDurationMinutes() : 0).sum();
        stats.setTodayPlannedStudyHours(Math.round((todayPlannedMins / 60.0) * 10.0) / 10.0);
        stats.setTodayCompletedStudyHours(studySessionService.getTodayStudyHours(student));
        stats.setWeeklyStudyHours(studySessionService.getWeeklyStudyHours(student));
        stats.setCurrentStreakDays(studySessionService.calculateStudyStreakDays(student));

        // Academic performance marks average & estimated CGPA
        Double overallMarksAvg = marksService.calculateOverallAveragePercentage(student);
        stats.setOverallMarksAveragePercentage(overallMarksAvg);
        stats.setEstimatedCgpa(marksService.calculateEstimatedCgpa(overallMarksAvg));

        // Differentiator A: What Should I Study Today? (top 3 priority recommendations)
        List<RecommendationDto> topRecs = recommendationEngineService.getTopRecommendationsToday(student);
        stats.setTopRecommendationsToday(topRecs);

        // Subjects requiring attention
        List<SubjectDto> needingAttention = subjects.stream()
                .filter(s -> (s.getCompletionPercentage() != null && s.getCompletionPercentage() < 45.0)
                        || (s.getAverageMarksPercentage() != null && s.getAverageMarksPercentage() < 60.0)
                        || (s.getNextAssessmentDaysRemaining() != null && s.getNextAssessmentDaysRemaining() <= 5))
                .collect(Collectors.toList());
        stats.setSubjectsNeedingAttention(needingAttention);

        return ResponseEntity.ok(stats);
    }
}
