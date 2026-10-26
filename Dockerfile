
# Stage 1: Build the Zoloto WAR using Java 21 and Maven
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copy Maven configuration first
COPY pom.xml .

# Download dependencies
RUN mvn -B dependency:go-offline

# Copy application source code and web resources
COPY src ./src

# Build the deployable WAR file
RUN mvn -B clean package -DskipTests


# Stage 2: Run Zoloto on Tomcat 10.1 with Java 21
FROM tomcat:10.1-jdk21-temurin

# Remove Tomcat's default example applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy Zoloto as the root application
COPY --from=build /build/target/Zoloto-0.0.1-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

# Render supplies PORT; default to 8080 for local Docker testing
ENV PORT=8080

EXPOSE 8080

# Configure Tomcat to listen on Render's assigned port
CMD ["sh", "-c", "sed -i 's/port=\"8080\"/port=\"'\"${PORT:-8080}\"'\"/' /usr/local/tomcat/conf/server.xml && catalina.sh run"]
