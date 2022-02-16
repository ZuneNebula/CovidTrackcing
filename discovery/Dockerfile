FROM openjdk:11-jre-slim
WORKDIR usr/src
ADD ./target/discovery.jar /usr/src/discovery.jar
ENTRYPOINT ["java","-jar","discovery.jar"]
