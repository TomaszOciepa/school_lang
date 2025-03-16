FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/student-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8091
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "student-service-0.0.1-SNAPSHOT.jar"]


