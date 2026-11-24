package com.syllabix.repository;

import com.syllabix.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudentId(Long studentId);
    List<Marks> findByStudentIdOrderByAssessmentDateDesc(Long studentId);
    List<Marks> findBySubjectId(Long subjectId);
    List<Marks> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    Optional<Marks> findByIdAndStudentId(Long id, Long studentId);

    @Query("SELECT AVG((m.marksObtained / m.maxMarks) * 100.0) FROM Marks m WHERE m.student.id = :studentId")
    Double calculateOverallAveragePercentage(@Param("studentId") Long studentId);

    @Query("SELECT AVG((m.marksObtained / m.maxMarks) * 100.0) FROM Marks m WHERE m.subject.id = :subjectId")
    Double calculateSubjectAveragePercentage(@Param("subjectId") Long subjectId);

    long countByStudentId(Long studentId);
}
