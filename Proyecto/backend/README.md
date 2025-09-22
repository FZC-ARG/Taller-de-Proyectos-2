# Sistema de Gestión Académica con IA

Backend Spring Boot para el Sistema de Gestión Académica con integración de Inteligencia Artificial.

## 🚀 Características

- **Autenticación JWT** con refresh tokens y rotación
- **Gestión de usuarios** con roles (ADMIN, DOCENTE, ALUMNO)
- **Gestión académica** completa (cursos, matrículas, calificaciones)
- **Integración con IA** para solicitudes y respuestas
- **Sistema de auditoría** completo
- **Rate limiting** y seguridad avanzada
- **OpenAPI/Swagger** documentation
- **Tests** unitarios e integración

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Security** con JWT
- **Spring Data JPA** con Hibernate
- **MySQL** 8.0
- **Flyway** para migraciones
- **JJWT** para tokens
- **Bucket4j** para rate limiting
- **Testcontainers** para tests
- **OpenAPI 3** con Swagger UI

## 📋 Requisitos

- Java 21+
- Maven 3.6+
- MySQL 8.0+
- Git

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd Backend/appmartin
```

### 2. Configurar la base de datos

```sql
CREATE DATABASE prmartin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar variables de entorno

Crear archivo `.env` en la raíz del proyecto:

```env
DB_URL=jdbc:mysql://localhost:3306/prmartin?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASS=tu_password
JWT_SECRET=mySecretKey123456789012345678901234567890
JWT_EXP=900000
REFRESH_TOKEN_EXP=604800000
```

### 4. Ejecutar la aplicación

```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# O ejecutar tests
./mvnw test

# O ejecutar tests de integración
./mvnw verify
```

La aplicación estará disponible en: `http://localhost:8081`

## 📚 API Documentation

### Swagger UI
- **URL**: http://localhost:8081/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs

### Endpoints Principales

#### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/refresh` - Renovar token
- `POST /api/auth/logout` - Cerrar sesión
- `GET /api/auth/status` - Estado del servicio

#### Administración
- `GET /api/admin/administradores` - Listar administradores
- `POST /api/admin/administradores` - Crear administrador
- `GET /api/admin/dashboard` - Dashboard administrativo

#### Auditoría
- `GET /api/auditoria` - Obtener registros de auditoría
- `GET /api/auditoria/usuario/{usuario}` - Auditoría por usuario
- `GET /api/auditoria/estadisticas` - Estadísticas de auditoría

#### Pruebas
- `GET /api/test/connection` - Test de conexión
- `GET /api/test/roles` - Listar roles
- `GET /api/test/usuarios` - Listar usuarios

#### Actuator
- `GET /actuator/health` - Health check
- `GET /actuator/info` - Información de la aplicación

## 🔐 Autenticación

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": "admin1",
    "contrasena": "password123"
  }'
```

### Usar token en requests
```bash
curl -X GET http://localhost:8081/api/admin/administradores \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Refresh token
```bash
curl -X POST http://localhost:8081/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

## 🧪 Testing

### Ejecutar tests unitarios
```bash
./mvnw test
```

### Ejecutar tests de integración
```bash
./mvnw verify
```

### Ejecutar con cobertura
```bash
./mvnw test jacoco:report
```

## 📊 Monitoreo

### Health Check
```bash
curl http://localhost:8081/actuator/health
```

### Métricas
```bash
curl http://localhost:8081/actuator/metrics
```

## 🔒 Seguridad

### Características implementadas:
- ✅ JWT con refresh tokens
- ✅ Rate limiting (5 intentos/15min por IP)
- ✅ Headers de seguridad (CSP, X-Frame-Options, etc.)
- ✅ Validación de entradas con @Valid
- ✅ Auditoría completa
- ✅ Encriptación BCrypt (cost 12)
- ✅ CORS configurado

### Headers de seguridad:
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY`
- `X-XSS-Protection: 1; mode=block`
- `Content-Security-Policy: default-src 'self'`
- `Strict-Transport-Security: max-age=31536000`

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/prsanmartin/appmartin/
│   │   ├── config/          # Configuraciones
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositorios JPA
│   │   ├── security/       # Configuración de seguridad
│   │   ├── service/        # Servicios de negocio
│   │   └── util/           # Utilidades
│   └── resources/
│       ├── db/migration/   # Migraciones Flyway
│       └── application.properties
└── test/                   # Tests unitarios e integración
```

## 🗄️ Base de Datos

### Tablas principales:
- `Usuarios` - Usuarios del sistema
- `Roles` - Roles (ADMIN, DOCENTE, ALUMNO)
- `Alumnos` - Información de alumnos
- `Docentes` - Información de docentes
- `Cursos` - Cursos académicos
- `Matriculas` - Matrículas de alumnos
- `Calificaciones` - Calificaciones
- `SolicitudesIA` - Solicitudes de IA
- `RespuestasIA` - Respuestas de IA
- `TestsGardner` - Tests de inteligencias múltiples
- `Auditoria` - Registros de auditoría
- `RefreshTokens` - Tokens de renovación

## 🚀 Despliegue

### Variables de entorno para producción:
```env
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://prod-db:3306/prmartin
DB_USER=prod_user
DB_PASS=secure_password
JWT_SECRET=very_secure_secret_key_256_bits
```

### Docker (opcional):
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/appmartin-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📝 Postman Collection

Importar el archivo `postman_collection.json` en Postman para probar todos los endpoints.

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 📞 Soporte

Para soporte técnico, contactar a:
- Email: desarrollo@prmartin.com
- Documentación: http://localhost:8081/swagger-ui/index.html

## 🔄 Changelog

### v1.0.0
- ✅ Implementación inicial
- ✅ Autenticación JWT con refresh tokens
- ✅ Sistema de auditoría completo
- ✅ Rate limiting y seguridad
- ✅ OpenAPI/Swagger documentation
- ✅ Tests unitarios e integración
- ✅ Migraciones Flyway

