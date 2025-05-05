FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/app.jar app.jar

ENV PORT=${PORT}
EXPOSE ${PORT}

CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]