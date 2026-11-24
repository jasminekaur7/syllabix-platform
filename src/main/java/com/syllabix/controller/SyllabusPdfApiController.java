package com.syllabix.controller;

import com.syllabix.dto.SyllabusExtractResultDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.SyllabusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/syllabus/pdf")
public class SyllabusPdfApiController {

    private final SyllabusService syllabusService;
    private final AuthenticationService authenticationService;

    public SyllabusPdfApiController(SyllabusService syllabusService, AuthenticationService authenticationService) {
        this.syllabusService = syllabusService;
        this.authenticationService = authenticationService;
    }

    /**
     * Advanced Feature: Extracts text from uploaded PDF and returns structured preview
     * of units and topics for student review and editing before saving.
     */
    @PostMapping("/extract")
    public ResponseEntity<SyllabusExtractResultDto> extractSyllabusPdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("A valid non-empty PDF file must be provided");
        }

        try {
            SyllabusExtractResultDto result = syllabusService.extractSyllabusFromPdf(file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read or parse PDF syllabus: " + e.getMessage(), e);
        }
    }

    /**
     * Persists the student-reviewed and approved units and topics for the specified subject.
     */
    @PostMapping("/save/{subjectId}")
    public ResponseEntity<Map<String, Object>> saveReviewedSyllabus(
            @PathVariable Long subjectId,
            @RequestBody SyllabusExtractResultDto payload) {
        Student student = authenticationService.getAuthenticatedStudent();
        syllabusService.saveReviewedExtractedSyllabus(subjectId, payload, student);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Syllabus units and topics successfully added to subject");
        return ResponseEntity.ok(response);
    }
}
