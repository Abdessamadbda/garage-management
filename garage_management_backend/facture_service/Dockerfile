# microservice-client/Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/facture_service-0.0.1-SNAPSHOT.jar facture-service.jar

EXPOSE 8085
CMD ["java", "-jar", "facture-service.jar"]
