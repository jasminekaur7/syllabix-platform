const fs = require('fs');
const path = require('path');
const svgs = require('./report_svgs.js');

const htmlContent = `<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UCS553 Mini Project Report - Syllabix</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        @page {
            size: A4 portrait;
            margin: 18mm 18mm 18mm 18mm;
        }
        @page :first {
            margin: 12mm 15mm 12mm 15mm;
        }
        body {
            font-family: 'Times New Roman', Times, serif;
            color: #1a1a1a;
            line-height: 1.55;
            background-color: #f8f9fa;
            font-size: 11.5pt;
        }
        .report-container {
            max-width: 880px;
            margin: 30px auto;
            background: #ffffff;
            padding: 40px 55px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            border-radius: 4px;
        }
        @media print {
            body {
                background: #ffffff;
                color: #000000;
                font-size: 11pt;
            }
            .report-container {
                max-width: 100%;
                margin: 0;
                padding: 0;
                box-shadow: none;
                border-radius: 0;
            }
            .no-print {
                display: none !important;
            }
            .page-break {
                page-break-after: always;
                break-after: page;
            }
            .cover-page {
                height: 245mm !important;
                max-height: 245mm !important;
                padding: 22px 20px !important;
                margin: 0 !important;
                box-sizing: border-box !important;
            }
            .diagram-box {
                page-break-inside: avoid;
                margin: 15px auto !important;
                text-align: center;
            }
            .diagram-box svg {
                max-width: 100% !important;
                height: auto !important;
            }
        }
        .cover-page {
            box-sizing: border-box;
            height: 900px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            text-align: center;
            border: 2px solid #222;
            padding: 26px 22px;
            margin-bottom: 0;
            background: #fff;
        }
        .cover-header h1 {
            font-size: 21pt;
            font-weight: bold;
            margin-bottom: 4px;
            color: #000;
        }
        .cover-header h2 {
            font-size: 16pt;
            font-weight: bold;
            margin-bottom: 10px;
            color: #333;
        }
        .cover-title {
            margin: 10px 0;
        }
        .cover-title h3 {
            font-size: 16pt;
            font-weight: bold;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            color: #800000;
            line-height: 1.35;
        }
        .cover-section-title {
            font-size: 11pt;
            font-style: italic;
            margin-bottom: 3px;
        }
        .cover-guide {
            font-size: 12pt;
            margin: 8px 0;
        }
        .cover-guide strong {
            font-size: 13pt;
        }
        .ti-logo-box {
            margin: 10px auto;
        }
        .ti-logo-text {
            font-size: 34pt;
            font-weight: 900;
            color: #d32f2f;
            line-height: 0.9;
            letter-spacing: -2px;
            display: inline-block;
        }
        .ti-subtext {
            font-size: 8.5pt;
            font-weight: bold;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: #800000;
            margin-top: 3px;
        }
        .ti-deemed {
            font-size: 7.5pt;
            color: #555;
            font-style: italic;
        }
        .cover-students {
            margin: 8px 0;
            text-align: left;
            display: inline-block;
            font-size: 11.5pt;
        }
        .cover-students-table {
            margin: 0 auto;
            font-size: 11.5pt;
        }
        .cover-students-table td {
            padding: 3px 14px;
        }
        .cover-footer {
            font-size: 11pt;
            line-height: 1.35;
            margin-top: 4px;
        }
        .cover-footer strong {
            font-size: 11.5pt;
        }
        h2.section-heading {
            font-size: 16pt;
            font-weight: bold;
            border-bottom: 2px solid #800000;
            padding-bottom: 5px;
            margin-top: 25px;
            margin-bottom: 14px;
            color: #800000;
        }
        h3.sub-heading {
            font-size: 13pt;
            font-weight: bold;
            margin-top: 20px;
            margin-bottom: 10px;
            color: #002D62;
        }
        h4.sub-sub-heading {
            font-size: 11.5pt;
            font-weight: bold;
            margin-top: 14px;
            margin-bottom: 6px;
            color: #333;
        }
        p, li {
            text-align: justify;
        }
        table.report-table {
            width: 100%;
            border-collapse: collapse;
            margin: 14px 0;
            font-size: 10.5pt;
        }
        table.report-table th, table.report-table td {
            border: 1px solid #444;
            padding: 6px 10px;
            vertical-align: top;
        }
        table.report-table th {
            background-color: #f2f2f2;
            font-weight: bold;
            color: #000;
        }
        code, pre {
            font-family: 'Consolas', 'Courier New', Courier, monospace;
        }
        pre.code-block {
            background-color: #f4f6f8;
            border: 1px solid #dcdcdc;
            border-left: 4px solid #800000;
            padding: 10px 14px;
            font-size: 9.5pt;
            line-height: 1.4;
            overflow-x: auto;
            border-radius: 3px;
            margin: 12px 0;
        }
        .diagram-box {
            text-align: center;
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 4px;
            padding: 10px;
            margin: 14px 0 20px 0;
        }
        .diagram-box svg {
            width: 100%;
            height: auto;
            display: block;
            margin: 0 auto;
        }
        .callout-box {
            background-color: #f8f9fc;
            border-left: 4px solid #002D62;
            padding: 12px 16px;
            margin: 14px 0;
            border-radius: 3px;
        }
        .status-badge {
            display: inline-block;
            padding: 2px 7px;
            font-size: 8.5pt;
            font-weight: bold;
            border-radius: 3px;
        }
        .badge-success { background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
        .badge-warning { background: #fff8e1; color: #f57f17; border: 1px solid #ffe082; }
        .badge-danger { background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    </style>
</head>
<body>

<div class="no-print bg-dark text-white py-2 px-3 text-center fixed-top d-flex justify-content-between align-items-center">
    <div>
        <i class="bi bi-file-earmark-pdf-fill text-danger me-2"></i>
        <strong>Syllabix Academic Report</strong> — Formatted strictly per TIET Guidelines
    </div>
    <div>
        <a href="https://syllabix-platform-2.onrender.com" target="_blank" class="btn btn-sm btn-outline-info me-2">
            <i class="bi bi-box-arrow-up-right me-1"></i> Live Web App
        </a>
        <button onclick="window.print()" class="btn btn-sm btn-danger px-3">
            <i class="bi bi-printer-fill me-1"></i> Print / Save as PDF (Ctrl + P)
        </button>
    </div>
</div>

<div style="height: 50px;" class="no-print"></div>

<div class="report-container">

    <!-- PAGE 1: COVER PAGE -->
    <div class="cover-page">
        <div class="cover-header">
            <h1>UCS553: Enterprise Web Application</h1>
            <h2>Mini Project Report</h2>
        </div>

        <div class="cover-title">
            <h3>SYLLABIX: INTELLIGENT STUDENT MANAGEMENT &amp; ADAPTIVE ACADEMIC PLANNING PLATFORM</h3>
        </div>

        <div class="cover-guide">
            <div class="cover-section-title">Under the Guidance of</div>
            <strong>Dr. ABC, XYZ</strong><br>
            Assistant Professor<br>
            Thapar Institute of Engineering Technology
        </div>

        <div class="ti-logo-box">
            <div class="ti-logo-text">ti</div>
            <div class="ti-subtext">THAPAR INSTITUTE</div>
            <div class="ti-deemed">OF ENGINEERING &amp; TECHNOLOGY<br>(Deemed to be University)</div>
        </div>

        <div class="cover-students">
            <div style="font-weight: bold; margin-bottom: 6px;">Submitted By :</div>
            <table class="cover-students-table">
                <tr>
                    <td><strong>Roll Number</strong></td>
                    <td><strong>Student Name</strong></td>
                </tr>
                <tr>
                    <td>1024030605</td>
                    <td>Akshat Gupta</td>
                </tr>
            </table>
        </div>

        <div class="cover-footer">
            <strong>Computer Science Engineering Department Thapar<br>Institute of Engineering Technology, Patiala</strong>
            <br><br>
            In Partial Fulfilment of the Requirements for the Degree of<br>
            Bachelor of Engineering in Computer Engineering<br>
            at<br>
            <strong>Thapar Institute of Engineering Technology, Patiala</strong>
            <br><br>
            <strong>November 2026</strong>
        </div>
    </div>

    <div class="page-break"></div>

    <!-- PAGE 2: PROJECT REPORT STRUCTURE & ABSTRACT -->
    <h2 class="section-heading">1. Project Report Structure</h2>
    <table class="report-table">
        <thead>
            <tr>
                <th style="width: 8%; text-align: center;">S. No.</th>
                <th style="width: 32%;">Section</th>
                <th style="width: 60%;">Contents</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td style="text-align: center;">1</td>
                <td><strong>Cover Page</strong></td>
                <td>Project title, student names, roll numbers, department and institution</td>
            </tr>
            <tr>
                <td style="text-align: center;">2</td>
                <td><strong>Abstract</strong></td>
                <td>Brief overview of the project</td>
            </tr>
            <tr>
                <td style="text-align: center;">3</td>
                <td><strong>Table of Contents</strong></td>
                <td>Chapters and page numbers</td>
            </tr>
            <tr>
                <td style="text-align: center;">4</td>
                <td><strong>Chapter 1: Introduction</strong></td>
                <td>Project background, problem statement, objectives and scope</td>
            </tr>
            <tr>
                <td style="text-align: center;">5</td>
                <td><strong>Chapter 2: System Design</strong></td>
                <td>System architecture, UML diagrams and database design</td>
            </tr>
            <tr>
                <td style="text-align: center;">6</td>
                <td><strong>Chapter 3: Implementation</strong></td>
                <td>Technologies, project structure, classes and modules</td>
            </tr>
            <tr>
                <td style="text-align: center;">7</td>
                <td><strong>Chapter 4: Results and Screenshots</strong></td>
                <td>Application outputs and screenshots</td>
            </tr>
            <tr>
                <td style="text-align: center;">8</td>
                <td><strong>References</strong></td>
                <td>Books, websites and documentation</td>
            </tr>
        </tbody>
    </table>

    <h2 class="section-heading">Abstract</h2>
    <p>
        Modern higher education institutions present students with complex, fragmented academic environments. At institutions like Thapar Institute of Engineering &amp; Technology (TIET), a student in their third year balances 7 demanding core courses (e.g., Computer Architecture, Enterprise Web Applications, Machine Learning, Foundations of Data Science, Image Processing, Ethics, and Software Engineering), multiple concurrent continuous assessment components (quizzes, lab evaluations, MSTs, ESTs), and tight deadlines. Conventional Learning Management Systems (LMS) and college ERP portals act merely as passive repositories—hosting course syllabi as unstructured text or PDF links without providing active guidance. Consequently, students struggle with three critical questions: <em>(1) What should I study today? (2) When should I study it? (3) Which topics require immediate attention to reach my target grade?</em>
    </p>
    <p>
        <strong>Syllabix</strong> is an intelligent, full-stack student management and adaptive academic planning platform built from the ground up to solve this challenge. Implemented strictly in <strong>Java 17 / Spring Boot 3.3.5</strong>, Syllabix replaces static CRUD dashboards with an active academic engine. Its flagship features include:
    </p>
    <ul>
        <li><strong>Adaptive Study Recommendation Engine:</strong> A normalized, 4-factor weighted heuristic algorithm (<em>35% Exam Urgency, 30% Syllabus Incompletion, 20% Topic Difficulty, 15% Academic Marks Deficit</em>) that calculates dynamic daily study directives without exceeding declared student availability.</li>
        <li><strong>Thapar LMS Course Evaluation Scheme Auto-Parser &amp; Target Run-Rate Calculator:</strong> An intelligent regex parser that takes raw course evaluation snippets copied directly from Thapar LMS (e.g., MST 30 marks, EST 40 marks, Lab 20 marks, Sessional Quiz 10 marks), tallies completed scores, calculates the exact percentage run-rate needed on remaining evaluations to secure target letter grades (A+ &ge; 88%, A &ge; 80%, B &ge; 65%), and auto-schedules the milestone assessments.</li>
        <li><strong>Official Thapar 20-Week Academic Calendar &amp; Datesheet Tracker:</strong> Direct integration of TIET's ODD SEM 2026-27 schedule, auto-detecting the current week (Week 17 • Teaching), countdowns to Assessment Week (Week 18) and End-Semester Examinations (Weeks 19–20), compensatory Saturday teaching schedules, and gazetted university holidays.</li>
        <li><strong>Lab Assignment Tracker (Assignments 1 to 8):</strong> Continuous tracking of practical submissions and viva readiness.</li>
        <li><strong>PDF Syllabus Extractor:</strong> Apache PDFBox-powered text extraction with human-in-the-loop validation for rapid curriculum onboarding.</li>
        <li><strong>Focus Pomodoro &amp; Productivity Tracker:</strong> Interval-based study sessions with Web Audio chime synthesis and persistent habit streak tracking.</li>
    </ul>
    <p>
        The system is deployed live on the cloud via Docker at <strong><code>https://syllabix-platform-2.onrender.com</code></strong> and backed by a comprehensive JUnit 5 and Mockito test suite achieving 100% pass rates across all core algorithmic services.
    </p>

    <div class="page-break"></div>

    <!-- PAGE 3: TABLE OF CONTENTS -->
    <h2 class="section-heading">Table of Contents</h2>
    <div class="row">
        <div class="col-12">
            <ul class="list-unstyled" style="font-size: 11pt; line-height: 1.85;">
                <li><strong>1. Cover Page</strong></li>
                <li><strong>2. Abstract</strong></li>
                <li><strong>3. Table of Contents</strong></li>
                <li><strong>4. Chapter 1: Introduction</strong>
                    <ul class="list-unstyled ps-4">
                        <li>1.1 Introduction to the Project</li>
                        <li>1.2 Problem Statement</li>
                        <li>1.3 Objectives of the Project</li>
                        <li>1.4 Scope of the Project</li>
                    </ul>
                </li>
                <li><strong>5. Chapter 2: System Design</strong>
                    <ul class="list-unstyled ps-4">
                        <li>2.1 System Architecture</li>
                        <li>2.2 Use Case Diagram</li>
                        <li>2.3 Class Diagram</li>
                        <li>2.4 Activity Diagram</li>
                        <li>2.5 Database Design / ER Diagram</li>
                        <li>2.6 Module Description</li>
                    </ul>
                </li>
                <li><strong>6. Chapter 3: Implementation</strong>
                    <ul class="list-unstyled ps-4">
                        <li>3.1 Technologies and Tools Used</li>
                        <li>3.2 Project Structure / Package Structure</li>
                        <li>3.3 Description of Classes and Interfaces</li>
                        <li>3.4 Implementation of Major Modules</li>
                        <li>3.5 Exception Handling</li>
                        <li>3.6 Collection Framework Usage</li>
                        <li>3.7 Database Connectivity</li>
                        <li>3.8 Important Code Snippets</li>
                    </ul>
                </li>
                <li><strong>7. Chapter 4: Results and Screenshots</strong>
                    <ul class="list-unstyled ps-4">
                        <li>4.1 Main Application Screen</li>
                        <li>4.2 Functional Screenshots</li>
                        <li>4.3 Input and Output Demonstrations</li>
                        <li>4.4 Error Handling Demonstrations</li>
                    </ul>
                </li>
                <li><strong>8. References</strong></li>
            </ul>
        </div>
    </div>

    <div class="page-break"></div>

    <!-- CHAPTER 1 -->
    <h2 class="section-heading">Chapter 1: Introduction</h2>

    <h3 class="sub-heading">1.1 Introduction to the Project</h3>
    <p>
        In contemporary university education, students are challenged not by a lack of study resources, but by cognitive overload and suboptimal time allocation. University portals provide students with syllabi, datesheets, and evaluation breakdowns, but leave the translation of this raw data into an actionable daily study schedule entirely to the student. Most students resort to ad-hoc, reactive studying—focusing on whichever examination is closest or cramming during the final days before Mid-Semester Tests (MST) or End-Semester Tests (EST).
    </p>
    <p>
        <strong>Syllabix</strong> is an intelligent academic management and adaptive planning platform tailored specifically to engineering students at Thapar Institute of Engineering &amp; Technology (TIET). Built as an enterprise-grade Java web application, Syllabix synchronizes curriculum tracking, continuous assessment monitoring, academic calendar awareness, and algorithmic study scheduling into a cohesive, institutional-grade portal.
    </p>

    <h3 class="sub-heading">1.2 Problem Statement</h3>
    <p>Standard Learning Management Systems (e.g., Moodle, Blackboard) and college ERP portals suffer from several critical shortcomings:</p>
    <ol>
        <li><strong>Passive Information Silos:</strong> Portals store syllabi as unparsed documents. Students cannot easily see their micro-level syllabus completion (e.g., topic-by-topic progress across units).</li>
        <li><strong>Evaluation Ambiguity:</strong> Course evaluation schemes are published as unstructured strings (e.g., <code>MST: 30 Marks, EST: 40 Marks, Lab: 20 Marks, Quiz: 10 Marks</code>). Students cannot easily determine the required marks or minimum percentage run-rate needed on remaining components to achieve their desired grade (A, A+, B).</li>
        <li><strong>Absence of Intelligent Scheduling:</strong> Existing platforms lack algorithmic awareness of student availability, topic difficulty, previous academic performance, and approaching datesheet deadlines.</li>
        <li><strong>Disconnection from University Academic Calendars:</strong> Key university milestones (e.g., Teaching Weeks, Assessment Weeks, EST Weeks, compensatory Saturday timetables, and Gazetted Holidays) are distributed via isolated PDF circulars rather than being dynamically integrated into daily academic workflows.</li>
    </ol>

    <h3 class="sub-heading">1.3 Objectives of the Project</h3>
    <p>The primary objectives of the Syllabix platform are:</p>
    <ol>
        <li><strong>Develop an Enterprise Java Backend:</strong> Implement a multi-layered, secure backend using Java 21, Spring Boot, Spring Security (BCrypt hashing, session authentication), and Spring Data JPA.</li>
        <li><strong>Design an Adaptive Recommendation Engine:</strong> Create a mathematically sound, normalized multi-factor priority algorithm to compute topic priority scores and generate conflict-free daily study timetables respecting student availability.</li>
        <li><strong>Automate LMS Evaluation Parsing &amp; Run-Rate Computation:</strong> Build an automated evaluation parser that extracts assessment components, calculates target grade cutoffs, computes required run-rates on remaining marks, and automatically registers assessments into the student’s schedule.</li>
        <li><strong>Integrate the Official TIET Academic Calendar:</strong> Digitize the 20-week ODD SEM 2026-27 calendar, enabling automatic week detection (e.g., Week 17 Teaching), datesheet milestone tracking, and holiday countdowns.</li>
        <li><strong>Support PDF Syllabus Onboarding:</strong> Provide an Apache PDFBox-backed text extraction pipeline to convert raw PDF documents into structured units and topics with student review.</li>
        <li><strong>Deliver a Dignified Institutional UI:</strong> Provide a responsive, high-fidelity user interface matching the official visual identity of Thapar Institute of Engineering &amp; Technology.</li>
    </ol>

    <h3 class="sub-heading">1.4 Scope of the Project</h3>
    <p>The scope of Syllabix encompasses:</p>
    <ul>
        <li><strong>Target Audience:</strong> Engineering undergraduate students, specifically 3rd-year students enrolled in <code>2627ODDSEM</code> (COE/CSED).</li>
        <li><strong>Academic Scope:</strong> 7 Third-Year Core Courses:
            <ul>
                <li><code>UCS510</code>: Computer Architecture and Organization (4.0 Credits)</li>
                <li><code>UCS553</code>: Enterprise Web Application (4.0 Credits)</li>
                <li><code>UCS421</code>: Ethics and Risk Mitigation in Computing (2.0 Credits)</li>
                <li><code>UCS548</code>: Foundations of Data Science (4.0 Credits)</li>
                <li><code>UCS615</code>: Image Processing (3.0 Credits)</li>
                <li><code>UML501</code>: Machine Learning (4.0 Credits)</li>
                <li><code>UCS503</code>: Software Engineering (4.0 Credits)</li>
            </ul>
        </li>
        <li><strong>Deployment Scope:</strong> Containerized with Docker and cloud-deployed on Render.com with persistent database storage and automated health checks.</li>
        <li><strong>Out of Scope:</strong> University administrative grading backends, automated student attendance biometric hardware integration, and non-student LMS faculty grading portals.</li>
    </ul>

    <div class="page-break"></div>

    <!-- CHAPTER 2: SYSTEM DESIGN -->
    <h2 class="section-heading">Chapter 2: System Design</h2>

    <h3 class="sub-heading">2.1 System Architecture</h3>
    <p>
        Syllabix follows an enterprise 3-tier Layered MVC (Model-View-Controller) architecture, ensuring clean separation of concerns, testability, and high maintainability.
    </p>
    <div class="diagram-box">
        ${svgs.architectureSvg}
    </div>

    <div class="page-break"></div>

    <h3 class="sub-heading">2.2 Use Case Diagram</h3>
    <p>
        The primary actors interacting with the system are the <strong>Registered Student</strong> and the <strong>System Automation Engine</strong>.
    </p>
    <div class="diagram-box">
        ${svgs.useCaseSvg}
    </div>

    <div class="page-break"></div>

    <h3 class="sub-heading">2.3 Class Diagram</h3>
    <p>
        The domain model captures all academic relationships with foreign key integrity and bidirectional JPA associations.
    </p>
    <div class="diagram-box">
        ${svgs.classDiagramSvg}
    </div>

    <div class="page-break"></div>

    <h3 class="sub-heading">2.4 Activity Diagram</h3>
    <p>
        The workflow below illustrates the recommendation and run-rate scheduling process:
    </p>
    <div class="diagram-box">
        ${svgs.activitySvg}
    </div>

    <div class="page-break"></div>

    <h3 class="sub-heading">2.5 Database Design / Entity-Relationship (ER) Diagram</h3>
    <p>
        Relational entity model capturing students, subjects, units, topics, assessments, marks, and study tasks:
    </p>
    <div class="diagram-box">
        ${svgs.erDiagramSvg}
    </div>

    <div class="page-break"></div>

    <h3 class="sub-heading">2.6 Module Description</h3>
    <ol>
        <li><strong>User Authentication &amp; Session Management Module:</strong> Student registration with duplicate email validation, BCrypt cryptographic hashing for stored credentials, secure cookie session management, and isolated multi-tenant workspace separation.</li>
        <li><strong>Subject &amp; Syllabus Management Module:</strong> Full CRUD management for registered courses, credit weights, faculty, target grades, and hierarchical units/topics with auto-calculated completion percentages.</li>
        <li><strong>Course Evaluation Scheme Auto-Parser &amp; Target Run-Rate Calculator:</strong> Tokenizes LMS evaluation text, extracts grading components, calculates target grade cutoffs (A+, A, B), and computes required run-rate percentage with feasibility verdicts.</li>
        <li><strong>Academic Calendar &amp; Datesheet Management Module:</strong> Real-time detection of active semester week (e.g., Week 17 • Teaching), countdowns to Assessment Week (Week 18) and EST Exams (Weeks 19–20), compensatory Saturday timetables, and gazetted holiday countdowns.</li>
        <li><strong>Lab Assignments Tracker Module (Assignments 1 to 8):</strong> Tracks laboratory submission status, report completions, and viva readiness.</li>
        <li><strong>Adaptive Study Recommendation &amp; Timetable Generator Module:</strong> 4-factor heuristic priority calculator and conflict-free daily scheduling bounded by student-declared daily hours.</li>
        <li><strong>Focus Study &amp; Pomodoro Module:</strong> Integrated 25/5-minute Pomodoro timer with Web Audio sound synthesis chimes and habit streak tracking.</li>
        <li><strong>Academic Performance Analytics &amp; Simulator Module:</strong> Subject-wise performance radar, score trend tracking, and "What-If" CGPA scenario simulator.</li>
    </ol>

    <div class="page-break"></div>

    <!-- CHAPTER 3: IMPLEMENTATION -->
    <h2 class="section-heading">Chapter 3: Implementation</h2>

    <h3 class="sub-heading">3.1 Technologies and Tools Used</h3>
    <ul>
        <li><strong>Programming Language:</strong> Java 21 (Amazon Corretto / Eclipse Temurin LTS)</li>
        <li><strong>Framework:</strong> Spring Boot 3.3.5</li>
        <li><strong>Modules Used:</strong>
            <ul>
                <li><code>spring-boot-starter-web</code> (REST APIs &amp; static template routing)</li>
                <li><code>spring-boot-starter-data-jpa</code> (Hibernate 6 object-relational mapping)</li>
                <li><code>spring-boot-starter-security</code> (BCrypt password encoder, SecurityFilterChain)</li>
                <li><code>spring-boot-starter-validation</code> (JSR-380 Bean Validation)</li>
            </ul>
        </li>
        <li><strong>Database Engine:</strong> H2 Database Engine v2.2.224 in persistent file-backed MySQL mode (<code>jdbc:h2:file:./data/syllabix_db;MODE=MySQL</code>) with zero-code toggle to native MySQL 8.0.</li>
        <li><strong>Document Processing:</strong> Apache PDFBox 3.0.3 for PDF syllabus extraction.</li>
        <li><strong>Frontend Architecture:</strong> HTML5, CSS3, Vanilla ES6 JavaScript, Bootstrap 5.3.3, Bootstrap Icons 1.11.3, Chart.js 4.4.1.</li>
        <li><strong>Testing &amp; Verification:</strong> JUnit 5, Mockito 5.11, Maven Surefire Plugin 3.2.5.</li>
        <li><strong>Containerization &amp; Cloud Hosting:</strong> Docker (Multi-stage build), Render Cloud Hosting (<code>https://syllabix-platform-2.onrender.com</code>).</li>
    </ul>

    <h3 class="sub-heading">3.2 Project Structure / Package Structure</h3>
    <pre class="code-block">
c:\\Users\\jasmi\\OneDrive\\Desktop\\java_project
├── Dockerfile                                 # Multi-stage Docker cloud build
├── render.yaml                                # Render Blueprint cloud config
├── pom.xml                                    # Maven dependencies & plugins
├── README.md                                  # Complete project documentation
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── syllabix
│   │   │           ├── SyllabixApplication.java     # Application entry point
│   │   │           ├── config
│   │   │           │   ├── DataInitializer.java     # Demo seed data (7 3rd Year Courses)
│   │   │           │   ├── PasswordConfig.java      # BCrypt encoder bean
│   │   │           │   └── SecurityConfig.java      # Spring Security configuration
│   │   │           ├── controller
│   │   │           │   ├── AcademicCalendarApiController.java # Calendar REST APIs
│   │   │           │   ├── AnalyticsApiController.java        # Analytics REST APIs
│   │   │           │   ├── AssessmentApiController.java       # Assessment REST APIs
│   │   │           │   ├── AuthenticationApiController.java   # Auth REST APIs
│   │   │           │   ├── InsightsApiController.java         # Insights REST APIs
│   │   │           │   ├── MarksApiController.java            # Marks REST APIs
│   │   │           │   ├── PlannerApiController.java          # Planner REST APIs
│   │   │           │   ├── StudySessionApiController.java     # Session REST APIs
│   │   │           │   ├── SubjectApiController.java          # Subject & Lab APIs
│   │   │           │   └── SyllabusApiController.java         # Syllabus & PDF APIs
│   │   │           ├── dto                          # Data Transfer Objects
│   │   │           ├── entity                       # JPA Domain Entities
│   │   │           ├── exception                    # Global Exception Handlers
│   │   │           ├── repository                   # Spring Data Repositories
│   │   │           └── service                      # Algorithmic Services
│   │   └── resources
│   │       ├── application.properties               # Database & server configuration
│   │       └── static                               # Frontend UI Assets
│   └── test                                         # Unit & Regression Test Suites
    </pre>

    <h3 class="sub-heading">3.3 Description of Classes and Interfaces</h3>
    <ul>
        <li><strong><code>RecommendationEngineService.java</code>:</strong> Implements the normalized 4-factor heuristic priority equation (Urgency, Incompletion, Difficulty, Marks Deficit).</li>
        <li><strong><code>CourseEvaluationParserService.java</code>:</strong> Employs regular expressions to parse LMS evaluation descriptions, match recorded marks, calculate target cutoffs, and evaluate run-rate percentages.</li>
        <li><strong><code>AcademicCalendarService.java</code>:</strong> Maintains the 20-week date ranges for TIET ODD SEM 2026-27, providing dynamic calculation of the active semester week and distance to milestones.</li>
        <li><strong><code>SecurityConfig.java</code>:</strong> Configures stateless session-backed authentication, CSRF exemptions for REST endpoints, and BCrypt bean isolation.</li>
        <li><strong><code>GlobalExceptionHandler.java</code>:</strong> Intercepts exceptions and returns structured JSON error responses with proper HTTP status codes.</li>
    </ul>

    <div class="page-break"></div>

    <h3 class="sub-heading">3.4 Implementation of Major Modules</h3>
    <h4 class="sub-sub-heading">A. Course Evaluation Scheme Parser &amp; Run-Rate Computation</h4>
    <p>
        When a student inputs an LMS evaluation notice, component patterns identify <code>LAB Evaluation</code>, <code>Sessional Quiz</code>, <code>MST</code>, and <code>EST</code> along with max marks. The service calculates target marks needed, required run-rate percentage, and assigns feasibility verdicts (<em>ACHIEVABLE</em>, <em>STRETCH</em>, <em>UNREALISTIC</em>).
    </p>

    <h4 class="sub-sub-heading">B. Academic Calendar Week Calculator</h4>
    <p>
        Using Java's <code>LocalDate</code>, the calendar service maps dates to corresponding semester weeks. For instance, <strong>November 25, 2026</strong> is evaluated as:
    </p>
    <ul>
        <li><strong>Week 17: Teaching</strong> (23 Nov – 28 Nov 2026).</li>
        <li>Followed immediately by <strong>Week 18: Lab Evaluations</strong> (30 Nov – 05 Dec 2026).</li>
        <li>Followed by <strong>Weeks 19–20: EST Examinations</strong> (07 Dec – 19 Dec 2026).</li>
    </ul>

    <h3 class="sub-heading">3.5 Exception Handling</h3>
    <p>
        The platform implements centralized exception handling using <code>@RestControllerAdvice</code>, gracefully handling <code>ResourceNotFoundException</code> (404), <code>MethodArgumentNotValidException</code> (400), and <code>AuthenticationException</code> (401).
    </p>

    <h3 class="sub-heading">3.6 Collection Framework Usage</h3>
    <p>
        The Java Collections Framework is utilized extensively throughout the business logic: <code>List&lt;T&gt;</code> and <code>ArrayList&lt;T&gt;</code> for ordered entities, <code>Map&lt;String, Double&gt;</code> for availability mapping, and the Java 8+ Streams API (<code>filter</code>, <code>map</code>, <code>sorted</code>, <code>collect</code>) for the recommendation pipeline:
    </p>
    <pre class="code-block">
List&lt;Topic&gt; rankedTopics = pendingTopics.stream()
    .sorted(Comparator.comparingDouble(t -> -calculatePriorityScore(t, student)))
    .collect(Collectors.toList());
    </pre>

    <h3 class="sub-heading">3.7 Database Connectivity</h3>
    <p>
        Managed through Spring Data JPA over <strong>HikariCP</strong> (max pool size 10, 30s timeout). The persistent file-backed H2 engine runs in MySQL compatibility mode (<code>MODE=MySQL</code>), ensuring zero-dependency deployment while maintaining native compatibility with MySQL 8.0.
    </p>

    <h3 class="sub-heading">3.8 Important Code Snippets</h3>
    <h4 class="sub-sub-heading">Snippet 1: The 4-Factor Weighted Priority Algorithm (<code>RecommendationEngineService.java</code>)</h4>
    <pre class="code-block">
public double calculateTopicPriorityScore(Topic topic, Student student) {
    // Factor 1: Upcoming Exam Urgency (35%)
    double urgencyScore = calculateExamUrgencyScore(topic.getSubject(), student);
    
    // Factor 2: Syllabus Incompletion (30%)
    double incompletionScore = calculateSyllabusIncompletionScore(topic.getSubject());
    
    // Factor 3: Topic Difficulty (20%)
    double difficultyScore = mapDifficultyToScore(topic.getDifficultyLevel());
    
    // Factor 4: Previous Academic Marks Deficit (15%)
    double performanceDeficitScore = calculatePerformanceDeficitScore(topic.getSubject(), student);
    
    // Weighted Heuristic Summation
    double finalScore = (0.35 * urgencyScore) +
                        (0.30 * incompletionScore) +
                        (0.20 * difficultyScore) +
                        (0.15 * performanceDeficitScore);
                        
    return Math.round(Math.min(100.0, Math.max(0.0, finalScore)) * 10.0) / 10.0;
}
    </pre>

    <h4 class="sub-sub-heading">Snippet 2: Target Grade Run-Rate Calculation (<code>CourseEvaluationParserService.java</code>)</h4>
    <pre class="code-block">
double targetCutoff = getCutoffPercentageForGrade(targetGrade);
double neededTotalMarks = dto.getTotalMarks() * (targetCutoff / 100.0);
double marksStillNeeded = Math.max(0.0, neededTotalMarks - scoredSoFar);

double runRate = 0.0;
if (remainingMarks > 0) {
    runRate = (marksStillNeeded / remainingMarks) * 100.0;
}

String feasibility;
if (runRate <= 65.0) {
    feasibility = "ACHIEVABLE";
} else if (runRate <= 85.0) {
    feasibility = "STRETCH";
} else {
    feasibility = "UNREALISTIC";
}
dto.setRequiredRunRatePercentage(Math.round(runRate * 10.0) / 10.0);
dto.setFeasibilityStatus(feasibility);
    </pre>

    <div class="page-break"></div>

    <!-- CHAPTER 4: RESULTS AND SCREENSHOTS -->
    <h2 class="section-heading">Chapter 4: Results and Screenshots</h2>

    <h3 class="sub-heading">4.1 Main Application Screen</h3>
    <p>
        The main dashboard serves as the central cockpit for student <strong>Akshat Gupta (<code>1024030605</code>)</strong>. It features the formal institutional masthead of Thapar Institute of Engineering &amp; Technology, the student academic status ribbon indicating Regular standing in Third Year (<code>2627ODDSEM</code>) with 7 enrolled courses (25.0 credits), the AI recommendation directive, the LMS evaluation run-rate calculator, and the lab tracker.
    </p>

    <h3 class="sub-heading">4.2 Functional Screenshots &amp; Live Outputs</h3>

    <div class="callout-box">
        <h4 style="margin-top: 0; color: #800000;">1. Thapar LMS Authentication Portal (<code>/login</code>)</h4>
        <p>Recreation of the Thapar LMS login interface with academic notice advisory for <code>2627ODDSEM</code> and 1-Click quick login for Jasmine Kaur.</p>
        <pre class="code-block" style="background:#fff;">
[THAPAR-LMS + Syllabix AI]
Notice: Third-Year curriculum (2627ODDSEM) course registrations and continuous evaluation timelines are active.
Pre-fill: jkaur_be24@thapar.edu | password123
        </pre>
    </div>

    <div class="callout-box">
        <h4 style="margin-top: 0; color: #800000;">2. Institutional Dashboard &amp; Course Evaluation Run-Rate (<code>/</code>)</h4>
        <p>Evaluation Parser Output for <code>UCS553</code> (Enterprise Web Application):</p>
        <ul>
            <li><strong>Parsed Components:</strong> LAB Evaluation: 20 Marks (Week 18), Sessional Quiz: 10 Marks (Week 12), MST: 30 Marks, EST: 40 Marks</li>
            <li><strong>Current Scored Marks:</strong> 23.5 / 25.0</li>
            <li><strong>Remaining Marks Available:</strong> 75.0 Marks</li>
            <li><strong>Target Grade A (Cutoff &ge; 80%):</strong> Requires 56.5 marks out of remaining 75.0 marks.</li>
            <li><strong>Required Run-Rate:</strong> <strong>75.3%</strong> &rarr; <span class="status-badge badge-warning">STRETCH (Achievable with consistent preparation)</span></li>
        </ul>
    </div>

    <div class="callout-box">
        <h4 style="margin-top: 0; color: #800000;">3. Course Catalog &amp; Registered Curriculum (<code>/subjects</code>)</h4>
        <p>Displays all 7 Third-Year subjects with credit badges, faculty names, and syllabus progress bars:</p>
        <table class="report-table">
            <thead>
                <tr>
                    <th>Course Code</th>
                    <th>Course Title</th>
                    <th>Credits</th>
                    <th>Course Coordinator</th>
                    <th>Completion</th>
                </tr>
            </thead>
            <tbody>
                <tr><td><code>UCS510</code></td><td>Computer Architecture and Organization</td><td>4.0</td><td>Dr. Rajesh Kumar</td><td>45%</td></tr>
                <tr><td><code>UCS553</code></td><td>Enterprise Web Application</td><td>4.0</td><td>Prof. Vivek Sharma</td><td>60%</td></tr>
                <tr><td><code>UCS421</code></td><td>Ethics and Risk Mitigation in Computing</td><td>2.0</td><td>Dr. Meenakshi Sood</td><td>75%</td></tr>
                <tr><td><code>UCS548</code></td><td>Foundations of Data Science</td><td>4.0</td><td>Dr. Anupam Garg</td><td>70%</td></tr>
                <tr><td><code>UCS615</code></td><td>Image Processing</td><td>3.0</td><td>Dr. Harpreet Kaur</td><td>50%</td></tr>
                <tr><td><code>UML501</code></td><td>Machine Learning</td><td>4.0</td><td>Dr. Neeraj Kumar</td><td>50%</td></tr>
                <tr><td><code>UCS503</code></td><td>Software Engineering</td><td>4.0</td><td>Prof. Sanjeev Sofat</td><td>60%</td></tr>
            </tbody>
        </table>
    </div>

    <div class="callout-box">
        <h4 style="margin-top: 0; color: #800000;">4. Official 20-Week Thapar Academic Calendar (<code>/calendar</code>)</h4>
        <p>
            Features the 20-Week Semester Strip auto-highlighting <strong>Week 17 (Current • 23–28 Nov 2026)</strong>, Datesheet Milestones (MST Exams, Diwali Break, Lab Evaluations, EST Exams, Answer Sheet Review), and Gazetted Holidays with live countdowns.
        </p>
    </div>

    <div class="page-break"></div>

    <h3 class="sub-heading">4.3 Input and Output Demonstrations</h3>
    <table class="report-table">
        <thead>
            <tr>
                <th style="width: 28%;">User Action / Input</th>
                <th style="width: 32%;">System Processing</th>
                <th style="width: 40%;">Displayed Output</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Click <strong>"Thapar Student"</strong> on Login Screen</td>
                <td>Injects credentials <code>jkaur_be24@thapar.edu</code> &amp; <code>password123</code></td>
                <td>Fields populated; authenticates with BCrypt; redirects to Dashboard.</td>
            </tr>
            <tr>
                <td>Paste raw LMS evaluation text for <code>UCS553</code></td>
                <td>Regex extracts 4 components, checks existing marks</td>
                <td>Displays component cards; computes remaining marks (75.0) and required run-rate (75.3%).</td>
            </tr>
            <tr>
                <td>Select date in Academic Calendar</td>
                <td>Filters assessments on selected date</td>
                <td>Highlights matching events with red (Urgent), orange (Approaching), or green badges.</td>
            </tr>
            <tr>
                <td>Click <strong>"Generate Study Plan"</strong> with 3.5 hrs availability</td>
                <td>Priority algorithm ranks pending topics</td>
                <td>Schedules 3 targeted study tasks (60m, 60m, 45m) with 15m intervals; leaves 30m buffer.</td>
            </tr>
        </tbody>
    </table>

    <h3 class="sub-heading">4.4 Error Handling Demonstrations</h3>
    <table class="report-table">
        <thead>
            <tr>
                <th style="width: 25%;">Scenario</th>
                <th style="width: 25%;">Input</th>
                <th style="width: 50%;">System Response</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Invalid Login Credentials</strong></td>
                <td>Incorrect password</td>
                <td><code>HTTP 401 Unauthorized</code>: "Invalid email or password" toast displayed.</td>
            </tr>
            <tr>
                <td><strong>Duplicate Course Registration</strong></td>
                <td>Registering <code>UCS510</code> again</td>
                <td><code>HTTP 400 Bad Request</code>: "Subject code UCS510 already registered for this student".</td>
            </tr>
            <tr>
                <td><strong>Negative Study Duration</strong></td>
                <td>Entering <code>-45</code> minutes</td>
                <td><code>HTTP 400 Bad Request</code>: Validation error "Duration must be greater than 0".</td>
            </tr>
            <tr>
                <td><strong>Unrealistic Target Run-Rate</strong></td>
                <td>Setting Target Grade A+ when marks already lost</td>
                <td>Calculates run-rate &gt; 100%; flags verdict as <span class="status-badge badge-danger">UNREALISTIC</span> and suggests achievable Grade B.</td>
            </tr>
        </tbody>
    </table>

    <div class="page-break"></div>

    <!-- REFERENCES -->
    <h2 class="section-heading">References</h2>
    <ol style="font-size: 11pt; line-height: 1.8;">
        <li><strong>Spring Boot Framework Documentation:</strong> Spring Boot Reference Guide (v3.3.5), VMware Tanzu / Broadcom. Available at: <a href="https://docs.spring.io/spring-boot/" target="_blank">https://docs.spring.io/spring-boot/</a></li>
        <li><strong>Spring Security Architecture:</strong> Spring Security Core Specification, VMware Tanzu. Available at: <a href="https://docs.spring.io/spring-security/" target="_blank">https://docs.spring.io/spring-security/</a></li>
        <li><strong>Hibernate ORM 6.5 User Guide:</strong> Red Hat, Inc. Available at: <a href="https://hibernate.org/orm/documentation/" target="_blank">https://hibernate.org/orm/documentation/</a></li>
        <li><strong>Apache PDFBox 3.0.3 API Reference:</strong> The Apache Software Foundation. Available at: <a href="https://pdfbox.apache.org/" target="_blank">https://pdfbox.apache.org/</a></li>
        <li><strong>Thapar Institute of Engineering &amp; Technology:</strong> Official Academic Calendar &amp; Datesheet for ODD SEMESTER 2026–2027, Office of Dean of Academic Affairs (DoAA), TIET Patiala.</li>
        <li><strong>Chart.js Documentation:</strong> Open source HTML5 Canvas charting library (v4.4.1). Available at: <a href="https://www.chartjs.org/" target="_blank">https://www.chartjs.org/</a></li>
        <li><strong>Production Cloud Deployment Link:</strong> Syllabix Cloud Web Service: <a href="https://syllabix-platform-2.onrender.com" target="_blank"><strong>https://syllabix-platform-2.onrender.com</strong></a></li>
        <li><strong>GitHub Project Repository:</strong> Source code repository: <a href="https://github.com/jasminekaur7/syllabix-platform" target="_blank"><strong>https://github.com/jasminekaur7/syllabix-platform</strong></a></li>
    </ol>

</div>

</body>
</html>
`;

fs.writeFileSync(path.join(__dirname, 'UCS553_Mini_Project_Report_Syllabix.html'), htmlContent, 'utf8');
console.log('Successfully compiled UCS553_Mini_Project_Report_Syllabix.html with embedded SVGs');
