# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Önce bağımlılıkları kopyalayarak önbellek avantajı sağlayalım
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Kaynak kodunu kopyala ve build et
COPY src ./src
RUN mvn clean package -DskipTests
# JAR dosyasının adını pom.xml'den bağımsız olarak alalım
RUN mv target/*.jar app.jar

# Runtime Stage
FROM openjdk:17-jre-slim
WORKDIR /app
# Build stage'den JAR dosyasını kopyala
COPY --from=build /app/app.jar app.jar
# Non-root kullanıcı oluştur
RUN useradd -m appuser
USER appuser
# Railway'in dinamik portunu kullan
EXPOSE ${PORT}
# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]
