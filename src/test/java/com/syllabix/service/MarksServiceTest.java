package com.syllabix.service;

import com.syllabix.dto.MarksDto;
import com.syllabix.entity.Marks;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarksServiceTest {

    @Mock
    private MarksRepository marksRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @InjectMocks
    private MarksService marksService;

    private Student student;
    private Subject subject;

    @BeforeEach
    void setUp() {
        student = new Student("Alex Mercer", "alex@test.edu", "pass", "NIT", "B.Tech", "CSE", 5);
        student.setId(1L);

        subject = new Subject(student, "Algorithms", "CS504", 5, 4, "DAA", "Dr. Ramesh", "A+", "#f59e0b");
        subject.setId(30L);
    }

    @Test
    @DisplayName("Verify Percentage Calculation and Letter Grade assignment")
    void testRecordMarksAndGradeCalculation() {
        when(subjectRepository.findByIdAndStudentId(30L, 1L)).thenReturn(Optional.of(subject));

        MarksDto input = new MarksDto();
        input.setSubjectId(30L);
        input.setMarksObtained(42.5);
        input.setMaxMarks(50.0);
        input.setAssessmentDate(LocalDate.now());
        input.setAssessmentType("MST");

        when(marksRepository.save(any(Marks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MarksDto recorded = marksService.recordMarks(input, student);

        assertNotNull(recorded);
        assertEquals(85.0, recorded.getPercentage());
        assertEquals("A+ (Excellent)", recorded.getGrade());
    }

    @Test
    @DisplayName("Verify 10-Point CGPA Conversion Formula")
    void testCgpaCalculation() {
        assertEquals("9.00 / 10.0", marksService.calculateEstimatedCgpa(85.5));
        assertEquals("10.00 / 10.0", marksService.calculateEstimatedCgpa(98.0));
        assertEquals("7.37 / 10.0", marksService.calculateEstimatedCgpa(70.0));
        assertEquals("N/A", marksService.calculateEstimatedCgpa(null));
    }
}
