FROM eclipse-temurin:21
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests
EXPOSE 8081
CMD ["java", "-jar", "target/products-service-0.0.1-SNAPSHOT.jar"]