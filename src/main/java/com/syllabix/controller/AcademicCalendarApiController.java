package com.syllabix.controller;

import com.syllabix.dto.AcademicCalendarWeekDto;
import com.syllabix.dto.ThaparDatesheetMilestoneDto;
import com.syllabix.dto.ThaparHolidayDto;
import com.syllabix.service.AcademicCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/academic-calendar")
public class AcademicCalendarApiController {

    private final AcademicCalendarService calendarService;

    public AcademicCalendarApiController(AcademicCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/current-week")
    public ResponseEntity<AcademicCalendarWeekDto> getCurrentWeek(
            @RequestParam(required = false) String date) {
        LocalDate refDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(calendarService.getCurrentWeek(refDate));
    }

    @GetMapping("/weeks")
    public ResponseEntity<List<AcademicCalendarWeekDto>> getAllWeeks(
            @RequestParam(required = false) String date) {
        LocalDate refDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(calendarService.getAllWeeks(refDate));
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<ThaparHolidayDto>> getHolidays(
            @RequestParam(required = false) String date) {
        LocalDate refDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(calendarService.getGazettedHolidays(refDate));
    }

    @GetMapping("/milestones")
    public ResponseEntity<List<ThaparDatesheetMilestoneDto>> getMilestones(
            @RequestParam(required = false) String date) {
        LocalDate refDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(calendarService.getDatesheetMilestones(refDate));
    }
}
