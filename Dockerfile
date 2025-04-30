FROM eclipse-temurin:21-jdk-alpine

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik JAR do kontenera
COPY target/*.jar app.jar

# Ustaw zmienną portu z Railway
ENV PORT=${PORT}

# Otwórz port (Railway ustawi PORT)
EXPOSE ${PORT}

# Uruchom aplikację
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]