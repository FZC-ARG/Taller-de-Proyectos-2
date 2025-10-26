# Desmartin - Sistema de Evaluación de Inteligencias Múltiples

## Descripción del Proyecto

Este es un proyecto Full-Stack POC (Proof of Concept) desarrollado con Spring Boot 3 y Java 21, que incluye un sistema de gestión de usuarios, tests de inteligencias múltiples, y chat con IA simulado.

## Tecnologías Utilizadas

- **Backend**: Spring Boot 3.3.5
- **Lenguaje**: Java 21
- **Base de Datos**: MySQL
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Frontend**: HTML5 + JavaScript (Vanilla)
- **Seguridad**: BCrypt (solo para hash de contraseñas)

## Estructura del Proyecto

```
src/main/java/com/appmartin/desmartin/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AdminController.java
│   ├── AlumnoController.java
│   ├── AuthController.java
│   ├── ChatController.java
│   ├── DocenteController.java
│   └── TestController.java
├── dto/
│   ├── AdministradorDTO.java
│   ├── AlumnoDTO.java
│   ├── ChatMensajeDTO.java
│   ├── ChatSesionDTO.java
│   ├── CompletarTestRequest.java
│   ├── CrearAlumnoRequest.java
│   ├── CrearChatSesionRequest.java
│   ├── CrearDocenteRequest.java
│   ├── CrearMensajeRequest.java
│   ├── DocenteDTO.java
│   ├── LoginRequest.java
│   ├── LogAccesoDTO.java
│   ├── PreguntaDTO.java
│   └── ResultadoDTO.java
├── model/
│   ├── Administrador.java
│   ├── Alumno.java
│   ├── ChatMensaje.java
│   ├── ChatSesion.java
│   ├── IntentoTest.java
│   ├── LogAcceso.java
│   ├── PreguntaTest.java
│   ├── RespuestaAlumno.java
│   ├── ResultadoTest.java
│   ├── TipoInteligencia.java
│   └── Docente.java
├── repository/
│   ├── AdministradorRepository.java
│   ├── AlumnoRepository.java
│   ├── ChatMensajeRepository.java
│   ├── ChatSesionRepository.java
│   ├── DocenteRepository.java
│   ├── IntentoTestRepository.java
│   ├── LogAccesoRepository.java
│   ├── PreguntaTestRepository.java
│   ├── RespuestaAlumnoRepository.java
│   ├── ResultadoTestRepository.java
│   └── TipoInteligenciaRepository.java
└── service/
    ├── AdminService.java
    ├── AuthService.java
    ├── ChatService.java
    └── TestService.java
```

## Requisitos Previos

1. **Java 21** instalado
2. **Maven** instalado
3. **MySQL** instalado y ejecutándose
4. **MySQL Workbench** o cliente MySQL para gestión de base de datos

## Instalación y Configuración

### 1. Crear la Base de Datos

Ejecuta los siguientes comandos en MySQL:

```sql
CREATE DATABASE prmartin;
USE prmartin;
```

Las tablas se crearán automáticamente gracias a la configuración `spring.jpa.hibernate.ddl-auto=update` en `application.properties`.

### 2. Configurar la Conexión a MySQL

Edita el archivo `src/main/resources/application.properties` si necesitas cambiar la configuración de conexión:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/prmartin
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 3. Compilar y Ejecutar el Proyecto

#### Opción 1: Usando Maven Wrapper (recomendado)

```bash
# En Windows
.\mvnw.cmd spring-boot:run

# En Linux/Mac
./mvnw spring-boot:run
```

#### Opción 2: Usando Maven instalado

```bash
mvn spring-boot:run
```

#### Opción 3: Compilar y ejecutar JAR

```bash
mvn clean package
java -jar target/desmartin-0.0.1-SNAPSHOT.jar
```

### 4. Acceder a la Aplicación

Una vez iniciado el servidor, abre tu navegador en:

- **Frontend**: http://localhost:8081

El servidor estará ejecutándose en el puerto **8081**.

## Endpoints de la API

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login/admin` | Login de administrador |
| POST | `/api/auth/login/docente` | Login de docente |
| POST | `/api/auth/login/alumno` | Login de alumno |

**Ejemplo de Request Body:**
```json
{
  "nombreUsuario": "admin",
  "contrasena": "admin123"
}
```

### Gestión de Docentes (Admin)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/admin/docentes` | Crear docente |
| GET | `/api/admin/docentes` | Listar todos los docentes |
| PUT | `/api/admin/docentes/{id}` | Actualizar docente |
| DELETE | `/api/admin/docentes/{id}` | Eliminar docente |

### Gestión de Alumnos (Admin)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/admin/alumnos` | Crear alumno |
| GET | `/api/admin/alumnos` | Listar todos los alumnos |
| PUT | `/api/admin/alumnos/{id}` | Actualizar alumno |
| DELETE | `/api/admin/alumnos/{id}` | Eliminar alumno |

### Test de Inteligencia

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/test/preguntas` | Obtener todas las preguntas |
| POST | `/api/test/completar` | Completar un test |

**Ejemplo de Request Body para completar test:**
```json
{
  "idAlumno": 1,
  "respuestas": [
    {"idPregunta": 1, "puntaje": 5},
    {"idPregunta": 2, "puntaje": 3}
  ]
}
```

### Resultados

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/alumno/{idAlumno}/resultados/ultimo` | Último resultado del alumno |
| GET | `/api/alumno/{idAlumno}/resultados/historial` | Historial completo de resultados |
| GET | `/api/docente/alumnos/{idAlumno}/resultados` | Ver resultados de un alumno (docente) |
| GET | `/api/admin/logs` | Ver logs de acceso |

### Chat con IA

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/chat/sesiones` | Crear sesión de chat |
| GET | `/api/chat/sesiones/docente/{idDocente}` | Obtener sesiones de un docente |
| GET | `/api/chat/sesiones/{idSesion}` | Obtener detalles de una sesión |
| POST | `/api/chat/sesiones/{idSesion}/mensajes` | Enviar mensaje |
| GET | `/api/chat/sesiones/{idSesion}/mensajes` | Obtener mensajes de una sesión |
| PUT | `/api/chat/mensajes/{idMensaje}` | Actualizar mensaje |
| DELETE | `/api/chat/mensajes/{idMensaje}` | Eliminar mensaje |

## Estructura de Base de Datos

El sistema utiliza las siguientes tablas principales:

- **administradores**: Gestión de administradores
- **docentes**: Gestión de docentes
- **alumnos**: Gestión de alumnos
- **tipos_inteligencia**: Tipos de inteligencia múltiple
- **preguntas_test**: Preguntas del test
- **intentos_test**: Intentos de realización del test
- **respuestas_alumno**: Respuestas individuales de cada alumno
- **resultados_test**: Resultados calculados por tipo de inteligencia
- **log_accesos**: Auditoría de accesos al sistema
- **chat_sesiones**: Sesiones de chat entre docente e IA
- **chat_mensajes**: Mensajes de las sesiones de chat

## Características de Seguridad

- ✅ **Contraseñas hasheadas**: Todas las contraseñas se almacenan usando BCrypt
- ⚠️ **Endpoints abiertos**: Todos los endpoints están disponibles sin autenticación (para POC)
- ⚠️ **Sin JWT**: No se implementa autenticación por tokens

**Nota**: Este es un proyecto POC. Para producción, se debe implementar:
- Autenticación JWT
- Roles y permisos
- Validación de entrada
- Manejo de errores robusto
- Tests unitarios e integración

## Características de la IA Simulada

El chat con IA utiliza una simulación básica. La respuesta automática de la IA es simplemente un mensaje que repite lo que escribió el docente. Para implementar la funcionalidad real:

1. Obtener API Key de DeepSeek
2. Implementar llamada HTTP en `ChatService.java`
3. Remover el comentario `// TODO: Implementar llamada real a la API de DeepSeek aquí`

## Uso del Frontend

El frontend incluye:
- ✅ Formularios para todos los endpoints POST
- ✅ Botones para todos los endpoints GET
- ✅ Formularios para PUT y DELETE
- ✅ Área de visualización de respuestas JSON
- ✅ Interfaz de chat en tiempo real
- ✅ Diseño responsive y moderno

## Datos de Prueba

Puedes crear usuarios de prueba usando el frontend o directamente mediante POST requests:

**Crear un Admin:**
```bash
curl -X POST http://localhost:8081/api/admin/docentes \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario": "admin", "contrasena": "admin123"}'
```

**Crear un Docente:**
```bash
curl -X POST http://localhost:8081/api/admin/docentes \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario": "docente1", "contrasena": "docente123"}'
```

**Crear un Alumno:**
```bash
curl -X POST http://localhost:8081/api/admin/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombreCompleto": "Juan Pérez", "nombreUsuario": "juan123", "contrasena": "alumno123"}'
```

## Solución de Problemas

### Error de conexión a MySQL
- Verifica que MySQL esté ejecutándose
- Verifica las credenciales en `application.properties`
- Asegúrate de que la base de datos `prmartin` exista

### Puerto 8081 ya en uso
- Cambia el puerto en `application.properties`: `server.port=8082`
- Actualiza el frontend para usar el nuevo puerto

### Error de compilación
- Verifica que Java 21 esté instalado: `java -version`
- Verifica que Maven esté instalado: `mvn -version`
- Ejecuta `mvn clean install` para limpiar y reinstalar dependencias

## Próximos Pasos

Para convertir este POC en una aplicación de producción:

1. Implementar autenticación JWT
2. Agregar validación de entrada
3. Implementar manejo de errores global
4. Agregar tests unitarios e integración
5. Configurar logging adecuado
6. Implementar integración real con API de DeepSeek
7. Agregar documentación Swagger/OpenAPI
8. Implementar paginación en endpoints de listado
9. Agregar filtros y búsqueda avanzada

## Autor

Desarrollado como POC para Taller de Proyectos 2

## Licencia

Este proyecto es un POC educativo y está disponible para uso académico.

