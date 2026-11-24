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
        if (studentRepository.count() > 0) {
            return; // Already seeded
        }

        System.out.println(">>> Initializing Syllabix Demo Academic Environment...");

        // 1. Seed Thapar Student (Jasmine Kaur - matching Thapar LMS login)
        Student student = new Student(
                "Jasmine Kaur",
                "jkaur_be24@thapar.edu",
                passwordEncoder.encode("password123"),
                "Thapar Institute of Engineering & Technology, Patiala",
                "Bachelor of Engineering (B.E.)",
                "Computer Engineering (COE)",
                5
        );
        student.setAcademicGoals("Target CGPA >= 9.2 in 2425ODDSEM. Ace DBMS (UCS501) and OS (UCS502) MSTs, maintain 100% submission on Lab Assignments 1-8, and secure A grades across all subjects.");
        student.setDailyStudyHours(3.5);
        student.setPreferredStudyTimings("EVENING");
        student.setWeeklyAvailability("{\"Monday\":3.5,\"Tuesday\":3.5,\"Wednesday\":4.0,\"Thursday\":3.5,\"Friday\":3.0,\"Saturday\":5.0,\"Sunday\":4.5}");
        studentRepository.save(student);

        // Also seed Alex Mercer for secondary compatibility
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

        // 2. Seed Subjects with authentic Thapar University course codes
        Subject dbms = new Subject(student, "Database Management Systems", "UCS501", 5, 4,
                "Relational databases, Normalization, Query Processing, Transactions and Indexing structures",
                "Dr. Sarah Jenkins", "A+", "#4f46e5");
        Subject os = new Subject(student, "Operating Systems", "UCS502", 5, 4,
                "Process synchronization, CPU Scheduling, Virtual Memory, Deadlocks, File systems",
                "Prof. Alan Vance", "A", "#0284c7");
        Subject cn = new Subject(student, "Computer Networks", "UCS503", 5, 3,
                "OSI and TCP/IP protocol suites, Routing algorithms, Transport layer flow and congestion control",
                "Dr. Elena Rostova", "A", "#10b981");
        Subject daa = new Subject(student, "Design & Analysis of Algorithms", "UCS504", 5, 4,
                "Asymptotic notation, Divide & Conquer, Dynamic Programming, Greedy approaches and Graph algorithms",
                "Dr. Ramesh Nair", "A+", "#f59e0b");
        Subject se = new Subject(student, "Software Engineering & Agile", "UCS505", 5, 3,
                "Software lifecycles, Agile Scrum, System design, Testing methodologies and CI/CD pipelines",
                "Prof. Michael Chang", "A", "#8b5cf6");

        subjectRepository.save(dbms);
        subjectRepository.save(os);
        subjectRepository.save(cn);
        subjectRepository.save(daa);
        subjectRepository.save(se);

        // 3. Seed Units and Topics for DBMS (CS501) - ~40% completed (Prompt scenario)
        Unit dbmsU1 = new Unit(dbms, 1, "Relational Model & Relational Algebra", "Tuple calculus, schema definition, fundamental operators");
        Unit dbmsU2 = new Unit(dbms, 2, "SQL Queries & Schema Normalization", "1NF, 2NF, 3NF, BCNF, functional dependencies and decomposition");
        Unit dbmsU3 = new Unit(dbms, 3, "Transactions & Concurrency Control", "ACID properties, serializability, 2-phase locking, deadlock handling");
        Unit dbmsU4 = new Unit(dbms, 4, "Indexing, Hashing & Storage", "B-Trees, B+ Trees, clustered/unclustered indexing, bitmap index");
        Unit dbmsU5 = new Unit(dbms, 5, "Query Optimization & Recovery", "Query execution plan, cost models, ARIES log recovery algorithm");
        unitRepository.save(dbmsU1);
        unitRepository.save(dbmsU2);
        unitRepository.save(dbmsU3);
        unitRepository.save(dbmsU4);
        unitRepository.save(dbmsU5);

        // Topics DBMS
        Topic t1 = new Topic(dbmsU1, dbms, student, "Relational Algebra Operations", "Selection, projection, Cartesian product and joins", "MEDIUM", 45, "COMPLETED", 4);
        t1.setLastStudiedDate(LocalDate.now().minusDays(10));
        Topic t2 = new Topic(dbmsU1, dbms, student, "ER to Relational Schema Mapping", "Entity sets, relationship cardinality and composite attributes", "EASY", 40, "COMPLETED", 5);
        t2.setLastStudiedDate(LocalDate.now().minusDays(12));

        Topic t3 = new Topic(dbmsU2, dbms, student, "Complex SQL Subqueries & CTEs", "Nested queries, window functions, recursive CTEs", "MEDIUM", 50, "COMPLETED", 4);
        t3.setLastStudiedDate(LocalDate.now().minusDays(4));
        Topic t4 = new Topic(dbmsU2, dbms, student, "BCNF Decomposition & Lossless Join", "Functional dependency closure, canonical cover and testing BCNF", "HARD", 60, "IN_PROGRESS", 2);
        t4.setLastStudiedDate(LocalDate.now().minusDays(2));

        Topic t5 = new Topic(dbmsU3, dbms, student, "Conflict & View Serializability", "Precedence graphs, testing conflict serializability", "HARD", 60, "NOT_STARTED", 2);
        Topic t6 = new Topic(dbmsU3, dbms, student, "Strict Two-Phase Locking (2PL)", "Lock conversions, conservative 2PL, preventing cascading rollbacks", "HARD", 55, "NOT_STARTED", 1);

        Topic t7 = new Topic(dbmsU4, dbms, student, "B+ Tree Insertion and Deletion Algorithms", "Node splitting, balance criteria, multi-level indexing", "HARD", 65, "NOT_STARTED", 2);
        Topic t8 = new Topic(dbmsU4, dbms, student, "Dynamic Hashing & Extendible Hashing", "Directory allocation, bucket overflows and doubling directory", "MEDIUM", 45, "NOT_STARTED", 3);

        Topic t9 = new Topic(dbmsU5, dbms, student, "Heuristic Query Optimization", "Relational algebra equivalence rules, pushing selections down", "MEDIUM", 50, "NOT_STARTED", 3);
        Topic t10 = new Topic(dbmsU5, dbms, student, "ARIES Recovery Algorithm", "Analysis, Redo, and Undo passes with write-ahead logging (WAL)", "HARD", 60, "NOT_STARTED", 1);

        topicRepository.save(t1);
        topicRepository.save(t2);
        topicRepository.save(t3);
        topicRepository.save(t4);
        topicRepository.save(t5);
        topicRepository.save(t6);
        topicRepository.save(t7);
        topicRepository.save(t8);
        topicRepository.save(t9);
        topicRepository.save(t10);

        // 4. Seed Units and Topics for Operating Systems (CS502) - ~75% completed (Prompt scenario)
        Unit osU1 = new Unit(os, 1, "Processes, Threads & System Calls", "Process control blocks, context switching, pthread library");
        Unit osU2 = new Unit(os, 2, "CPU Scheduling & IPC", "Round Robin, Multi-level feedback queues, pipes and shared memory");
        Unit osU3 = new Unit(os, 3, "Synchronization & Deadlock Management", "Semaphores, Monitors, Banker's Algorithm, Resource Allocation Graph");
        Unit osU4 = new Unit(os, 4, "Memory Management & Paging", "Demand paging, Page replacement (LRU, FIFO), Thrashing");
        unitRepository.save(osU1);
        unitRepository.save(osU2);
        unitRepository.save(osU3);
        unitRepository.save(osU4);

        Topic ot1 = new Topic(osU1, os, student, "Process Lifecycle & Context Switching", "PCB state transitions, fork/exec system calls", "EASY", 35, "COMPLETED", 5);
        ot1.setLastStudiedDate(LocalDate.now().minusDays(15));
        Topic ot2 = new Topic(osU1, os, student, "Kernel vs User Threads", "Many-to-one, one-to-one mapping models", "MEDIUM", 40, "COMPLETED", 4);
        ot2.setLastStudiedDate(LocalDate.now().minusDays(14));

        Topic ot3 = new Topic(osU2, os, student, "Multilevel Feedback Queue Scheduling", "Preemption, aging techniques, starvation avoidance", "MEDIUM", 45, "COMPLETED", 4);
        ot3.setLastStudiedDate(LocalDate.now().minusDays(8));
        Topic ot4 = new Topic(osU2, os, student, "Inter-Process Communication via Shared Memory", "Race conditions, memory mapping with shmget", "MEDIUM", 45, "COMPLETED", 4);
        ot4.setLastStudiedDate(LocalDate.now().minusDays(7));

        Topic ot5 = new Topic(osU3, os, student, "Classic Synchronization Problems", "Dining Philosophers, Readers-Writers with semaphores", "HARD", 55, "COMPLETED", 4);
        ot5.setLastStudiedDate(LocalDate.now().minusDays(3));
        Topic ot6 = new Topic(osU3, os, student, "Banker's Algorithm for Deadlock Avoidance", "Safe state verification, resource request algorithm", "HARD", 50, "COMPLETED", 4);
        ot6.setLastStudiedDate(LocalDate.now().minusDays(3));

        Topic ot7 = new Topic(osU4, os, student, "Virtual Memory & Inverted Page Tables", "Multi-level paging, TLB hit ratio calculations", "HARD", 60, "IN_PROGRESS", 3);
        ot7.setLastStudiedDate(LocalDate.now().minusDays(1));
        Topic ot8 = new Topic(osU4, os, student, "Page Replacement: LRU vs Working Set", "Belady's anomaly, clock algorithm, thrashing mitigation", "HARD", 55, "NOT_STARTED", 2);

        topicRepository.save(ot1);
        topicRepository.save(ot2);
        topicRepository.save(ot3);
        topicRepository.save(ot4);
        topicRepository.save(ot5);
        topicRepository.save(ot6);
        topicRepository.save(ot7);
        topicRepository.save(ot8);

        // 5. Seed Units and Topics for Computer Networks (CS503)
        Unit cnU1 = new Unit(cn, 1, "Network Layer & Routing", "IP addressing, CIDR, Dijkstra SPF, distance vector routing");
        Unit cnU2 = new Unit(cn, 2, "Transport Protocols & Flow Control", "TCP 3-way handshake, sliding window, Reno/Cubic congestion control");
        unitRepository.save(cnU1);
        unitRepository.save(cnU2);

        Topic cnt1 = new Topic(cnU1, cn, student, "Subnetting and CIDR Calculation", "VLSM, address block allocation, broadcast domains", "MEDIUM", 45, "COMPLETED", 4);
        cnt1.setLastStudiedDate(LocalDate.now().minusDays(6));
        Topic cnt2 = new Topic(cnU1, cn, student, "Link-State Routing & OSPF", "Dijkstra algorithm, link state packets, routing loops", "HARD", 50, "NOT_STARTED", 3);
        Topic cnt3 = new Topic(cnU2, cn, student, "TCP Congestion Control State Machine", "Slow start, congestion avoidance, fast retransmit, fast recovery", "HARD", 55, "NOT_STARTED", 2);
        topicRepository.save(cnt1);
        topicRepository.save(cnt2);
        topicRepository.save(cnt3);

        // 6. Seed Assessments (Matching exact prompt requirements!)
        LocalDateTime now = LocalDateTime.now();

        // Assessment 1: DBMS MST in 5 days (Urgent Red)
        Assessment aDbmsMst = new Assessment(
                student, dbms,
                "Mid-Semester Test (MST-1): DBMS Core",
                "MST",
                now.plusDays(5).withHour(10).withMinute(0),
                "Units 1, 2, and 3 (Relational Algebra, SQL Normalization, Serializability)",
                50.0,
                "PREPARING",
                "URGENT",
                "Must practice BCNF decomposition proofs and conflict serializability graphs"
        );

        // Assessment 2: Operating Systems MST in 8 days (Approaching Orange)
        Assessment aOsMst = new Assessment(
                student, os,
                "Mid-Semester Test (MST-1): Operating Systems",
                "MST",
                now.plusDays(8).withHour(14).withMinute(0),
                "Units 1, 2, and 3 (Processes, CPU Scheduling, Synchronization)",
                50.0,
                "READY",
                "HIGH",
                "Review Banker's algorithm safe sequence checks and Dining Philosophers semaphore code"
        );

        // Assessment 3: Computer Networks Quiz in 12 days (Green)
        Assessment aCnQuiz = new Assessment(
                student, cn,
                "Network Layer & CIDR Subnetting Quiz",
                "QUIZ",
                now.plusDays(12).withHour(11).withMinute(30),
                "Unit 1: Subnetting, VLSM and IP routing",
                25.0,
                "NOT_STARTED",
                "MEDIUM",
                "Online MCQs and subnet mask calculation problems"
        );

        // Assessment 4: Algorithms End-Sem in 25 days
        Assessment aDaaEst = new Assessment(
                student, daa,
                "End-Semester Examination (EST): Algorithms",
                "EST",
                now.plusDays(25).withHour(9).withMinute(30),
                "Comprehensive syllabus (Units 1 to 5)",
                100.0,
                "NOT_STARTED",
                "HIGH",
                "Comprehensive university exam covering dynamic programming and graph theory"
        );

        assessmentRepository.save(aDbmsMst);
        assessmentRepository.save(aOsMst);
        assessmentRepository.save(aCnQuiz);
        assessmentRepository.save(aDaaEst);

        // 7. Seed Marks for Past Assessments
        Marks m1 = new Marks(student, null, dbms, 18.5, 20.0, LocalDate.now().minusDays(20), "QUIZ", "High score in Relational Algebra. Lost 1.5 marks in outer joins.");
        Marks m2 = new Marks(student, null, os, 16.0, 20.0, LocalDate.now().minusDays(18), "QUIZ", "Solid understanding of CPU scheduling. Practice IPC shared memory code.");
        Marks m3 = new Marks(student, null, cn, 22.0, 25.0, LocalDate.now().minusDays(14), "ASSIGNMENT", "Network simulation lab report received grade A.");
        Marks m4 = new Marks(student, null, daa, 42.0, 50.0, LocalDate.now().minusDays(30), "MST", "Great work on divide and conquer; review dynamic programming memoization.");
        Marks m5 = new Marks(student, null, se, 23.5, 25.0, LocalDate.now().minusDays(10), "ASSIGNMENT", "Agile sprint backlog and user story mapping accepted with distinction.");

        marksRepository.save(m1);
        marksRepository.save(m2);
        marksRepository.save(m3);
        marksRepository.save(m4);
        marksRepository.save(m5);

        // 8. Seed Study Tasks for Today and Upcoming Days
        StudyTask task1 = new StudyTask(
                student, t4, dbms,
                LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(19, 0), 60,
                "NEW_LEARNING", "CRITICAL", 88.5,
                "DBMS examination is in 5 days; BCNF decomposition is heavily weighted on MST-1"
        );
        StudyTask task2 = new StudyTask(
                student, t5, dbms,
                LocalDate.now(), LocalTime.of(19, 15), LocalTime.of(20, 15), 60,
                "NEW_LEARNING", "URGENT", 84.0,
                "High priority conflict serializability topic pending with upcoming exam"
        );
        StudyTask task3 = new StudyTask(
                student, ot7, os,
                LocalDate.now(), LocalTime.of(20, 30), LocalTime.of(21, 15), 45,
                "REVISION", "HIGH", 68.0,
                "Solidify virtual memory paging concepts ahead of OS test in 8 days"
        );

        studyTaskRepository.save(task1);
        studyTaskRepository.save(task2);
        studyTaskRepository.save(task3);

        // 9. Seed Study Sessions for Study Streak Tracking
        StudySession s1 = new StudySession(
                student, dbms, t3, null,
                now.minusDays(3).withHour(18).withMinute(0), now.minusDays(3).withHour(18).withMinute(50), 50,
                "POMODORO", "Completed complex SQL nested subqueries and window rank functions.", 5
        );
        StudySession s2 = new StudySession(
                student, os, ot6, null,
                now.minusDays(2).withHour(19).withMinute(0), now.minusDays(2).withHour(19).withMinute(45), 45,
                "POMODORO", "Solved Banker's algorithm safe sequence exercises without error.", 5
        );
        StudySession s3 = new StudySession(
                student, dbms, t4, null,
                now.minusDays(1).withHour(18).withMinute(0), now.minusDays(1).withHour(19).withMinute(0), 60,
                "CUSTOM", "Worked through functional dependency canonical cover steps.", 4
        );

        studySessionRepository.save(s1);
        studySessionRepository.save(s2);
        studySessionRepository.save(s3);

        System.out.println(">>> Syllabix Demo Academic Environment successfully initialized with 5 Subjects, 21 Topics, 4 Assessments, and Past Analytics!");
    }
}
