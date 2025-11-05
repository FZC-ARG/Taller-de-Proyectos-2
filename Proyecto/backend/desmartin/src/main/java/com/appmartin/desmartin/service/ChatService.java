package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    @Autowired
    private ChatSesionRepository chatSesionRepository;
    
    @Autowired
    private ChatMensajeRepository chatMensajeRepository;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private OpenRouterService openRouterService;
    
    @Autowired
    private ContextoIAService contextoIAService;
    
    /**
     * Obtiene la última sesión activa entre un docente y un alumno sin crearla
     * 
     * @param idDocente ID del docente
     * @param idAlumno ID del alumno
     * @return DTO de la sesión si existe, null si no existe
     */
    public ChatSesionDTO obtenerUltimaSesion(Integer idDocente, Integer idAlumno) {
        // Validar que el docente existe
        docenteRepository.findById(idDocente)
            .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        // Validar que el alumno existe
        alumnoRepository.findById(idAlumno)
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        
        // Buscar última sesión activa entre docente y alumno
        List<ChatSesion> sesionesExistentes = chatSesionRepository
            .findUltimaSesionActivaPorDocenteYAlumno(idDocente, idAlumno);
        
        if (!sesionesExistentes.isEmpty()) {
            ChatSesion sesionExistente = sesionesExistentes.get(0);
            logger.info("Obteniendo última sesión activa ID: {} entre docente {} y alumno {}", 
                sesionExistente.getIdSesion(), idDocente, idAlumno);
            
            return new ChatSesionDTO(
                sesionExistente.getIdSesion(),
                sesionExistente.getDocente().getIdDocente(),
                sesionExistente.getAlumno() != null ? sesionExistente.getAlumno().getIdAlumno() : null,
                sesionExistente.getCurso() != null ? sesionExistente.getCurso().getIdCurso() : null,
                sesionExistente.getTituloSesion(),
                sesionExistente.getFechaCreacion()
            );
        }
        
        logger.info("No se encontró sesión previa entre docente {} y alumno {}", idDocente, idAlumno);
        return null;
    }
    
    /**
     * Crea una nueva sesión de chat o reutiliza una sesión existente activa
     * 
     * Para chats individuales (con idAlumno):
     * - Busca primero si existe una sesión activa previa entre el docente y el alumno
     * - Si existe, reutiliza esa sesión (no crea una nueva)
     * - Si no existe, crea una nueva sesión
     * 
     * Para chats grupales (con idCurso):
     * - Siempre crea una nueva sesión (no reutiliza)
     * 
     * @param request Request con datos de la sesión
     * @return DTO de la sesión (nueva o reutilizada)
     */
    public ChatSesionDTO crearSesion(CrearChatSesionRequest request) {
        // Validar que el docente existe
        Docente docente = docenteRepository.findById(request.getIdDocente())
            .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        // Validar que la sesión es O individual O grupal (no ambas, no ninguna)
        boolean esIndividual = request.getIdAlumno() != null;
        boolean esGrupal = request.getIdCurso() != null;
        
        if (esIndividual && esGrupal) {
            throw new IllegalArgumentException("Una sesión no puede ser individual y grupal a la vez");
        }
        
        if (!esIndividual && !esGrupal) {
            throw new IllegalArgumentException("Una sesión debe ser individual (idAlumno) o grupal (idCurso)");
        }
        
        // Para chats individuales: buscar sesión activa existente
        if (esIndividual) {
            Alumno alumno = alumnoRepository.findById(request.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
            
            // Si forzarCreacion es true, saltar la búsqueda de sesión previa
            if (request.getForzarCreacion() == null || !request.getForzarCreacion()) {
                // Buscar última sesión activa entre docente y alumno
                List<ChatSesion> sesionesExistentes = chatSesionRepository
                    .findUltimaSesionActivaPorDocenteYAlumno(request.getIdDocente(), request.getIdAlumno());
                
                if (!sesionesExistentes.isEmpty()) {
                    // Reutilizar la última sesión activa
                    ChatSesion sesionExistente = sesionesExistentes.get(0);
                    logger.info("Reutilizando sesión existente ID: {} entre docente {} y alumno {}", 
                        sesionExistente.getIdSesion(), request.getIdDocente(), request.getIdAlumno());
                    
                    // Actualizar título si se proporciona uno nuevo
                    if (request.getTituloSesion() != null && !request.getTituloSesion().trim().isEmpty()) {
                        sesionExistente.setTituloSesion(request.getTituloSesion());
                        chatSesionRepository.save(sesionExistente);
                    }
                    
                    return new ChatSesionDTO(
                        sesionExistente.getIdSesion(),
                        sesionExistente.getDocente().getIdDocente(),
                        sesionExistente.getAlumno() != null ? sesionExistente.getAlumno().getIdAlumno() : null,
                        sesionExistente.getCurso() != null ? sesionExistente.getCurso().getIdCurso() : null,
                        sesionExistente.getTituloSesion(),
                        sesionExistente.getFechaCreacion()
                    );
                }
            } else {
                logger.info("Forzando creación de nueva sesión (forzarCreacion=true) entre docente {} y alumno {}", 
                    request.getIdDocente(), request.getIdAlumno());
            }
            
            // No existe sesión previa, crear nueva sesión individual
            logger.info("No se encontró sesión previa, creando nueva sesión individual para docente {} y alumno {}", 
                request.getIdDocente(), request.getIdAlumno());
            
            ChatSesion sesion = new ChatSesion();
            sesion.setDocente(docente);
            sesion.setAlumno(alumno);
            sesion.setCurso(null);
            sesion.setTituloSesion(request.getTituloSesion() != null && !request.getTituloSesion().trim().isEmpty() 
                ? request.getTituloSesion() 
                : "Consulta Individual");
            
            ChatSesion saved = chatSesionRepository.save(sesion);
            return new ChatSesionDTO(
                saved.getIdSesion(),
                saved.getDocente().getIdDocente(),
                saved.getAlumno() != null ? saved.getAlumno().getIdAlumno() : null,
                saved.getCurso() != null ? saved.getCurso().getIdCurso() : null,
                saved.getTituloSesion(),
                saved.getFechaCreacion()
            );
        }
        
        // Configurar como chat grupal (siempre crea nueva sesión)
        if (esGrupal) {
            Curso curso = cursoRepository.findById(request.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            
            // Validar que el docente dicta el curso
            if (!curso.getDocente().getIdDocente().equals(docente.getIdDocente())) {
                throw new IllegalArgumentException("El docente no dicta este curso");
            }
            
            logger.info("Creando nueva sesión grupal para docente {} y curso {}", 
                request.getIdDocente(), request.getIdCurso());
            
            ChatSesion sesion = new ChatSesion();
            sesion.setDocente(docente);
            sesion.setCurso(curso);
            sesion.setAlumno(null);
            sesion.setTituloSesion(request.getTituloSesion() != null && !request.getTituloSesion().trim().isEmpty() 
                ? request.getTituloSesion() 
                : "Consulta Grupal");
            
            ChatSesion saved = chatSesionRepository.save(sesion);
            return new ChatSesionDTO(
                saved.getIdSesion(),
                saved.getDocente().getIdDocente(),
                saved.getAlumno() != null ? saved.getAlumno().getIdAlumno() : null,
                saved.getCurso() != null ? saved.getCurso().getIdCurso() : null,
                saved.getTituloSesion(),
                saved.getFechaCreacion()
            );
        }
        
        // Este punto no debería alcanzarse debido a las validaciones anteriores
        throw new IllegalStateException("Error al crear sesión: estado inválido");
    }
    
    public List<ChatSesionDTO> obtenerSesionesPorDocente(Integer idDocente) {
        return chatSesionRepository.findByDocente_IdDocente(idDocente).stream()
            .map(s -> new ChatSesionDTO(
                s.getIdSesion(),
                s.getDocente().getIdDocente(),
                s.getAlumno() != null ? s.getAlumno().getIdAlumno() : null,
                s.getCurso() != null ? s.getCurso().getIdCurso() : null,
                s.getTituloSesion(),
                s.getFechaCreacion()
            ))
            .collect(Collectors.toList());
    }
    
    public ChatSesionDTO obtenerSesion(Integer idSesion) {
        ChatSesion sesion = chatSesionRepository.findById(idSesion)
            .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        
        return new ChatSesionDTO(
            sesion.getIdSesion(),
            sesion.getDocente().getIdDocente(),
            sesion.getAlumno() != null ? sesion.getAlumno().getIdAlumno() : null,
            sesion.getCurso() != null ? sesion.getCurso().getIdCurso() : null,
            sesion.getTituloSesion(),
            sesion.getFechaCreacion()
        );
    }
    
    /**
     * Crea un nuevo mensaje en una sesión de chat
     * 
     * Este método:
     * 1. Guarda el mensaje del docente
     * 2. Obtiene el historial completo de mensajes previos de la sesión
     * 3. Genera el contexto dinámico según el tipo de sesión (individual o grupal)
     * 4. Construye los mensajes para la IA incluyendo contexto e historial
     * 5. Envía la solicitud a OpenRouter API
     * 6. Guarda la respuesta de la IA
     * 
     * El historial de mensajes se carga automáticamente, permitiendo que la IA
     * tenga contexto completo de la conversación previa.
     * 
     * @param idSesion ID de la sesión de chat
     * @param request Request con el contenido del mensaje
     * @return DTO del mensaje de la IA generado
     */
    public ChatMensajeDTO crearMensaje(Integer idSesion, CrearMensajeRequest request) {
        logger.info("Creando mensaje para sesión ID: {}", idSesion);
        
        ChatSesion sesion = chatSesionRepository.findById(idSesion)
            .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        
        // Guardar mensaje del docente
        ChatMensaje mensajeDocente = new ChatMensaje();
        mensajeDocente.setChatSesion(sesion);
        mensajeDocente.setEmisor(ChatMensaje.Emisor.docente);
        mensajeDocente.setContenido(request.getContenido());
        chatMensajeRepository.save(mensajeDocente);
        logger.debug("Mensaje del docente guardado: ID {}", mensajeDocente.getIdMensaje());
        
        try {
            // Obtener historial completo de mensajes previos de la sesión
            // Esto incluye todos los mensajes anteriores (docente e IA) para mantener el contexto
            List<ChatMensajeDTO> historialMensajes = obtenerMensajesPorSesion(idSesion).stream()
                .filter(m -> m.getIdMensaje() != mensajeDocente.getIdMensaje()) // Excluir el mensaje recién guardado
                .collect(Collectors.toList());
            
            logger.info("Historial de mensajes cargado: {} mensajes previos", historialMensajes.size());
            
            // Generar contexto según el tipo de sesión
            String contexto;
            if (sesion.getAlumno() != null) {
                // Chat individual (alumno)
                logger.info("Generando contexto para alumno ID: {}", sesion.getAlumno().getIdAlumno());
                contexto = contextoIAService.generarContextoAlumno(sesion.getAlumno().getIdAlumno());
            } else if (sesion.getCurso() != null) {
                // Chat grupal (curso)
                logger.info("Generando contexto para curso ID: {}", sesion.getCurso().getIdCurso());
                contexto = contextoIAService.generarContextoCurso(sesion.getCurso().getIdCurso());
            } else {
                // Fallback: contexto genérico (no debería pasar por validación anterior)
                logger.warn("Sesión sin alumno ni curso, usando contexto genérico");
                contexto = "Eres un asistente educativo. Ayuda al docente con sus consultas pedagógicas.";
            }
            
            // Construir mensajes para la IA (contexto + historial + nuevo mensaje)
            List<Map<String, String>> mensajesParaIA = contextoIAService.construirMensajesParaIA(
                contexto,
                request.getContenido(),
                historialMensajes
            );
            
            logger.info("Enviando {} mensajes a OpenRouter API", mensajesParaIA.size());
            
            // Llamar a OpenRouter para obtener respuesta de la IA
            String respuestaIA = openRouterService.enviarMensaje(mensajesParaIA);
            
            logger.info("Respuesta de IA obtenida: {} caracteres", respuestaIA.length());
            
            // Guardar respuesta de la IA
            ChatMensaje mensajeIA = new ChatMensaje();
            mensajeIA.setChatSesion(sesion);
            mensajeIA.setEmisor(ChatMensaje.Emisor.ia);
            mensajeIA.setContenido(respuestaIA);
            ChatMensaje mensajeIASaved = chatMensajeRepository.save(mensajeIA);
            
            logger.info("Mensaje de IA guardado: ID {}", mensajeIASaved.getIdMensaje());
            
            return new ChatMensajeDTO(
                mensajeIASaved.getIdMensaje(),
                mensajeIASaved.getChatSesion().getIdSesion(),
                mensajeIASaved.getEmisor().name(),
                mensajeIASaved.getContenido(),
                mensajeIASaved.getFechaHoraEnvio()
            );
            
        } catch (Exception e) {
            logger.error("Error al obtener respuesta de la IA para sesión ID: {}", idSesion, e);
            logger.error("Causa del error: {}", e.getCause() != null ? e.getCause().getMessage() : "Desconocida");
            
            // Mensaje de error más amigable según el tipo de error
            String mensajeError = "Lo siento, hubo un error al procesar tu solicitud. ";
            
            if (e.getMessage() != null && e.getMessage().contains("API key")) {
                mensajeError += "La API key de OpenRouter no está configurada correctamente. Verifica la configuración del servidor.";
            } else if (e.getMessage() != null && e.getMessage().contains("conexión")) {
                mensajeError += "No se pudo conectar con el servicio de IA. Verifica tu conexión a internet.";
            } else if (e.getMessage() != null && e.getMessage().contains("Timeout")) {
                mensajeError += "El servicio de IA está tardando demasiado en responder. Por favor, intenta nuevamente.";
            } else {
                mensajeError += "Por favor, intenta nuevamente. Si el problema persiste, contacta al administrador.";
            }
            
            // En caso de error, guardar mensaje de error de la IA
            ChatMensaje mensajeErrorObj = new ChatMensaje();
            mensajeErrorObj.setChatSesion(sesion);
            mensajeErrorObj.setEmisor(ChatMensaje.Emisor.ia);
            mensajeErrorObj.setContenido(mensajeError);
            ChatMensaje mensajeErrorSaved = chatMensajeRepository.save(mensajeErrorObj);
            
            return new ChatMensajeDTO(
                mensajeErrorSaved.getIdMensaje(),
                mensajeErrorSaved.getChatSesion().getIdSesion(),
                mensajeErrorSaved.getEmisor().name(),
                mensajeErrorSaved.getContenido(),
                mensajeErrorSaved.getFechaHoraEnvio()
            );
        }
    }
    
    public ChatMensajeDTO actualizarMensaje(Integer idMensaje, CrearMensajeRequest request) {
        ChatMensaje mensaje = chatMensajeRepository.findById(idMensaje)
            .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        
        mensaje.setContenido(request.getContenido());
        ChatMensaje saved = chatMensajeRepository.save(mensaje);
        
        return new ChatMensajeDTO(
            saved.getIdMensaje(),
            saved.getChatSesion().getIdSesion(),
            saved.getEmisor().name(),
            saved.getContenido(),
            saved.getFechaHoraEnvio()
        );
    }
    
    public void eliminarMensaje(Integer idMensaje) {
        chatMensajeRepository.deleteById(idMensaje);
    }
    
    public List<ChatMensajeDTO> obtenerMensajesPorSesion(Integer idSesion) {
        return chatMensajeRepository.findByChatSesion_IdSesionOrderByFechaHoraEnvioAsc(idSesion).stream()
            .map(m -> new ChatMensajeDTO(
                m.getIdMensaje(),
                m.getChatSesion().getIdSesion(),
                m.getEmisor().name(),
                m.getContenido(),
                m.getFechaHoraEnvio()
            ))
            .collect(Collectors.toList());
    }
    
    // Nuevos métodos para soporte de chats por curso
    
    public List<ChatSesionDTO> obtenerSesionesPorCurso(Integer idCurso) {
        return chatSesionRepository.findByCurso_IdCurso(idCurso).stream()
            .map(s -> new ChatSesionDTO(
                s.getIdSesion(),
                s.getDocente().getIdDocente(),
                s.getAlumno() != null ? s.getAlumno().getIdAlumno() : null,
                s.getCurso() != null ? s.getCurso().getIdCurso() : null,
                s.getTituloSesion(),
                s.getFechaCreacion()
            ))
            .collect(Collectors.toList());
    }
    
    public List<ChatSesionDTO> obtenerSesionesPorAlumno(Integer idAlumno) {
        return chatSesionRepository.findByAlumno_IdAlumno(idAlumno).stream()
            .map(s -> new ChatSesionDTO(
                s.getIdSesion(),
                s.getDocente().getIdDocente(),
                s.getAlumno() != null ? s.getAlumno().getIdAlumno() : null,
                s.getCurso() != null ? s.getCurso().getIdCurso() : null,
                s.getTituloSesion(),
                s.getFechaCreacion()
            ))
            .collect(Collectors.toList());
    }
}

