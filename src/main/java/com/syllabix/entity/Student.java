package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(length = 200)
    private String college;

    @Column(length = 100)
    private String degree;

    @Column(length = 100)
    private String branch;

    @Column(name = "current_semester")
    private Integer currentSemester = 1;

    @Column(name = "academic_goals", columnDefinition = "TEXT")
    private String academicGoals;

    @Column(name = "daily_study_hours")
    private Double dailyStudyHours = 3.0;

    @Column(name = "preferred_study_timings", length = 50)
    private String preferredStudyTimings = "EVENING"; // MORNING, AFTERNOON, EVENING, NIGHT

    @Column(name = "weekly_availability", columnDefinition = "TEXT")
    private String weeklyAvailability;

    @Column(length = 30)
    private String role = "ROLE_STUDENT";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Student() {}

    public Student(String fullName, String email, String passwordHash, String college, String degree, String branch, Integer currentSemester) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.college = college;
        this.degree = degree;
        this.branch = branch;
        this.currentSemester = currentSemester != null ? currentSemester : 1;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

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

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
