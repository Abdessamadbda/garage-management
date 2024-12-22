# Use a base image with JDK
FROM openjdk:11-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the packaged JAR into the container
COPY target/demo-0.0.1-SNAPSHOT.jar eureka-server.jar

# Expose the port Eureka Server runs on
EXPOSE 8761

# Run the JAR file
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
