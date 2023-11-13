FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app
COPY . .

RUN mvn clean package

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/DemowebsocketApplication.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]