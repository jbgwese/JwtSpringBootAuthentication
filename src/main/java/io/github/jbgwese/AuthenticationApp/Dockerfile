FROM openjdk:17-jdk

WORKDIR /app

#COPY target/AuthenticationApp-0.0.1-SNAPSHOT.jar /app/jwt-authentication-app.jar
COPY target/spring-jwt-authentication.jar /app/spring-jwt-authentication.jar

EXPOSE 8085
CMD ["java", "-jar", "spring-jwt-authentication.jar"]
