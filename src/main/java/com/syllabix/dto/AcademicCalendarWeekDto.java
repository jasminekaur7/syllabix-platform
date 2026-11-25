package com.syllabix.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AcademicCalendarWeekDto {

    private Integer weekNumber;
    private String weekLabel;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type; // TEACHING, MST, EVALUATION, EST, NON_TEACHING_BREAK
    private String description;
    private Boolean isCurrentWeek = false;
    private List<String> notes = new ArrayList<>();

    public AcademicCalendarWeekDto() {}

    public AcademicCalendarWeekDto(Integer weekNumber, String weekLabel, LocalDate startDate, LocalDate endDate, String type, String description) {
        this.weekNumber = weekNumber;
        this.weekLabel = weekLabel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.description = description;
    }

    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(Integer weekNumber) { this.weekNumber = weekNumber; }

    public String getWeekLabel() { return weekLabel; }
    public void setWeekLabel(String weekLabel) { this.weekLabel = weekLabel; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsCurrentWeek() { return isCurrentWeek; }
    public void setIsCurrentWeek(Boolean currentWeek) { isCurrentWeek = currentWeek; }

    public List<String> getNotes() { return notes; }
    public void setNotes(List<String> notes) { this.notes = notes; }
}
