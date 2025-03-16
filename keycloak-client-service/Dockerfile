FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/keycloak-client-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8095
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "keycloak-client-service-0.0.1-SNAPSHOT.jar"]


