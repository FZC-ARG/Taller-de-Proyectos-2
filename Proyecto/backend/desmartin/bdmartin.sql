-- -----------------------------------------------------
-- Creación de la Base de Datos
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `prmartin` DEFAULT CHARACTER SET utf8mb4;
USE `prmartin`;

-- -----------------------------------------------------
-- 1. GESTIÓN DE USUARIOS
-- -----------------------------------------------------

-- Tabla para Administradores
CREATE TABLE administradores (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

-- Tabla para Docentes
CREATE TABLE docentes (
    id_docente INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

-- Tabla para Alumnos
CREATE TABLE alumnos (
    id_alumno INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

-- -----------------------------------------------------
-- 2. GESTIÓN DE CURSOS
-- -----------------------------------------------------

-- Tabla de cursos dictados por docentes
CREATE TABLE cursos (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso VARCHAR(150) NOT NULL,
    descripcion TEXT,
    id_docente_fk INT NOT NULL,
    FOREIGN KEY (id_docente_fk) REFERENCES docentes(id_docente)
);

-- Relación muchos a muchos entre alumnos y cursos
CREATE TABLE alumnos_cursos (
    id_alumno_fk INT NOT NULL,
    id_curso_fk INT NOT NULL,
    fecha_matricula DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_alumno_fk, id_curso_fk),
    FOREIGN KEY (id_alumno_fk) REFERENCES alumnos(id_alumno),
    FOREIGN KEY (id_curso_fk) REFERENCES cursos(id_curso)
);

-- -----------------------------------------------------
-- 3. TEST DE INTELIGENCIAS
-- -----------------------------------------------------

CREATE TABLE tipos_inteligencia (
    id_inteligencia INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE preguntas_test (
    id_pregunta INT AUTO_INCREMENT PRIMARY KEY,
    id_inteligencia_fk INT NOT NULL,
    texto_pregunta TEXT NOT NULL,
    FOREIGN KEY (id_inteligencia_fk) REFERENCES tipos_inteligencia(id_inteligencia)
);

CREATE TABLE intentos_test (
    id_intento INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno_fk INT NOT NULL,
    fecha_realizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_alumno_fk) REFERENCES alumnos(id_alumno)
);

CREATE TABLE respuestas_alumno (
    id_respuesta INT AUTO_INCREMENT PRIMARY KEY,
    id_intento_fk INT NOT NULL,
    id_pregunta_fk INT NOT NULL,
    puntaje TINYINT NOT NULL,
    FOREIGN KEY (id_intento_fk) REFERENCES intentos_test(id_intento),
    FOREIGN KEY (id_pregunta_fk) REFERENCES preguntas_test(id_pregunta)
);

CREATE TABLE resultados_test (
    id_resultado INT AUTO_INCREMENT PRIMARY KEY,
    id_intento_fk INT NOT NULL,
    id_inteligencia_fk INT NOT NULL,
    puntaje_calculado FLOAT NOT NULL,
    FOREIGN KEY (id_intento_fk) REFERENCES intentos_test(id_intento),
    FOREIGN KEY (id_inteligencia_fk) REFERENCES tipos_inteligencia(id_inteligencia)
);

-- -----------------------------------------------------
-- 4. REGISTRO DE ACCESOS / AUDITORÍA
-- -----------------------------------------------------

CREATE TABLE log_accesos (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    tipo_usuario ENUM('admin', 'docente', 'alumno') NOT NULL,
    fecha_hora_acceso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------
-- 5. CHAT CON IA (DOCENTES)
-- -----------------------------------------------------

CREATE TABLE chat_sesiones (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_docente_fk INT NOT NULL,
    id_alumno_fk INT NULL,
    titulo_sesion VARCHAR(255) NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_docente_fk) REFERENCES docentes(id_docente),
    FOREIGN KEY (id_alumno_fk) REFERENCES alumnos(id_alumno)
);

CREATE TABLE chat_mensajes (
    id_mensaje INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion_fk INT NOT NULL,
    emisor ENUM('docente', 'ia') NOT NULL,
    contenido TEXT NOT NULL,
    fecha_hora_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_sesion_fk) REFERENCES chat_sesiones(id_sesion)
);
