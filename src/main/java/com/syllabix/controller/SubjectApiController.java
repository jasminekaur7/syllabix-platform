package com.syllabix.controller;

import com.syllabix.dto.CourseEvaluationSchemeDto;
import com.syllabix.dto.LabAssignmentDto;
import com.syllabix.dto.SubjectDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import com.syllabix.service.CourseEvaluationParserService;
import com.syllabix.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectApiController {

    private final SubjectService subjectService;
    private final AuthenticationService authenticationService;
    private final CourseEvaluationParserService courseEvaluationParserService;

    public SubjectApiController(SubjectService subjectService,
                                AuthenticationService authenticationService,
                                CourseEvaluationParserService courseEvaluationParserService) {
        this.subjectService = subjectService;
        this.authenticationService = authenticationService;
        this.courseEvaluationParserService = courseEvaluationParserService;
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

    // =========================================================================
    // THAPAR LMS COURSE EVALUATION SCHEME & TARGET GRADE CALCULATOR ENDPOINTS
    // =========================================================================

    @PostMapping("/{id}/evaluation-scheme/parse")
    public ResponseEntity<CourseEvaluationSchemeDto> parseEvaluationScheme(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        Student student = authenticationService.getAuthenticatedStudent();
        String rawText = payload.get("rawSchemeText");
        String targetGrade = payload.get("targetGrade");
        CourseEvaluationSchemeDto dto = courseEvaluationParserService.parseAndAnalyze(id, rawText, targetGrade, student);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/evaluation-scheme/apply")
    public ResponseEntity<CourseEvaluationSchemeDto> applyEvaluationScheme(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        Student student = authenticationService.getAuthenticatedStudent();
        String rawText = payload.get("rawSchemeText");
        String targetGrade = payload.get("targetGrade");
        CourseEvaluationSchemeDto dto = courseEvaluationParserService.parseAndAnalyze(id, rawText, targetGrade, student);
        courseEvaluationParserService.persistParsedAssessments(id, dto, student);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/evaluation-scheme")
    public ResponseEntity<CourseEvaluationSchemeDto> getEvaluationScheme(@PathVariable Long id) {
        Student student = authenticationService.getAuthenticatedStudent();
        CourseEvaluationSchemeDto dto = courseEvaluationParserService.parseAndAnalyze(id, null, "A", student);
        return ResponseEntity.ok(dto);
    }

    /**
     * Returns the semester lab assignments matching the Thapar LMS Course Page (Image 3)
     */
    @GetMapping("/thapar/labs")
    public ResponseEntity<List<LabAssignmentDto>> getThaparLabAssignments() {
        List<LabAssignmentDto> labs = new ArrayList<>();
        LocalDate baseDate = LocalDate.now();

        labs.add(new LabAssignmentDto(1L, 1L, "UCS553", "Assignment 1", "Spring Boot Initializer & MVC Controller Setup", "Create enterprise web application with REST endpoints and Thymeleaf", baseDate.minusDays(20), true));
        labs.add(new LabAssignmentDto(2L, 1L, "UCS553", "Assignment 2", "Spring Dependency Injection & Bean Configuration", "Implement Service layer, Component scanning, and custom qualifiers", baseDate.minusDays(15), true));
        labs.add(new LabAssignmentDto(3L, 1L, "UCS553", "Assignment 3", "JPA & Hibernate Entity Relationships", "Design One-to-Many and Many-to-Many entities with foreign key constraints", baseDate.minusDays(10), true));
        labs.add(new LabAssignmentDto(4L, 1L, "UCS553", "Assignment 4", "Spring Data Repositories & JPQL Queries", "Write custom JPQL queries, pagination, and sorting criteria", baseDate.minusDays(4), true));
        labs.add(new LabAssignmentDto(5L, 1L, "UCS553", "Assignment 4.1", "Form Validation & Exception Handling", "Implement Bean Validation (@Valid, @NotNull) and GlobalExceptionHandler", baseDate.minusDays(1), true));
        labs.add(new LabAssignmentDto(6L, 1L, "UCS553", "Assignment 5", "Spring Security Authentication & BCrypt Hashing", "Configure SecurityFilterChain, UserDetailsService, and role-based access", baseDate.plusDays(3), false));
        labs.add(new LabAssignmentDto(7L, 1L, "UCS553", "Assignment 6", "JWT Stateless Token Authentication Filter", "Generate, sign, and validate JSON Web Tokens for secure REST APIs", baseDate.plusDays(8), false));
        labs.add(new LabAssignmentDto(8L, 1L, "UCS553", "Assignment 7", "REST API Unit & Integration Testing", "Write JUnit 5 and Mockito test suites with MockMvc assertions", baseDate.plusDays(14), false));
        labs.add(new LabAssignmentDto(9L, 1L, "UCS553", "Assignment 8", "Docker Containerization & Production Build", "Package multi-stage Docker container with production properties", baseDate.plusDays(21), false));

        return ResponseEntity.ok(labs);
    }
}

