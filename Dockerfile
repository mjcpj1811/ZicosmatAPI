FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} zicosmat.jar

ENTRYPOINT ["java", "-jar", "Zicosmat.jar"]

EXPOSE 8080