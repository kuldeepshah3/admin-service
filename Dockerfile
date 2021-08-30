FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} admin-service.jar
ENTRYPOINT ["java", "-jar", "/admin-service.jar"]
EXPOSE 9092