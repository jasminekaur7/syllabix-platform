package com.syllabix.dto;

import java.time.LocalDate;

public class ThaparDatesheetMilestoneDto {

    private String title;
    private String weekRange;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type; // MST, EVALUATION, EST, BREAK, REVIEW
    private Long daysUntil;
    private String status; // UPCOMING, IN_PROGRESS, COMPLETED
    private String details;

    public ThaparDatesheetMilestoneDto() {}

    public ThaparDatesheetMilestoneDto(String title, String weekRange, LocalDate startDate, LocalDate endDate, String type, String details) {
        this.title = title;
        this.weekRange = weekRange;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.details = details;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getWeekRange() { return weekRange; }
    public void setWeekRange(String weekRange) { this.weekRange = weekRange; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getDaysUntil() { return daysUntil; }
    public void setDaysUntil(Long daysUntil) { this.daysUntil = daysUntil; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
