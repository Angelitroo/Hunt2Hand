FROM amazoncorretto:23

WORKDIR /app

RUN yum install -y tar gzip

COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml .

RUN chmod +x mvnw

RUN ls -l /app

# Resuelve las dependencias
RUN ./mvnw clean dependency:resolve -X

COPY src/ src/

# Compila y empaqueta el proyecto
RUN ./mvnw clean package -DskipTests

# Verifica que el archivo JAR se ha creado
RUN ls -l target/

EXPOSE 8080

# Ejecuta el JAR generado
CMD ["java", "-jar", "target/Hunt2Hand-0.0.1-SNAPSHOT.jar"]
