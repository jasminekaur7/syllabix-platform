package com.syllabix.repository;

import com.syllabix.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByStudentId(Long studentId);
    List<Topic> findBySubjectId(Long subjectId);
    List<Topic> findByUnitId(Long unitId);
    Optional<Topic> findByIdAndStudentId(Long id, Long studentId);

    List<Topic> findByStudentIdAndCompletionStatus(Long studentId, String completionStatus);

    @Query("SELECT t FROM Topic t WHERE t.student.id = :studentId AND t.completionStatus <> 'COMPLETED' ORDER BY t.priorityScore DESC")
    List<Topic> findPendingTopicsByStudentOrderByPriority(@Param("studentId") Long studentId);

    @Query("SELECT t FROM Topic t WHERE t.subject.id = :subjectId AND t.completionStatus <> 'COMPLETED' ORDER BY t.priorityScore DESC")
    List<Topic> findPendingTopicsBySubjectOrderByPriority(@Param("subjectId") Long subjectId);

    long countBySubjectId(Long subjectId);
    long countBySubjectIdAndCompletionStatus(Long subjectId, String completionStatus);
    long countByStudentIdAndCompletionStatus(Long studentId, String completionStatus);
    long countByStudentId(Long studentId);

    @Query("SELECT t FROM Topic t WHERE t.student.id = :studentId AND LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Topic> searchTopics(@Param("studentId") Long studentId, @Param("query") String query);
}
