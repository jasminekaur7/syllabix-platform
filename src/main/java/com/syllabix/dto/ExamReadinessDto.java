package com.syllabix.dto;

import java.time.LocalDateTime;

public class ExamReadinessDto {
    private Long assessmentId;
    private String assessmentTitle;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private LocalDateTime assessmentDateTime;
    private Long daysRemaining;

    // Readiness Score (0 to 100%)
    private Double readinessScore;
    private String readinessGrade; // HIGH_READINESS, MODERATE_READINESS, AT_RISK, CRITICAL
    private String explanation;

    // 5-Factor Breakdown
    private Double syllabusCompletionRate; // %
    private Double topicDifficultyMastery; // %
    private Double previousPerformanceScore; // %
    private Double revisionCoverageRate; // %
    private Double timeSufficiencyScore; // %

    public ExamReadinessDto() {}

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public String getAssessmentTitle() { return assessmentTitle; }
    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public LocalDateTime getAssessmentDateTime() { return assessmentDateTime; }
    public void setAssessmentDateTime(LocalDateTime assessmentDateTime) { this.assessmentDateTime = assessmentDateTime; }

    public Long getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(Long daysRemaining) { this.daysRemaining = daysRemaining; }

    public Double getReadinessScore() { return readinessScore; }
    public void setReadinessScore(Double readinessScore) { this.readinessScore = readinessScore; }

    public String getReadinessGrade() { return readinessGrade; }
    public void setReadinessGrade(String readinessGrade) { this.readinessGrade = readinessGrade; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public Double getSyllabusCompletionRate() { return syllabusCompletionRate; }
    public void setSyllabusCompletionRate(Double syllabusCompletionRate) { this.syllabusCompletionRate = syllabusCompletionRate; }

    public Double getTopicDifficultyMastery() { return topicDifficultyMastery; }
    public void setTopicDifficultyMastery(Double topicDifficultyMastery) { this.topicDifficultyMastery = topicDifficultyMastery; }

    public Double getPreviousPerformanceScore() { return previousPerformanceScore; }
    public void setPreviousPerformanceScore(Double previousPerformanceScore) { this.previousPerformanceScore = previousPerformanceScore; }

    public Double getRevisionCoverageRate() { return revisionCoverageRate; }
    public void setRevisionCoverageRate(Double revisionCoverageRate) { this.revisionCoverageRate = revisionCoverageRate; }

    public Double getTimeSufficiencyScore() { return timeSufficiencyScore; }
    public void setTimeSufficiencyScore(Double timeSufficiencyScore) { this.timeSufficiencyScore = timeSufficiencyScore; }
}
