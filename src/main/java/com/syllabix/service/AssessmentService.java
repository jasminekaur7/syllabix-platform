package com.syllabix.service;

import com.syllabix.dto.AssessmentDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.AssessmentTopic;
import com.syllabix.entity.Marks;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.AssessmentTopicRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentTopicRepository assessmentTopicRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final MarksRepository marksRepository;

    public AssessmentService(AssessmentRepository assessmentRepository,
                             AssessmentTopicRepository assessmentTopicRepository,
                             SubjectRepository subjectRepository,
                             TopicRepository topicRepository,
                             MarksRepository marksRepository) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentTopicRepository = assessmentTopicRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.marksRepository = marksRepository;
    }

    public List<AssessmentDto> getAllAssessments(Student student) {
        List<Assessment> assessments = assessmentRepository.findByStudentIdOrderByAssessmentDateTimeAsc(student.getId());
        return assessments.stream().map(this::enrichAssessmentDto).collect(Collectors.toList());
    }

    public List<AssessmentDto> getUpcomingAssessments(Student student) {
        List<Assessment> upcoming = assessmentRepository.findUpcomingAssessments(student.getId(), LocalDateTime.now());
        return upcoming.stream().map(this::enrichAssessmentDto).collect(Collectors.toList());
    }

    public List<AssessmentDto> getAssessmentsForCalendar(Student student, LocalDateTime start, LocalDateTime end) {
        List<Assessment> list = assessmentRepository.findAssessmentsBetween(student.getId(), start, end);
        return list.stream().map(this::enrichAssessmentDto).collect(Collectors.toList());
    }

    public AssessmentDto getAssessmentById(Long id, Student student) {
        Assessment assessment = assessmentRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + id));

        return enrichAssessmentDto(assessment);
    }

    @Transactional
    public AssessmentDto createAssessment(AssessmentDto dto, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(dto.getSubjectId(), student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + dto.getSubjectId()));

        Assessment assessment = new Assessment(
                student,
                subject,
                dto.getTitle(),
                dto.getType(),
                dto.getAssessmentDateTime(),
                dto.getSyllabusCoverage(),
                dto.getMaxMarks(),
                dto.getPreparationStatus(),
                dto.getPriority(),
                dto.getNotes()
        );

        assessmentRepository.save(assessment);

        // Link topics if provided
        if (dto.getTopicIds() != null && !dto.getTopicIds().isEmpty()) {
            for (Long topicId : dto.getTopicIds()) {
                Optional<Topic> topicOpt = topicRepository.findByIdAndStudentId(topicId, student.getId());
                topicOpt.ifPresent(topic -> assessmentTopicRepository.save(new AssessmentTopic(assessment, topic)));
            }
        }

        return enrichAssessmentDto(assessment);
    }

    @Transactional
    public AssessmentDto updateAssessment(Long id, AssessmentDto dto, Student student) {
        Assessment assessment = assessmentRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + id));

        if (dto.getSubjectId() != null && !dto.getSubjectId().equals(assessment.getSubject().getId())) {
            Subject subject = subjectRepository.findByIdAndStudentId(dto.getSubjectId(), student.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + dto.getSubjectId()));
            assessment.setSubject(subject);
        }

        assessment.setTitle(dto.getTitle());
        if (dto.getType() != null) assessment.setType(dto.getType());
        assessment.setAssessmentDateTime(dto.getAssessmentDateTime());
        assessment.setSyllabusCoverage(dto.getSyllabusCoverage());
        if (dto.getMaxMarks() != null) assessment.setMaxMarks(dto.getMaxMarks());
        if (dto.getPreparationStatus() != null) assessment.setPreparationStatus(dto.getPreparationStatus());
        if (dto.getPriority() != null) assessment.setPriority(dto.getPriority());
        assessment.setNotes(dto.getNotes());

        assessmentRepository.save(assessment);

        if (dto.getTopicIds() != null) {
            assessmentTopicRepository.deleteByAssessmentId(assessment.getId());
            for (Long topicId : dto.getTopicIds()) {
                Optional<Topic> topicOpt = topicRepository.findByIdAndStudentId(topicId, student.getId());
                topicOpt.ifPresent(topic -> assessmentTopicRepository.save(new AssessmentTopic(assessment, topic)));
            }
        }

        return enrichAssessmentDto(assessment);
    }

    @Transactional
    public void deleteAssessment(Long id, Student student) {
        Assessment assessment = assessmentRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found: " + id));

        assessmentRepository.delete(assessment);
    }

    public AssessmentDto enrichAssessmentDto(Assessment a) {
        AssessmentDto dto = new AssessmentDto();
        dto.setId(a.getId());
        dto.setSubjectId(a.getSubject().getId());
        dto.setSubjectName(a.getSubject().getName());
        dto.setSubjectCode(a.getSubject().getCode());
        dto.setSubjectColor(a.getSubject().getColorHex());
        dto.setTitle(a.getTitle());
        dto.setType(a.getType());
        dto.setAssessmentDateTime(a.getAssessmentDateTime());
        dto.setSyllabusCoverage(a.getSyllabusCoverage());
        dto.setMaxMarks(a.getMaxMarks());
        dto.setPreparationStatus(a.getPreparationStatus());
        dto.setPriority(a.getPriority());
        dto.setNotes(a.getNotes());

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(now, a.getAssessmentDateTime());
        long daysRemaining = diff.toDays();
        dto.setDaysRemaining(daysRemaining);

        // Urgency color assignment
        if (a.getAssessmentDateTime().isBefore(now)) {
            dto.setUrgencyLevel("PAST");
        } else if (daysRemaining <= 3) {
            dto.setUrgencyLevel("URGENT"); // Red
        } else if (daysRemaining <= 7) {
            dto.setUrgencyLevel("APPROACHING"); // Orange
        } else {
            dto.setUrgencyLevel("SUFFICIENT"); // Green
        }

        // Linked marks if already entered
        List<Marks> marksList = marksRepository.findByStudentIdAndSubjectId(a.getStudent().getId(), a.getSubject().getId());
        for (Marks m : marksList) {
            if (m.getAssessment() != null && m.getAssessment().getId().equals(a.getId())) {
                dto.setMarksObtained(m.getMarksObtained());
                dto.setMarksPercentage(Math.round(m.getPercentage() * 10.0) / 10.0);
                break;
            }
        }

        // Linked topic IDs
        List<AssessmentTopic> atList = assessmentTopicRepository.findByAssessmentId(a.getId());
        dto.setTopicIds(atList.stream().map(at -> at.getTopic().getId()).collect(Collectors.toList()));

        return dto;
    }
}
