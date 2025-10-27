# backend/Dockerfile

# =========================
#  Build Stage
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration and source code
COPY pom.xml .
COPY src ./src

# Build the project (skip tests for faster builds)
RUN mvn -B -DskipTests clean package


# =========================
#  Run Stage
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/target/project-three-event-management-system-0.0.1-SNAPSHOT.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""

# Expose port
EXPOSE 8080

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
