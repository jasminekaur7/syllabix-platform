const fs = require('fs');
const path = require('path');

// 1. System Architecture SVG
const architectureSvg = `
<svg width="780" height="420" viewBox="0 0 780 420" xmlns="http://www.w3.org/2000/svg" style="font-family:'Segoe UI',Arial,sans-serif;">
  <defs>
    <linearGradient id="gradHeader" x1="0%" y1="0%" x2="100%" y2="0%">
      <stop offset="0%" style="stop-color:#800000;stop-opacity:1" />
      <stop offset="100%" style="stop-color:#b30000;stop-opacity:1" />
    </linearGradient>
    <linearGradient id="gradBox" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#f8fafc;stop-opacity:1" />
      <stop offset="100%" style="stop-color:#edf2f7;stop-opacity:1" />
    </linearGradient>
    <marker id="arrow" viewBox="0 0 10 10" refX="5" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 0 L 10 5 L 0 10 z" fill="#002D62" />
    </marker>
  </defs>

  <!-- LAYER 1: CLIENT -->
  <rect x="20" y="10" width="740" height="60" rx="6" fill="#f0f4f8" stroke="#002D62" stroke-width="1.5"/>
  <rect x="20" y="10" width="740" height="22" rx="6" fill="#002D62"/>
  <text x="390" y="26" fill="#ffffff" font-size="11" font-weight="bold" text-anchor="middle">CLIENT PRESENTATION LAYER (BROWSER)</text>
  <rect x="35" y="38" width="220" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="145" y="54" fill="#1e293b" font-size="10" font-weight="bold" text-anchor="middle">Institutional UI (HTML5 / Bootstrap 5)</text>
  <rect x="275" y="38" width="220" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="385" y="54" fill="#1e293b" font-size="10" font-weight="bold" text-anchor="middle">Chart.js Analytics &amp; Visualizations</text>
  <rect x="515" y="38" width="230" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="630" y="54" fill="#1e293b" font-size="10" font-weight="bold" text-anchor="middle">HTML5 Web Audio Chime Synthesizer</text>

  <!-- ARROW 1-2 -->
  <line x1="390" y1="70" x2="390" y2="90" stroke="#002D62" stroke-width="2" marker-end="url(#arrow)" />
  <text x="405" y="83" fill="#64748b" font-size="9" font-weight="bold">HTTP REST / JSON</text>

  <!-- LAYER 2: SECURITY -->
  <rect x="140" y="92" width="500" height="42" rx="6" fill="#fff1f2" stroke="#e11d48" stroke-width="1.5"/>
  <text x="390" y="110" fill="#9f1239" font-size="11" font-weight="bold" text-anchor="middle">Spring Security Filter Chain &amp; Auth Filter</text>
  <text x="390" y="125" fill="#475569" font-size="9.5" text-anchor="middle">BCrypt Hashing &bull; Session Cookie Management &bull; CSRF Validation</text>

  <!-- ARROW 2-3 -->
  <line x1="390" y1="134" x2="390" y2="152" stroke="#002D62" stroke-width="2" marker-end="url(#arrow)" />

  <!-- LAYER 3: CONTROLLERS -->
  <rect x="20" y="154" width="740" height="60" rx="6" fill="#f8fafc" stroke="#475569" stroke-width="1.5"/>
  <rect x="20" y="154" width="740" height="20" rx="6" fill="#475569"/>
  <text x="390" y="168" fill="#ffffff" font-size="11" font-weight="bold" text-anchor="middle">REST CONTROLLER LAYER (Spring MVC)</text>
  <rect x="30" y="180" width="130" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="95" y="196" fill="#1e293b" font-size="9" font-weight="bold" text-anchor="middle">AuthApiController</text>
  <rect x="175" y="180" width="130" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="240" y="196" fill="#1e293b" font-size="9" font-weight="bold" text-anchor="middle">SubjectApiController</text>
  <rect x="320" y="180" width="135" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="387" y="196" fill="#1e293b" font-size="9" font-weight="bold" text-anchor="middle">PlannerApiController</text>
  <rect x="470" y="180" width="140" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="540" y="196" fill="#1e293b" font-size="9" font-weight="bold" text-anchor="middle">CalendarApiController</text>
  <rect x="625" y="180" width="125" height="24" rx="4" fill="#ffffff" stroke="#cbd5e1"/>
  <text x="687" y="196" fill="#1e293b" font-size="9" font-weight="bold" text-anchor="middle">InsightsApiController</text>

  <!-- ARROW 3-4 -->
  <line x1="390" y1="214" x2="390" y2="232" stroke="#002D62" stroke-width="2" marker-end="url(#arrow)" />

  <!-- LAYER 4: BUSINESS SERVICES -->
  <rect x="20" y="234" width="740" height="66" rx="6" fill="#fdf2f2" stroke="#800000" stroke-width="1.5"/>
  <rect x="20" y="234" width="740" height="20" rx="6" fill="url(#gradHeader)"/>
  <text x="390" y="248" fill="#ffffff" font-size="11" font-weight="bold" text-anchor="middle">BUSINESS LOGIC &amp; ALGORITHMIC LAYER</text>
  <rect x="30" y="260" width="220" height="30" rx="4" fill="#ffffff" stroke="#fca5a5"/>
  <text x="140" y="274" fill="#800000" font-size="9.5" font-weight="bold" text-anchor="middle">RecommendationEngineService</text>
  <text x="140" y="286" fill="#475569" font-size="8" text-anchor="middle">4-Factor Normalized Priority Model</text>

  <rect x="265" y="260" width="220" height="30" rx="4" fill="#ffffff" stroke="#fca5a5"/>
  <text x="375" y="274" fill="#800000" font-size="9.5" font-weight="bold" text-anchor="middle">CourseEvaluationParserService</text>
  <text x="375" y="286" fill="#475569" font-size="8" text-anchor="middle">LMS Regex &amp; Grade Run-Rate</text>

  <rect x="500" y="260" width="250" height="30" rx="4" fill="#ffffff" stroke="#fca5a5"/>
  <text x="625" y="274" fill="#800000" font-size="9.5" font-weight="bold" text-anchor="middle">AcademicCalendarService</text>
  <text x="625" y="286" fill="#475569" font-size="8" text-anchor="middle">20-Week TIET Datesheet Engine</text>

  <!-- ARROW 4-5 -->
  <line x1="390" y1="300" x2="390" y2="318" stroke="#002D62" stroke-width="2" marker-end="url(#arrow)" />

  <!-- LAYER 5: PERSISTENCE & DB -->
  <rect x="20" y="320" width="740" height="85" rx="6" fill="#f0fdf4" stroke="#16a34a" stroke-width="1.5"/>
  <rect x="20" y="320" width="740" height="20" rx="6" fill="#16a34a"/>
  <text x="390" y="334" fill="#ffffff" font-size="11" font-weight="bold" text-anchor="middle">DATA PERSISTENCE &amp; STORAGE LAYER</text>

  <rect x="40" y="348" width="320" height="46" rx="4" fill="#ffffff" stroke="#86efac"/>
  <text x="200" y="367" fill="#14532d" font-size="10.5" font-weight="bold" text-anchor="middle">Spring Data JPA &bull; Hibernate 6 ORM</text>
  <text x="200" y="382" fill="#475569" font-size="9" text-anchor="middle">HikariCP High-Performance Connection Pool</text>

  <rect x="400" y="348" width="340" height="46" rx="4" fill="#ffffff" stroke="#86efac"/>
  <text x="570" y="367" fill="#14532d" font-size="10.5" font-weight="bold" text-anchor="middle">Relational Database Engine</text>
  <text x="570" y="382" fill="#475569" font-size="9" text-anchor="middle">Persistent H2 (MySQL Mode) &bull; Production MySQL 8.0</text>
</svg>
`;

// 2. Use Case Diagram SVG
const useCaseSvg = `
<svg width="780" height="460" viewBox="0 0 780 460" xmlns="http://www.w3.org/2000/svg" style="font-family:'Segoe UI',Arial,sans-serif;">
  <!-- System Boundary -->
  <rect x="180" y="20" width="420" height="425" rx="8" fill="#ffffff" stroke="#800000" stroke-width="2"/>
  <rect x="180" y="20" width="420" height="28" rx="8" fill="#800000"/>
  <text x="390" y="39" fill="#ffffff" font-size="12" font-weight="bold" text-anchor="middle">Syllabix Academic Platform</text>

  <!-- Left Actor: Registered Student -->
  <circle cx="80" cy="180" r="16" fill="#f8fafc" stroke="#002D62" stroke-width="2"/>
  <line x1="80" y1="196" x2="80" y2="235" stroke="#002D62" stroke-width="2"/>
  <line x1="55" y1="210" x2="105" y2="210" stroke="#002D62" stroke-width="2"/>
  <line x1="80" y1="235" x2="60" y2="275" stroke="#002D62" stroke-width="2"/>
  <line x1="80" y1="235" x2="100" y2="275" stroke="#002D62" stroke-width="2"/>
  <text x="80" y="295" fill="#002D62" font-size="11" font-weight="bold" text-anchor="middle">Registered Student</text>
  <text x="80" y="310" fill="#64748b" font-size="9" text-anchor="middle">(Jasmine Kaur)</text>

  <!-- Right Actor: Core Engine -->
  <rect x="655" y="195" width="85" height="50" rx="6" fill="#f8fafc" stroke="#800000" stroke-width="2"/>
  <text x="697" y="218" fill="#800000" font-size="10.5" font-weight="bold" text-anchor="middle">&lt;&lt;System&gt;&gt;</text>
  <text x="697" y="233" fill="#1e293b" font-size="10" font-weight="bold" text-anchor="middle">Syllabix Core</text>

  <!-- Use Case Ellipses -->
  <!-- UC1 -->
  <ellipse cx="390" cy="70" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="74" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC1: Authenticate / Register / BCrypt Login</text>
  <line x1="96" y1="180" x2="230" y2="70" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC2 -->
  <ellipse cx="390" cy="107" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="111" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC2: View Academic Dashboard &amp; Roll Ribbon</text>
  <line x1="96" y1="185" x2="230" y2="107" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC3 -->
  <ellipse cx="390" cy="144" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="148" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC3: Manage 7 Registered Subjects &amp; Units</text>
  <line x1="96" y1="195" x2="230" y2="144" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC4 -->
  <ellipse cx="390" cy="181" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="185" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC4: Extract Syllabus PDF (Apache PDFBox)</text>
  <line x1="96" y1="210" x2="230" y2="181" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC5 -->
  <ellipse cx="390" cy="218" rx="160" ry="15" fill="#fdf2f8" stroke="#db2777" stroke-width="1.4"/>
  <text x="390" y="222" fill="#be185d" font-size="9.5" font-weight="bold" text-anchor="middle">UC5: Paste LMS Scheme &amp; Calculate Run-Rate</text>
  <line x1="96" y1="215" x2="230" y2="218" stroke="#94a3b8" stroke-width="1.2"/>
  <line x1="550" y1="218" x2="655" y2="215" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC6 -->
  <ellipse cx="390" cy="255" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="259" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC6: Track Lab Assignments (Assignments 1-8)</text>
  <line x1="96" y1="225" x2="230" y2="255" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC7 -->
  <ellipse cx="390" cy="292" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="296" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC7: Consult 20-Week TIET Academic Calendar</text>
  <line x1="96" y1="235" x2="230" y2="292" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC8 -->
  <ellipse cx="390" cy="329" rx="160" ry="15" fill="#fef2f2" stroke="#dc2626" stroke-width="1.4"/>
  <text x="390" y="333" fill="#b91c1c" font-size="9.5" font-weight="bold" text-anchor="middle">UC8: Generate Adaptive Daily Study Timetable</text>
  <line x1="96" y1="245" x2="230" y2="329" stroke="#94a3b8" stroke-width="1.2"/>
  <line x1="550" y1="329" x2="655" y2="225" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC9 -->
  <ellipse cx="390" cy="366" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="370" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC9: Execute Pomodoro Study Session &amp; Streak</text>
  <line x1="96" y1="255" x2="230" y2="366" stroke="#94a3b8" stroke-width="1.2"/>

  <!-- UC10 -->
  <ellipse cx="390" cy="403" rx="160" ry="15" fill="#f0f9ff" stroke="#0284c7" stroke-width="1.2"/>
  <text x="390" y="407" fill="#0369a1" font-size="9.5" font-weight="bold" text-anchor="middle">UC10: Simulate What-If CGPA &amp; Exam Readiness</text>
  <line x1="96" y1="265" x2="230" y2="403" stroke="#94a3b8" stroke-width="1.2"/>
</svg>
`;

// 3. Class Diagram SVG
const classDiagramSvg = `
<svg width="780" height="490" viewBox="0 0 780 490" xmlns="http://www.w3.org/2000/svg" style="font-family:'Segoe UI',Arial,sans-serif;">
  <!-- Student Class -->
  <rect x="20" y="10" width="165" height="150" rx="4" fill="#ffffff" stroke="#002D62" stroke-width="1.5"/>
  <rect x="20" y="10" width="165" height="22" rx="4" fill="#002D62"/>
  <text x="102" y="26" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">Student</text>
  <text x="26" y="45" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="26" y="58" fill="#334155" font-size="8.5">+ String fullName</text>
  <text x="26" y="71" fill="#334155" font-size="8.5">+ String email</text>
  <text x="26" y="84" fill="#334155" font-size="8.5">+ String password (BCrypt)</text>
  <text x="26" y="97" fill="#334155" font-size="8.5">+ Integer semester (5)</text>
  <text x="26" y="110" fill="#334155" font-size="8.5">+ Double dailyHours</text>
  <line x1="20" y1="116" x2="185" y2="116" stroke="#cbd5e1" stroke-width="1"/>
  <text x="26" y="130" fill="#0f172a" font-size="8.5">+ register()</text>
  <text x="26" y="143" fill="#0f172a" font-size="8.5">+ updateAvailability()</text>

  <!-- Subject Class -->
  <rect x="235" y="10" width="155" height="150" rx="4" fill="#ffffff" stroke="#800000" stroke-width="1.5"/>
  <rect x="235" y="10" width="155" height="22" rx="4" fill="#800000"/>
  <text x="312" y="26" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">Subject</text>
  <text x="241" y="45" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="241" y="58" fill="#334155" font-size="8.5">+ String code (UCS553)</text>
  <text x="241" y="71" fill="#334155" font-size="8.5">+ String name</text>
  <text x="241" y="84" fill="#334155" font-size="8.5">+ Integer credits (4.0)</text>
  <text x="241" y="97" fill="#334155" font-size="8.5">+ String targetGrade</text>
  <text x="241" y="110" fill="#334155" font-size="8.5">+ String teacherName</text>
  <line x1="235" y1="116" x2="390" y2="116" stroke="#cbd5e1" stroke-width="1"/>
  <text x="241" y="130" fill="#0f172a" font-size="8.5">+ getCompletionRate()</text>
  <text x="241" y="143" fill="#0f172a" font-size="8.5">+ calculateDeficit()</text>

  <!-- Unit Class -->
  <rect x="440" y="10" width="145" height="110" rx="4" fill="#ffffff" stroke="#475569" stroke-width="1.5"/>
  <rect x="440" y="10" width="145" height="22" rx="4" fill="#475569"/>
  <text x="512" y="26" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">Unit</text>
  <text x="446" y="45" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="446" y="58" fill="#334155" font-size="8.5">+ Integer unitNumber</text>
  <text x="446" y="71" fill="#334155" font-size="8.5">+ String title</text>
  <text x="446" y="84" fill="#334155" font-size="8.5">+ String description</text>
  <line x1="440" y1="90" x2="585" y2="90" stroke="#cbd5e1" stroke-width="1"/>
  <text x="446" y="103" fill="#0f172a" font-size="8.5">+ getTopics()</text>

  <!-- Topic Class -->
  <rect x="620" y="10" width="145" height="150" rx="4" fill="#ffffff" stroke="#475569" stroke-width="1.5"/>
  <rect x="620" y="10" width="145" height="22" rx="4" fill="#475569"/>
  <text x="692" y="26" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">Topic</text>
  <text x="626" y="45" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="626" y="58" fill="#334155" font-size="8.5">+ String name</text>
  <text x="626" y="71" fill="#334155" font-size="8.5">+ String difficultyLevel</text>
  <text x="626" y="84" fill="#334155" font-size="8.5">+ Integer durationMin</text>
  <text x="626" y="97" fill="#334155" font-size="8.5">+ String completionStatus</text>
  <text x="626" y="110" fill="#334155" font-size="8.5">+ Integer confidence</text>
  <line x1="620" y1="116" x2="765" y2="116" stroke="#cbd5e1" stroke-width="1"/>
  <text x="626" y="130" fill="#0f172a" font-size="8.5">+ markComplete()</text>
  <text x="626" y="143" fill="#0f172a" font-size="8.5">+ updateConfidence()</text>

  <!-- Connection: Student -> Subject (1 to *) -->
  <line x1="185" y1="80" x2="235" y2="80" stroke="#002D62" stroke-width="1.5"/>
  <text x="190" y="74" fill="#002D62" font-size="8" font-weight="bold">1</text>
  <text x="220" y="74" fill="#002D62" font-size="8" font-weight="bold">*</text>

  <!-- Connection: Subject -> Unit (1 to *) -->
  <line x1="390" y1="65" x2="440" y2="65" stroke="#475569" stroke-width="1.5"/>
  <text x="395" y="59" fill="#475569" font-size="8" font-weight="bold">1</text>
  <text x="428" y="59" fill="#475569" font-size="8" font-weight="bold">*</text>

  <!-- Connection: Unit -> Topic (1 to *) -->
  <line x1="585" y1="65" x2="620" y2="65" stroke="#475569" stroke-width="1.5"/>
  <text x="590" y="59" fill="#475569" font-size="8" font-weight="bold">1</text>
  <text x="608" y="59" fill="#475569" font-size="8" font-weight="bold">*</text>

  <!-- SECOND ROW -->
  <!-- Assessment Class -->
  <rect x="20" y="210" width="165" height="130" rx="4" fill="#ffffff" stroke="#b91c1c" stroke-width="1.5"/>
  <rect x="20" y="210" width="165" height="22" rx="4" fill="#b91c1c"/>
  <text x="102" y="226" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">Assessment</text>
  <text x="26" y="245" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="26" y="258" fill="#334155" font-size="8.5">+ String title (MST/EST)</text>
  <text x="26" y="271" fill="#334155" font-size="8.5">+ String type</text>
  <text x="26" y="284" fill="#334155" font-size="8.5">+ LocalDateTime date</text>
  <text x="26" y="297" fill="#334155" font-size="8.5">+ Double maxMarks</text>
  <line x1="20" y1="303" x2="185" y2="303" stroke="#cbd5e1" stroke-width="1"/>
  <text x="26" y="318" fill="#0f172a" font-size="8.5">+ isApproaching()</text>
  <text x="26" y="331" fill="#0f172a" font-size="8.5">+ calculateDaysLeft()</text>

  <!-- Marks Class -->
  <rect x="235" y="210" width="155" height="120" rx="4" fill="#ffffff" stroke="#b91c1c" stroke-width="1.5"/>
  <rect x="235" y="210" width="155" height="22" rx="4" fill="#b91c1c"/>
  <text x="312" y="226" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">Marks</text>
  <text x="241" y="245" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="241" y="258" fill="#334155" font-size="8.5">+ Double marksObtained</text>
  <text x="241" y="271" fill="#334155" font-size="8.5">+ Double maxMarks</text>
  <text x="241" y="284" fill="#334155" font-size="8.5">+ LocalDate date</text>
  <line x1="235" y1="290" x2="390" y2="290" stroke="#cbd5e1" stroke-width="1"/>
  <text x="241" y="306" fill="#0f172a" font-size="8.5">+ getPercentage()</text>

  <!-- StudyTask Class -->
  <rect x="430" y="210" width="165" height="140" rx="4" fill="#ffffff" stroke="#16a34a" stroke-width="1.5"/>
  <rect x="430" y="210" width="165" height="22" rx="4" fill="#16a34a"/>
  <text x="512" y="226" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">StudyTask</text>
  <text x="436" y="245" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="436" y="258" fill="#334155" font-size="8.5">+ LocalDate scheduledDate</text>
  <text x="436" y="271" fill="#334155" font-size="8.5">+ LocalTime startTime</text>
  <text x="436" y="284" fill="#334155" font-size="8.5">+ Integer durationMinutes</text>
  <text x="436" y="297" fill="#334155" font-size="8.5">+ Double priorityScore</text>
  <text x="436" y="310" fill="#334155" font-size="8.5">+ Boolean isCompleted</text>
  <line x1="430" y1="316" x2="595" y2="316" stroke="#cbd5e1" stroke-width="1"/>
  <text x="436" y="331" fill="#0f172a" font-size="8.5">+ markComplete()</text>

  <!-- StudySession Class -->
  <rect x="625" y="210" width="140" height="120" rx="4" fill="#ffffff" stroke="#16a34a" stroke-width="1.5"/>
  <rect x="625" y="210" width="140" height="22" rx="4" fill="#16a34a"/>
  <text x="695" y="226" fill="#ffffff" font-size="10.5" font-weight="bold" text-anchor="middle">StudySession</text>
  <text x="631" y="245" fill="#334155" font-size="8.5">+ Long id</text>
  <text x="631" y="258" fill="#334155" font-size="8.5">+ LocalDateTime start</text>
  <text x="631" y="271" fill="#334155" font-size="8.5">+ LocalDateTime end</text>
  <text x="631" y="284" fill="#334155" font-size="8.5">+ Integer durationMin</text>
  <line x1="625" y1="290" x2="765" y2="290" stroke="#cbd5e1" stroke-width="1"/>
  <text x="631" y="306" fill="#0f172a" font-size="8.5">+ logStreak()</text>

  <!-- Vertical Links -->
  <line x1="102" y1="160" x2="102" y2="210" stroke="#b91c1c" stroke-width="1.5"/>
  <text x="106" y="175" fill="#b91c1c" font-size="8" font-weight="bold">1</text>
  <text x="106" y="200" fill="#b91c1c" font-size="8" font-weight="bold">*</text>

  <line x1="312" y1="160" x2="312" y2="210" stroke="#b91c1c" stroke-width="1.5"/>
  <text x="316" y="175" fill="#b91c1c" font-size="8" font-weight="bold">1</text>
  <text x="316" y="200" fill="#b91c1c" font-size="8" font-weight="bold">*</text>

  <line x1="695" y1="160" x2="540" y2="210" stroke="#16a34a" stroke-width="1.5"/>
  <line x1="512" y1="350" x2="625" y2="300" stroke="#16a34a" stroke-width="1.5"/>
</svg>
`;

// 4. Activity Diagram SVG
const activitySvg = `
<svg width="780" height="490" viewBox="0 0 780 490" xmlns="http://www.w3.org/2000/svg" style="font-family:'Segoe UI',Arial,sans-serif;">
  <defs>
    <marker id="actArrow" viewBox="0 0 10 10" refX="5" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
      <path d="M 0 0 L 10 5 L 0 10 z" fill="#1e293b" />
    </marker>
  </defs>

  <!-- Start Node -->
  <circle cx="390" cy="20" r="12" fill="#002D62"/>
  <text x="390" y="42" fill="#002D62" font-size="9" font-weight="bold" text-anchor="middle">Start: Request Timetable</text>

  <line x1="390" y1="32" x2="390" y2="55" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- Step 1: Fetch Data -->
  <rect x="250" y="55" width="280" height="30" rx="5" fill="#f1f5f9" stroke="#475569" stroke-width="1.2"/>
  <text x="390" y="74" fill="#0f172a" font-size="9.5" text-anchor="middle">Fetch Enrolled Courses, Topics &amp; Datesheet</text>

  <line x1="390" y1="85" x2="390" y2="105" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- Decision: Assessments? -->
  <polygon points="390,105 450,125 390,145 330,125" fill="#fef3c7" stroke="#d97706" stroke-width="1.2"/>
  <text x="390" y="129" fill="#92400e" font-size="9" font-weight="bold" text-anchor="middle">Upcoming Exam?</text>

  <!-- Yes -> Urgency Calc -->
  <line x1="450" y1="125" x2="520" y2="125" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>
  <text x="475" y="118" fill="#16a34a" font-size="8.5" font-weight="bold">Yes</text>
  <rect x="520" y="110" width="230" height="30" rx="4" fill="#fee2e2" stroke="#dc2626" stroke-width="1"/>
  <text x="635" y="129" fill="#991b1b" font-size="9" text-anchor="middle">Urgency = 100 &times; (1 - DaysLeft/30)</text>

  <!-- No -> Zero Urgency -->
  <line x1="330" y1="125" x2="260" y2="125" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>
  <text x="290" y="118" fill="#dc2626" font-size="8.5" font-weight="bold">No</text>
  <rect x="50" y="110" width="210" height="30" rx="4" fill="#f1f5f9" stroke="#94a3b8" stroke-width="1"/>
  <text x="155" y="129" fill="#475569" font-size="9" text-anchor="middle">Set Urgency = 0.0</text>

  <!-- Merge to 4-Factor Calculation -->
  <line x1="635" y1="140" x2="635" y2="175" stroke="#1e293b" stroke-width="1.5"/>
  <line x1="635" y1="175" x2="390" y2="175" stroke="#1e293b" stroke-width="1.5"/>
  <line x1="155" y1="140" x2="155" y2="175" stroke="#1e293b" stroke-width="1.5"/>
  <line x1="155" y1="175" x2="390" y2="175" stroke="#1e293b" stroke-width="1.5"/>
  <line x1="390" y1="175" x2="390" y2="195" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- Step 2: Compute Remaining Factors -->
  <rect x="180" y="195" width="420" height="42" rx="5" fill="#fdf2f2" stroke="#800000" stroke-width="1.5"/>
  <text x="390" y="212" fill="#800000" font-size="10" font-weight="bold" text-anchor="middle">Evaluate 4 Normalized Factors for Each Pending Topic</text>
  <text x="390" y="227" fill="#475569" font-size="8.5" text-anchor="middle">Exam Urgency (35%) + Incompletion (30%) + Difficulty (20%) + Marks Deficit (15%)</text>

  <line x1="390" y1="237" x2="390" y2="255" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- Step 3: Sort Ranking -->
  <rect x="230" y="255" width="320" height="28" rx="4" fill="#f8fafc" stroke="#334155" stroke-width="1.2"/>
  <text x="390" y="273" fill="#0f172a" font-size="9.5" text-anchor="middle">Sort Topics Descending by Calculated Priority Score</text>

  <line x1="390" y1="283" x2="390" y2="305" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- Decision: Capacity Check -->
  <polygon points="390,305 465,325 390,345 315,325" fill="#fef3c7" stroke="#d97706" stroke-width="1.2"/>
  <text x="390" y="329" fill="#92400e" font-size="9" font-weight="bold" text-anchor="middle">Allocated &lt; Daily Hours?</text>

  <!-- Yes -> Schedule Block -->
  <line x1="315" y1="325" x2="220" y2="325" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>
  <text x="260" y="318" fill="#16a34a" font-size="8.5" font-weight="bold">Yes</text>
  <rect x="40" y="310" width="180" height="42" rx="4" fill="#f0fdf4" stroke="#16a34a" stroke-width="1.2"/>
  <text x="130" y="327" fill="#15803d" font-size="9" font-weight="bold" text-anchor="middle">Schedule Topic Block (45-60m)</text>
  <text x="130" y="341" fill="#475569" font-size="8" text-anchor="middle">+ 15m Buffer / Break Interval</text>

  <!-- Loop back to Capacity Check -->
  <line x1="130" y1="352" x2="130" y2="375" stroke="#1e293b" stroke-width="1.5"/>
  <line x1="130" y1="375" x2="390" y2="375" stroke="#1e293b" stroke-width="1.5"/>
  <line x1="390" y1="375" x2="390" y2="345" stroke="#1e293b" stroke-width="1.5"/>

  <!-- No -> Persist & Render -->
  <line x1="465" y1="325" x2="530" y2="325" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>
  <text x="490" y="318" fill="#dc2626" font-size="8.5" font-weight="bold">No</text>
  <rect x="530" y="310" width="220" height="30" rx="4" fill="#f1f5f9" stroke="#475569" stroke-width="1.2"/>
  <text x="640" y="329" fill="#0f172a" font-size="9" text-anchor="middle">Persist Study Tasks via Spring Data JPA</text>

  <line x1="640" y1="340" x2="640" y2="390" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- Render UI -->
  <rect x="520" y="390" width="240" height="30" rx="4" fill="#eff6ff" stroke="#2563eb" stroke-width="1.2"/>
  <text x="640" y="409" fill="#1d4ed8" font-size="9" font-weight="bold" text-anchor="middle">Render Conflict-Free Daily Timetable</text>

  <line x1="640" y1="420" x2="640" y2="445" stroke="#1e293b" stroke-width="1.5" marker-end="url(#actArrow)"/>

  <!-- End Node -->
  <circle cx="640" cy="455" r="10" fill="none" stroke="#800000" stroke-width="2"/>
  <circle cx="640" cy="455" r="6" fill="#800000"/>
  <text x="640" y="475" fill="#800000" font-size="9" font-weight="bold" text-anchor="middle">End Workflow</text>
</svg>
`;

// 5. ER Diagram SVG
const erDiagramSvg = `
<svg width="780" height="490" viewBox="0 0 780 490" xmlns="http://www.w3.org/2000/svg" style="font-family:'Segoe UI',Arial,sans-serif;">
  <!-- STUDENTS TABLE -->
  <rect x="20" y="20" width="165" height="150" rx="4" fill="#ffffff" stroke="#002D62" stroke-width="1.5"/>
  <rect x="20" y="20" width="165" height="22" rx="4" fill="#002D62"/>
  <text x="102" y="35" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">STUDENTS</text>
  <text x="26" y="55" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="26" y="70" fill="#334155" font-size="8.5">full_name : VARCHAR</text>
  <text x="26" y="85" fill="#334155" font-size="8.5">email : VARCHAR [UK]</text>
  <text x="26" y="100" fill="#334155" font-size="8.5">password : VARCHAR</text>
  <text x="26" y="115" fill="#334155" font-size="8.5">branch : VARCHAR</text>
  <text x="26" y="130" fill="#334155" font-size="8.5">semester : INT (5)</text>
  <text x="26" y="145" fill="#334155" font-size="8.5">daily_study_hours : DOUBLE</text>
  <text x="26" y="160" fill="#334155" font-size="8.5">created_at : TIMESTAMP</text>

  <!-- SUBJECTS TABLE -->
  <rect x="235" y="20" width="160" height="150" rx="4" fill="#ffffff" stroke="#800000" stroke-width="1.5"/>
  <rect x="235" y="20" width="160" height="22" rx="4" fill="#800000"/>
  <text x="315" y="35" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">SUBJECTS</text>
  <text x="241" y="55" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="241" y="70" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">student_id</tspan> : BIGINT [FK]</text>
  <text x="241" y="85" fill="#334155" font-size="8.5">code : VARCHAR (UCS553)</text>
  <text x="241" y="100" fill="#334155" font-size="8.5">name : VARCHAR</text>
  <text x="241" y="115" fill="#334155" font-size="8.5">credits : INT (4.0)</text>
  <text x="241" y="130" fill="#334155" font-size="8.5">target_grade : VARCHAR</text>
  <text x="241" y="145" fill="#334155" font-size="8.5">teacher_name : VARCHAR</text>
  <text x="241" y="160" fill="#334155" font-size="8.5">color_hex : VARCHAR</text>

  <!-- UNITS TABLE -->
  <rect x="445" y="20" width="145" height="110" rx="4" fill="#ffffff" stroke="#475569" stroke-width="1.5"/>
  <rect x="445" y="20" width="145" height="22" rx="4" fill="#475569"/>
  <text x="517" y="35" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">UNITS</text>
  <text x="451" y="55" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="451" y="70" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">subject_id</tspan> : BIGINT [FK]</text>
  <text x="451" y="85" fill="#334155" font-size="8.5">unit_number : INT</text>
  <text x="451" y="100" fill="#334155" font-size="8.5">title : VARCHAR</text>
  <text x="451" y="115" fill="#334155" font-size="8.5">description : TEXT</text>

  <!-- TOPICS TABLE -->
  <rect x="625" y="20" width="140" height="150" rx="4" fill="#ffffff" stroke="#475569" stroke-width="1.5"/>
  <rect x="625" y="20" width="140" height="22" rx="4" fill="#475569"/>
  <text x="695" y="35" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">TOPICS</text>
  <text x="631" y="55" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="631" y="70" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">unit_id</tspan> : BIGINT [FK]</text>
  <text x="631" y="85" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">subject_id</tspan> : BIGINT [FK]</text>
  <text x="631" y="100" fill="#334155" font-size="8.5">name : VARCHAR</text>
  <text x="631" y="115" fill="#334155" font-size="8.5">difficulty : VARCHAR</text>
  <text x="631" y="130" fill="#334155" font-size="8.5">estimated_min : INT</text>
  <text x="631" y="145" fill="#334155" font-size="8.5">status : VARCHAR</text>
  <text x="631" y="160" fill="#334155" font-size="8.5">confidence : INT</text>

  <!-- ER Links Top Row -->
  <line x1="185" y1="75" x2="235" y2="75" stroke="#002D62" stroke-width="1.5"/>
  <text x="190" y="70" fill="#002D62" font-size="8" font-weight="bold">1</text>
  <text x="222" y="70" fill="#002D62" font-size="8" font-weight="bold">&infin;</text>

  <line x1="395" y1="75" x2="445" y2="75" stroke="#475569" stroke-width="1.5"/>
  <text x="400" y="70" fill="#475569" font-size="8" font-weight="bold">1</text>
  <text x="432" y="70" fill="#475569" font-size="8" font-weight="bold">&infin;</text>

  <line x1="590" y1="75" x2="625" y2="75" stroke="#475569" stroke-width="1.5"/>
  <text x="595" y="70" fill="#475569" font-size="8" font-weight="bold">1</text>
  <text x="612" y="70" fill="#475569" font-size="8" font-weight="bold">&infin;</text>

  <!-- BOTTOM ROW -->
  <!-- ASSESSMENTS TABLE -->
  <rect x="20" y="210" width="165" height="135" rx="4" fill="#ffffff" stroke="#b91c1c" stroke-width="1.5"/>
  <rect x="20" y="210" width="165" height="22" rx="4" fill="#b91c1c"/>
  <text x="102" y="225" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">ASSESSMENTS</text>
  <text x="26" y="245" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="26" y="260" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">subject_id</tspan> : BIGINT [FK]</text>
  <text x="26" y="275" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">student_id</tspan> : BIGINT [FK]</text>
  <text x="26" y="290" fill="#334155" font-size="8.5">title : VARCHAR (MST)</text>
  <text x="26" y="305" fill="#334155" font-size="8.5">type : VARCHAR</text>
  <text x="26" y="320" fill="#334155" font-size="8.5">max_marks : DOUBLE</text>
  <text x="26" y="335" fill="#334155" font-size="8.5">date_time : TIMESTAMP</text>

  <!-- MARKS TABLE -->
  <rect x="235" y="210" width="160" height="135" rx="4" fill="#ffffff" stroke="#b91c1c" stroke-width="1.5"/>
  <rect x="235" y="210" width="160" height="22" rx="4" fill="#b91c1c"/>
  <text x="315" y="225" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">MARKS</text>
  <text x="241" y="245" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="241" y="260" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">subject_id</tspan> : BIGINT [FK]</text>
  <text x="241" y="275" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">student_id</tspan> : BIGINT [FK]</text>
  <text x="241" y="290" fill="#334155" font-size="8.5">marks_obtained : DOUBLE</text>
  <text x="241" y="305" fill="#334155" font-size="8.5">max_marks : DOUBLE</text>
  <text x="241" y="320" fill="#334155" font-size="8.5">assessment_date : DATE</text>
  <text x="241" y="335" fill="#334155" font-size="8.5">assessment_type : VARCHAR</text>

  <!-- STUDY_TASKS TABLE -->
  <rect x="440" y="210" width="160" height="150" rx="4" fill="#ffffff" stroke="#16a34a" stroke-width="1.5"/>
  <rect x="440" y="210" width="160" height="22" rx="4" fill="#16a34a"/>
  <text x="520" y="225" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">STUDY_TASKS</text>
  <text x="446" y="245" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="446" y="260" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">student_id</tspan> : BIGINT [FK]</text>
  <text x="446" y="275" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">topic_id</tspan> : BIGINT [FK]</text>
  <text x="446" y="290" fill="#334155" font-size="8.5">scheduled_date : DATE</text>
  <text x="446" y="305" fill="#334155" font-size="8.5">duration_min : INT</text>
  <text x="446" y="320" fill="#334155" font-size="8.5">priority_score : DOUBLE</text>
  <text x="446" y="335" fill="#334155" font-size="8.5">is_completed : BOOLEAN</text>

  <!-- STUDY_SESSIONS TABLE -->
  <rect x="625" y="210" width="140" height="135" rx="4" fill="#ffffff" stroke="#16a34a" stroke-width="1.5"/>
  <rect x="625" y="210" width="140" height="22" rx="4" fill="#16a34a"/>
  <text x="695" y="225" fill="#ffffff" font-size="10" font-weight="bold" text-anchor="middle">STUDY_SESSIONS</text>
  <text x="631" y="245" fill="#0f172a" font-size="8.5"><tspan font-weight="bold">id</tspan> : BIGINT [PK]</text>
  <text x="631" y="260" fill="#dc2626" font-size="8.5"><tspan font-weight="bold">student_id</tspan> : BIGINT [FK]</text>
  <text x="631" y="275" fill="#334155" font-size="8.5">start_time : TIMESTAMP</text>
  <text x="631" y="290" fill="#334155" font-size="8.5">end_time : TIMESTAMP</text>
  <text x="631" y="305" fill="#334155" font-size="8.5">duration_min : INT</text>
  <text x="631" y="320" fill="#334155" font-size="8.5">session_type : VARCHAR</text>
  <text x="631" y="335" fill="#334155" font-size="8.5">focus_rating : INT</text>

  <!-- Vertical Links -->
  <line x1="102" y1="170" x2="102" y2="210" stroke="#b91c1c" stroke-width="1.5"/>
  <text x="106" y="185" fill="#b91c1c" font-size="8" font-weight="bold">1</text>
  <text x="106" y="202" fill="#b91c1c" font-size="8" font-weight="bold">&infin;</text>

  <line x1="315" y1="170" x2="315" y2="210" stroke="#b91c1c" stroke-width="1.5"/>
  <text x="319" y="185" fill="#b91c1c" font-size="8" font-weight="bold">1</text>
  <text x="319" y="202" fill="#b91c1c" font-size="8" font-weight="bold">&infin;</text>

  <line x1="695" y1="170" x2="520" y2="210" stroke="#16a34a" stroke-width="1.5"/>
  <text x="650" y="185" fill="#16a34a" font-size="8" font-weight="bold">1</text>
  <text x="535" y="202" fill="#16a34a" font-size="8" font-weight="bold">&infin;</text>
</svg>
`;

module.exports = {
  architectureSvg,
  useCaseSvg,
  classDiagramSvg,
  activitySvg,
  erDiagramSvg
};
