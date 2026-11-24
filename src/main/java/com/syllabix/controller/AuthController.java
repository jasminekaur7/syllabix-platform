package com.syllabix.controller;

import com.syllabix.dto.AuthRequest;
import com.syllabix.dto.AuthResponse;
import com.syllabix.dto.PasswordResetRequest;
import com.syllabix.dto.RegisterRequest;
import com.syllabix.dto.StudentProfileDto;
import com.syllabix.entity.Student;
import com.syllabix.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request,
                                              HttpServletRequest httpRequest,
                                              HttpServletResponse httpResponse) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        httpRequest.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

        Student student = authenticationService.getAuthenticatedStudent();

        return ResponseEntity.ok(new AuthResponse(
                true,
                "Logged in successfully",
                student.getId(),
                student.getFullName(),
                student.getEmail()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return ResponseEntity.ok(new AuthResponse(true, "Logged out successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        AuthResponse response = authenticationService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<StudentProfileDto> getCurrentProfile() {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(authenticationService.getProfile(student));
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentProfileDto> updateProfile(@RequestBody StudentProfileDto dto) {
        Student student = authenticationService.getAuthenticatedStudent();
        return ResponseEntity.ok(authenticationService.updateProfile(student, dto));
    }
}
