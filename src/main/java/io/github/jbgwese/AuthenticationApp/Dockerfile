FROM openjdk:17-jdk

RUN apt-get update && apt-get install -y postgresql

USER postgres

RUN /etc/init.d/postgresql start && \
    psql --command "CREATE USER job WITH SUPERUSER PASSWORD 'password';" && \
    createdb -O postgres job

EXPOSE 5432

CMD ["postgres"]



#WORKDIR /app

#COPY target/AuthenticationApp-0.0.1-SNAPSHOT.jar /app/jwt-authentication-app.jar
#COPY ./target/spring-jwt-authentication.jar /app/spring-jwt-authentication.jar

EXPOSE 8085
CMD ["java", "-jar", "spring-jwt-authentication.jar"]
