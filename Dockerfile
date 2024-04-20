FROM ghcr.io/mulecode/tool-set-java:1.5.0 as builder
WORKDIR /app
COPY ./data ./data
COPY ./src ./src
COPY ./pom.xml .
RUN mvn clean install

FROM ghcr.io/mulecode/tool-set-java:1.5.0
WORKDIR /
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-server", "-jar", "app.jar"]
