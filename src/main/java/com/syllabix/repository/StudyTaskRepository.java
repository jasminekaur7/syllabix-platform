package com.syllabix.repository;

import com.syllabix.entity.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
    List<StudyTask> findByStudentId(Long studentId);
    List<StudyTask> findByStudentIdAndPlannedDate(Long studentId, LocalDate plannedDate);
    List<StudyTask> findByStudentIdAndPlannedDateBetweenOrderByPlannedDateAscStartTimeAsc(Long studentId, LocalDate startDate, LocalDate endDate);
    List<StudyTask> findByStudentIdAndIsCompletedFalseOrderByPlannedDateAscPriorityScoreDesc(Long studentId);
    Optional<StudyTask> findByIdAndStudentId(Long id, Long studentId);

    @Query("SELECT COUNT(t) FROM StudyTask t WHERE t.student.id = :studentId AND t.isCompleted = true AND t.plannedDate BETWEEN :startDate AND :endDate")
    long countCompletedTasksBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(t) FROM StudyTask t WHERE t.student.id = :studentId AND t.plannedDate BETWEEN :startDate AND :endDate")
    long countTotalTasksBetween(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    void deleteByStudentIdAndPlannedDateGreaterThanEqual(Long studentId, LocalDate fromDate);
}
