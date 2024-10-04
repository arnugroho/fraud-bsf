FROM openjdk:24-ea-17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} fraudbfs.jar
ENTRYPOINT ["java", "-jar","fraudbfs.jar"]



