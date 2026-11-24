-- =====================================================================
-- SYLLABIX ACADEMIC PLATFORM - RELATIONAL DATABASE SCHEMA (MySQL 8)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS syllabix CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE syllabix;

-- 1. Student / User Account Entity
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    college VARCHAR(200),
    degree VARCHAR(100),
    branch VARCHAR(100),
    current_semester INT DEFAULT 1,
    academic_goals TEXT,
    daily_study_hours DOUBLE DEFAULT 3.0,
    preferred_study_timings VARCHAR(50) DEFAULT 'EVENING',
    weekly_availability JSON,
    role VARCHAR(30) DEFAULT 'ROLE_STUDENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Academic Subject Entity
CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL,
    semester INT NOT NULL,
    credits INT DEFAULT 3,
    description TEXT,
    teacher_name VARCHAR(100),
    target_grade VARCHAR(10) DEFAULT 'A',
    color_hex VARCHAR(20) DEFAULT '#4f46e5',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subject_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE KEY uk_student_subject_code (student_id, code),
    INDEX idx_subject_student (student_id),
    INDEX idx_subject_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Syllabus Unit / Chapter Entity
CREATE TABLE IF NOT EXISTS units (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    unit_number INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    CONSTRAINT fk_unit_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    INDEX idx_unit_subject (subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Syllabus Topic Entity
CREATE TABLE IF NOT EXISTS topics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    unit_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM', -- EASY, MEDIUM, HARD
    estimated_duration_minutes INT DEFAULT 45,
    completion_status VARCHAR(20) DEFAULT 'NOT_STARTED', -- NOT_STARTED, IN_PROGRESS, COMPLETED
    confidence_level INT DEFAULT 3, -- 1 to 5
    priority_score DOUBLE DEFAULT 0.0,
    last_studied_date DATE NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_topic_unit FOREIGN KEY (unit_id) REFERENCES units(id) ON DELETE CASCADE,
    CONSTRAINT fk_topic_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT fk_topic_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX idx_topic_subject (subject_id),
    INDEX idx_topic_status (completion_status),
    INDEX idx_topic_difficulty (difficulty),
    INDEX idx_topic_priority (priority_score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Academic Assessments & Examinations Entity
CREATE TABLE IF NOT EXISTS assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    type VARCHAR(30) NOT NULL, -- QUIZ, MST, EST, ASSIGNMENT, LAB, PROJECT, VIVA
    assessment_date_time DATETIME NOT NULL,
    syllabus_coverage TEXT,
    max_marks DOUBLE NOT NULL DEFAULT 100.0,
    preparation_status VARCHAR(30) DEFAULT 'NOT_STARTED', -- NOT_STARTED, PREPARING, READY
    priority VARCHAR(20) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, URGENT
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assessment_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_assessment_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    INDEX idx_assessment_student (student_id),
    INDEX idx_assessment_subject (subject_id),
    INDEX idx_assessment_date (assessment_date_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Many-to-Many Bridge: Assessment <-> Covered Topics
CREATE TABLE IF NOT EXISTS assessment_topics (
    assessment_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    PRIMARY KEY (assessment_id, topic_id),
    CONSTRAINT fk_at_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE,
    CONSTRAINT fk_at_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Assessment Marks Entity
CREATE TABLE IF NOT EXISTS marks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    assessment_id BIGINT NULL,
    subject_id BIGINT NOT NULL,
    marks_obtained DOUBLE NOT NULL,
    max_marks DOUBLE NOT NULL,
    assessment_date DATE NOT NULL,
    assessment_type VARCHAR(30) NOT NULL,
    feedback_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_marks_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_marks_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE SET NULL,
    CONSTRAINT fk_marks_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    INDEX idx_marks_student (student_id),
    INDEX idx_marks_subject (subject_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Study Tasks / Timetable Items
CREATE TABLE IF NOT EXISTS study_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    planned_date DATE NOT NULL,
    start_time TIME NULL,
    end_time TIME NULL,
    duration_minutes INT NOT NULL DEFAULT 45,
    task_type VARCHAR(30) DEFAULT 'NEW_LEARNING', -- NEW_LEARNING, REVISION, PRACTICE, EXAM_PREP
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    priority_score DOUBLE DEFAULT 0.0,
    rationale TEXT,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    INDEX idx_task_student_date (student_id, planned_date),
    INDEX idx_task_completed (is_completed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Real-Time Study Sessions (Pomodoro & Custom Tracker)
CREATE TABLE IF NOT EXISTS study_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    topic_id BIGINT NULL,
    study_task_id BIGINT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL,
    session_type VARCHAR(30) DEFAULT 'POMODORO', -- POMODORO, CUSTOM
    notes TEXT,
    rating INT DEFAULT 5, -- Productivity rating 1 to 5
    is_confirmed BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_session_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT fk_session_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE SET NULL,
    CONSTRAINT fk_session_task FOREIGN KEY (study_task_id) REFERENCES study_tasks(id) ON DELETE SET NULL,
    INDEX idx_session_student (student_id),
    INDEX idx_session_start (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Study Plans
CREATE TABLE IF NOT EXISTS study_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    daily_target_hours DOUBLE DEFAULT 3.0,
    total_sessions INT DEFAULT 0,
    status VARCHAR(30) DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, ARCHIVED
    algorithm_weights JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_plan_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX idx_plan_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Personalized Academic Insights & Risk Telemetry
CREATE TABLE IF NOT EXISTS academic_insights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL, -- RISK_ALERT, READINESS_SCORE, REVISION_OPPORTUNITY, STRENGTH_WEAKNESS
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(20) DEFAULT 'INFO', -- INFO, WARNING, CRITICAL, SUCCESS
    actionable_suggestion TEXT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_dismissed BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_insight_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX idx_insight_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
