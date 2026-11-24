package com.syllabix.service;

import com.syllabix.dto.AuthRequest;
import com.syllabix.dto.AuthResponse;
import com.syllabix.dto.PasswordResetRequest;
import com.syllabix.dto.RegisterRequest;
import com.syllabix.dto.StudentProfileDto;
import com.syllabix.entity.Student;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.exception.ValidationException;
import com.syllabix.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthenticationService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No student found with email: " + email));

        return new User(
                student.getEmail(),
                student.getPasswordHash(),
                Collections.singletonList(() -> student.getRole())
        );
    }

    public Student getAuthenticatedStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            // Default demo student fallback (supports Thapar Institute user and demo)
            return studentRepository.findByEmail("jkaur_be24@thapar.edu")
                    .orElseGet(() -> studentRepository.findByEmail("alex.student@university.edu")
                    .orElseGet(() -> studentRepository.findAll().stream().findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("No active student session found"))));
        }
        String email = auth.getName();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated student account not found: " + email));
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("An account with email " + request.getEmail() + " already exists");
        }

        Student student = new Student(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getCollege(),
                request.getDegree(),
                request.getBranch(),
                request.getCurrentSemester()
        );

        if (request.getDailyStudyHours() != null) {
            student.setDailyStudyHours(request.getDailyStudyHours());
        }
        if (request.getPreferredStudyTimings() != null) {
            student.setPreferredStudyTimings(request.getPreferredStudyTimings());
        }

        studentRepository.save(student);

        return new AuthResponse(
                true,
                "Student account registered successfully. Please proceed to log in.",
                student.getId(),
                student.getFullName(),
                student.getEmail()
        );
    }

    @Transactional
    public AuthResponse resetPassword(PasswordResetRequest request) {
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No student found with email: " + request.getEmail()));

        student.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        studentRepository.save(student);

        return new AuthResponse(true, "Password has been reset successfully. You may now log in with your new password.");
    }

    public StudentProfileDto getProfile(Student student) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setCollege(student.getCollege());
        dto.setDegree(student.getDegree());
        dto.setBranch(student.getBranch());
        dto.setCurrentSemester(student.getCurrentSemester());
        dto.setAcademicGoals(student.getAcademicGoals());
        dto.setDailyStudyHours(student.getDailyStudyHours());
        dto.setPreferredStudyTimings(student.getPreferredStudyTimings());
        dto.setWeeklyAvailability(student.getWeeklyAvailability());
        return dto;
    }

    @Transactional
    public StudentProfileDto updateProfile(Student student, StudentProfileDto dto) {
        student.setFullName(dto.getFullName());
        student.setCollege(dto.getCollege());
        student.setDegree(dto.getDegree());
        student.setBranch(dto.getBranch());
        student.setCurrentSemester(dto.getCurrentSemester());
        student.setAcademicGoals(dto.getAcademicGoals());
        if (dto.getDailyStudyHours() != null) {
            student.setDailyStudyHours(dto.getDailyStudyHours());
        }
        if (dto.getPreferredStudyTimings() != null) {
            student.setPreferredStudyTimings(dto.getPreferredStudyTimings());
        }
        if (dto.getWeeklyAvailability() != null) {
            student.setWeeklyAvailability(dto.getWeeklyAvailability());
        }

        studentRepository.save(student);
        return getProfile(student);
    }
}
