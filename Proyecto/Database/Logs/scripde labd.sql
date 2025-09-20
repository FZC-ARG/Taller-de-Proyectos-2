-- Script simple para crear las tablas necesarias
CREATE DATABASE IF NOT EXISTS prmartin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE prmartin;

-- Tabla de Roles
CREATE TABLE IF NOT EXISTS Roles (
    IdRol INT AUTO_INCREMENT PRIMARY KEY,
    NombreRol VARCHAR(20) NOT NULL UNIQUE
);

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS Usuarios (
    IdUsuario INT AUTO_INCREMENT PRIMARY KEY,
    NombreUsuario VARCHAR(80) NOT NULL UNIQUE,
    CorreoElectronico VARCHAR(150) NOT NULL UNIQUE,
    ContrasenaHash VARCHAR(255) NOT NULL,
    IdRol INT NOT NULL,
    FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (IdRol) REFERENCES Roles(IdRol)
);

-- Tabla de Administradores
CREATE TABLE IF NOT EXISTS Administradores (
    IdAdministrador INT AUTO_INCREMENT PRIMARY KEY,
    NombreCompleto VARCHAR(100) NOT NULL,
    Usuario VARCHAR(50) NOT NULL UNIQUE,
    CorreoElectronico VARCHAR(150) NOT NULL UNIQUE,
    ContrasenaHash VARCHAR(255) NOT NULL,
    Activo BOOLEAN NOT NULL DEFAULT TRUE,
    FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    UltimoAcceso DATETIME NULL,
    NivelPrivilegio INT NOT NULL DEFAULT 1
);

-- Insertar roles básicos
INSERT IGNORE INTO Roles (NombreRol) VALUES 
('ADMIN'),
('DOCENTE'),
('ALUMNO');

-- Insertar administradores iniciales (contraseñas: admin123, admin456, admin789)
INSERT IGNORE INTO Administradores (NombreCompleto, Usuario, CorreoElectronico, ContrasenaHash, Activo, NivelPrivilegio) VALUES 
('Administrador Principal', 'admin1', 'admin1@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE, 1),
('Administrador Secundario', 'admin2', 'admin2@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE, 1),
('Administrador Terciario', 'admin3', 'admin3@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', TRUE, 1);

-- Mostrar información
SELECT 'Base de datos y tablas creadas exitosamente' as Status;
SHOW TABLES;
