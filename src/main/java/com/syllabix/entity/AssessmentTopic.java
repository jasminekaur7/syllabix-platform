package com.syllabix.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "assessment_topics")
public class AssessmentTopic implements Serializable {

    @EmbeddedId
    private AssessmentTopicId id = new AssessmentTopicId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("assessmentId")
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public AssessmentTopic() {}

    public AssessmentTopic(Assessment assessment, Topic topic) {
        this.assessment = assessment;
        this.topic = topic;
        this.id = new AssessmentTopicId(assessment.getId(), topic.getId());
    }

    public AssessmentTopicId getId() { return id; }
    public void setId(AssessmentTopicId id) { this.id = id; }

    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    @Embeddable
    public static class AssessmentTopicId implements Serializable {
        private Long assessmentId;
        private Long topicId;

        public AssessmentTopicId() {}
        public AssessmentTopicId(Long assessmentId, Long topicId) {
            this.assessmentId = assessmentId;
            this.topicId = topicId;
        }

        public Long getAssessmentId() { return assessmentId; }
        public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

        public Long getTopicId() { return topicId; }
        public void setTopicId(Long topicId) { this.topicId = topicId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssessmentTopicId that = (AssessmentTopicId) o;
            return Objects.equals(assessmentId, that.assessmentId) && Objects.equals(topicId, that.topicId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(assessmentId, topicId);
        }
    }
}
