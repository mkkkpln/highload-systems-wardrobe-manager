FROM maven:3.9.8-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

ENV DB_URL=jdbc:postgresql://db:5432/wardrobe \
    DB_USER=wardrobe \
    DB_PASSWORD=wardrobe

ENTRYPOINT ["java", "-jar", "app.jar"]
