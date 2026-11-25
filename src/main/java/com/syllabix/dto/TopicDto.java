package com.syllabix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TopicDto {
    private Long id;
    private Long unitId;
    private String unitTitle;
    private Integer unitNumber;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    @NotBlank(message = "Topic name is required")
    private String name;

    private String description;
    private String difficulty = "MEDIUM"; // EASY, MEDIUM, HARD
    private Integer estimatedDurationMinutes = 45;
    private String completionStatus = "NOT_STARTED"; // NOT_STARTED, IN_PROGRESS, COMPLETED
    private Integer confidenceLevel = 3; // 1 to 5
    private Double priorityScore = 0.0;
    private LocalDate lastStudiedDate;
    private String aiReasoning;

    public TopicDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }

    public String getUnitTitle() { return unitTitle; }
    public void setUnitTitle(String unitTitle) { this.unitTitle = unitTitle; }

    public Integer getUnitNumber() { return unitNumber; }
    public void setUnitNumber(Integer unitNumber) { this.unitNumber = unitNumber; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }

    public String getCompletionStatus() { return completionStatus; }
    public void setCompletionStatus(String completionStatus) { this.completionStatus = completionStatus; }

    public Integer getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(Integer confidenceLevel) { this.confidenceLevel = confidenceLevel; }

    public Double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Double priorityScore) { this.priorityScore = priorityScore; }

    public LocalDate getLastStudiedDate() { return lastStudiedDate; }
    public void setLastStudiedDate(LocalDate lastStudiedDate) { this.lastStudiedDate = lastStudiedDate; }

    public String getAiReasoning() { return aiReasoning; }
    public void setAiReasoning(String aiReasoning) { this.aiReasoning = aiReasoning; }
}
