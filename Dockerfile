FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/task-manager.jar /app/task-manager.jar

EXPOSE 8081

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/task-manager.jar"]