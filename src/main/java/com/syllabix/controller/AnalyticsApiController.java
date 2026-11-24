package com.syllabix.controller;

import com.syllabix.entity.Student;
import com.syllabix.service.AnalyticsService;
import com.syllabix.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsApiController {

    private final AnalyticsService analyticsService;
    private final AuthenticationService authenticationService;

    public AnalyticsApiController(AnalyticsService analyticsService, AuthenticationService authenticationService) {
        this.analyticsService = analyticsService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(analyticsService.getComprehensiveAnalytics(student));
    }
}
