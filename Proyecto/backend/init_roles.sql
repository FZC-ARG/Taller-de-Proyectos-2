-- Script para inicializar roles básicos en AppMartin
-- Ejecutar en H2 Console: http://localhost:8081/h2-console

-- Crear tabla roles si no existe
CREATE TABLE IF NOT EXISTS roles (
    IdRol INT AUTO_INCREMENT PRIMARY KEY,
    NombreRol VARCHAR(20) NOT NULL UNIQUE
);

-- Insertar roles básicos
INSERT INTO roles (NombreRol) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE NombreRol = NombreRol;
INSERT INTO roles (NombreRol) VALUES ('DOCENTE') ON DUPLICATE KEY UPDATE NombreRol = NombreRol;
INSERT INTO roles (NombreRol) VALUES ('ALUMNO') ON DUPLICATE KEY UPDATE NombreRol = NombreRol;

-- Verificar inserción
SELECT * FROM roles;
