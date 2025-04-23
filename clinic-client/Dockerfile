# ====== Build Stage ======
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /client
COPY . .
RUN mvn clean package -DskipTests

# ====== Runtime Stage ======
FROM openjdk:17-jdk-slim
WORKDIR /client
COPY --from=build /client/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8081
