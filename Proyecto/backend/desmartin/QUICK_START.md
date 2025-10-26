# Guía de Inicio Rápido - Desmartin

## ⚡ Inicio Rápido en 5 Pasos

### Paso 1: Verificar Requisitos

```bash
# Verificar Java 21
java -version

# Verificar Maven
mvn -version

# Verificar MySQL
mysql --version
```

### Paso 2: Crear Base de Datos

Abre MySQL Workbench o terminal MySQL y ejecuta:

```sql
CREATE DATABASE prmartin;
USE prmartin;
```

### Paso 3: Ejecutar el Proyecto

En la terminal, navega a la carpeta del proyecto y ejecuta:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

Espera a ver el mensaje:
```
Started DesmartinApplication in X.XXX seconds
```

### Paso 4: Abrir el Navegador

Abre tu navegador en:
```
http://localhost:8081
```

### Paso 5: Probar la Aplicación

1. **Crear un Docente:**
   - Usuario: `docente1`
   - Contraseña: `docente123`
   - Click en "Crear Docente"

2. **Crear un Alumno:**
   - Nombre Completo: `Juan Pérez`
   - Usuario: `juan123`
   - Contraseña: `alumno123`
   - Click en "Crear Alumno"

3. **Hacer Login:**
   - Selecciona "Login Docente"
   - Usuario: `docente1`
   - Contraseña: `docente123`
   - Click en "Login Docente"

## 📋 Flujo Completo de Prueba

### 1. Crear Usuarios

**Crear Docente:**
```json
POST /api/admin/docentes
{
  "nombreUsuario": "docente1",
  "contrasena": "docente123"
}
```

**Crear Alumno:**
```json
POST /api/admin/alumnos
{
  "nombreCompleto": "Juan Pérez",
  "nombreUsuario": "juan123",
  "contrasena": "alumno123"
}
```

### 2. Hacer Login

**Login Alumno:**
```json
POST /api/auth/login/alumno
{
  "nombreUsuario": "juan123",
  "contrasena": "alumno123"
}
```

**Resultado:** Deberías recibir el DTO del alumno con su ID.

### 3. Completar un Test

**Completar Test:**
```json
POST /api/test/completar
{
  "idAlumno": 1,
  "respuestas": [
    {"idPregunta": 1, "puntaje": 5},
    {"idPregunta": 2, "puntaje": 3},
    {"idPregunta": 3, "puntaje": 4}
  ]
}
```

**Nota:** Primero debes obtener las preguntas con `GET /api/test/preguntas` para saber los IDs de las preguntas.

### 4. Ver Resultados

**Último Resultado:**
```json
GET /api/alumno/1/resultados/ultimo
```

**Historial:**
```json
GET /api/alumno/1/resultados/historial
```

### 5. Probar Chat

**Crear Sesión:**
```json
POST /api/chat/sesiones
{
  "idDocente": 1,
  "idAlumno": 1,
  "tituloSesion": "Consulta sobre resultados"
}
```

**Enviar Mensaje:**
```json
POST /api/chat/sesiones/1/mensajes
{
  "contenido": "¿Cómo interpreto los resultados?"
}
```

**Ver Mensajes:**
```json
GET /api/chat/sesiones/1/mensajes
```

## 🐛 Solución de Problemas Comunes

### Error: "Cannot connect to MySQL"

**Solución:**
1. Verifica que MySQL esté ejecutándose
2. Verifica las credenciales en `application.properties`
3. Verifica que la base de datos `prmartin` exista

### Error: "Port 8081 already in use"

**Solución:**
1. Cambia el puerto en `application.properties`:
   ```properties
   server.port=8082
   ```
2. Actualiza el frontend para usar el nuevo puerto

### Error: "Class not found"

**Solución:**
```bash
mvn clean install
mvn spring-boot:run
```

### Las tablas no se crean automáticamente

**Solución:**
1. Verifica que `spring.jpa.hibernate.ddl-auto=update` esté en `application.properties`
2. Verifica la conexión a MySQL
3. Verifica los logs del servidor

## 📱 Uso del Frontend

### Interfaz Principal

El frontend incluye:

1. **Sección de Autenticación**
   - 3 formularios de login
   - Campos de usuario y contraseña

2. **Gestión de Docentes**
   - Crear, listar, actualizar, eliminar

3. **Gestión de Alumnos**
   - Crear, listar, actualizar, eliminar

4. **Test de Inteligencia**
   - Obtener preguntas
   - Completar test con JSON

5. **Resultados**
   - Ver último resultado
   - Ver historial
   - Ver logs

6. **Chat con IA**
   - Crear sesiones
   - Enviar mensajes
   - Ver conversación

### Formato JSON para Respuestas

Al completar un test, usa este formato:

```json
[
  {"puntaje": 5},
  {"puntaje": 3},
  {"puntaje": 4}
]
```

Pero debes incluir el `idPregunta`:

```json
[
  {"idPregunta": 1, "puntaje": 5},
  {"idPregunta": 2, "puntaje": 3},
  {"idPregunta": 3, "puntaje": 4}
]
```

## 🎯 Pruebas Rápidas con Postman

Importa estos requests en Postman:

### Colección de Ejemplos

**1. Crear Docente**
- Method: POST
- URL: `http://localhost:8081/api/admin/docentes`
- Body (JSON):
```json
{
  "nombreUsuario": "docente1",
  "contrasena": "docente123"
}
```

**2. Login Docente**
- Method: POST
- URL: `http://localhost:8081/api/auth/login/docente`
- Body (JSON):
```json
{
  "nombreUsuario": "docente1",
  "contrasena": "docente123"
}
```

**3. Listar Docentes**
- Method: GET
- URL: `http://localhost:8081/api/admin/docentes`

**4. Obtener Preguntas**
- Method: GET
- URL: `http://localhost:8081/api/test/preguntas`

**5. Ver Logs**
- Method: GET
- URL: `http://localhost:8081/api/admin/logs`

## 🔍 Verificación de Instalación

Para verificar que todo está funcionando:

1. **Verifica la conexión a MySQL:**
   ```bash
   mysql -u root -p
   USE prmartin;
   SHOW TABLES;
   ```

2. **Verifica que el servidor esté corriendo:**
   ```bash
   curl http://localhost:8081/api/admin/docentes
   ```

3. **Verifica el frontend:**
   Abre `http://localhost:8081` en tu navegador

## 📊 Ejemplo de Flujo Completo

### Secuencia de Operaciones

```
1. Crear Docente
   ↓
2. Crear Alumno
   ↓
3. Login Alumno
   ↓
4. Obtener Preguntas
   ↓
5. Completar Test
   ↓
6. Ver Resultados
   ↓
7. Crear Sesión Chat
   ↓
8. Enviar Mensaje
   ↓
9. Ver Mensajes
```

### Resultado Esperado

Al finalizar este flujo deberías tener:

- ✅ 1 docente creado
- ✅ 1 alumno creado
- ✅ 1 intento de test registrado
- ✅ Respuestas guardadas
- ✅ Resultados calculados
- ✅ 1 sesión de chat creada
- ✅ Mensajes intercambiados
- ✅ Logs de acceso registrados

## 💡 Tips Útiles

1. **Usa el frontend** para pruebas rápidas
2. **Usa Postman/cURL** para pruebas más avanzadas
3. **Revisa los logs** del servidor para debugging
4. **Consulta ENDPOINTS.md** para documentación completa
5. **Las contraseñas** se hashean automáticamente

## 🚀 Próximos Pasos

Una vez que hayas probado el POC:

1. Revisa el código fuente
2. Experimenta con diferentes endpoints
3. Crea más usuarios y tests
4. Prueba el chat con múltiples sesiones
5. Explora los logs de acceso

## 📞 Soporte

Si encuentras problemas:

1. Revisa `README.md` para documentación completa
2. Revisa `ENDPOINTS.md` para ejemplos de endpoints
3. Revisa los logs del servidor
4. Verifica que todos los requisitos estén instalados

---

**¡Disfruta probando el sistema!** 🎉

