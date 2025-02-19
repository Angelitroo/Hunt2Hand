FROM amazoncorretto:23

WORKDIR /app

RUN yum install -y tar

COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml .

RUN chmod +x mvnw

RUN ls -l /app

RUN ./mvnw clean dependency:resolve -X

COPY src/ src/

RUN ./mvnw clean package -DskipTests

RUN ls -l target/

EXPOSE 8080

CMD ["java", "-jar", "target/Hunt2Hand-0.0.1-SNAPSHOT.jar"]
