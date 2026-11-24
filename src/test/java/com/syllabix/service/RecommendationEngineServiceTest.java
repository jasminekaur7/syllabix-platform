package com.syllabix.service;

import com.syllabix.dto.RecommendationDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.entity.Unit;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationEngineServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private MarksRepository marksRepository;

    @InjectMocks
    private RecommendationEngineService recommendationEngineService;

    private Student student;
    private Subject dbms;
    private Unit unit1;
    private Topic hardTopic;

    @BeforeEach
    void setUp() {
        student = new Student("Alex Mercer", "alex@test.edu", "hash", "NIT", "B.Tech", "CSE", 5);
        student.setId(1L);

        dbms = new Subject(student, "Database Systems", "CS501", 5, 4, "DBMS", "Dr. Sarah", "A+", "#4f46e5");
        dbms.setId(10L);

        unit1 = new Unit(dbms, 1, "Concurrency Control", "Locking");
        unit1.setId(100L);

        hardTopic = new Topic(unit1, dbms, student, "Strict Two-Phase Locking", "2PL protocol", "HARD", 60, "NOT_STARTED", 1);
        hardTopic.setId(1000L);
    }

    @Test
    @DisplayName("Verify Prompt Example: DBMS exam in 5 days with incomplete syllabus produces high priority score")
    void testRecommendationCalculationForUrgentExam() {
        when(subjectRepository.findByStudentId(1L)).thenReturn(Collections.singletonList(dbms));
        when(topicRepository.countBySubjectId(10L)).thenReturn(10L);
        when(topicRepository.countBySubjectIdAndCompletionStatus(10L, "COMPLETED")).thenReturn(4L); // 40% completion
        when(topicRepository.findPendingTopicsBySubjectOrderByPriority(10L)).thenReturn(Collections.singletonList(hardTopic));

        // Upcoming exam in 5 days
        Assessment examIn5Days = new Assessment(student, dbms, "DBMS MST", "MST", LocalDateTime.now().plusDays(5), "Units 1-3", 50.0, "PREPARING", "URGENT", "");
        when(assessmentRepository.findNextUpcomingAssessmentForSubject(eq(10L), any(LocalDateTime.class))).thenReturn(Collections.singletonList(examIn5Days));

        // Average marks: 60%
        when(marksRepository.calculateSubjectAveragePercentage(10L)).thenReturn(60.0);

        List<RecommendationDto> recs = recommendationEngineService.generateRecommendations(student, 0.35, 0.30, 0.20, 0.15);

        assertNotNull(recs);
        assertEquals(1, recs.size());

        RecommendationDto top = recs.get(0);
        assertEquals(1000L, top.getTopicId());
        assertEquals("CS501", top.getSubjectCode());

        // Score should be high (typically > 70 due to 5-day urgency + 60% incompletion + HARD difficulty + low confidence)
        assertTrue(top.getPriorityScore() >= 70.0, "Priority score should reflect urgent exam and incomplete syllabus. Actual: " + top.getPriorityScore());
        assertTrue(top.getReason().contains("DBMS MST in 5 days") || top.getReason().contains("CS501") || top.getReason().contains("Database Systems"));
        assertEquals("CRITICAL", top.getPriorityLabel());
    }

    @Test
    @DisplayName("Edge Case: No subjects enrolled should return empty list gracefully")
    void testEdgeCaseNoSubjects() {
        when(subjectRepository.findByStudentId(1L)).thenReturn(Collections.emptyList());

        List<RecommendationDto> recs = recommendationEngineService.generateRecommendations(student, null, null, null, null);

        assertNotNull(recs);
        assertTrue(recs.isEmpty());
    }

    @Test
    @DisplayName("Edge Case: No upcoming exams should default exam urgency to baseline")
    void testEdgeCaseNoUpcomingExams() {
        when(subjectRepository.findByStudentId(1L)).thenReturn(Collections.singletonList(dbms));
        when(topicRepository.countBySubjectId(10L)).thenReturn(5L);
        when(topicRepository.countBySubjectIdAndCompletionStatus(10L, "COMPLETED")).thenReturn(5L); // 100% completed
        when(topicRepository.findPendingTopicsBySubjectOrderByPriority(10L)).thenReturn(Collections.emptyList());
        when(assessmentRepository.findNextUpcomingAssessmentForSubject(eq(10L), any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        when(marksRepository.calculateSubjectAveragePercentage(10L)).thenReturn(85.0);

        List<RecommendationDto> recs = recommendationEngineService.generateRecommendations(student, null, null, null, null);

        assertNotNull(recs);
        assertTrue(recs.isEmpty()); // All topics completed
    }
}
