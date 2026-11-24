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

        labs.add(new LabAssignmentDto(1L, 1L, "UCS501", "Assignment 1", "SQL DDL & Relational Schema Definition", "Create university database schema with foreign keys and cascade rules", baseDate.minusDays(20), true));
        labs.add(new LabAssignmentDto(2L, 1L, "UCS501", "Assignment 2", "Complex SQL Queries & Joins", "Execute multi-table joins, subqueries, and aggregate grouping", baseDate.minusDays(15), true));
        labs.add(new LabAssignmentDto(3L, 1L, "UCS501", "Assignment 3", "Nested Queries & Views", "Create parameterized views and correlated subqueries", baseDate.minusDays(10), true));
        labs.add(new LabAssignmentDto(4L, 1L, "UCS501", "Assignment 4", "Normalization: 1NF, 2NF, 3NF & BCNF", "Decompose unnormalized relation into BCNF lossless-join forms", baseDate.minusDays(4), true));
        labs.add(new LabAssignmentDto(5L, 1L, "UCS501", "Assignment 4.1", "Functional Dependency Closure Verifier", "Implement algorithm to find canonical cover and candidate keys", baseDate.minusDays(1), true));
        labs.add(new LabAssignmentDto(6L, 1L, "UCS501", "Assignment 5", "Transactions & ACID Properties Simulation", "Demonstrate dirty read, non-repeatable read, and phantom read anomalies", baseDate.plusDays(3), false));
        labs.add(new LabAssignmentDto(7L, 1L, "UCS501", "Assignment 6", "Strict Two-Phase Locking & Precedence Graphs", "Simulate 2PL concurrency manager and cycle detection", baseDate.plusDays(8), false));
        labs.add(new LabAssignmentDto(8L, 1L, "UCS501", "Assignment 7", "B+ Tree Indexing & Search Evaluation", "Measure query execution time with clustered vs non-clustered index", baseDate.plusDays(14), false));
        labs.add(new LabAssignmentDto(9L, 1L, "UCS501", "Assignment 8", "Database Backup, Recovery & ARIES Logging", "Demonstrate checkpointing and undo/redo log replay simulation", baseDate.plusDays(21), false));

        return ResponseEntity.ok(labs);
    }
}

