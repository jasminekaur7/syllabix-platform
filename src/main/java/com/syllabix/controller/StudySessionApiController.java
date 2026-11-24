package com.syllabix.controller;

import com.syllabix.dto.StudySessionDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.StudySessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class StudySessionApiController {

    private final StudySessionService studySessionService;
    private final AuthenticationService authenticationService;

    public StudySessionApiController(StudySessionService studySessionService, AuthenticationService authenticationService) {
        this.studySessionService = studySessionService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<StudySessionDto>> getAllSessions() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(studySessionService.getAllSessions(student));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSessionStats() {
        Student student = authenticationService.getAuthenticatedStudent();
        Map<String, Object> stats = new HashMap<>();
        stats.put("todayStudyHours", studySessionService.getTodayStudyHours(student));
        stats.put("weeklyStudyHours", studySessionService.getWeeklyStudyHours(student));
        stats.put("currentStreakDays", studySessionService.calculateStudyStreakDays(student));
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<StudySessionDto> logSession(@Valid @RequestBody StudySessionDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(studySessionService.logSession(dto, student));
    }
}
