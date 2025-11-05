# 🔄 Implementación: Reutilización de Sesiones de Chat Activas

## ✅ Historia de Usuario Implementada

**Como docente, quiero que cuando vuelva a ingresar a la sesión de un alumno, el sistema me devuelva automáticamente la última conversación que tuve con la IA sobre ese alumno. Si no existe una conversación previa, el sistema debe comportarse igual que ahora, creando una nueva sesión.**

---

## 📋 Resumen de Cambios

### **Archivos Modificados:**

1. ✅ **ChatSesionRepository.java** - Agregado método para buscar última sesión activa
2. ✅ **ChatService.java** - Modificada lógica de `crearSesion()` para reutilizar sesiones existentes
3. ✅ **ChatService.crearMensaje()** - Mejorado logging para historial de mensajes

### **Archivos que NO requieren cambios:**

- ✅ **ContextoIAService.java** - Ya maneja el historial correctamente
- ✅ **ChatController.java** - La interfaz pública no cambia
- ✅ **OpenRouterService.java** - No requiere cambios

---

## 🔧 Implementación Técnica

### **1. Método en ChatSesionRepository**

Se agregó un método para buscar la última sesión activa entre un docente y un alumno:

```java
@Query("SELECT s FROM ChatSesion s WHERE s.docente.idDocente = :idDocente AND s.alumno.idAlumno = :idAlumno AND s.curso IS NULL ORDER BY s.fechaCreacion DESC")
List<ChatSesion> findUltimaSesionActivaPorDocenteYAlumno(@Param("idDocente") Integer idDocente, @Param("idAlumno") Integer idAlumno);
```

**Características:**
- Busca sesiones individuales (sin curso)
- Filtra por docente y alumno específicos
- Ordena por fecha de creación descendente (más reciente primero)
- Retorna lista (primer elemento es la sesión más reciente)

---

### **2. Lógica de Reutilización en ChatService**

#### **Flujo para Chats Individuales:**

```java
1. Validar que docente y alumno existen
   ↓
2. Buscar sesión activa previa entre docente y alumno
   ↓
3. ¿Existe sesión previa?
   ├─ SÍ → Reutilizar sesión existente
   │       - Actualizar título si se proporciona uno nuevo
   │       - Retornar DTO de sesión existente
   │       - Log: "Reutilizando sesión existente ID: X"
   │
   └─ NO → Crear nueva sesión
           - Crear nueva entidad ChatSesion
           - Asignar docente y alumno
           - Guardar en BD
           - Retornar DTO de nueva sesión
           - Log: "Creando nueva sesión individual"
```

#### **Flujo para Chats Grupales:**

```java
1. Validar que docente y curso existen
   ↓
2. Validar que docente dicta el curso
   ↓
3. Crear nueva sesión grupal
   (No reutiliza sesiones grupales)
```

---

### **3. Carga Automática del Historial**

El historial de mensajes se carga automáticamente en `crearMensaje()`:

```java
// Obtener historial completo de mensajes previos de la sesión
List<ChatMensajeDTO> historialMensajes = obtenerMensajesPorSesion(idSesion).stream()
    .filter(m -> m.getIdMensaje() != mensajeDocente.getIdMensaje())
    .collect(Collectors.toList());

logger.info("Historial de mensajes cargado: {} mensajes previos", historialMensajes.size());
```

**Luego se pasa al ContextoIAService:**

```java
List<Map<String, String>> mensajesParaIA = contextoIAService.construirMensajesParaIA(
    contexto,
    request.getContenido(),
    historialMensajes  // ← Historial completo incluido aquí
);
```

El `ContextoIAService.construirMensajesParaIA()` ya maneja el historial correctamente:

```java
// Agregar historial de mensajes (si existe)
if (historialMensajes != null && !historialMensajes.isEmpty()) {
    for (ChatMensajeDTO mensaje : historialMensajes) {
        String role = mensaje.getEmisor().equals("docente") ? "user" : "assistant";
        mensajes.add(Map.of("role", role, "content", mensaje.getContenido()));
    }
}
```

---

## 🎯 Cumplimiento de Requisitos

### ✅ **1. Búsqueda de última sesión activa entre idDocente y idAlumno**

**Implementado en:** `ChatSesionRepository.findUltimaSesionActivaPorDocenteYAlumno()`

```java
List<ChatSesion> sesionesExistentes = chatSesionRepository
    .findUltimaSesionActivaPorDocenteYAlumno(request.getIdDocente(), request.getIdAlumno());
```

**Características:**
- Query JPA optimizada
- Filtra por docente y alumno
- Ordena por fecha descendente
- Solo sesiones individuales (sin curso)

---

### ✅ **2. Recuperación de mensajes previos si existen**

**Implementado en:** `ChatService.crearMensaje()`

```java
List<ChatMensajeDTO> historialMensajes = obtenerMensajesPorSesion(idSesion).stream()
    .filter(m -> m.getIdMensaje() != mensajeDocente.getIdMensaje())
    .collect(Collectors.toList());
```

**Características:**
- Se ejecuta automáticamente al crear un mensaje
- Obtiene todos los mensajes de la sesión (docente e IA)
- Excluye el mensaje recién guardado
- Se pasa al servicio de contexto para la IA

---

### ✅ **3. Reutilización de sesión existente si está activa**

**Implementado en:** `ChatService.crearSesion()`

```java
if (!sesionesExistentes.isEmpty()) {
    // Reutilizar la última sesión activa
    ChatSesion sesionExistente = sesionesExistentes.get(0);
    logger.info("Reutilizando sesión existente ID: {} entre docente {} y alumno {}", 
        sesionExistente.getIdSesion(), request.getIdDocente(), request.getIdAlumno());
    
    // Actualizar título si se proporciona uno nuevo
    if (request.getTituloSesion() != null && !request.getTituloSesion().trim().isEmpty()) {
        sesionExistente.setTituloSesion(request.getTituloSesion());
        chatSesionRepository.save(sesionExistente);
    }
    
    return new ChatSesionDTO(...);
}
```

**Características:**
- Retorna la sesión existente en lugar de crear una nueva
- Actualiza el título si se proporciona uno nuevo
- Mantiene todos los mensajes previos
- Logging detallado para debugging

---

### ✅ **4. Creación de nueva sesión solo si no existe previa**

**Implementado en:** `ChatService.crearSesion()`

```java
if (!sesionesExistentes.isEmpty()) {
    // Reutilizar sesión existente
} else {
    // No existe sesión previa, crear nueva
    logger.info("No se encontró sesión previa, creando nueva sesión individual");
    ChatSesion sesion = new ChatSesion();
    // ... crear nueva sesión
}
```

**Características:**
- Solo crea nueva sesión si no existe previa
- Comportamiento idéntico al anterior si no hay sesión previa
- Logging para tracking

---

### ✅ **5. Carga automática del contexto histórico en la IA**

**Implementado en:** 
- `ChatService.crearMensaje()` - Obtiene historial
- `ContextoIAService.construirMensajesParaIA()` - Construye mensajes con historial

**Flujo completo:**

```
1. ChatService.crearMensaje()
   ↓ Obtiene historial de mensajes
   
2. ContextoIAService.generarContextoAlumno()
   ↓ Genera contexto actualizado del alumno
   
3. ContextoIAService.construirMensajesParaIA()
   ↓ Construye mensajes para IA:
   - System message (contexto del alumno)
   - Historial de mensajes previos (docente ↔ IA)
   - Nuevo mensaje del docente
   
4. OpenRouterService.enviarMensaje()
   ↓ Envía a OpenRouter API con historial completo
   
5. IA responde con contexto completo de la conversación
```

**Ejemplo de estructura de mensajes enviados a la IA:**

```json
[
  {
    "role": "system",
    "content": "CONTEXTO DEL ESTUDIANTE:\nNombre: Juan Pérez\n..."
  },
  {
    "role": "user",
    "content": "Hola, ¿cómo puedo ayudar a este alumno?"
  },
  {
    "role": "assistant",
    "content": "Basándome en el perfil de Juan..."
  },
  {
    "role": "user",
    "content": "¿Qué estrategias recomiendas?"
  }
]
```

---

## 📝 Ejemplos de Uso

### **Ejemplo 1: Primera vez (crea nueva sesión)**

```http
POST /api/chat/sesiones
{
  "idDocente": 1,
  "idAlumno": 2,
  "tituloSesion": "Consulta sobre Juan"
}
```

**Respuesta:**
```json
{
  "idSesion": 10,
  "idDocente": 1,
  "idAlumno": 2,
  "idCurso": null,
  "tituloSesion": "Consulta sobre Juan",
  "fechaCreacion": "2024-11-05T10:00:00"
}
```

**Log:**
```
INFO: No se encontró sesión previa, creando nueva sesión individual para docente 1 y alumno 2
```

---

### **Ejemplo 2: Segunda vez (reutiliza sesión)**

```http
POST /api/chat/sesiones
{
  "idDocente": 1,
  "idAlumno": 2,
  "tituloSesion": "Seguimiento de Juan"
}
```

**Respuesta:**
```json
{
  "idSesion": 10,  // ← Mismo ID que antes
  "idDocente": 1,
  "idAlumno": 2,
  "idCurso": null,
  "tituloSesion": "Seguimiento de Juan",  // ← Título actualizado
  "fechaCreacion": "2024-11-05T10:00:00"  // ← Fecha original
}
```

**Log:**
```
INFO: Reutilizando sesión existente ID: 10 entre docente 1 y alumno 2
```

---

### **Ejemplo 3: Enviar mensaje en sesión reutilizada**

```http
POST /api/chat/sesiones/10/mensajes
{
  "contenido": "¿Qué más puedo hacer?"
}
```

**Flujo interno:**
1. Guarda mensaje del docente
2. Obtiene historial completo (incluye mensajes previos de la sesión 10)
3. Genera contexto actualizado del alumno
4. Construye mensajes para IA con historial completo
5. Envía a OpenRouter con contexto histórico
6. IA responde con conocimiento de conversación previa

**Log:**
```
INFO: Creando mensaje para sesión ID: 10
INFO: Historial de mensajes cargado: 4 mensajes previos
INFO: Generando contexto para alumno ID: 2
INFO: Enviando 6 mensajes a OpenRouter API  // ← Incluye historial
```

---

## 🔍 Casos Especiales

### **1. Múltiples sesiones previas**

Si existen múltiples sesiones entre el mismo docente y alumno, se reutiliza la **más reciente** (ordenada por `fechaCreacion DESC`).

```java
List<ChatSesion> sesionesExistentes = chatSesionRepository
    .findUltimaSesionActivaPorDocenteYAlumno(idDocente, idAlumno);

ChatSesion sesionExistente = sesionesExistentes.get(0); // ← Primera = más reciente
```

---

### **2. Chats grupales**

Los chats grupales **NO se reutilizan**. Siempre se crea una nueva sesión para cada chat grupal.

```java
if (esGrupal) {
    // Siempre crear nueva sesión grupal
    ChatSesion sesion = new ChatSesion();
    // ...
}
```

**Razón:** Los chats grupales pueden tener diferentes propósitos y contextos, por lo que cada uno es independiente.

---

### **3. Actualización de título**

Si se proporciona un nuevo título al reutilizar una sesión, se actualiza:

```java
if (request.getTituloSesion() != null && !request.getTituloSesion().trim().isEmpty()) {
    sesionExistente.setTituloSesion(request.getTituloSesion());
    chatSesionRepository.save(sesionExistente);
}
```

**Ejemplo:**
- Primera vez: `tituloSesion = "Consulta inicial"`
- Segunda vez: `tituloSesion = "Seguimiento"` → Se actualiza en BD

---

## 🧪 Testing

### **Test 1: Crear sesión nueva (primera vez)**

```java
CrearChatSesionRequest request = new CrearChatSesionRequest();
request.setIdDocente(1);
request.setIdAlumno(2);
request.setTituloSesion("Primera consulta");

ChatSesionDTO sesion = chatService.crearSesion(request);

// Verificar que es nueva
assertNotNull(sesion.getIdSesion());
// Verificar que no hay sesiones previas
List<ChatSesion> previas = chatSesionRepository
    .findUltimaSesionActivaPorDocenteYAlumno(1, 2);
assertEquals(1, previas.size()); // Solo la que acabamos de crear
```

### **Test 2: Reutilizar sesión existente**

```java
// Primera creación
ChatSesionDTO sesion1 = chatService.crearSesion(request1);

// Segunda creación (mismo docente y alumno)
ChatSesionDTO sesion2 = chatService.crearSesion(request2);

// Verificar que es la misma sesión
assertEquals(sesion1.getIdSesion(), sesion2.getIdSesion());
```

### **Test 3: Historial cargado correctamente**

```java
// Crear sesión
ChatSesionDTO sesion = chatService.crearSesion(request);

// Enviar mensaje 1
chatService.crearMensaje(sesion.getIdSesion(), mensaje1);

// Enviar mensaje 2
ChatMensajeDTO respuesta2 = chatService.crearMensaje(sesion.getIdSesion(), mensaje2);

// Verificar que el historial incluye mensaje 1
// (Se verifica en logs o interceptando llamadas a OpenRouter)
```

---

## 📊 Logs y Monitoreo

### **Logs Generados:**

```
INFO: No se encontró sesión previa, creando nueva sesión individual para docente 1 y alumno 2
INFO: Reutilizando sesión existente ID: 10 entre docente 1 y alumno 2
INFO: Creando mensaje para sesión ID: 10
INFO: Historial de mensajes cargado: 4 mensajes previos
INFO: Generando contexto para alumno ID: 2
INFO: Enviando 6 mensajes a OpenRouter API
```

---

## ✅ Checklist de Implementación

- [x] Método agregado en `ChatSesionRepository` para buscar última sesión activa
- [x] Lógica de reutilización implementada en `ChatService.crearSesion()`
- [x] Historial de mensajes cargado automáticamente
- [x] Contexto histórico incluido en mensajes para IA
- [x] Logging detallado para debugging
- [x] Actualización de título al reutilizar sesión
- [x] Comportamiento diferenciado para chats individuales vs grupales
- [x] Código comentado y documentado
- [x] Manejo de casos especiales (múltiples sesiones, títulos, etc.)

---

## 🎉 Resultado

La historia de usuario está **completamente implementada**. El sistema ahora:

✅ Busca automáticamente sesiones activas previas  
✅ Reutiliza sesiones existentes para chats individuales  
✅ Carga el historial completo de mensajes  
✅ Incluye el contexto histórico en las llamadas a la IA  
✅ Mantiene el comportamiento anterior si no hay sesión previa  
✅ Crea nuevas sesiones solo cuando es necesario  

---

**Última actualización:** 2024-11-05  
**Versión:** 1.0

