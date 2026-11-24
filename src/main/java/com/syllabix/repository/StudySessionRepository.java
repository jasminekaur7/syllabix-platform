package com.syllabix.repository;

import com.syllabix.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    List<StudySession> findByStudentId(Long studentId);
    List<StudySession> findByStudentIdOrderByStartTimeDesc(Long studentId);
    List<StudySession> findBySubjectId(Long subjectId);
    Optional<StudySession> findByIdAndStudentId(Long id, Long studentId);

    @Query("SELECT s FROM StudySession s WHERE s.student.id = :studentId AND s.startTime >= :since ORDER BY s.startTime DESC")
    List<StudySession> findRecentSessions(@Param("studentId") Long studentId, @Param("since") LocalDateTime since);

    @Query("SELECT SUM(s.durationMinutes) FROM StudySession s WHERE s.student.id = :studentId AND s.startTime >= :since")
    Long sumStudyMinutesSince(@Param("studentId") Long studentId, @Param("since") LocalDateTime since);

    @Query("SELECT s.subject.name, SUM(s.durationMinutes) FROM StudySession s WHERE s.student.id = :studentId GROUP BY s.subject.name")
    List<Object[]> getStudyTimePerSubject(@Param("studentId") Long studentId);
}
