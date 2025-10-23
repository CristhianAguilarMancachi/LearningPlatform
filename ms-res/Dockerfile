# ========== FASE DE CONSTRUCCIÓN ==========
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Copiar archivos del proyecto
COPY . .

# Ejecutar Maven dentro del contenedor para generar el .jar
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# ========== FASE FINAL ==========
FROM eclipse-temurin:17-jre-alpine
WORKDIR /application

# Crear usuario seguro
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

# Copiar el .jar generado desde la fase anterior
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]
