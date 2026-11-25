package com.syllabix.dto;

import java.time.LocalDate;

public class ThaparHolidayDto {

    private LocalDate date;
    private String name;
    private String description;
    private Long daysRemaining;
    private Boolean isPast = false;

    public ThaparHolidayDto() {}

    public ThaparHolidayDto(LocalDate date, String name, String description) {
        this.date = date;
        this.name = name;
        this.description = description;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(Long daysRemaining) { this.daysRemaining = daysRemaining; }

    public Boolean getIsPast() { return isPast; }
    public void setIsPast(Boolean past) { isPast = past; }
}
