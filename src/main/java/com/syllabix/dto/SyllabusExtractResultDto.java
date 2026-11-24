package com.syllabix.dto;

import java.util.ArrayList;
import java.util.List;

public class SyllabusExtractResultDto {
    private String subjectName;
    private String subjectCode;
    private String rawTextSnippet;
    private List<ExtractedUnitDto> units = new ArrayList<>();

    public SyllabusExtractResultDto() {}

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getRawTextSnippet() { return rawTextSnippet; }
    public void setRawTextSnippet(String rawTextSnippet) { this.rawTextSnippet = rawTextSnippet; }

    public List<ExtractedUnitDto> getUnits() { return units; }
    public void setUnits(List<ExtractedUnitDto> units) { this.units = units; }

    public static class ExtractedUnitDto {
        private Integer unitNumber;
        private String title;
        private String description;
        private List<ExtractedTopicDto> topics = new ArrayList<>();

        public ExtractedUnitDto() {}

        public Integer getUnitNumber() { return unitNumber; }
        public void setUnitNumber(Integer unitNumber) { this.unitNumber = unitNumber; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public List<ExtractedTopicDto> getTopics() { return topics; }
        public void setTopics(List<ExtractedTopicDto> topics) { this.topics = topics; }
    }

    public static class ExtractedTopicDto {
        private String name;
        private String difficulty = "MEDIUM";
        private Integer estimatedDurationMinutes = 45;

        public ExtractedTopicDto() {}

        public ExtractedTopicDto(String name, String difficulty, Integer estimatedDurationMinutes) {
            this.name = name;
            this.difficulty = difficulty != null ? difficulty : "MEDIUM";
            this.estimatedDurationMinutes = estimatedDurationMinutes != null ? estimatedDurationMinutes : 45;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

        public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
        public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }
    }
}
