package com.syllabix.dto;

import java.util.List;

public class DashboardStatsDto {
    private String studentName;
    private Long totalEnrolledSubjects;
    private Double overallSyllabusCompletionPercentage;
    private Long totalTopics;
    private Long completedTopics;
    private Long inProgressTopics;
    private Long notStartedTopics;

    private Long upcomingAssessmentsCount;
    private Long pendingTasksCount;
    private Long completedTasksCount;
    private Double todayPlannedStudyHours;
    private Double todayCompletedStudyHours;
    private Double weeklyStudyHours;
    private Integer currentStreakDays;

    private Double overallMarksAveragePercentage;
    private String estimatedCgpa;

    private List<RecommendationDto> topRecommendationsToday;
    private List<AssessmentDto> upcomingAssessments;
    private List<StudyTaskDto> todayTasks;
    private List<SubjectDto> subjectsNeedingAttention;
    private List<SubjectDto> subjects;

    public DashboardStatsDto() {}

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getTotalEnrolledSubjects() { return totalEnrolledSubjects; }
    public void setTotalEnrolledSubjects(Long totalEnrolledSubjects) { this.totalEnrolledSubjects = totalEnrolledSubjects; }

    public Double getOverallSyllabusCompletionPercentage() { return overallSyllabusCompletionPercentage; }
    public void setOverallSyllabusCompletionPercentage(Double overallSyllabusCompletionPercentage) { this.overallSyllabusCompletionPercentage = overallSyllabusCompletionPercentage; }

    public Long getTotalTopics() { return totalTopics; }
    public void setTotalTopics(Long totalTopics) { this.totalTopics = totalTopics; }

    public Long getCompletedTopics() { return completedTopics; }
    public void setCompletedTopics(Long completedTopics) { this.completedTopics = completedTopics; }

    public Long getInProgressTopics() { return inProgressTopics; }
    public void setInProgressTopics(Long inProgressTopics) { this.inProgressTopics = inProgressTopics; }

    public Long getNotStartedTopics() { return notStartedTopics; }
    public void setNotStartedTopics(Long notStartedTopics) { this.notStartedTopics = notStartedTopics; }

    public Long getUpcomingAssessmentsCount() { return upcomingAssessmentsCount; }
    public void setUpcomingAssessmentsCount(Long upcomingAssessmentsCount) { this.upcomingAssessmentsCount = upcomingAssessmentsCount; }

    public Long getPendingTasksCount() { return pendingTasksCount; }
    public void setPendingTasksCount(Long pendingTasksCount) { this.pendingTasksCount = pendingTasksCount; }

    public Long getCompletedTasksCount() { return completedTasksCount; }
    public void setCompletedTasksCount(Long completedTasksCount) { this.completedTasksCount = completedTasksCount; }

    public Double getTodayPlannedStudyHours() { return todayPlannedStudyHours; }
    public void setTodayPlannedStudyHours(Double todayPlannedStudyHours) { this.todayPlannedStudyHours = todayPlannedStudyHours; }

    public Double getTodayCompletedStudyHours() { return todayCompletedStudyHours; }
    public void setTodayCompletedStudyHours(Double todayCompletedStudyHours) { this.todayCompletedStudyHours = todayCompletedStudyHours; }

    public Double getWeeklyStudyHours() { return weeklyStudyHours; }
    public void setWeeklyStudyHours(Double weeklyStudyHours) { this.weeklyStudyHours = weeklyStudyHours; }

    public Integer getCurrentStreakDays() { return currentStreakDays; }
    public void setCurrentStreakDays(Integer currentStreakDays) { this.currentStreakDays = currentStreakDays; }

    public Double getOverallMarksAveragePercentage() { return overallMarksAveragePercentage; }
    public void setOverallMarksAveragePercentage(Double overallMarksAveragePercentage) { this.overallMarksAveragePercentage = overallMarksAveragePercentage; }

    public String getEstimatedCgpa() { return estimatedCgpa; }
    public void setEstimatedCgpa(String estimatedCgpa) { this.estimatedCgpa = estimatedCgpa; }

    public List<RecommendationDto> getTopRecommendationsToday() { return topRecommendationsToday; }
    public void setTopRecommendationsToday(List<RecommendationDto> topRecommendationsToday) { this.topRecommendationsToday = topRecommendationsToday; }

    public List<AssessmentDto> getUpcomingAssessments() { return upcomingAssessments; }
    public void setUpcomingAssessments(List<AssessmentDto> upcomingAssessments) { this.upcomingAssessments = upcomingAssessments; }

    public List<StudyTaskDto> getTodayTasks() { return todayTasks; }
    public void setTodayTasks(List<StudyTaskDto> todayTasks) { this.todayTasks = todayTasks; }

    public List<SubjectDto> getSubjectsNeedingAttention() { return subjectsNeedingAttention; }
    public void setSubjectsNeedingAttention(List<SubjectDto> subjectsNeedingAttention) { this.subjectsNeedingAttention = subjectsNeedingAttention; }

    public List<SubjectDto> getSubjects() { return subjects; }
    public void setSubjects(List<SubjectDto> subjects) { this.subjects = subjects; }
}
