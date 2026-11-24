package com.syllabix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SubjectDto {
    private Long id;

    @NotBlank(message = "Subject name is required")
    private String name;

    @NotBlank(message = "Subject code is required")
    private String code;

    @NotNull(message = "Semester is required")
    private Integer semester;

    private Integer credits = 3;
    private String description;
    private String teacherName;
    private String targetGrade = "A";
    private String colorHex = "#4f46e5";

    // Calculated metrics
    private Double completionPercentage = 0.0;
    private Long totalTopics = 0L;
    private Long completedTopics = 0L;
    private Double averageMarksPercentage;
    private String nextAssessmentTitle;
    private String nextAssessmentDate;
    private Long nextAssessmentDaysRemaining;

    private List<UnitDto> units;

    public SubjectDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getTargetGrade() { return targetGrade; }
    public void setTargetGrade(String targetGrade) { this.targetGrade = targetGrade; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public Double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Double completionPercentage) { this.completionPercentage = completionPercentage; }

    public Long getTotalTopics() { return totalTopics; }
    public void setTotalTopics(Long totalTopics) { this.totalTopics = totalTopics; }

    public Long getCompletedTopics() { return completedTopics; }
    public void setCompletedTopics(Long completedTopics) { this.completedTopics = completedTopics; }

    public Double getAverageMarksPercentage() { return averageMarksPercentage; }
    public void setAverageMarksPercentage(Double averageMarksPercentage) { this.averageMarksPercentage = averageMarksPercentage; }

    public String getNextAssessmentTitle() { return nextAssessmentTitle; }
    public void setNextAssessmentTitle(String nextAssessmentTitle) { this.nextAssessmentTitle = nextAssessmentTitle; }

    public String getNextAssessmentDate() { return nextAssessmentDate; }
    public void setNextAssessmentDate(String nextAssessmentDate) { this.nextAssessmentDate = nextAssessmentDate; }

    public Long getNextAssessmentDaysRemaining() { return nextAssessmentDaysRemaining; }
    public void setNextAssessmentDaysRemaining(Long nextAssessmentDaysRemaining) { this.nextAssessmentDaysRemaining = nextAssessmentDaysRemaining; }

    public List<UnitDto> getUnits() { return units; }
    public void setUnits(List<UnitDto> units) { this.units = units; }
}
