package com.syllabix.dto;

public class WhatIfResultDto {
    private Double currentOverallAveragePercentage;
    private Double projectedOverallAveragePercentage;
    private Double percentageDelta;

    private Double currentSubjectAveragePercentage;
    private Double projectedSubjectAveragePercentage;
    private Double subjectPercentageDelta;

    private Double currentSyllabusCompletionPercentage;
    private Double projectedSyllabusCompletionPercentage;

    private String currentEstimatedCgpa;
    private String projectedEstimatedCgpa;

    private String scenarioSummary;
    private String impactAnalysis;

    public WhatIfResultDto() {}

    public Double getCurrentOverallAveragePercentage() { return currentOverallAveragePercentage; }
    public void setCurrentOverallAveragePercentage(Double currentOverallAveragePercentage) { this.currentOverallAveragePercentage = currentOverallAveragePercentage; }

    public Double getProjectedOverallAveragePercentage() { return projectedOverallAveragePercentage; }
    public void setProjectedOverallAveragePercentage(Double projectedOverallAveragePercentage) { this.projectedOverallAveragePercentage = projectedOverallAveragePercentage; }

    public Double getPercentageDelta() { return percentageDelta; }
    public void setPercentageDelta(Double percentageDelta) { this.percentageDelta = percentageDelta; }

    public Double getCurrentSubjectAveragePercentage() { return currentSubjectAveragePercentage; }
    public void setCurrentSubjectAveragePercentage(Double currentSubjectAveragePercentage) { this.currentSubjectAveragePercentage = currentSubjectAveragePercentage; }

    public Double getProjectedSubjectAveragePercentage() { return projectedSubjectAveragePercentage; }
    public void setProjectedSubjectAveragePercentage(Double projectedSubjectAveragePercentage) { this.projectedSubjectAveragePercentage = projectedSubjectAveragePercentage; }

    public Double getSubjectPercentageDelta() { return subjectPercentageDelta; }
    public void setSubjectPercentageDelta(Double subjectPercentageDelta) { this.subjectPercentageDelta = subjectPercentageDelta; }

    public Double getCurrentSyllabusCompletionPercentage() { return currentSyllabusCompletionPercentage; }
    public void setCurrentSyllabusCompletionPercentage(Double currentSyllabusCompletionPercentage) { this.currentSyllabusCompletionPercentage = currentSyllabusCompletionPercentage; }

    public Double getProjectedSyllabusCompletionPercentage() { return projectedSyllabusCompletionPercentage; }
    public void setProjectedSyllabusCompletionPercentage(Double projectedSyllabusCompletionPercentage) { this.projectedSyllabusCompletionPercentage = projectedSyllabusCompletionPercentage; }

    public String getCurrentEstimatedCgpa() { return currentEstimatedCgpa; }
    public void setCurrentEstimatedCgpa(String currentEstimatedCgpa) { this.currentEstimatedCgpa = currentEstimatedCgpa; }

    public String getProjectedEstimatedCgpa() { return projectedEstimatedCgpa; }
    public void setProjectedEstimatedCgpa(String projectedEstimatedCgpa) { this.projectedEstimatedCgpa = projectedEstimatedCgpa; }

    public String getScenarioSummary() { return scenarioSummary; }
    public void setScenarioSummary(String scenarioSummary) { this.scenarioSummary = scenarioSummary; }

    public String getImpactAnalysis() { return impactAnalysis; }
    public void setImpactAnalysis(String impactAnalysis) { this.impactAnalysis = impactAnalysis; }
}
