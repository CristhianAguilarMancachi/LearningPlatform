# Análisis del Microservicio de Usuarios

## Tabla de Evaluación

| Criterio de Evaluación | Descripción | Evidencia |
|------------------------|-------------|-----------|
| Configuración de Base de Datos | Base de datos H2 en memoria configurada correctamente | ```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.jpa.hibernate.ddl-auto=create-drop
``` |
| Endpoints CRUD | Implementación completa de operaciones CRUD con documentación Swagger | ```java
@RestController
@RequestMapping("/usuarios")
- POST /usuarios (crear)
- GET /usuarios (listar)
- GET /usuarios/{id} (obtener)
- PUT /usuarios/{id} (actualizar)
- DELETE /usuarios/{id} (eliminar)
``` |
| Tipos de Consultas | Se identificaron los siguientes tipos de consultas | 1. **Derived Query**: `existsByEmail(String email)`
2. **JPA Repository**: Operaciones CRUD básicas heredadas
3. No se encontraron Native Queries ni Criteria Queries |
| Configuración Eureka | Cliente Eureka configurado correctamente | ```properties
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
``` |
| Swagger/OpenAPI | Documentación API configurada | ```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
``` |

## Detalles de la Implementación

### Base de Datos
- Utiliza H2 como base de datos en memoria
- Consola H2 habilitada en `/h2-console`
- DDL automático en modo `create-drop`
- SQL logging habilitado

### API REST
- API RESTful completamente documentada con OpenAPI/Swagger
- Endpoints organizados siguiendo mejores prácticas REST
- Manejo de respuestas HTTP apropiado

### Queries JPA
1. **Derived Queries**:
   - `existsByEmail`: Verifica la existencia de un usuario por email
2. **JPA Repository**:
   - findAll()
   - findById()
   - save()
   - delete()

### Service Discovery
- Cliente Eureka habilitado con `@EnableDiscoveryClient`
- Configurado para registrarse en Eureka Server
- Preferencia de IP sobre hostname para registro

## Puntos de Mejora Recomendados

1. **Consultas**:
   - Agregar Native Queries para consultas complejas
   - Implementar Criteria Queries para búsquedas dinámicas

2. **Base de Datos**:
   - Considerar migrar a una base de datos persistente para producción
   - Implementar migración de datos con Flyway o Liquibase

3. **Seguridad**:
   - Agregar autenticación y autorización
   - Implementar JWT o OAuth2

## Cómo Probar

1. **Acceder a la documentación API**:
   ```
   http://localhost:8082/swagger-ui.html
   ```

2. **Consola H2**:
   ```
   http://localhost:8082/h2-console
   JDBC URL: jdbc:h2:mem:testdb
   Usuario: sa
   Password: [vacío]
   ```

3. **Verificar registro en Eureka**:
   ```
   http://localhost:8761
   ```