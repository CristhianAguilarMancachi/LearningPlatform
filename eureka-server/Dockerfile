# Fase de construcción
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Copiar el proyecto y construir el jar
COPY . .
#RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Extraer las capas del JAR
RUN java -Djarmode=layertools -jar target/*.jar extract --destination extracted

# Fase final
FROM eclipse-temurin:17-jre-alpine
WORKDIR /application

# Crear usuario seguro
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar las capas extraídas
COPY --from=builder /workspace/extracted/dependencies/ ./
COPY --from=builder /workspace/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/extracted/application/ ./

EXPOSE 8761

ENTRYPOINT ["java", "-cp","/application","org.springframework.boot.loader.launch.JarLauncher"]
