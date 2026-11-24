package com.syllabix.dto;

import java.time.LocalDate;

public class LabAssignmentDto {

    private Long id;
    private Long subjectId;
    private String subjectCode;
    private String assignmentNumber; // e.g. "Assignment 1", "Assignment 4.1"
    private String title;            // e.g. "Data Exploration & Summary Statistics in R"
    private String description;
    private LocalDate dueDate;
    private Integer daysRemaining;
    private Boolean isCompleted = false;
    private Double maxMarks = 10.0;
    private Double marksObtained;

    public LabAssignmentDto() {}

    public LabAssignmentDto(Long id, Long subjectId, String subjectCode, String assignmentNumber, String title, String description, LocalDate dueDate, Boolean isCompleted) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.assignmentNumber = assignmentNumber;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getAssignmentNumber() { return assignmentNumber; }
    public void setAssignmentNumber(String assignmentNumber) { this.assignmentNumber = assignmentNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Integer getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(Integer daysRemaining) { this.daysRemaining = daysRemaining; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean completed) { isCompleted = completed; }

    public Double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Double maxMarks) { this.maxMarks = maxMarks; }

    public Double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Double marksObtained) { this.marksObtained = marksObtained; }
}
