package com.syllabix.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;

@Service
public class CurriculumCatalogService {

    private static final Logger log = LoggerFactory.getLogger(CurriculumCatalogService.class);
    private final Map<String, CourseCurriculum> catalog = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static class CourseCurriculum {
        private String code;
        private String name;
        private List<UnitCurriculum> units = new ArrayList<>();

        public CourseCurriculum() {}

        public CourseCurriculum(String code, String name, List<UnitCurriculum> units) {
            this.code = code;
            this.name = name;
            this.units = units;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<UnitCurriculum> getUnits() { return units; }
        public void setUnits(List<UnitCurriculum> units) { this.units = units; }
    }

    public static class UnitCurriculum {
        private Integer unitNumber;
        private String title;
        private List<String> topics = new ArrayList<>();

        public UnitCurriculum() {}

        public UnitCurriculum(Integer unitNumber, String title, List<String> topics) {
            this.unitNumber = unitNumber;
            this.title = title;
            this.topics = topics;
        }

        public Integer getUnitNumber() { return unitNumber; }
        public void setUnitNumber(Integer unitNumber) { this.unitNumber = unitNumber; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public List<String> getTopics() { return topics; }
        public void setTopics(List<String> topics) { this.topics = topics; }
    }

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("syllabus/thapar_curriculum_catalog.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    Map<String, CourseCurriculum> loaded = objectMapper.readValue(
                            is, new TypeReference<Map<String, CourseCurriculum>>() {});
                    catalog.putAll(loaded);
                    log.info("Loaded {} courses from Thapar curriculum catalog JSON.", catalog.size());
                }
            } else {
                log.warn("thapar_curriculum_catalog.json not found in classpath.");
            }
        } catch (Exception e) {
            log.error("Failed to load Thapar curriculum catalog: {}", e.getMessage(), e);
        }
    }

    /**
     * Finds course curriculum by subject code or fuzzy matching on name
     */
    public Optional<CourseCurriculum> findCourse(String code, String name) {
        if (code != null) {
            String normCode = code.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
            for (Map.Entry<String, CourseCurriculum> entry : catalog.entrySet()) {
                String entryNorm = entry.getKey().replaceAll("[^A-Za-z0-9]", "").toUpperCase();
                if (entryNorm.equals(normCode) || normCode.contains(entryNorm) || entryNorm.contains(normCode)) {
                    return Optional.of(entry.getValue());
                }
            }
        }

        if (name != null && !name.trim().isEmpty()) {
            String cleanName = name.toLowerCase().replaceAll("[^a-z0-9]", " ").trim();
            for (CourseCurriculum c : catalog.values()) {
                String cNameClean = c.getName().toLowerCase().replaceAll("[^a-z0-9]", " ").trim();
                if (cNameClean.equals(cleanName) || cNameClean.contains(cleanName) || cleanName.contains(cNameClean)) {
                    return Optional.of(c);
                }
            }
        }

        return Optional.empty();
    }

    public Map<String, CourseCurriculum> getFullCatalog() {
        return Collections.unmodifiableMap(catalog);
    }
}
