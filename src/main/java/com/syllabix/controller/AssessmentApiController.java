package com.syllabix.controller;

import com.syllabix.dto.AssessmentDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AssessmentService;
import com.syllabix.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentApiController {

    private final AssessmentService assessmentService;
    private final AuthenticationService authenticationService;

    public AssessmentApiController(AssessmentService assessmentService, AuthenticationService authenticationService) {
        this.assessmentService = assessmentService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDto>> getAllAssessments() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(assessmentService.getAllAssessments(student));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<AssessmentDto>> getUpcomingAssessments() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(assessmentService.getUpcomingAssessments(student));
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<AssessmentDto>> getAssessmentsForCalendar(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(assessmentService.getAssessmentsForCalendar(student, start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentDto> getAssessmentById(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(assessmentService.getAssessmentById(id, student));
    }

    @PostMapping
    public ResponseEntity<AssessmentDto> createAssessment(@Valid @RequestBody AssessmentDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(assessmentService.createAssessment(dto, student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssessmentDto> updateAssessment(@PathVariable Long id, @Valid @RequestBody AssessmentDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(assessmentService.updateAssessment(id, dto, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        assessmentService.deleteAssessment(id, student);
        return ResponseEntity.noContent().build();
    }
}
