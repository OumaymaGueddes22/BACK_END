

# Use the official OpenJDK base image
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
COPY . .
RUN mvn clean package -DskipTests
# Copy the compiled JAR file into the container
#COPY target/demowebsocket.jar app.jar
#COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demowebsocket.jar

FROM openjdk:17.0.1-jdk-slim

COPY --from=build /target/demowebsocket-0.0.1-SNAPSHOT.jar demowebsocket.jar


EXPOSE 8080

ENTRYPOINT  ["java", "-jar", "demowebsocket.jar"]

# Expose the port that your Spring Boot application will run on
#EXPOSE 8080

# Specify the command to run your application
#CMD ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-jar", "demowebsocket.jar"]
