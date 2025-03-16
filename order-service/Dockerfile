FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/order-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8097
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "order-service-0.0.1-SNAPSHOT.jar"]


