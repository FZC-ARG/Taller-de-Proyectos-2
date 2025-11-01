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
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL
);

-- Tabla para Docentes
CREATE TABLE docentes (
    id_docente INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL
);

-- Tabla para Alumnos
CREATE TABLE alumnos (
    id_alumno INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL
);

-- -----------------------------------------------------
-- 2. GESTIÓN DE CURSOS
-- -----------------------------------------------------

CREATE TABLE cursos (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso VARCHAR(150) NOT NULL,
    descripcion TEXT,
    id_docente_fk INT NOT NULL,
    FOREIGN KEY (id_docente_fk) REFERENCES docentes(id_docente)
);

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



-- -----------------------------------------------------
-- DATOS PREGUNTAS
-- -----------------------------------------------------
-- DATOS PARA LOS TIPOS DE INTELIGENCIA.
INSERT INTO tipos_inteligencia (id_inteligencia, nombre, descripcion) VALUES
(1, 'Lógico-Matemática', 'Capacidad para el razonamiento lógico, el reconocimiento de patrones y la resolución de problemas matemáticos.'),
(2, 'Lingüístico-Verbal', 'Habilidad para utilizar el lenguaje de manera efectiva, ya sea oral o escrita.'),
(3, 'Espacial-Visual', 'Capacidad para pensar en tres dimensiones y visualizar con precisión.'),
(4, 'Corporal-Kinestésica', 'Capacidad para utilizar el cuerpo para expresar ideas y sentimientos, así como para realizar actividades físicas.'),
(5, 'Musical', 'Habilidad para percibir, discriminar, transformar y expresar las formas musicales.'),
(6, 'Intrapersonal', 'Capacidad para comprenderse a uno mismo, sus pensamientos y sentimientos, y usar ese conocimiento para guiar su vida.'),
(7, 'Interpersonal', 'Habilidad para entender y relacionarse eficazmente con otras personas.'),
(8, 'Naturalista', 'Habilidad para identificar, clasificar y manipular elementos del medio ambiente, objetos, animales o plantas.');

-- DATOS DE LAS PREGUNTAS
INSERT INTO preguntas_test (id_pregunta, id_inteligencia_fk, texto_pregunta) VALUES
-- Inteligencia Lógico-Matemática (1)
(1, 1, '¿Es capaz de reconocer y comprender causa y efecto con relación a su edad?'),
(5, 1, '¿Se cuestiona acerca del funcionamiento de las cosas?'),
(13, 1, '¿Es capaz de resolver problemas de matemáticas mentalmente y con rapidez?'),
(21, 1, '¿Disfruta las clases de matemáticas?'),
(25, 1, '¿Le agrada clasificar y jerarquizar cosas?'),
(28, 1, '¿Encuentra placer al desarmar y volver a armar las cosas?'),
(29, 1, '¿Encuentra placer resolviendo juego de matemáticas en la computadora?'),
(33, 1, '¿Es capaz de resolver juegos que requieren lógica?'),
(37, 1, '¿Le gustan los juegos de mesa?'),

-- Inteligencia Lingüístico-Verbal (2)
(4, 2, '¿Se expresa en forma dramática?'),
(49, 2, '¿Suele crear y/o relatar cuentos bromas y chistes?'),
(41, 2, '¿Se comunica con los demás de una manera marcadamente verbal?'),
(65, 2, '¿Comprende y goza de los juegos de palabras?'),
(69, 2, '¿Sus producciones escritas son las esperadas para su edad?'),
(73, 2, '¿Tiene facilidad para las lenguas extranjeras?'),
(77, 2, '¿Relaciona el contenido del texto con la realidad?'),
(57, 2, '¿Sostiene con argumentos el criterio respecto al texto?'),

-- Inteligencia Espacial-Visual (3)
(48, 3, '¿Comunica imágenes visuales nítidas?'),
(52, 3, '¿Realiza creaciones tridimensionales avanzadas para su edad?'),
(56, 3, '¿Tiene facilidad para la lectura de mapas, gráficos y diagramas?'),
(60, 3, '¿Le agrada resolver actividades visuales (rompecabezas, laberintos, etc.)?'),
(68, 3, '¿Disfruta viendo películas, diapositivas y otras presentaciones visuales?'),
(76, 3, '¿Dibuja figuras avanzadas para su edad?'),
(80, 3, '¿Puede orientarse fácilmente en un lugar al que va por primera vez?'),
(44, 3, '¿Realiza garabatos en sus libros y otros materiales de trabajo?'),

-- Inteligencia Corporal-Kinestésica (4)
(12, 4, '¿Sobresale en la práctica de uno o más deportes?'),
(20, 4, '¿Suele moverse, estar inquieto al estar sentado por largo tiempo?'),
(24, 4, '¿Demuestra destreza en actividades que requieren de coordinación motora sutil?'),
(32, 4, '¿Es bueno imitando los movimientos típicos y gestos de otra persona?'),
(40, 4, '¿Me considero una persona coordinada?'),
(8, 4, '¿Encuentra placer al realizar las experiencias táctiles (plastilina, cerámica, macilla, etc.)?'),
(36, 4, '¿Suele tocar las cosas con las manos apenas las ve?'),
(72, 4, '¿Le gusta realizar actividades de arte?'),

-- Inteligencia Musical (5)
(2, 5, '¿Suele cantar canciones que no han sido aprendidas en clase?'),
(6, 5, '¿Identifica la música desentonada o que suena mal?'),
(10, 5, '¿Disfruta escuchar música?'),
(14, 5, '¿Tiene buena memoria para las melodías de las canciones?'),
(22, 5, '¿Tiene buena voz para cantar?'),
(26, 5, '¿Tamborilea rítmicamente sobre la mesa o escritorio mientras está trabajando?'),
(30, 5, '¿Posee algún instrumento musical que sepa tocar?'),
(34, 5, '¿Canta sin darse cuenta?'),
(38, 5, '¿Habla o se mueve rítmicamente?'),

-- Inteligencia Intrapersonal (6)
(3, 6, '¿Parece tener un gran amor propio?'),
(15, 6, '¿Posee un concepto práctico de sus habilidades y debilidades?'),
(19, 6, '¿Es capaz de expresar sus sentimientos acertadamente?'),
(23, 6, '¿Tiene un buen desempeño cuando trabaja solo?'),
(27, 6, '¿Le gusta más trabajar solo que en grupo?'),
(35, 6, '¿Posee un buen sentido de autodirección?'),
(39, 6, '¿Se interesa por un pasatiempo sobre el cual no habla mucho a los demás?'),
(11, 6, '¿Utiliza sus errores y logros de la vida para aprender de ellos?'),

-- Inteligencia Interpersonal (7)
(45, 7, '¿Respeta los turnos en la conversación, cede la palabra?'),
(46, 7, '¿Le gusta hablar con sus compañeros?'),
(50, 7, '¿Es empático y/o se interesa por los demás?'),
(54, 7, '¿Demuestra ser un líder por naturaleza?'),
(58, 7, '¿Posee dos o más buenos amigos?'),
(61, 7, '¿Tiene don de convencimiento, facilidad de palabras?'),
(62, 7, '¿Es capaz de aconsejar a sus compañeros que tienen problemas?'),
(66, 7, '¿Disfruta jugando con otros jóvenes?'),
(74, 7, '¿Forma parte de algún club o grupo social?'),
(78, 7, '¿Le gusta participar en actividades de ayuda comunitaria?'),
(42, 7, '¿Sus compañeros buscan estar con usted?'),

-- Inteligencia Naturalista (8)
(18, 8, '¿Demuestra sensibilidad ante los ruidos del ambiente?'),
(43, 8, '¿Es sensible con las criaturas del mundo natural?'),
(47, 8, '¿Le gusta vivir en el campo?'),
(51, 8, '¿Puede reconocer patrones en la naturaleza?'),
(59, 8, '¿Disfruta y se interesa por la naturaleza?'),
(63, 8, '¿Se siente identificado como parte de la naturaleza?'),
(67, 8, '¿Tiene mascota favorita?'),
(71, 8, '¿Le agradan las flores y las plantas?'),
(75, 8, '¿Disfruta ver alimentos que la tierra produce?'),
(79, 8, '¿Reconoce y clasifica las diferentes especies?');
