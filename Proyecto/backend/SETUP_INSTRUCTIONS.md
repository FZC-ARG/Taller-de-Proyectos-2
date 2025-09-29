# Test Gardner Backend - Setup Instructions

## 📋 Prerequisitos

- ✅ Java 21+
- ✅ Maven 3.6+
- ✅ XAMPP con MySQL 8.0+
- ✅ IDE recomendado: IntelliJ IDEA o VS Code

## 🚀 Setup Rápido

### 1. Setup Base de Datos

```bash
# Iniciar XAMPP MySQL (puerto 3306)
# Crear base de datos desde XAMPP Control Panel
# o ejecutar:
mysql -u root -p

# En MySQL console:
CREATE DATABASE prmartin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;

# Importar datos existentes
mysql -u root -p prmartin < "BD/prmartin (1).sql"
```

### 2. Configuración del Proyecto

```bash
# Clonar/Navegar al proyecto
cd backend

# Instalar dependencias
./mvnw clean compile

# Verificar configuración
cat appmartin/src/main/resources/application.properties
```

**Verificar configuración MySQL:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prmartin?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

### 3. Ejecución

```bash
# Build y test completo
./mvnw clean install

# Ejecutar aplicación
./mvnw spring-boot:run -pl appmartin

# La aplicación estará disponible en:
# http://localhost:8081
```

### 4. Verificación

#### A. Swagger UI
```
http://localhost:8081/swagger-ui.html
```

#### B. Endpoints Test Gardner
```
GET /api/test-gardner/questions/all
GET /api/test-gardner/1/can-take
POST /api/test-gardner/1/autosave
```

#### C. Base de Datos
```sql
-- Verificar migraciones aplicadas
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 5;

-- Verificar preguntas creadas  
SELECT COUNT(*) FROM preguntas_gardner;
-- Debería mostrar: 32

-- Verificar tabla TestsGardner extendida
DESCRIBE TestsGardner;
-- Debería mostrar nuevos campos: estado_guardado, version_guardado, etc.
```

## 🧪 Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Tests específicos del módulo Gardner
./mvnw test -Dtest=TestGardnerServiceTest
./mvnw test -Dtest=ScoringServiceTest  
./mvnw test -Dtest=TestGardnerIntegrationTest
```

## 🔧 Desarrollo

### Endpoints Disponibles

| Endpoint | Método | Rol | Descripción |
|----------|--------|-----|-------------|
| `/api/test-gardner/questions/all` | GET | ALUMNO/DOCENTE | Todas las preguntas |
| `/api/test-gardner/{id}/autosave` | POST | ALUMNO | Autoguardado |
| `/api/test-gardner/{id}/submit` | POST | ALUMNO | Finalizar test |
| `/api/test-gardner/{id}/history` | GET | ALUMNO | Historial |
| `/api/test-gardner/{id}/can-take` | GET | ALUMNO | Puede tomar test |

### Estructura de Respuesta Ejemplo

**Autosave Success:**
```json
{
  "status": "saved",
  "idTest": 42,
  "version": 2,
  "estado": "BORRADOR",
  "timestamp": "2024-12-17T10:30:00",
  "clientRequestId": "uuid-generated"
}
```

**Test Result:**
```json
{
  "idTest": 42,
  "idAlumno": 1,
  "puntajesBrutos": {"musical": 8, "logico": 6},
  "puntajesPonderados": {"musical": 80, "logico": 60},
  "inteligenciaPredominante": "musical",
  "puntajeTotal": 70.0,
  "estado": "CALCULADO",
  "descripcionInteligencia": "Inteligencia Musical-Rítmica...",
  "recomendacionesAcademicas": "Prueba actividades relacionadas con música..."
}
```

## 🐛 Troubleshooting

### Error de Conexión BD
```bash
# Verificar XAMPP MySQL corriendo
# Telnet al puerto:
telnet localhost 3306

# Verificar credenciales en application.properties
```

### Error de Migración Flyway
```sql
-- Reset Flyway si necesario
DELETE FROM flyway_schema_history WHERE version IN ('2', '3');
-- Reiniciar aplicación para reaplicar migraciones
```

### Error de Tests
```bash
# Limpiar y rebuild
./mvnw clean
./mvnw test

# Ejecutar sin Spring Boot context si hay problemas
./mvnw test -Dtest=TestGardnerServiceTest
```

## 📁 Archivos Importantes

### Migraciones Flyway
- `appmartin/src/main/resources/db/migration/V2__create_preguntas_gardner.sql`
- `appmartin/src/main/resources/db/migration/V3__extend_testsgardner_table.sql`

### Entidades Principales
- `appmartin/src/main/java/com/prsanmartin/appmartin/entity/PreguntaGardner.java`
- `appmartin/src/main/java/com/prsanmartin/appmartin/entity/TestGardner.java`

### Servicios
- `appmartin/src/main/java/com/prsanmartin/appmartin/service/TestGardnerService.java`
- `appmartin/src/main/java/com/prsanmartin/appmartin/service/TestGardnerServiceImpl.java`

### Controlador
- `appmartin/src/main/java/com/prsanmartin/appmartin/controller/TestGardnerController.java`

### DTOs
- `appmartin/src/main/java/com/prsanmartin/appmartin/dto/AutosaveRequestDTO.java`
- `appmartin/src/main/java/com/prsanmartin/appmartin/dto/TestResultDTO.java`

### Tests
- `appmartin/src/test/java/com/prsanmartin/appmartin/service/TestGardnerServiceTest.java`
- `appmartin/src/test/java/com/prsanmartin/appmartin/service/ScoringServiceTest.java`
- `appmartin/src/test/java/com/prsanmartin/appmartin/integration/TestGardnerIntegrationTest.java`

## ✅ Checklist de Verificación

- [ ] ✅ MySQL corriendo en puerto 3306
- [ ] ✅ Base de datos `prmartin` creada
- [ ] ✅ Aplicación inicia sin errores (`/actuator/health`)
- [ ] ✅ Swagger UI accesible en `/swagger-ui.html`
- [ ] ✅ Endpoints Test Gardner visibles en Swagger
- [ ] ✅ Migraciones aplicadas (V2, V3 en flyway_schema_history)
- [ ] ✅ Tabla `preguntas_gardner` con 32 registros
- [ ] ✅ Tests pasan exitosamente
- [ ] ✅ Autosave responde con status "saved"
- [ ] ✅ Submit calcula puntajes correctamente
- [ ] ✅ Sin errores en logs

---

**🎉 ¡Sistema listo para uso!**

Para cualquier problema adicional, revisar `analysis_report.md` para detalles técnicos completos.
