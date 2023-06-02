FROM maven as builder

COPY . /app

RUN cd /app && mvn install

FROM openjdk:17-jdk-slim

COPY --from=builder /app/target/tenpo-1.0.1.jar /opt/backend.jar

CMD java -jar /opt/backend.jar


