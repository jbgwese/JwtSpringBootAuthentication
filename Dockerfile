FROM openjdk:17-jdk

WORKDIR /app

COPY target/AuthenticationApp-0.0.1-SNAPSHOT.jar /app/jwt-authentication-app.jar

EXPOSE 8080

CMD ["java", "-jar", "jwt-authentication-app.jar.jar"]