FROM eclipse-temurin:21-jdk
VOLUME /tmp
COPY target/users-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]