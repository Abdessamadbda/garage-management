# microservice-client/Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/planification_service-0.0.1-SNAPSHOT.jar planification_service.jar

EXPOSE 8082
CMD ["java", "-jar", "planification_service.jar"]
