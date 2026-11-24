package com.syllabix.repository;

import com.syllabix.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByStudentId(Long studentId);
    List<Assessment> findByStudentIdOrderByAssessmentDateTimeAsc(Long studentId);
    List<Assessment> findBySubjectId(Long subjectId);
    Optional<Assessment> findByIdAndStudentId(Long id, Long studentId);

    @Query("SELECT a FROM Assessment a WHERE a.student.id = :studentId AND a.assessmentDateTime >= :fromDateTime ORDER BY a.assessmentDateTime ASC")
    List<Assessment> findUpcomingAssessments(@Param("studentId") Long studentId, @Param("fromDateTime") LocalDateTime fromDateTime);

    @Query("SELECT a FROM Assessment a WHERE a.student.id = :studentId AND a.assessmentDateTime BETWEEN :startDate AND :endDate ORDER BY a.assessmentDateTime ASC")
    List<Assessment> findAssessmentsBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Assessment a WHERE a.subject.id = :subjectId AND a.assessmentDateTime >= :now ORDER BY a.assessmentDateTime ASC")
    List<Assessment> findNextUpcomingAssessmentForSubject(@Param("subjectId") Long subjectId, @Param("now") LocalDateTime now);

    long countByStudentIdAndAssessmentDateTimeAfter(Long studentId, LocalDateTime now);
}
