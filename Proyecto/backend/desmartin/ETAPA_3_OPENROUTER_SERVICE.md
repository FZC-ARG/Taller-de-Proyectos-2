# 🤖 ETAPA 3: Creación del Servicio de IA (OpenRouterClient)

## ✅ Resumen de Cambios Realizados

### **1. Configuración en `application.properties`**

**Configuración Agregada:**
```properties
# --- Configuración de OpenRouter API ---
openrouter.api.url=https://openrouter.ai/api/v1/chat/completions
openrouter.api.key=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
openrouter.api.model=deepseek/deepseek-chat-v3.1:free
openrouter.api.http-referer=https://desmartin.app
openrouter.api.x-title=Desmartin
```

**Beneficios:**
- ✅ API key fuera del código fuente (más seguro)
- ✅ Configuración centralizada
- ✅ Fácil de cambiar sin recompilar
- ✅ Puede usar variables de entorno en producción

---

### **2. Servicio `OpenRouterService`**

#### **Características Principales:**

**✅ Anotación `@Service`**
- Integrado con Spring Boot
- Inyección de dependencias automática
- Disponible para ser usado en otros servicios

**✅ Configuración desde `application.properties`**
- Todas las propiedades se cargan automáticamente
- Usa `@Value` para inyección de propiedades

**✅ Cliente HTTP Robusto**
- Usa `java.net.http.HttpClient` (Java 11+)
- Timeout configurable (30 segundos)
- Manejo de errores robusto

**✅ Retry Logic (Exponential Backoff)**
- Hasta 3 intentos automáticos
- Espera exponencial: 2s, 4s, 8s
- Logging detallado de cada intento

**✅ Logging con SLF4J**
- Logs informativos, de advertencia y errores
- Facilita debugging y monitoreo

**✅ Manejo de Respuestas JSON**
- Usa Jackson ObjectMapper
- Extrae contenido de la respuesta de OpenRouter
- Validación de estructura de respuesta

---

### **3. Métodos Implementados**

#### **`enviarMensaje(List<Map<String, String>> mensajes)`**
- **Propósito**: Envía múltiples mensajes (historial de conversación)
- **Parámetros**: Lista de mensajes con `role` y `content`
- **Retorna**: Respuesta de la IA como String
- **Características**:
  - Validación de entrada
  - Retry logic automático
  - Manejo de errores robusto

#### **`enviarMensajeSimple(String mensajeUsuario)`**
- **Propósito**: Envía un mensaje simple sin historial
- **Parámetros**: Mensaje del usuario como String
- **Retorna**: Respuesta de la IA como String
- **Uso**: Para pruebas o mensajes simples

#### **`verificarConectividad()`**
- **Propósito**: Verifica que la API de OpenRouter esté disponible
- **Retorna**: `true` si está disponible, `false` en caso contrario
- **Uso**: Para health checks o inicialización

---

### **4. Estructura de Mensajes**

**Formato de Mensajes:**
```java
List<Map<String, String>> mensajes = new ArrayList<>();
mensajes.add(Map.of("role", "user", "content", "Mensaje del usuario"));
mensajes.add(Map.of("role", "assistant", "content", "Respuesta anterior de la IA"));
mensajes.add(Map.of("role", "user", "content", "Nuevo mensaje del usuario"));
```

**Roles Soportados:**
- `user`: Mensaje del docente
- `assistant`: Respuesta anterior de la IA
- `system`: Mensaje del sistema (opcional, para contexto inicial)

---

### **5. Manejo de Errores**

#### **Tipos de Errores Manejados:**

1. **Errores de Validación:**
   - Lista de mensajes vacía
   - Mensaje nulo

2. **Errores de HTTP:**
   - Status code diferente de 200
   - Timeout de conexión
   - Errores de red

3. **Errores de Respuesta:**
   - Respuesta sin `choices`
   - Respuesta sin `message`
   - Respuesta sin `content`

4. **Errores de Retry:**
   - Todos los intentos fallaron
   - Interrupción durante el retry

#### **Logging:**
- ✅ Logs informativos para operaciones exitosas
- ⚠️ Logs de advertencia para reintentos
- ❌ Logs de error para fallos críticos
- 🔍 Logs de debug para detalles de respuestas

---

### **6. Estructura de Respuesta de OpenRouter**

**Formato de Respuesta:**
```json
{
  "choices": [
    {
      "message": {
        "content": "Respuesta de la IA..."
      }
    }
  ]
}
```

**Procesamiento:**
1. Parsear JSON con Jackson
2. Extraer `choices[0].message.content`
3. Validar que existe cada nivel
4. Retornar contenido como String

---

### **7. Seguridad**

#### **Mejoras de Seguridad Implementadas:**

1. ✅ **API Key en Properties:**
   - No hardcodeada en el código
   - Fácil de cambiar sin recompilar
   - Puede usar variables de entorno en producción

2. ✅ **Headers de Seguridad:**
   - `HTTP-Referer`: Identifica el origen de la petición
   - `X-Title`: Identifica la aplicación

3. ✅ **Timeout:**
   - Limita el tiempo de espera
   - Previene bloqueos indefinidos

4. ✅ **Validación de Entrada:**
   - Valida que los mensajes no estén vacíos
   - Previene peticiones inválidas

---

### **8. Performance**

#### **Optimizaciones Implementadas:**

1. ✅ **HttpClient Reutilizable:**
   - Se crea una sola vez
   - Reutiliza conexiones HTTP

2. ✅ **Timeout Configurado:**
   - 30 segundos por defecto
   - Previene esperas indefinidas

3. ✅ **Retry Inteligente:**
   - Exponential backoff
   - No bloquea innecesariamente
   - Se detiene después de 3 intentos

---

## 📊 Flujo de Ejecución

### **Flujo Normal:**
```
1. Servicio recibe lista de mensajes
2. Construye JSON de petición
3. Crea HttpRequest con headers
4. Envía petición a OpenRouter
5. Espera respuesta (timeout 30s)
6. Valida respuesta (status 200)
7. Extrae contenido de JSON
8. Retorna respuesta como String
```

### **Flujo con Retry:**
```
1. Intento 1 falla → Espera 2s → Intento 2
2. Intento 2 falla → Espera 4s → Intento 3
3. Intento 3 falla → Lanza excepción
```

---

## 🔧 Integración con el Proyecto

### **Dependencias Usadas:**
- ✅ `java.net.http.HttpClient` (Java 11+)
- ✅ `com.fasterxml.jackson.databind.ObjectMapper` (Spring Boot Web)
- ✅ `org.slf4j.Logger` (Spring Boot Starter)

**No requiere dependencias adicionales** - Todo está incluido en Spring Boot.

---

### **Uso en Otros Servicios:**

```java
@Service
public class ChatService {
    
    @Autowired
    private OpenRouterService openRouterService;
    
    public void enviarMensaje() {
        List<Map<String, String>> mensajes = new ArrayList<>();
        mensajes.add(Map.of("role", "user", "content", "¿Cómo interpreto los resultados?"));
        
        String respuesta = openRouterService.enviarMensaje(mensajes);
        // Usar respuesta...
    }
}
```

---

## ✅ Validación de Compilación

**Resultado:**
- ✅ Sin errores de compilación
- ✅ Sin warnings críticos
- ✅ Todas las dependencias disponibles
- ✅ Integrado con Spring Boot

---

## 📝 Archivos Modificados/Creados

1. ✅ **`src/main/resources/application.properties`**
   - Agregada configuración de OpenRouter

2. ✅ **`src/main/java/com/appmartin/desmartin/service/OpenRouterService.java`**
   - Servicio completo creado

3. ✅ **`src/main/java/com/appmartin/desmartin/OpenRouterService.java`**
   - Archivo antiguo eliminado (reemplazado por el nuevo)

---

## 🎯 Próximos Pasos

**ETAPA 3 Completada:**
- ✅ Servicio de OpenRouter creado
- ✅ API key movida a properties
- ✅ Cliente HTTP robusto implementado
- ✅ Manejo de errores y retry logic
- ✅ Logging implementado

**Siguiente Etapa:**
- ➡️ **ETAPA 4**: Servicio de Contexto Dinámico
  - Crear `ContextoIAService` para generar prompts
  - Integrar con `TestService` para obtener resultados
  - Formatear contexto para alumno individual
  - Formatear contexto para grupo/curso

---

## ⚠️ Notas Importantes

1. **API Key**: La API key está en `application.properties`. En producción, considera usar variables de entorno:
   ```properties
   openrouter.api.key=${OPENROUTER_API_KEY}
   ```

2. **Rate Limiting**: OpenRouter tiene límites de rate. Si necesitas más, considera:
   - Implementar cola de mensajes
   - Caché de respuestas frecuentes
   - Throttling de peticiones

3. **Costos**: El modelo `deepseek/deepseek-chat-v3.1:free` es gratuito, pero otros modelos pueden tener costos.

4. **Monitoreo**: Los logs te permitirán monitorear:
   - Número de peticiones
   - Tiempo de respuesta
   - Errores y reintentos

5. **Testing**: Puedes probar el servicio con:
   ```java
   @Autowired
   private OpenRouterService openRouterService;
   
   @Test
   public void testOpenRouter() {
       String respuesta = openRouterService.enviarMensajeSimple("Hola");
       assertNotNull(respuesta);
   }
   ```

