package com.syllabix.controller;

import com.syllabix.dto.SubjectDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectApiController {

    private final SubjectService subjectService;
    private final AuthenticationService authenticationService;

    public SubjectApiController(SubjectService subjectService, AuthenticationService authenticationService) {
        this.subjectService = subjectService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(subjectService.getAllSubjects(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(subjectService.getSubjectById(id, student));
    }

    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@Valid @RequestBody SubjectDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(subjectService.createSubject(dto, student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(subjectService.updateSubject(id, dto, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        subjectService.deleteSubject(id, student);
        return ResponseEntity.noContent().build();
    }
}
