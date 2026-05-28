FROM eclipse-temurin:21-jdk-jammy AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

ENV SERVER_PORT=8080

RUN groupadd -r appuser && useradd -r -g appuser appuser

USER appuser

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]