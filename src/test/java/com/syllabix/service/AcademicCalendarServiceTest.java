package com.syllabix.service;

import com.syllabix.dto.AcademicCalendarWeekDto;
import com.syllabix.dto.ThaparDatesheetMilestoneDto;
import com.syllabix.dto.ThaparHolidayDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicCalendarServiceTest {

    private AcademicCalendarService calendarService;

    @BeforeEach
    void setUp() {
        calendarService = new AcademicCalendarService();
    }

    @Test
    @DisplayName("Verify official Thapar ODD SEM 2026-27 calendar structure (20 weeks + Diwali)")
    void testGetAllWeeks() {
        LocalDate refDate = LocalDate.of(2026, 11, 25);
        List<AcademicCalendarWeekDto> weeks = calendarService.getAllWeeks(refDate);

        assertNotNull(weeks);
        assertEquals(21, weeks.size(), "Should contain 20 semester weeks plus Diwali break");

        // Verify Week 9 is MST
        AcademicCalendarWeekDto w9 = weeks.stream().filter(w -> w.getWeekNumber() == 9).findFirst().orElse(null);
        assertNotNull(w9);
        assertEquals("MST", w9.getType());

        // Verify Week 18 is Assessment / Evaluation Week
        AcademicCalendarWeekDto w18 = weeks.stream().filter(w -> w.getWeekNumber() == 18).findFirst().orElse(null);
        assertNotNull(w18);
        assertEquals("EVALUATION", w18.getType());

        // Verify Week 19 is EST
        AcademicCalendarWeekDto w19 = weeks.stream().filter(w -> w.getWeekNumber() == 19).findFirst().orElse(null);
        assertNotNull(w19);
        assertEquals("EST", w19.getType());
    }

    @Test
    @DisplayName("Verify detection of current academic week for 25 Nov 2026 (Week 17)")
    void testGetCurrentWeek() {
        LocalDate today = LocalDate.of(2026, 11, 25);
        AcademicCalendarWeekDto current = calendarService.getCurrentWeek(today);

        assertNotNull(current);
        assertEquals(17, current.getWeekNumber());
        assertTrue(current.getIsCurrentWeek());
        assertEquals("TEACHING", current.getType());
    }

    @Test
    @DisplayName("Verify official Gazetted Holidays extracted from Thapar PDF")
    void testGetGazettedHolidays() {
        LocalDate ref = LocalDate.of(2026, 11, 25);
        List<ThaparHolidayDto> holidays = calendarService.getGazettedHolidays(ref);

        assertNotNull(holidays);
        assertEquals(6, holidays.size(), "Should have 6 official gazetted holidays");

        // Shaheedi Diwas on 14 Dec 2026 should be upcoming
        ThaparHolidayDto shaheediDiwas = holidays.stream()
                .filter(h -> h.getName().contains("Shaheedi Diwas"))
                .findFirst().orElse(null);
        assertNotNull(shaheediDiwas);
        assertEquals(LocalDate.of(2026, 12, 14), shaheediDiwas.getDate());
        assertEquals(19, shaheediDiwas.getDaysRemaining());
        assertFalse(shaheediDiwas.getIsPast());
    }

    @Test
    @DisplayName("Verify official Datesheet Milestones (MST, EST, Evaluation Week)")
    void testGetDatesheetMilestones() {
        LocalDate ref = LocalDate.of(2026, 11, 25);
        List<ThaparDatesheetMilestoneDto> milestones = calendarService.getDatesheetMilestones(ref);

        assertNotNull(milestones);
        assertTrue(milestones.size() >= 4);

        // EST should be upcoming in 12 days
        ThaparDatesheetMilestoneDto est = milestones.stream()
                .filter(m -> "EST".equals(m.getType()))
                .findFirst().orElse(null);
        assertNotNull(est);
        assertEquals(LocalDate.of(2026, 12, 7), est.getStartDate());
        assertEquals(12, est.getDaysUntil());
        assertEquals("UPCOMING", est.getStatus());
    }
}
