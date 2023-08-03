FROM openjdk:11-jdk

RUN groupadd app && useradd app -g app
RUN mkdir -m 0755 -p /usr/local/app/bin
RUN chown -R app:app /usr/local/app
RUN wget -O /usr/local/app/bin/apm-agent.jar https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.28.4/elastic-apm-agent-1.28.4.jar


ARG JAR_FILE=target/*.jar
ARG NAME

LABEL name=${NAME}

COPY ${JAR_FILE} /usr/local/app/bin/application.jar

EXPOSE 8090

ENTRYPOINT ["/usr/local/openjdk-11/bin/java", "-jar", "/usr/local/app/bin/application.jar"]
