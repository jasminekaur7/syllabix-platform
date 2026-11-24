package com.syllabix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class AssessmentDto {
    private Long id;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private String subjectName;
    private String subjectCode;
    private String subjectColor;

    @NotBlank(message = "Assessment title is required")
    private String title;

    private String type = "MST"; // QUIZ, MST, EST, ASSIGNMENT, LAB, PROJECT, VIVA

    @NotNull(message = "Date and time is required")
    private LocalDateTime assessmentDateTime;

    private String syllabusCoverage;
    private Double maxMarks = 100.0;
    private String preparationStatus = "NOT_STARTED"; // NOT_STARTED, PREPARING, READY
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT
    private String notes;

    // Derived properties
    private Long daysRemaining;
    private String urgencyLevel; // URGENT (red, <=3 days), APPROACHING (orange, <=7 days), SUFFICIENT (green, >7 days), PAST (gray)
    private Double marksObtained;
    private Double marksPercentage;
    private List<Long> topicIds;

    public AssessmentDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectColor() { return subjectColor; }
    public void setSubjectColor(String subjectColor) { this.subjectColor = subjectColor; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getAssessmentDateTime() { return assessmentDateTime; }
    public void setAssessmentDateTime(LocalDateTime assessmentDateTime) { this.assessmentDateTime = assessmentDateTime; }

    public String getSyllabusCoverage() { return syllabusCoverage; }
    public void setSyllabusCoverage(String syllabusCoverage) { this.syllabusCoverage = syllabusCoverage; }

    public Double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Double maxMarks) { this.maxMarks = maxMarks; }

    public String getPreparationStatus() { return preparationStatus; }
    public void setPreparationStatus(String preparationStatus) { this.preparationStatus = preparationStatus; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(Long daysRemaining) { this.daysRemaining = daysRemaining; }

    public String getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public Double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Double marksObtained) { this.marksObtained = marksObtained; }

    public Double getMarksPercentage() { return marksPercentage; }
    public void setMarksPercentage(Double marksPercentage) { this.marksPercentage = marksPercentage; }

    public List<Long> getTopicIds() { return topicIds; }
    public void setTopicIds(List<Long> topicIds) { this.topicIds = topicIds; }
}
