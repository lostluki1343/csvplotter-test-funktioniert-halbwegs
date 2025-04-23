# 1. Build-Stage: Maven + Eclipse Temurin 21 auf Alpine
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Runtime-Stage: Eclipse Temurin 21 JRE auf Alpine
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Alle JARs aus build-Stage übernehmen und als app.jar ablegen
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
