-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-10-2025 a las 19:09:53
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `prmartin`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `accesosusuarios`
--

CREATE TABLE `accesosusuarios` (
  `id_acceso` int(11) NOT NULL,
  `fecha_acceso` datetime(6) DEFAULT NULL,
  `id_usuario` int(11) DEFAULT NULL,
  `resultado` varchar(20) DEFAULT NULL,
  `rolUsuario` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos`
--

CREATE TABLE `alumnos` (
  `IdAlumno` int(11) NOT NULL,
  `IdUsuario` int(11) NOT NULL,
  `AnioIngreso` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auditoria`
--

CREATE TABLE `auditoria` (
  `IdAuditoria` int(11) NOT NULL,
  `IdUsuario` int(11) DEFAULT NULL,
  `Accion` varchar(150) NOT NULL,
  `Entidad` varchar(50) NOT NULL,
  `IdEntidad` int(11) DEFAULT NULL,
  `Detalles` text DEFAULT NULL,
  `DireccionIP` varchar(50) DEFAULT NULL,
  `FechaEvento` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `calificaciones`
--

CREATE TABLE `calificaciones` (
  `IdCalificacion` int(11) NOT NULL,
  `IdMatricula` int(11) NOT NULL,
  `Periodo` varchar(20) NOT NULL,
  `Nota` decimal(5,2) NOT NULL CHECK (`Nota` >= 0 and `Nota` <= 20),
  `FechaRegistro` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cursos`
--

CREATE TABLE `cursos` (
  `IdCurso` int(11) NOT NULL,
  `NombreCurso` varchar(150) NOT NULL,
  `IdDocente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `docentes`
--

CREATE TABLE `docentes` (
  `IdDocente` int(11) NOT NULL,
  `IdUsuario` int(11) NOT NULL,
  `Especialidad` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `flyway_schema_history`
--

CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `flyway_schema_history`
--

INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
(1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2025-09-21 23:06:46', 0, 1),
(2, '2', 'create preguntas gardner', 'SQL', 'V2__create_preguntas_gardner.sql', -352493240, 'root', '2025-10-03 00:53:42', 102, 1),
(3, '3', 'extend testsgardner table', 'SQL', 'V3__extend_testsgardner_table.sql', -1939879112, 'root', '2025-10-03 00:53:42', 229, 1),
(4, '4', 'add password reset columns', 'SQL', 'V4__add_password_reset_columns.sql', 1345204673, 'root', '2025-10-12 04:32:44', 13, 1),
(5, '5', 'create recommendation system', 'SQL', 'V5__create_recommendation_system.sql', -2064209171, 'root', '2025-10-20 01:00:58', 89, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_recomendaciones`
--

CREATE TABLE `historial_recomendaciones` (
  `id_historial` int(11) NOT NULL,
  `id_alumno` int(11) NOT NULL,
  `id_test` int(11) NOT NULL,
  `id_plantilla` int(11) NOT NULL,
  `inteligencia_predominante` varchar(100) NOT NULL,
  `puntaje_inteligencia` int(11) NOT NULL,
  `recomendacion_generada` text NOT NULL,
  `tipo_recomendacion` enum('academica','extracurricular','carrera','personal') NOT NULL,
  `fecha_generacion` datetime DEFAULT current_timestamp(),
  `fecha_aplicacion` datetime DEFAULT NULL,
  `estado` enum('generada','aplicada','descartada') DEFAULT 'generada',
  `notas` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `matriculas`
--

CREATE TABLE `matriculas` (
  `IdMatricula` int(11) NOT NULL,
  `IdAlumno` int(11) NOT NULL,
  `IdCurso` int(11) NOT NULL,
  `FechaMatricula` date DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `plantillas_recomendaciones`
--

CREATE TABLE `plantillas_recomendaciones` (
  `id_plantilla` int(11) NOT NULL,
  `nombre_plantilla` varchar(100) NOT NULL,
  `tipo_inteligencia` enum('musical','logico_matematico','espacial','linguistico','corporal_cinestesico','interpersonal','intrapersonal','naturalista') NOT NULL,
  `puntaje_minimo` int(11) NOT NULL DEFAULT 0,
  `puntaje_maximo` int(11) NOT NULL DEFAULT 100,
  `contenido_recomendacion` text NOT NULL,
  `tipo_recomendacion` enum('academica','extracurricular','carrera','personal') NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_modificacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `plantillas_recomendaciones`
--

INSERT INTO `plantillas_recomendaciones` (`id_plantilla`, `nombre_plantilla`, `tipo_inteligencia`, `puntaje_minimo`, `puntaje_maximo`, `contenido_recomendacion`, `tipo_recomendacion`, `activo`, `fecha_creacion`, `fecha_modificacion`) VALUES
(1, 'Música Avanzada', 'musical', 80, 100, 'Excelente aptitud musical. Recomendamos: clases de instrumento avanzadas, composición musical, participación en orquestas estudiantiles, exploración de tecnología musical.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(2, 'Música Intermedia', 'musical', 60, 79, 'Buena aptitud musical. Recomendamos: clases de instrumento básicas, apreciación musical, canto coral, actividades rítmicas.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(3, 'Música Básica', 'musical', 40, 59, 'Aptitud musical en desarrollo. Recomendamos: exploración de diferentes géneros musicales, actividades de escucha activa, ritmo básico.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(4, 'Matemáticas Avanzadas', 'logico_matematico', 80, 100, 'Excelente razonamiento lógico-matemático. Recomendamos: matemáticas avanzadas, programación, ciencias exactas, proyectos de investigación.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(5, 'Matemáticas Intermedias', 'logico_matematico', 60, 79, 'Buena capacidad lógica. Recomendamos: matemáticas aplicadas, lógica formal, resolución de problemas complejos.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(6, 'Matemáticas Básicas', 'logico_matematico', 40, 59, 'Capacidad lógica en desarrollo. Recomendamos: fundamentos matemáticos, juegos de lógica, pensamiento crítico.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(7, 'Arte Visual Avanzado', 'espacial', 80, 100, 'Excelente percepción espacial. Recomendamos: arte visual avanzado, diseño gráfico, arquitectura, proyectos espaciales.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(8, 'Arte Visual Intermedio', 'espacial', 60, 79, 'Buena percepción espacial. Recomendamos: arte visual, diseño, geometría aplicada, proyectos creativos.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(9, 'Arte Visual Básico', 'espacial', 40, 59, 'Percepción espacial en desarrollo. Recomendamos: dibujo básico, geometría, actividades visuales.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(10, 'Comunicación Avanzada', 'linguistico', 80, 100, 'Excelente capacidad lingüística. Recomendamos: literatura avanzada, escritura creativa, debates, comunicación profesional.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(11, 'Comunicación Intermedia', 'linguistico', 60, 79, 'Buena capacidad lingüística. Recomendamos: literatura, escritura, expresión oral, idiomas.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(12, 'Comunicación Básica', 'linguistico', 40, 59, 'Capacidad lingüística en desarrollo. Recomendamos: lectura comprensiva, escritura básica, vocabulario.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(13, 'Actividad Física Avanzada', 'corporal_cinestesico', 80, 100, 'Excelente coordinación corporal. Recomendamos: deportes competitivos, danza avanzada, teatro, actividades físicas especializadas.', 'extracurricular', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(14, 'Actividad Física Intermedia', 'corporal_cinestesico', 60, 79, 'Buena coordinación corporal. Recomendamos: deportes recreativos, danza, actividades manuales, expresión corporal.', 'extracurricular', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(15, 'Actividad Física Básica', 'corporal_cinestesico', 40, 59, 'Coordinación corporal en desarrollo. Recomendamos: ejercicios básicos, actividades de movimiento, coordinación.', 'extracurricular', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(16, 'Liderazgo Avanzado', 'interpersonal', 80, 100, 'Excelente capacidad interpersonal. Recomendamos: liderazgo estudiantil, trabajo en equipo avanzado, mediación de conflictos.', 'personal', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(17, 'Liderazgo Intermedio', 'interpersonal', 60, 79, 'Buena capacidad interpersonal. Recomendamos: trabajo en equipo, actividades grupales, comunicación interpersonal.', 'personal', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(18, 'Liderazgo Básico', 'interpersonal', 40, 59, 'Capacidad interpersonal en desarrollo. Recomendamos: actividades grupales básicas, comunicación, colaboración.', 'personal', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(19, 'Autoconocimiento Avanzado', 'intrapersonal', 80, 100, 'Excelente autoconocimiento. Recomendamos: proyectos individuales avanzados, reflexión profunda, autodisciplina.', 'personal', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(20, 'Autoconocimiento Intermedio', 'intrapersonal', 60, 79, 'Buen autoconocimiento. Recomendamos: proyectos individuales, reflexión personal, metas personales.', 'personal', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(21, 'Autoconocimiento Básico', 'intrapersonal', 40, 59, 'Autoconocimiento en desarrollo. Recomendamos: reflexión básica, autoevaluación, objetivos personales.', 'personal', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(22, 'Ciencias Naturales Avanzadas', 'naturalista', 80, 100, 'Excelente conexión con la naturaleza. Recomendamos: biología avanzada, ecología, investigación ambiental, actividades outdoor.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(23, 'Ciencias Naturales Intermedias', 'naturalista', 60, 79, 'Buena conexión con la naturaleza. Recomendamos: biología, ciencias ambientales, actividades al aire libre.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58'),
(24, 'Ciencias Naturales Básicas', 'naturalista', 40, 59, 'Conexión con la naturaleza en desarrollo. Recomendamos: observación natural, actividades outdoor básicas.', 'academica', 1, '2025-10-19 20:00:58', '2025-10-19 20:00:58');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `preguntas_gardner`
--

CREATE TABLE `preguntas_gardner` (
  `id_pregunta` int(11) NOT NULL,
  `texto_pregunta` text NOT NULL,
  `opcion_a` varchar(500) NOT NULL,
  `opcion_b` varchar(500) NOT NULL,
  `opcion_c` varchar(500) NOT NULL,
  `opcion_d` varchar(500) NOT NULL,
  `tipo_inteligencia` enum('musical','logico_matematico','espacial','linguistico','corporal_cinestesico','interpersonal','intrapersonal','naturalista') NOT NULL,
  `orden_secuencia` int(11) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `fecha_modificacion` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `preguntas_gardner`
--

INSERT INTO `preguntas_gardner` (`id_pregunta`, `texto_pregunta`, `opcion_a`, `opcion_b`, `opcion_c`, `opcion_d`, `tipo_inteligencia`, `orden_secuencia`, `activo`, `fecha_creacion`, `fecha_modificacion`) VALUES
(1, '¿Cómo prefieres trabajar cuando necesitas concentrarte?', 'Trabajo mejor con música de fondo', 'Necesito silencio absoluto para concentrarme', 'Me concentro igual con o sin música', 'Solo puedo trabajar con ciertos tipos de música', 'musical', 1, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(2, 'Cuando escuchas una canción nueva, ¿qué te llama más la atención?', 'La melodía y los arreglos musicales', 'La letra y el mensaje', 'El ritmo y la percusión', 'La voz del cantante', 'musical', 2, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(3, '¿Cómo recuerdas mejor la información?', 'Cantando o tarareando', 'Escribiendo notas', 'Haciendo esquemas visuales', 'Repitiendo en voz alta', 'musical', 3, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(4, '¿Qué tipo de actividades disfrutas más en tu tiempo libre?', 'Tocar un instrumento o cantar', 'Leer libros o escuchar podcasts', 'Hacer ejercicios o deporte', 'Meditar o reflexionar', 'musical', 4, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(5, '¿Cómo te gusta resolver problemas complejos?', 'Paso a paso, analizando cada parte', 'Probando diferentes soluciones', 'Pidiendo ayuda a otros', 'Intuitivamente, siguiendo mi instinto', 'logico_matematico', 5, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(6, '¿Qué tipo de juegos te gustan más?', 'Sudoku, crucigramas, puzzles', 'Videojuegos de estrategia', 'Juegos de mesa competitivos', 'Juegos creativos y artísticos', 'logico_matematico', 6, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(7, 'Cuando ves datos y números, ¿qué prefieres hacer?', 'Analizarlos y buscar patrones', 'Crear gráficos y visualizaciones', 'Contarlos y organizarlos', 'Buscar la historia detrás de los números', 'logico_matematico', 7, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(8, '¿Cómo aprendes mejor los procedimientos?', 'Entendiendo la lógica paso a paso', 'Practicando repetidamente', 'Viendo demostraciones visuales', 'Trabajando con otros', 'logico_matematico', 8, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(9, '¿Cómo te orientas mejor en lugares nuevos?', 'Memorizando puntos de referencia', 'Creando mapas mentales', 'Siguiendo direcciones verbales', 'Preguntando siempre', 'espacial', 9, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(10, '¿Qué tipo de arte te gusta más?', 'Pintura y dibujo', 'Escultura y cerámica', 'Fotografía y diseño gráfico', 'Arquitectura y urbanismo', 'espacial', 10, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(11, '¿Cómo organizas mejor tu espacio de trabajo?', 'Todo visible y accesible', 'Minimalista y ordenado', 'Creativo y colorido', 'Funcional y práctico', 'espacial', 11, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(12, '¿Qué te gusta hacer en tu tiempo libre?', 'Jugar videojuegos de acción', 'Hacer manualidades o artesanías', 'Ver documentales', 'Caminar en la naturaleza', 'espacial', 12, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(13, '¿Cómo prefieres comunicar tus ideas?', 'Escribiendo textos detallados', 'Hablando en público', 'Usando metáforas y analogías', 'Contando historias', 'linguistico', 13, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(14, '¿Qué tipo de libros te interesan más?', 'Novelas y literatura', 'Libros técnicos y científicos', 'Biografías y memorias', 'Poesía y textos filosóficos', 'linguistico', 14, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(15, '¿Cómo aprendes mejor idiomas nuevos?', 'Estudiando estructuras gramaticales', 'Practicando conversaciones', 'Escuchando música y medios', 'Experimentando con la cultura', 'linguistico', 15, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(16, '¿Qué disfrutas más en una conversación?', 'Debatir temas complejos', 'Contar historias divertidas', 'Escuchar experiencias de otros', 'Resolver problemas juntos', 'linguistico', 16, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(17, '¿Cómo prefieres aprender algo nuevo?', 'Leyendo instrucciones', 'Practicando directamente', 'Viendo videos tutoriales', 'Trabajando con un mentor', 'corporal_cinestesico', 17, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(18, '¿Qué actividades físicas disfrutas más?', 'Deportes de equipo', 'Actividades individuales', 'Bailar y moverse', 'Actividades al aire libre', 'corporal_cinestesico', 18, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(19, '¿Cómo manejas el estrés mejor?', 'Haciendo ejercicio', 'Hablando con alguien', 'Meditando o respirando', 'Trabajando manualmente', 'corporal_cinestesico', 19, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(20, '¿Qué te motiva más en tu trabajo?', 'Ver resultados tangibles', 'Resolver problemas complejos', 'Ayudar a otras personas', 'Aprender cosas nuevas', 'corporal_cinestesico', 20, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(21, '¿Cómo prefieres trabajar en equipo?', 'Como líder del proyecto', 'Como colaborador activo', 'Como coordinador de tareas', 'Como facilitador de comunicación', 'interpersonal', 21, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(22, '¿Qué disfrutas más en grupo?', 'Organizar eventos y actividades', 'Mediar conflictos y diferencias', 'Motivar y guiar a otros', 'Crear un ambiente positivo', 'interpersonal', 22, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(23, '¿Cómo ayudas mejor a otros cuando tienen problemas?', 'Ofreciendo múltiples opciones', 'Escuchando sin juzgar', 'Compartiendo experiencias similares', 'Guiándolos hacia soluciones', 'interpersonal', 23, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(24, '¿Qué te motiva más en relaciones sociales?', 'Ayudar a otros a crecer', 'Aprender de diferentes perspectivas', 'Crear conexiones significativas', 'Resolver conflictos constructivamente', 'interpersonal', 24, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(25, '¿Cómo tomas decisiones importantes?', 'Analizando pros y contras detalladamente', 'Siguiendo mi intuición', 'Consultando con personas de confianza', 'Necesito tiempo para reflexionar', 'intrapersonal', 25, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(26, '¿Qué te ayuda más a conocerte mejor?', 'Practicar la meditación', 'Escribir un diario personal', 'Tener conversaciones profundas', 'Experimentar situaciones nuevas', 'intrapersonal', 26, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(27, '¿Cómo defines tus metas personales?', 'Analisisando mi situación actual', 'Siguiendo mi instinto', 'Basándome en valores profundos', 'Reflexionando sobre mi propósito', 'intrapersonal', 27, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(28, '¿Qué te motiva más internamente?', 'Alcanzar objetivos personales', 'Crecer y desarrollarme', 'Ayudar a otros a crecer', 'Vivir según mis valores', 'intrapersonal', 28, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(29, '¿Qué te interesa más sobre la naturaleza?', 'Observar animales silvestres', 'Estudiar plantas y ecosistemas', 'Practicar actividades al aire libre', 'Aprender sobre conservación', 'naturalista', 29, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(30, '¿Cómo disfrutas más tu tiempo libre?', 'Excursionismo y senderismo', 'Observar aves o fotografía natural', 'Jardinería o huerto', 'Campaing y actividades outdoor', 'naturalista', 30, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(31, '¿Qué preocupación ambiental te motiva más?', 'Cambio climático y sostenibilidad', 'Biodiversidad y especies en peligro', 'Contaminación y salud ambiental', 'Educación ambiental en comunidades', 'naturalista', 31, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42'),
(32, '¿Cómo prefieres aprender sobre ciencia?', 'Experimentando y observando directamente', 'Lectura de investigación', 'Documentales y videos', 'Trabajando en laboratorios', 'naturalista', 32, 1, '2025-10-02 19:53:42', '2025-10-02 19:53:42');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `refreshtokens`
--

CREATE TABLE `refreshtokens` (
  `IdRefreshToken` int(11) NOT NULL,
  `IdUsuario` int(11) NOT NULL,
  `Token` varchar(255) NOT NULL,
  `IssuedAt` datetime DEFAULT NULL,
  `ExpiresAt` datetime NOT NULL,
  `Revoked` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `respuestasia`
--

CREATE TABLE `respuestasia` (
  `IdRespuesta` int(11) NOT NULL,
  `IdSolicitud` int(11) NOT NULL,
  `RespuestaJSON` text NOT NULL,
  `ModeloIA` varchar(100) DEFAULT NULL,
  `Confianza` decimal(5,2) DEFAULT NULL,
  `FechaRespuesta` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `IdRol` int(11) NOT NULL,
  `NombreRol` varchar(20) NOT NULL,
  `nombre_rol` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`IdRol`, `NombreRol`, `nombre_rol`) VALUES
(1, 'ADMIN', ''),
(2, 'DOCENTE', ''),
(3, 'ALUMNO', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitudesia`
--

CREATE TABLE `solicitudesia` (
  `IdSolicitud` int(11) NOT NULL,
  `IdDocente` int(11) NOT NULL,
  `IdCurso` int(11) NOT NULL,
  `IdAlumno` int(11) DEFAULT NULL,
  `DatosEntrada` text NOT NULL,
  `Estado` varchar(20) DEFAULT 'PENDIENTE',
  `FechaSolicitud` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `testsgardner`
--

CREATE TABLE `testsgardner` (
  `IdTest` int(11) NOT NULL,
  `IdAlumno` int(11) NOT NULL,
  `Respuestas` text DEFAULT NULL,
  `Puntajes` text DEFAULT NULL,
  `estado_guardado` enum('BORRADOR','FINAL','CALCULADO') DEFAULT 'BORRADOR',
  `version_guardado` int(11) DEFAULT 1,
  `client_request_id` varchar(255) DEFAULT NULL,
  `inteligencia_predominante` varchar(100) DEFAULT NULL,
  `puntaje_total` decimal(5,2) DEFAULT NULL,
  `tiempo_inicio` datetime DEFAULT NULL,
  `tiempo_fin` datetime DEFAULT NULL,
  `modificado_por` varchar(255) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `FechaAplicacion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Disparadores `testsgardner`
--
DELIMITER $$
CREATE TRIGGER `tr_generate_recommendations` AFTER UPDATE ON `testsgardner` FOR EACH ROW BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE plantilla_id INT;
    DECLARE plantilla_contenido TEXT;
    DECLARE plantilla_tipo VARCHAR(50);
    DECLARE puntaje_inteligencia INT;
    
    -- Only trigger when test is marked as CALCULADO
    IF NEW.estado_guardado = 'CALCULADO' AND OLD.estado_guardado != 'CALCULADO' THEN
        
        -- Get the score for the predominant intelligence
        SET puntaje_inteligencia = (
            SELECT JSON_EXTRACT(NEW.Puntajes, CONCAT('$."', NEW.inteligencia_predominante, '"'))
        );
        
        -- Find matching template
        SELECT id_plantilla, contenido_recomendacion, tipo_recomendacion
        INTO plantilla_id, plantilla_contenido, plantilla_tipo
        FROM plantillas_recomendaciones 
        WHERE tipo_inteligencia = NEW.inteligencia_predominante 
        AND puntaje_minimo <= puntaje_inteligencia 
        AND puntaje_maximo >= puntaje_inteligencia
        AND activo = TRUE
        LIMIT 1;
        
        -- Insert recommendation if template found
        IF plantilla_id IS NOT NULL THEN
            INSERT INTO historial_recomendaciones (
                id_alumno, id_test, id_plantilla, inteligencia_predominante, 
                puntaje_inteligencia, recomendacion_generada, tipo_recomendacion
            ) VALUES (
                NEW.IdAlumno, NEW.IdTest, plantilla_id, NEW.inteligencia_predominante,
                puntaje_inteligencia, plantilla_contenido, plantilla_tipo
            );
        END IF;
        
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `IdUsuario` int(11) NOT NULL,
  `NombreUsuario` varchar(80) NOT NULL,
  `CorreoElectronico` varchar(150) NOT NULL,
  `ContrasenaHash` varchar(255) NOT NULL,
  `IdRol` int(11) NOT NULL,
  `FechaCreacion` datetime DEFAULT current_timestamp(),
  `contrasena_hash` varchar(255) NOT NULL,
  `correo_electronico` varchar(150) NOT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `nombre_usuario` varchar(80) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `password_reset_token` varchar(255) DEFAULT NULL,
  `password_reset_expiry` datetime DEFAULT NULL,
  `failed_attempts` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `accesosusuarios`
--
ALTER TABLE `accesosusuarios`
  ADD PRIMARY KEY (`id_acceso`);

--
-- Indices de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD PRIMARY KEY (`IdAlumno`),
  ADD UNIQUE KEY `IdUsuario` (`IdUsuario`);

--
-- Indices de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  ADD PRIMARY KEY (`IdAuditoria`),
  ADD KEY `IdUsuario` (`IdUsuario`);

--
-- Indices de la tabla `calificaciones`
--
ALTER TABLE `calificaciones`
  ADD PRIMARY KEY (`IdCalificacion`),
  ADD KEY `IdMatricula` (`IdMatricula`);

--
-- Indices de la tabla `cursos`
--
ALTER TABLE `cursos`
  ADD PRIMARY KEY (`IdCurso`),
  ADD KEY `IdDocente` (`IdDocente`);

--
-- Indices de la tabla `docentes`
--
ALTER TABLE `docentes`
  ADD PRIMARY KEY (`IdDocente`),
  ADD UNIQUE KEY `IdUsuario` (`IdUsuario`);

--
-- Indices de la tabla `flyway_schema_history`
--
ALTER TABLE `flyway_schema_history`
  ADD PRIMARY KEY (`installed_rank`),
  ADD KEY `flyway_schema_history_s_idx` (`success`);

--
-- Indices de la tabla `historial_recomendaciones`
--
ALTER TABLE `historial_recomendaciones`
  ADD PRIMARY KEY (`id_historial`),
  ADD KEY `id_plantilla` (`id_plantilla`),
  ADD KEY `idx_alumno` (`id_alumno`),
  ADD KEY `idx_test` (`id_test`),
  ADD KEY `idx_fecha_generacion` (`fecha_generacion`),
  ADD KEY `idx_estado` (`estado`);

--
-- Indices de la tabla `matriculas`
--
ALTER TABLE `matriculas`
  ADD PRIMARY KEY (`IdMatricula`),
  ADD UNIQUE KEY `IdAlumno` (`IdAlumno`,`IdCurso`),
  ADD KEY `IdCurso` (`IdCurso`);

--
-- Indices de la tabla `plantillas_recomendaciones`
--
ALTER TABLE `plantillas_recomendaciones`
  ADD PRIMARY KEY (`id_plantilla`),
  ADD KEY `idx_tipo_inteligencia` (`tipo_inteligencia`),
  ADD KEY `idx_puntaje_range` (`puntaje_minimo`,`puntaje_maximo`),
  ADD KEY `idx_tipo_recomendacion` (`tipo_recomendacion`),
  ADD KEY `idx_activo` (`activo`);

--
-- Indices de la tabla `preguntas_gardner`
--
ALTER TABLE `preguntas_gardner`
  ADD PRIMARY KEY (`id_pregunta`),
  ADD UNIQUE KEY `idx_unique_orden_inteligencia` (`tipo_inteligencia`,`orden_secuencia`),
  ADD KEY `idx_tipo_inteligencia` (`tipo_inteligencia`),
  ADD KEY `idx_orden` (`orden_secuencia`),
  ADD KEY `idx_activo` (`activo`);

--
-- Indices de la tabla `refreshtokens`
--
ALTER TABLE `refreshtokens`
  ADD PRIMARY KEY (`IdRefreshToken`),
  ADD UNIQUE KEY `Token` (`Token`),
  ADD KEY `IdUsuario` (`IdUsuario`);

--
-- Indices de la tabla `respuestasia`
--
ALTER TABLE `respuestasia`
  ADD PRIMARY KEY (`IdRespuesta`),
  ADD KEY `IdSolicitud` (`IdSolicitud`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`IdRol`),
  ADD UNIQUE KEY `NombreRol` (`NombreRol`);

--
-- Indices de la tabla `solicitudesia`
--
ALTER TABLE `solicitudesia`
  ADD PRIMARY KEY (`IdSolicitud`),
  ADD KEY `IdDocente` (`IdDocente`),
  ADD KEY `IdCurso` (`IdCurso`),
  ADD KEY `IdAlumno` (`IdAlumno`);

--
-- Indices de la tabla `testsgardner`
--
ALTER TABLE `testsgardner`
  ADD PRIMARY KEY (`IdTest`),
  ADD UNIQUE KEY `idx_unique_client_request` (`client_request_id`),
  ADD KEY `IdAlumno` (`IdAlumno`),
  ADD KEY `idx_estado_guardado` (`estado_guardado`),
  ADD KEY `idx_client_request_id` (`client_request_id`),
  ADD KEY `idx_inteligencia_predominante` (`inteligencia_predominante`),
  ADD KEY `idx_fecha_aplicacion` (`FechaAplicacion`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`IdUsuario`),
  ADD UNIQUE KEY `NombreUsuario` (`NombreUsuario`),
  ADD UNIQUE KEY `CorreoElectronico` (`CorreoElectronico`),
  ADD UNIQUE KEY `UKduxldumspflsqyka52vo72hse` (`correo_electronico`),
  ADD UNIQUE KEY `UKof5vabgukahdwmgxk4kjrbu98` (`nombre_usuario`),
  ADD KEY `IdRol` (`IdRol`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `accesosusuarios`
--
ALTER TABLE `accesosusuarios`
  MODIFY `id_acceso` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  MODIFY `IdAlumno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  MODIFY `IdAuditoria` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `calificaciones`
--
ALTER TABLE `calificaciones`
  MODIFY `IdCalificacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cursos`
--
ALTER TABLE `cursos`
  MODIFY `IdCurso` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `docentes`
--
ALTER TABLE `docentes`
  MODIFY `IdDocente` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `historial_recomendaciones`
--
ALTER TABLE `historial_recomendaciones`
  MODIFY `id_historial` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `matriculas`
--
ALTER TABLE `matriculas`
  MODIFY `IdMatricula` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `plantillas_recomendaciones`
--
ALTER TABLE `plantillas_recomendaciones`
  MODIFY `id_plantilla` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `preguntas_gardner`
--
ALTER TABLE `preguntas_gardner`
  MODIFY `id_pregunta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de la tabla `refreshtokens`
--
ALTER TABLE `refreshtokens`
  MODIFY `IdRefreshToken` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `respuestasia`
--
ALTER TABLE `respuestasia`
  MODIFY `IdRespuesta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `IdRol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `solicitudesia`
--
ALTER TABLE `solicitudesia`
  MODIFY `IdSolicitud` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `testsgardner`
--
ALTER TABLE `testsgardner`
  MODIFY `IdTest` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `IdUsuario` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD CONSTRAINT `alumnos_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`);

--
-- Filtros para la tabla `auditoria`
--
ALTER TABLE `auditoria`
  ADD CONSTRAINT `auditoria_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`);

--
-- Filtros para la tabla `calificaciones`
--
ALTER TABLE `calificaciones`
  ADD CONSTRAINT `calificaciones_ibfk_1` FOREIGN KEY (`IdMatricula`) REFERENCES `matriculas` (`IdMatricula`);

--
-- Filtros para la tabla `cursos`
--
ALTER TABLE `cursos`
  ADD CONSTRAINT `cursos_ibfk_1` FOREIGN KEY (`IdDocente`) REFERENCES `docentes` (`IdDocente`);

--
-- Filtros para la tabla `docentes`
--
ALTER TABLE `docentes`
  ADD CONSTRAINT `docentes_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`);

--
-- Filtros para la tabla `historial_recomendaciones`
--
ALTER TABLE `historial_recomendaciones`
  ADD CONSTRAINT `historial_recomendaciones_ibfk_1` FOREIGN KEY (`id_alumno`) REFERENCES `alumnos` (`IdAlumno`),
  ADD CONSTRAINT `historial_recomendaciones_ibfk_2` FOREIGN KEY (`id_test`) REFERENCES `testsgardner` (`IdTest`),
  ADD CONSTRAINT `historial_recomendaciones_ibfk_3` FOREIGN KEY (`id_plantilla`) REFERENCES `plantillas_recomendaciones` (`id_plantilla`);

--
-- Filtros para la tabla `matriculas`
--
ALTER TABLE `matriculas`
  ADD CONSTRAINT `matriculas_ibfk_1` FOREIGN KEY (`IdAlumno`) REFERENCES `alumnos` (`IdAlumno`),
  ADD CONSTRAINT `matriculas_ibfk_2` FOREIGN KEY (`IdCurso`) REFERENCES `cursos` (`IdCurso`);

--
-- Filtros para la tabla `refreshtokens`
--
ALTER TABLE `refreshtokens`
  ADD CONSTRAINT `refreshtokens_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`);

--
-- Filtros para la tabla `respuestasia`
--
ALTER TABLE `respuestasia`
  ADD CONSTRAINT `respuestasia_ibfk_1` FOREIGN KEY (`IdSolicitud`) REFERENCES `solicitudesia` (`IdSolicitud`);

--
-- Filtros para la tabla `solicitudesia`
--
ALTER TABLE `solicitudesia`
  ADD CONSTRAINT `solicitudesia_ibfk_1` FOREIGN KEY (`IdDocente`) REFERENCES `docentes` (`IdDocente`),
  ADD CONSTRAINT `solicitudesia_ibfk_2` FOREIGN KEY (`IdCurso`) REFERENCES `cursos` (`IdCurso`),
  ADD CONSTRAINT `solicitudesia_ibfk_3` FOREIGN KEY (`IdAlumno`) REFERENCES `alumnos` (`IdAlumno`);

--
-- Filtros para la tabla `testsgardner`
--
ALTER TABLE `testsgardner`
  ADD CONSTRAINT `testsgardner_ibfk_1` FOREIGN KEY (`IdAlumno`) REFERENCES `alumnos` (`IdAlumno`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`IdRol`) REFERENCES `roles` (`IdRol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
