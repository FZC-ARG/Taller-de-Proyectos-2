-- -----------------------------------------------------
-- Creación de la Base de Datos
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `prmartin` DEFAULT CHARACTER SET utf8mb4 ;
USE `prmartin` ;

-- -----------------------------------------------------
-- 1. GESTIÓN DE USUARIOS
-- Como solicitaste, tres tablas separadas para cada rol.
-- -----------------------------------------------------

-- Tabla para Administradores
CREATE TABLE administradores (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    -- La contraseña será hasheada por el backend
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
    -- Este es el nombre real/completo para mostrar
    nombre_completo VARCHAR(255) NOT NULL,
    -- Este es el nombre de usuario único para iniciar sesión
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

-- -----------------------------------------------------
-- 2. ESTRUCTURA DEL TEST
-- Definición de las inteligencias y sus preguntas.
-- -----------------------------------------------------

-- Tabla para los tipos de inteligencia (Lógico-Matemática, Lingüística, etc.)
CREATE TABLE tipos_inteligencia (
    id_inteligencia INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla para almacenar cada pregunta del test
CREATE TABLE preguntas_test (
    id_pregunta INT AUTO_INCREMENT PRIMARY KEY,
    -- Cada pregunta pertenece a un tipo de inteligencia
    id_inteligencia_fk INT NOT NULL,
    texto_pregunta TEXT NOT NULL,
    FOREIGN KEY (id_inteligencia_fk) REFERENCES tipos_inteligencia(id_inteligencia)
);

-- -----------------------------------------------------
-- 3. REALIZACIÓN Y RESULTADOS DEL TEST
-- Aquí guardamos las respuestas y los puntajes calculados.
-- -----------------------------------------------------

-- Esta tabla registra cada vez que un alumno completa un test (un "intento")
-- Esto permite el historial de pruebas [cite: 114]
CREATE TABLE intentos_test (
    id_intento INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno_fk INT NOT NULL,
    fecha_realizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_alumno_fk) REFERENCES alumnos(id_alumno)
);

-- Aquí se guarda la respuesta (1-5) que dio el alumno a CADA pregunta
CREATE TABLE respuestas_alumno (
    id_respuesta INT AUTO_INCREMENT PRIMARY KEY,
    id_intento_fk INT NOT NULL,
    id_pregunta_fk INT NOT NULL,
    -- El puntaje de 1 a 5 que dio el alumno
    puntaje TINYINT NOT NULL,
    FOREIGN KEY (id_intento_fk) REFERENCES intentos_test(id_intento),
    FOREIGN KEY (id_pregunta_fk) REFERENCES preguntas_test(id_pregunta)
);

-- Aquí se guarda el puntaje FINAL (calculado por el backend)
-- por cada tipo de inteligencia y por cada intento.
CREATE TABLE resultados_test (
    id_resultado INT AUTO_INCREMENT PRIMARY KEY,
    id_intento_fk INT NOT NULL,
    id_inteligencia_fk INT NOT NULL,
    -- El puntaje total o promedio que el backend calculó
    puntaje_calculado FLOAT NOT NULL,
    FOREIGN KEY (id_intento_fk) REFERENCES intentos_test(id_intento),
    FOREIGN KEY (id_inteligencia_fk) REFERENCES tipos_inteligencia(id_inteligencia)
);

-- -----------------------------------------------------
-- 4. REGISTRO Y AUDITORÍA
-- Log de accesos, como se menciona en la HU 1.8 
-- -----------------------------------------------------

CREATE TABLE log_accesos (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    -- El ID del usuario que accedió (puede ser de admin, docente o alumno)
    id_usuario INT NOT NULL,
    -- Un ENUM para saber qué tipo de usuario era
    tipo_usuario ENUM('admin', 'docente', 'alumno') NOT NULL,
    fecha_hora_acceso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------
-- 5. CHAT CON IA (DOCENTES)
-- Satisface el requisito de tener chats generales y por alumno.
-- -----------------------------------------------------

-- Una "sesión" o "conversación" iniciada por un docente.
CREATE TABLE chat_sesiones (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_docente_fk INT NOT NULL,
    -- Si es un chat general, id_alumno_fk será NULL.
    -- Si es un chat sobre un alumno, aquí va su ID.
    id_alumno_fk INT NULL,
    titulo_sesion VARCHAR(255) NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_docente_fk) REFERENCES docentes(id_docente),
    FOREIGN KEY (id_alumno_fk) REFERENCES alumnos(id_alumno)
);

-- Los mensajes dentro de cada sesión [cite: 133]
CREATE TABLE chat_mensajes (
    id_mensaje INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion_fk INT NOT NULL,
    -- Para saber si el mensaje es del docente o de la IA
    emisor ENUM('docente', 'ia') NOT NULL,
    contenido TEXT NOT NULL,
    fecha_hora_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_sesion_fk) REFERENCES chat_sesiones(id_sesion)
    -- El backend se encargará de la lógica de "editar" (UPDATE)
    -- y "borrar" (DELETE) mensajes.
);
