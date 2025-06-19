# Stage 1: Build with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Runtime image
FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=""

EXPOSE 80
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.datasource.url=${SPRING_DATASOURCE_URL}"]