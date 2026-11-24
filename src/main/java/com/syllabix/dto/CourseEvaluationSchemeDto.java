package com.syllabix.dto;

import java.util.ArrayList;
import java.util.List;

public class CourseEvaluationSchemeDto {

    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String rawSchemeText;
    private List<EvaluationComponentDto> components = new ArrayList<>();
    private Double totalMarks = 100.0;
    private Double scoredMarksSoFar = 0.0;
    private Double completedComponentsMax = 0.0;
    private Double remainingMarks = 100.0;

    private String targetGrade = "A";
    private Double targetGradeCutoffPercentage = 80.0;
    private Double neededMarksOnRemaining = 0.0;
    private Double requiredRunRatePercentage = 0.0;
    private String feasibilityStatus = "ACHIEVABLE"; // ACHIEVABLE, STRETCH, CRITICAL_EFFORT, IMPOSSIBLE
    private String personalizedAdvice;
    private List<String> breakdownAdvice = new ArrayList<>();

    public CourseEvaluationSchemeDto() {}

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getRawSchemeText() { return rawSchemeText; }
    public void setRawSchemeText(String rawSchemeText) { this.rawSchemeText = rawSchemeText; }

    public List<EvaluationComponentDto> getComponents() { return components; }
    public void setComponents(List<EvaluationComponentDto> components) { this.components = components; }

    public Double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Double totalMarks) { this.totalMarks = totalMarks; }

    public Double getScoredMarksSoFar() { return scoredMarksSoFar; }
    public void setScoredMarksSoFar(Double scoredMarksSoFar) { this.scoredMarksSoFar = scoredMarksSoFar; }

    public Double getCompletedComponentsMax() { return completedComponentsMax; }
    public void setCompletedComponentsMax(Double completedComponentsMax) { this.completedComponentsMax = completedComponentsMax; }

    public Double getRemainingMarks() { return remainingMarks; }
    public void setRemainingMarks(Double remainingMarks) { this.remainingMarks = remainingMarks; }

    public String getTargetGrade() { return targetGrade; }
    public void setTargetGrade(String targetGrade) { this.targetGrade = targetGrade; }

    public Double getTargetGradeCutoffPercentage() { return targetGradeCutoffPercentage; }
    public void setTargetGradeCutoffPercentage(Double targetGradeCutoffPercentage) { this.targetGradeCutoffPercentage = targetGradeCutoffPercentage; }

    public Double getNeededMarksOnRemaining() { return neededMarksOnRemaining; }
    public void setNeededMarksOnRemaining(Double neededMarksOnRemaining) { this.neededMarksOnRemaining = neededMarksOnRemaining; }

    public Double getRequiredRunRatePercentage() { return requiredRunRatePercentage; }
    public void setRequiredRunRatePercentage(Double requiredRunRatePercentage) { this.requiredRunRatePercentage = requiredRunRatePercentage; }

    public String getFeasibilityStatus() { return feasibilityStatus; }
    public void setFeasibilityStatus(String feasibilityStatus) { this.feasibilityStatus = feasibilityStatus; }

    public String getPersonalizedAdvice() { return personalizedAdvice; }
    public void setPersonalizedAdvice(String personalizedAdvice) { this.personalizedAdvice = personalizedAdvice; }

    public List<String> getBreakdownAdvice() { return breakdownAdvice; }
    public void setBreakdownAdvice(List<String> breakdownAdvice) { this.breakdownAdvice = breakdownAdvice; }
}
