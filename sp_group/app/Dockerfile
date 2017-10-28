FROM openjdk:8

RUN mkdir -p /sp_group/deploy
COPY ./deploy/app-0.0.1-SNAPSHOT.jar /usr/local/bin/app-0.0.1-SNAPSHOT.jar
RUN chmod -R 777 /usr/local/bin/app-0.0.1-SNAPSHOT.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /usr/local/bin/app-0.0.1-SNAPSHOT.jar
