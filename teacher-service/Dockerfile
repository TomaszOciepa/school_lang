FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/teacher-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8092
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "teacher-service-0.0.1-SNAPSHOT.jar"]

