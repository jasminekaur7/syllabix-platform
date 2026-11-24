package com.syllabix.repository;

import com.syllabix.entity.AcademicInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicInsightRepository extends JpaRepository<AcademicInsight, Long> {
    List<AcademicInsight> findByStudentIdAndIsDismissedFalseOrderByGeneratedAtDesc(Long studentId);
    List<AcademicInsight> findByStudentIdOrderByGeneratedAtDesc(Long studentId);
    Optional<AcademicInsight> findByIdAndStudentId(Long id, Long studentId);
    void deleteByStudentId(Long studentId);
}
