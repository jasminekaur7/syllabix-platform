package com.syllabix.controller;

import com.syllabix.dto.MarksDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.MarksService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marks")
public class MarksApiController {

    private final MarksService marksService;
    private final AuthenticationService authenticationService;

    public MarksApiController(MarksService marksService, AuthenticationService authenticationService) {
        this.marksService = marksService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<MarksDto>> getAllMarks() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(marksService.getAllMarks(student));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<MarksDto>> getMarksBySubject(@PathVariable Long subjectId) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(marksService.getMarksBySubject(subjectId, student));
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getMarksSummary() {
        Student student = authenticationService.getAuthenticatedStudent();
        Double overallAvg = marksService.calculateOverallAveragePercentage(student);

        Map<String, Object> summary = new HashMap<>();
        summary.put("overallAveragePercentage", overallAvg);
        summary.put("overallGrade", marksService.computeGrade(overallAvg));
        summary.put("estimatedCgpa", marksService.calculateEstimatedCgpa(overallAvg));
        return ResponseEntity.ok(summary);
    }

    @PostMapping
    public ResponseEntity<MarksDto> recordMarks(@Valid @RequestBody MarksDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(marksService.recordMarks(dto, student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarksDto> updateMarks(@PathVariable Long id, @Valid @RequestBody MarksDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(marksService.updateMarks(id, dto, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        marksService.deleteMarks(id, student);
        return ResponseEntity.noContent().build();
    }
}
