package com.syllabix.dto;

public class RecommendationDto {
    private Long topicId;
    private String topicName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String subjectColor;
    private String difficulty;
    private Integer estimatedDurationMinutes;
    private Double priorityScore; // 0 to 100
    private String priorityLabel; // HIGH, URGENT, CRITICAL
    private String reason; // Clear human explanation of why recommended

    // Priority breakdown factors (for algorithmic transparency)
    private Double examUrgencyFactor;
    private Double syllabusIncompletionFactor;
    private Double topicDifficultyFactor;
    private Double previousPerformanceFactor;

    public RecommendationDto() {}

    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectColor() { return subjectColor; }
    public void setSubjectColor(String subjectColor) { this.subjectColor = subjectColor; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }

    public Double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Double priorityScore) { this.priorityScore = priorityScore; }

    public String getPriorityLabel() { return priorityLabel; }
    public void setPriorityLabel(String priorityLabel) { this.priorityLabel = priorityLabel; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Double getExamUrgencyFactor() { return examUrgencyFactor; }
    public void setExamUrgencyFactor(Double examUrgencyFactor) { this.examUrgencyFactor = examUrgencyFactor; }

    public Double getSyllabusIncompletionFactor() { return syllabusIncompletionFactor; }
    public void setSyllabusIncompletionFactor(Double syllabusIncompletionFactor) { this.syllabusIncompletionFactor = syllabusIncompletionFactor; }

    public Double getTopicDifficultyFactor() { return topicDifficultyFactor; }
    public void setTopicDifficultyFactor(Double topicDifficultyFactor) { this.topicDifficultyFactor = topicDifficultyFactor; }

    public Double getPreviousPerformanceFactor() { return previousPerformanceFactor; }
    public void setPreviousPerformanceFactor(Double previousPerformanceFactor) { this.previousPerformanceFactor = previousPerformanceFactor; }
}
