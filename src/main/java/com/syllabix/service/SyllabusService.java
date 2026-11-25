package com.syllabix.service;

import com.syllabix.dto.SyllabusExtractResultDto;
import com.syllabix.dto.TopicDto;
import com.syllabix.dto.UnitDto;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.entity.Unit;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import com.syllabix.repository.UnitRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SyllabusService {

    private final SubjectRepository subjectRepository;
    private final UnitRepository unitRepository;
    private final TopicRepository topicRepository;
    private final AiTopicEvaluatorService aiTopicEvaluatorService;

    public SyllabusService(SubjectRepository subjectRepository,
                           UnitRepository unitRepository,
                           TopicRepository topicRepository,
                           AiTopicEvaluatorService aiTopicEvaluatorService) {
        this.subjectRepository = subjectRepository;
        this.unitRepository = unitRepository;
        this.topicRepository = topicRepository;
        this.aiTopicEvaluatorService = aiTopicEvaluatorService;
    }

    public List<UnitDto> getUnitsForSubject(Long subjectId, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        List<Unit> units = unitRepository.findBySubjectIdOrderByUnitNumberAsc(subject.getId());
        return units.stream().map(unit -> {
            UnitDto dto = new UnitDto();
            dto.setId(unit.getId());
            dto.setSubjectId(subject.getId());
            dto.setUnitNumber(unit.getUnitNumber());
            dto.setTitle(unit.getTitle());
            dto.setDescription(unit.getDescription());

            List<Topic> topics = topicRepository.findByUnitId(unit.getId());
            List<TopicDto> topicDtos = topics.stream().map(this::toTopicDto).collect(Collectors.toList());
            dto.setTopics(topicDtos);

            long total = topicDtos.size();
            long completed = topicDtos.stream().filter(t -> "COMPLETED".equalsIgnoreCase(t.getCompletionStatus())).count();
            dto.setCompletionPercentage(total > 0 ? Math.round((completed * 100.0 / total) * 10.0) / 10.0 : 0.0);

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public UnitDto createUnit(Long subjectId, UnitDto dto, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        Unit unit = new Unit(subject, dto.getUnitNumber(), dto.getTitle(), dto.getDescription());
        unitRepository.save(unit);

        dto.setId(unit.getId());
        dto.setSubjectId(subject.getId());
        dto.setCompletionPercentage(0.0);
        return dto;
    }

    @Transactional
    public UnitDto updateUnit(Long unitId, UnitDto dto, Student student) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found: " + unitId));

        if (!unit.getSubject().getStudent().getId().equals(student.getId())) {
            throw new ResourceNotFoundException("Unit does not belong to student");
        }

        unit.setTitle(dto.getTitle());
        unit.setUnitNumber(dto.getUnitNumber());
        unit.setDescription(dto.getDescription());
        unitRepository.save(unit);

        dto.setId(unit.getId());
        dto.setSubjectId(unit.getSubject().getId());
        return dto;
    }

    @Transactional
    public void deleteUnit(Long unitId, Student student) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found: " + unitId));

        if (!unit.getSubject().getStudent().getId().equals(student.getId())) {
            throw new ResourceNotFoundException("Unit does not belong to student");
        }

        unitRepository.delete(unit);
    }

    public List<TopicDto> getTopicsForSubject(Long subjectId, Student student) {
        subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        return topicRepository.findBySubjectId(subjectId).stream()
                .map(this::toTopicDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopicDto createTopic(Long unitId, TopicDto dto, Student student) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found: " + unitId));

        Subject subject = unit.getSubject();
        if (!subject.getStudent().getId().equals(student.getId())) {
            throw new ResourceNotFoundException("Unit does not belong to student");
        }

        Topic topic = new Topic(
                unit,
                subject,
                student,
                dto.getName(),
                dto.getDescription(),
                dto.getDifficulty(),
                dto.getEstimatedDurationMinutes(),
                dto.getCompletionStatus(),
                dto.getConfidenceLevel()
        );

        if (dto.getAiReasoning() != null && !dto.getAiReasoning().trim().isEmpty()) {
            topic.setAiReasoning(dto.getAiReasoning());
        } else {
            AiTopicEvaluatorService.EvaluationResult eval = aiTopicEvaluatorService.evaluateTopic(
                    subject.getName(), subject.getCode(), unit.getTitle(), dto.getName(), dto.getDescription());
            topic.setDifficulty(eval.getDifficulty());
            if (dto.getEstimatedDurationMinutes() == null || dto.getEstimatedDurationMinutes() == 45) {
                topic.setEstimatedDurationMinutes(eval.getEstimatedMinutes());
            }
            topic.setAiReasoning(eval.getAiReasoning());
        }

        if ("COMPLETED".equalsIgnoreCase(dto.getCompletionStatus())) {
            topic.setLastStudiedDate(LocalDate.now());
        }
        if (dto.getAiReasoning() != null) {
            topic.setAiReasoning(dto.getAiReasoning());
        }

        topicRepository.save(topic);
        return toTopicDto(topic);
    }

    @Transactional
    public TopicDto updateTopic(Long topicId, TopicDto dto, Student student) {
        Topic topic = topicRepository.findByIdAndStudentId(topicId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + topicId));

        topic.setName(dto.getName());
        topic.setDescription(dto.getDescription());
        if (dto.getDifficulty() != null) topic.setDifficulty(dto.getDifficulty());
        if (dto.getEstimatedDurationMinutes() != null) topic.setEstimatedDurationMinutes(dto.getEstimatedDurationMinutes());
        if (dto.getConfidenceLevel() != null) topic.setConfidenceLevel(dto.getConfidenceLevel());
        if (dto.getAiReasoning() != null) topic.setAiReasoning(dto.getAiReasoning());

        if (dto.getCompletionStatus() != null) {
            String prevStatus = topic.getCompletionStatus();
            topic.setCompletionStatus(dto.getCompletionStatus());
            if ("COMPLETED".equalsIgnoreCase(dto.getCompletionStatus()) && !"COMPLETED".equalsIgnoreCase(prevStatus)) {
                topic.setLastStudiedDate(LocalDate.now());
            }
        }

        topicRepository.save(topic);
        return toTopicDto(topic);
    }

    @Transactional
    public TopicDto toggleTopicStatus(Long topicId, String status, Student student) {
        Topic topic = topicRepository.findByIdAndStudentId(topicId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + topicId));

        topic.setCompletionStatus(status);
        if ("COMPLETED".equalsIgnoreCase(status)) {
            topic.setLastStudiedDate(LocalDate.now());
        }
        topicRepository.save(topic);
        return toTopicDto(topic);
    }

    @Transactional
    public void deleteTopic(Long topicId, Student student) {
        Topic topic = topicRepository.findByIdAndStudentId(topicId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + topicId));

        topicRepository.delete(topic);
    }

    public List<TopicDto> searchTopics(String query, Student student) {
        return topicRepository.searchTopics(student.getId(), query).stream()
                .map(this::toTopicDto)
                .collect(Collectors.toList());
    }

    /**
     * Advanced Feature: Extract syllabus structure from PDF file using Apache PDFBox.
     * Parses the PDF text, extracts Units/Modules and individual Topics, and returns structured
     * preview DTO for student review and editing before saving.
     */
    public SyllabusExtractResultDto extractSyllabusFromPdf(MultipartFile file) throws IOException {
        SyllabusExtractResultDto result = new SyllabusExtractResultDto();

        byte[] bytes = file.getBytes();
        String fullText;

        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            fullText = stripper.getText(document);
        }

        result.setRawTextSnippet(fullText.length() > 500 ? fullText.substring(0, 500) + "..." : fullText);

        // Heuristics for course title & code
        Pattern codePattern = Pattern.compile("([A-Z]{2,4}[-\\s]?[0-9]{3,4})");
        Matcher codeMatcher = codePattern.matcher(fullText);
        if (codeMatcher.find()) {
            result.setSubjectCode(codeMatcher.group(1).replaceAll("\\s+", "-"));
        }

        String[] lines = fullText.split("\\r?\\n");
        List<SyllabusExtractResultDto.ExtractedUnitDto> extractedUnits = new ArrayList<>();
        SyllabusExtractResultDto.ExtractedUnitDto currentUnit = null;
        int unitCount = 0;

        Pattern unitHeaderPattern = Pattern.compile("(?i)^(unit|module|chapter)\\s*([0-9ivx]+)[:\\-\\s]*(.*)");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher unitMatcher = unitHeaderPattern.matcher(line);
            if (unitMatcher.find()) {
                unitCount++;
                currentUnit = new SyllabusExtractResultDto.ExtractedUnitDto();
                currentUnit.setUnitNumber(unitCount);
                String title = unitMatcher.group(3).trim();
                currentUnit.setTitle(title.isEmpty() ? ("Unit " + unitCount) : title);
                currentUnit.setDescription(line);
                extractedUnits.add(currentUnit);
            } else if (currentUnit != null) {
                // Topic line heuristic: bullets, dashes, numbers, commas, or semicolons
                if (line.matches("^[\\-\\•\\*\\d+\\.\\)]+\\s*.*") || line.contains(";") || line.contains(",")) {
                    String clean = line.replaceFirst("^[\\-\\•\\*\\d+\\.\\)]+\\s*", "").trim();
                    if (clean.length() > 3 && clean.length() < 120) {
                        currentUnit.getTopics().add(new SyllabusExtractResultDto.ExtractedTopicDto(clean, "MEDIUM", 45));
                    }
                } else if (line.length() > 4 && line.length() < 100 && !line.startsWith("Page") && !line.startsWith("Copyright")) {
                    currentUnit.getTopics().add(new SyllabusExtractResultDto.ExtractedTopicDto(line, "MEDIUM", 45));
                }
            }
        }

        // Fallback: If no explicit "Unit" keywords found, group lines into 3-4 default units
        if (extractedUnits.isEmpty()) {
            SyllabusExtractResultDto.ExtractedUnitDto fallbackUnit = new SyllabusExtractResultDto.ExtractedUnitDto();
            fallbackUnit.setUnitNumber(1);
            fallbackUnit.setTitle("General Core Syllabus");
            for (String line : lines) {
                String clean = line.trim().replaceFirst("^[\\-\\•\\*\\d+\\.\\)]+\\s*", "");
                if (clean.length() > 5 && clean.length() < 120 && !clean.toLowerCase().contains("syllabus")) {
                    fallbackUnit.getTopics().add(new SyllabusExtractResultDto.ExtractedTopicDto(clean, "MEDIUM", 45));
                }
            }
            if (!fallbackUnit.getTopics().isEmpty()) {
                extractedUnits.add(fallbackUnit);
            }
        }

        result.setUnits(extractedUnits);
        return result;
    }

    /**
     * Persist reviewed extracted syllabus units and topics into the database for a subject
     */
    @Transactional
    public void saveReviewedExtractedSyllabus(Long subjectId, SyllabusExtractResultDto payload, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        int unitNumber = 1;
        for (SyllabusExtractResultDto.ExtractedUnitDto u : payload.getUnits()) {
            Unit unit = new Unit(subject, unitNumber++, u.getTitle(), u.getDescription());
            unitRepository.save(unit);

            if (u.getTopics() != null) {
                for (SyllabusExtractResultDto.ExtractedTopicDto t : u.getTopics()) {
                    Topic topic = new Topic(
                            unit,
                            subject,
                            student,
                            t.getName(),
                            "",
                            t.getDifficulty() != null ? t.getDifficulty() : "MEDIUM",
                            t.getEstimatedDurationMinutes() != null ? t.getEstimatedDurationMinutes() : 45,
                            "NOT_STARTED",
                            3
                    );
                    topicRepository.save(topic);
                }
            }
        }
    }

    @Transactional
    public TopicDto aiEvaluateTopic(Long topicId, Student student) {
        Topic topic = topicRepository.findByIdAndStudentId(topicId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found: " + topicId));

        AiTopicEvaluatorService.EvaluationResult eval = aiTopicEvaluatorService.evaluateTopic(
                topic.getSubject().getName(),
                topic.getSubject().getCode(),
                topic.getUnit().getTitle(),
                topic.getName(),
                topic.getDescription()
        );

        topic.setDifficulty(eval.getDifficulty());
        topic.setEstimatedDurationMinutes(eval.getEstimatedMinutes());
        topic.setAiReasoning(eval.getAiReasoning());
        topicRepository.save(topic);

        return toTopicDto(topic);
    }

    public TopicDto toTopicDto(Topic topic) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setUnitId(topic.getUnit().getId());
        dto.setUnitTitle(topic.getUnit().getTitle());
        dto.setUnitNumber(topic.getUnit().getUnitNumber());
        dto.setSubjectId(topic.getSubject().getId());
        dto.setSubjectName(topic.getSubject().getName());
        dto.setSubjectCode(topic.getSubject().getCode());
        dto.setName(topic.getName());
        dto.setDescription(topic.getDescription());
        dto.setDifficulty(topic.getDifficulty());
        dto.setEstimatedDurationMinutes(topic.getEstimatedDurationMinutes());
        dto.setCompletionStatus(topic.getCompletionStatus());
        dto.setConfidenceLevel(topic.getConfidenceLevel());
        dto.setPriorityScore(topic.getPriorityScore());
        dto.setLastStudiedDate(topic.getLastStudiedDate());
        dto.setAiReasoning(topic.getAiReasoning());
        return dto;
    }
}
