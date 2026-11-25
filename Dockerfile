# ==============================================================================
# Syllabix - Cloud-Optimized Multi-Stage Dockerfile for Render / Railway / Fly.io
# ==============================================================================

# Stage 1: Build JAR using Maven and OpenJDK 21
FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder
WORKDIR /build

# Set memory limits to comfortably fit within free-tier 512MB RAM builders
ENV MAVEN_OPTS="-Xmx384m -XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# Copy pom.xml and source tree
COPY pom.xml .
COPY src ./src

# Package production executable Spring Boot JAR
RUN mvn clean package -DskipTests --no-transfer-progress -B

# Stage 2: Minimal OpenJDK 21 JRE runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the generated Spring Boot fat JAR
COPY --from=builder /build/target/*.jar app.jar

# Setup environment
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=dev
ENV JAVA_OPTS="-Xmx384m -Xms128m"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
