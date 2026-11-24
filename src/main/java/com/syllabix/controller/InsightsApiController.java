package com.syllabix.controller;

import com.syllabix.dto.ExamReadinessDto;
import com.syllabix.dto.RecommendationDto;
import com.syllabix.dto.WhatIfRequestDto;
import com.syllabix.dto.WhatIfResultDto;
import com.syllabix.entity.AcademicInsight;
import com.syllabix.entity.Student;
import com.syllabix.service.AcademicInsightsService;
import com.syllabix.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
public class InsightsApiController {

    private final AcademicInsightsService academicInsightsService;
    private final AuthenticationService authenticationService;

    public InsightsApiController(AcademicInsightsService academicInsightsService, AuthenticationService authenticationService) {
        this.academicInsightsService = academicInsightsService;
        this.authenticationService = authenticationService;
    }

    /**
     * Differentiator B: Exam Readiness Scores for all upcoming assessments
     */
    @GetMapping("/readiness")
    public ResponseEntity<List<ExamReadinessDto>> getExamReadinessScores() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(academicInsightsService.calculateExamReadinessScores(student));
    }

    /**
     * Differentiator C: What-If Academic Simulator
     */
    @PostMapping("/simulate")
    public ResponseEntity<WhatIfResultDto> simulateScenario(@RequestBody WhatIfRequestDto req) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(academicInsightsService.simulateAcademicScenario(student, req));
    }

    /**
     * Differentiator D: Smart Revision Opportunities based on spaced repetition
     */
    @GetMapping("/revision")
    public ResponseEntity<List<RecommendationDto>> getSmartRevisionList() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(academicInsightsService.getSmartRevisionOpportunities(student));
    }

    /**
     * Differentiator E: Academic Risk Alerts and telemetry
     */
    @GetMapping("/alerts")
    public ResponseEntity<List<AcademicInsight>> getRiskAlerts() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(academicInsightsService.detectAcademicRiskAlerts(student));
    }
}
