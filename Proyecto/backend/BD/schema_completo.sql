-- Script completo para la base de datos del proyecto
-- Universidad: Taller de Proyectos 2
-- Proyecto: Sistema de Gestión Académica con IA

CREATE DATABASE IF NOT EXISTS prmartin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE prmartin;

-- =============================================
-- TABLAS PRINCIPALES
-- =============================================

-- Tabla Roles
CREATE TABLE IF NOT EXISTS Roles (
    IdRol INT AUTO_INCREMENT PRIMARY KEY,
    NombreRol VARCHAR(20) NOT NULL UNIQUE
);

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS Usuarios (
    IdUsuario INT AUTO_INCREMENT PRIMARY KEY,
    NombreUsuario VARCHAR(80) NOT NULL,
    CorreoElectronico VARCHAR(150) NOT NULL UNIQUE,
    ContrasenaHash VARCHAR(255) NOT NULL,
    IdRol INT NOT NULL,
    FechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (NombreUsuario),
    FOREIGN KEY (IdRol) REFERENCES Roles(IdRol)
);

-- Tabla Alumnos
CREATE TABLE IF NOT EXISTS Alumnos (
    IdAlumno INT AUTO_INCREMENT PRIMARY KEY,
    IdUsuario INT NOT NULL UNIQUE,
    AnioIngreso INT NOT NULL,
    FOREIGN KEY (IdUsuario) REFERENCES Usuarios(IdUsuario)
);

-- Tabla Docentes
CREATE TABLE IF NOT EXISTS Docentes (
    IdDocente INT AUTO_INCREMENT PRIMARY KEY,
    IdUsuario INT NOT NULL UNIQUE,
    Especialidad VARCHAR(100) NOT NULL,
    FOREIGN KEY (IdUsuario) REFERENCES Usuarios(IdUsuario)
);

-- Tabla Cursos
CREATE TABLE IF NOT EXISTS Cursos (
    IdCurso INT AUTO_INCREMENT PRIMARY KEY,
    NombreCurso VARCHAR(150) NOT NULL,
    IdDocente INT NOT NULL,
    FOREIGN KEY (IdDocente) REFERENCES Docentes(IdDocente)
);

-- Tabla Matriculas
CREATE TABLE IF NOT EXISTS Matriculas (
    IdMatricula INT AUTO_INCREMENT PRIMARY KEY,
    IdAlumno INT NOT NULL,
    IdCurso INT NOT NULL,
    FechaMatricula DATE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (IdAlumno, IdCurso),
    FOREIGN KEY (IdAlumno) REFERENCES Alumnos(IdAlumno),
    FOREIGN KEY (IdCurso) REFERENCES Cursos(IdCurso)
);

-- Tabla Calificaciones
CREATE TABLE IF NOT EXISTS Calificaciones (
    IdCalificacion INT AUTO_INCREMENT PRIMARY KEY,
    IdMatricula INT NOT NULL,
    Periodo VARCHAR(20) NOT NULL,
    Nota DECIMAL(5,2) NOT NULL CHECK (Nota >= 0 AND Nota <= 20),
    FechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (IdMatricula) REFERENCES Matriculas(IdMatricula)
);

-- Tabla Auditoria
CREATE TABLE IF NOT EXISTS Auditoria (
    IdAuditoria INT AUTO_INCREMENT PRIMARY KEY,
    IdUsuario INT,
    Accion VARCHAR(150) NOT NULL,
    Entidad VARCHAR(50) NOT NULL,
    IdEntidad INT,
    Detalles TEXT,
    DireccionIP VARCHAR(50),
    FechaEvento DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (IdUsuario) REFERENCES Usuarios(IdUsuario)
);

-- Tabla SolicitudesIA
CREATE TABLE IF NOT EXISTS SolicitudesIA (
    IdSolicitud INT AUTO_INCREMENT PRIMARY KEY,
    IdDocente INT NOT NULL,
    IdCurso INT NOT NULL,
    IdAlumno INT,
    DatosEntrada TEXT NOT NULL,
    Estado VARCHAR(20) DEFAULT 'PENDIENTE',
    FechaSolicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (IdDocente) REFERENCES Docentes(IdDocente),
    FOREIGN KEY (IdCurso) REFERENCES Cursos(IdCurso),
    FOREIGN KEY (IdAlumno) REFERENCES Alumnos(IdAlumno)
);

-- Tabla RespuestasIA
CREATE TABLE IF NOT EXISTS RespuestasIA (
    IdRespuesta INT AUTO_INCREMENT PRIMARY KEY,
    IdSolicitud INT NOT NULL,
    RespuestaJSON TEXT NOT NULL,
    ModeloIA VARCHAR(100),
    Confianza DECIMAL(5,2),
    FechaRespuesta DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (IdSolicitud) REFERENCES SolicitudesIA(IdSolicitud)
);

-- Tabla TestsGardner
CREATE TABLE IF NOT EXISTS TestsGardner (
    IdTest INT AUTO_INCREMENT PRIMARY KEY,
    IdAlumno INT NOT NULL,
    Respuestas TEXT,
    Puntajes TEXT,
    FechaAplicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (IdAlumno) REFERENCES Alumnos(IdAlumno)
);

-- =============================================
-- DATOS INICIALES
-- =============================================

-- Insertar roles básicos
INSERT IGNORE INTO Roles (NombreRol) VALUES 
('ADMIN'),
('DOCENTE'),
('ALUMNO');

-- Insertar usuarios de prueba (contraseña: password123)
INSERT IGNORE INTO Usuarios (NombreUsuario, CorreoElectronico, ContrasenaHash, IdRol) VALUES 
-- Administradores
('admin1', 'admin1@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 1),
('admin2', 'admin2@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 1),
-- Docentes
('prof.martinez', 'martinez@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 2),
('prof.garcia', 'garcia@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 2),
('prof.lopez', 'lopez@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 2),
-- Alumnos
('alumno001', 'alumno001@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 3),
('alumno002', 'alumno002@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 3),
('alumno003', 'alumno003@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 3),
('alumno004', 'alumno004@prmartin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 3);

-- Insertar docentes
INSERT IGNORE INTO Docentes (IdUsuario, Especialidad) VALUES 
(3, 'Inteligencia Artificial'),
(4, 'Desarrollo de Software'),
(5, 'Base de Datos');

-- Insertar alumnos
INSERT IGNORE INTO Alumnos (IdUsuario, AnioIngreso) VALUES 
(6, 2023),
(7, 2023),
(8, 2024),
(9, 2024);

-- Insertar cursos
INSERT IGNORE INTO Cursos (NombreCurso, IdDocente) VALUES 
('Inteligencia Artificial Avanzada', 1),
('Desarrollo Web con Spring Boot', 2),
('Diseño de Base de Datos', 3),
('Machine Learning', 1),
('Programación Orientada a Objetos', 2);

-- Insertar matrículas de ejemplo
INSERT IGNORE INTO Matriculas (IdAlumno, IdCurso) VALUES 
(1, 1), (1, 2), (1, 3),
(2, 1), (2, 4),
(3, 2), (3, 3), (3, 5),
(4, 1), (4, 2), (4, 4);

-- Insertar calificaciones de ejemplo
INSERT IGNORE INTO Calificaciones (IdMatricula, Periodo, Nota) VALUES 
(1, 'Q1-2024', 18.5),
(1, 'Q2-2024', 17.0),
(2, 'Q1-2024', 16.5),
(2, 'Q2-2024', 18.0),
(3, 'Q1-2024', 19.0),
(4, 'Q1-2024', 17.5),
(4, 'Q2-2024', 18.5),
(5, 'Q1-2024', 16.0),
(6, 'Q1-2024', 18.0),
(6, 'Q2-2024', 17.5);

-- Insertar solicitudes de IA de ejemplo
INSERT IGNORE INTO SolicitudesIA (IdDocente, IdCurso, IdAlumno, DatosEntrada, Estado) VALUES 
(1, 1, 1, '{"pregunta": "¿Cómo optimizar un algoritmo de machine learning?", "contexto": "estudiante_intermedio"}', 'COMPLETADA'),
(1, 1, 2, '{"pregunta": "Explicar redes neuronales", "contexto": "estudiante_principiante"}', 'PENDIENTE'),
(2, 2, 3, '{"pregunta": "Mejorar rendimiento de Spring Boot", "contexto": "proyecto_real"}', 'EN_PROCESO');

-- Insertar respuestas de IA de ejemplo
INSERT IGNORE INTO RespuestasIA (IdSolicitud, RespuestaJSON, ModeloIA, Confianza) VALUES 
(1, '{"respuesta": "Para optimizar algoritmos de ML: 1) Normalizar datos, 2) Ajustar hiperparámetros, 3) Usar validación cruzada", "ejemplos": ["GridSearch", "RandomSearch"]}', 'GPT-4', 0.95);

-- Insertar tests de Gardner de ejemplo
INSERT IGNORE INTO TestsGardner (IdAlumno, Respuestas, Puntajes) VALUES 
(1, '{"musical": 8, "logico": 9, "espacial": 7, "linguistico": 8, "corporal": 6, "interpersonal": 7, "intrapersonal": 8, "naturalista": 6}', '{"musical": 80, "logico": 90, "espacial": 70, "linguistico": 80, "corporal": 60, "interpersonal": 70, "intrapersonal": 80, "naturalista": 60}'),
(2, '{"musical": 6, "logico": 8, "espacial": 9, "linguistico": 7, "corporal": 8, "interpersonal": 9, "intrapersonal": 7, "naturalista": 8}', '{"musical": 60, "logico": 80, "espacial": 90, "linguistico": 70, "corporal": 80, "interpersonal": 90, "intrapersonal": 70, "naturalista": 80}');

-- =============================================
-- VERIFICACIÓN
-- =============================================

SELECT 'Base de datos creada exitosamente' as Status;
SELECT 'Tablas creadas:' as Info;
SHOW TABLES;

SELECT 'Datos insertados:' as Info;
SELECT 'Roles:', COUNT(*) FROM Roles;
SELECT 'Usuarios:', COUNT(*) FROM Usuarios;
SELECT 'Docentes:', COUNT(*) FROM Docentes;
SELECT 'Alumnos:', COUNT(*) FROM Alumnos;
SELECT 'Cursos:', COUNT(*) FROM Cursos;
SELECT 'Matrículas:', COUNT(*) FROM Matriculas;
SELECT 'Calificaciones:', COUNT(*) FROM Calificaciones;
