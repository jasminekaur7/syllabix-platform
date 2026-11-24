package com.syllabix.dto;

public class StudentProfileDto {
    private Long id;
    private String fullName;
    private String email;
    private String college;
    private String degree;
    private String branch;
    private Integer currentSemester;
    private String academicGoals;
    private Double dailyStudyHours;
    private String preferredStudyTimings;
    private String weeklyAvailability;

    public StudentProfileDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(Integer currentSemester) { this.currentSemester = currentSemester; }

    public String getAcademicGoals() { return academicGoals; }
    public void setAcademicGoals(String academicGoals) { this.academicGoals = academicGoals; }

    public Double getDailyStudyHours() { return dailyStudyHours; }
    public void setDailyStudyHours(Double dailyStudyHours) { this.dailyStudyHours = dailyStudyHours; }

    public String getPreferredStudyTimings() { return preferredStudyTimings; }
    public void setPreferredStudyTimings(String preferredStudyTimings) { this.preferredStudyTimings = preferredStudyTimings; }

    public String getWeeklyAvailability() { return weeklyAvailability; }
    public void setWeeklyAvailability(String weeklyAvailability) { this.weeklyAvailability = weeklyAvailability; }
}
