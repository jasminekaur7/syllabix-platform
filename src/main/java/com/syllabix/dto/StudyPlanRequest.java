package com.syllabix.dto;

import java.time.LocalDate;
import java.util.List;

public class StudyPlanRequest {
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double dailyStudyHours;
    private List<Long> subjectIds; // Optional filter; null means all subjects
    private Double examUrgencyWeight = 0.35;
    private Double syllabusIncompletionWeight = 0.30;
    private Double topicDifficultyWeight = 0.20;
    private Double previousPerformanceWeight = 0.15;

    public StudyPlanRequest() {}

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getDailyStudyHours() { return dailyStudyHours; }
    public void setDailyStudyHours(Double dailyStudyHours) { this.dailyStudyHours = dailyStudyHours; }

    public List<Long> getSubjectIds() { return subjectIds; }
    public void setSubjectIds(List<Long> subjectIds) { this.subjectIds = subjectIds; }

    public Double getExamUrgencyWeight() { return examUrgencyWeight; }
    public void setExamUrgencyWeight(Double examUrgencyWeight) { this.examUrgencyWeight = examUrgencyWeight; }

    public Double getSyllabusIncompletionWeight() { return syllabusIncompletionWeight; }
    public void setSyllabusIncompletionWeight(Double syllabusIncompletionWeight) { this.syllabusIncompletionWeight = syllabusIncompletionWeight; }

    public Double getTopicDifficultyWeight() { return topicDifficultyWeight; }
    public void setTopicDifficultyWeight(Double topicDifficultyWeight) { this.topicDifficultyWeight = topicDifficultyWeight; }

    public Double getPreviousPerformanceWeight() { return previousPerformanceWeight; }
    public void setPreviousPerformanceWeight(Double previousPerformanceWeight) { this.previousPerformanceWeight = previousPerformanceWeight; }
}
