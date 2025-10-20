-- Migration V5: Create recommendation system tables
-- Project: Sistema de Gestión Académica con IA - Recommendation System
-- Date: December 2024

-- Create recommendation templates table
CREATE TABLE IF NOT EXISTS plantillas_recomendaciones (
    id_plantilla INT AUTO_INCREMENT PRIMARY KEY,
    nombre_plantilla VARCHAR(100) NOT NULL,
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
    puntaje_minimo INT NOT NULL DEFAULT 0,
    puntaje_maximo INT NOT NULL DEFAULT 100,
    contenido_recomendacion TEXT NOT NULL,
    tipo_recomendacion ENUM('academica', 'extracurricular', 'carrera', 'personal') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tipo_inteligencia (tipo_inteligencia),
    INDEX idx_puntaje_range (puntaje_minimo, puntaje_maximo),
    INDEX idx_tipo_recomendacion (tipo_recomendacion),
    INDEX idx_activo (activo)
);

-- Create recommendation history table
CREATE TABLE IF NOT EXISTS historial_recomendaciones (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    id_test INT NOT NULL,
    id_plantilla INT NOT NULL,
    inteligencia_predominante VARCHAR(100) NOT NULL,
    puntaje_inteligencia INT NOT NULL,
    recomendacion_generada TEXT NOT NULL,
    tipo_recomendacion ENUM('academica', 'extracurricular', 'carrera', 'personal') NOT NULL,
    fecha_generacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_aplicacion DATETIME NULL,
    estado ENUM('generada', 'aplicada', 'descartada') DEFAULT 'generada',
    notas TEXT NULL,
    FOREIGN KEY (id_alumno) REFERENCES Alumnos(IdAlumno),
    FOREIGN KEY (id_test) REFERENCES TestsGardner(IdTest),
    FOREIGN KEY (id_plantilla) REFERENCES plantillas_recomendaciones(id_plantilla),
    INDEX idx_alumno (id_alumno),
    INDEX idx_test (id_test),
    INDEX idx_fecha_generacion (fecha_generacion),
    INDEX idx_estado (estado)
);

-- Insert sample recommendation templates
INSERT INTO plantillas_recomendaciones (nombre_plantilla, tipo_inteligencia, puntaje_minimo, puntaje_maximo, contenido_recomendacion, tipo_recomendacion) VALUES

-- Musical Intelligence Templates
('Música Avanzada', 'musical', 80, 100, 'Excelente aptitud musical. Recomendamos: clases de instrumento avanzadas, composición musical, participación en orquestas estudiantiles, exploración de tecnología musical.', 'academica'),
('Música Intermedia', 'musical', 60, 79, 'Buena aptitud musical. Recomendamos: clases de instrumento básicas, apreciación musical, canto coral, actividades rítmicas.', 'academica'),
('Música Básica', 'musical', 40, 59, 'Aptitud musical en desarrollo. Recomendamos: exploración de diferentes géneros musicales, actividades de escucha activa, ritmo básico.', 'academica'),

-- Logical-Mathematical Intelligence Templates
('Matemáticas Avanzadas', 'logico_matematico', 80, 100, 'Excelente razonamiento lógico-matemático. Recomendamos: matemáticas avanzadas, programación, ciencias exactas, proyectos de investigación.', 'academica'),
('Matemáticas Intermedias', 'logico_matematico', 60, 79, 'Buena capacidad lógica. Recomendamos: matemáticas aplicadas, lógica formal, resolución de problemas complejos.', 'academica'),
('Matemáticas Básicas', 'logico_matematico', 40, 59, 'Capacidad lógica en desarrollo. Recomendamos: fundamentos matemáticos, juegos de lógica, pensamiento crítico.', 'academica'),

-- Spatial Intelligence Templates
('Arte Visual Avanzado', 'espacial', 80, 100, 'Excelente percepción espacial. Recomendamos: arte visual avanzado, diseño gráfico, arquitectura, proyectos espaciales.', 'academica'),
('Arte Visual Intermedio', 'espacial', 60, 79, 'Buena percepción espacial. Recomendamos: arte visual, diseño, geometría aplicada, proyectos creativos.', 'academica'),
('Arte Visual Básico', 'espacial', 40, 59, 'Percepción espacial en desarrollo. Recomendamos: dibujo básico, geometría, actividades visuales.', 'academica'),

-- Linguistic Intelligence Templates
('Comunicación Avanzada', 'linguistico', 80, 100, 'Excelente capacidad lingüística. Recomendamos: literatura avanzada, escritura creativa, debates, comunicación profesional.', 'academica'),
('Comunicación Intermedia', 'linguistico', 60, 79, 'Buena capacidad lingüística. Recomendamos: literatura, escritura, expresión oral, idiomas.', 'academica'),
('Comunicación Básica', 'linguistico', 40, 59, 'Capacidad lingüística en desarrollo. Recomendamos: lectura comprensiva, escritura básica, vocabulario.', 'academica'),

-- Bodily-Kinesthetic Intelligence Templates
('Actividad Física Avanzada', 'corporal_cinestesico', 80, 100, 'Excelente coordinación corporal. Recomendamos: deportes competitivos, danza avanzada, teatro, actividades físicas especializadas.', 'extracurricular'),
('Actividad Física Intermedia', 'corporal_cinestesico', 60, 79, 'Buena coordinación corporal. Recomendamos: deportes recreativos, danza, actividades manuales, expresión corporal.', 'extracurricular'),
('Actividad Física Básica', 'corporal_cinestesico', 40, 59, 'Coordinación corporal en desarrollo. Recomendamos: ejercicios básicos, actividades de movimiento, coordinación.', 'extracurricular'),

-- Interpersonal Intelligence Templates
('Liderazgo Avanzado', 'interpersonal', 80, 100, 'Excelente capacidad interpersonal. Recomendamos: liderazgo estudiantil, trabajo en equipo avanzado, mediación de conflictos.', 'personal'),
('Liderazgo Intermedio', 'interpersonal', 60, 79, 'Buena capacidad interpersonal. Recomendamos: trabajo en equipo, actividades grupales, comunicación interpersonal.', 'personal'),
('Liderazgo Básico', 'interpersonal', 40, 59, 'Capacidad interpersonal en desarrollo. Recomendamos: actividades grupales básicas, comunicación, colaboración.', 'personal'),

-- Intrapersonal Intelligence Templates
('Autoconocimiento Avanzado', 'intrapersonal', 80, 100, 'Excelente autoconocimiento. Recomendamos: proyectos individuales avanzados, reflexión profunda, autodisciplina.', 'personal'),
('Autoconocimiento Intermedio', 'intrapersonal', 60, 79, 'Buen autoconocimiento. Recomendamos: proyectos individuales, reflexión personal, metas personales.', 'personal'),
('Autoconocimiento Básico', 'intrapersonal', 40, 59, 'Autoconocimiento en desarrollo. Recomendamos: reflexión básica, autoevaluación, objetivos personales.', 'personal'),

-- Naturalistic Intelligence Templates
('Ciencias Naturales Avanzadas', 'naturalista', 80, 100, 'Excelente conexión con la naturaleza. Recomendamos: biología avanzada, ecología, investigación ambiental, actividades outdoor.', 'academica'),
('Ciencias Naturales Intermedias', 'naturalista', 60, 79, 'Buena conexión con la naturaleza. Recomendamos: biología, ciencias ambientales, actividades al aire libre.', 'academica'),
('Ciencias Naturales Básicas', 'naturalista', 40, 59, 'Conexión con la naturaleza en desarrollo. Recomendamos: observación natural, actividades outdoor básicas.', 'academica');

-- Create trigger to automatically generate recommendations after test completion
DELIMITER $$

CREATE TRIGGER tr_generate_recommendations 
AFTER UPDATE ON TestsGardner
FOR EACH ROW
BEGIN
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
END$$

DELIMITER ;
