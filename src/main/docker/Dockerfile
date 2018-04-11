FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD LoginApplication-0.0.2-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]