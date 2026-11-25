# UCS553: Enterprise Web Application
## Project Viva-Voce & Final Evaluation Master Defense Guide
**Candidate:** Jasmine Kaur (`Roll Number: 102203045`, COE, 5th Sem `2627ODDSEM`)  
**Project:** *SYLLABIX: Intelligent Student Management & Adaptive Academic Planning Platform*  
**Course Code:** UCS553 (Enterprise Web Application)  
**Evaluator Target:** Lab Evaluation Before EST (15 Marks: 10 Marks Execution & Submission + 5 Marks Viva & Report)

---

## Part 1: How the Evaluation Marks Are Distributed

According to your official **Lab Evaluation Scheme**:
```
Total Lab Marks: 30 Marks
  ├── Pre-MST Lab Experiments & Viva: 15 Marks (Already evaluated)
  └── Pre-EST Project Evaluation: 15 Marks (THIS EVALUATION)
        ├── 10 Marks: Complete Execution & Submission of Project Work
        │     ├── Problem Definition
        │     ├── System Design
        │     ├── Working Functionality (Substantial weight!)
        │     ├── Database Integration
        │     ├── Testing
        │     └── Documentation (Source code, DB scripts, Report)
        └── 5 Marks: Viva-Voce & Project Report
```

> [!IMPORTANT]
> The evaluation guidelines explicitly state: **"Project marks will not be awarded merely for presentation; working functionality will carry substantial weight. Each student should clearly demonstrate their individual contribution."**
> 
> Syllabix is already running live on the cloud, backed by pure enterprise Java 21 / Spring Boot, 15 passing JUnit tests, an active H2/MySQL database, and a publication-quality report. You are in an exceptional position to secure a full **15/15**.

---

## Part 2: The 30-Second Elevator Pitch
*(Memorize this opening statement when the evaluator asks: "Tell me about your project.")*

> "Sir/Ma'am, standard Learning Management Systems like Moodle or Thapar LMS are **passive information silos**—they store syllabus PDFs and datesheet notices, but leave the translation of that raw data into an actionable daily study timetable entirely to the student. 
>
> To solve this, I designed and built **Syllabix**—an enterprise Java web application built on **Spring Boot 3.3.5 and Java 21**. Syllabix actively synchronizes the student's curriculum with Thapar's official 20-week academic calendar. It features an **intelligent LMS regex parser** that computes target grade run-rates from raw evaluation notices, and an **adaptive recommendation engine** that uses a normalized 4-factor heuristic (*35% Exam Urgency, 30% Syllabus Incompletion, 20% Topic Difficulty, 15% Marks Deficit*) to generate conflict-free daily study timetables tailored to the student's available hours. 
>
> The platform is containerized with Docker, live on Render Cloud with full database persistence, and backed by a 100% passing JUnit test suite."

---

## Part 3: The 3-Minute Live Demonstration Script
*(Follow this exact click-and-explain sequence when demonstrating the project.)*

### Step 1: Authentication & Institutional Masthead (`0:00 - 0:45`)
1. **Open:** `https://syllabix-platform-2.onrender.com/login`
2. **Show:** Point out the Thapar LMS visual identity, the crimson sidebar, and the notice alert for `2627ODDSEM`.
3. **Action:** Click **"Thapar Student"** to auto-fill `jkaur_be24@thapar.edu` and log in.
4. **Explain:** *"The authentication is managed through Spring Security with BCrypt cryptographic password hashing. Sessions are managed via secure cookies with multi-tenant data isolation."*

### Step 2: Dashboard, Roll Ribbon & Course Catalog (`0:45 - 1:30`)
1. **Show:** Point to the top institutional ribbon: *"Jasmine Kaur | Roll: 102203045 | COE Regular | 7 Core Courses | 25.0 Credits"*.
2. **Show:** The 7 Third-Year registered courses (`UCS510`, `UCS553`, `UCS421`, `UCS548`, `UCS615`, `UML501`, `UCS503`).
3. **Explain:** *"Each course tracks credits, faculty names, and dynamically computes syllabus completion percentages from micro-level topic states in the database."*

### Step 3: Flagship Innovation 1 — LMS Evaluation Parser & Run-Rate (`1:30 - 2:15`)
1. **Action:** In the LMS Evaluation Scheme card, paste the sample notice for `UCS553`:
   ```
   LAB Evaluation: 20 Marks (Week 18), Sessional Quiz: 10 Marks (Week 12), MST: 30 Marks, EST: 40 Marks
   ```
2. **Click:** **"Parse & Calculate Run-Rate"**.
3. **Show:** The system parses all 4 assessment tokens, identifies scored marks (23.5/25), remaining marks (75.0), and calculates the required run-rate (75.3%) for Target Grade A with status **"STRETCH"**.
4. **Explain:** *"This solves a real pain point where students don't know what percentage they need on their final EST and lab exams to retain an A grade."*

### Step 4: Flagship Innovation 2 — 20-Week Academic Calendar & AI Planner (`2:15 - 3:00`)
1. **Navigate:** Go to `/calendar`.
2. **Show:** The 20-week semester strip. Show how **Week 17 (Teaching • Nov 23-28)** is automatically highlighted as the active week. Show the countdown to Lab Evaluation Week (Week 18) and EST Exams (Weeks 19-20).
3. **Navigate:** Go to `/planner`. Set daily hours to `3.5 hours` and click **"Generate Adaptive Study Plan"**.
4. **Show:** The generated non-overlapping schedule blocks sorted by calculated priority score.
5. **Conclude:** *"Everything is persisted in our relational schema using Spring Data JPA and Hibernate over HikariCP."*

---

## Part 4: Comprehensive Viva Questions & Model Answers

### Category 1: Problem Definition & Architecture

#### Q1: What is the main problem your application solves?
**Answer:**  
"Standard LMS systems (Moodle, Blackboard) and college ERPs are passive document repositories. They don't help students answer: *(1) What should I study today? (2) How much should I study? (3) Which topics are critical to hit my target grade?*  
Syllabix transforms static syllabi into an active scheduling engine by connecting curriculum tracking, LMS evaluation grading schemes, university academic calendars, and daily time constraints into an automated adaptive study timetable."

#### Q2: Explain the high-level architecture of Syllabix.
**Answer:**  
"Syllabix follows a **3-tier Layered MVC (Model-View-Controller) architecture**:
1. **Presentation Layer:** Responsive HTML5, Bootstrap 5, Custom CSS conforming to Thapar visual standards, Chart.js for radar analytics, and Web Audio API for timer synthesis.
2. **Application / Controller Layer:** Spring MVC REST controllers (`SubjectApiController`, `PlannerApiController`, `CalendarApiController`, `InsightsApiController`) guarded by a Spring Security Filter Chain with BCrypt hashing and session authentication.
3. **Business Logic Layer:** Algorithmic services including `RecommendationEngineService`, `CourseEvaluationParserService`, and `AcademicCalendarService`.
4. **Data Persistence Layer:** Spring Data JPA with Hibernate 6 ORM over HikariCP connection pooling, interfacing with a relational database running in MySQL compatibility mode."

#### Q3: Why did you choose Spring Boot over vanilla Servlets/JSP or Node.js/Django?
**Answer:**  
"Because this is an Enterprise Web Application (UCS553), Spring Boot is the industry gold standard:
- It eliminates boilerplate through **Auto-Configuration** and opinionated starters (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`).
- It provides **Inversion of Control (IoC)** and **Dependency Injection (DI)**, making business services modular and unit-testable with Mockito.
- It includes production-grade features like HikariCP connection pooling, declarative `@Transactional` management, and container readiness via Docker."

---

### Category 2: Core Java & Object-Oriented Design

#### Q4: How did you use the Java Collections Framework in your project?
**Answer:**  
"The Collections Framework is central to our algorithmic engine:
- `List<T>` / `ArrayList<T>`: Used for holding ordered subjects, units, topics, and evaluation tokens.
- `Map<String, Double>`: Used for student weekly availability mappings (`{"Monday": 3.5, "Tuesday": 3.5}`) and grade cutoff lookups (`{"A+": 88.0, "A": 80.0, "B": 65.0}`).
- **Java 8+ Streams API (`filter`, `map`, `sorted`, `collect`):** Used in `StudyPlanService` and `RecommendationEngineService` to filter pending topics, calculate priority scores, sort topics descending by priority score, and collect them into scheduled session blocks."

#### Q5: How is Exception Handling implemented in your backend?
**Answer:**  
"We implement centralized, global exception handling using `@RestControllerAdvice` in `GlobalExceptionHandler.java`:
- Custom business exceptions like `ResourceNotFoundException` return `HTTP 404 NOT FOUND` with a standardized JSON error payload containing timestamps and entity messages.
- Spring validation errors (`MethodArgumentNotValidException`, `ConstraintViolationException`) return `HTTP 400 BAD REQUEST` with a structured field-to-message error map.
- Authentication failures return `HTTP 401 UNAUTHORIZED`.  
This ensures the client never receives raw stack traces or unhandled 500 errors."

#### Q6: Explain Inversion of Control (IoC) and Dependency Injection (DI) with an example from your code.
**Answer:**  
"IoC means the framework controls the lifecycle and binding of objects rather than the developer using `new ClassName()`.  
In Syllabix, `RecommendationEngineService` depends on `SubjectRepository` and `AssessmentRepository`. Instead of instantiating them manually, we use **Constructor-based Dependency Injection**:
```java
@Service
public class RecommendationEngineService {
    private final SubjectRepository subjectRepo;
    private final AssessmentRepository assessmentRepo;

    public RecommendationEngineService(SubjectRepository subjectRepo, 
                                       AssessmentRepository assessmentRepo) {
        this.subjectRepo = subjectRepo;
        this.assessmentRepo = assessmentRepo;
    }
}
```
Spring's IoC container automatically discovers the `@Repository` beans and injects them at application startup, promoting loose coupling and making unit testing trivial with Mockito."

---

### Category 3: Algorithmic Innovations (Unique Selling Points)

#### Q7: Exactly how does your Adaptive Recommendation Engine calculate topic priority?
**Answer:**  
"It uses a mathematically normalized **4-factor weighted heuristic equation**:
$$\text{Priority Score} = 0.35 \cdot U_{\text{exam}} + 0.30 \cdot I_{\text{syllabus}} + 0.20 \cdot D_{\text{topic}} + 0.15 \cdot M_{\text{deficit}}$$

Where each factor is normalized to a scale of $0.0$ to $100.0$:
1. **Exam Urgency ($U_{\text{exam}}$ - 35\% weight):** Computed from days remaining until the nearest assessment:
   $$U = 100 \times \left(1 - \frac{\min(\text{daysLeft}, 30)}{30}\right)$$
   An exam in 2 days gives $\approx 93.3$; an exam 30+ days away gives $0.0$.
2. **Syllabus Incompletion ($I_{\text{syllabus}}$ - 30\% weight):** $100.0 - \text{Subject Completion \%}$. If only 40\% of a subject is complete, this contributes $60.0$.
3. **Topic Difficulty ($D_{\text{topic}}$ - 20\% weight):** Categorical mapping: $\text{EASY} = 25.0$, $\text{MEDIUM} = 60.0$, $\text{HARD} = 100.0$.
4. **Academic Marks Deficit ($M_{\text{deficit}}$ - 15\% weight):** $100.0 - \text{Subject Average \%}$. If the student's current average in the course is 65\%, the deficit score is $35.0$.

The final score is clamped between $0.0$ and $100.0$. The engine then greedily allocates topics into study blocks of 45–60 minutes plus 15-minute rest buffers without exceeding the student's declared daily availability."

#### Q8: How does the LMS Course Evaluation Scheme Parser work?
**Answer:**  
"It operates in three stages:
1. **Regex Tokenization:** Uses compiled `java.util.regex.Pattern` expressions to match common Thapar LMS announcement patterns (e.g., `(LAB Evaluation|Sessional Quiz|MST|EST|Quiz|Assignment)[:\s\-]+(\d+)\s*(Marks|marks|%)`).
2. **Cutoff Comparison:** Cross-references the student's target grade ($A^+ \ge 88\%$, $A \ge 80\%$, $A^- \ge 73\%$, $B \ge 65\%$) against total marks to determine total target marks needed.
3. **Run-Rate Calculation:**
   $$\text{Run Rate \%} = \left(\frac{\text{Target Marks Needed} - \text{Current Scored Marks}}{\text{Remaining Unassessed Marks}}\right) \times 100$$
   - If $\le 65\%$: Marked **ACHIEVABLE** (green).
   - If $65\% - 85\%$: Marked **STRETCH** (yellow).
   - If $> 85\%$ or $> 100\%$: Marked **UNREALISTIC** (red), warning the student to adjust expectations or focus heavily on high-weight components."

#### Q9: How is the Thapar 20-Week Academic Calendar integrated?
**Answer:**  
"Rather than hardcoding static calendar dates, `AcademicCalendarService` implements a dynamic 20-week date model for TIET's ODD SEM 2026-27 (21 July 2026 to 19 December 2026).  
Using Java's `LocalDate.now()`, the system calculates:
1. The active semester week (e.g., Week 17 • Teaching).
2. Proximity to major academic milestones (Diwali break, Lab Evaluation Week 18, EST Exam Weeks 19–20).
3. Automatic reminders for compensatory Saturday teaching schedules and gazetted university holidays."

---

### Category 4: Database & Spring Data JPA

#### Q10: What is your database design? Explain the entities and relationships.
**Answer:**  
"The database schema consists of **8 normalized relational tables**:
- `STUDENTS` (1 to Many) $\rightarrow$ `SUBJECTS`
- `SUBJECTS` (1 to Many) $\rightarrow$ `UNITS` (1 to Many) $\rightarrow$ `TOPICS`
- `SUBJECTS` (1 to Many) $\rightarrow$ `ASSESSMENTS`
- `SUBJECTS` (1 to Many) $\rightarrow$ `MARKS`
- `STUDENTS` (1 to Many) $\rightarrow$ `STUDY_TASKS` (associated with `TOPICS`)
- `STUDENTS` (1 to Many) $\rightarrow$ `STUDY_SESSIONS` (tracks focus duration and streaks)

Foreign key constraints, cascade rules, and indexing on `(student_id, code)` enforce relational integrity."

#### Q11: Why H2 in MySQL mode instead of standalone MySQL/PostgreSQL?
**Answer:**  
"We configured H2 in persistent file-backed mode with MySQL dialect:
```properties
spring.datasource.url=jdbc:h2:file:./data/syllabix_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```
This design gives us two distinct enterprise advantages:
1. **Zero-Configuration Portability:** The database file is embedded directly within the container on Render Cloud, eliminating external network latency and database hosting costs while surviving container restarts.
2. **Production-Ready Compatibility:** Because it runs in `MODE=MySQL`, switching to an external enterprise MySQL 8.0 cluster requires only changing the connection URL and driver in `application.properties` with zero modifications to entity code or SQL queries."

#### Q12: How do you handle transactions and concurrency?
**Answer:**  
"All state-modifying service methods (such as generating study plans, enrolling courses, and recording marks) are annotated with `@Transactional`.  
This ensures **ACID properties**: if a failure occurs midway through allocating multiple study tasks, the transaction rolls back completely to prevent orphan records. HikariCP manages connection acquisition with a max pool size of 10 and a 30-second timeout."

---

### Category 5: Security & Session Management

#### Q13: How is authentication and password security handled?
**Answer:**  
"We use Spring Security 6 with `BCryptPasswordEncoder`:
- Passwords are never stored in plain text. BCrypt applies a salted, adaptive one-way hashing algorithm with a work factor of 10.
- Login requests to `/api/auth/login` authenticate credentials; upon success, an HTTP session is established with a secure cookie.
- Multi-tenancy is enforced at the repository query level: every database lookup filters strictly by the authenticated student's session ID (`findByStudentId(...)`), ensuring students cannot access or tamper with other students' academic records."

#### Q14: How do you prevent CSRF and SQL Injection attacks?
**Answer:**  
- **SQL Injection Prevention:** Spring Data JPA uses Hibernate with parameterized prepared statements. Dynamic user inputs are treated as literal values rather than executable SQL fragments.
- **CSRF Protection:** State-modifying REST endpoints require authenticated session tokens, and input validation is enforced using JSR-380 bean validation annotations (`@NotNull`, `@Size`, `@Min`, `@Email`)."

---

### Category 6: Testing & Quality Assurance

#### Q15: How did you test your application?
**Answer:**  
"We implemented a comprehensive unit and regression testing suite using **JUnit 5 and Mockito**:
- `RecommendationEngineServiceTest`: Verifies the 4-factor heuristic calculations, edge cases (e.g., zero days left, no assessments), and weight summation.
- `CourseEvaluationParserServiceTest`: Verifies regex extraction of diverse LMS notice formats, run-rate math, and boundary conditions (achievable vs. unrealistic).
- `AcademicCalendarServiceTest`: Validates date-to-week mapping across all 20 weeks of the semester.
- `MarksServiceTest` and `StudyPlanServiceTest`: Verify transactional integrity and capacity limits.  
All 15 unit tests pass with 0 failures during the Maven build (`mvn clean test`)."

---

### Category 7: Cloud Deployment & DevOps

#### Q16: How is Syllabix deployed to the cloud?
**Answer:**  
"Syllabix is deployed using a multi-stage **Dockerfile** on **Render Cloud**:
1. **Build Stage:** Uses `maven:3.9.8-eclipse-temurin-21-jammy` to compile source code and package the Spring Boot executable JAR with memory tuning (`MAVEN_OPTS="-Xmx384m"`).
2. **Runtime Stage:** Uses a lightweight `eclipse-temurin:21-jre-jammy` image to minimize the container footprint.
3. **Execution:** The application runs on port `8080` with persistent volume storage for the database file at `/data/syllabix_db.mv.db`.  
The live service is reachable globally at `https://syllabix-platform-2.onrender.com`."

#### Q17: What was a significant technical challenge you solved during deployment?
**Answer:**  
"On Render's free tier, build containers have a strict 512MB RAM ceiling. The default OpenJDK 21 Maven build consumed more than 512MB of heap memory during compilation, causing the Linux kernel OOM (Out Of Memory) Killer to terminate the build with exit status 1.  
I resolved this by optimizing the Maven memory footprint in `Dockerfile`:
```dockerfile
ENV MAVEN_OPTS="-Xmx384m -XX:+TieredCompilation -XX:TieredStopAtLevel=1"
RUN mvn clean package -DskipTests --no-transfer-progress -B
```
This capped memory allocation at 384MB and disabled excessive JVM compilation tiers during build time, allowing the image to build successfully in 2 minutes 47 seconds and run smoothly."

---

### Category 8: "Trap" Questions Examiners Love to Ask

#### Q18: "Isn't this just a basic To-Do list or calendar app?"
**Answer (Confident & Technical):**  
"No, sir/ma'am. A to-do list is purely static and manual—the user decides what to put in and when to do it.  
Syllabix is an **active heuristic scheduler**:
1. It reads university-specific evaluation schemes and calculates mathematically required run-rates.
2. It ranks curriculum topics using a multi-variable normalized optimization function combining upcoming datesheet deadlines, syllabus completion deficits, topic complexity, and historical marks.
3. It respects daily student time budgets and prevents cognitive overload by automatically inserting rest intervals."

#### Q19: "What happens if a student has an impossible run-rate, like needing 110% on remaining exams?"
**Answer:**  
"Our `CourseEvaluationParserService` specifically handles this boundary condition. When `runRate > 100.0%`, the system automatically flags the feasibility status as **UNREALISTIC** (red badge). Instead of giving false hope, the dashboard advises the student to recalibrate their target grade to an achievable tier (e.g., Target Grade B or C), computes the new run-rate for that tier, and identifies high-credit subjects where improvement will have the highest impact on their CGPA."

#### Q20: "What was your individual contribution to the project?"
**Answer:**  
"I designed and implemented the entire end-to-end platform:
1. **Backend:** Configured Spring Boot 3.3.5, Spring Security BCrypt authentication, Spring Data JPA domain models, and wrote the core algorithms in `RecommendationEngineService` and `CourseEvaluationParserService`.
2. **Data & Curriculum:** Seeded and configured all 7 official Third-Year courses for `2627ODDSEM` and integrated the 20-week TIET academic calendar.
3. **Frontend:** Developed the institutional UI using HTML5, Bootstrap 5, Chart.js, and Web Audio synthesizers matching Thapar University branding.
4. **DevOps & Verification:** Containerized the application with Docker, resolved memory optimization on Render Cloud, authored the comprehensive 15-test JUnit suite, and authored the complete documentation and LaTeX/PDF reports."

---

## Part 5: Rapid Cheat Sheet for Defense Day

| Concept | Key Keywords to Mention |
| :--- | :--- |
| **Backend Framework** | Spring Boot 3.3.5, Java 21 LTS, Inversion of Control, Dependency Injection |
| **Security** | Spring Security 6, `BCryptPasswordEncoder` (work factor 10), Session Cookies, CSRF, Prepared Statements |
| **Persistence** | Spring Data JPA, Hibernate 6 ORM, HikariCP Connection Pool (max 10), `@Transactional` ACID compliance |
| **Database** | Persistent file-based H2 in MySQL compatibility mode (`MODE=MySQL`), easily swapped to MySQL 8.0 |
| **Algorithm** | 4-Factor Normalized Heuristic: $0.35 \cdot \text{Urgency} + 0.30 \cdot \text{Incompletion} + 0.20 \cdot \text{Difficulty} + 0.15 \cdot \text{Deficit}$ |
| **LMS Parser** | Regex tokenization, Target grade cutoffs ($A^+ \ge 88, A \ge 80, B \ge 65$), Run-rate formula, Feasibility verdicts |
| **Testing** | JUnit 5, Mockito, 15 unit tests, 100% pass rate (`mvn clean test`) |
| **Deployment** | Docker multi-stage build, Render Cloud, `MAVEN_OPTS="-Xmx384m"` |
| **Live Link** | `https://syllabix-platform-2.onrender.com` |
| **GitHub Repo** | `https://github.com/jasminekaur7/syllabix-platform` |
