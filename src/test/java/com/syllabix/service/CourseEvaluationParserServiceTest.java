package com.syllabix.service;

import com.syllabix.dto.CourseEvaluationSchemeDto;
import com.syllabix.dto.EvaluationComponentDto;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseEvaluationParserServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private MarksRepository marksRepository;

    @InjectMocks
    private CourseEvaluationParserService evaluationParserService;

    private Student student;
    private Subject subject;

    @BeforeEach
    void setUp() {
        student = new Student("Jasmine Kaur", "jkaur_be24@thapar.edu", "pass", "TIET", "B.E.", "COE", 5);
        student.setId(1L);

        subject = new Subject(student, "Database Management Systems", "UCS501", 5, 4, "DBMS", "Dr. Sarah", "A", "#4f46e5");
        subject.setId(10L);
    }

    @Test
    @DisplayName("Parse exact Thapar LMS Course Evaluation text from Image 3")
    void testParseThaparLmsCourseEvaluationSnippet() {
        String snippet = "The Marks breakup (tentative) of the course is as follows:\n" +
                "• LAB Evaluation : 20 Marks (Week 13, Dec., 2026 in scheduled LABs)\n" +
                "• Sessional Quiz : 10 Marks (Week 12, Oct 15, 2026), syllabus: same as MST\n" +
                "• Sessional Make up Quiz : 10 Marks (Week 13, Dec 4, 2026; 4:30 PM, syllabus: upto EST)\n" +
                "• MST : 30 Marks\n" +
                "• EST : 40 Marks";

        when(subjectRepository.findByIdAndStudentId(10L, 1L)).thenReturn(Optional.of(subject));
        when(marksRepository.findByStudentIdAndSubjectId(1L, 10L)).thenReturn(Collections.emptyList());

        CourseEvaluationSchemeDto dto = evaluationParserService.parseAndAnalyze(10L, snippet, "A", student);

        assertNotNull(dto);
        assertEquals(10L, dto.getSubjectId());
        assertEquals("UCS501", dto.getSubjectCode());

        // Verify extracted components: Lab (20), Quiz (10), Make up Quiz (10), MST (30), EST (40)
        assertFalse(dto.getComponents().isEmpty());

        EvaluationComponentDto lab = dto.getComponents().stream()
                .filter(c -> "LAB".equals(c.getComponentType()))
                .findFirst().orElse(null);
        assertNotNull(lab);
        assertEquals(20.0, lab.getMaxMarks());
        assertTrue(lab.getScheduleDetails().contains("Week 13"));

        EvaluationComponentDto mst = dto.getComponents().stream()
                .filter(c -> "MST".equals(c.getComponentType()))
                .findFirst().orElse(null);
        assertNotNull(mst);
        assertEquals(30.0, mst.getMaxMarks());

        EvaluationComponentDto est = dto.getComponents().stream()
                .filter(c -> "EST".equals(c.getComponentType()))
                .findFirst().orElse(null);
        assertNotNull(est);
        assertEquals(40.0, est.getMaxMarks());

        // Target Grade A requires 80% cutoff -> 80% run-rate when starting fresh (STRETCH goal)
        assertEquals(80.0, dto.getTargetGradeCutoffPercentage());
        assertEquals("STRETCH", dto.getFeasibilityStatus());
        assertNotNull(dto.getPersonalizedAdvice());
    }
}
