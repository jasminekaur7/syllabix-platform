package com.syllabix.config;

import com.syllabix.entity.*;
import com.syllabix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final UnitRepository unitRepository;
    private final TopicRepository topicRepository;
    private final AssessmentRepository assessmentRepository;
    private final MarksRepository marksRepository;
    private final StudyTaskRepository studyTaskRepository;
    private final StudySessionRepository studySessionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           UnitRepository unitRepository,
                           TopicRepository topicRepository,
                           AssessmentRepository assessmentRepository,
                           MarksRepository marksRepository,
                           StudyTaskRepository studyTaskRepository,
                           StudySessionRepository studySessionRepository,
                           PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.unitRepository = unitRepository;
        this.topicRepository = topicRepository;
        this.assessmentRepository = assessmentRepository;
        this.marksRepository = marksRepository;
        this.studyTaskRepository = studyTaskRepository;
        this.studySessionRepository = studySessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Ensure Jasmine Kaur exists and has curriculum
        Student jasmine = studentRepository.findByEmail("jkaur_be24@thapar.edu").orElse(null);
        if (jasmine == null) {
            jasmine = new Student(
                    "Jasmine Kaur",
                    "jkaur_be24@thapar.edu",
                    passwordEncoder.encode("password123"),
                    "Thapar Institute of Engineering & Technology, Patiala",
                    "Bachelor of Engineering (B.E.)",
                    "Computer Engineering (COE)",
                    5
            );
            jasmine.setAcademicGoals("Target CGPA >= 9.25 in 2627ODDSEM (Third Year). Excel in Computer Architecture (UCS510), Enterprise Web App (UCS553), Machine Learning (UML501), Data Science (UCS548), Image Processing (UCS615), Ethics (UCS421), and Software Engineering (UCS503).");
            jasmine.setDailyStudyHours(3.5);
            jasmine.setPreferredStudyTimings("EVENING");
            jasmine.setWeeklyAvailability("{\"Monday\":3.5,\"Tuesday\":3.5,\"Wednesday\":4.0,\"Thursday\":3.5,\"Friday\":3.0,\"Saturday\":5.0,\"Sunday\":4.5}");
            jasmine = studentRepository.save(jasmine);
            seedCurriculumForStudent(jasmine);
        } else {
            List<Subject> existingSubjects = subjectRepository.findByStudentId(jasmine.getId());
            boolean hasUcs510 = existingSubjects.stream().anyMatch(s -> "UCS510".equalsIgnoreCase(s.getCode()));
            if (!hasUcs510 || existingSubjects.size() < 7) {
                seedCurriculumForStudent(jasmine);
            }
        }

        // 2. Ensure Akshat Gupta (agupta8_be24@thapar.edu / admin@1234) exists
        Student akshat = studentRepository.findByEmail("agupta8_be24@thapar.edu").orElse(null);
        if (akshat == null) {
            akshat = new Student(
                    "Akshat Gupta",
                    "agupta8_be24@thapar.edu",
                    passwordEncoder.encode("admin@1234"),
                    "Thapar Institute of Engineering & Technology, Patiala",
                    "Bachelor of Engineering (B.E.)",
                    "Computer Engineering (COE)",
                    5
            );
            akshat.setAcademicGoals("Target CGPA >= 9.50 in 2627ODDSEM (Third Year). Excel in Computer Architecture (UCS510), Enterprise Web App (UCS553), Machine Learning (UML501), Data Science (UCS548), Image Processing (UCS615), Ethics (UCS421), and Software Engineering (UCS503).");
            akshat.setDailyStudyHours(3.5);
            akshat.setPreferredStudyTimings("EVENING");
            akshat.setWeeklyAvailability("{\"Monday\":3.5,\"Tuesday\":3.5,\"Wednesday\":4.0,\"Thursday\":3.5,\"Friday\":3.0,\"Saturday\":5.0,\"Sunday\":4.5}");
            akshat = studentRepository.save(akshat);
            seedCurriculumForStudent(akshat);
            System.out.println(">>> Syllabix: New student account created: agupta8_be24@thapar.edu (Akshat Gupta)");
        } else {
            akshat.setPasswordHash(passwordEncoder.encode("admin@1234"));
            studentRepository.save(akshat);
            List<Subject> akshatSubjects = subjectRepository.findByStudentId(akshat.getId());
            if (akshatSubjects.isEmpty()) {
                seedCurriculumForStudent(akshat);
            }
            System.out.println(">>> Syllabix: Student account verified and updated: agupta8_be24@thapar.edu (Akshat Gupta)");
        }

        // 3. Ensure secondary demo student (Alex Mercer) exists
        if (!studentRepository.existsByEmail("alex.student@university.edu")) {
            Student alex = new Student(
                    "Alex Mercer",
                    "alex.student@university.edu",
                    passwordEncoder.encode("password123"),
                    "National Institute of Technology",
                    "Bachelor of Technology",
                    "Computer Science & Engineering",
                    5
            );
            studentRepository.save(alex);
        }
    }

    private void seedCurriculumForStudent(Student student) {
        System.out.println(">>> Initializing Official Thapar Third-Year (2627ODDSEM) Academic Curriculum for " + student.getFullName() + "...");

        // =========================================================================
        // SEED THE 7 OFFICIAL THIRD-YEAR SUBJECTS
        // 1. UCS510: Computer Architecture and Organization
        // 2. UCS553: Enterprise Web Application (2627ODDSEM)
        // 3. UCS421: Ethics and Risk Mitigation in Computing (2627ODDSEM)
        // 4. UCS548: Foundations of Data Science (2627ODDSEM)
        // 5. UCS615: Image Processing (2627ODDSEM)
        // 6. UML501: Machine Learning (2627ODDSEM)
        // 7. UCS503: Software Engineering (2627ODDSEM)
        // =========================================================================

        Subject ucs510 = new Subject(student, "Computer Architecture and Organization", "UCS510", 5, 4,
                "Instruction set architectures, MIPS pipeline design, hazard handling, memory hierarchies, and multiprocessor cache coherency.",
                "Dr. Rajesh Kumar", "A", "#800000"); // Formal Maroon

        Subject ucs553 = new Subject(student, "Enterprise Web Application", "UCS553", 5, 4,
                "Enterprise Java, Spring Boot microservices, Spring Security, JPA/Hibernate persistence, and modern responsive frontend frameworks.",
                "Prof. Vivek Sharma", "A+", "#002D62"); // Formal Navy

        Subject ucs421 = new Subject(student, "Ethics and Risk Mitigation in Computing", "UCS421", 5, 2,
                "Professional codes of ethics (ACM/IEEE), algorithmic bias, AI safety governance, Indian DPDP Act, and open-source licensing compliance.",
                "Dr. Meenakshi Sood", "A", "#4A5568"); // Formal Slate

        Subject ucs548 = new Subject(student, "Foundations of Data Science", "UCS548", 5, 4,
                "Probability distributions, exploratory data analysis, hypothesis testing, PCA/SVD dimensionality reduction, and clustering algorithms.",
                "Dr. Anupam Garg", "A+", "#1B4D3E"); // Formal Dark Forest

        Subject ucs615 = new Subject(student, "Image Processing", "UCS615", 5, 3,
                "Digital image fundamentals, spatial and frequency domain filtering, 2D Fourier transforms, morphological processing, and edge detection.",
                "Dr. Harpreet Kaur", "A", "#B8860B"); // Formal Dark Gold

        Subject uml501 = new Subject(student, "Machine Learning", "UML501", 5, 4,
                "Supervised regression/classification, Decision Trees, SVM, Backpropagation neural networks, clustering, and model validation.",
                "Dr. Neeraj Kumar", "A+", "#6B1D2F"); // Formal Burgundy

        Subject ucs503 = new Subject(student, "Software Engineering", "UCS503", 5, 4,
                "Agile Scrum methodologies, UML design, software architecture patterns, test automation (unit/integration/mutation), and CI/CD pipelines.",
                "Prof. Sanjeev Sofat", "A", "#2E5B88"); // Formal Steel Blue

        subjectRepository.save(ucs510);
        subjectRepository.save(ucs553);
        subjectRepository.save(ucs421);
        subjectRepository.save(ucs548);
        subjectRepository.save(ucs615);
        subjectRepository.save(uml501);
        subjectRepository.save(ucs503);

        // =========================================================================
        // 1. UNITS & TOPICS: UCS510 (Computer Architecture) - ~45% Complete
        // =========================================================================
        Unit u1_510 = new Unit(ucs510, 1, "Instruction Set Architecture & Machine Language", "MIPS instruction formats, register conventions, addressing modes");
        Unit u2_510 = new Unit(ucs510, 2, "Datapath & Control Unit Design", "Single-cycle datapath, ALU control, hardwired vs microprogrammed control");
        Unit u3_510 = new Unit(ucs510, 3, "Pipelining & Hazard Resolution", "5-stage MIPS pipeline, data hazards, forwarding, branch prediction");
        Unit u4_510 = new Unit(ucs510, 4, "Memory Hierarchy & Cache Design", "Direct-mapped, set-associative cache, replacement policies, multi-level caches");
        unitRepository.save(u1_510);
        unitRepository.save(u2_510);
        unitRepository.save(u3_510);
        unitRepository.save(u4_510);

        Topic t1_510 = new Topic(u1_510, ucs510, student, "MIPS R-Type and I-Type Instruction Encoding", "Opcode, funct, rs, rt, rd fields and immediate arithmetic", "MEDIUM", 45, "COMPLETED", 5);
        t1_510.setLastStudiedDate(LocalDate.now().minusDays(12));
        Topic t2_510 = new Topic(u1_510, ucs510, student, "Addressing Modes & Assembly Control Flow", "Base addressing, PC-relative, and pseudo-direct jump addressing", "EASY", 40, "COMPLETED", 4);
        t2_510.setLastStudiedDate(LocalDate.now().minusDays(10));
        Topic t3_510 = new Topic(u2_510, ucs510, student, "Single-Cycle Datapath Execution", "Timing analysis, critical path delay and control signals", "MEDIUM", 50, "COMPLETED", 4);
        t3_510.setLastStudiedDate(LocalDate.now().minusDays(6));
        Topic t4_510 = new Topic(u3_510, ucs510, student, "Data Hazards & Forwarding Unit Design", "Load-use hazard stalls, EX-to-EX and MEM-to-EX forwarding logic", "HARD", 60, "IN_PROGRESS", 2);
        t4_510.setLastStudiedDate(LocalDate.now().minusDays(1));
        Topic t5_510 = new Topic(u3_510, ucs510, student, "Branch Prediction & Control Hazards", "Dynamic 2-bit branch predictor, branch delay slots", "HARD", 55, "NOT_STARTED", 2);
        Topic t6_510 = new Topic(u4_510, ucs510, student, "Set-Associative Cache Hit Ratio & Miss Rates", "Compulsory, capacity, and conflict misses with LRU policy", "HARD", 65, "NOT_STARTED", 1);
        topicRepository.save(t1_510);
        topicRepository.save(t2_510);
        topicRepository.save(t3_510);
        topicRepository.save(t4_510);
        topicRepository.save(t5_510);
        topicRepository.save(t6_510);

        // =========================================================================
        // 2. UNITS & TOPICS: UCS553 (Enterprise Web Application) - ~60% Complete
        // =========================================================================
        Unit u1_553 = new Unit(ucs553, 1, "Spring Boot Framework & Dependency Injection", "Inversion of control, bean lifecycles, and auto-configuration");
        Unit u2_553 = new Unit(ucs553, 2, "RESTful Architecture & Spring Security", "REST endpoints, JWT authentication, and method security");
        Unit u3_553 = new Unit(ucs553, 3, "Database Persistence with JPA & Hibernate", "Entity relations, JPQL, transaction management and connection pools");
        Unit u4_553 = new Unit(ucs553, 4, "Enterprise Integration & Cloud Deployment", "Microservices communication, Docker containerization, CI/CD");
        unitRepository.save(u1_553);
        unitRepository.save(u2_553);
        unitRepository.save(u3_553);
        unitRepository.save(u4_553);

        Topic t1_553 = new Topic(u1_553, ucs553, student, "Spring Boot Application Architecture", "Component scanning, configuration properties, and profiles", "EASY", 40, "COMPLETED", 5);
        t1_553.setLastStudiedDate(LocalDate.now().minusDays(14));
        Topic t2_553 = new Topic(u2_553, ucs553, student, "Spring Security Filter Chain & JWT Tokens", "Authentication provider, SecurityFilterChain, and stateless sessions", "HARD", 60, "COMPLETED", 4);
        t2_553.setLastStudiedDate(LocalDate.now().minusDays(4));
        Topic t3_553 = new Topic(u3_553, ucs553, student, "Hibernate Entity Mapping & Cascading Rules", "One-to-Many, Many-to-One, lazy vs eager fetching", "MEDIUM", 50, "COMPLETED", 4);
        t3_553.setLastStudiedDate(LocalDate.now().minusDays(2));
        Topic t4_553 = new Topic(u3_553, ucs553, student, "JPA Criteria Queries & Transaction Isolation", "ACID transactions, optimistic locking, and JPQL joins", "HARD", 55, "IN_PROGRESS", 3);
        t4_553.setLastStudiedDate(LocalDate.now().minusDays(1));
        Topic t5_553 = new Topic(u4_553, ucs553, student, "Docker Containerization & Multi-Stage Builds", "Dockerfile optimization, docker-compose orchestration", "MEDIUM", 45, "NOT_STARTED", 2);
        topicRepository.save(t1_553);
        topicRepository.save(t2_553);
        topicRepository.save(t3_553);
        topicRepository.save(t4_553);
        topicRepository.save(t5_553);

        // =========================================================================
        // 3. UNITS & TOPICS: UML501 (Machine Learning) - ~50% Complete
        // =========================================================================
        Unit u1_501 = new Unit(uml501, 1, "Linear Models & Optimization", "Linear regression, logistic regression, gradient descent algorithms");
        Unit u2_501 = new Unit(uml501, 2, "Decision Trees & Ensemble Learning", "Information gain, Gini index, Random Forest, AdaBoost, XGBoost");
        Unit u3_501 = new Unit(uml501, 3, "Support Vector Machines & Neural Networks", "Hyperplane margin maximization, kernel trick, multi-layer perceptron");
        unitRepository.save(u1_501);
        unitRepository.save(u2_501);
        unitRepository.save(u3_501);

        Topic t1_501 = new Topic(u1_501, uml501, student, "Gradient Descent & Cost Optimization", "Batch, mini-batch, stochastic gradient descent, learning rate decay", "MEDIUM", 45, "COMPLETED", 5);
        t1_501.setLastStudiedDate(LocalDate.now().minusDays(9));
        Topic t2_501 = new Topic(u2_501, uml501, student, "Decision Tree Splitting & Pruning", "ID3, C4.5, post-pruning techniques, handling continuous features", "MEDIUM", 50, "COMPLETED", 4);
        t2_501.setLastStudiedDate(LocalDate.now().minusDays(5));
        Topic t3_501 = new Topic(u3_501, uml501, student, "Support Vector Machines & Kernel Trick", "Soft-margin SVM, RBF kernel, dual problem formulation", "HARD", 65, "IN_PROGRESS", 2);
        t3_501.setLastStudiedDate(LocalDate.now().minusDays(2));
        Topic t4_501 = new Topic(u3_501, uml501, student, "Backpropagation & Neural Network Training", "Activation functions (ReLU, Sigmoid), chain rule, vanishing gradients", "HARD", 60, "NOT_STARTED", 1);
        topicRepository.save(t1_501);
        topicRepository.save(t2_501);
        topicRepository.save(t3_501);
        topicRepository.save(t4_501);

        // =========================================================================
        // 4. UNITS & TOPICS: UCS548 (Foundations of Data Science) - ~70% Complete
        // =========================================================================
        Unit u1_548 = new Unit(ucs548, 1, "Statistical Inference & Probability", "Central Limit Theorem, confidence intervals, hypothesis tests");
        Unit u2_548 = new Unit(ucs548, 2, "Dimensionality Reduction & Clustering", "PCA, Singular Value Decomposition, K-Means clustering");
        unitRepository.save(u1_548);
        unitRepository.save(u2_548);

        Topic t1_548 = new Topic(u1_548, ucs548, student, "Hypothesis Testing: T-Test & Chi-Square", "Null hypothesis formulation, p-values, degrees of freedom", "MEDIUM", 45, "COMPLETED", 4);
        t1_548.setLastStudiedDate(LocalDate.now().minusDays(8));
        Topic t2_548 = new Topic(u2_548, ucs548, student, "Principal Component Analysis (PCA)", "Covariance matrix, eigenvalues, projecting onto principal axes", "HARD", 55, "COMPLETED", 4);
        t2_548.setLastStudiedDate(LocalDate.now().minusDays(3));
        Topic t3_548 = new Topic(u2_548, ucs548, student, "K-Means Clustering & Silhouette Score", "Centroid initialization, convergence criteria, determining optimal K", "MEDIUM", 40, "NOT_STARTED", 3);
        topicRepository.save(t1_548);
        topicRepository.save(t2_548);
        topicRepository.save(t3_548);

        // =========================================================================
        // 5. UNITS & TOPICS: UCS615 (Image Processing) - ~50% Complete
        // =========================================================================
        Unit u1_615 = new Unit(ucs615, 1, "Spatial Domain Filtering & Enhancements", "Histogram equalization, smoothing linear filters, Laplacian sharpening");
        Unit u2_615 = new Unit(ucs615, 2, "Image Segmentation & Edge Detection", "Gradient operators (Sobel, Prewitt), Canny edge detector, thresholding");
        unitRepository.save(u1_615);
        unitRepository.save(u2_615);

        Topic t1_615 = new Topic(u1_615, ucs615, student, "Histogram Equalization & Contrast Stretching", "Cumulative distribution function, transformation function mapping", "EASY", 35, "COMPLETED", 5);
        t1_615.setLastStudiedDate(LocalDate.now().minusDays(7));
        Topic t2_615 = new Topic(u2_615, ucs615, student, "Canny Edge Detection Algorithm", "Gaussian smoothing, gradient calculation, non-maximum suppression, hysteresis", "HARD", 55, "IN_PROGRESS", 3);
        t2_615.setLastStudiedDate(LocalDate.now().minusDays(2));
        topicRepository.save(t1_615);
        topicRepository.save(t2_615);

        // =========================================================================
        // 6. UNITS & TOPICS: UCS421 (Ethics & Risk Mitigation) - ~75% Complete
        // =========================================================================
        Unit u1_421 = new Unit(ucs421, 1, "Professional Ethics & Algorithmic Accountability", "ACM Code of Conduct, algorithmic discrimination, AI explainability");
        Unit u2_421 = new Unit(ucs421, 2, "Cyber Regulations & Data Privacy", "Indian DPDP Act, GDPR compliance, intellectual property in software");
        unitRepository.save(u1_421);
        unitRepository.save(u2_421);

        Topic t1_421 = new Topic(u1_421, ucs421, student, "Algorithmic Bias & Fair Machine Learning", "Disparate impact, demographic parity, fairness metrics in AI", "MEDIUM", 40, "COMPLETED", 4);
        t1_421.setLastStudiedDate(LocalDate.now().minusDays(11));
        Topic t2_421 = new Topic(u2_421, ucs421, student, "Indian Digital Personal Data Protection (DPDP) Act", "Data fiduciary obligations, consent mechanisms, penalties", "EASY", 35, "COMPLETED", 5);
        t2_421.setLastStudiedDate(LocalDate.now().minusDays(5));
        topicRepository.save(t1_421);
        topicRepository.save(t2_421);

        // =========================================================================
        // 7. UNITS & TOPICS: UCS503 (Software Engineering) - ~60% Complete
        // =========================================================================
        Unit u1_503 = new Unit(ucs503, 1, "Agile Development & Requirements Modeling", "Scrum sprints, user stories, use case modeling, SRS documentation");
        Unit u2_503 = new Unit(ucs503, 2, "Design Patterns & Automated Testing", "Creational/Structural patterns, JUnit test pyramids, mutation testing");
        unitRepository.save(u1_503);
        unitRepository.save(u2_503);

        Topic t1_503 = new Topic(u1_503, ucs503, student, "Agile Scrum Ceremonies & Sprint Backlog Management", "Sprint planning, daily standup, burn-down charts, retrospective", "EASY", 35, "COMPLETED", 5);
        t1_503.setLastStudiedDate(LocalDate.now().minusDays(15));
        Topic t2_503 = new Topic(u2_503, ucs503, student, "Design Patterns: Factory, Singleton & Observer", "UML class structure, loose coupling, dependency inversion", "MEDIUM", 50, "COMPLETED", 4);
        t2_503.setLastStudiedDate(LocalDate.now().minusDays(6));
        Topic t3_503 = new Topic(u2_503, ucs503, student, "White-Box Testing: Path Coverage & Cyclomatic Complexity", "Control flow graphs, basis path testing, branch coverage", "HARD", 55, "NOT_STARTED", 2);
        topicRepository.save(t1_503);
        topicRepository.save(t2_503);
        topicRepository.save(t3_503);

        // =========================================================================
        // SEED ASSESSMENTS (Matching official Thapar ODD SEM 2026-27 schedule)
        // Reference date: 25 Nov 2026 (Week 17 • Teaching)
        // Week 18 (30 Nov - 05 Dec): Lab Evaluations & Sessional Make-up Quiz
        // Week 19-20 (07 Dec - 19 Dec): EST (End-Semester Examinations)
        // =========================================================================
        LocalDateTime refNow = LocalDateTime.now();

        // 1. UCS510: Computer Architecture MST / Eval in 5 days (Week 18)
        Assessment a1_510 = new Assessment(
                student, ucs510,
                "Minor Test (MST-2): Pipelining & Cache Design",
                "MST",
                refNow.plusDays(5).withHour(10).withMinute(0),
                "Units 2 & 3: Datapath, 5-stage MIPS pipeline, data hazard forwarding",
                50.0,
                "PREPARING",
                "URGENT",
                "Practice forwarding unit Boolean equations and pipeline timing diagrams."
        );

        // 2. UCS553: Enterprise Web App Lab Evaluation in 6 days (Week 18)
        Assessment a1_553 = new Assessment(
                student, ucs553,
                "Lab Evaluation & Viva (Week 18): Spring Boot & JPA",
                "LAB",
                refNow.plusDays(6).withHour(14).withMinute(0),
                "Assignments 1 to 8: Spring REST APIs, JWT Security, and JPA Persistence",
                20.0,
                "PREPARING",
                "URGENT",
                "Submit all pending GitHub lab repository links prior to scheduled viva."
        );

        // 3. UML501: Machine Learning Sessional Quiz in 9 days (Week 18/19)
        Assessment a1_501 = new Assessment(
                student, uml501,
                "Sessional Quiz: Classification & Neural Networks",
                "QUIZ",
                refNow.plusDays(9).withHour(11).withMinute(30),
                "Units 2 & 3: Decision Trees, SVM dual formulation, and Backpropagation",
                25.0,
                "READY",
                "HIGH",
                "Formula sheet permitted for cross-entropy loss and gradient formulas."
        );

        // 4. UCS548: Data Science Project Milestone in 11 days
        Assessment a1_548 = new Assessment(
                student, ucs548,
                "Data Science Term Project Presentation",
                "PROJECT",
                refNow.plusDays(11).withHour(15).withMinute(0),
                "End-to-end exploratory analysis, PCA dimensionality reduction, and clustering",
                30.0,
                "PREPARING",
                "MEDIUM",
                "Submit Jupyter Notebook with visualizations and executive summary PDF."
        );

        // 5. UCS503: Software Engineering EST in 16 days (Week 19 EST)
        Assessment a1_503 = new Assessment(
                student, ucs503,
                "End-Semester Examination (EST): Software Engineering",
                "EST",
                refNow.plusDays(16).withHour(9).withMinute(30),
                "Comprehensive syllabus (Agile, UML Design, Design Patterns, Test Automation)",
                100.0,
                "NOT_STARTED",
                "HIGH",
                "Official university hall ticket required; examination duration 3 hours."
        );

        assessmentRepository.save(a1_510);
        assessmentRepository.save(a1_553);
        assessmentRepository.save(a1_501);
        assessmentRepository.save(a1_548);
        assessmentRepository.save(a1_503);

        // =========================================================================
        // SEED PAST MARKS
        // =========================================================================
        Marks m1 = new Marks(student, null, ucs510, 19.0, 20.0, LocalDate.now().minusDays(22), "QUIZ", "High score in MIPS instruction encoding. Excellent work.");
        Marks m2 = new Marks(student, null, ucs553, 23.5, 25.0, LocalDate.now().minusDays(18), "ASSIGNMENT", "Spring Security filter chain implementation with JWT distinction.");
        Marks m3 = new Marks(student, null, uml501, 18.0, 20.0, LocalDate.now().minusDays(14), "QUIZ", "Accurate mathematical derivations for gradient descent updates.");
        Marks m4 = new Marks(student, null, ucs548, 43.0, 50.0, LocalDate.now().minusDays(30), "MST", "Strong grasp of hypothesis testing and PCA eigenvalue calculations.");
        Marks m5 = new Marks(student, null, ucs421, 24.0, 25.0, LocalDate.now().minusDays(10), "ASSIGNMENT", "Comprehensive analysis of Indian DPDP Act 2023 compliance.");
        Marks m6 = new Marks(student, null, ucs503, 44.0, 50.0, LocalDate.now().minusDays(28), "MST", "Exceptional UML class diagrams and software design pattern architecture.");

        marksRepository.save(m1);
        marksRepository.save(m2);
        marksRepository.save(m3);
        marksRepository.save(m4);
        marksRepository.save(m5);
        marksRepository.save(m6);

        // =========================================================================
        // SEED STUDY TASKS FOR TODAY
        // =========================================================================
        StudyTask task1 = new StudyTask(
                student, t4_510, ucs510,
                LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(19, 0), 60,
                "NEW_LEARNING", "CRITICAL", 89.0,
                "Computer Architecture MST is in 5 days; master MIPS pipeline forwarding logic."
        );
        StudyTask task2 = new StudyTask(
                student, t4_553, ucs553,
                LocalDate.now(), LocalTime.of(19, 15), LocalTime.of(20, 15), 60,
                "NEW_LEARNING", "URGENT", 85.0,
                "Enterprise Web App Lab Evaluation is in 6 days; review JPA Criteria queries."
        );
        StudyTask task3 = new StudyTask(
                student, t3_501, uml501,
                LocalDate.now(), LocalTime.of(20, 30), LocalTime.of(21, 15), 45,
                "REVISION", "HIGH", 72.0,
                "Solidify SVM dual formulation and soft margin slack variables ahead of quiz."
        );

        studyTaskRepository.save(task1);
        studyTaskRepository.save(task2);
        studyTaskRepository.save(task3);

        // =========================================================================
        // SEED RECENT STUDY SESSIONS FOR STUDY STREAK
        // =========================================================================
        StudySession s1 = new StudySession(
                student, ucs510, t3_510, null,
                refNow.minusDays(3).withHour(18).withMinute(0), refNow.minusDays(3).withHour(18).withMinute(50), 50,
                "POMODORO", "Analyzed single-cycle datapath execution delays and control word mapping.", 5
        );
        StudySession s2 = new StudySession(
                student, ucs553, t2_553, null,
                refNow.minusDays(2).withHour(19).withMinute(0), refNow.minusDays(2).withHour(19).withMinute(45), 45,
                "POMODORO", "Verified stateless Spring Security JWT authentication filter chain.", 5
        );
        StudySession s3 = new StudySession(
                student, uml501, t2_501, null,
                refNow.minusDays(1).withHour(18).withMinute(0), refNow.minusDays(1).withHour(19).withMinute(0), 60,
                "CUSTOM", "Computed Decision Tree Gini impurity reduction across split features.", 4
        );

        studySessionRepository.save(s1);
        studySessionRepository.save(s2);
        studySessionRepository.save(s3);

        System.out.println(">>> Syllabix: Official Thapar Third-Year (2627ODDSEM) Academic Environment successfully initialized with 7 Subjects, 24 Topics, 5 Assessments, and Verified Records!");
    }
}