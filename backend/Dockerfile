FROM maven AS dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

FROM dependencies AS build
COPY . .
RUN mvn package

FROM openjdk:8-jdk-alpine
#FROM java:8-jre
ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
COPY --from=build ${JAR_FILE} app.jar
#COPY static/* static/*
#COPY qualitool.p12 qualitool.p12
COPY keystore.properties keystore.properties

ENV LANG en_GB.UTF-8
RUN apk add --update ttf-dejavu ttf-droid ttf-freefont ttf-liberation ttf-ubuntu-font-family && rm -rf /var/cache/apk/*

RUN echo '#!/bin/sh' >> /entrypoint.sh
RUN echo 'keytool -import -alias localhost -file /cert/fullchain.pem -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt' >> /entrypoint.sh
RUN echo 'java -jar /app.jar' >> /entrypoint.sh

RUN chmod +x /entrypoint.sh

#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["/entrypoint.sh"]