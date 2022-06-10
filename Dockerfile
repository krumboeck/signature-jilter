FROM openjdk:17

RUN groupadd -r app && useradd -r -s /bin/false -g app app

COPY target/*.jar /application.jar

USER app
EXPOSE 8025
VOLUME /config
VOLUME /templates
CMD ["java", "-Xms768m", "-Xmx2048m", "-XX:MaxMetaspaceSize=1024m", "-jar", "/application.jar"]
