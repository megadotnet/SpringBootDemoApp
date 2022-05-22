FROM openjdk:8-jdk-alpine
MAINTAINER megadotnet
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

HEALTHCHECK --start-period=30s --interval=30s --timeout=5s --retries=3  CMD curl -m 5 --silent --fail --request GET http://localhost:7080/actuator/health | jq --exit-status -n 'inputs | if has("status") then .status=="UP" else false end' > /dev/null || exit 1