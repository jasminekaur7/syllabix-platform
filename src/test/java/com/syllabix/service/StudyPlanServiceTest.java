package com.syllabix.service;

import com.syllabix.dto.RecommendationDto;
import com.syllabix.dto.StudyPlanRequest;
import com.syllabix.dto.StudyTaskDto;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.entity.Unit;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.StudyPlanRepository;
import com.syllabix.repository.StudyTaskRepository;
import com.syllabix.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudyPlanServiceTest {

    @Mock
    private StudyPlanRepository studyPlanRepository;

    @Mock
    private StudyTaskRepository studyTaskRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private RecommendationEngineService recommendationEngineService;

    @InjectMocks
    private StudyPlanService studyPlanService;

    private Student student;
    private Subject subject;
    private Unit unit;
    private Topic topic1;
    private Topic topic2;

    @BeforeEach
    void setUp() {
        student = new Student("Alex Mercer", "alex@test.edu", "pass", "NIT", "B.Tech", "CSE", 5);
        student.setId(1L);
        student.setDailyStudyHours(2.0); // 120 minutes daily budget

        subject = new Subject(student, "Operating Systems", "CS502", 5, 4, "OS", "Prof. Alan", "A", "#0284c7");
        subject.setId(20L);

        unit = new Unit(subject, 1, "Paging", "Virtual memory");
        unit.setId(200L);

        topic1 = new Topic(unit, subject, student, "Page Replacement LRU", "Algorithm", "HARD", 50, "NOT_STARTED", 2);
        topic1.setId(2001L);

        topic2 = new Topic(unit, subject, student, "Inverted Page Tables", "Concept", "MEDIUM", 45, "NOT_STARTED", 3);
        topic2.setId(2002L);
    }

    @Test
    @DisplayName("Study Timetable Generation distributes sessions respecting declared daily hours budget")
    void testGenerateStudyPlanRespectsDailyBudget() {
        StudyPlanRequest req = new StudyPlanRequest();
        req.setStartDate(LocalDate.now());
        req.setEndDate(LocalDate.now().plusDays(3));
        req.setDailyStudyHours(2.0);

        RecommendationDto r1 = new RecommendationDto();
        r1.setTopicId(2001L);
        r1.setSubjectId(20L);
        r1.setPriorityScore(85.0);
        r1.setPriorityLabel("URGENT");
        r1.setReason("OS test approaching");

        RecommendationDto r2 = new RecommendationDto();
        r2.setTopicId(2002L);
        r2.setSubjectId(20L);
        r2.setPriorityScore(72.0);
        r2.setPriorityLabel("HIGH");
        r2.setReason("Important memory topic");

        when(recommendationEngineService.generateRecommendations(any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(r1, r2));

        when(topicRepository.findById(2001L)).thenReturn(Optional.of(topic1));
        when(topicRepository.findById(2002L)).thenReturn(Optional.of(topic2));
        when(assessmentRepository.findNextUpcomingAssessmentForSubject(eq(20L), any())).thenReturn(Collections.emptyList());

        List<StudyTaskDto> generated = studyPlanService.generateStudyPlan(student, req);

        assertNotNull(generated);
        assertEquals(2, generated.size());

        // Verify total minutes scheduled does not exceed 120 mins on day 1
        int day1Minutes = generated.stream()
                .filter(t -> t.getPlannedDate().equals(LocalDate.now()))
                .mapToInt(StudyTaskDto::getDurationMinutes)
                .sum();

        assertTrue(day1Minutes <= 120, "Day 1 scheduled minutes must not exceed daily budget of 120 mins");
        verify(studyTaskRepository, times(1)).saveAll(anyList());
        verify(studyPlanRepository, times(1)).save(any());
    }
}
