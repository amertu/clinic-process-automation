# ====== Build Stage ======
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /server
COPY . .
RUN mvn clean package -DskipTests

# ====== Runtime Stage ======
FROM openjdk:17-jdk-slim
WORKDIR /server
COPY --from=build /server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080