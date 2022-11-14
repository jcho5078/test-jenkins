FROM adoptopenjdk/openjdk11
CMD ["./mvnw", "clean", "package"]
CMD ["pwd"]
CMD ["ls"]
ARG  JAR_FILE_PATH=./target/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]