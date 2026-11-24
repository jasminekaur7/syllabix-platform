package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_sessions")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_task_id")
    private StudyTask studyTask;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "session_type", length = 30)
    private String sessionType = "POMODORO"; // POMODORO, CUSTOM

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private Integer rating = 5; // Productivity rating 1 to 5

    @Column(name = "is_confirmed")
    private Boolean isConfirmed = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public StudySession() {}

    public StudySession(Student student, Subject subject, Topic topic, StudyTask studyTask, LocalDateTime startTime, LocalDateTime endTime, Integer durationMinutes, String sessionType, String notes, Integer rating) {
        this.student = student;
        this.subject = subject;
        this.topic = topic;
        this.studyTask = studyTask;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 25;
        this.sessionType = sessionType != null ? sessionType : "POMODORO";
        this.notes = notes;
        this.rating = rating != null ? rating : 5;
        this.isConfirmed = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    public StudyTask getStudyTask() { return studyTask; }
    public void setStudyTask(StudyTask studyTask) { this.studyTask = studyTask; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Boolean getIsConfirmed() { return isConfirmed; }
    public void setIsConfirmed(Boolean confirmed) { isConfirmed = confirmed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
