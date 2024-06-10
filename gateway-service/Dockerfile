FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/gateway-service-0.0.1-SNAPSHOT.jar .
EXPOSE 9000
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "gateway-service-0.0.1-SNAPSHOT.jar"]
