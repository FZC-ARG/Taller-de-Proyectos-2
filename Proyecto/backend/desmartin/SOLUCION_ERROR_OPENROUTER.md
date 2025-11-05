# 🔧 Solución de Error: OpenRouter Connection Failed

## 🔍 Análisis del Error

### **Error Reportado:**
```
No se pudo comunicar con OpenRouter después de 3 intentos
```

### **Posibles Causas:**

1. **Problemas de Conexión:**
   - Sin conexión a internet
   - Firewall bloqueando conexiones HTTPS
   - Proxy corporativo bloqueando la API

2. **API Key Inválida:**
   - API key incorrecta
   - API key expirada
   - API key sin permisos suficientes

3. **Problemas con la URL:**
   - URL incorrecta
   - Servidor de OpenRouter caído

4. **Problemas con el Formato del JSON:**
   - Estructura de mensajes incorrecta
   - Caracteres especiales no escapados

---

## ✅ Mejoras Implementadas

### **1. Logging Mejorado**

**Antes:**
```java
logger.warn("Intento {} fallido al comunicarse con OpenRouter: {}", intentos, e.getMessage());
```

**Después:**
```java
logger.warn("Intento {} fallido - Timeout al comunicarse con OpenRouter: {}", intentos, e.getMessage());
logger.warn("Intento {} fallido - Error de conexión con OpenRouter: {}", intentos, e.getMessage());
logger.warn("Intento {} fallido al comunicarse con OpenRouter: {} - Tipo: {}", 
    intentos, e.getMessage(), e.getClass().getSimpleName());
logger.debug("Stack trace completo:", e);
```

**Beneficios:**
- ✅ Identifica el tipo específico de error
- ✅ Logs más detallados para debugging
- ✅ Stack trace completo para análisis

### **2. Manejo de Errores Específicos**

**Errores Detectados:**
- ✅ `HttpTimeoutException` - Timeout de conexión
- ✅ `ConnectException` - Error de conexión
- ✅ `401 Unauthorized` - API key inválida
- ✅ `429 Too Many Requests` - Rate limit

**Manejo Específico:**
```java
if (statusCode == 401) {
    throw new RuntimeException("API key inválida o expirada. Verifica tu API key en application.properties");
}

if (statusCode == 429) {
    logger.warn("Rate limit alcanzado, esperando antes de reintentar...");
    Thread.sleep(5000); // Esperar 5 segundos adicionales
}
```

### **3. Validación de Configuración**

**Validación Agregada:**
```java
if (apiKey == null || apiKey.isEmpty()) {
    logger.error("API key de OpenRouter no está configurada");
    throw new RuntimeException("API key de OpenRouter no está configurada. Verifica application.properties");
}
```

**Logging de Configuración:**
```java
@PostConstruct
public void init() {
    logger.info("=== Configuración OpenRouter ===");
    logger.info("URL: {}", apiUrl);
    logger.info("Model: {}", model);
    logger.info("API Key configurada: {}", apiKey != null && !apiKey.isEmpty() ? "SÍ" : "NO");
    // ...
}
```

### **4. Mensajes de Error Mejorados**

**Para el Usuario:**
- ✅ Mensajes más amigables
- ✅ Sugerencias de solución
- ✅ Identificación del tipo de error

**Ejemplo:**
```
Lo siento, hubo un error al procesar tu solicitud. 
No se pudo conectar con el servicio de IA. Verifica tu conexión a internet.
```

---

## 🔍 Pasos para Diagnosticar

### **1. Verificar Logs del Servidor**

Al iniciar la aplicación, busca estos logs:
```
=== Configuración OpenRouter ===
URL: https://openrouter.ai/api/v1/chat/completions
Model: deepseek/deepseek-chat-v3.1:free
API Key configurada: SÍ
===============================
```

Si ves "API Key configurada: NO", verifica `application.properties`.

### **2. Verificar Logs al Enviar Mensaje**

Busca estos logs:
```
Enviando X mensajes a OpenRouter API
Intento 1 de 3 - Enviando petición a OpenRouter...
Respuesta de OpenRouter - Status: XXX
```

### **3. Verificar la Conexión**

**Probar manualmente con curl:**
```bash
curl -X POST https://openrouter.ai/api/v1/chat/completions \
  -H "Authorization: Bearer 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" \
  -H "Content-Type: application/json" \
  -d '{"model": "deepseek/deepseek-chat-v3.1:free", "messages": [{"role": "user", "content": "Hola"}]}'
```

Si esto funciona, el problema está en el código Java.
Si no funciona, el problema es la conexión o la API key.

---

## 🛠️ Soluciones Posibles

### **Solución 1: Verificar API Key**

1. Verifica que la API key en `application.properties` sea correcta
2. Verifica que la API key no haya expirado
3. Verifica que la API key tenga permisos para el modelo `deepseek/deepseek-chat-v3.1:free`

### **Solución 2: Verificar Conexión a Internet**

1. Verifica que tengas conexión a internet
2. Verifica que no haya firewall bloqueando HTTPS
3. Verifica que no haya proxy corporativo bloqueando la API

### **Solución 3: Verificar URL**

1. Verifica que la URL sea correcta: `https://openrouter.ai/api/v1/chat/completions`
2. Prueba acceder a la URL en el navegador

### **Solución 4: Verificar Formato del JSON**

Revisa los logs para ver el `Request body`:
```
Request body: {"model":"deepseek/deepseek-chat-v3.1:free","messages":[...]}
```

Verifica que el JSON sea válido.

---

## 📝 Cambios Realizados

1. ✅ **Logging mejorado** - Más detalles sobre errores
2. ✅ **Manejo de errores específicos** - Timeout, conexión, 401, 429
3. ✅ **Validación de configuración** - Verifica API key antes de usar
4. ✅ **Mensajes de error amigables** - Para el usuario final
5. ✅ **Logging de configuración** - Al iniciar el servicio

---

## 🎯 Próximos Pasos

1. **Reinicia el servidor** para ver los nuevos logs
2. **Intenta enviar un mensaje** y revisa los logs
3. **Verifica los logs** para identificar el tipo específico de error
4. **Sigue las soluciones** según el tipo de error encontrado

---

## ⚠️ Notas Importantes

1. **Los logs ahora son más detallados** - Revisa la consola del servidor
2. **El error se muestra en el chat** - Pero los detalles están en los logs
3. **Verifica la conexión a internet** - Es el problema más común
4. **Verifica la API key** - Debe estar correcta en `application.properties`

