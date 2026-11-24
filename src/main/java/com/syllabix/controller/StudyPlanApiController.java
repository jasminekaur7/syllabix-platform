package com.syllabix.controller;

import com.syllabix.dto.RecommendationDto;
import com.syllabix.dto.StudyPlanRequest;
import com.syllabix.dto.StudyTaskDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.RecommendationEngineService;
import com.syllabix.service.StudyPlanService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
public class StudyPlanApiController {

    private final StudyPlanService studyPlanService;
    private final RecommendationEngineService recommendationEngineService;
    private final AuthenticationService authenticationService;

    public StudyPlanApiController(StudyPlanService studyPlanService,
                                  RecommendationEngineService recommendationEngineService,
                                  AuthenticationService authenticationService) {
        this.studyPlanService = studyPlanService;
        this.recommendationEngineService = recommendationEngineService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/tasks/today")
    public ResponseEntity<List<StudyTaskDto>> getTodayTasks() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(studyPlanService.getTasksForDate(student, LocalDate.now()));
    }

    @GetMapping("/tasks/date")
    public ResponseEntity<List<StudyTaskDto>> getTasksForDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(studyPlanService.getTasksForDate(student, date));
    }

    @GetMapping("/tasks/week")
    public ResponseEntity<List<StudyTaskDto>> getTasksForWeek(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        Student student = authenticationService.getAuthenticatedStudent();
        LocalDate start = startDate != null ? startDate : LocalDate.now();
        return ResponseEntity.ok(studyPlanService.getTasksForWeek(student, start));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(
            @RequestParam(value = "urgencyWeight", required = false) Double urgencyWeight,
            @RequestParam(value = "incompletionWeight", required = false) Double incompletionWeight,
            @RequestParam(value = "difficultyWeight", required = false) Double difficultyWeight,
            @RequestParam(value = "weaknessWeight", required = false) Double weaknessWeight) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(recommendationEngineService.generateRecommendations(student, urgencyWeight, incompletionWeight, difficultyWeight, weaknessWeight));
    }

    @GetMapping("/recommendations/today")
    public ResponseEntity<List<RecommendationDto>> getTopRecommendationsToday() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(recommendationEngineService.getTopRecommendationsToday(student));
    }

    @PostMapping("/generate")
    public ResponseEntity<List<StudyTaskDto>> generatePlan(@RequestBody StudyPlanRequest request) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(studyPlanService.generateStudyPlan(student, request));
    }

    @PatchMapping("/tasks/{taskId}/toggle")
    public ResponseEntity<StudyTaskDto> toggleTaskCompletion(@PathVariable Long taskId) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(studyPlanService.toggleTaskCompletion(taskId, student));
    }

    @PatchMapping("/tasks/{taskId}/reschedule")
    public ResponseEntity<StudyTaskDto> rescheduleTask(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> payload) {
        Student student = authenticationService.getAuthenticatedStudent();
        LocalDate newDate = LocalDate.parse(payload.get("newDate"));
        return ResponseEntity.ok(studyPlanService.rescheduleTask(taskId, newDate, student));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        Student student = authenticationService.getAuthenticatedStudent();
        studyPlanService.deleteTask(taskId, student);
        return ResponseEntity.noContent().build();
    }
}
