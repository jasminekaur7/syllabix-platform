package com.syllabix.dto;

public class WhatIfRequestDto {
    private Long targetSubjectId;
    private Double hypotheticalAssessmentScore; // e.g. 85.0
    private Double hypotheticalAssessmentMaxMarks; // e.g. 100.0
    private Integer additionalTopicsCompleted; // e.g. 3
    private Double additionalDailyStudyHours; // e.g. 2.0

    public WhatIfRequestDto() {}

    public Long getTargetSubjectId() { return targetSubjectId; }
    public void setTargetSubjectId(Long targetSubjectId) { this.targetSubjectId = targetSubjectId; }

    public Double getHypotheticalAssessmentScore() { return hypotheticalAssessmentScore; }
    public void setHypotheticalAssessmentScore(Double hypotheticalAssessmentScore) { this.hypotheticalAssessmentScore = hypotheticalAssessmentScore; }

    public Double getHypotheticalAssessmentMaxMarks() { return hypotheticalAssessmentMaxMarks; }
    public void setHypotheticalAssessmentMaxMarks(Double hypotheticalAssessmentMaxMarks) { this.hypotheticalAssessmentMaxMarks = hypotheticalAssessmentMaxMarks; }

    public Integer getAdditionalTopicsCompleted() { return additionalTopicsCompleted; }
    public void setAdditionalTopicsCompleted(Integer additionalTopicsCompleted) { this.additionalTopicsCompleted = additionalTopicsCompleted; }

    public Double getAdditionalDailyStudyHours() { return additionalDailyStudyHours; }
    public void setAdditionalDailyStudyHours(Double additionalDailyStudyHours) { this.additionalDailyStudyHours = additionalDailyStudyHours; }
}
