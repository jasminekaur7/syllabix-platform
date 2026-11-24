package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "academic_insights")
public class AcademicInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 50)
    private String category; // RISK_ALERT, READINESS_SCORE, REVISION_OPPORTUNITY, STRENGTH_WEAKNESS

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(length = 20)
    private String severity = "INFO"; // INFO, WARNING, CRITICAL, SUCCESS

    @Column(name = "actionable_suggestion", columnDefinition = "TEXT")
    private String actionableSuggestion;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Column(name = "is_dismissed")
    private Boolean isDismissed = false;

    public AcademicInsight() {}

    public AcademicInsight(Student student, String category, String title, String message, String severity, String actionableSuggestion) {
        this.student = student;
        this.category = category;
        this.title = title;
        this.message = message;
        this.severity = severity != null ? severity : "INFO";
        this.actionableSuggestion = actionableSuggestion;
        this.generatedAt = LocalDateTime.now();
        this.isDismissed = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getActionableSuggestion() { return actionableSuggestion; }
    public void setActionableSuggestion(String actionableSuggestion) { this.actionableSuggestion = actionableSuggestion; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public Boolean getIsDismissed() { return isDismissed; }
    public void setIsDismissed(Boolean dismissed) { isDismissed = dismissed; }
}
