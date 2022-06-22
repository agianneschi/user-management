# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/usermanagement-*.jar
ADD ${JAR_FILE} usermanagement.jar
ENTRYPOINT exec java -jar usermanagement.jar
