FROM maven:3.9.7-eclipse-temurin-21 as builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar ./pharmacy-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "pharmacy-service.jar"]
