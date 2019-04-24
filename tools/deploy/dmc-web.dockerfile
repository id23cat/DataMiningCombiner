FROM maven:3.6.1-jdk-8 AS build
COPY . .
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine
COPY --from=build /DMCweb/target ./

CMD java -jar ./dmc-web-0.0.1-SNAPSHOT.war