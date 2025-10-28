-- Migration V2: Create preguntas_gardner table for Gardner test questions
-- Project: Sistema de Gestión Académica con IA - Test Gardner Module
-- Date: December 2024

-- Create preguntas_gardner table
CREATE TABLE IF NOT EXISTS preguntas_gardner (
    id_pregunta INT AUTO_INCREMENT PRIMARY KEY,
    texto_pregunta TEXT NOT NULL,
    opcion_a VARCHAR(500) NOT NULL,
    opcion_b VARCHAR(500) NOT NULL,
    opcion_c VARCHAR(500) NOT NULL,
    opcion_d VARCHAR(500) NOT NULL,
    tipo_inteligencia ENUM(
        'musical', 
        'logico_matematico', 
        'espacial', 
        'linguistico', 
        'corporal_cinestesico', 
        'interpersonal', 
        'intrapersonal', 
        'naturalista'
    ) NOT NULL,
    orden_secuencia INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tipo_inteligencia (tipo_inteligencia),
    INDEX idx_orden (orden_secuencia),
    INDEX idx_activo (activo)
);

-- Insert sample Gardner test questions
INSERT INTO preguntas_gardner (texto_pregunta, opcion_a, opcion_b, opcion_c, opcion_d, tipo_inteligencia, orden_secuencia) VALUES

-- Musical Intelligence Questions (1-10)
('¿Cómo prefieres trabajar cuando necesitas concentrarte?', 'Trabajo mejor con música de fondo', 'Necesito silencio absoluto para concentrarme', 'Me concentro igual con o sin música', 'Solo puedo trabajar con ciertos tipos de música', 'musical', 1),
('Cuando escuchas una canción nueva, ¿qué te llama más la atención?', 'La melodía y los arreglos musicales', 'La letra y el mensaje', 'El ritmo y la percusión', 'La voz del cantante', 'musical', 2),
('¿Cómo recuerdas mejor la información?', 'Cantando o tarareando', 'Escribiendo notas', 'Haciendo esquemas visuales', 'Repitiendo en voz alta', 'musical', 3),
('¿Qué tipo de actividades disfrutas más en tu tiempo libre?', 'Tocar un instrumento o cantar', 'Leer libros o escuchar podcasts', 'Hacer ejercicios o deporte', 'Meditar o reflexionar', 'musical', 4),

-- Logical-Mathematical Intelligence Questions (5-8)
('¿Cómo te gusta resolver problemas complejos?', 'Paso a paso, analizando cada parte', 'Probando diferentes soluciones', 'Pidiendo ayuda a otros', 'Intuitivamente, siguiendo mi instinto', 'logico_matematico', 5),
('¿Qué tipo de juegos te gustan más?', 'Sudoku, crucigramas, puzzles', 'Videojuegos de estrategia', 'Juegos de mesa competitivos', 'Juegos creativos y artísticos', 'logico_matematico', 6),
('Cuando ves datos y números, ¿qué prefieres hacer?', 'Analizarlos y buscar patrones', 'Crear gráficos y visualizaciones', 'Contarlos y organizarlos', 'Buscar la historia detrás de los números', 'logico_matematico', 7),
('¿Cómo aprendes mejor los procedimientos?', 'Entendiendo la lógica paso a paso', 'Practicando repetidamente', 'Viendo demostraciones visuales', 'Trabajando con otros', 'logico_matematico', 8),

-- Spatial Intelligence Questions (9-12)
('¿Cómo te orientas mejor en lugares nuevos?', 'Memorizando puntos de referencia', 'Creando mapas mentales', 'Siguiendo direcciones verbales', 'Preguntando siempre', 'espacial', 9),
('¿Qué tipo de arte te gusta más?', 'Pintura y dibujo', 'Escultura y cerámica', 'Fotografía y diseño gráfico', 'Arquitectura y urbanismo', 'espacial', 10),
('¿Cómo organizas mejor tu espacio de trabajo?', 'Todo visible y accesible', 'Minimalista y ordenado', 'Creativo y colorido', 'Funcional y práctico', 'espacial', 11),
('¿Qué te gusta hacer en tu tiempo libre?', 'Jugar videojuegos de acción', 'Hacer manualidades o artesanías', 'Ver documentales', 'Caminar en la naturaleza', 'espacial', 12),

-- Linguistic Intelligence Questions (13-16)
('¿Cómo prefieres comunicar tus ideas?', 'Escribiendo textos detallados', 'Hablando en público', 'Usando metáforas y analogías', 'Contando historias', 'linguistico', 13),
('¿Qué tipo de libros te interesan más?', 'Novelas y literatura', 'Libros técnicos y científicos', 'Biografías y memorias', 'Poesía y textos filosóficos', 'linguistico', 14),
('¿Cómo aprendes mejor idiomas nuevos?', 'Estudiando estructuras gramaticales', 'Practicando conversaciones', 'Escuchando música y medios', 'Experimentando con la cultura', 'linguistico', 15),
('¿Qué disfrutas más en una conversación?', 'Debatir temas complejos', 'Contar historias divertidas', 'Escuchar experiencias de otros', 'Resolver problemas juntos', 'linguistico', 16),

-- Bodily-Kinesthetic Intelligence Questions (17-20)
('¿Cómo prefieres aprender algo nuevo?', 'Leyendo instrucciones', 'Practicando directamente', 'Viendo videos tutoriales', 'Trabajando con un mentor', 'corporal_cinestesico', 17),
('¿Qué actividades físicas disfrutas más?', 'Deportes de equipo', 'Actividades individuales', 'Bailar y moverse', 'Actividades al aire libre', 'corporal_cinestesico', 18),
('¿Cómo manejas el estrés mejor?', 'Haciendo ejercicio', 'Hablando con alguien', 'Meditando o respirando', 'Trabajando manualmente', 'corporal_cinestesico', 19),
('¿Qué te motiva más en tu trabajo?', 'Ver resultados tangibles', 'Resolver problemas complejos', 'Ayudar a otras personas', 'Aprender cosas nuevas', 'corporal_cinestesico', 20),

-- Interpersonal Intelligence Questions (21-24)
('¿Cómo prefieres trabajar en equipo?', 'Como líder del proyecto', 'Como colaborador activo', 'Como coordinador de tareas', 'Como facilitador de comunicación', 'interpersonal', 21),
('¿Qué disfrutas más en grupo?', 'Organizar eventos y actividades', 'Mediar conflictos y diferencias', 'Motivar y guiar a otros', 'Crear un ambiente positivo', 'interpersonal', 22),
('¿Cómo ayudas mejor a otros cuando tienen problemas?', 'Ofreciendo múltiples opciones', 'Escuchando sin juzgar', 'Compartiendo experiencias similares', 'Guiándolos hacia soluciones', 'interpersonal', 23),
('¿Qué te motiva más en relaciones sociales?', 'Ayudar a otros a crecer', 'Aprender de diferentes perspectivas', 'Crear conexiones significativas', 'Resolver conflictos constructivamente', 'interpersonal', 24),

-- Intrapersonal Intelligence Questions (25-28)
('¿Cómo tomas decisiones importantes?', 'Analizando pros y contras detalladamente', 'Siguiendo mi intuición', 'Consultando con personas de confianza', 'Necesito tiempo para reflexionar', 'intrapersonal', 25),
('¿Qué te ayuda más a conocerte mejor?', 'Practicar la meditación', 'Escribir un diario personal', 'Tener conversaciones profundas', 'Experimentar situaciones nuevas', 'intrapersonal', 26),
('¿Cómo defines tus metas personales?', 'Analisisando mi situación actual', 'Siguiendo mi instinto', 'Basándome en valores profundos', 'Reflexionando sobre mi propósito', 'intrapersonal', 27),
('¿Qué te motiva más internamente?', 'Alcanzar objetivos personales', 'Crecer y desarrollarme', 'Ayudar a otros a crecer', 'Vivir según mis valores', 'intrapersonal', 28),

-- Naturalistic Intelligence Questions (29-32)
('¿Qué te interesa más sobre la naturaleza?', 'Observar animales silvestres', 'Estudiar plantas y ecosistemas', 'Practicar actividades al aire libre', 'Aprender sobre conservación', 'naturalista', 29),
('¿Cómo disfrutas más tu tiempo libre?', 'Excursionismo y senderismo', 'Observar aves o fotografía natural', 'Jardinería o huerto', 'Campaing y actividades outdoor', 'naturalista', 30),
('¿Qué preocupación ambiental te motiva más?', 'Cambio climático y sostenibilidad', 'Biodiversidad y especies en peligro', 'Contaminación y salud ambiental', 'Educación ambiental en comunidades', 'naturalista', 31),
('¿Cómo prefieres aprender sobre ciencia?', 'Experimentando y observando directamente', 'Lectura de investigación', 'Documentales y videos', 'Trabajando en laboratorios', 'naturalista', 32);

-- Add constraint to ensure unique order per intelligence type
ALTER TABLE preguntas_gardner ADD CONSTRAINT idx_unique_orden_inteligencia UNIQUE (tipo_inteligencia, orden_secuencia);
