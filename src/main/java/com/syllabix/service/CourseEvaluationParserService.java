package com.syllabix.service;

import com.syllabix.dto.CourseEvaluationSchemeDto;
import com.syllabix.dto.EvaluationComponentDto;
import com.syllabix.entity.Assessment;
import com.syllabix.entity.Marks;
import com.syllabix.entity.Student;
import com.syllabix.entity.Subject;
import com.syllabix.exception.ResourceNotFoundException;
import com.syllabix.repository.AssessmentRepository;
import com.syllabix.repository.MarksRepository;
import com.syllabix.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CourseEvaluationParserService {

    private final SubjectRepository subjectRepository;
    private final AssessmentRepository assessmentRepository;
    private final MarksRepository marksRepository;

    public CourseEvaluationParserService(SubjectRepository subjectRepository,
                                         AssessmentRepository assessmentRepository,
                                         MarksRepository marksRepository) {
        this.subjectRepository = subjectRepository;
        this.assessmentRepository = assessmentRepository;
        this.marksRepository = marksRepository;
    }

    /**
     * Parses raw text copied directly from Thapar-LMS / Moodle course evaluation section.
     * Example input from Thapar LMS (Image 3):
     * "• LAB Evaluation : 20 Marks (Week 13, Dec., 2026 in scheduled LABs)
     *  • Sessional Quiz : 10 Marks (Week 12, Oct 15, 2026), syllabus: same as MST
     *  • Sessional Make up Quiz : 10 Marks (Week 13, Dec 4, 2026; 4:30 PM, syllabus: upto EST)
     *  • MST : 30 Marks
     *  • EST : 40 Marks"
     */
    public CourseEvaluationSchemeDto parseAndAnalyze(Long subjectId, String rawText, String targetGrade, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        if (targetGrade == null || targetGrade.trim().isEmpty()) {
            targetGrade = subject.getTargetGrade() != null ? subject.getTargetGrade() : "A";
        }

        CourseEvaluationSchemeDto dto = new CourseEvaluationSchemeDto();
        dto.setSubjectId(subject.getId());
        dto.setSubjectName(subject.getName());
        dto.setSubjectCode(subject.getCode());
        dto.setRawSchemeText(rawText);
        dto.setTargetGrade(targetGrade);

        double targetCutoff = getCutoffPercentageForGrade(targetGrade);
        dto.setTargetGradeCutoffPercentage(targetCutoff);

        List<EvaluationComponentDto> components = parseRawTextIntoComponents(rawText);
        if (components.isEmpty()) {
            // Default Thapar University Engineering standard evaluation scheme if input is empty
            components = getDefaultThaparEvaluationComponents();
        }

        dto.setComponents(components);

        double totalMax = components.stream().mapToDouble(EvaluationComponentDto::getMaxMarks).sum();
        dto.setTotalMarks(totalMax > 0 ? totalMax : 100.0);

        // Fetch marks recorded so far for this subject to compute current real-time score
        List<Marks> recordedMarks = marksRepository.findByStudentIdAndSubjectId(student.getId(), subjectId);
        double scoredSoFar = 0.0;
        double completedMax = 0.0;

        for (EvaluationComponentDto comp : components) {
            // Try to match with existing recorded marks
            for (Marks m : recordedMarks) {
                String aTitle = m.getAssessment() != null ? m.getAssessment().getTitle() : "";
                if (comp.getComponentType().equalsIgnoreCase(m.getAssessmentType()) ||
                        (!aTitle.isEmpty() && comp.getTitle().toLowerCase().contains(aTitle.toLowerCase()))) {
                    comp.setObtainedMarks(m.getMarksObtained());
                    comp.setIsCompleted(true);
                    scoredSoFar += m.getMarksObtained();
                    completedMax += m.getMaxMarks();
                    break;
                }
            }
        }

        dto.setScoredMarksSoFar(Math.round(scoredSoFar * 10.0) / 10.0);
        dto.setCompletedComponentsMax(Math.round(completedMax * 10.0) / 10.0);

        double remainingMarks = Math.max(0.0, dto.getTotalMarks() - completedMax);
        dto.setRemainingMarks(Math.round(remainingMarks * 10.0) / 10.0);

        // Required Run-Rate calculation for Target Grade
        double targetAbsoluteMarks = (dto.getTotalMarks() * targetCutoff) / 100.0;
        double neededOnRemaining = Math.max(0.0, targetAbsoluteMarks - scoredSoFar);
        dto.setNeededMarksOnRemaining(Math.round(neededOnRemaining * 10.0) / 10.0);

        double runRate = remainingMarks > 0 ? (neededOnRemaining / remainingMarks) * 100.0 : 0.0;
        runRate = Math.round(runRate * 10.0) / 10.0;
        dto.setRequiredRunRatePercentage(runRate);

        // Feasibility Verdict & Personalized Advice
        List<String> adviceList = new ArrayList<>();
        if (remainingMarks <= 0) {
            dto.setFeasibilityStatus("EVALUATION_CONCLUDED");
            dto.setPersonalizedAdvice(String.format("All components completed. Total score: %.1f / %.1f (%.1f%%).",
                    scoredSoFar, dto.getTotalMarks(), (scoredSoFar / dto.getTotalMarks()) * 100.0));
        } else if (runRate > 100.0) {
            dto.setFeasibilityStatus("IMPOSSIBLE");
            dto.setPersonalizedAdvice(String.format("Target Grade %s (%.0f%%) is mathematically out of reach even with 100%% marks on remaining exams. Consider setting a target of Grade %s.",
                    targetGrade, targetCutoff, getNextLowerGrade(targetGrade)));
        } else if (runRate >= 88.0) {
            dto.setFeasibilityStatus("CRITICAL_EFFORT");
            dto.setPersonalizedAdvice(String.format("Grade %s requires extraordinary precision: %.1f%% average across upcoming assessments (%.1f / %.1f marks).",
                    targetGrade, runRate, neededOnRemaining, remainingMarks));
        } else if (runRate >= 75.0) {
            dto.setFeasibilityStatus("STRETCH");
            dto.setPersonalizedAdvice(String.format("Grade %s is achievable with focused preparation: you need %.1f / %.1f marks (%.1f%% run rate).",
                    targetGrade, neededOnRemaining, remainingMarks, runRate));
        } else {
            dto.setFeasibilityStatus("ACHIEVABLE");
            dto.setPersonalizedAdvice(String.format("You are in a strong position for Grade %s! You need %.1f / %.1f marks (%.1f%% run rate) across remaining evaluations.",
                    targetGrade, neededOnRemaining, remainingMarks, runRate));
        }

        // Generate Component-Specific Advice
        for (EvaluationComponentDto comp : components) {
            if (!comp.getIsCompleted()) {
                double suggestedScore = (comp.getMaxMarks() * Math.min(100.0, runRate)) / 100.0;
                suggestedScore = Math.round(suggestedScore * 10.0) / 10.0;
                adviceList.add(String.format("%s: Aim for at least %.1f / %.1f marks (%s)",
                        comp.getTitle(), suggestedScore, comp.getMaxMarks(), comp.getScheduleDetails() != null ? comp.getScheduleDetails() : "Upcoming"));
            }
        }
        dto.setBreakdownAdvice(adviceList);

        return dto;
    }

    @Transactional
    public List<Assessment> persistParsedAssessments(Long subjectId, CourseEvaluationSchemeDto scheme, Student student) {
        Subject subject = subjectRepository.findByIdAndStudentId(subjectId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));

        List<Assessment> created = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        int dayOffset = 5; // Stagger dates realistically for MST, Lab, Quiz, EST
        for (EvaluationComponentDto comp : scheme.getComponents()) {
            LocalDateTime examDate;
            String urgency;
            if ("MST".equalsIgnoreCase(comp.getComponentType())) {
                examDate = now.plusDays(5).withHour(10).withMinute(0);
                urgency = "URGENT";
            } else if ("EST".equalsIgnoreCase(comp.getComponentType())) {
                examDate = now.plusDays(25).withHour(9).withMinute(30);
                urgency = "SUFFICIENT";
            } else if ("LAB".equalsIgnoreCase(comp.getComponentType())) {
                examDate = now.plusDays(12).withHour(14).withMinute(0);
                urgency = "HIGH";
            } else {
                examDate = now.plusDays(dayOffset).withHour(11).withMinute(30);
                urgency = "HIGH";
                dayOffset += 7;
            }

            Assessment assessment = new Assessment();
            assessment.setStudent(student);
            assessment.setSubject(subject);
            assessment.setTitle(comp.getTitle());
            assessment.setType(comp.getComponentType());
            assessment.setAssessmentDateTime(examDate);
            assessment.setMaxMarks(comp.getMaxMarks());
            assessment.setSyllabusCoverage(comp.getSyllabusCoverage() != null ? comp.getSyllabusCoverage() : "According to course outline");
            assessment.setPreparationStatus(comp.getIsCompleted() ? "COMPLETED" : "PREPARING");
            assessment.setPriority(urgency);
            assessment.setNotes(comp.getScheduleDetails() != null ? comp.getScheduleDetails() : "Imported from Thapar LMS Course Evaluations");

            created.add(assessmentRepository.save(assessment));
        }

        return created;
    }

    private List<EvaluationComponentDto> parseRawTextIntoComponents(String rawText) {
        List<EvaluationComponentDto> components = new ArrayList<>();
        if (rawText == null || rawText.trim().isEmpty()) {
            return components;
        }

        String[] lines = rawText.split("\\r?\\n");
        // Regex pattern matches: e.g. "LAB Evaluation : 20 Marks (Week 13...)"
        Pattern markPattern = Pattern.compile("(?i)(LAB|Quiz|Sessional|MST|EST|Assignment|Project)[^:\\d]*:?\\s*(\\d+(\\.\\d+)?)\\s*(Marks|marks|M)?");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            Matcher matcher = markPattern.matcher(trimmed);
            if (matcher.find()) {
                String keyword = matcher.group(1).toUpperCase();
                double marks = Double.parseDouble(matcher.group(2));

                String compType = "OTHER";
                if (keyword.contains("LAB")) compType = "LAB";
                else if (keyword.contains("QUIZ") || keyword.contains("SESSIONAL")) compType = "QUIZ";
                else if (keyword.contains("MST")) compType = "MST";
                else if (keyword.contains("EST")) compType = "EST";
                else if (keyword.contains("ASSIGNMENT")) compType = "ASSIGNMENT";

                String title = cleanLineTitle(trimmed);
                String schedule = extractParentheses(trimmed);
                String syllabus = extractSyllabusNote(trimmed);

                components.add(new EvaluationComponentDto(title, compType, marks, marks, schedule, syllabus));
            }
        }

        return components;
    }

    private List<EvaluationComponentDto> getDefaultThaparEvaluationComponents() {
        List<EvaluationComponentDto> list = new ArrayList<>();
        list.add(new EvaluationComponentDto("LAB Evaluation", "LAB", 20.0, 20.0, "Week 13 in scheduled LABs", "All semester lab exercises"));
        list.add(new EvaluationComponentDto("Sessional Quiz", "QUIZ", 10.0, 10.0, "Week 12 (4:30 PM)", "Same syllabus as MST"));
        list.add(new EvaluationComponentDto("Mid-Semester Test (MST)", "MST", 30.0, 30.0, "Semester Week 8", "Units 1, 2, and 3"));
        list.add(new EvaluationComponentDto("End-Semester Examination (EST)", "EST", 40.0, 40.0, "Semester Finals", "Comprehensive (Units 1-5)"));
        return list;
    }

    private String cleanLineTitle(String line) {
        String cleaned = line.replaceAll("^[•\\-*\\s]+", "");
        int colonIdx = cleaned.indexOf(':');
        if (colonIdx > 0) {
            return cleaned.substring(0, colonIdx).trim();
        }
        int parenIdx = cleaned.indexOf('(');
        if (parenIdx > 0) {
            return cleaned.substring(0, parenIdx).trim();
        }
        return cleaned;
    }

    private String extractParentheses(String line) {
        int start = line.indexOf('(');
        int end = line.lastIndexOf(')');
        if (start >= 0 && end > start) {
            return line.substring(start + 1, end).trim();
        }
        return null;
    }

    private String extractSyllabusNote(String line) {
        Pattern pattern = Pattern.compile("(?i)syllabus\\s*:\\s*([^\\)\\n]+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private double getCutoffPercentageForGrade(String grade) {
        if (grade == null) return 80.0;
        switch (grade.toUpperCase()) {
            case "A+": return 88.0;
            case "A":  return 80.0;
            case "A-": return 73.0;
            case "B":  return 65.0;
            case "B-": return 58.0;
            case "C":  return 50.0;
            case "C-": return 45.0;
            case "D":  return 40.0;
            default:   return 80.0;
        }
    }

    private String getNextLowerGrade(String grade) {
        if ("A+".equalsIgnoreCase(grade)) return "A";
        if ("A".equalsIgnoreCase(grade)) return "A-";
        if ("A-".equalsIgnoreCase(grade)) return "B";
        if ("B".equalsIgnoreCase(grade)) return "B-";
        return "C";
    }
}
