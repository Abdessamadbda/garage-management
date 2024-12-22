# microservice-client/Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/client_service-0.0.1-SNAPSHOT.jar client-service.jar

EXPOSE 8081
CMD ["java", "-jar", "client-service.jar"]
