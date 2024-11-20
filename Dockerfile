FROM openjdk:17-jdk-alpine
RUN mkdir /opt/myapp
COPY ./build/libs/java-refactoring-test-0.0.1-SNAPSHOT.jar /opt/myapp/app.jar
CMD ["java", "-jar", "/opt/myapp/app.jar"]