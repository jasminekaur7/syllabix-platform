package com.syllabix.service;

import com.syllabix.dto.StudySessionDto;
import com.syllabix.entity.Student;
import com.syllabix.entity.StudySession;
import com.syllabix.entity.StudyTask;
import com.syllabix.entity.Subject;
import com.syllabix.entity.Topic;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.repository.StudySessionRepository;
import com.syllabix.repository.StudyTaskRepository;
import com.syllabix.repository.SubjectRepository;
import com.syllabix.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final StudyTaskRepository studyTaskRepository;

    public StudySessionService(StudySessionRepository studySessionRepository,
                               SubjectRepository subjectRepository,
                               TopicRepository topicRepository,
                               StudyTaskRepository studyTaskRepository) {
        this.studySessionRepository = studySessionRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.studyTaskRepository = studyTaskRepository;
    }

    public List<StudySessionDto> getAllSessions(Student student) {
        List<StudySession> sessions = studySessionRepository.findByStudentIdOrderByStartTimeDesc(student.getId());
        return sessions.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public StudySessionDto logSession(StudySessionDto dto, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(dto.getSubjectId(), student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + dto.getSubjectId()));

        Topic topic = null;
        if (dto.getTopicId() != null) {
            topic = topicRepository.findByIdAndStudentId(dto.getTopicId(), student.getId()).orElse(null);
            if (topic != null) {
                topic.setLastStudiedDate(LocalDate.now());
                if ("NOT_STARTED".equalsIgnoreCase(topic.getCompletionStatus())) {
                    topic.setCompletionStatus("IN_PROGRESS");
                }
                topicRepository.save(topic);
            }
        }

        StudyTask task = null;
        if (dto.getStudyTaskId() != null) {
            task = studyTaskRepository.findByIdAndStudentId(dto.getStudyTaskId(), student.getId()).orElse(null);
            if (task != null) {
                task.setIsCompleted(true);
                task.setCompletedAt(LocalDateTime.now());
                studyTaskRepository.save(task);
            }
        }

        LocalDateTime start = dto.getStartTime() != null ? dto.getStartTime() : LocalDateTime.now().minusMinutes(dto.getDurationMinutes());
        LocalDateTime end = dto.getEndTime() != null ? dto.getEndTime() : LocalDateTime.now();

        StudySession session = new StudySession(
                student,
                subject,
                topic,
                task,
                start,
                end,
                dto.getDurationMinutes(),
                dto.getSessionType(),
                dto.getNotes(),
                dto.getRating()
        );

        session.setIsConfirmed(dto.getIsConfirmed() != null ? dto.getIsConfirmed() : true);
        studySessionRepository.save(session);
        return toDto(session);
    }

    public Integer calculateStudyStreakDays(Student student) {
        List<StudySession> sessions = studySessionRepository.findByStudentIdOrderByStartTimeDesc(student.getId());
        if (sessions.isEmpty()) return 0;

        Set<LocalDate> studyDates = sessions.stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsConfirmed()))
                .map(s -> s.getStartTime().toLocalDate())
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();
        LocalDate checkDate = studyDates.contains(today) ? today : today.minusDays(1);

        int streak = 0;
        while (studyDates.contains(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }

        return streak;
    }

    public Double getTodayStudyHours(Student student) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Long minutes = studySessionRepository.sumStudyMinutesSince(student.getId(), startOfDay);
        return minutes != null ? Math.round((minutes / 60.0) * 10.0) / 10.0 : 0.0;
    }

    public Double getWeeklyStudyHours(Student student) {
        LocalDateTime weekAgo = LocalDate.now().minusDays(7).atStartOfDay();
        Long minutes = studySessionRepository.sumStudyMinutesSince(student.getId(), weekAgo);
        return minutes != null ? Math.round((minutes / 60.0) * 10.0) / 10.0 : 0.0;
    }

    public StudySessionDto toDto(StudySession session) {
        StudySessionDto dto = new StudySessionDto();
        dto.setId(session.getId());
        dto.setSubjectId(session.getSubject().getId());
        dto.setSubjectName(session.getSubject().getName());
        dto.setSubjectColor(session.getSubject().getColorHex());
        if (session.getTopic() != null) {
            dto.setTopicId(session.getTopic().getId());
            dto.setTopicName(session.getTopic().getName());
        }
        if (session.getStudyTask() != null) {
            dto.setStudyTaskId(session.getStudyTask().getId());
        }
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setSessionType(session.getSessionType());
        dto.setNotes(session.getNotes());
        dto.setRating(session.getRating());
        dto.setIsConfirmed(session.getIsConfirmed());
        return dto;
    }
}
