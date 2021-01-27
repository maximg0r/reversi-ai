FROM gradle:jdk11

# set a directory for the app
WORKDIR /usr/src/app

# copy all the files to the container
COPY . .

RUN gradle build

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/reversi-0.0.1-SNAPSHOT.jar"]