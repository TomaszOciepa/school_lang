FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/eureka-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8761
CMD java -jar eureka-service-0.0.1-SNAPSHOT.jar

