package com.syllabix.controller;

import com.syllabix.dto.TopicDto;
import com.syllabix.dto.UnitDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.SyllabusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
public class TopicApiController {

    private final SyllabusService syllabusService;
    private final AuthenticationService authenticationService;

    public TopicApiController(SyllabusService syllabusService, AuthenticationService authenticationService) {
        this.syllabusService = syllabusService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<TopicDto>> getTopicsForSubject(@PathVariable Long subjectId) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(syllabusService.getTopicsForSubject(subjectId, student));
    }

    @GetMapping("/units/subject/{subjectId}")
    public ResponseEntity<List<UnitDto>> getUnitsForSubject(@PathVariable Long subjectId) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(syllabusService.getUnitsForSubject(subjectId, student));
    }

    @PostMapping("/unit/{unitId}")
    public ResponseEntity<TopicDto> createTopic(@PathVariable Long unitId, @Valid @RequestBody TopicDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(syllabusService.createTopic(unitId, dto, student));
    }

    @PostMapping("/units/subject/{subjectId}")
    public ResponseEntity<UnitDto> createUnit(@PathVariable Long subjectId, @Valid @RequestBody UnitDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(syllabusService.createUnit(subjectId, dto, student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Long id, @RequestBody TopicDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(syllabusService.updateTopic(id, dto, student));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TopicDto> toggleStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Student student = authenticationService.getAuthenticatedStudent();
        String status = payload.getOrDefault("status", "COMPLETED");
        return ResponseEntity.ok(syllabusService.toggleTopicStatus(id, status, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        syllabusService.deleteTopic(id, student);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/units/{unitId}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long unitId) {
        Student student = authenticationService.getAuthenticatedStudent();
        syllabusService.deleteUnit(unitId, student);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TopicDto>> searchTopics(@RequestParam("q") String query) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(syllabusService.searchTopics(query, student));
    }
}
