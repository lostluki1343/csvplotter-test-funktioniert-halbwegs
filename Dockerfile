#############################################
# 1) Builder-Stage: weiter mit Maven+Temurin
#############################################
FROM maven:3.9.8-eclipse-temurin-21 AS builder
WORKDIR /app

# Nur POM kopieren und Dependencies laden
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Quellcode kopieren und Paket bauen
COPY src ./src
RUN mvn clean package -DskipTests -B

#############################################
# 2) Runtime-Stage: Ubuntu + OpenJDK installieren
#############################################
FROM ubuntu:22.04
# Fehler bei nicht-interaktiven apt-Aufrufen vermeiden
ENV DEBIAN_FRONTEND=noninteractive

# System aktualisieren, OpenJDK installieren, temporäre Files löschen
RUN apt-get update \
 && apt-get install -y --no-install-recommends openjdk-21-jre-headless \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Fertiges JAR aus der Builder-Stage übernehmen
COPY --from=builder /app/target/AIIT_CSV_Server-0.0.1-SNAPSHOT.jar app.jar

# Plots-Verzeichnis anlegen und als Volume markieren
ENV PLOT_STORAGE_PATH=/data/plots
RUN mkdir -p ${PLOT_STORAGE_PATH}
VOLUME ${PLOT_STORAGE_PATH}

# App-Port
EXPOSE 8080

# Start-Kommando: JAR ausführen, Speicherort der Plots per Property übergeben
ENTRYPOINT ["java","-jar","/app/app.jar","--plot.storage.path=${PLOT_STORAGE_PATH}"]
