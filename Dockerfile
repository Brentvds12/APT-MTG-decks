FROM openjdk:18-ea-11-jdk-alpine3.15
EXPOSE 8051
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]