# 📊 ETAPA 1: Validación y Optimización de Base de Datos

## 🔍 Análisis de la Estructura Actual

### **Tabla: `chat_sesiones`**

**Estructura Actual:**
```sql
CREATE TABLE `chat_sesiones` (
  `id_sesion` int(11) NOT NULL,
  `id_docente_fk` int(11) NOT NULL,
  `id_alumno_fk` int(11) DEFAULT NULL,
  `titulo_sesion` varchar(255) NOT NULL,
  `fecha_creacion` datetime DEFAULT current_timestamp()
)
```

**Análisis:**
- ✅ Soporta chats individuales por alumno (`id_alumno_fk` puede ser NULL)
- ❌ **NO soporta chats grupales por curso**
- ✅ Tiene relación con docente (obligatoria)
- ✅ Tiene timestamp de creación automático

**Problemas Identificados:**
1. Falta campo `id_curso_fk` para soportar chats grupales
2. Falta índice en `id_docente_fk` para consultas rápidas
3. Falta índice en `id_alumno_fk` para consultas por alumno
4. No hay constraint que valide que una sesión es O individual O grupal (no ambas)

---

### **Tabla: `chat_mensajes`**

**Estructura Actual:**
```sql
CREATE TABLE `chat_mensajes` (
  `id_mensaje` int(11) NOT NULL,
  `id_sesion_fk` int(11) NOT NULL,
  `emisor` enum('docente','ia') NOT NULL,
  `contenido` text NOT NULL,
  `fecha_hora_envio` datetime DEFAULT current_timestamp()
)
```

**Análisis:**
- ✅ Estructura correcta y completa
- ✅ Tiene foreign key a `chat_sesiones`
- ✅ Timestamp automático
- ✅ Enum para tipo de emisor

**Optimizaciones Necesarias:**
1. Índice en `id_sesion_fk` para consultas de historial
2. Índice en `fecha_hora_envio` para ordenamiento

---

## 🔧 Cambios Propuestos

### **1. Agregar soporte para chats por curso**

**Cambio en `chat_sesiones`:**
```sql
ALTER TABLE `chat_sesiones`
ADD COLUMN `id_curso_fk` int(11) DEFAULT NULL AFTER `id_alumno_fk`,
ADD CONSTRAINT `chat_sesiones_ibfk_3` 
  FOREIGN KEY (`id_curso_fk`) REFERENCES `cursos` (`id_curso`) 
  ON DELETE CASCADE;
```

**Lógica de Negocio:**
- Si `id_alumno_fk` tiene valor → Chat individual
- Si `id_curso_fk` tiene valor → Chat grupal
- Si ambos son NULL → Error (sesión inválida)
- Si ambos tienen valor → Error (sesión ambigua)

### **2. Agregar índices para optimización**

```sql
-- Índice para consultas por docente
ALTER TABLE `chat_sesiones`
ADD INDEX `idx_docente` (`id_docente_fk`);

-- Índice para consultas por alumno
ALTER TABLE `chat_sesiones`
ADD INDEX `idx_alumno` (`id_alumno_fk`);

-- Índice para consultas por curso
ALTER TABLE `chat_sesiones`
ADD INDEX `idx_curso` (`id_curso_fk`);

-- Índice para mensajes por sesión
ALTER TABLE `chat_mensajes`
ADD INDEX `idx_sesion` (`id_sesion_fk`);

-- Índice para ordenamiento por fecha
ALTER TABLE `chat_mensajes`
ADD INDEX `idx_fecha_envio` (`fecha_hora_envio`);
```

### **3. Agregar constraint para validación**

```sql
-- Constraint: Una sesión debe ser O individual O grupal (no ambas)
ALTER TABLE `chat_sesiones`
ADD CONSTRAINT `chk_sesion_tipo` 
  CHECK (
    (`id_alumno_fk` IS NOT NULL AND `id_curso_fk` IS NULL) OR
    (`id_alumno_fk` IS NULL AND `id_curso_fk` IS NOT NULL)
  );
```

**Nota:** MySQL/MariaDB no soporta CHECK constraints en versiones antiguas. 
Si no funciona, validaremos en el código Java.

---

## 📝 Script de Migración Completo

```sql
-- ============================================
-- MIGRACIÓN: Agregar soporte para chats por curso
-- Fecha: 2025-01-XX
-- Descripción: Extiende chat_sesiones para soportar chats grupales
-- ============================================

USE `prmartin`;

-- 1. Agregar columna para curso
ALTER TABLE `chat_sesiones`
ADD COLUMN `id_curso_fk` int(11) DEFAULT NULL AFTER `id_alumno_fk`;

-- 2. Agregar foreign key a cursos
ALTER TABLE `chat_sesiones`
ADD CONSTRAINT `chat_sesiones_ibfk_3` 
  FOREIGN KEY (`id_curso_fk`) REFERENCES `cursos` (`id_curso`) 
  ON DELETE CASCADE;

-- 3. Agregar índices para optimización
ALTER TABLE `chat_sesiones`
ADD INDEX `idx_docente` (`id_docente_fk`),
ADD INDEX `idx_alumno` (`id_alumno_fk`),
ADD INDEX `idx_curso` (`id_curso_fk`);

ALTER TABLE `chat_mensajes`
ADD INDEX `idx_sesion` (`id_sesion_fk`),
ADD INDEX `idx_fecha_envio` (`fecha_hora_envio`);

-- 4. Verificar estructura final
DESCRIBE `chat_sesiones`;
DESCRIBE `chat_mensajes`;

-- 5. Verificar índices
SHOW INDEX FROM `chat_sesiones`;
SHOW INDEX FROM `chat_mensajes`;
```

---

## ✅ Validación Post-Migración

### **Casos de Prueba:**

1. **Chat Individual (Existente)**
   ```sql
   INSERT INTO chat_sesiones (id_docente_fk, id_alumno_fk, id_curso_fk, titulo_sesion)
   VALUES (1, 5, NULL, 'Consulta sobre Juan');
   -- ✅ Debe funcionar
   ```

2. **Chat Grupal (Nuevo)**
   ```sql
   INSERT INTO chat_sesiones (id_docente_fk, id_alumno_fk, id_curso_fk, titulo_sesion)
   VALUES (1, NULL, 2, 'Consulta sobre Matemáticas 1');
   -- ✅ Debe funcionar
   ```

3. **Sesión Inválida (Ambos NULL)**
   ```sql
   INSERT INTO chat_sesiones (id_docente_fk, id_alumno_fk, id_curso_fk, titulo_sesion)
   VALUES (1, NULL, NULL, 'Sesión inválida');
   -- ❌ Debe fallar (validar en código Java)
   ```

4. **Sesión Ambigua (Ambos con valor)**
   ```sql
   INSERT INTO chat_sesiones (id_docente_fk, id_alumno_fk, id_curso_fk, titulo_sesion)
   VALUES (1, 5, 2, 'Sesión ambigua');
   -- ❌ Debe fallar (validar en código Java)
   ```

---

## 📊 Estructura Final Esperada

### **`chat_sesiones`**
```sql
CREATE TABLE `chat_sesiones` (
  `id_sesion` int(11) NOT NULL AUTO_INCREMENT,
  `id_docente_fk` int(11) NOT NULL,
  `id_alumno_fk` int(11) DEFAULT NULL,      -- NULL para chats grupales
  `id_curso_fk` int(11) DEFAULT NULL,      -- NULL para chats individuales
  `titulo_sesion` varchar(255) NOT NULL,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_sesion`),
  KEY `idx_docente` (`id_docente_fk`),
  KEY `idx_alumno` (`id_alumno_fk`),
  KEY `idx_curso` (`id_curso_fk`),
  CONSTRAINT `chat_sesiones_ibfk_1` FOREIGN KEY (`id_docente_fk`) REFERENCES `docentes` (`id_docente`),
  CONSTRAINT `chat_sesiones_ibfk_2` FOREIGN KEY (`id_alumno_fk`) REFERENCES `alumnos` (`id_alumno`),
  CONSTRAINT `chat_sesiones_ibfk_3` FOREIGN KEY (`id_curso_fk`) REFERENCES `cursos` (`id_curso`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### **`chat_mensajes`**
```sql
CREATE TABLE `chat_mensajes` (
  `id_mensaje` int(11) NOT NULL AUTO_INCREMENT,
  `id_sesion_fk` int(11) NOT NULL,
  `emisor` enum('docente','ia') NOT NULL,
  `contenido` text NOT NULL,
  `fecha_hora_envio` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_mensaje`),
  KEY `idx_sesion` (`id_sesion_fk`),
  KEY `idx_fecha_envio` (`fecha_hora_envio`),
  CONSTRAINT `chat_mensajes_ibfk_1` FOREIGN KEY (`id_sesion_fk`) REFERENCES `chat_sesiones` (`id_sesion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 🎯 Próximos Pasos

Una vez completada esta etapa:
1. ✅ Estructura de BD optimizada
2. ✅ Soporte para chats individuales y grupales
3. ✅ Índices para optimización de consultas
4. ➡️ **Siguiente**: Actualizar modelos JPA (ETAPA 2)

---

## ⚠️ Notas Importantes

1. **Backward Compatibility**: Los chats existentes seguirán funcionando (tienen `id_alumno_fk`, `id_curso_fk` será NULL)

2. **Validación en Código**: Como MySQL puede no soportar CHECK constraints, validaremos en Java que:
   - O `id_alumno_fk` tiene valor O `id_curso_fk` tiene valor
   - No ambos, no ninguno

3. **Cascade Delete**: Si se elimina un curso, se eliminan sus chats (ON DELETE CASCADE)

4. **Índices**: Agregados para optimizar consultas frecuentes:
   - Sesiones por docente
   - Sesiones por alumno
   - Sesiones por curso
   - Mensajes por sesión ordenados por fecha

