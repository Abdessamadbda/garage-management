# microservice-client/Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/vehicle-service-0.0.1-SNAPSHOT.jar vehicle-service.jar

EXPOSE 8085
CMD ["java", "-jar", "vehicle-service.jar"]
