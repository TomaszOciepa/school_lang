FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/course-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8093
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "course-service-0.0.1-SNAPSHOT.jar"]
