-- Datos de prueba para AppMartin Backend
-- Ejecutar después de las correcciones para probar los endpoints

-- Insertar roles si no existen
INSERT IGNORE INTO roles (IdRol, NombreRol, nombre_rol) VALUES 
(1, 'ADMIN', 'ADMIN'),
(2, 'DOCENTE', 'DOCENTE'),
(3, 'ALUMNO', 'ALUMNO');

-- Insertar usuario administrador de prueba
INSERT IGNORE INTO usuarios (
    IdUsuario, 
    nombre_usuario, 
    correo_electronico, 
    contrasena_hash, 
    id_rol, 
    fecha_creacion, 
    activo
) VALUES (
    1, 
    'admin', 
    'admin@sistema.com', 
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz2e', -- password: 123456
    1, 
    NOW(), 
    1
);

-- Insertar usuario docente de prueba
INSERT IGNORE INTO usuarios (
    IdUsuario, 
    nombre_usuario, 
    correo_electronico, 
    contrasena_hash, 
    id_rol, 
    fecha_creacion, 
    activo
) VALUES (
    2, 
    'docente1', 
    'docente1@sistema.com', 
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz2e', -- password: 123456
    2, 
    NOW(), 
    1
);

-- Insertar usuario alumno de prueba
INSERT IGNORE INTO usuarios (
    IdUsuario, 
    nombre_usuario, 
    correo_electronico, 
    contrasena_hash, 
    id_rol, 
    fecha_creacion, 
    activo
) VALUES (
    3, 
    'alumno1', 
    'alumno1@sistema.com', 
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz2e', -- password: 123456
    3, 
    NOW(), 
    1
);

-- Insertar docente
INSERT IGNORE INTO docentes (IdDocente, IdUsuario, Especialidad) VALUES 
(1, 2, 'Matemáticas');

-- Insertar alumno
INSERT IGNORE INTO alumnos (IdAlumno, IdUsuario, AnioIngreso) VALUES 
(1, 3, 2024);

-- Insertar curso de prueba
INSERT IGNORE INTO cursos (IdCurso, NombreCurso, IdDocente) VALUES 
(1, 'Matemáticas I', 1);

-- Insertar matrícula de prueba
INSERT IGNORE INTO matriculas (IdMatricula, IdAlumno, IdCurso, FechaMatricula) VALUES 
(1, 1, 1, CURDATE());

-- Verificar datos insertados
SELECT 'Usuarios:' as Tabla, COUNT(*) as Total FROM usuarios
UNION ALL
SELECT 'Roles:', COUNT(*) FROM roles
UNION ALL
SELECT 'Alumnos:', COUNT(*) FROM alumnos
UNION ALL
SELECT 'Docentes:', COUNT(*) FROM docentes
UNION ALL
SELECT 'Cursos:', COUNT(*) FROM cursos
UNION ALL
SELECT 'Matrículas:', COUNT(*) FROM matriculas;
