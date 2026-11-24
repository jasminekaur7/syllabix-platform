package com.syllabix.service;

import com.syllabix.dto.ExamReadinessDto;
import com.syllabix.dto.WhatIfRequestDto;
import com.syllabix.dto.WhatIfResultDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.entity.Unit;
import com.syllabix.repository.AcademicInsightRepository;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcademicInsightsServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private MarksRepository marksRepository;

    @Mock
    private AcademicInsightRepository academicInsightRepository;

    @Mock
    private MarksService marksService;

    @InjectMocks
    private AcademicInsightsService academicInsightsService;

    private Student student;
    private Subject subject;
    private Unit unit;
    private Assessment assessment;
    private Topic topic1;
    private Topic topic2;

    @BeforeEach
    void setUp() {
        student = new Student("Alex Mercer", "alex@test.edu", "pass", "NIT", "B.Tech", "CSE", 5);
        student.setId(1L);
        student.setDailyStudyHours(3.0);

        subject = new Subject(student, "Computer Networks", "CS503", 5, 3, "CN", "Dr. Elena", "A", "#10b981");
        subject.setId(40L);

        unit = new Unit(subject, 1, "Transport Layer", "TCP");
        unit.setId(400L);

        topic1 = new Topic(unit, subject, student, "TCP Congestion Control", "State machine", "HARD", 60, "NOT_STARTED", 2);
        topic1.setId(4001L);

        topic2 = new Topic(unit, subject, student, "Subnetting", "CIDR", "MEDIUM", 45, "COMPLETED", 4);
        topic2.setId(4002L);

        assessment = new Assessment(student, subject, "CN Mid-Term", "MST", LocalDateTime.now().plusDays(6), "Units 1-2", 50.0, "PREPARING", "HIGH", "");
        assessment.setId(40001L);
    }

    @Test
    @DisplayName("Verify Exam Readiness Score 5-factor composite computation")
    void testCalculateExamReadinessScores() {
        when(assessmentRepository.findUpcomingAssessments(any(), any())).thenReturn(Collections.singletonList(assessment));
        when(topicRepository.countBySubjectId(40L)).thenReturn(2L);
        when(topicRepository.countBySubjectIdAndCompletionStatus(40L, "COMPLETED")).thenReturn(1L); // 50% syllabus
        when(topicRepository.findBySubjectId(40L)).thenReturn(Arrays.asList(topic1, topic2));
        when(marksRepository.calculateSubjectAveragePercentage(40L)).thenReturn(75.0);

        List<ExamReadinessDto> readinessList = academicInsightsService.calculateExamReadinessScores(student);

        assertNotNull(readinessList);
        assertEquals(1, readinessList.size());

        ExamReadinessDto dto = readinessList.get(0);
        assertEquals("CS503", dto.getSubjectCode());
        assertEquals(50.0, dto.getSyllabusCompletionRate());
        assertEquals(75.0, dto.getPreviousPerformanceScore());
        assertTrue(dto.getReadinessScore() > 0 && dto.getReadinessScore() <= 100);
        assertNotNull(dto.getReadinessGrade());
        assertNotNull(dto.getExplanation());
    }

    @Test
    @DisplayName("Verify What-If Academic Scenario Simulator forecast")
    void testSimulateAcademicScenario() {
        when(marksRepository.calculateOverallAveragePercentage(1L)).thenReturn(72.0);
        when(subjectRepository.findByIdAndStudentId(40L, 1L)).thenReturn(Optional.of(subject));
        when(marksRepository.calculateSubjectAveragePercentage(40L)).thenReturn(70.0);
        when(topicRepository.countByStudentId(1L)).thenReturn(20L);
        when(topicRepository.countByStudentIdAndCompletionStatus(1L, "COMPLETED")).thenReturn(10L); // 50%
        when(marksRepository.findByStudentIdAndSubjectId(1L, 40L)).thenReturn(Collections.emptyList());
        when(marksRepository.findByStudentId(1L)).thenReturn(Collections.emptyList());
        when(marksService.calculateEstimatedCgpa(any())).thenReturn("8.20 / 10.0");

        WhatIfRequestDto req = new WhatIfRequestDto();
        req.setTargetSubjectId(40L);
        req.setHypotheticalAssessmentScore(90.0);
        req.setHypotheticalAssessmentMaxMarks(100.0);
        req.setAdditionalTopicsCompleted(4);
        req.setAdditionalDailyStudyHours(1.5);

        WhatIfResultDto result = academicInsightsService.simulateAcademicScenario(student, req);

        assertNotNull(result);
        assertEquals(72.0, result.getCurrentOverallAveragePercentage());
        assertEquals(70.0, result.getCurrentSubjectAveragePercentage());
        assertEquals(90.0, result.getProjectedSubjectAveragePercentage());
        assertEquals(20.0, result.getSubjectPercentageDelta()); // 90 - 70 = +20%
        assertEquals(70.0, result.getProjectedSyllabusCompletionPercentage()); // 14 / 20 = 70%
        assertNotNull(result.getScenarioSummary());
    }
}
