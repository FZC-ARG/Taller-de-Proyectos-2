# 🔄 Implementación de Política de Retención de 30 Días

## ✅ Tarea Completada: 3.2.2

### 📋 Resumen

Se ha implementado la política de retención automática de 30 días para eliminar mensajes y sesiones de chat antiguos. El sistema ahora elimina automáticamente:

- **Mensajes de chat** (`chat_mensajes`) con más de 30 días
- **Sesiones de chat** (`chat_sesiones`) con más de 30 días

---

## 🔧 Archivos Modificados/Creados

### 1. **ChatMensajeRepository.java** (Modificado)
- ✅ Agregado método `deleteByFechaHoraEnvioBefore()` para eliminar mensajes antiguos

### 2. **ChatSesionRepository.java** (Modificado)
- ✅ Agregado método `deleteByFechaCreacionBefore()` para eliminar sesiones antiguas

### 3. **LimpiezaChatService.java** (Nuevo)
- ✅ Servicio de limpieza con tarea programada
- ✅ Ejecución automática diaria a las 2:00 AM
- ✅ Métodos manuales para testing

### 4. **DesmartinApplication.java** (Modificado)
- ✅ Agregada anotación `@EnableScheduling` para habilitar tareas programadas

### 5. **application.properties** (Modificado)
- ✅ Agregada configuración de retención: `chat.retencion.dias=30`
- ✅ Agregada configuración de cron: `chat.limpieza.cron=0 0 2 * * ?`

---

## 📝 Funcionalidades Implementadas

### **1. Limpieza Automática Programada**

El servicio se ejecuta automáticamente todos los días a las **2:00 AM** usando la expresión cron configurada.

**Configuración por defecto:**
```properties
chat.retencion.dias=30
chat.limpieza.cron=0 0 2 * * ?
```

**Cron expression explicada:**
- `0` - Segundo 0
- `0` - Minuto 0
- `2` - Hora 2 (2:00 AM)
- `*` - Cualquier día del mes
- `*` - Cualquier mes
- `?` - Cualquier día de la semana

### **2. Proceso de Limpieza**

El proceso de limpieza realiza lo siguiente:

1. **Calcula la fecha límite**: `LocalDateTime.now().minusDays(30)`
2. **Elimina mensajes antiguos**: Mensajes con `fecha_hora_envio` anterior a la fecha límite
3. **Elimina sesiones antiguas**: Sesiones con `fecha_creacion` anterior a la fecha límite
4. **Registra logs**: Información detallada de cuántos registros se eliminaron

### **3. Logs de Ejecución**

El servicio registra información detallada:

```
=== Iniciando limpieza automática de chat ===
Días de retención configurados: 30
Fecha límite de eliminación: 2024-10-06T02:00:00
Eliminando mensajes anteriores a: 2024-10-06T02:00:00
Se eliminaron 150 mensajes antiguos
Eliminando sesiones anteriores a: 2024-10-06T02:00:00
Se eliminaron 25 sesiones antiguas
=== Limpieza completada ===
Mensajes eliminados: 150
Sesiones eliminadas: 25
```

---

## 🎯 Uso

### **Ejecución Automática**

La limpieza se ejecuta automáticamente todos los días a las 2:00 AM. No requiere intervención manual.

### **Configuración Personalizada**

Puedes cambiar la hora de ejecución o los días de retención en `application.properties`:

```properties
# Cambiar a 60 días de retención
chat.retencion.dias=60

# Cambiar a las 3:00 AM
chat.limpieza.cron=0 0 3 * * ?

# Ejecutar cada 12 horas
chat.limpieza.cron=0 0 */12 * * ?
```

### **Ejecución Manual (Para Testing)**

Si necesitas ejecutar la limpieza manualmente para testing:

```java
@Autowired
private LimpiezaChatService limpiezaChatService;

// Ejecutar limpieza manual con 30 días
LimpiezaChatService.LimpiezaResultado resultado = 
    limpiezaChatService.ejecutarLimpiezaManual(30);

System.out.println("Mensajes eliminados: " + resultado.getMensajesEliminados());
System.out.println("Sesiones eliminadas: " + resultado.getSesionesEliminadas());
```

### **Ejecutar desde un Endpoint (Opcional)**

Si quieres permitir ejecución manual desde un endpoint (solo para administradores), puedes agregar:

```java
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private LimpiezaChatService limpiezaChatService;
    
    @PostMapping("/chat/limpiar")
    public ResponseEntity<?> ejecutarLimpiezaManual() {
        LimpiezaChatService.LimpiezaResultado resultado = 
            limpiezaChatService.ejecutarLimpiezaManual(30);
        
        return ResponseEntity.ok(Map.of(
            "mensajesEliminados", resultado.getMensajesEliminados(),
            "sesionesEliminadas", resultado.getSesionesEliminadas(),
            "fechaLimite", resultado.getFechaLimite().toString()
        ));
    }
}
```

---

## ⚠️ Consideraciones Importantes

### **1. Foreign Keys y CASCADE**

En la base de datos, la tabla `chat_mensajes` tiene una foreign key hacia `chat_sesiones`:

```sql
ALTER TABLE `chat_mensajes`
  ADD CONSTRAINT `chat_mensajes_ibfk_1` 
  FOREIGN KEY (`id_sesion_fk`) REFERENCES `chat_sesiones` (`id_sesion`);
```

**IMPORTANTE:** Si la foreign key tiene `ON DELETE CASCADE`, los mensajes se eliminarán automáticamente al eliminar una sesión. Si no tiene CASCADE, el orden de eliminación es importante:

1. Primero se eliminan los mensajes
2. Luego se eliminan las sesiones

Este orden ya está implementado en el servicio.

### **2. Verificar CASCADE en BD**

Para verificar si tienes CASCADE configurado, ejecuta:

```sql
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    REFERENCED_TABLE_NAME,
    DELETE_RULE
FROM 
    information_schema.REFERENTIAL_CONSTRAINTS
WHERE 
    CONSTRAINT_SCHEMA = 'prmartin'
    AND TABLE_NAME = 'chat_mensajes';
```

Si `DELETE_RULE` es `CASCADE`, los mensajes se eliminarán automáticamente. Si es `RESTRICT` o `NO ACTION`, necesitas eliminar mensajes primero (ya implementado).

### **3. Agregar CASCADE (Opcional pero Recomendado)**

Si quieres agregar CASCADE para simplificar, puedes ejecutar:

```sql
-- Eliminar constraint actual
ALTER TABLE `chat_mensajes` 
DROP FOREIGN KEY `chat_mensajes_ibfk_1`;

-- Agregar constraint con CASCADE
ALTER TABLE `chat_mensajes`
ADD CONSTRAINT `chat_mensajes_ibfk_1` 
FOREIGN KEY (`id_sesion_fk`) 
REFERENCES `chat_sesiones` (`id_sesion`)
ON DELETE CASCADE;
```

Con CASCADE, puedes eliminar solo las sesiones y los mensajes se eliminarán automáticamente.

---

## 🧪 Testing

### **1. Verificar que la Tarea Programada Funciona**

1. Inicia la aplicación
2. Espera a que se ejecute la tarea programada (o cambia el cron a un tiempo cercano para testing)
3. Revisa los logs para ver la ejecución

### **2. Testing Manual**

Puedes crear un endpoint temporal para probar:

```java
@GetMapping("/test/limpieza")
public ResponseEntity<?> testLimpieza() {
    LimpiezaChatService.LimpiezaResultado resultado = 
        limpiezaChatService.ejecutarLimpiezaManual(30);
    return ResponseEntity.ok(resultado);
}
```

### **3. Verificar Datos Antiguos**

Antes de ejecutar la limpieza, verifica qué datos se eliminarán:

```sql
-- Mensajes con más de 30 días
SELECT COUNT(*) as mensajes_antiguos
FROM chat_mensajes
WHERE fecha_hora_envio < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- Sesiones con más de 30 días
SELECT COUNT(*) as sesiones_antiguas
FROM chat_sesiones
WHERE fecha_creacion < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

---

## 📊 Monitoreo

### **Logs a Revisar**

El servicio genera logs detallados que puedes monitorear:

- ✅ Inicio de limpieza
- ✅ Días de retención configurados
- ✅ Fecha límite calculada
- ✅ Mensajes eliminados
- ✅ Sesiones eliminadas
- ✅ Errores (si ocurren)

### **Ejemplo de Logs Exitosos**

```
2024-11-05 02:00:00.123 INFO  LimpiezaChatService - === Iniciando limpieza automática de chat ===
2024-11-05 02:00:00.124 INFO  LimpiezaChatService - Días de retención configurados: 30
2024-11-05 02:00:00.125 INFO  LimpiezaChatService - Fecha límite de eliminación: 2024-10-06T02:00:00
2024-11-05 02:00:00.126 INFO  LimpiezaChatService - Eliminando mensajes anteriores a: 2024-10-06T02:00:00
2024-11-05 02:00:00.234 INFO  LimpiezaChatService - Se eliminaron 150 mensajes antiguos
2024-11-05 02:00:00.235 INFO  LimpiezaChatService - Eliminando sesiones anteriores a: 2024-10-06T02:00:00
2024-11-05 02:00:00.345 INFO  LimpiezaChatService - Se eliminaron 25 sesiones antiguas
2024-11-05 02:00:00.346 INFO  LimpiezaChatService - === Limpieza completada ===
2024-11-05 02:00:00.347 INFO  LimpiezaChatService - Mensajes eliminados: 150
2024-11-05 02:00:00.348 INFO  LimpiezaChatService - Sesiones eliminadas: 25
```

---

## ✅ Checklist de Implementación

- [x] Métodos agregados en `ChatMensajeRepository`
- [x] Métodos agregados en `ChatSesionRepository`
- [x] Servicio `LimpiezaChatService` creado
- [x] Tarea programada con `@Scheduled` implementada
- [x] `@EnableScheduling` agregado en aplicación principal
- [x] Configuración en `application.properties`
- [x] Logs detallados implementados
- [x] Manejo de errores implementado
- [x] Método manual para testing
- [x] Documentación completa

---

## 🎉 Resultado

La tarea **3.2.2 - Establecer política de retención de 30 días (borrado automático)** está ahora **COMPLETADA**.

El sistema ahora:
- ✅ Elimina automáticamente mensajes con más de 30 días
- ✅ Elimina automáticamente sesiones con más de 30 días
- ✅ Ejecuta la limpieza diariamente a las 2:00 AM
- ✅ Registra logs detallados de cada ejecución
- ✅ Permite configuración personalizada de días y horario
- ✅ Incluye métodos para ejecución manual

---

**Última actualización:** 2024-11-05  
**Versión:** 1.0

