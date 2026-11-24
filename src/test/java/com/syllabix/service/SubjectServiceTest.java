package com.syllabix.service;

import com.syllabix.dto.SubjectDto;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.exception.ValidationException;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import com.syllabix.repository.UnitRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private MarksRepository marksRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Alex Mercer", "alex@test.edu", "pass", "NIT", "B.Tech", "CSE", 5);
        student.setId(1L);
    }

    @Test
    @DisplayName("Verify duplicate subject code for the same student is rejected")
    void testCreateSubjectDuplicateCodeThrowsException() {
        when(subjectRepository.existsByStudentIdAndCode(1L, "CS501")).thenReturn(true);

        SubjectDto dto = new SubjectDto();
        dto.setCode("CS501");
        dto.setName("Database Systems");
        dto.setSemester(5);

        assertThrows(ValidationException.class, () -> subjectService.createSubject(dto, student));
    }

    @Test
    @DisplayName("Verify Subject syllabus completion percentage calculation")
    void testSubjectSyllabusCompletionCalculation() {
        Subject sub = new Subject(student, "Computer Networks", "CS503", 5, 3, "CN", "Prof", "A", "#10b981");
        sub.setId(50L);

        when(topicRepository.countBySubjectId(50L)).thenReturn(8L);
        when(topicRepository.countBySubjectIdAndCompletionStatus(50L, "COMPLETED")).thenReturn(6L); // 6 / 8 = 75.0%
        when(marksRepository.calculateSubjectAveragePercentage(50L)).thenReturn(82.5);
        when(assessmentRepository.findNextUpcomingAssessmentForSubject(any(), any())).thenReturn(Collections.emptyList());

        SubjectDto enriched = subjectService.enrichSubjectDto(sub);

        assertNotNull(enriched);
        assertEquals(75.0, enriched.getCompletionPercentage());
        assertEquals(82.5, enriched.getAverageMarksPercentage());
    }
}
