package com.syllabix.repository;

import com.syllabix.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
    List<StudyPlan> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    Optional<StudyPlan> findFirstByStudentIdAndStatusOrderByCreatedAtDesc(Long studentId, String status);
    Optional<StudyPlan> findByIdAndStudentId(Long id, Long studentId);
}
