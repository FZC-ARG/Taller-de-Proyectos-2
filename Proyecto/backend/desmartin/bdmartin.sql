-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 05-11-2025 a las 17:17:17
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
-- Estructura de tabla para la tabla `administradores`
--

CREATE TABLE `administradores` (
  `id_admin` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre_usuario` VARCHAR(255) NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `nombre` VARCHAR(255) NOT NULL,
  `apellido` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_admin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
-- Volcado de datos para la tabla `administradores`
--

INSERT INTO `administradores` (`id_admin`, `nombre_usuario`, `contrasena`, `nombre`, `apellido`) VALUES
(1, 'dante1', '$2a$10$IlGuA8Grb5l7nVgd4SEJcez1kLYUIS11.7YLmsIQh5hgck0j/4fWe', 'dante', 'romani');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos`
--

CREATE TABLE `alumnos` (
  `id_alumno` int(11) NOT NULL,
  `nombre_usuario` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `fecha_nacimiento` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumnos`
--

INSERT INTO `alumnos` (`id_alumno`, `nombre_usuario`, `contrasena`, `nombre`, `apellido`, `fecha_nacimiento`) VALUES
(1, 'ejemplo_usuario', '$2y$10$e1xAmP9QZkL8J9xU1ZsO1uYzKfQeW9XzvGJvK9QhO1ZsO1uYzKfQeW', 'Juan', 'Pérez Gómez', '2000-05-15'),
(2, 'dante', '$2a$10$18uukyiiGnorouokH2nYW.cQGxLLa498kYAXqgkgJXIBuWJM6ojzG', 'dante', 'romani', '2012-01-19');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos_cursos`
--

CREATE TABLE `alumnos_cursos` (
  `id_alumno_fk` int(11) NOT NULL,
  `id_curso_fk` int(11) NOT NULL,
  `fecha_matricula` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumnos_cursos`
--

INSERT INTO `alumnos_cursos` (`id_alumno_fk`, `id_curso_fk`, `fecha_matricula`) VALUES
(1, 1, NULL),
(2, 1, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `chat_mensajes`
--

CREATE TABLE `chat_mensajes` (
  `id_mensaje` int(11) NOT NULL,
  `id_sesion_fk` int(11) NOT NULL,
  `emisor` enum('docente','ia') NOT NULL,
  `contenido` text NOT NULL,
  `fecha_hora_envio` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `chat_mensajes`
--

INSERT INTO `chat_mensajes` (`id_mensaje`, `id_sesion_fk`, `emisor`, `contenido`, `fecha_hora_envio`) VALUES
(1, 1, 'docente', 'hola', '2025-11-05 01:06:05'),
(2, 1, 'ia', 'Lo siento, hubo un error al procesar tu solicitud. Por favor, intenta nuevamente. Error: No se pudo comunicar con OpenRouter después de 3 intentos', '2025-11-05 01:06:13'),
(3, 2, 'docente', 'hola', '2025-11-05 01:16:14'),
(4, 2, 'ia', 'Lo siento, hubo un error al procesar tu solicitud. Por favor, intenta nuevamente. Si el problema persiste, contacta al administrador.', '2025-11-05 01:16:21'),
(5, 3, 'docente', 'hola', '2025-11-05 02:30:05'),
(6, 3, 'ia', 'Lo siento, hubo un error al procesar tu solicitud. Por favor, intenta nuevamente. Si el problema persiste, contacta al administrador.', '2025-11-05 02:30:13'),
(7, 4, 'docente', 'hola', '2025-11-05 02:39:11'),
(8, 5, 'docente', 'hola', '2025-11-05 02:55:18'),
(9, 6, 'docente', 'hola', '2025-11-05 03:07:32'),
(10, 7, 'docente', 'hola', '2025-11-05 03:12:08'),
(11, 8, 'docente', 'hola', '2025-11-05 03:23:07'),
(12, 8, 'ia', '¡Hola! Bienvenido.\n\nBasándome en el perfil de inteligencias múltiples de Juan, puedo observar que sus fortalezas principales son:\n- **Inteligencia Lógico-Matemática (5,00/5.00)**: Resuelve problemas de manera lógica, analítica y estructurada.\n- **Inteligencia Espacial-Visual (5,00/5.00)**: Tiene buena capacidad para visualizar, diseñar y orientarse espacialmente.\n- **Inteligencia Intrapersonal (4,70/5.00)**: Tiene buena autoconocimiento y capacidad de reflexión.\n\nPara potenciar su aprendizaje, te recomendaría algunas estrategias:\n\n**1. Aprovecha su pensamiento lógico:**\n- Presenta desafíos o problemas que requieran análisis paso a paso.\n- Introduce conceptos nuevos mostrando patrones o relaciones lógicas.\n\n**2. Involucra su capacidad espacial-visual:**\n- Usa diagramas, mapas mentales, gráficos o videos para explicar.\n- Permítele visualizar soluciones antes de implementarlas.\n\n**3. Fortalece su intrapersonal:**\n- Proporciona momentos para autorreflexión (ej: \"¿Qué estrategia usaste? ¿Funcionó?\")\n- Conecta los aprendizajes a sus metas personales.\n\n¿Te gustaría que profundice en alguna estrategia específica o tienes alguna situación de aprendizaje concreta en la que quieras aplicar esto? <｜begin▁of▁sentence｜>', '2025-11-05 03:23:32'),
(13, 8, 'docente', 'puedes darme sugerencias para el apredizaje de ese alumno que puede hacer para mejorrar ne matematicas', '2025-11-05 03:43:12'),
(14, 8, 'ia', '¡Claro! Aquí tienes algunas sugerencias prácticas y bien estructuradas para que ese alumno mejore en matemáticas. Las he dividido en categorías para que sea más fácil seguirlas.\n\n### 1. **Diagnostica las debilidades exactas**\nAntes de empezar, es crucial identificar **qué áreas específicas** son las que causan problemas. ¿Son las bases (como aritmética básica o álgebra elemental)? ¿O son conceptos más avanzados? Hacer un repaso general con ejercicios variados puede ayudar a detectar los puntos débiles.\n\n### 2. **Refuerza las bases**\nLas matemáticas son acumulativas. Si las bases no son sólidas, se dificulta el aprendizaje de temas más avanzados.\n- **Repasa lo fundamental**: Operaciones básicas (suma, resta, multiplicación, división), fracciones, porcentajes, y álgebra básica (ecuaciones simples).\n- **Usa recursos online**: Khan Academy tiene excelentes videos y ejercicios gratuitos organizados por temas.\n\n### 3. **Establece una rutina de estudio**\nLa consistencia es clave.\n- **Dedica tiempo regular**: 30-45 minutos al día, en lugar de horas los fines de semana.\n- **Empieza con lo fácil**: Comienza con ejercicios que sepan hacer para ganar confianza, luego avanza gradualmente.\n\n### 4. **Utiliza recursos variados**\nDiferentes personas aprenden de maneras distintas.\n- **Videos**: Khan Academy, YouTube (canales como \"Derivando\" o \"Math2Me\").\n- **Aplicaciones**: Photomath (para ver soluciones paso a paso), Brilliant (para aprendizaje con puzzles), etc.\n- **Ejercicios con respuestas**: Muchos sitios web ofrecen ejercicios con soluciones detalladas para practicar.\n\n### 5. **Aplica las matemáticas a la vida real**\nConectar los conceptos abstractos con situaciones cotidianas puede hacerlos más tangibles y menos intimidantes.\n- **Presupuestos**: Hacer un presupuesto mensual puede practicar aritmética y porcentajes.\n- **Proyectos DIY**: Medir y calcular áreas/volúmenes para proyectos caseros.\n- **Juegos de estrategia**: Ajedrez, algunos videojuegos de estrategia, que requieren pensamiento lógico.\n\n### 6. **Practica con ejercicios progresivos**\nNo te quedes en la zona de confort. Una vez dominado un nivel, pasa al siguiente.\n- **Incrementa la dificultad gradualmente**: Si los ejercicios de un tema son fáciles, busca problemas más desafiantes.\n- **Integra temas**: Por ejemplo, problemas que combinen álgebra y geometría.\n\n### 7. **Busca ayuda cuando sea necesario**\nNo hay que tener miedo a pedir ayuda.\n- **Profesores/tutores**: Un par de sesiones con un tutor pueden aclarar conceptos que llevan tiempo confusos.\n- **Grupos de estudio**: A veces, discutir problemas con otros puede ofrecer nuevas perspectivas.\n\n### 8. **Mantén una actitud positiva**\nLa mentalidad es importante. Ver las matemáticas como un desafío interesante, en lugar de una tarea abrumadora, cambia la experiencia.\n- **Celebra los pequeños logros**: Resolver un problema difícil es una victoria.\n- **Acepta los errores**: Son parte del aprendizaje. Analizar por qué algo estuvo mal enseña más que acertar.\n\n### 9. **Relaciona con otros intereses**\nSi la persona tiene otros hobbies (música, deporte, videojuegos), encontrar la conexión con las matemáticas puede hacerlas más atractivas.\n- **Música y ritmo**: Tienen base en patrones y secuencias.\n- **Deportes**: Estadísticas y probabilidades.\n- **Arte y diseño**: Geometría y proporciones.\n\n### 10. **Sé paciente y persistente**\nMejorar en matemáticas no sucede de la noche a la mañana. Es un proceso.\n- **Establece metas realistas**: Por ejemplo, \"entender este capítulo en dos semanas\".\n- **Revisa regularmente**: Lo que se aprende se olvida si no se usa. Revisar temas anteriores ayuda a fijarlos.\n\n¿Te gustaría que profundice en alguna de estas áreas o tienes alguna situación específica en mente?', '2025-11-05 03:43:46'),
(15, 9, 'docente', 'hola', '2025-11-05 03:54:12'),
(16, 9, 'ia', '¡Hola! Bienvenido/a. Como asistente especializado en inteligencias múltiples, puedo ayudarte a interpretar y aplicar los resultados del test de inteligencias múltiples para tu grupo de matemáticas.\n\nVeo que el grupo tiene una inteligencia **espacial-visual** muy desarrollada (5,00/5,00), seguida de la **lógico-matemática** (4,75/5,00). Esto es excelente para el área de matemáticas, especialmente con temas como geometría y ecuaciones diferenciales que implican visualización y razonamiento estructurado.\n\n**¿En qué puedo ayudarte hoy?** Por ejemplo:\n- ¿Quieres sugerencias para diseñar una sesión que aproveche estas fortalezas?\n- ¿Necesitas ayuda para interpretar el perfil de algún estudiante en particular?\n- ¿Quieres ideas para integrar a los estudiantes que aún no han completado el test?\n\nEstoy aquí para ayudarte. 😊', '2025-11-05 03:54:25'),
(17, 9, 'docente', 'quiero que planees un clase en base a ese alumno', '2025-11-05 03:55:45'),
(18, 9, 'ia', '¡Excelente! Vamos a planificar una sesión de clase para el estudiante **dante romani** (y por extensión, para el grupo) basándonos en el perfil de inteligencias múltiples.\n\n**Objetivo de la sesión:** Aprovechar las fortalezas del grupo (especialmente la inteligencia espacial-visual y lógico-matemática) para abordar un tema de matemáticas, como las ecuaciones diferenciales o la geometría.\n\n**Duración estimada:** 60 minutos\n\n---\n\n###  **Estructura de la sesión: \"Visualizando ecuaciones diferenciales\"**\n\n#### 1. **Inicio - Activación visual (10 min)**\n   - **Actividad:** Presentar un problema de contexto real que se modele con una ecuación diferencial simple. Por ejemplo: \n      - \"Imaginen que un tanque de agua pierde agua a una tasa proporcional a la cantidad presente. Si inicialmente hay 1000 litros, ¿cómo modelamos esto?\"\n   - **Recurso:** Usar un video corto o una imagen que muestre el escenario para activar el pensamiento visual-espacial.\n   - **Objetivo:** Que los estudiantes se conecten con el problema de manera tangible.\n\n#### 2. **Desarrollo - Modelado y resolución visual (25 min)**\n   - **Paso 1:** En grupos de 2 (dante romani y otro estudiante), plantear la ecuación diferencial en términos de diagramas o gráficos. Por ejemplo:\n      - \"Dibujen cómo cambia el volumen de agua en el tiempo. ¿Qué forma tiene la curva?\"\n   - **Integración tecnológica:** Usar una herramienta como GeoGebra o Desmos para graficar la solución de forma dinámica. Esto aprovecha la inteligencia espacial.\n   - **Paso 2:** Relacionar con conceptos geométricos. Por ejemplo, si la solución es una exponencial, relacionarla con crecimiento de poblaciones (inteligencia naturalista).\n\n#### 3. **Cierre - Aplicación y reflexión (15 min)**\n   - **Actividad:** Cada estudiante (o pareja) presenta su modelo y solución de manera visual (usando un diagrama o gráfico en la pizarra).\n   - **Reflexión metacognitiva:** \"¿Cómo nos ayudó visualizar el problema para resolverlo? ¿Qué otras situaciones se modelan así?\"\n   - **Conexión interpersonal:** Compartir soluciones y aprender de otros.\n\n---\n\n###  **Por qué esta estructura funciona para dante romani y el grupo:**\n- **Aprovecha la inteligencia espacial-visual:** Al usar diagramas, gráficos y herramientas visuales, se conecta con la fortaleza principal del grupo.\n- **Engancha la lógica matemática:** La resolución de ecuaciones diferenciales es inherentemente lógica, pero al visualizarla, se hace más accesible.\n- **Fomenta la colaboración:** Al trabajar en parejas, se activa la inteligencia interpersonal, que también está presente en el perfil.\n\n###  **Recomendaciones adicionales:**\n1. **Para el estudiante sin test (dante romani aún no ha completado el test):** Incluir actividades que le permitan brillar en otras áreas, como la participación verbal o kinestésica (ej: mover objetos para representar ecuaciones).\n2. **Seguimiento:** Después de la sesión, preguntar: \"¿Qué estrategia te funcionó mejor: visualizar, analizar lógicamente o discutir con otros?\"\n3. **Recursos:** Usar colores, diagramas en la pared y permitir movimiento en el aula para mantener engagement.\n\n---\n\n¿Te gustaría que ajuste algún elemento de esta sesión? ¿O necesitas ideas más específicas para otro tema? 😊<｜begin▁of▁sentence｜>', '2025-11-05 03:56:15'),
(19, 8, 'docente', 'dame sugerencias de la opcion 10', '2025-11-05 03:57:10'),
(20, 8, 'ia', '¡Claro! Desarrollemos la **Opción 10: \"Sé paciente y persistente\"** con más detalle y ideas prácticas.\n\n### **10. Sé paciente y persistente**\nEsta actitud es crucial porque las matemáticas no se dominan de la noche a la mañana. Requieren práctica constante y, a veces, enfrentarse a la frustración de no entender algo inmediatamente.\n\n#### **Cómo aplicar esto en la práctica:**\n\n**a. Establece metas pequeñas y realistas:**\n- En lugar de decir \"voy a aprender todo el álgebra este mes\", divide en partes.\n- Ejemplo: \"Esta semana, voy a dominar cómo resolver ecuaciones de primer grado con una incógnita.\" \n- Una vez dominado, pasa a \"ecuaciones con paréntesis\" o \"sistemas de ecuaciones\".\n\n**b. Programa sesiones de revisión:**\n- Lo que no se repasa se olvida. Programa un tiempo cada semana para repasar temas anteriores.\n- Usa la técnica de \"repaso espaciado\": Revisar un tema un día después de aprenderlo, luego a la semana, y luego al mes, ayuda a fijarlo en la memoria a largo plazo.\n\n**c. Mantén un registro de progreso:**\n- Anota en un cuaderno cada vez que resuelves un problema difícil o entiendes un concepto nuevo. Ver ese registro creciente motiva.\n- También anota errores comunes para no repetirlos.\n\n**d. Aplica la \"mentalidad de crecimiento\":**\n- En lugar de pensar \"soy malo en matemáticas\", piensa \"todavía no he dominado esto, pero puedo hacerlo\".\n- Después de un error, pregúntate: \"¿Qué aprendí de esto? ¿Cómo lo haría diferente la próxima vez?\"\n\n**e. Celebra los avances, por pequeños que sean:**\n- ¿Lograste resolver un problema que antes no podías? ¡Tómate un momento para reconocerlo!\n- ¿Entendiste una explicación que antes era confusa? Date crédito.\n\n**f. Sé consistente, no perfecto:**\n- Es mejor estudiar 30 minutos cada día que 5 horas un sábado y luego no tocar el tema por dos semanas.\n- La constancia crea bases más sólidas que los atracones de estudio.\n\n**g. Aprende de los errores:**\n- Cuando un problema salga mal, no sólo veas la respuesta correcta. Pregunta:\n  - ¿Por qué me equivoqué? (¿Fue un error de cálculo, una mala interpretación, un concepto mal entendido?)\n  - ¿Cómo puedo evitar ese error la próxima vez?\n  - ¿Hay un patrón en mis errores? (Por ejemplo, ¿siempre olvido el signo negativo?)\n\n**h. Conecta con la vida diaria:**\n- Practica estimando: ¿Cuánto costará aproximadamente la compra? ¿Cuánto tiempo falta para llegar? \n- Esto hace que las matemáticas sean una herramienta, no sólo un tema de estudio.\n\n**i. Mantén los recursos a mano:**\n- Ten a mano tu resumen de fórmulas, una calculadora (si está permitido usarla para algunos cálculos), y acceso a esos recursos online que mencionamos.\n- No te detengas demasiado tiempo en un solo problema. Si después de 15 minutos no sale, déjalo por ahora, revisa el tema con un video o explicación, y vuelve a él más tarde.\n\n**j. Reflexiona sobre tu aprendizaje:**\n- Al final de la semana, tómate 10 minutos para pensar:\n  - ¿Qué aprendí esta semana?\n  - ¿Qué se me hizo fácil? ¿Qué fue difícil?\n  - ¿Cómo puedo hacer que lo difícil sea más fácil la próxima vez?\n\n---\n\n¿Te parece útil? ¿Quieres que desarrolle alguna de las otras opciones o tienes alguna duda concreta?', '2025-11-05 03:57:40'),
(21, 9, 'docente', 'dame mas sugeerecias sobre activacion visual la 1 pess', '2025-11-05 03:58:31'),
(22, 9, 'ia', '¡Claro! Aquí tienes algunas sugerencias para mejorar esa **activación visual en la 1ª persona** en tu juego, narrativa o experiencia:\n\n---\n\n### **1. Usa una perspectiva en primera persona auténtica**\n   - **Inmersión sensorial:** Describe no solo lo que se ve, sino también sonidos, olores y sensaciones táctiles (ej: \"El aire huele a ozono y tierra mojada\").\n   - **Movimiento natural:** Incluye pequeños movimientos de cabeza al mirar, cambios de peso al moverte, etc., para dar realismo.\n\n### **2. Efectos visuales estratégicos**\n   - **Desenfoque (blur) y enfoque (focus):** Al mirar algo específico, el resto se desenfoca ligeramente.\n   - **Efectos de luz:** Destellos, lens flares o distorsiones cuando algo importante ocurre.\n   - **Cambios de color o saturación** para indicar emociones (ej: tonos rojizos al enfadar, azules al estar triste).\n\n### **3. Interactividad con el entorno**\n   - **Permite interactuar con objetos cercanos** (tocar una pared y que se desprenda polvo, mover una cortina y que entre luz...).\n   - **Usa el entorno para contar historia** (ej: ver marcas de balas en una pared, huellas de sangre que se arrastran...).\n\n### **4. Pistas visuales (visual cues)**\n   - **Usa texturas, colores o patrones** para guiar al jugador/personaje (ej: un camino de migas de pan brillantes, marcas de tiza que solo el prota ve...).\n   - **Elementos que solo la 1ª persona puede ver** (ej: una realidad alterna que se superpone, como en \"Life is Strange\").\n\n### **5. Integración con la jugabilidad**\n   - **Si es un juego, que los puzzles requieran usar la vista de formas creativas** (ej: usar espejos para ver ángulos muertos, mirar por cerraduras...).\n   - **Inventario visual:** Que el inventario o menú se proyecte de forma diegética (ej: en la mano, en una pared...).\n\n### **6. Estilo artístico coherente**\n   - **Elige un estilo visual que refuerce la narrativa** (ej: colores desaturados para flashbacks, tonos sepia para recuerdos...).\n   - **Usa la luz y la sombra** para dirigir la atención y crear atmósfera.\n\n### **7. Animaciones y transiciones**\n   - **Animaciones al interactuar** (abrir puertas, agacharse...) que sean fluidas y respondan a input.\n   - **Transiciones entre escenas o modos** (ej: al enfocar un objeto, la escena cambia suavemente a una vista más detallada).\n\n### **8. Feedback visual inmediato**\n   - **Efectos al recibir daño** (visión borrosa, tintes rojos...).\n   - **Indicadores de estado** (hunger, health...) integrados en la pantalla de forma orgánica.\n\n### **9. Personalización y progresión**\n   - **Mejoras visuales al progresar** (ej: ver mejor en la oscuridad, notar detalles antes invisibles...).\n   - **Personalización del \"equipo\" visual** (gafas, ocular...).\n\n### **10. Accesibilidad**\n   - **Opción de ajustar la intensidad de los efectos** (motion blur, depth of field...).\n   - **Subtítulos y descripciones** para eventos importantes.\n\n---\n\n**¿En qué tipo de proyecto estás trabajando?** (juego, relato, realidad virtual...) Puedo afinar más las sugerencias. ', '2025-11-05 03:59:02'),
(23, 10, 'docente', 'hola', '2025-11-05 03:59:42'),
(24, 10, 'ia', '¡Hola! Bienvenido/a. Soy tu asistente especializado en inteligencias múltiples y pedagogía personalizada.\n\nVeo que tienes un grupo de Matemáticas con 2 estudiantes y un perfil de inteligencias bastante interesante. La inteligencia **espacial-visual** es la más desarrollada en el grupo (promedio 5/5), seguida de la **lógico-matemática** (4.75/5) y la **naturalista** (4.3/5). \n\nEsto me indica que tu grupo, en general, podría beneficiarse de estrategias pedagógicas que aprovechen su fortaleza en visualización espacial y razonamiento lógico. Por ejemplo, podrías:\n\n1. **Usar visualizaciones y diagramas** para explicar conceptos matemáticos (geometría, ecuaciones diferenciales)\n2. **Incorporar ejemplos del mundo real** (naturalista) para conectar con aplicaciones prácticas\n3. **Incluir actividades kinestésicas** breves para mantener el engagement, aunque no sea su fortaleza principal\n\n¿Te gustaría que profundice en alguna de estas áreas o tienes alguna pregunta específica sobre cómo diseñar tus lecciones?<｜begin▁of▁sentence｜>', '2025-11-05 03:59:57'),
(25, 8, 'docente', 'que mas puedes hacer', '2025-11-05 10:58:39'),
(26, 8, 'ia', 'Lo siento, hubo un error al procesar tu solicitud. La API key de OpenRouter no está configurada correctamente. Verifica la configuración del servidor.', '2025-11-05 10:58:46'),
(27, 8, 'docente', 'hola que peudes hacer', '2025-11-05 11:00:19'),
(28, 8, 'ia', 'CH\n', '2025-11-05 11:00:32'),
(29, 8, 'docente', 'que', '2025-11-05 11:00:42'),
(30, 8, 'ia', ' 2023-11-11 1-3 2-10 3-4 5 6-7-7 0-7 8-9 9-10 9 0-9 0-9 0-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-9 9-', '2025-11-05 11:03:07'),
(31, 8, 'docente', 'en que me peuedes hayudar', '2025-11-05 11:03:18'),
(32, 8, 'ia', 'Gracias por tu confianza. Te puedo ayudar de muchas maneras, aquí tienes algunas de las principales:\n\n**📚 En matemáticas y otras materias:**\n- Explicar conceptos que no entiendas (desde aritmética básica hasta cálculo)\n- Resolver problemas paso a paso\n- Encontrar recursos de estudio personalizados\n- Crear planes de estudio\n\n**🧠 Sobre técnicas de aprendizaje:**\n- Cómo organizar tu tiempo para estudiar\n- Técnicas de memorización (para fórmulas, etc.)\n- Cómo mantener la motivación\n- Transformar \"No me gusta matemáticas\" a \"Es desafiante pero interesante\"\n\n**📝 Sobre tu situación específica:**\n- Si estás en la escuela: Ayuda con tareas, proyectos, exámenes\n- Si eres adulto aprendiendo por tu cuenta: Recursos y métodos\n- Si eres padre/madre queriendo ayudar a tu hijo: Cómo explicarles, juegos educativos\n\n** Otras áreas:**\n- Aplicar matemáticas a tu trabajo o vida diaria\n- Entender noticias científicas\n- Planificar metas a largo plazo (estudiar una carrera, etc.)\n\n**Dime simplemente:**\n- ¿Qué tema o problema tienes en mente?\n- ¿En qué área o nivel educativo estás? (primaria, secundaria, universidad, autoaprendizaje...)\n- ¿Qué has intentado hasta ahora?\n- ¿Hay algo que especialmente te frustre o confunda?\n\n**No te preocupes si no estás seguro aún. Puedo ayudarte a explorar opciones paso a paso. **<｜begin▁of▁sentence｜>', '2025-11-05 11:03:36');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `chat_sesiones`
--

CREATE TABLE `chat_sesiones` (
  `id_sesion` int(11) NOT NULL,
  `id_docente_fk` int(11) NOT NULL,
  `id_alumno_fk` int(11) DEFAULT NULL,
  `id_curso_fk` int(11) DEFAULT NULL,
  `titulo_sesion` varchar(255) NOT NULL,
  `fecha_creacion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `chat_sesiones`
--

INSERT INTO `chat_sesiones` (`id_sesion`, `id_docente_fk`, `id_alumno_fk`, `id_curso_fk`, `titulo_sesion`, `fecha_creacion`) VALUES
(1, 1, 1, NULL, 'sesion1 ', '2025-11-05 01:05:51'),
(2, 1, 1, NULL, 'hola worl', '2025-11-05 01:14:27'),
(3, 1, 1, NULL, 'hola', '2025-11-05 02:29:51'),
(4, 1, 1, NULL, 'hola2', '2025-11-05 02:39:01'),
(5, 1, 1, NULL, 'HOLA3', '2025-11-05 02:55:11'),
(6, 1, 1, NULL, 'hola1', '2025-11-05 03:07:25'),
(7, 1, 1, NULL, 'jhgjhgvjh', '2025-11-05 03:12:01'),
(8, 1, 1, NULL, '1khcg', '2025-11-05 03:23:00'),
(9, 1, NULL, 1, '1khcg', '2025-11-05 03:54:06'),
(10, 1, NULL, 1, 'gffff', '2025-11-05 03:59:27');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cursos`
--

CREATE TABLE `cursos` (
  `id_curso` int(11) NOT NULL,
  `nombre_curso` varchar(255) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `id_docente_fk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cursos`
--

INSERT INTO `cursos` (`id_curso`, `nombre_curso`, `descripcion`, `id_docente_fk`) VALUES
(1, 'matematicas ', 'area donde se desmepana ecuaciones difereciales y geometria', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `docentes`
--

CREATE TABLE `docentes` (
  `id_docente` int(11) NOT NULL,
  `nombre_usuario` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellido` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `docentes`
--

INSERT INTO `docentes` (`id_docente`, `nombre_usuario`, `contrasena`, `nombre`, `apellido`) VALUES
(1, 'dante1', '$2a$10$SM9pWMJaQKMI257EZhMySOMakvD/wX5WYKPOuEAjlHqayp.2YZIIi', 'dantessss', 'romansssssss');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `intentos_test`
--

CREATE TABLE `intentos_test` (
  `id_intento` int(11) NOT NULL,
  `id_alumno_fk` int(11) NOT NULL,
  `fecha_realizacion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `intentos_test`
--

INSERT INTO `intentos_test` (`id_intento`, `id_alumno_fk`, `fecha_realizacion`) VALUES
(1, 1, '2025-11-01 20:48:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `log_accesos`
--

CREATE TABLE `log_accesos` (
  `id_log` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `tipo_usuario` enum('admin','docente','alumno') NOT NULL,
  `fecha_hora_acceso` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `preguntas_test`
--

CREATE TABLE `preguntas_test` (
  `id_pregunta` int(11) NOT NULL,
  `id_inteligencia_fk` int(11) NOT NULL,
  `texto_pregunta` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `preguntas_test`
--

INSERT INTO `preguntas_test` (`id_pregunta`, `id_inteligencia_fk`, `texto_pregunta`) VALUES
(1, 1, '¿Es capaz de reconocer y comprender causa y efecto con relación a su edad?'),
(2, 5, '¿Suele cantar canciones que no han sido aprendidas en clase?'),
(3, 6, '¿Parece tener un gran amor propio?'),
(4, 2, '¿Se expresa en forma dramática?'),
(5, 1, '¿Se cuestiona acerca del funcionamiento de las cosas?'),
(6, 5, '¿Identifica la música desentonada o que suena mal?'),
(8, 4, '¿Encuentra placer al realizar las experiencias táctiles (plastilina, cerámica, macilla, etc.)?'),
(10, 5, '¿Disfruta escuchar música?'),
(11, 6, '¿Utiliza sus errores y logros de la vida para aprender de ellos?'),
(12, 4, '¿Sobresale en la práctica de uno o más deportes?'),
(13, 1, '¿Es capaz de resolver problemas de matemáticas mentalmente y con rapidez?'),
(14, 5, '¿Tiene buena memoria para las melodías de las canciones?'),
(15, 6, '¿Posee un concepto práctico de sus habilidades y debilidades?'),
(18, 8, '¿Demuestra sensibilidad ante los ruidos del ambiente?'),
(19, 6, '¿Es capaz de expresar sus sentimientos acertadamente?'),
(20, 4, '¿Suele moverse, estar inquieto al estar sentado por largo tiempo?'),
(21, 1, '¿Disfruta las clases de matemáticas?'),
(22, 5, '¿Tiene buena voz para cantar?'),
(23, 6, '¿Tiene un buen desempeño cuando trabaja solo?'),
(24, 4, '¿Demuestra destreza en actividades que requieren de coordinación motora sutil?'),
(25, 1, '¿Le agrada clasificar y jerarquizar cosas?'),
(26, 5, '¿Tamborilea rítmicamente sobre la mesa o escritorio mientras está trabajando?'),
(27, 6, '¿Le gusta más trabajar solo que en grupo?'),
(28, 1, '¿Encuentra placer al desarmar y volver a armar las cosas?'),
(29, 1, '¿Encuentra placer resolviendo juego de matemáticas en la computadora?'),
(30, 5, '¿Posee algún instrumento musical que sepa tocar?'),
(32, 4, '¿Es bueno imitando los movimientos típicos y gestos de otra persona?'),
(33, 1, '¿Es capaz de resolver juegos que requieren lógica?'),
(34, 5, '¿Canta sin darse cuenta?'),
(35, 6, '¿Posee un buen sentido de autodirección?'),
(36, 4, '¿Suele tocar las cosas con las manos apenas las ve?'),
(37, 1, '¿Le gustan los juegos de mesa?'),
(38, 5, '¿Habla o se mueve rítmicamente?'),
(39, 6, '¿Se interesa por un pasatiempo sobre el cual no habla mucho a los demás?'),
(40, 4, '¿Me considero una persona coordinada?'),
(41, 2, '¿Se comunica con los demás de una manera marcadamente verbal?'),
(42, 7, '¿Sus compañeros buscan estar con usted?'),
(43, 8, '¿Es sensible con las criaturas del mundo natural?'),
(44, 3, '¿Realiza garabatos en sus libros y otros materiales de trabajo?'),
(45, 7, '¿Respeta los turnos en la conversación, cede la palabra?'),
(46, 7, '¿Le gusta hablar con sus compañeros?'),
(47, 8, '¿Le gusta vivir en el campo?'),
(48, 3, '¿Comunica imágenes visuales nítidas?'),
(49, 2, '¿Suele crear y/o relatar cuentos bromas y chistes?'),
(50, 7, '¿Es empático y/o se interesa por los demás?'),
(51, 8, '¿Puede reconocer patrones en la naturaleza?'),
(52, 3, '¿Realiza creaciones tridimensionales avanzadas para su edad?'),
(54, 7, '¿Demuestra ser un líder por naturaleza?'),
(56, 3, '¿Tiene facilidad para la lectura de mapas, gráficos y diagramas?'),
(57, 2, '¿Sostiene con argumentos el criterio respecto al texto?'),
(58, 7, '¿Posee dos o más buenos amigos?'),
(59, 8, '¿Disfruta y se interesa por la naturaleza?'),
(60, 3, '¿Le agrada resolver actividades visuales (rompecabezas, laberintos, etc.)?'),
(61, 7, '¿Tiene don de convencimiento, facilidad de palabras?'),
(62, 7, '¿Es capaz de aconsejar a sus compañeros que tienen problemas?'),
(63, 8, '¿Se siente identificado como parte de la naturaleza?'),
(65, 2, '¿Comprende y goza de los juegos de palabras?'),
(66, 7, '¿Disfruta jugando con otros jóvenes?'),
(67, 8, '¿Tiene mascota favorita?'),
(68, 3, '¿Disfruta viendo películas, diapositivas y otras presentaciones visuales?'),
(69, 2, '¿Sus producciones escritas son las esperadas para su edad?'),
(71, 8, '¿Le agradan las flores y las plantas?'),
(72, 4, '¿Le gusta realizar actividades de arte?'),
(73, 2, '¿Tiene facilidad para las lenguas extranjeras?'),
(74, 7, '¿Forma parte de algún club o grupo social?'),
(75, 8, '¿Disfruta ver alimentos que la tierra produce?'),
(76, 3, '¿Dibuja figuras avanzadas para su edad?'),
(77, 2, '¿Relaciona el contenido del texto con la realidad?'),
(78, 7, '¿Le gusta participar en actividades de ayuda comunitaria?'),
(79, 8, '¿Reconoce y clasifica las diferentes especies?'),
(80, 3, '¿Puede orientarse fácilmente en un lugar al que va por primera vez?');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `respuestas_alumno`
--

CREATE TABLE `respuestas_alumno` (
  `id_respuesta` int(11) NOT NULL,
  `id_intento_fk` int(11) NOT NULL,
  `id_pregunta_fk` int(11) NOT NULL,
  `puntaje` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `respuestas_alumno`
--

INSERT INTO `respuestas_alumno` (`id_respuesta`, `id_intento_fk`, `id_pregunta_fk`, `puntaje`) VALUES
(1, 1, 1, 5),
(2, 1, 2, 4),
(3, 1, 3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultados_test`
--

CREATE TABLE `resultados_test` (
  `id_resultado` int(11) NOT NULL,
  `id_intento_fk` int(11) NOT NULL,
  `id_inteligencia_fk` int(11) NOT NULL,
  `puntaje_calculado` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `resultados_test`
--

INSERT INTO `resultados_test` (`id_resultado`, `id_intento_fk`, `id_inteligencia_fk`, `puntaje_calculado`) VALUES
(1, 1, 1, 5),
(2, 1, 5, 4),
(3, 1, 6, 3),
(4, 1, 1, 4.5),
(5, 1, 2, 3.8),
(6, 1, 3, 5),
(7, 1, 4, 4.2),
(8, 1, 5, 3.5),
(9, 1, 6, 4.7),
(10, 1, 7, 4),
(11, 1, 8, 4.3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_inteligencia`
--

CREATE TABLE `tipos_inteligencia` (
  `id_inteligencia` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipos_inteligencia`
--

INSERT INTO `tipos_inteligencia` (`id_inteligencia`, `nombre`, `descripcion`) VALUES
(1, 'Lógico-Matemática', 'Capacidad para el razonamiento lógico, el reconocimiento de patrones y la resolución de problemas matemáticos.'),
(2, 'Lingüístico-Verbal', 'Habilidad para utilizar el lenguaje de manera efectiva, ya sea oral o escrita.'),
(3, 'Espacial-Visual', 'Capacidad para pensar en tres dimensiones y visualizar con precisión.'),
(4, 'Corporal-Kinestésica', 'Capacidad para utilizar el cuerpo para expresar ideas y sentimientos, así como para realizar actividades físicas.'),
(5, 'Musical', 'Habilidad para percibir, discriminar, transformar y expresar las formas musicales.'),
(6, 'Intrapersonal', 'Capacidad para comprenderse a uno mismo, sus pensamientos y sentimientos, y usar ese conocimiento para guiar su vida.'),
(7, 'Interpersonal', 'Habilidad para entender y relacionarse eficazmente con otras personas.'),
(8, 'Naturalista', 'Habilidad para identificar, clasificar y manipular elementos del medio ambiente, objetos, animales o plantas.');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD PRIMARY KEY (`id_admin`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`);

--
-- Indices de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD PRIMARY KEY (`id_alumno`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`);

--
-- Indices de la tabla `alumnos_cursos`
--
ALTER TABLE `alumnos_cursos`
  ADD PRIMARY KEY (`id_alumno_fk`,`id_curso_fk`),
  ADD KEY `id_curso_fk` (`id_curso_fk`);

--
-- Indices de la tabla `chat_mensajes`
--
ALTER TABLE `chat_mensajes`
  ADD PRIMARY KEY (`id_mensaje`),
  ADD KEY `id_sesion_fk` (`id_sesion_fk`),
  ADD KEY `idx_sesion` (`id_sesion_fk`),
  ADD KEY `idx_fecha_envio` (`fecha_hora_envio`);

--
-- Indices de la tabla `chat_sesiones`
--
ALTER TABLE `chat_sesiones`
  ADD PRIMARY KEY (`id_sesion`),
  ADD KEY `id_docente_fk` (`id_docente_fk`),
  ADD KEY `id_alumno_fk` (`id_alumno_fk`),
  ADD KEY `idx_docente` (`id_docente_fk`),
  ADD KEY `idx_alumno` (`id_alumno_fk`),
  ADD KEY `idx_curso` (`id_curso_fk`);

--
-- Indices de la tabla `cursos`
--
ALTER TABLE `cursos`
  ADD PRIMARY KEY (`id_curso`),
  ADD KEY `id_docente_fk` (`id_docente_fk`);

--
-- Indices de la tabla `docentes`
--
ALTER TABLE `docentes`
  ADD PRIMARY KEY (`id_docente`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`);

--
-- Indices de la tabla `intentos_test`
--
ALTER TABLE `intentos_test`
  ADD PRIMARY KEY (`id_intento`),
  ADD KEY `id_alumno_fk` (`id_alumno_fk`);

--
-- Indices de la tabla `log_accesos`
--
ALTER TABLE `log_accesos`
  ADD PRIMARY KEY (`id_log`);

--
-- Indices de la tabla `preguntas_test`
--
ALTER TABLE `preguntas_test`
  ADD PRIMARY KEY (`id_pregunta`),
  ADD KEY `id_inteligencia_fk` (`id_inteligencia_fk`);

--
-- Indices de la tabla `respuestas_alumno`
--
ALTER TABLE `respuestas_alumno`
  ADD PRIMARY KEY (`id_respuesta`),
  ADD KEY `id_intento_fk` (`id_intento_fk`),
  ADD KEY `id_pregunta_fk` (`id_pregunta_fk`);

--
-- Indices de la tabla `resultados_test`
--
ALTER TABLE `resultados_test`
  ADD PRIMARY KEY (`id_resultado`),
  ADD KEY `id_intento_fk` (`id_intento_fk`),
  ADD KEY `id_inteligencia_fk` (`id_inteligencia_fk`);

--
-- Indices de la tabla `tipos_inteligencia`
--
ALTER TABLE `tipos_inteligencia`
  ADD PRIMARY KEY (`id_inteligencia`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `administradores`
--
ALTER TABLE `administradores`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  MODIFY `id_alumno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `chat_mensajes`
--
ALTER TABLE `chat_mensajes`
  MODIFY `id_mensaje` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de la tabla `chat_sesiones`
--
ALTER TABLE `chat_sesiones`
  MODIFY `id_sesion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `cursos`
--
ALTER TABLE `cursos`
  MODIFY `id_curso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `docentes`
--
ALTER TABLE `docentes`
  MODIFY `id_docente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `intentos_test`
--
ALTER TABLE `intentos_test`
  MODIFY `id_intento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `log_accesos`
--
ALTER TABLE `log_accesos`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `preguntas_test`
--
ALTER TABLE `preguntas_test`
  MODIFY `id_pregunta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;

--
-- AUTO_INCREMENT de la tabla `respuestas_alumno`
--
ALTER TABLE `respuestas_alumno`
  MODIFY `id_respuesta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `resultados_test`
--
ALTER TABLE `resultados_test`
  MODIFY `id_resultado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `tipos_inteligencia`
--
ALTER TABLE `tipos_inteligencia`
  MODIFY `id_inteligencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alumnos_cursos`
--
ALTER TABLE `alumnos_cursos`
  ADD CONSTRAINT `alumnos_cursos_ibfk_1` FOREIGN KEY (`id_alumno_fk`) REFERENCES `alumnos` (`id_alumno`),
  ADD CONSTRAINT `alumnos_cursos_ibfk_2` FOREIGN KEY (`id_curso_fk`) REFERENCES `cursos` (`id_curso`);

--
-- Filtros para la tabla `chat_mensajes`
--
ALTER TABLE `chat_mensajes`
  ADD CONSTRAINT `chat_mensajes_ibfk_1` FOREIGN KEY (`id_sesion_fk`) REFERENCES `chat_sesiones` (`id_sesion`);

--
-- Filtros para la tabla `chat_sesiones`
--
ALTER TABLE `chat_sesiones`
  ADD CONSTRAINT `chat_sesiones_ibfk_1` FOREIGN KEY (`id_docente_fk`) REFERENCES `docentes` (`id_docente`),
  ADD CONSTRAINT `chat_sesiones_ibfk_2` FOREIGN KEY (`id_alumno_fk`) REFERENCES `alumnos` (`id_alumno`),
  ADD CONSTRAINT `chat_sesiones_ibfk_3` FOREIGN KEY (`id_curso_fk`) REFERENCES `cursos` (`id_curso`) ON DELETE CASCADE;

--
-- Filtros para la tabla `cursos`
--
ALTER TABLE `cursos`
  ADD CONSTRAINT `cursos_ibfk_1` FOREIGN KEY (`id_docente_fk`) REFERENCES `docentes` (`id_docente`);

--
-- Filtros para la tabla `intentos_test`
--
ALTER TABLE `intentos_test`
  ADD CONSTRAINT `intentos_test_ibfk_1` FOREIGN KEY (`id_alumno_fk`) REFERENCES `alumnos` (`id_alumno`);

--
-- Filtros para la tabla `preguntas_test`
--
ALTER TABLE `preguntas_test`
  ADD CONSTRAINT `preguntas_test_ibfk_1` FOREIGN KEY (`id_inteligencia_fk`) REFERENCES `tipos_inteligencia` (`id_inteligencia`);

--
-- Filtros para la tabla `respuestas_alumno`
--
ALTER TABLE `respuestas_alumno`
  ADD CONSTRAINT `respuestas_alumno_ibfk_1` FOREIGN KEY (`id_intento_fk`) REFERENCES `intentos_test` (`id_intento`),
  ADD CONSTRAINT `respuestas_alumno_ibfk_2` FOREIGN KEY (`id_pregunta_fk`) REFERENCES `preguntas_test` (`id_pregunta`);

--
-- Filtros para la tabla `resultados_test`
--
ALTER TABLE `resultados_test`
  ADD CONSTRAINT `resultados_test_ibfk_1` FOREIGN KEY (`id_intento_fk`) REFERENCES `intentos_test` (`id_intento`),
  ADD CONSTRAINT `resultados_test_ibfk_2` FOREIGN KEY (`id_inteligencia_fk`) REFERENCES `tipos_inteligencia` (`id_inteligencia`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
