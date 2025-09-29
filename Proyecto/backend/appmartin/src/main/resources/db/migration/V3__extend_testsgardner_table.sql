-- Migration V3: Extend TestsGardner table with additional columns for autosave and better tracking
-- Project: Sistema de Gestión Académica con IA - Test Gardner Module
-- Date: December 2024

-- Add new columns to existing TestsGardner table
ALTER TABLE TestsGardner 
ADD COLUMN estado_guardado ENUM('BORRADOR', 'FINAL', 'CALCULADO') DEFAULT 'BORRADOR' AFTER Puntajes,
ADD COLUMN version_guardado INT DEFAULT 1 AFTER estado_guardado,
ADD COLUMN client_request_id VARCHAR(255) NULL AFTER version_guardado,
ADD COLUMN inteligencia_predominante VARCHAR(100) NULL AFTER client_request_id,
ADD COLUMN puntaje_total DECIMAL(5,2) NULL AFTER inteligencia_predominante,
ADD COLUMN tiempo_inicio DATETIME NULL AFTER puntaje_total,
ADD COLUMN tiempo_fin DATETIME NULL AFTER tiempo_inicio,
ADD COLUMN modificado_por VARCHAR(255) NULL AFTER tiempo_fin,
ADD COLUMN notas TEXT NULL AFTER modificado_por,
ADD INDEX idx_estado_guardado (estado_guardado),
ADD INDEX idx_client_request_id (client_request_id),
ADD INDEX idx_inteligencia_predominante (inteligencia_predominante),
ADD INDEX idx_fecha_aplicacion (FechaAplicacion);

-- Add unique constraint for client request ID to prevent duplicates
ALTER TABLE TestsGardner ADD CONSTRAINT UNIQUE idx_unique_client_request UNIQUE (client_request_id);
