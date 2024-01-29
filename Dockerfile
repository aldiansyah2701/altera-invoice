FROM eclipse-temurin:11
ARG JAR_FILE=target/*.jar
WORKDIR /opt
ENV PORT 8081
EXPOSE 8081
COPY ${JAR_FILE} invoice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","invoice-0.0.1-SNAPSHOT.jar"]