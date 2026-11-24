package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_plans")
public class StudyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "daily_target_hours")
    private Double dailyTargetHours = 3.0;

    @Column(name = "total_sessions")
    private Integer totalSessions = 0;

    @Column(length = 30)
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, ARCHIVED

    @Column(name = "algorithm_weights", columnDefinition = "TEXT")
    private String algorithmWeights;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public StudyPlan() {}

    public StudyPlan(Student student, String name, LocalDate startDate, LocalDate endDate, Double dailyTargetHours, Integer totalSessions, String algorithmWeights) {
        this.student = student;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyTargetHours = dailyTargetHours != null ? dailyTargetHours : 3.0;
        this.totalSessions = totalSessions != null ? totalSessions : 0;
        this.status = "ACTIVE";
        this.algorithmWeights = algorithmWeights;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getDailyTargetHours() { return dailyTargetHours; }
    public void setDailyTargetHours(Double dailyTargetHours) { this.dailyTargetHours = dailyTargetHours; }

    public Integer getTotalSessions() { return totalSessions; }
    public void setTotalSessions(Integer totalSessions) { this.totalSessions = totalSessions; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAlgorithmWeights() { return algorithmWeights; }
    public void setAlgorithmWeights(String algorithmWeights) { this.algorithmWeights = algorithmWeights; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
