package com.syllabix.dto;

import java.time.LocalDateTime;

public class StudySessionDto {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private String subjectColor;
    private Long topicId;
    private String topicName;
    private Long studyTaskId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String sessionType = "POMODORO"; // POMODORO, CUSTOM
    private String notes;
    private Integer rating = 5;
    private Boolean isConfirmed = true;

    public StudySessionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectColor() { return subjectColor; }
    public void setSubjectColor(String subjectColor) { this.subjectColor = subjectColor; }

    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public Long getStudyTaskId() { return studyTaskId; }
    public void setStudyTaskId(Long studyTaskId) { this.studyTaskId = studyTaskId; }

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
}
