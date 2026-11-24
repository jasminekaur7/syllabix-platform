package com.syllabix.repository;

import com.syllabix.entity.AssessmentTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentTopicRepository extends JpaRepository<AssessmentTopic, AssessmentTopic.AssessmentTopicId> {
    List<AssessmentTopic> findByAssessmentId(Long assessmentId);
    List<AssessmentTopic> findByTopicId(Long topicId);
    void deleteByAssessmentId(Long assessmentId);
}
