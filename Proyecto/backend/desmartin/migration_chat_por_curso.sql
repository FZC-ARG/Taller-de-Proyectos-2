-- ============================================
-- MIGRACIÓN: Agregar soporte para chats por curso
-- Fecha: 2025-01-XX
-- Descripción: Extiende chat_sesiones para soportar chats grupales por curso
-- ============================================

USE `prmartin`;

-- ============================================
-- PASO 1: Agregar columna para curso
-- ============================================
ALTER TABLE `chat_sesiones`
ADD COLUMN `id_curso_fk` int(11) DEFAULT NULL AFTER `id_alumno_fk`;

-- ============================================
-- PASO 2: Agregar foreign key a cursos
-- ============================================
ALTER TABLE `chat_sesiones`
ADD CONSTRAINT `chat_sesiones_ibfk_3` 
  FOREIGN KEY (`id_curso_fk`) REFERENCES `cursos` (`id_curso`) 
  ON DELETE CASCADE;

-- ============================================
-- PASO 3: Agregar índices para optimización
-- ============================================

-- Índices en chat_sesiones
ALTER TABLE `chat_sesiones`
ADD INDEX `idx_docente` (`id_docente_fk`),
ADD INDEX `idx_alumno` (`id_alumno_fk`),
ADD INDEX `idx_curso` (`id_curso_fk`);

-- Índices en chat_mensajes
ALTER TABLE `chat_mensajes`
ADD INDEX `idx_sesion` (`id_sesion_fk`),
ADD INDEX `idx_fecha_envio` (`fecha_hora_envio`);

-- ============================================
-- PASO 4: Verificar estructura final
-- ============================================
DESCRIBE `chat_sesiones`;
DESCRIBE `chat_mensajes`;

-- ============================================
-- PASO 5: Verificar índices creados
-- ============================================
SHOW INDEX FROM `chat_sesiones`;
SHOW INDEX FROM `chat_mensajes`;

-- ============================================
-- FIN DE MIGRACIÓN
-- ============================================
-- 
-- NOTAS:
-- 1. Los chats existentes seguirán funcionando (id_curso_fk será NULL)
-- 2. Validar en código Java que una sesión es O individual O grupal
-- 3. Los índices mejorarán el rendimiento de consultas frecuentes
--

