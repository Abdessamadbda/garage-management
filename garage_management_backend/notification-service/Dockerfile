# microservice-client/Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar

EXPOSE 8085
CMD ["java", "-jar", "notification-service.jar"]
