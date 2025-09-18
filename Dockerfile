FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/bank-application-1.0.0.jar app.jar

# Копируем ресурсы из builder stage в финальный образ
COPY --from=builder /app/src/main/resources/ /app/resources/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]