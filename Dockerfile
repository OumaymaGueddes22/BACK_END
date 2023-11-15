 FROM maven:3-openjdk-17 AS build
 COPY . . 
 RUN mvn clean package -DskipTests 

# Use the official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file into the container
#COPY target/demowebsocket.jar app.jar
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demowebsocket.jar


# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run your application
#CMD ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "demowebsocket.jar"]