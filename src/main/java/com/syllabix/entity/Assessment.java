package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 30)
    private String type = "MST"; // QUIZ, MST, EST, ASSIGNMENT, LAB, PROJECT, VIVA

    @Column(name = "assessment_date_time", nullable = false)
    private LocalDateTime assessmentDateTime;

    @Column(name = "syllabus_coverage", columnDefinition = "TEXT")
    private String syllabusCoverage;

    @Column(name = "max_marks", nullable = false)
    private Double maxMarks = 100.0;

    @Column(name = "preparation_status", length = 30)
    private String preparationStatus = "NOT_STARTED"; // NOT_STARTED, PREPARING, READY

    @Column(length = 20)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Assessment() {}

    public Assessment(Student student, Subject subject, String title, String type, LocalDateTime assessmentDateTime, String syllabusCoverage, Double maxMarks, String preparationStatus, String priority, String notes) {
        this.student = student;
        this.subject = subject;
        this.title = title;
        this.type = type != null ? type : "MST";
        this.assessmentDateTime = assessmentDateTime;
        this.syllabusCoverage = syllabusCoverage;
        this.maxMarks = maxMarks != null ? maxMarks : 100.0;
        this.preparationStatus = preparationStatus != null ? preparationStatus : "NOT_STARTED";
        this.priority = priority != null ? priority : "MEDIUM";
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
