package com.syllabix.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subjects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "code"})
})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false)
    private Integer semester;

    @Column
    private Integer credits = 3;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "teacher_name", length = 100)
    private String teacherName;

    @Column(name = "target_grade", length = 10)
    private String targetGrade = "A";

    @Column(name = "color_hex", length = 20)
    private String colorHex = "#4f46e5";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Subject() {}

    public Subject(Student student, String name, String code, Integer semester, Integer credits, String description, String teacherName, String targetGrade, String colorHex) {
        this.student = student;
        this.name = name;
        this.code = code;
        this.semester = semester;
        this.credits = credits != null ? credits : 3;
        this.description = description;
        this.teacherName = teacherName;
        this.targetGrade = targetGrade != null ? targetGrade : "A";
        this.colorHex = colorHex != null ? colorHex : "#4f46e5";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getTargetGrade() { return targetGrade; }
    public void setTargetGrade(String targetGrade) { this.targetGrade = targetGrade; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
