FROM openjdk:11-jre-slim
WORKDIR usr/src
ADD ./target/covidstats.jar /usr/src/covidstats.jar
ENTRYPOINT ["java","-jar","covidstats.jar"]
