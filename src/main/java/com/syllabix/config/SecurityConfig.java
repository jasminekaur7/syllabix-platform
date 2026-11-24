package com.syllabix.config;

import com.syllabix.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public DaoAuthenticationProvider authenticationProvider(AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authenticationService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabled for REST APIs
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Enable H2 console / PDF frames
            .authorizeHttpRequests(auth -> auth
                // Static resources & UI pages
                .requestMatchers(
                    "/", "/index.html", "/login", "/login.html", "/register", "/register.html",
                    "/subjects", "/subjects.html", "/syllabus", "/syllabus.html",
                    "/calendar", "/calendar.html", "/planner", "/planner.html",
                    "/analytics", "/analytics.html", "/sessions", "/sessions.html",
                    "/insights", "/insights.html", "/profile", "/profile.html",
                    "/css/**", "/js/**", "/images/**", "/favicon.ico", "/h2-console/**",
                    "/api/auth/**"
                ).permitAll()
                // All other API endpoints
                .requestMatchers("/api/**").permitAll() // Permits frictionless demo testing while retaining session-aware identity
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                .permitAll()
            );

        return http.build();
    }
}
