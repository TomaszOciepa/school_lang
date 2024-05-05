FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine as builder
ADD target/gateway-service-0.0.1-SNAPSHOT.jar .
EXPOSE 9000
CMD java -jar gateway-service-0.0.1-SNAPSHOT.jar

