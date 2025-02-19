FROM openjdk:23-jdk-slim

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/Hunt2Hand-0.0.1-SNAPSHOT.jar"]