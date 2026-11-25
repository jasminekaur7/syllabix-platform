# ==============================================================================
# Syllabix Intelligent Academic Management Platform
# Production Multi-Stage Dockerfile for Cloud Deployment (Render, Railway, Fly.io, Cloud Run)
# ==============================================================================

# Stage 1: Build the application with Maven & OpenJDK 21
FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder
WORKDIR /workspace/app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production JAR package without running tests
RUN mvn clean package -DskipTests

# Stage 2: Minimal runtime image with OpenJDK 21 JRE
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root system user for security
RUN groupadd -r syllabix && useradd -r -g syllabix -m -d /app syllabix

# Copy the built JAR artifact from builder stage
COPY --from=builder /workspace/app/target/syllabix-platform-1.0.0.jar /app/syllabix.jar

# Setup persistent directory for H2/data
RUN mkdir -p /app/data && chown -R syllabix:syllabix /app

USER syllabix

# Cloud platforms automatically pass the PORT environment variable
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT}/login || exit 1

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/syllabix.jar"]
