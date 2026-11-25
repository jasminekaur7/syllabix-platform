package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String difficulty = "MEDIUM"; // EASY, MEDIUM, HARD

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes = 45;

    @Column(name = "completion_status", length = 20)
    private String completionStatus = "NOT_STARTED"; // NOT_STARTED, IN_PROGRESS, COMPLETED

    @Column(name = "confidence_level")
    private Integer confidenceLevel = 3; // 1 to 5

    @Column(name = "priority_score")
    private Double priorityScore = 0.0;

    @Column(name = "last_studied_date")
    private LocalDate lastStudiedDate;

    @Column(name = "ai_reasoning", columnDefinition = "TEXT")
    private String aiReasoning;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Topic() {}

    public Topic(Unit unit, Subject subject, Student student, String name, String description, String difficulty, Integer estimatedDurationMinutes, String completionStatus, Integer confidenceLevel) {
        this.unit = unit;
        this.subject = subject;
        this.student = student;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty != null ? difficulty : "MEDIUM";
        this.estimatedDurationMinutes = estimatedDurationMinutes != null ? estimatedDurationMinutes : 45;
        this.completionStatus = completionStatus != null ? completionStatus : "NOT_STARTED";
        this.confidenceLevel = confidenceLevel != null ? confidenceLevel : 3;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
