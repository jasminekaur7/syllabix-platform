package com.syllabix.service;

import com.syllabix.dto.SubjectDto;
import com.syllabix.dto.TopicDto;
import com.syllabix.dto.UnitDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.entity.Unit;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.exception.ValidationException;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import com.syllabix.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UnitRepository unitRepository;
    private final TopicRepository topicRepository;
    private final MarksRepository marksRepository;
    private final AssessmentRepository assessmentRepository;
    private final CurriculumCatalogService curriculumCatalogService;
    private final AiTopicEvaluatorService aiTopicEvaluatorService;

    public SubjectService(SubjectRepository subjectRepository,
                          UnitRepository unitRepository,
                          TopicRepository topicRepository,
                          MarksRepository marksRepository,
                          AssessmentRepository assessmentRepository,
                          CurriculumCatalogService curriculumCatalogService,
                          AiTopicEvaluatorService aiTopicEvaluatorService) {
        this.subjectRepository = subjectRepository;
        this.unitRepository = unitRepository;
        this.topicRepository = topicRepository;
        this.marksRepository = marksRepository;
        this.assessmentRepository = assessmentRepository;
        this.curriculumCatalogService = curriculumCatalogService;
        this.aiTopicEvaluatorService = aiTopicEvaluatorService;
    }

    public List<SubjectDto> getAllSubjects(Student student) {
        List<Subject> subjects = subjectRepository.findByStudentIdOrderBySemesterAscNameAsc(student.getId());
        return subjects.stream().map(this::enrichSubjectDto).collect(Collectors.toList());
    }

    public SubjectDto getSubjectById(Long id, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found or does not belong to student: " + id));

        SubjectDto dto = enrichSubjectDto(subject);

        // Populate units and nested topics
        List<Unit> units = unitRepository.findBySubjectIdOrderByUnitNumberAsc(subject.getId());
        List<UnitDto> unitDtos = new ArrayList<>();

        for (Unit unit : units) {
            UnitDto uDto = new UnitDto();
            uDto.setId(unit.getId());
            uDto.setSubjectId(subject.getId());
            uDto.setUnitNumber(unit.getUnitNumber());
            uDto.setTitle(unit.getTitle());
            uDto.setDescription(unit.getDescription());

            List<Topic> topics = topicRepository.findByUnitId(unit.getId());
            List<TopicDto> topicDtos = topics.stream().map(t -> {
                TopicDto tDto = new TopicDto();
                tDto.setId(t.getId());
                tDto.setUnitId(unit.getId());
                tDto.setUnitTitle(unit.getTitle());
                tDto.setUnitNumber(unit.getUnitNumber());
                tDto.setSubjectId(subject.getId());
                tDto.setSubjectName(subject.getName());
                tDto.setSubjectCode(subject.getCode());
                tDto.setName(t.getName());
                tDto.setDescription(t.getDescription());
                tDto.setDifficulty(t.getDifficulty());
                tDto.setEstimatedDurationMinutes(t.getEstimatedDurationMinutes());
                tDto.setCompletionStatus(t.getCompletionStatus());
                tDto.setConfidenceLevel(t.getConfidenceLevel());
                tDto.setPriorityScore(t.getPriorityScore());
                tDto.setLastStudiedDate(t.getLastStudiedDate());
                tDto.setAiReasoning(t.getAiReasoning());
                return tDto;
            }).collect(Collectors.toList());

            uDto.setTopics(topicDtos);

            long totalInUnit = topicDtos.size();
            long completedInUnit = topicDtos.stream().filter(t -> "COMPLETED".equalsIgnoreCase(t.getCompletionStatus())).count();
            uDto.setCompletionPercentage(totalInUnit > 0 ? Math.round((completedInUnit * 100.0 / totalInUnit) * 10.0) / 10.0 : 0.0);

            unitDtos.add(uDto);
        }

        dto.setUnits(unitDtos);
        return dto;
    }

    @Transactional
    public SubjectDto createSubject(SubjectDto dto, Student student) {
        if (subjectRepository.existsByStudentIdAndCode(student.getId(), dto.getCode())) {
            throw new ValidationException("A subject with code " + dto.getCode() + " already exists for your account");
        }

        Subject subject = new Subject(
                student,
                dto.getName(),
                dto.getCode().toUpperCase().trim(),
                dto.getSemester(),
                dto.getCredits(),
                dto.getDescription(),
                dto.getTeacherName(),
                dto.getTargetGrade(),
                dto.getColorHex()
        );

        subjectRepository.save(subject);
        populateSyllabusAndEvaluateWithAi(subject, student);
        return enrichSubjectDto(subject);
    }

    @Transactional
    public SubjectDto aiEvaluateSubject(Long subjectId, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        List<Unit> units = unitRepository.findBySubjectIdOrderByUnitNumberAsc(subject.getId());
        if (units.isEmpty()) {
            populateSyllabusAndEvaluateWithAi(subject, student);
        } else {
            for (Unit unit : units) {
                List<Topic> topics = topicRepository.findByUnitId(unit.getId());
                for (Topic topic : topics) {
                    AiTopicEvaluatorService.EvaluationResult eval =
                            aiTopicEvaluatorService.evaluateTopic(subject.getName(), subject.getCode(), unit.getTitle(), topic.getName(), topic.getDescription());
                    topic.setDifficulty(eval.getDifficulty());
                    topic.setEstimatedDurationMinutes(eval.getEstimatedMinutes());
                    topic.setAiReasoning(eval.getAiReasoning());
                    topicRepository.save(topic);
                }
            }
        }

        return getSubjectById(subjectId, student);
    }

    @Transactional
    public void populateSyllabusAndEvaluateWithAi(Subject subject, Student student) {
        List<Unit> existingUnits = unitRepository.findBySubjectIdOrderByUnitNumberAsc(subject.getId());
        if (!existingUnits.isEmpty()) {
            return;
        }

        java.util.Optional<CurriculumCatalogService.CourseCurriculum> courseOpt =
                curriculumCatalogService.findCourse(subject.getCode(), subject.getName());

        if (courseOpt.isPresent()) {
            CurriculumCatalogService.CourseCurriculum course = courseOpt.get();
            for (CurriculumCatalogService.UnitCurriculum uCur : course.getUnits()) {
                Unit unit = new Unit(subject, uCur.getUnitNumber(), uCur.getTitle(), "Extracted from official university scheme & syllabus");
                unitRepository.save(unit);

                for (String topicName : uCur.getTopics()) {
                    AiTopicEvaluatorService.EvaluationResult eval =
                            aiTopicEvaluatorService.evaluateTopic(subject.getName(), subject.getCode(), uCur.getTitle(), topicName, null);

                    Topic topic = new Topic(
                            unit,
                            subject,
                            student,
                            topicName,
                            "AI evaluated from official Thapar syllabus",
                            eval.getDifficulty(),
                            eval.getEstimatedMinutes(),
                            "NOT_STARTED",
                            3
                    );
                    topic.setAiReasoning(eval.getAiReasoning());
                    topicRepository.save(topic);
                }
            }
        } else {
            // Sensible fallback: generate 3 structured curriculum units and evaluate them
            String[] defaultUnits = {
                "Foundations & Theoretical Core",
                "Core Methodologies, Architecture & Implementation",
                "Advanced Systems, Integration & Emerging Trends"
            };

            for (int i = 0; i < defaultUnits.length; i++) {
                Unit unit = new Unit(subject, i + 1, defaultUnits[i], "Standard modular curriculum for " + subject.getName());
                unitRepository.save(unit);

                String[] sampleTopics = (i == 0)
                        ? new String[]{"Fundamental Definitions & Core Principles", "Mathematical Foundations & Terminology", "System Context & Requirements"}
                        : (i == 1)
                        ? new String[]{"Core Design Patterns & Methodologies", "Procedural Algorithms & Performance Constraints", "Data Models & Error Handling"}
                        : new String[]{"Advanced Scalability & System Optimization", "Integration Patterns & Frameworks", "Case Studies & Assessment Synthesis"};

                for (String tName : sampleTopics) {
                    AiTopicEvaluatorService.EvaluationResult eval =
                            aiTopicEvaluatorService.evaluateTopic(subject.getName(), subject.getCode(), unit.getTitle(), tName, null);

                    Topic topic = new Topic(
                            unit,
                            subject,
                            student,
                            tName,
                            "AI evaluated curriculum topic",
                            eval.getDifficulty(),
                            eval.getEstimatedMinutes(),
                            "NOT_STARTED",
                            3
                    );
                    topic.setAiReasoning(eval.getAiReasoning());
                    topicRepository.save(topic);
                }
            }
        }
    }

    @Transactional
    public SubjectDto updateSubject(Long id, SubjectDto dto, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));

        subject.setName(dto.getName());
        subject.setSemester(dto.getSemester());
        if (dto.getCredits() != null) subject.setCredits(dto.getCredits());
        subject.setDescription(dto.getDescription());
        subject.setTeacherName(dto.getTeacherName());
        if (dto.getTargetGrade() != null) subject.setTargetGrade(dto.getTargetGrade());
        if (dto.getColorHex() != null) subject.setColorHex(dto.getColorHex());

        subjectRepository.save(subject);
        return enrichSubjectDto(subject);
    }

    @Transactional
    public void deleteSubject(Long id, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));

        subjectRepository.delete(subject);
    }

    public SubjectDto enrichSubjectDto(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setCode(subject.getCode());
        dto.setSemester(subject.getSemester());
        dto.setCredits(subject.getCredits());
        dto.setDescription(subject.getDescription());
        dto.setTeacherName(subject.getTeacherName());
        dto.setTargetGrade(subject.getTargetGrade());
        dto.setColorHex(subject.getColorHex());

        // Topic completion metrics
        long totalTopics = topicRepository.countBySubjectId(subject.getId());
        long completedTopics = topicRepository.countBySubjectIdAndCompletionStatus(subject.getId(), "COMPLETED");
        dto.setTotalTopics(totalTopics);
        dto.setCompletedTopics(completedTopics);

        double pct = totalTopics > 0 ? (completedTopics * 100.0 / totalTopics) : 0.0;
        dto.setCompletionPercentage(Math.round(pct * 10.0) / 10.0);

        // Average marks percentage
        Double avgMarks = marksRepository.calculateSubjectAveragePercentage(subject.getId());
        dto.setAverageMarksPercentage(avgMarks != null ? Math.round(avgMarks * 10.0) / 10.0 : null);

        // Next upcoming assessment
        List<Assessment> upcoming = assessmentRepository.findNextUpcomingAssessmentForSubject(subject.getId(), LocalDateTime.now());
        if (!upcoming.isEmpty()) {
            Assessment next = upcoming.get(0);
            dto.setNextAssessmentTitle(next.getTitle() + " (" + next.getType() + ")");
            dto.setNextAssessmentDate(next.getAssessmentDateTime().toString());
            long days = Duration.between(LocalDateTime.now(), next.getAssessmentDateTime()).toDays();
            dto.setNextAssessmentDaysRemaining(Math.max(0, days));
        }

        return dto;
    }
}
