# 빌드
FROM gradle:8.10-jdk21 AS build

WORKDIR /cheftoo-back-prod
COPY . .
RUN ./gradlew clean build -x test

# 실행
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /cheftoo-back-prod/build/libs/*.jar cheftoo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "cheftoo.jar"]
