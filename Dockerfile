# ==========================================
# Stage 1: Build the application fat JAR
# ==========================================
# FIX: Updated to a valid Maven image that supports Java 25
FROM maven:3-eclipse-temurin-25 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration files first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Compile and package the application into a JAR, skipping tests for faster builds
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Minimal runtime environment
# ==========================================
FROM eclipse-temurin:25-jre-noble

# Set the application directory
WORKDIR /app

# Create a dedicated non-root user and group for enhanced security
RUN groupadd -r spring && useradd -r -g spring spring

# Change ownership of the working directory to the non-root user
RUN chown -R spring:spring /app

# Switch context to the non-root user
USER spring:spring

# FIX: Target the directory instead of renaming via wildcard, or use a specific jar name
COPY --from=builder /app/target/*.jar /app/app.jar

# Inform Docker that the container listens on port 8080 at runtime
EXPOSE 8080

# Execute the Spring Boot application safely
# FIX: Updated to point to the actual target directory structure
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
