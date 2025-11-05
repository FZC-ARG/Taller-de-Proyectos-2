# 🏗️ Plan Arquitectónico: Integración IA Conversacional con DeepSeek

## 📋 Análisis del Estado Actual

### ✅ Componentes Existentes
- **BD**: Tablas `chat_sesiones` y `chat_mensajes` ya creadas
- **Modelos**: `ChatSesion`, `ChatMensaje` implementados
- **Repositorios**: `ChatSesionRepository`, `ChatMensajeRepository` funcionales
- **Servicio**: `ChatService` básico (simula respuestas de IA)
- **Controlador**: `ChatController` con endpoints CRUD
- **DTOs**: `ChatSesionDTO`, `ChatMensajeDTO`, `CrearChatSesionRequest`, `CrearMensajeRequest`
- **OpenRouter**: Clase de prueba `OpenRouterService.java` (no integrada)

### ⚠️ Problemas Detectados
1. **Chats por curso**: No existe soporte para chats grupales por curso
2. **Integración IA**: Respuestas simuladas, no hay llamada real a OpenRouter
3. **Contexto dinámico**: No se envían datos del alumno a la IA
4. **Seguridad**: API key hardcodeada en código (riesgo de seguridad)
5. **Estructura**: `OpenRouterService` no es un servicio Spring Bean
6. **Manejo de errores**: No hay manejo robusto de errores de API externa

### 📊 Requisitos Funcionales
1. ✅ Chats únicos por alumno (ya existe)
2. ❌ Chats por curso (falta implementar)
3. ✅ Historial de conversación (ya existe)
4. ✅ Retomar sesiones (ya existe)
5. ✅ Borrar/listar conversaciones (ya existe)
6. ❌ Integración real con DeepSeek (falta)
7. ❌ Contexto personalizado por alumno (falta)

---

## 🎯 Plan de Desarrollo por Etapas

### **ETAPA 1: Validar y Optimizar Estructura de BD** ✅
**Objetivo**: Asegurar que la BD soporta todos los casos de uso

**Tareas**:
- [x] Validar estructura actual de `chat_sesiones`
- [ ] Agregar campo `id_curso_fk` para soportar chats grupales
- [ ] Agregar índices para optimizar consultas
- [ ] Validar constraints y relaciones
- [ ] Documentar cambios necesarios

**Entregables**:
- Script SQL de migración (si es necesario)
- Documentación de estructura optimizada

---

### **ETAPA 2: Actualizar Modelos y Repositorios** ✅
**Objetivo**: Reflejar cambios de BD en las entidades JPA

**Tareas**:
- [ ] Actualizar `ChatSesion` para soportar curso (opcional)
- [ ] Agregar métodos de repositorio para consultas por curso
- [ ] Validar relaciones JPA

**Entregables**:
- Modelos actualizados
- Repositorios extendidos

---

### **ETAPA 3: Crear Servicio de IA (OpenRouterClient)** ✅
**Objetivo**: Cliente HTTP robusto para comunicarse con OpenRouter API

**Tareas**:
- [ ] Crear `OpenRouterService` como @Service Spring
- [ ] Mover API key a `application.properties`
- [ ] Implementar método para llamar a DeepSeek
- [ ] Manejo de errores y retry logic
- [ ] Logging de llamadas

**Entregables**:
- `OpenRouterService.java` completo
- Configuración en `application.properties`

---

### **ETAPA 4: Servicio de Contexto Dinámico** ✅
**Objetivo**: Generar contexto personalizado basado en datos del alumno

**Tareas**:
- [ ] Crear `ContextoIAService` para generar prompts
- [ ] Integrar con `TestService` para obtener resultados
- [ ] Formatear contexto para alumno individual
- [ ] Formatear contexto para grupo/curso
- [ ] Incluir datos relevantes (inteligencias, puntajes, nombre, etc.)

**Entregables**:
- `ContextoIAService.java`
- Métodos de generación de contexto

---

### **ETAPA 5: Actualizar ChatService** ✅
**Objetivo**: Integrar IA real y contexto dinámico

**Tareas**:
- [ ] Inyectar `OpenRouterService` en `ChatService`
- [ ] Inyectar `ContextoIAService` en `ChatService`
- [ ] Reemplazar simulación por llamada real a IA
- [ ] Construir historial de mensajes para contexto
- [ ] Agregar contexto según tipo de sesión (alumno/curso)

**Entregables**:
- `ChatService` actualizado con IA real

---

### **ETAPA 6: Extender ChatController** ✅
**Objetivo**: Agregar endpoints para chats por curso

**Tareas**:
- [ ] Endpoint: Crear sesión por curso
- [ ] Endpoint: Listar sesiones por curso
- [ ] Actualizar DTOs si es necesario
- [ ] Validar autorización (solo docente del curso puede crear sesión)

**Entregables**:
- Controlador extendido
- Nuevos endpoints documentados

---

### **ETAPA 7: Mejoras de Seguridad** ✅
**Objetivo**: Proteger API key y validar accesos

**Tareas**:
- [ ] Validar que solo docentes pueden crear sesiones
- [ ] Validar que docente solo accede a sus propios chats
- [ ] Validar que docente solo accede a chats de sus cursos
- [ ] Rate limiting básico (opcional)

**Entregables**:
- Validaciones de seguridad implementadas

---

### **ETAPA 8: Testing y Optimización** ✅
**Objetivo**: Verificar funcionamiento completo

**Tareas**:
- [ ] Probar creación de chat por alumno
- [ ] Probar creación de chat por curso
- [ ] Probar respuestas con contexto personalizado
- [ ] Probar historial y recuperación de sesiones
- [ ] Optimizar consultas a BD si es necesario

**Entregables**:
- Sistema funcional y probado
- Documentación de uso

---

## 🔧 Detalles Técnicos

### **Estructura de Chat por Curso**
```sql
-- Agregar a chat_sesiones:
id_curso_fk INT NULL, -- NULL = chat individual, valor = chat grupal
FOREIGN KEY (id_curso_fk) REFERENCES cursos(id_curso)
```

### **Flujo de Contexto Dinámico**
1. Docente envía mensaje
2. Sistema identifica tipo de sesión (alumno/curso)
3. Si es alumno: obtiene resultados del test
4. Si es curso: obtiene perfil grupal (promedios, etc.)
5. Construye prompt con contexto
6. Envía a OpenRouter con historial
7. Guarda respuesta de IA

### **Configuración de Seguridad**
```properties
# application.properties
openrouter.api.key=${OPENROUTER_API_KEY}
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.model=deepseek/deepseek-chat-v3.1:free
```

---

## 📝 Notas de Implementación

- **Prioridad**: Seguridad > Funcionalidad > Performance
- **Principio**: Separación de responsabilidades (SRP)
- **Patrón**: Service Layer para lógica de negocio
- **Manejo de errores**: Usar excepciones personalizadas
- **Logging**: Usar SLF4J con niveles apropiados

---

## ✅ Checklist de Validación

Antes de pasar a la siguiente etapa:
- [ ] Código compila sin errores
- [ ] No hay warnings críticos
- [ ] Estructura sigue convenciones del proyecto
- [ ] Integración con componentes existentes funciona
- [ ] Documentación actualizada

