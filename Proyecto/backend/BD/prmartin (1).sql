-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-09-2025 a las 04:56:01
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
(1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2025-09-21 23:06:46', 0, 1);

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
  `FechaAplicacion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `activo` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

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
-- Indices de la tabla `matriculas`
--
ALTER TABLE `matriculas`
  ADD PRIMARY KEY (`IdMatricula`),
  ADD UNIQUE KEY `IdAlumno` (`IdAlumno`,`IdCurso`),
  ADD KEY `IdCurso` (`IdCurso`);

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
  ADD KEY `IdAlumno` (`IdAlumno`);

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
-- AUTO_INCREMENT de la tabla `matriculas`
--
ALTER TABLE `matriculas`
  MODIFY `IdMatricula` int(11) NOT NULL AUTO_INCREMENT;

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



-- DATOS DE PRUEBA PARA LA TABLA 

INSERT INTO roles (IdRol, NombreRol, nombre_rol) VALUES
(1, 'ADMIN', ''),
(2, 'DOCENTE', ''),
(3, 'ALUMNO', ''),
(4, 'INVITADO', ''),
(5, 'SUPERVISOR', '');

INSERT INTO usuarios (IdUsuario, NombreUsuario, CorreoElectronico, ContrasenaHash, IdRol, contrasena_hash, correo_electronico, nombre_usuario, id_rol, activo)
VALUES
(1, 'admin', 'admin@colegio.edu', 'hash1', 1, 'hash1', 'admin@colegio.edu', 'admin', 1, b'1'),
(2, 'docente1', 'profesor1@colegio.edu', 'hash2', 2, 'hash2', 'profesor1@colegio.edu', 'docente1', 2, b'1'),
(3, 'docente2', 'profesor2@colegio.edu', 'hash3', 2, 'hash3', 'profesor2@colegio.edu', 'docente2', 2, b'1'),
(4, 'alumno1', 'alumno1@colegio.edu', 'hash4', 3, 'hash4', 'alumno1@colegio.edu', 'alumno1', 3, b'1'),
(5, 'alumno2', 'alumno2@colegio.edu', 'hash5', 3, 'hash5', 'alumno2@colegio.edu', 'alumno2', 3, b'1');

INSERT INTO docentes (IdDocente, IdUsuario, Especialidad)
VALUES
(1, 2, 'Matemáticas'),
(2, 3, 'Comunicación'),
(3, 1, 'Informática'),
(4, 2, 'Ciencias'),
(5, 3, 'Inglés');

INSERT INTO alumnos (IdAlumno, IdUsuario, AnioIngreso)
VALUES
(1, 4, 2023),
(2, 5, 2024),
(3, 4, 2023),
(4, 5, 2024),
(5, 4, 2025);

INSERT INTO cursos (IdCurso, NombreCurso, IdDocente)
VALUES
(1, 'Matemáticas I', 1),
(2, 'Lenguaje y Comunicación', 2),
(3, 'Ciencias Naturales', 4),
(4, 'Inglés Básico', 5),
(5, 'Computación', 3);

INSERT INTO matriculas (IdMatricula, IdAlumno, IdCurso, FechaMatricula)
VALUES
(1, 1, 1, '2025-03-10'),
(2, 1, 2, '2025-03-10'),
(3, 2, 3, '2025-03-11'),
(4, 2, 4, '2025-03-11'),
(5, 1, 5, '2025-03-12');

INSERT INTO calificaciones (IdCalificacion, IdMatricula, Periodo, Nota, FechaRegistro)
VALUES
(1, 1, 'Bimestre 1', 18.5, '2025-04-01 10:00:00'),
(2, 2, 'Bimestre 1', 16.0, '2025-04-01 10:00:00'),
(3, 3, 'Bimestre 1', 14.5, '2025-04-01 10:00:00'),
(4, 4, 'Bimestre 1', 17.2, '2025-04-01 10:00:00'),
(5, 5, 'Bimestre 1', 19.0, '2025-04-01 10:00:00');

INSERT INTO testsgardner (IdTest, IdAlumno, Respuestas, Puntajes, FechaAplicacion)
VALUES
(1, 1, '{"preg1":"A","preg2":"B"}', '{"Lingüística":15,"Lógica":13}', '2025-04-05 09:00:00'),
(2, 2, '{"preg1":"C","preg2":"A"}', '{"Musical":14,"Espacial":12}', '2025-04-05 09:10:00'),
(3, 1, '{"preg1":"B","preg2":"D"}', '{"Corporal":16,"Interpersonal":15}', '2025-04-05 09:15:00'),
(4, 2, '{"preg1":"D","preg2":"B"}', '{"Naturalista":13,"Intrapersonal":14}', '2025-04-05 09:20:00'),
(5, 1, '{"preg1":"A","preg2":"C"}', '{"Lógica":18,"Espacial":17}', '2025-04-05 09:30:00');

INSERT INTO solicitudesia (IdSolicitud, IdDocente, IdCurso, IdAlumno, DatosEntrada, Estado, FechaSolicitud)
VALUES
(1, 1, 1, 1, '{"tema":"álgebra","rendimiento":18}', 'PENDIENTE', '2025-04-10 12:00:00'),
(2, 2, 2, 2, '{"tema":"lectura","rendimiento":16}', 'PROCESADA', '2025-04-10 12:05:00'),
(3, 3, 5, 1, '{"tema":"programación","rendimiento":19}', 'PENDIENTE', '2025-04-10 12:10:00'),
(4, 1, 1, 2, '{"tema":"geometría","rendimiento":15}', 'PROCESADA', '2025-04-10 12:15:00'),
(5, 2, 2, 1, '{"tema":"gramática","rendimiento":17}', 'PENDIENTE', '2025-04-10 12:20:00');

INSERT INTO respuestasia (IdRespuesta, IdSolicitud, RespuestaJSON, ModeloIA, Confianza, FechaRespuesta)
VALUES
(1, 1, '{"sugerencia":"reforzar álgebra con ejercicios prácticos"}', 'GPT-5-Edu', 0.92, '2025-04-10 12:30:00'),
(2, 2, '{"sugerencia":"mejorar comprensión lectora con textos breves"}', 'GPT-5-Edu', 0.88, '2025-04-10 12:31:00'),
(3, 3, '{"sugerencia":"avanzar a temas de lógica"}', 'GPT-5-Edu', 0.95, '2025-04-10 12:32:00'),
(4, 4, '{"sugerencia":"usar videos educativos de geometría"}', 'GPT-5-Edu', 0.90, '2025-04-10 12:33:00'),
(5, 5, '{"sugerencia":"realizar análisis gramaticales cortos"}', 'GPT-5-Edu', 0.85, '2025-04-10 12:34:00');

INSERT INTO refreshtokens (IdRefreshToken, IdUsuario, Token, IssuedAt, ExpiresAt, Revoked)
VALUES
(1, 1, 'token_admin', '2025-04-01 00:00:00', '2025-05-01 00:00:00', 0),
(2, 2, 'token_doc1', '2025-04-01 00:00:00', '2025-05-01 00:00:00', 0),
(3, 3, 'token_doc2', '2025-04-01 00:00:00', '2025-05-01 00:00:00', 0),
(4, 4, 'token_alum1', '2025-04-01 00:00:00', '2025-05-01 00:00:00', 0),
(5, 5, 'token_alum2', '2025-04-01 00:00:00', '2025-05-01 00:00:00', 0);


INSERT INTO auditoria (IdAuditoria, IdUsuario, Accion, Entidad, IdEntidad, Detalles, DireccionIP, FechaEvento)
VALUES
(1, 1, 'INSERT', 'usuarios', 1, 'Creación de usuario admin', '192.168.0.10', '2025-04-01 09:00:00'),
(2, 2, 'INSERT', 'cursos', 1, 'Nuevo curso Matemáticas I', '192.168.0.11', '2025-04-01 09:05:00'),
(3, 4, 'UPDATE', 'calificaciones', 1, 'Modificación de nota', '192.168.0.12', '2025-04-01 09:10:00'),
(4, 3, 'DELETE', 'matriculas', 2, 'Eliminación de matrícula duplicada', '192.168.0.13', '2025-04-01 09:15:00'),
(5, 1, 'LOGIN', 'usuarios', 1, 'Inicio de sesión exitoso', '192.168.0.10', '2025-04-01 09:20:00');
