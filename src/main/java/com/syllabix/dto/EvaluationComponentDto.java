package com.syllabix.dto;

public class EvaluationComponentDto {

    private String title;               // e.g. "LAB Evaluation", "Sessional Quiz", "MST", "EST"
    private String componentType;       // "LAB", "QUIZ", "MST", "EST", "ASSIGNMENT"
    private Double maxMarks;            // e.g. 20.0, 10.0, 30.0, 40.0
    private Double weightagePercentage;  // e.g. 20.0, 10.0, 30.0, 40.0
    private String scheduleDetails;     // e.g. "Week 13, Dec., 2026 in scheduled LABs"
    private String syllabusCoverage;    // e.g. "same as MST", "upto EST"
    private Double obtainedMarks;       // null if not yet scored
    private Boolean isCompleted = false;

    public EvaluationComponentDto() {}

    public EvaluationComponentDto(String title, String componentType, Double maxMarks, Double weightagePercentage, String scheduleDetails, String syllabusCoverage) {
        this.title = title;
        this.componentType = componentType;
        this.maxMarks = maxMarks;
        this.weightagePercentage = weightagePercentage;
        this.scheduleDetails = scheduleDetails;
        this.syllabusCoverage = syllabusCoverage;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }

    public Double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Double maxMarks) { this.maxMarks = maxMarks; }

    public Double getWeightagePercentage() { return weightagePercentage; }
    public void setWeightagePercentage(Double weightagePercentage) { this.weightagePercentage = weightagePercentage; }

    public String getScheduleDetails() { return scheduleDetails; }
    public void setScheduleDetails(String scheduleDetails) { this.scheduleDetails = scheduleDetails; }

    public String getSyllabusCoverage() { return syllabusCoverage; }
    public void setSyllabusCoverage(String syllabusCoverage) { this.syllabusCoverage = syllabusCoverage; }

    public Double getObtainedMarks() { return obtainedMarks; }
    public void setObtainedMarks(Double obtainedMarks) { this.obtainedMarks = obtainedMarks; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean completed) { isCompleted = completed; }
}
