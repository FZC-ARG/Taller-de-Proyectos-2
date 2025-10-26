# API Endpoints - Desmartin

## Base URL
```
http://localhost:8081/api
```

## Autenticación

### Login Administrador
```http
POST /api/auth/login/admin
Content-Type: application/json

{
  "nombreUsuario": "admin",
  "contrasena": "admin123"
}
```

**Respuesta exitosa (200):**
```json
{
  "idAdmin": 1,
  "nombreUsuario": "admin"
}
```

**Respuesta error (401):**
```json
"Credenciales inválidas"
```

---

### Login Docente
```http
POST /api/auth/login/docente
Content-Type: application/json

{
  "nombreUsuario": "docente1",
  "contrasena": "docente123"
}
```

**Respuesta exitosa (200):**
```json
{
  "idDocente": 1,
  "nombreUsuario": "docente1"
}
```

---

### Login Alumno
```http
POST /api/auth/login/alumno
Content-Type: application/json

{
  "nombreUsuario": "alumno1",
  "contrasena": "alumno123"
}
```

**Respuesta exitosa (200):**
```json
{
  "idAlumno": 1,
  "nombreCompleto": "Juan Pérez",
  "nombreUsuario": "alumno1"
}
```

---

## Gestión de Docentes (Admin)

### Crear Docente
```http
POST /api/admin/docentes
Content-Type: application/json

{
  "nombreUsuario": "docente1",
  "contrasena": "docente123"
}
```

**Respuesta (200):**
```json
{
  "idDocente": 1,
  "nombreUsuario": "docente1"
}
```

---

### Listar Docentes
```http
GET /api/admin/docentes
```

**Respuesta (200):**
```json
[
  {
    "idDocente": 1,
    "nombreUsuario": "docente1"
  },
  {
    "idDocente": 2,
    "nombreUsuario": "docente2"
  }
]
```

---

### Actualizar Docente
```http
PUT /api/admin/docentes/{id}
Content-Type: application/json

{
  "nombreUsuario": "docente_actualizado",
  "contrasena": "nuevaContrasena123"
}
```

**Respuesta (200):**
```json
{
  "idDocente": 1,
  "nombreUsuario": "docente_actualizado"
}
```

---

### Eliminar Docente
```http
DELETE /api/admin/docentes/{id}
```

**Respuesta (200):** Sin contenido

---

## Gestión de Alumnos (Admin)

### Crear Alumno
```http
POST /api/admin/alumnos
Content-Type: application/json

{
  "nombreCompleto": "Juan Pérez",
  "nombreUsuario": "juan123",
  "contrasena": "alumno123"
}
```

**Respuesta (200):**
```json
{
  "idAlumno": 1,
  "nombreCompleto": "Juan Pérez",
  "nombreUsuario": "juan123\n"
}
```

---

### Listar Alumnos
```http
GET /api/admin/alumnos
```

**Respuesta (200):**
```json
[
  {
    "idAlumno": 1,
    "nombreCompleto": "Juan Pérez",
    "nombreUsuario": "juan123"
  }
]
```

---

### Actualizar Alumno
```http
PUT /api/admin/alumnos/{id}
Content-Type: application/json

{
  "nombreCompleto": "Juan Carlos Pérez",
  "nombreUsuario": "juan_perez",
  "contrasena": "nuevaContrasena123"
}
```

**Respuesta (200):**
```json
{
  "idAlumno": 1,
  "nombreCompleto": "Juan Carlos Pérez",
  "nombreUsuario": "juan_perez"
}
```

---

### Eliminar Alumno
```http
DELETE /api/admin/alumnos/{id}
```

**Respuesta (200):** Sin contenido

---

## Test de Inteligencia

### Obtener Preguntas
```http
GET /api/test/preguntas
```

**Respuesta (200):**
```json
[
  {
    "idPregunta": 1,
    "idInteligencia": 1,
    "nombreInteligencia": "Lingüística",
    "textoPregunta": "¿Disfrutas escribiendo historias o poemas?"
  },
  {
    "idPregunta": 2,
    "idInteligencia": 2,
    "nombreInteligencia": "Lógico-Matemática",
    "textoPregunta": "¿Te gusta resolver problemas matemáticos?"
  }
]
```

---

### Completar Test
```http
POST /api/test/completar
Content-Type: application/json

{
  "idAlumno": 1,
  "respuestas": [
    {
      "idPregunta": 1,
      "puntaje": 5
    },
    {
      "idPregunta": 2,
      "puntaje": 3
    },
    {
      "idPregunta": 3,
      "puntaje": 4
    }
  ]
}
```

**Respuesta (200):**
```json
"Test completado exitosamente"
```

**Nota:** Esta operación es transaccional y realiza lo siguiente:
1. Crea un intento de test
2. Guarda todas las respuestas
3. Calcula puntajes por tipo de inteligencia
4. Guarda los resultados calculados

---

## Resultados

### Último Resultado del Alumno
```http
GET /api/alumno/{idAlumno}/resultados/ultimo
```

**Respuesta (200):**
```json
[
  {
    "idResultado": 1,
    "idIntento": 1,
    "fechaRealizacion": "2024-12-20T10:30:00",
    "idInteligencia": 1,
    "nombreInteligencia": "Lingüística",
    "puntajeCalculado": 4.5
  },
  {
    "idResultado": 2,
    "idIntento": 1,
    "fechaRealizacion": "2024-12-20T10:30:00",
    "idInteligencia": 2,
    "nombreInteligencia": "Lógico-Matemática",
    "puntajeCalculado": 3.5
  }
]
```

---

### Historial de Resultados del Alumno
```http
GET /api/alumno/{idAlumno}/resultados/historial
```

**Respuesta (200):**
```json
[
  {
    "idResultado": 1,
    "idIntento": 1,
    "fechaRealizacion": "2024-12-20T10:30:00",
    "idInteligencia": 1,
    "nombreInteligencia": "Lingüística",
    "puntajeCalculado": 4.5
  },
  {
    "idResultado": 2,
    "idIntento": 1,
    "fechaRealizacion": "2024-12-20T10:30:00",
    "idInteligencia": 2,
    "nombreInteligencia": "Lógico-Matemática",
    "puntajeCalculado": 3.5
  },
  {
    "idResultado": 3,
    "idIntento": 2,
    "fechaRealizacion": "2024-12-21T14:20:00",
    "idInteligencia": 1,
    "nombreInteligencia": "Lingüística",
    "puntajeCalculado": 5.0
  }
]
```

---

### Ver Resultados de Alumno (Docente)
```http
GET /api/docente/alumnos/{idAlumno}/resultados
```

**Respuesta:** Igual que el historial de resultados

---

### Ver Logs de Acceso
```http
GET /api/admin/logs
```

**Respuesta (200):**
```json
[
  {
    "idLog": 1,
    "idUsuario": 1,
    "tipoUsuario": "admin",
    "fechaHoraAcceso": "2024-12-20T08:00:00"
  },
  {
    "idLog": 2,
    "idUsuario": 1,
    "tipoUsuario": "docente",
    "fechaHoraAcceso": "2024-12-20T09:15:00"
  },
  {
    "idLog": 3,
    "idUsuario": 2,
    "tipoUsuario": "alumno",
    "fechaHoraAcceso": "2024-12-20T10:30:00"
  }
]
```

---

## Chat con IA

### Crear Sesión de Chat
```http
POST /api/chat/sesiones
Content-Type: application/json

{
  "idDocente": 1,
  "idAlumno": 1,
  "tituloSesion": "Consulta sobre resultados"
}
```

**Respuesta (200):**
```json
{
  "idSesion": 1,
  "idDocente": 1,
  "idAlumno": 1,
  "tituloSesion": "Consulta sobre resultados",
  "fechaCreacion": "2024-12-20T10:00:00"
}
```

**Nota:** El campo `idAlumno` es opcional

---

### Obtener Sesiones de un Docente
```http
GET /api/chat/sesiones/docente/{idDocente}
```

**Respuesta (200):**
```json
[
  {
    "idSesion": 1,
    "idDocente": 1,
    "idAlumno": 1,
    "tituloSesion": "Consulta sobre resultados",
    "fechaCreacion": "2024-12-20T10:00:00"
  },
  {
    "idSesion": 2,
    "idDocente": 1,
    "idAlumno": null,
    "tituloSesion": "Dudas generales",
    "fechaCreacion": "2024-12-20T11:00:00"
  }
]
```

---

### Obtener Detalles de una Sesión
```http
GET /api/chat/sesiones/{idSesion}
```

**Respuesta (200):**
```json
{
  "idSesion": 1,
  "idDocente": 1,
  "idAlumno": 1,
  "tituloSesion": "Consulta sobre resultados",
  "fechaCreacion": "2024-12-20T10:00:00"
}
```

---

### Enviar Mensaje
```http
POST /api/chat/sesiones/{idSesion}/mensajes
Content-Type: application/json

{
  "contenido": "¿Cómo interpreto los resultados del test?"
}
```

**Respuesta (200):**
```json
{
  "idMensaje": 2,
  "idSesion": 1,
  "emisor": "ia",
  "contenido": "Esta es una respuesta simulada de la IA. El docente escribió: ¿Cómo interpreto los resultados del test?",
  "fechaHoraEnvio": "2024-12-20T10:05:00"
}
```

**Nota:** 
- Guarda el mensaje del docente
- Genera automáticamente una respuesta simulada de la IA
- Retorna el mensaje de la IA

---

### Obtener Mensajes de una Sesión
```http
GET /api/chat/sesiones/{idSesion}/mensajes
```

**Respuesta (200):**
```json
[
  {
    "idMensaje": 1,
    "idSesion": 1,
    "emisor": "docente",
    "contenido": "¿Cómo interpreto los resultados del test?",
    "fechaHoraEnvio": "2024-12-20T10:04:00"
  },
  {
    "idMensaje": 2,
    "idSesion": 1,
    "emisor": "ia",
    "contenido": "Esta es una respuesta simulada de la IA. El docente escribió: ¿Cómo interpreto los resultados del test?",
    "fechaHoraEnvio": "2024-12-20T10:05:00"
  }
]
```

---

### Actualizar Mensaje
```http
PUT /api/chat/mensajes/{idMensaje}
Content-Type: application/json

{
  "contenido": "Mensaje actualizado"
}
```

**Respuesta (200):**
```json
{
  "idMensaje": 1,
  "idSesion": 1,
  "emisor": "docente",
  "contenido": "Mensaje actualizado",
  "fechaHoraEnvio": "2024-12-20T10:04:00"
}
```

---

### Eliminar Mensaje
```http
DELETE /api/chat/mensajes/{idMensaje}
```

**Respuesta (200):** Sin contenido

---

## Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **401 Unauthorized**: Credenciales inválidas (solo en login)
- **500 Internal Server Error**: Error del servidor

## Notas Importantes

1. **Seguridad**: Todos los endpoints están abiertos (sin autenticación requerida). Esto es intencional para el POC.

2. **Contraseñas**: Todas las contraseñas se hashean automáticamente con BCrypt antes de guardarse.

3. **Timestamps**: Se generan automáticamente para:
   - `fechaRealizacion` en intentos_test
   - `fechaHoraAcceso` en log_accesos
   - `fechaCreacion` en chat_sesiones
   - `fechaHoraEnvio` en chat_mensajes

4. **Transacciones**: El endpoint `POST /api/test/completar` es transaccional. Si falla alguna parte, se revierte todo.

5. **Simulación de IA**: El chat con IA es simulado. Ver `ChatService.java` para implementar la integración real con DeepSeek.

6. **Formato de Fechas**: Las fechas se retornan en formato ISO 8601 (`yyyy-MM-ddTHH:mm:ss`).

## Ejemplos de Uso con cURL

### Crear un docente
```bash
curl -X POST http://localhost:8081/api/admin/docentes \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario": "docente1", "contrasena": "docente123"}'
```

### Login de docente
```bash
curl -X POST http://localhost:8081/api/auth/login/docente \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario": "docente1", "contrasena": "docente123"}'
```

### Obtener preguntas
```bash
curl http://localhost:8081/api/test/preguntas
```

### Completar test
```bash
curl -X POST http://localhost:8081/api/test/completar \
  -H "Content-Type: application/json" \
  -d '{"idAlumno": 1, "respuestas": [{"idPregunta": 1, "puntaje": 5}]}'
```

