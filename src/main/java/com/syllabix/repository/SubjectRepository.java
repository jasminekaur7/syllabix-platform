package com.syllabix.repository;

import com.syllabix.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByStudentId(Long studentId);
    List<Subject> findByStudentIdOrderBySemesterAscNameAsc(Long studentId);
    Optional<Subject> findByIdAndStudentId(Long id, Long studentId);
    boolean existsByStudentIdAndCode(Long studentId, String code);
    long countByStudentId(Long studentId);
}
