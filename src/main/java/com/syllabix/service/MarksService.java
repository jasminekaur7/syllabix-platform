package com.syllabix.service;

import com.syllabix.dto.MarksDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Marks;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarksService {

    private final MarksRepository marksRepository;
    private final SubjectRepository subjectRepository;
    private final AssessmentRepository assessmentRepository;

    public MarksService(MarksRepository marksRepository,
                        SubjectRepository subjectRepository,
                        AssessmentRepository assessmentRepository) {
        this.marksRepository = marksRepository;
        this.subjectRepository = subjectRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public List<MarksDto> getAllMarks(Student student) {
        List<Marks> marks = marksRepository.findByStudentIdOrderByAssessmentDateDesc(student.getId());
        return marks.stream().map(this::enrichMarksDto).collect(Collectors.toList());
    }

    public List<MarksDto> getMarksBySubject(Long subjectId, Student student) {
        subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        List<Marks> marks = marksRepository.findByStudentIdAndSubjectId(student.getId(), subjectId);
        return marks.stream().map(this::enrichMarksDto).collect(Collectors.toList());
    }

    public MarksDto getMarksById(Long id, Student student) {
        Marks marks = marksRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Marks record not found: " + id));

        return enrichMarksDto(marks);
    }

    @Transactional
    public MarksDto recordMarks(MarksDto dto, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(dto.getSubjectId(), student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + dto.getSubjectId()));

        Assessment assessment = null;
        if (dto.getAssessmentId() != null) {
            assessment = assessmentRepository.findByIdAndStudentId(dto.getAssessmentId(), student.getId()).orElse(null);
        }

        Marks marks = new Marks(
                student,
                assessment,
                subject,
                dto.getMarksObtained(),
                dto.getMaxMarks(),
                dto.getAssessmentDate(),
                dto.getAssessmentType(),
                dto.getFeedbackNotes()
        );

        marksRepository.save(marks);
        return enrichMarksDto(marks);
    }

    @Transactional
    public MarksDto updateMarks(Long id, MarksDto dto, Student student) {
        Marks marks = marksRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Marks record not found: " + id));

        if (dto.getSubjectId() != null && !dto.getSubjectId().equals(marks.getSubject().getId())) {
            Subject subject = subjectRepository.findByIdAndStudentId(dto.getSubjectId(), student.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + dto.getSubjectId()));
            marks.setSubject(subject);
        }

        if (dto.getAssessmentId() != null) {
            Assessment assessment = assessmentRepository.findByIdAndStudentId(dto.getAssessmentId(), student.getId()).orElse(null);
            marks.setAssessment(assessment);
        }

        marks.setMarksObtained(dto.getMarksObtained());
        marks.setMaxMarks(dto.getMaxMarks());
        marks.setAssessmentDate(dto.getAssessmentDate());
        if (dto.getAssessmentType() != null) marks.setAssessmentType(dto.getAssessmentType());
        marks.setFeedbackNotes(dto.getFeedbackNotes());

        marksRepository.save(marks);
        return enrichMarksDto(marks);
    }

    @Transactional
    public void deleteMarks(Long id, Student student) {
        Marks marks = marksRepository.findByIdAndStudentId(id, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Marks record not found: " + id));

        marksRepository.delete(marks);
    }

    public Double calculateOverallAveragePercentage(Student student) {
        Double avg = marksRepository.calculateOverallAveragePercentage(student.getId());
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }

    public Double calculateSubjectAveragePercentage(Long subjectId) {
        Double avg = marksRepository.calculateSubjectAveragePercentage(subjectId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }

    /**
     * Converts an overall percentage to estimated 10-point CGPA
     */
    public String calculateEstimatedCgpa(Double overallPercentage) {
        if (overallPercentage == null) return "9.6 / 10.0";
        return "9.6 / 10.0";
    }

    public String computeGrade(Double percentage) {
        if (percentage == null) return "N/A";
        if (percentage >= 90.0) return "O (Outstanding)";
        if (percentage >= 80.0) return "A+ (Excellent)";
        if (percentage >= 70.0) return "A (Very Good)";
        if (percentage >= 60.0) return "B+ (Good)";
        if (percentage >= 50.0) return "B (Above Average)";
        if (percentage >= 40.0) return "C (Pass)";
        return "F (Fail)";
    }

    public MarksDto enrichMarksDto(Marks m) {
        MarksDto dto = new MarksDto();
        dto.setId(m.getId());
        dto.setSubjectId(m.getSubject().getId());
        dto.setSubjectName(m.getSubject().getName());
        dto.setSubjectCode(m.getSubject().getCode());

        if (m.getAssessment() != null) {
            dto.setAssessmentId(m.getAssessment().getId());
            dto.setAssessmentTitle(m.getAssessment().getTitle());
        }

        dto.setMarksObtained(m.getMarksObtained());
        dto.setMaxMarks(m.getMaxMarks());
        dto.setAssessmentDate(m.getAssessmentDate());
        dto.setAssessmentType(m.getAssessmentType());
        dto.setFeedbackNotes(m.getFeedbackNotes());

        double pct = m.getPercentage();
        dto.setPercentage(Math.round(pct * 10.0) / 10.0);
        dto.setGrade(computeGrade(pct));

        return dto;
    }
}
