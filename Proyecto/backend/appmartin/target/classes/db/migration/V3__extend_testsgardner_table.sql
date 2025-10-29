-- Migration V3: Extend TestsGardner table with additional columns for autosave and better tracking
-- Project: Sistema de Gestión Académica con IA - Test Gardner Module
-- Date: December 2024

-- NOTE: Target DB is MySQL 5.5, which lacks IF NOT EXISTS for ADD COLUMN/INDEX.
-- We use dynamic SQL against INFORMATION_SCHEMA to avoid errors when columns/indexes already exist.

-- estado_guardado
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'estado_guardado'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN estado_guardado ENUM(\'BORRADOR\', \'FINAL\', \'CALCULADO\') DEFAULT \'BORRADOR\' AFTER Puntajes'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- version_guardado
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'version_guardado'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN version_guardado INT DEFAULT 1 AFTER estado_guardado'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- client_request_id
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'client_request_id'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN client_request_id VARCHAR(255) NULL AFTER version_guardado'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- inteligencia_predominante
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'inteligencia_predominante'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN inteligencia_predominante VARCHAR(100) NULL AFTER client_request_id'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- puntaje_total
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'puntaje_total'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN puntaje_total DECIMAL(5,2) NULL AFTER inteligencia_predominante'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- tiempo_inicio
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'tiempo_inicio'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN tiempo_inicio DATETIME NULL AFTER puntaje_total'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- tiempo_fin
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'tiempo_fin'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN tiempo_fin DATETIME NULL AFTER tiempo_inicio'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- modificado_por
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'modificado_por'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN modificado_por VARCHAR(255) NULL AFTER tiempo_fin'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- notas
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND COLUMN_NAME = 'notas'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD COLUMN notas TEXT NULL AFTER modificado_por'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- Indexes
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND INDEX_NAME = 'idx_estado_guardado'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD INDEX idx_estado_guardado (estado_guardado)'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND INDEX_NAME = 'idx_client_request_id'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD INDEX idx_client_request_id (client_request_id)'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND INDEX_NAME = 'idx_inteligencia_predominante'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD INDEX idx_inteligencia_predominante (inteligencia_predominante)'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND INDEX_NAME = 'idx_fecha_aplicacion'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD INDEX idx_fecha_aplicacion (FechaAplicacion)'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;

-- Unique constraint (creates a unique index under the hood)
SET @stmt = (
  SELECT IF(
    EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND CONSTRAINT_NAME = 'idx_unique_client_request'
    ) OR EXISTS(
      SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'TestsGardner' AND INDEX_NAME = 'idx_unique_client_request'
    ),
    'SELECT 1',
    'ALTER TABLE TestsGardner ADD CONSTRAINT idx_unique_client_request UNIQUE (client_request_id)'
  )
);
PREPARE s FROM @stmt; EXECUTE s; DEALLOCATE PREPARE s;
