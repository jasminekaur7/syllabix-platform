package com.syllabix.service;

import com.syllabix.dto.AcademicCalendarWeekDto;
import com.syllabix.dto.ThaparDatesheetMilestoneDto;
import com.syllabix.dto.ThaparHolidayDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AcademicCalendarService {

    /**
     * Builds the complete official 20-week schedule for Thapar Institute ODD SEM 2026-27
     * exactly as specified in the official Academic Calendar PDF.
     */
    public List<AcademicCalendarWeekDto> getAllWeeks(LocalDate today) {
        if (today == null) today = LocalDate.now();

        List<AcademicCalendarWeekDto> weeks = new ArrayList<>();

        // Week 1 (July-Aug): 27 Jul - 31 Jul
        weeks.add(createWeek(1, "Week -1 (July-Aug)", LocalDate.of(2026, 7, 27), LocalDate.of(2026, 7, 31), "TEACHING", "Teaching (Semester Commencement)", today));

        // Week 2 (Aug): 3 Aug - 7 Aug
        weeks.add(createWeek(2, "Week -2 (Aug)", LocalDate.of(2026, 8, 3), LocalDate.of(2026, 8, 7), "TEACHING", "Teaching", today));

        // Week 3 (Aug): 10 Aug - 14 Aug
        weeks.add(createWeek(3, "Week -3 (Aug)", LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 14), "TEACHING", "Teaching", today));

        // Week 4 (Aug): 17 Aug - 21 Aug
        weeks.add(createWeek(4, "Week -4 (Aug)", LocalDate.of(2026, 8, 17), LocalDate.of(2026, 8, 21), "TEACHING", "Teaching", today));

        // Week 5 (Aug): 24 Aug - 28 Aug
        weeks.add(createWeek(5, "Week -5 (Aug)", LocalDate.of(2026, 8, 24), LocalDate.of(2026, 8, 28), "TEACHING", "Teaching", today));

        // Week 6 (Sept): 31 Aug - 04 Sep (04/09 Janamashtmi Holiday)
        AcademicCalendarWeekDto w6 = createWeek(6, "Week -6 (Sept)", LocalDate.of(2026, 8, 31), LocalDate.of(2026, 9, 4), "TEACHING", "Teaching", today);
        w6.setNotes(Collections.singletonList("04/09/2026: Janamashtmi (Holiday)"));
        weeks.add(w6);

        // Week 7 (Sept): 7 Sep - 11 Sep
        weeks.add(createWeek(7, "Week -7 (Sept)", LocalDate.of(2026, 9, 7), LocalDate.of(2026, 9, 11), "TEACHING", "Teaching", today));

        // Week 8 (Sept): 14 Sep - 18 Sep
        weeks.add(createWeek(8, "Week -8 (Sept)", LocalDate.of(2026, 9, 14), LocalDate.of(2026, 9, 18), "TEACHING", "Teaching & Pre-MST Revision", today));

        // Week 9 (Sept): 21 Sep - 26 Sep (MST Week 1)
        AcademicCalendarWeekDto w9 = createWeek(9, "Week -9 (Sept)", LocalDate.of(2026, 9, 21), LocalDate.of(2026, 9, 26), "MST", "Mid-Semester Tests (MST - Part 1)", today);
        w9.setNotes(Collections.singletonList("Mid-Semester Examinations across scheduled slots"));
        weeks.add(w9);

        // Week 10 (Sept-Oct): 28 Sep - 03 Oct (MST Week 2, 02/10 Gandhi Jayanti)
        AcademicCalendarWeekDto w10 = createWeek(10, "Week -10 (Sept-Oct)", LocalDate.of(2026, 9, 28), LocalDate.of(2026, 10, 3), "MST", "Mid-Semester Tests (MST - Part 2)", today);
        w10.setNotes(Collections.singletonList("02/10/2026: Gandhi Jayanti (Holiday)"));
        weeks.add(w10);

        // Week 11 (Oct): 5 Oct - 9 Oct
        weeks.add(createWeek(11, "Week -11 (Oct)", LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 9), "TEACHING", "Teaching (Post-MST Curriculum)", today));

        // Week 12 (Oct): 12 Oct - 16 Oct
        AcademicCalendarWeekDto w12 = createWeek(12, "Week -12 (Oct)", LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 16), "TEACHING", "Teaching", today);
        w12.setNotes(Collections.singletonList("Week 12 (15 Oct): Sessional Quiz 1 (10 Marks, same as MST syllabus)"));
        weeks.add(w12);

        // Week 13 (Oct): 19 Oct - 23 Oct (19-NT, 20-H Dussehra)
        AcademicCalendarWeekDto w13 = createWeek(13, "Week -13 (Oct)", LocalDate.of(2026, 10, 19), LocalDate.of(2026, 10, 23), "TEACHING", "Teaching", today);
        w13.setNotes(Arrays.asList("19/10/2026: Non-Teaching Day (Compensated on 31st Oct)", "20/10/2026: Dussehra (Holiday)"));
        weeks.add(w13);

        // Week 14 (Oct): 26 Oct - 31 Oct (26-H Balmiki Ji, 31st Teaching in lieu of 19th Oct)
        AcademicCalendarWeekDto w14 = createWeek(14, "Week -14 (Oct)", LocalDate.of(2026, 10, 26), LocalDate.of(2026, 10, 31), "TEACHING", "Teaching", today);
        w14.setNotes(Arrays.asList("26/10/2026: Birthday of Maharishi Balmiki Ji (Holiday)", "31/10/2026: Teaching Day in lieu of 19th Oct with Monday's Timetable"));
        weeks.add(w14);

        // Week 15 (Nov): 2 Nov - 6 Nov
        weeks.add(createWeek(15, "Week -15 (Nov)", LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 6), "TEACHING", "Teaching", today));

        // NT (Non-Teaching) Week: 9 Nov - 13 Nov (Diwali Break)
        AcademicCalendarWeekDto diwali = createWeek(0, "Diwali Break (Nov)", LocalDate.of(2026, 11, 9), LocalDate.of(2026, 11, 13), "NON_TEACHING_BREAK", "Diwali Break (Non-Teaching)", today);
        diwali.setNotes(Collections.singletonList("09/11/2026 to 13/11/2026: University Holiday / Diwali Break"));
        weeks.add(diwali);

        // Week 16 (Nov): 16 Nov - 20 Nov
        weeks.add(createWeek(16, "Week -16 (Nov)", LocalDate.of(2026, 11, 16), LocalDate.of(2026, 11, 20), "TEACHING", "Teaching & Lab Catchup", today));

        // Week 17 (Nov): 23 Nov - 28 Nov (23-NT, 24-H Gurupurab, 28th Teaching in lieu of 23rd Nov)
        AcademicCalendarWeekDto w17 = createWeek(17, "Week -17 (Nov)", LocalDate.of(2026, 11, 23), LocalDate.of(2026, 11, 28), "TEACHING", "Teaching (Current Week)", today);
        w17.setNotes(Arrays.asList("23/11/2026: Non-Teaching Day (Compensated on 28th Nov)", "24/11/2026: Gurupurab (Guru Nanak Dev Ji) (Holiday)", "28/11/2026: Teaching Day in lieu of 23rd Nov with Monday's Timetable"));
        weeks.add(w17);

        // Week 18 (Dec): 30 Nov - 05 Dec (Assessment / Evaluation week)
        AcademicCalendarWeekDto w18 = createWeek(18, "Week -18 (Dec)", LocalDate.of(2026, 11, 30), LocalDate.of(2026, 12, 5), "EVALUATION", "Assessment / Lab Evaluation Week", today);
        w18.setNotes(Arrays.asList("30 Nov - 05 Dec: LAB Evaluations & End-Term Submissions", "04/12/2026 (4:30 PM): Sessional Make up Quiz (10 Marks, syllabus upto EST)"));
        weeks.add(w18);

        // Week 19 (Dec): 07 Dec - 12 Dec (EST Week 1)
        AcademicCalendarWeekDto w19 = createWeek(19, "Week -19 (Dec)", LocalDate.of(2026, 12, 7), LocalDate.of(2026, 12, 12), "EST", "End-Semester Examinations (EST - Part 1)", today);
        w19.setNotes(Collections.singletonList("Comprehensive End-Semester Theory Examinations (Units 1 to 5)"));
        weeks.add(w19);

        // Week 20 (Dec): 14 Dec - 19 Dec (EST Week 2, 14-H Shaheedi Diwas)
        AcademicCalendarWeekDto w20 = createWeek(20, "Week -20 (Dec)", LocalDate.of(2026, 12, 14), LocalDate.of(2026, 12, 19), "EST", "End-Semester Examinations (EST - Part 2)", today);
        w20.setNotes(Arrays.asList("14/12/2026: Shaheedi Diwas (Guru Teg Bahadur Ji) (Holiday)", "17-18 Dec 2026: Showing evaluated EST answer sheets (Slot 1)"));
        weeks.add(w20);

        return weeks;
    }

    public AcademicCalendarWeekDto getCurrentWeek(LocalDate today) {
        if (today == null) today = LocalDate.now();

        List<AcademicCalendarWeekDto> all = getAllWeeks(today);
        for (AcademicCalendarWeekDto w : all) {
            if (!today.isBefore(w.getStartDate()) && !today.isAfter(w.getEndDate())) {
                w.setIsCurrentWeek(true);
                return w;
            }
        }

        // If today is outside or between weekends, find closest upcoming or default to Week 17
        for (AcademicCalendarWeekDto w : all) {
            if (today.isBefore(w.getStartDate())) {
                return w;
            }
        }

        return all.get(all.size() - 1);
    }

    public List<ThaparHolidayDto> getGazettedHolidays(LocalDate today) {
        if (today == null) today = LocalDate.now();

        List<ThaparHolidayDto> holidays = new ArrayList<>();
        holidays.add(createHoliday(LocalDate.of(2026, 9, 4), "Janamashtmi", "Lord Krishna Janmashtami Gazetted Holiday", today));
        holidays.add(createHoliday(LocalDate.of(2026, 10, 2), "Gandhi Jayanti", "National Holiday honoring Mahatma Gandhi", today));
        holidays.add(createHoliday(LocalDate.of(2026, 10, 20), "Dussehra", "Vijayadashami Gazetted Holiday", today));
        holidays.add(createHoliday(LocalDate.of(2026, 10, 26), "Birthday of Maharishi Balmiki Ji", "Gazetted Holiday", today));
        holidays.add(createHoliday(LocalDate.of(2026, 11, 24), "Gurupurab (Guru Nanak Dev Ji)", "Prakash Utsav Gazetted Holiday", today));
        holidays.add(createHoliday(LocalDate.of(2026, 12, 14), "Shaheedi Diwas (Guru Teg Bahadur Ji)", "Martyrdom Day of Guru Tegh Bahadur Ji", today));

        return holidays;
    }

    public List<ThaparDatesheetMilestoneDto> getDatesheetMilestones(LocalDate today) {
        if (today == null) today = LocalDate.now();

        List<ThaparDatesheetMilestoneDto> list = new ArrayList<>();

        list.add(createMilestone("Mid-Semester Tests (MST)", "Weeks 9 & 10",
                LocalDate.of(2026, 9, 21), LocalDate.of(2026, 10, 3), "MST",
                "Mid-term evaluations (30 Marks weightage). Covers syllabus Units 1 to 3.", today));

        list.add(createMilestone("Diwali Break (Non-Teaching)", "Break",
                LocalDate.of(2026, 11, 9), LocalDate.of(2026, 11, 13), "BREAK",
                "Diwali university vacation.", today));

        list.add(createMilestone("Assessment / Lab Evaluation Week", "Week 18",
                LocalDate.of(2026, 11, 30), LocalDate.of(2026, 12, 5), "EVALUATION",
                "Internal assessment freeze, final lab evaluations (20 Marks), and sessional make-up quizzes.", today));

        list.add(createMilestone("End-Semester Examinations (EST)", "Weeks 19 & 20",
                LocalDate.of(2026, 12, 7), LocalDate.of(2026, 12, 19), "EST",
                "Final theory examinations (40-50 Marks weightage). Comprehensive syllabus (Units 1 to 5).", today));

        list.add(createMilestone("Evaluated EST Answer Sheet Review", "Post-Exam",
                LocalDate.of(2026, 12, 17), LocalDate.of(2027, 1, 5), "REVIEW",
                "Official window for showing evaluated EST answer sheets: 17-18 Dec 2026 & 04-05 Jan 2027.", today));

        return list;
    }

    private AcademicCalendarWeekDto createWeek(int num, String label, LocalDate start, LocalDate end, String type, String desc, LocalDate today) {
        AcademicCalendarWeekDto dto = new AcademicCalendarWeekDto(num, label, start, end, type, desc);
        if (!today.isBefore(start) && !today.isAfter(end)) {
            dto.setIsCurrentWeek(true);
        }
        return dto;
    }

    private ThaparHolidayDto createHoliday(LocalDate date, String name, String desc, LocalDate today) {
        ThaparHolidayDto dto = new ThaparHolidayDto(date, name, desc);
        long days = ChronoUnit.DAYS.between(today, date);
        dto.setDaysRemaining(days);
        dto.setIsPast(days < 0);
        return dto;
    }

    private ThaparDatesheetMilestoneDto createMilestone(String title, String weekRange, LocalDate start, LocalDate end, String type, String details, LocalDate today) {
        ThaparDatesheetMilestoneDto dto = new ThaparDatesheetMilestoneDto(title, weekRange, start, end, type, details);
        long daysUntil = ChronoUnit.DAYS.between(today, start);
        dto.setDaysUntil(daysUntil);

        if (today.isBefore(start)) {
            dto.setStatus("UPCOMING");
        } else if (!today.isAfter(end)) {
            dto.setStatus("IN_PROGRESS");
        } else {
            dto.setStatus("COMPLETED");
        }
        return dto;
    }
}
