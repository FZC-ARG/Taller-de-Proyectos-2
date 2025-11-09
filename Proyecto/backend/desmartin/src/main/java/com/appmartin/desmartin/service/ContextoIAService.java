package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.ia.IAPersonalityService;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para generar contexto dinámico para la IA
 * 
 * Este servicio genera prompts personalizados basados en:
 * - Resultados del test de inteligencias múltiples del alumno
 * - Datos del alumno (nombre, edad, etc.)
 * - Perfil grupal del curso (estadísticas, promedios, etc.)
 * 
 * @author Desmartin Team
 * @version 1.0
 */
@Service
public class ContextoIAService {

    private static final Logger logger = LoggerFactory.getLogger(ContextoIAService.class);
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private AlumnoCursoRepository alumnoCursoRepository;

    @Autowired
    private IAPersonalityService iaPersonalityService;
    
    /**
     * Genera el contexto completo para un chat individual (alumno específico)
     * 
     * @param idAlumno ID del alumno
     * @return String con el contexto formateado para la IA
     */
    public String generarContextoAlumno(Integer idAlumno) {
        logger.info("Generando contexto para alumno ID: {}", idAlumno);
        
        // Obtener datos del alumno
        Alumno alumno = alumnoRepository.findById(idAlumno)
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        
        // Calcular edad
        int edad = calcularEdad(alumno.getFechaNacimiento());
        
        // Obtener resultados del test
        List<ResultadoDTO> resultados = testService.obtenerUltimoResultado(idAlumno);
        
        // Construir contexto
        StringBuilder contexto = new StringBuilder();
        
        // Información básica del alumno
        contexto.append("CONTEXTO DEL ESTUDIANTE:\n");
        contexto.append("========================\n");
        contexto.append(String.format("Nombre: %s %s\n", alumno.getNombre(), alumno.getApellido()));
        contexto.append(String.format("Edad: %d años\n", edad));
        contexto.append(String.format("Usuario: %s\n\n", alumno.getNombreUsuario()));
        
        // Resultados del test de inteligencias múltiples
        if (resultados.isEmpty()) {
            contexto.append("⚠️ El estudiante no ha completado el test de inteligencias múltiples.\n");
            contexto.append("No hay datos de perfil de aprendizaje disponibles.\n\n");
        } else {
            contexto.append("PERFIL DE INTELIGENCIAS MÚLTIPLES:\n");
            contexto.append("==================================\n");
            
            // Ordenar por puntaje (mayor a menor)
            List<ResultadoDTO> resultadosOrdenados = resultados.stream()
                .sorted((a, b) -> Float.compare(b.getPuntajeCalculado(), a.getPuntajeCalculado()))
                .collect(Collectors.toList());
            
            // Top 3 inteligencias
            contexto.append("Top 3 Inteligencias Desarrolladas:\n");
            for (int i = 0; i < Math.min(3, resultadosOrdenados.size()); i++) {
                ResultadoDTO resultado = resultadosOrdenados.get(i);
                contexto.append(String.format("%d. %s: %.2f/5.00\n", 
                    i + 1, 
                    resultado.getNombreInteligencia(), 
                    resultado.getPuntajeCalculado()));
            }
            
            contexto.append("\n");
            
            // Todas las inteligencias
            contexto.append("Perfil Completo:\n");
            for (ResultadoDTO resultado : resultadosOrdenados) {
                contexto.append(String.format("- %s: %.2f/5.00\n", 
                    resultado.getNombreInteligencia(), 
                    resultado.getPuntajeCalculado()));
            }
            
            contexto.append("\n");
            
            // Análisis y recomendaciones
            contexto.append("ANÁLISIS Y RECOMENDACIONES:\n");
            contexto.append("===========================\n");
            
            // Inteligencia dominante
            ResultadoDTO inteligenciaDominante = resultadosOrdenados.get(0);
            contexto.append(String.format("Inteligencia Dominante: %s (%.2f/5.00)\n", 
                inteligenciaDominante.getNombreInteligencia(), 
                inteligenciaDominante.getPuntajeCalculado()));
            
            contexto.append("\n");
            contexto.append("IMPORTANTE: Usa esta información para:\n");
            contexto.append("- Entender el estilo de aprendizaje del estudiante\n");
            contexto.append("- Sugerir estrategias pedagógicas personalizadas\n");
            contexto.append("- Recomendar actividades según sus fortalezas\n");
            contexto.append("- Explicar conceptos usando sus inteligencias dominantes\n\n");
        }
        
        String contextoFinal = contexto.toString();
        logger.info("Contexto generado para alumno {}: {} caracteres", idAlumno, contextoFinal.length());
        
        return contextoFinal;
    }
    
    /**
     * Genera el contexto completo para un chat grupal (curso)
     * 
     * @param idCurso ID del curso
     * @return String con el contexto formateado para la IA
     */
    public String generarContextoCurso(Integer idCurso) {
        logger.info("Generando contexto para curso ID: {}", idCurso);
        
        // Obtener datos del curso
        Curso curso = cursoRepository.findById(idCurso)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        // Obtener alumnos del curso
        List<AlumnoDTO> alumnos = obtenerAlumnosDelCurso(idCurso);
        
        if (alumnos.isEmpty()) {
            throw new RuntimeException("El curso no tiene alumnos matriculados");
        }
        
        // Construir contexto
        StringBuilder contexto = new StringBuilder();
        
        // Información del curso
        contexto.append("CONTEXTO DEL CURSO:\n");
        contexto.append("===================\n");
        contexto.append(String.format("Nombre: %s\n", curso.getNombreCurso()));
        if (curso.getDescripcion() != null && !curso.getDescripcion().isEmpty()) {
            contexto.append(String.format("Descripción: %s\n", curso.getDescripcion()));
        }
        contexto.append(String.format("Docente: %s\n", curso.getDocente().getNombreUsuario()));
        contexto.append(String.format("Total de estudiantes: %d\n\n", alumnos.size()));
        
        // Perfil grupal de inteligencias
        contexto.append("PERFIL GRUPAL DE INTELIGENCIAS:\n");
        contexto.append("===============================\n");
        
        // Calcular estadísticas por tipo de inteligencia
        Map<String, List<Float>> inteligenciasPorNombre = new HashMap<>();
        Map<String, Integer> inteligenciasPorId = new HashMap<>();
        
        int alumnosConTest = 0;
        for (AlumnoDTO alumno : alumnos) {
            try {
                List<ResultadoDTO> resultados = testService.obtenerUltimoResultado(alumno.getIdAlumno());
                
                if (!resultados.isEmpty()) {
                    alumnosConTest++;
                    
                    for (ResultadoDTO resultado : resultados) {
                        String nombreInteligencia = resultado.getNombreInteligencia();
                        inteligenciasPorNombre.putIfAbsent(nombreInteligencia, new ArrayList<>());
                        inteligenciasPorNombre.get(nombreInteligencia).add(resultado.getPuntajeCalculado());
                        
                        inteligenciasPorId.put(nombreInteligencia, resultado.getIdInteligencia());
                    }
                }
            } catch (Exception e) {
                logger.debug("Alumno {} no tiene resultados de test", alumno.getIdAlumno());
            }
        }
        
        if (alumnosConTest == 0) {
            contexto.append("⚠️ Ningún estudiante del curso ha completado el test de inteligencias múltiples.\n");
            contexto.append("No hay datos de perfil grupal disponibles.\n\n");
        } else {
            contexto.append(String.format("Estudiantes con test completado: %d de %d\n\n", 
                alumnosConTest, alumnos.size()));
            
            // Calcular promedios por inteligencia
            List<Map.Entry<String, Float>> promedios = inteligenciasPorNombre.entrySet().stream()
                .map(entry -> {
                    List<Float> puntajes = entry.getValue();
                    float promedio = puntajes.stream()
                        .reduce(0f, Float::sum) / puntajes.size();
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), promedio);
                })
                .sorted((a, b) -> Float.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());
            
            // Top 3 inteligencias del grupo
            contexto.append("Top 3 Inteligencias del Grupo:\n");
            for (int i = 0; i < Math.min(3, promedios.size()); i++) {
                Map.Entry<String, Float> entry = promedios.get(i);
                contexto.append(String.format("%d. %s: %.2f/5.00 (promedio)\n", 
                    i + 1, entry.getKey(), entry.getValue()));
            }
            
            contexto.append("\n");
            
            // Todas las inteligencias
            contexto.append("Perfil Completo del Grupo:\n");
            for (Map.Entry<String, Float> entry : promedios) {
                contexto.append(String.format("- %s: %.2f/5.00 (promedio)\n", 
                    entry.getKey(), entry.getValue()));
            }
            
            contexto.append("\n");
            
            // Análisis grupal
            contexto.append("ANÁLISIS Y RECOMENDACIONES GRUPALES:\n");
            contexto.append("====================================\n");
            
            // Inteligencia dominante del grupo
            Map.Entry<String, Float> inteligenciaDominante = promedios.get(0);
            contexto.append(String.format("Inteligencia Dominante del Grupo: %s (%.2f/5.00)\n", 
                inteligenciaDominante.getKey(), inteligenciaDominante.getValue()));
            
            contexto.append("\n");
            contexto.append("IMPORTANTE: Usa esta información para:\n");
            contexto.append("- Planificar estrategias pedagógicas grupales\n");
            contexto.append("- Diseñar actividades que aprovechen las fortalezas del grupo\n");
            contexto.append("- Identificar áreas de mejora colectiva\n");
            contexto.append("- Sugerir dinámicas de trabajo colaborativo\n\n");
        }
        
        // Lista de estudiantes (opcional, para referencia)
        contexto.append("ESTUDIANTES DEL CURSO:\n");
        contexto.append("======================\n");
        for (AlumnoDTO alumno : alumnos) {
            contexto.append(String.format("- %s %s (ID: %d)\n", 
                alumno.getNombre(), alumno.getApellido(), alumno.getIdAlumno()));
        }
        
        String contextoFinal = contexto.toString();
        logger.info("Contexto generado para curso {}: {} caracteres", idCurso, contextoFinal.length());
        
        return contextoFinal;
    }
    
    /**
     * Construye el mensaje del sistema (system message) para la IA
     * 
     * @param contexto Contexto del alumno o curso
     * @return Map con el mensaje del sistema
     */
    public Map<String, String> construirMensajeSistema(String contexto) {
        String personalityPrompt = iaPersonalityService.buildPersonalityPrompt();
        String mensajeSistema = """
            Eres un asistente educativo especializado en inteligencias múltiples y pedagogía personalizada.
            
            Tu tarea es ayudar a docentes a interpretar y aplicar los resultados del test de inteligencias múltiples
            para mejorar la experiencia de aprendizaje de sus estudiantes.
            
            %s
            
            %s
            
            INSTRUCCIONES:
            - Responde de manera clara, profesional y educativa
            - Usa el contexto proporcionado para dar recomendaciones específicas
            - Sugiere estrategias pedagógicas prácticas y aplicables
            - Explica conceptos de manera didáctica
            - Si no tienes información suficiente, indícalo honestamente
            
            IMPORTANTE: Siempre responde en español.
            """.formatted(personalityPrompt, contexto);
        
        return Map.of("role", "system", "content", mensajeSistema);
    }
    
    /**
     * Construye la lista de mensajes para enviar a la IA
     * 
     * @param contexto Contexto del alumno o curso
     * @param mensajeUsuario Mensaje del docente
     * @param historialMensajes Historial de mensajes previos (opcional)
     * @return Lista de mensajes formateados
     */
    public List<Map<String, String>> construirMensajesParaIA(
            String contexto, 
            String mensajeUsuario, 
            List<ChatMensajeDTO> historialMensajes) {
        
        List<Map<String, String>> mensajes = new ArrayList<>();
        
        // Agregar mensaje del sistema con contexto
        mensajes.add(construirMensajeSistema(contexto));
        
        // Agregar historial de mensajes (si existe)
        if (historialMensajes != null && !historialMensajes.isEmpty()) {
            for (ChatMensajeDTO mensaje : historialMensajes) {
                String role = mensaje.getEmisor().equals("docente") ? "user" : "assistant";
                mensajes.add(Map.of("role", role, "content", mensaje.getContenido()));
            }
        }
        
        // Agregar nuevo mensaje del usuario
        mensajes.add(Map.of("role", "user", "content", mensajeUsuario));
        
        return mensajes;
    }
    
    /**
     * Obtiene los alumnos de un curso
     */
    private List<AlumnoDTO> obtenerAlumnosDelCurso(Integer idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        return alumnoCursoRepository.findByCurso(curso).stream()
            .map(ac -> {
                Alumno alumno = ac.getAlumno();
                return new AlumnoDTO(
                    alumno.getIdAlumno(),
                    alumno.getNombreUsuario(),
                    alumno.getNombre(),
                    alumno.getApellido(),
                    alumno.getFechaNacimiento()
                );
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula la edad del alumno basándose en su fecha de nacimiento
     */
    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}

