# Manual de Mantenimiento — Desmartin

**Fecha:** 2025-01-11  
**Autor:** IA Asistente  
**Versión:** 1.0

---

## 1. Introducción

### 1.1 Propósito del Sistema

**Desmartin** es un sistema Full-Stack de evaluación de inteligencias múltiples desarrollado como POC (Proof of Concept) que permite:

- Gestión de usuarios (Administradores, Docentes, Alumnos)
- Evaluación de inteligencias múltiples mediante tests estructurados
- Chat con IA integrado mediante OpenRouter API para asistencia pedagógica personalizada
- Gestión de cursos y matrículas
- Sistema de auditoría y logging de accesos
- Retención automática de datos de chat (30 días)

### 1.2 Arquitectura Detectada

El sistema está construido con una arquitectura de tres capas:

**Backend:**
- **Framework:** Spring Boot 3.3.5
- **Lenguaje:** Java 21
- **ORM:** JPA/Hibernate
- **Build Tool:** Maven
- **Base de Datos:** MySQL/MariaDB (base de datos: `prmartin`)
- **Puerto:** 8081
- **Seguridad:** Spring Security (BCrypt para hash de contraseñas)

**Frontend:**
- **Framework:** Angular 19.2.0
- **Lenguaje:** TypeScript 5.7.2
- **Build Tool:** Angular CLI
- **Dependencias principales:** RxJS, Chart.js, SweetAlert2, Marked

**Servicios Externos:**
- **OpenRouter API:** Integración con modelo DeepSeek Chat v3.1 para chat con IA
- **URL:** https://openrouter.ai/api/v1/chat/completions

### 1.3 Objetivo del Manual

Este manual proporciona procedimientos técnicos detallados para el mantenimiento preventivo y correctivo del sistema Desmartin, asegurando su disponibilidad, integridad y seguridad continuas.

---

## 2. Objetivos del Mantenimiento

### 2.1 Mantenimiento Preventivo

El mantenimiento preventivo tiene como objetivo:

- **Prevenir fallos:** Identificar y resolver problemas antes de que afecten la producción
- **Optimizar rendimiento:** Mantener el sistema operando eficientemente
- **Asegurar disponibilidad:** Garantizar uptime del 99%+
- **Proteger datos:** Mantener integridad y seguridad de la información

### 2.2 Mantenimiento Correctivo

El mantenimiento correctivo se enfoca en:

- **Resolución rápida de errores:** Minimizar tiempo de inactividad
- **Diagnóstico preciso:** Identificar causas raíz de problemas
- **Recuperación de datos:** Restaurar información en caso de pérdida
- **Documentación de incidentes:** Registrar soluciones para referencia futura

### 2.3 Integridad, Seguridad y Disponibilidad

**Integridad:**
- Validación de integridad referencial en base de datos
- Verificación de backups completos y consistentes
- Monitoreo de transacciones y logs

**Seguridad:**
- Actualización de dependencias con vulnerabilidades conocidas
- Rotación de API keys y credenciales
- Auditoría de accesos y acciones de usuarios
- Revisión de políticas de retención de datos

**Disponibilidad:**
- Monitoreo de servicios críticos (MySQL, Spring Boot, OpenRouter API)
- Verificación de espacio en disco
- Optimización de consultas y índices
- Gestión de recursos del servidor

---

## 3. Mantenimiento Preventivo

### 3.1 Tareas Semanales

#### 3.1.1 Verificación de Logs del Sistema

**Objetivo:** Detectar errores, advertencias y patrones anómalos.

**Procedimiento:**

1. **Revisar logs de Spring Boot:**
   ```bash
   # En Windows PowerShell
   Get-Content logs/spring.log -Tail 100 | Select-String -Pattern "ERROR|WARN"
   
   # En Linux/Mac
   tail -n 100 logs/spring.log | grep -E "ERROR|WARN"
   ```

2. **Revisar logs de auditoría en base de datos:**
   ```sql
   USE prmartin;
   
   -- Ver últimos 50 logs de error
   SELECT * FROM log_accesos 
   WHERE nivel = 'ERROR' 
   ORDER BY fecha_hora DESC 
   LIMIT 50;
   
   -- Ver intentos de login fallidos
   SELECT * FROM log_accesos 
   WHERE accion LIKE '%LOGIN_FAIL%' 
   ORDER BY fecha_hora DESC 
   LIMIT 50;
   ```

3. **Verificar logs de OpenRouter API:**
   ```bash
   # Buscar errores de comunicación con OpenRouter
   grep -i "openrouter\|api key\|timeout" logs/spring.log | tail -20
   ```

**Acciones si se detectan problemas:**
- Errores de conexión a MySQL: Verificar servicio MySQL y credenciales
- Errores de OpenRouter: Verificar API key y conectividad
- Errores de memoria: Revisar configuración de JVM

#### 3.1.2 Verificación de Espacio en Disco

**Objetivo:** Prevenir fallos por falta de espacio.

**Procedimiento:**

```bash
# Windows PowerShell
Get-PSDrive C | Select-Object Used,Free

# Linux/Mac
df -h

# Verificar tamaño de base de datos MySQL
mysql -u root -p -e "SELECT table_schema AS 'Database', 
ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)' 
FROM information_schema.TABLES 
WHERE table_schema = 'prmartin' 
GROUP BY table_schema;"
```

**Umbral de alerta:** Si el espacio libre es < 20%, ejecutar limpieza.

#### 3.1.3 Verificación de Servicios Críticos

**Objetivo:** Asegurar que todos los servicios estén operativos.

**Procedimiento:**

1. **Verificar MySQL:**
   ```bash
   mysqladmin -u root -p ping
   ```

2. **Verificar Spring Boot:**
   ```bash
   curl http://localhost:8081/api/admin/docentes
   # Debe retornar JSON o lista vacía []
   ```

3. **Verificar OpenRouter API:**
   ```bash
   curl -X POST https://openrouter.ai/api/v1/chat/completions \
     -H "Authorization: Bearer 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" \
     -H "Content-Type: application/json" \
     -d '{"model":"deepseek/deepseek-chat-v3.1:free","messages":[{"role":"user","content":"test"}]}'
   ```

#### 3.1.4 Revisión de Métricas de Rendimiento

**Objetivo:** Identificar degradación de rendimiento.

**Procedimiento:**

```sql
-- Verificar consultas lentas (si está habilitado slow query log)
SHOW VARIABLES LIKE 'slow_query_log';

-- Verificar conexiones activas
SHOW STATUS LIKE 'Threads_connected';

-- Verificar consultas en ejecución
SHOW PROCESSLIST;
```

**Acciones:**
- Si hay consultas bloqueadas > 30 segundos: Investigar y optimizar
- Si conexiones activas > 80% del máximo: Revisar pool de conexiones

### 3.2 Tareas Mensuales

#### 3.2.1 Actualización de Dependencias

**Objetivo:** Mantener seguridad y rendimiento actualizados.

**Backend (Maven):**

```bash
cd backend/desmartin

# Verificar dependencias desactualizadas
mvn versions:display-dependency-updates

# Verificar plugins desactualizados
mvn versions:display-plugin-updates

# Actualizar dependencias (revisar cambios antes de aplicar)
mvn versions:use-latest-versions
```

**Frontend (npm):**

```bash
cd Frontend

# Verificar paquetes desactualizados
npm outdated

# Actualizar dependencias menores y parches (revisar cambios)
npm update

# Para actualizaciones mayores, revisar changelog primero
npm install package@latest
```

**Nota:** Siempre probar en ambiente de desarrollo antes de aplicar en producción.

#### 3.2.2 Optimización de Base de Datos

**Objetivo:** Mantener rendimiento óptimo de consultas.

**Procedimiento:**

```sql
USE prmartin;

-- Analizar tablas para optimización
ANALYZE TABLE administradores;
ANALYZE TABLE docentes;
ANALYZE TABLE alumnos;
ANALYZE TABLE chat_sesiones;
ANALYZE TABLE chat_mensajes;
ANALYZE TABLE log_accesos;
ANALYZE TABLE resultados_test;

-- Optimizar tablas fragmentadas
OPTIMIZE TABLE chat_mensajes;
OPTIMIZE TABLE chat_sesiones;
OPTIMIZE TABLE log_accesos;

-- Verificar índices existentes
SHOW INDEX FROM chat_sesiones;
SHOW INDEX FROM chat_mensajes;
SHOW INDEX FROM log_accesos;
```

**Verificar índices críticos:**
- `chat_sesiones`: `idx_docente`, `idx_alumno`, `idx_curso`
- `chat_mensajes`: `idx_sesion`, `idx_fecha_envio`
- `log_accesos`: Índice en `fecha_hora` (si existe)

#### 3.2.3 Limpieza Manual de Datos Antiguos

**Objetivo:** Liberar espacio y mantener rendimiento.

**Nota:** El sistema tiene limpieza automática configurada a las 2:00 AM diariamente, pero se puede ejecutar manualmente si es necesario.

**Procedimiento:**

```sql
-- Verificar datos antiguos antes de eliminar
SELECT COUNT(*) as mensajes_antiguos 
FROM chat_mensajes 
WHERE fecha_hora_envio < DATE_SUB(NOW(), INTERVAL 30 DAY);

SELECT COUNT(*) as sesiones_antiguas 
FROM chat_sesiones 
WHERE fecha_creacion < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- Verificar logs antiguos (opcional, según política de retención)
SELECT COUNT(*) as logs_antiguos 
FROM log_accesos 
WHERE fecha_hora < DATE_SUB(NOW(), INTERVAL 90 DAY);
```

**Ejecutar limpieza manual desde código Java (si está disponible):**
```java
// A través de endpoint o servicio administrativo
// LimpiezaChatService.ejecutarLimpiezaManual(30)
```

#### 3.2.4 Revisión de Configuración de Seguridad

**Objetivo:** Asegurar que las configuraciones de seguridad estén actualizadas.

**Verificaciones:**

1. **Revisar application.properties:**
   ```bash
   # Verificar que no haya contraseñas en texto plano
   grep -i "password" backend/desmartin/src/main/resources/application.properties
   
   # Verificar configuración de API key de OpenRouter
   grep "openrouter.api.key" backend/desmartin/src/main/resources/application.properties
   ```

2. **Verificar políticas de retención:**
   ```properties
   # En application.properties debe estar configurado:
   chat.retencion.dias=30
   chat.limpieza.cron=0 0 2 * * ?
   ```

3. **Revisar logs de acceso sospechosos:**
   ```sql
   -- Intentos de acceso fallidos repetidos
   SELECT id_usuario, tipo_usuario, COUNT(*) as intentos_fallidos
   FROM log_accesos
   WHERE accion LIKE '%LOGIN_FAIL%'
   AND fecha_hora > DATE_SUB(NOW(), INTERVAL 1 DAY)
   GROUP BY id_usuario, tipo_usuario
   HAVING intentos_fallidos > 5;
   ```

#### 3.2.5 Verificación de Integridad de Base de Datos

**Objetivo:** Detectar y corregir inconsistencias.

**Procedimiento:**

```sql
USE prmartin;

-- Verificar integridad referencial
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE
FROM information_schema.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'prmartin'
AND CONSTRAINT_TYPE = 'FOREIGN KEY';

-- Verificar registros huérfanos (ejemplo: mensajes sin sesión)
SELECT COUNT(*) as mensajes_huerfanos
FROM chat_mensajes cm
LEFT JOIN chat_sesiones cs ON cm.id_sesion_fk = cs.id_sesion
WHERE cs.id_sesion IS NULL;

-- Verificar sesiones sin docente válido
SELECT COUNT(*) as sesiones_sin_docente
FROM chat_sesiones cs
LEFT JOIN docentes d ON cs.id_docente_fk = d.id_docente
WHERE d.id_docente IS NULL;
```

**Acciones si se detectan problemas:**
- Registros huérfanos: Ejecutar scripts de limpieza o corrección
- Foreign keys rotas: Revisar y corregir datos manualmente

### 3.3 Tareas Trimestrales

#### 3.3.1 Auditoría Completa del Sistema

**Objetivo:** Evaluación exhaustiva del estado del sistema.

**Procedimiento:**

1. **Revisar estadísticas de uso:**
   ```sql
   -- Usuarios activos en últimos 90 días
   SELECT tipo_usuario, COUNT(DISTINCT id_usuario) as usuarios_activos
   FROM log_accesos
   WHERE fecha_hora > DATE_SUB(NOW(), INTERVAL 90 DAY)
   GROUP BY tipo_usuario;
   
   -- Tests completados
   SELECT COUNT(*) as total_tests
   FROM intentos_test
   WHERE fecha_realizacion > DATE_SUB(NOW(), INTERVAL 90 DAY);
   
   -- Sesiones de chat creadas
   SELECT COUNT(*) as total_sesiones
   FROM chat_sesiones
   WHERE fecha_creacion > DATE_SUB(NOW(), INTERVAL 90 DAY);
   ```

2. **Revisar rendimiento de endpoints:**
   ```sql
   -- Endpoints más lentos (si está configurado logging de duración)
   SELECT endpoint, AVG(duracion_ms) as tiempo_promedio_ms, COUNT(*) as llamadas
   FROM log_accesos
   WHERE fecha_hora > DATE_SUB(NOW(), INTERVAL 90 DAY)
   AND duracion_ms IS NOT NULL
   GROUP BY endpoint
   ORDER BY tiempo_promedio_ms DESC
   LIMIT 10;
   ```

3. **Revisar errores más frecuentes:**
   ```sql
   -- Top 10 errores
   SELECT descripcion, COUNT(*) as frecuencia
   FROM log_accesos
   WHERE nivel = 'ERROR'
   AND fecha_hora > DATE_SUB(NOW(), INTERVAL 90 DAY)
   GROUP BY descripcion
   ORDER BY frecuencia DESC
   LIMIT 10;
   ```

#### 3.3.2 Revisión de Políticas de Retención

**Objetivo:** Asegurar que las políticas de retención sean adecuadas.

**Verificaciones:**

1. **Revisar política de retención de chat (30 días):**
   ```sql
   -- Verificar que la limpieza automática está funcionando
   SELECT 
       DATE(fecha_creacion) as fecha,
       COUNT(*) as sesiones_creadas
   FROM chat_sesiones
   WHERE fecha_creacion > DATE_SUB(NOW(), INTERVAL 60 DAY)
   GROUP BY DATE(fecha_creacion)
   ORDER BY fecha DESC;
   ```

2. **Evaluar necesidad de ajustar políticas:**
   - Si el espacio en disco es crítico: Reducir días de retención
   - Si hay requerimientos legales: Aumentar días de retención
   - Revisar con stakeholders antes de cambiar políticas

#### 3.3.3 Actualización Mayor de Dependencias

**Objetivo:** Mantener el sistema actualizado con versiones estables más recientes.

**Procedimiento:**

1. **Backend - Spring Boot:**
   ```bash
   # Revisar changelog de Spring Boot
   # Actualizar pom.xml con nueva versión
   # Probar exhaustivamente en desarrollo
   ```

2. **Frontend - Angular:**
   ```bash
   cd Frontend
   
   # Revisar guía de migración de Angular
   ng update @angular/core @angular/cli
   
   # Actualizar dependencias relacionadas
   npm install
   ```

3. **Base de Datos - MySQL:**
   ```bash
   # Verificar versión actual
   mysql --version
   
   # Revisar changelog de MySQL/MariaDB
   # Planificar actualización en ventana de mantenimiento
   ```

**Importante:** Siempre probar en ambiente de desarrollo y tener plan de rollback.

#### 3.3.4 Revisión de Configuración de JVM

**Objetivo:** Optimizar uso de memoria y rendimiento.

**Verificaciones:**

```bash
# Ver configuración actual de JVM
jps -v

# Revisar uso de memoria
jstat -gc <PID> 1000 10

# Verificar configuración en aplicación
# Revisar si hay parámetros JVM configurados en:
# - Scripts de inicio
# - application.properties (si aplica)
# - Variables de entorno
```

**Recomendaciones típicas para Spring Boot:**
```bash
# Ejemplo de configuración JVM
java -Xms512m -Xmx1024m -XX:+UseG1GC -jar desmartin-0.0.1-SNAPSHOT.jar
```

#### 3.3.5 Revisión de Documentación

**Objetivo:** Mantener documentación actualizada.

**Verificaciones:**

1. Revisar que `README.md` refleje el estado actual del sistema
2. Verificar que `ENDPOINTS.md` esté actualizado con todos los endpoints
3. Revisar que este manual de mantenimiento esté actualizado
4. Documentar cambios significativos realizados en el trimestre

---

## 4. Mantenimiento Correctivo

### 4.1 Errores Comunes y Soluciones

#### 4.1.1 Error: "Cannot connect to MySQL"

**Síntomas:**
- La aplicación no inicia
- Errores en logs: `Communications link failure`
- Endpoints retornan error 500

**Diagnóstico:**

```bash
# Verificar que MySQL esté ejecutándose
mysqladmin -u root -p ping

# Verificar credenciales en application.properties
cat backend/desmartin/src/main/resources/application.properties | grep datasource

# Verificar que la base de datos exista
mysql -u root -p -e "SHOW DATABASES LIKE 'prmartin';"
```

**Solución:**

1. **Si MySQL no está ejecutándose:**
   ```bash
   # Windows (servicio)
   net start MySQL80
   
   # Linux
   sudo systemctl start mysql
   # o
   sudo service mysql start
   ```

2. **Si las credenciales son incorrectas:**
   - Editar `backend/desmartin/src/main/resources/application.properties`
   - Actualizar `spring.datasource.username` y `spring.datasource.password`
   - Reiniciar aplicación

3. **Si la base de datos no existe:**
   ```sql
   CREATE DATABASE prmartin;
   USE prmartin;
   -- Ejecutar script de inicialización si existe
   ```

#### 4.1.2 Error: "Port 8081 already in use"

**Síntomas:**
- La aplicación no inicia
- Error: `Port 8081 is already in use`

**Diagnóstico:**

```bash
# Windows
netstat -ano | findstr :8081

# Linux/Mac
lsof -i :8081
# o
netstat -tulpn | grep 8081
```

**Solución:**

1. **Opción 1: Detener proceso que usa el puerto**
   ```bash
   # Windows (usar PID del netstat)
   taskkill /PID <PID> /F
   
   # Linux/Mac
   kill -9 <PID>
   ```

2. **Opción 2: Cambiar puerto de la aplicación**
   ```properties
   # En application.properties
   server.port=8082
   ```
   - Actualizar frontend para usar nuevo puerto
   - Reiniciar aplicación

#### 4.1.3 Error: "OpenRouter API key not configured correctly"

**Síntomas:**
- Chat con IA no funciona
- Errores en logs: `API key de OpenRouter no está configurada`
- Respuestas de chat retornan error

**Diagnóstico:**

```bash
# Verificar configuración en application.properties
grep "openrouter.api.key" backend/desmartin/src/main/resources/application.properties

# Probar API key manualmente
curl -X POST https://openrouter.ai/api/v1/chat/completions \
  -H "Authorization: Bearer <API_KEY>" \
  -H "Content-Type: application/json" \
  -d '{"model":"deepseek/deepseek-chat-v3.1:free","messages":[{"role":"user","content":"test"}]}'
```

**Solución:**

1. **Verificar que la API key esté configurada:**
   ```properties
   openrouter.api.key=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
   ```

2. **Si la API key es inválida o expiró:**
   - Obtener nueva API key de https://openrouter.ai
   - Actualizar en `application.properties`
   - Reiniciar aplicación

3. **Verificar conectividad a OpenRouter:**
   ```bash
   ping openrouter.ai
   curl -I https://openrouter.ai
   ```

#### 4.1.4 Error: "OutOfMemoryError" o "Heap Space"

**Síntomas:**
- La aplicación se cuelga o crashea
- Errores en logs: `java.lang.OutOfMemoryError: Java heap space`
- Rendimiento degradado

**Diagnóstico:**

```bash
# Ver uso de memoria actual
jstat -gc <PID> 1000 5

# Ver configuración JVM
jps -v
```

**Solución:**

1. **Aumentar heap size:**
   ```bash
   # Al iniciar aplicación
   java -Xms1024m -Xmx2048m -jar desmartin-0.0.1-SNAPSHOT.jar
   
   # O configurar en script de inicio permanente
   ```

2. **Optimizar consultas que consumen mucha memoria:**
   ```sql
   -- Revisar consultas que cargan muchos datos
   -- Implementar paginación en endpoints que listan muchos registros
   ```

3. **Revisar memory leaks:**
   - Usar herramientas de profiling (JProfiler, VisualVM)
   - Revisar código que mantiene referencias a objetos grandes

#### 4.1.5 Error: "Foreign Key Constraint Fails"

**Síntomas:**
- No se pueden eliminar registros
- Errores al intentar operaciones de eliminación
- Error: `Cannot delete or update a parent row`

**Diagnóstico:**

```sql
-- Verificar foreign keys involucradas
SHOW CREATE TABLE chat_mensajes;
SHOW CREATE TABLE chat_sesiones;

-- Verificar registros dependientes
SELECT COUNT(*) FROM chat_mensajes WHERE id_sesion_fk = <ID_SESION>;
```

**Solución:**

1. **Eliminar registros dependientes primero:**
   ```sql
   -- Ejemplo: Eliminar mensajes antes de eliminar sesión
   DELETE FROM chat_mensajes WHERE id_sesion_fk = <ID_SESION>;
   DELETE FROM chat_sesiones WHERE id_sesion = <ID_SESION>;
   ```

2. **Usar CASCADE si es apropiado:**
   ```sql
   -- Modificar foreign key para incluir ON DELETE CASCADE
   ALTER TABLE chat_mensajes
   DROP FOREIGN KEY chat_mensajes_ibfk_1,
   ADD CONSTRAINT chat_mensajes_ibfk_1
   FOREIGN KEY (id_sesion_fk) REFERENCES chat_sesiones(id_sesion)
   ON DELETE CASCADE;
   ```

#### 4.1.6 Error: "Limpieza automática de chat no funciona"

**Síntomas:**
- Datos antiguos no se eliminan automáticamente
- Base de datos crece sin control

**Diagnóstico:**

```sql
-- Verificar que hay datos antiguos
SELECT COUNT(*) FROM chat_mensajes 
WHERE fecha_hora_envio < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- Verificar logs de limpieza
SELECT * FROM log_accesos 
WHERE descripcion LIKE '%limpieza%' 
ORDER BY fecha_hora DESC LIMIT 10;
```

**Solución:**

1. **Verificar que @EnableScheduling esté habilitado:**
   ```java
   // En DesmartinApplication.java debe estar:
   @EnableScheduling
   ```

2. **Verificar configuración de cron:**
   ```properties
   chat.limpieza.cron=0 0 2 * * ?
   chat.retencion.dias=30
   ```

3. **Ejecutar limpieza manualmente:**
   ```java
   // A través del servicio
   LimpiezaChatService.ejecutarLimpiezaManual(30);
   ```

4. **Revisar logs de Spring Boot para errores en tarea programada**

### 4.2 Procedimiento General de Corrección

#### 4.2.1 Pasos para Resolver un Error

1. **Identificar el error:**
   - Revisar logs de aplicación
   - Revisar logs de base de datos
   - Revisar logs del sistema operativo
   - Consultar logs de auditoría en `log_accesos`

2. **Reproducir el error:**
   - Intentar reproducir en ambiente de desarrollo
   - Documentar pasos para reproducir
   - Capturar stack traces completos

3. **Investigar causa raíz:**
   - Buscar en documentación
   - Buscar en issues conocidos de dependencias
   - Revisar código relacionado
   - Consultar logs históricos

4. **Desarrollar solución:**
   - Crear fix en ambiente de desarrollo
   - Probar solución exhaustivamente
   - Documentar cambios realizados

5. **Aplicar solución:**
   - Hacer backup antes de aplicar cambios
   - Aplicar en producción en ventana de mantenimiento
   - Monitorear después de aplicar

6. **Verificar solución:**
   - Confirmar que el error no se reproduce
   - Verificar que no se introdujeron nuevos problemas
   - Actualizar documentación si es necesario

#### 4.2.2 Herramientas de Diagnóstico

**Logs:**
- Spring Boot logs: `logs/spring.log` o salida de consola
- MySQL logs: `/var/log/mysql/error.log` (Linux) o configuración específica
- Logs de auditoría: Tabla `log_accesos` en base de datos

**Comandos útiles:**
```bash
# Ver logs en tiempo real
tail -f logs/spring.log

# Buscar errores específicos
grep -i "error\|exception" logs/spring.log | tail -50

# Ver procesos Java
jps -lvm

# Ver uso de recursos
top
htop
```

**Consultas SQL útiles:**
```sql
-- Ver últimas operaciones
SELECT * FROM log_accesos ORDER BY fecha_hora DESC LIMIT 100;

-- Ver errores recientes
SELECT * FROM log_accesos 
WHERE nivel = 'ERROR' 
ORDER BY fecha_hora DESC LIMIT 50;

-- Ver rendimiento de endpoints
SELECT endpoint, AVG(duracion_ms) as avg_ms, COUNT(*) as calls
FROM log_accesos
WHERE fecha_hora > DATE_SUB(NOW(), INTERVAL 1 HOUR)
GROUP BY endpoint;
```

---

## 5. Respaldo de Información

### 5.1 Backup Manual

#### 5.1.1 Backup de Base de Datos MySQL

**Backup completo de base de datos:**

```bash
# Windows
mysqldump -u root -p prmartin > backup_prmartin_YYYYMMDD.sql

# Linux/Mac
mysqldump -u root -p prmartin > backup_prmartin_$(date +%Y%m%d).sql
```

**Backup con compresión:**

```bash
# Windows PowerShell
mysqldump -u root -p prmartin | Compress-Archive -DestinationPath backup_prmartin_YYYYMMDD.zip

# Linux/Mac
mysqldump -u root -p prmartin | gzip > backup_prmartin_$(date +%Y%m%d).sql.gz
```

**Backup de tablas específicas:**

```bash
# Backup solo de datos críticos
mysqldump -u root -p prmartin administradores docentes alumnos > backup_usuarios_YYYYMMDD.sql

# Backup de chat (para análisis antes de limpieza)
mysqldump -u root -p prmartin chat_sesiones chat_mensajes > backup_chat_YYYYMMDD.sql
```

#### 5.1.2 Backup de Código Fuente

**Backup completo del proyecto:**

```bash
# Crear backup del código
tar -czf backup_codigo_$(date +%Y%m%d).tar.gz backend/ Frontend/

# O usar git para crear tag de respaldo
git tag backup-YYYYMMDD
git push origin backup-YYYYMMDD
```

#### 5.1.3 Backup de Configuración

**Backup de archivos de configuración:**

```bash
# Backup de application.properties
cp backend/desmartin/src/main/resources/application.properties \
   backup/application.properties.YYYYMMDD

# Backup de configuración de Angular
cp Frontend/angular.json backup/angular.json.YYYYMMDD
cp Frontend/package.json backup/package.json.YYYYMMDD
```

### 5.2 Backup Programado

#### 5.2.1 Script de Backup Automático (Linux/Mac)

**Crear script `backup_desmartin.sh`:**

```bash
#!/bin/bash

# Configuración
DB_NAME="prmartin"
DB_USER="root"
DB_PASS="tu_password"
BACKUP_DIR="/ruta/backups/desmartin"
DATE=$(date +%Y%m%d_%H%M%S)
RETENTION_DAYS=30

# Crear directorio si no existe
mkdir -p $BACKUP_DIR

# Backup de base de datos
mysqldump -u $DB_USER -p$DB_PASS $DB_NAME | gzip > $BACKUP_DIR/db_backup_$DATE.sql.gz

# Backup de código (si está en ruta específica)
tar -czf $BACKUP_DIR/code_backup_$DATE.tar.gz /ruta/proyecto/backend /ruta/proyecto/Frontend

# Eliminar backups antiguos (más de 30 días)
find $BACKUP_DIR -name "*.sql.gz" -mtime +$RETENTION_DAYS -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +$RETENTION_DAYS -delete

echo "Backup completado: $DATE"
```

**Configurar en crontab:**

```bash
# Editar crontab
crontab -e

# Ejecutar backup diario a las 3:00 AM
0 3 * * * /ruta/scripts/backup_desmartin.sh >> /var/log/backup_desmartin.log 2>&1
```

#### 5.2.2 Script de Backup Automático (Windows)

**Crear script `backup_desmartin.ps1`:**

```powershell
# Configuración
$DB_NAME = "prmartin"
$DB_USER = "root"
$DB_PASS = "tu_password"
$BACKUP_DIR = "C:\Backups\Desmartin"
$DATE = Get-Date -Format "yyyyMMdd_HHmmss"
$RETENTION_DAYS = 30

# Crear directorio si no existe
New-Item -ItemType Directory -Force -Path $BACKUP_DIR

# Backup de base de datos
$backupFile = "$BACKUP_DIR\db_backup_$DATE.sql"
& mysqldump -u $DB_USER -p$DB_PASS $DB_NAME | Out-File -FilePath $backupFile -Encoding UTF8
Compress-Archive -Path $backupFile -DestinationPath "$backupFile.zip"
Remove-Item $backupFile

# Backup de código
$codeBackup = "$BACKUP_DIR\code_backup_$DATE.zip"
Compress-Archive -Path "C:\Proyecto\backend", "C:\Proyecto\Frontend" -DestinationPath $codeBackup

# Eliminar backups antiguos
Get-ChildItem -Path $BACKUP_DIR -Filter "*.zip" | 
    Where-Object { $_.LastWriteTime -lt (Get-Date).AddDays(-$RETENTION_DAYS) } | 
    Remove-Item

Write-Host "Backup completado: $DATE"
```

**Configurar en Task Scheduler:**

1. Abrir Task Scheduler
2. Crear tarea básica
3. Configurar trigger: Diario a las 3:00 AM
4. Acción: Iniciar programa `powershell.exe`
5. Argumentos: `-File "C:\Scripts\backup_desmartin.ps1"`

### 5.3 Restauración de Backup

#### 5.3.1 Restaurar Base de Datos

**Desde archivo SQL:**

```bash
# Restaurar backup completo
mysql -u root -p prmartin < backup_prmartin_YYYYMMDD.sql

# Restaurar desde archivo comprimido
gunzip < backup_prmartin_YYYYMMDD.sql.gz | mysql -u root -p prmartin
```

**Restaurar tablas específicas:**

```bash
mysql -u root -p prmartin < backup_usuarios_YYYYMMDD.sql
```

#### 5.3.2 Verificar Integridad del Backup

**Antes de restaurar:**

```bash
# Verificar que el archivo SQL es válido
head -n 20 backup_prmartin_YYYYMMDD.sql

# Verificar tamaño del backup
ls -lh backup_prmartin_YYYYMMDD.sql

# Probar restauración en base de datos de prueba
mysql -u root -p -e "CREATE DATABASE prmartin_test;"
mysql -u root -p prmartin_test < backup_prmartin_YYYYMMDD.sql
mysql -u root -p -e "DROP DATABASE prmartin_test;"
```

---

## 6. Actualización del Sistema

### 6.1 Proceso de Actualización

#### 6.1.1 Preparación

1. **Hacer backup completo:**
   ```bash
   # Backup de base de datos
   mysqldump -u root -p prmartin > backup_pre_update_$(date +%Y%m%d).sql
   
   # Backup de código
   git tag pre-update-$(date +%Y%m%d)
   git push origin pre-update-$(date +%Y%m%d)
   ```

2. **Revisar changelog y notas de versión:**
   - Revisar changelog de Spring Boot
   - Revisar changelog de Angular
   - Revisar changelog de dependencias principales
   - Identificar breaking changes

3. **Probar en ambiente de desarrollo:**
   - Aplicar actualizaciones en desarrollo
   - Ejecutar suite de pruebas
   - Verificar funcionalidades críticas

#### 6.1.2 Actualización del Backend

**Proceso paso a paso:**

```bash
cd backend/desmartin

# 1. Actualizar dependencias en pom.xml (revisar cambios primero)
# Editar pom.xml manualmente o usar:
mvn versions:use-latest-versions

# 2. Limpiar y compilar
mvn clean install

# 3. Verificar que compile sin errores
mvn compile

# 4. Ejecutar tests (si existen)
mvn test

# 5. Construir JAR
mvn clean package

# 6. Detener aplicación actual
# (Depende de cómo esté ejecutándose: servicio, proceso, etc.)

# 7. Copiar nuevo JAR
cp target/desmartin-0.0.1-SNAPSHOT.jar /ruta/aplicacion/

# 8. Iniciar aplicación
java -jar desmartin-0.0.1-SNAPSHOT.jar
```

**Actualización de Spring Boot mayor:**

Si se actualiza a una versión mayor de Spring Boot (ej: 3.3.x a 3.4.x):

1. Revisar guía de migración de Spring Boot
2. Actualizar `pom.xml` con nueva versión
3. Revisar cambios en `application.properties`
4. Actualizar código si hay breaking changes
5. Probar exhaustivamente

#### 6.1.3 Actualización del Frontend

**Proceso paso a paso:**

```bash
cd Frontend

# 1. Hacer backup de package.json
cp package.json package.json.backup

# 2. Actualizar Angular CLI globalmente (si es necesario)
npm install -g @angular/cli@latest

# 3. Actualizar Angular y dependencias
ng update @angular/core @angular/cli

# 4. Actualizar otras dependencias
npm update

# 5. Instalar nuevas dependencias
npm install

# 6. Verificar que compile
ng build

# 7. Probar en desarrollo
ng serve

# 8. Construir para producción
ng build --configuration production

# 9. Desplegar archivos de dist/
cp -r dist/proyecto-ia/* /ruta/servidor/web/
```

**Actualización mayor de Angular:**

Si se actualiza a una versión mayor de Angular (ej: 19.x a 20.x):

1. Revisar guía de migración de Angular
2. Ejecutar `ng update @angular/core @angular/cli --migrate-only`
3. Revisar y aplicar cambios sugeridos
4. Actualizar código manualmente si es necesario
5. Probar exhaustivamente

#### 6.1.4 Actualización de Base de Datos

**Si hay cambios en esquema:**

1. **Revisar migraciones pendientes:**
   ```sql
   -- Verificar estructura actual
   DESCRIBE tabla_nombre;
   SHOW CREATE TABLE tabla_nombre;
   ```

2. **Aplicar scripts de migración:**
   ```bash
   # Ejecutar scripts SQL de migración
   mysql -u root -p prmartin < migration_chat_por_curso.sql
   ```

3. **Verificar que las migraciones se aplicaron correctamente:**
   ```sql
   -- Verificar estructura después de migración
   DESCRIBE tabla_nombre;
   SHOW INDEX FROM tabla_nombre;
   ```

#### 6.1.5 Verificación Post-Actualización

**Checklist de verificación:**

1. ✅ Aplicación inicia correctamente
2. ✅ Conexión a base de datos funciona
3. ✅ Endpoints principales responden
4. ✅ Login funciona para todos los tipos de usuario
5. ✅ Chat con IA funciona
6. ✅ Tests se pueden completar
7. ✅ Frontend carga correctamente
8. ✅ No hay errores en logs

**Comandos de verificación:**

```bash
# Verificar que la aplicación está corriendo
curl http://localhost:8081/api/admin/docentes

# Verificar logs
tail -f logs/spring.log

# Verificar base de datos
mysql -u root -p -e "USE prmartin; SHOW TABLES;"
```

### 6.2 Rollback

#### 6.2.1 Procedimiento de Rollback

Si la actualización causa problemas:

1. **Detener aplicación actual**

2. **Restaurar código anterior:**
   ```bash
   # Desde git
   git checkout pre-update-YYYYMMDD
   
   # O restaurar desde backup
   tar -xzf backup_codigo_YYYYMMDD.tar.gz
   ```

3. **Restaurar base de datos:**
   ```bash
   mysql -u root -p prmartin < backup_pre_update_YYYYMMDD.sql
   ```

4. **Recompilar y desplegar versión anterior:**
   ```bash
   cd backend/desmartin
   mvn clean package
   java -jar target/desmartin-0.0.1-SNAPSHOT.jar
   ```

5. **Verificar que todo funciona**

6. **Documentar problemas encontrados para futuras actualizaciones**

---

## 7. Documentación de Cambios

### 7.1 Formato CHANGELOG.md

Se recomienda mantener un archivo `CHANGELOG.md` con el siguiente formato:

```markdown
# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [Unreleased]

### Agregado
- Nueva funcionalidad X
- Nuevo endpoint Y

### Cambiado
- Mejora en rendimiento de endpoint Z

### Deprecado
- Endpoint antiguo (será removido en v2.0)

### Eliminado
- Funcionalidad obsoleta

### Corregido
- Bug en autenticación
- Error en cálculo de resultados

### Seguridad
- Actualización de dependencia con vulnerabilidad

## [1.0.0] - 2025-01-11

### Agregado
- Sistema de gestión de usuarios (Admin, Docentes, Alumnos)
- Sistema de evaluación de inteligencias múltiples
- Chat con IA integrado con OpenRouter
- Sistema de auditoría y logging
- Limpieza automática de datos de chat (30 días)
- Documentación completa de endpoints

### Cambiado
- Migración de Spring Boot 2.x a 3.3.5
- Migración de Angular 18 a 19.2.0

### Corregido
- Error en cálculo de resultados de test
- Problema de conexión a OpenRouter API

## [0.9.0] - 2024-12-15

### Agregado
- Sistema básico de chat
- Integración inicial con OpenRouter

### Cambiado
- Mejoras en rendimiento de consultas a base de datos

## [0.8.0] - 2024-11-20

### Agregado
- Sistema de tests de inteligencias múltiples
- Gestión de resultados

### Corregido
- Bug en creación de alumnos
```

### 7.2 Registro de Cambios en Base de Datos

Para cambios en esquema de base de datos, mantener registro:

```sql
-- Crear tabla de registro de migraciones (opcional)
CREATE TABLE IF NOT EXISTS schema_migrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    migration_name VARCHAR(255) NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT
);

-- Registrar migración aplicada
INSERT INTO schema_migrations (migration_name, description)
VALUES ('migration_chat_por_curso', 'Agregar soporte para chats por curso');
```

### 7.3 Documentación de Endpoints

Mantener `ENDPOINTS.md` actualizado con:
- Nuevos endpoints agregados
- Endpoints modificados
- Endpoints deprecados
- Cambios en formato de request/response

---

## 8. Herramientas Recomendadas

### 8.1 Desarrollo y Build

**Backend:**
- **IDE:** IntelliJ IDEA, Eclipse, VS Code
- **Java:** JDK 21
- **Maven:** 3.8+ (incluido mvnw en proyecto)
- **MySQL Workbench:** Para gestión de base de datos
- **Postman/Insomnia:** Para pruebas de API

**Frontend:**
- **IDE:** VS Code, WebStorm
- **Node.js:** 18+ (verificar compatibilidad con Angular 19)
- **Angular CLI:** 19.2.8+
- **Navegador:** Chrome DevTools para debugging

### 8.2 Monitoreo y Logging

**Logs:**
- **Spring Boot Actuator:** Ya incluido en dependencias
- **SLF4J + Logback:** Configurado por defecto en Spring Boot
- **MySQL Slow Query Log:** Para identificar consultas lentas

**Monitoreo:**
- **JConsole/VisualVM:** Para monitoreo de JVM
- **MySQL Workbench:** Para monitoreo de base de datos
- **cURL/Postman:** Para monitoreo de endpoints

### 8.3 Backup y Restauración

**Base de Datos:**
- **mysqldump:** Herramienta estándar de MySQL
- **MySQL Workbench:** Interfaz gráfica para backups
- **Compresión:** gzip (Linux/Mac), PowerShell Compress-Archive (Windows)

**Código:**
- **Git:** Control de versiones
- **tar/gzip:** Para backups de archivos
- **GitHub/GitLab:** Repositorio remoto

### 8.4 Testing

**Backend:**
- **JUnit 5:** Framework de testing (incluido en Spring Boot Starter Test)
- **Mockito:** Para mocking (incluido)
- **Spring Boot Test:** Para tests de integración

**Frontend:**
- **Jasmine + Karma:** Framework de testing de Angular
- **Protractor/Cypress:** Para tests end-to-end (si se implementan)

### 8.5 Seguridad

**Análisis de Vulnerabilidades:**
- **OWASP Dependency Check:** Para analizar dependencias
  ```bash
  mvn org.owasp:dependency-check-maven:check
  ```
- **npm audit:** Para frontend
  ```bash
  npm audit
  npm audit fix
  ```

**Análisis de Código:**
- **SonarQube:** Para análisis estático de código
- **SpotBugs:** Para análisis de código Java

---

## 9. Contacto de Soporte

### 9.1 Información de Contacto

**Email de Soporte Técnico:**
```
soporte@desmartin.app
```

**Email para Reportar Bugs:**
```
bugs@desmartin.app
```

**Nota:** Estos emails son placeholders. Actualizar con información real del proyecto.

### 9.2 Escalación de Problemas

**Nivel 1 - Soporte Básico:**
- Problemas de uso común
- Consultas sobre funcionalidades
- Problemas de configuración básica

**Nivel 2 - Soporte Técnico:**
- Problemas de integración
- Errores en aplicación
- Problemas de rendimiento

**Nivel 3 - Soporte Avanzado:**
- Problemas críticos del sistema
- Errores que afectan producción
- Problemas de seguridad

### 9.3 Información para Reportar Problemas

Al reportar un problema, incluir:

1. **Descripción del problema:**
   - Qué estaba intentando hacer
   - Qué resultado esperaba
   - Qué resultado obtuvo

2. **Información del entorno:**
   - Versión de Java
   - Versión de MySQL
   - Versión de Node.js (si aplica)
   - Sistema operativo

3. **Logs relevantes:**
   - Logs de Spring Boot
   - Logs de base de datos
   - Logs de navegador (si aplica)

4. **Pasos para reproducir:**
   - Pasos detallados para reproducir el problema

5. **Screenshots o capturas:**
   - Si aplica, incluir screenshots del error

---

## 10. Conclusión

Este Manual de Mantenimiento proporciona una guía completa y técnica para el mantenimiento del sistema Desmartin. Siguiendo los procedimientos descritos en este documento, se puede asegurar:

- **Alta disponibilidad** del sistema mediante mantenimiento preventivo regular
- **Resolución rápida** de problemas mediante procedimientos de mantenimiento correctivo documentados
- **Integridad de datos** mediante estrategias de backup y restauración
- **Evolución continua** mediante procesos de actualización estructurados
- **Trazabilidad** mediante documentación adecuada de cambios

Es importante revisar y actualizar este manual periódicamente para reflejar cambios en el sistema, nuevas funcionalidades, y lecciones aprendidas de incidentes pasados.

**Última actualización:** 2025-01-11  
**Próxima revisión recomendada:** 2025-04-11 (trimestral)

---

## Anexos

### Anexo A: Comandos Rápidos de Referencia

```bash
# Iniciar aplicación
cd backend/desmartin
./mvnw spring-boot:run

# Compilar proyecto
mvn clean package

# Ver logs en tiempo real
tail -f logs/spring.log

# Backup rápido de BD
mysqldump -u root -p prmartin > backup_$(date +%Y%m%d).sql

# Verificar puerto
netstat -ano | findstr :8081  # Windows
lsof -i :8081                  # Linux/Mac

# Ver procesos Java
jps -lvm

# Verificar MySQL
mysqladmin -u root -p ping
```

### Anexo B: Estructura de Directorios del Proyecto

```
Proyecto/
├── backend/
│   └── desmartin/
│       ├── src/
│       │   └── main/
│       │       ├── java/com/appmartin/desmartin/
│       │       │   ├── config/
│       │       │   ├── controller/
│       │       │   ├── dto/
│       │       │   ├── model/
│       │       │   ├── repository/
│       │       │   └── service/
│       │       └── resources/
│       │           └── application.properties
│       ├── pom.xml
│       ├── mvnw
│       └── mvnw.cmd
├── Frontend/
│   ├── src/
│   │   └── app/
│   ├── angular.json
│   ├── package.json
│   └── tsconfig.json
└── Manual_de_Mantenimiento_Desmartin.md
```

### Anexo C: Configuración de Variables de Entorno Recomendadas

Para producción, considerar mover configuraciones sensibles a variables de entorno:

```bash
# application.properties (producción)
spring.datasource.password=${DB_PASSWORD}
openrouter.api.key=${OPENROUTER_API_KEY}
```

```bash
# Variables de entorno
export DB_PASSWORD="password_seguro"
export OPENROUTER_API_KEY="sk-or-v1-..."
```

---

**Fin del Manual de Mantenimiento**

