FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/email-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8096
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "email-service-0.0.1-SNAPSHOT.jar"]


