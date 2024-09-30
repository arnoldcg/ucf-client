FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ucf.jar
ENTRYPOINT ["java","-jar","/ucf.jar"]