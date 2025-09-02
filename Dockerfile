# 빌드
FROM gradle:8.10-jdk21 AS build

WORKDIR /cheftoo-back
COPY . .

RUN ./gradlew clean build -x test

# 실행
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

ARG SPRING_REDIS_HOST
ARG SPRING_REDIS_PORT
ENV SPRING_REDIS_HOST=$SPRING_REDIS_HOST
ENV SPRING_REDIS_PORT=$SPRING_REDIS_PORT

COPY --from=build /cheftoo-back/build/libs/*.jar cheftoo.jar
COPY src/main/resources/application.properties ./application.properties

ENTRYPOINT ["java", "-Dspring.config.location=file:./application.properties", "-jar", "cheftoo.jar"]

