FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/calendar-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8094
CMD java -jar calendar-service-0.0.1-SNAPSHOT.jar

