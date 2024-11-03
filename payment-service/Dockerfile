FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/payment-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8098
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "payment-service-0.0.1-SNAPSHOT.jar"]


