# ===== 1. Build stage =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set workdir inside container
WORKDIR /app

# Copy pom.xml and resolve dependencies first (cache-friendly)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Build the Spring Boot JAR
RUN mvn -B clean package -DskipTests

# ===== 2. Runtime stage =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar from previous stage
# Change the jar name if your final artifact name is different
COPY --from=build /app/target/*.jar app.jar

# Expose the Spring Boot port
EXPOSE 8080

# Environment (you can override these on Render)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
