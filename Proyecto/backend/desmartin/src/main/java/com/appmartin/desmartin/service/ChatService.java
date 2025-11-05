package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    // ==================== SISTEMA DE COLA DE PROCESAMIENTO ====================
    // Configuración del sistema de cola para procesar mensajes de chat
    private static final int MAX_THREADS_SIMULTANEOS = 3; // Máximo 3 solicitudes simultáneas a la IA
    private static final int TAMANO_COLA_MAXIMO = 100; // Tamaño máximo de la cola (si está llena, espera)
    
    // ThreadPoolExecutor para procesar las solicitudes de chat
    // - Core pool size: 3 (siempre mantiene 3 threads activos)
    // - Maximum pool size: 3 (nunca excede 3 threads)
    // - Queue: LinkedBlockingQueue con capacidad máxima (si está llena, espera)
    // - RejectedExecutionHandler: CallerRunsPolicy (si está llena, ejecuta en el thread actual)
    private ExecutorService executorService;
    
    /**
     * Inicializa el sistema de cola de procesamiento al iniciar el servicio
     */
    @PostConstruct
    public void inicializarCola() {
        logger.info("=== Inicializando Sistema de Cola de Chat ===");
        logger.info("Máximo de threads simultáneos: {}", MAX_THREADS_SIMULTANEOS);
        logger.info("Tamaño máximo de cola: {}", TAMANO_COLA_MAXIMO);
        
        // Crear ThreadPoolExecutor con configuración de cola
        BlockingQueue<Runnable> cola = new LinkedBlockingQueue<>(TAMANO_COLA_MAXIMO);
        
        this.executorService = new ThreadPoolExecutor(
            MAX_THREADS_SIMULTANEOS,  // Core pool size (threads mínimos que siempre están activos)
            MAX_THREADS_SIMULTANEOS,  // Maximum pool size (máximo de threads que pueden existir)
            60L,                      // Keep alive time (tiempo que un thread extra espera antes de terminar)
            TimeUnit.SECONDS,         // Unidad de tiempo para keep alive
            cola,                     // Cola de trabajo (si está llena, las tareas esperan)
            new ThreadFactory() {     // Factory personalizado para crear threads con nombres descriptivos
                private int contador = 1;
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "ChatProcessor-" + contador++);
                    thread.setDaemon(false); // No son daemon threads
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // Si la cola está llena, ejecuta en el thread actual (espera)
        );
        
        logger.info("Sistema de cola inicializado correctamente");
        logger.info("=============================================");
    }
    
    /**
     * Cierra el sistema de cola al destruir el servicio
     */
    @PreDestroy
    public void cerrarCola() {
        logger.info("=== Cerrando Sistema de Cola de Chat ===");
        if (executorService != null) {
            executorService.shutdown();
            try {
                // Esperar hasta que todas las tareas terminen (máximo 30 segundos)
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    logger.warn("El executor no terminó en 30 segundos, forzando cierre...");
                    executorService.shutdownNow();
                    // Esperar otros 10 segundos
                    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                        logger.error("El executor no terminó después de forzar el cierre");
                    }
                }
                logger.info("Sistema de cola cerrado correctamente");
            } catch (InterruptedException e) {
                logger.error("Error al cerrar el sistema de cola", e);
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        logger.info("==========================================");
    }
    
    /**
     * Obtiene estadísticas del sistema de cola
     * 
     * @return Map con estadísticas de la cola
     */
    public Map<String, Object> obtenerEstadisticasCola() {
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) executorService;
            return Map.of(
                "threadsActivos", tpe.getActiveCount(),
                "threadsCompletadas", tpe.getCompletedTaskCount(),
                "tareasEnCola", tpe.getQueue().size(),
                "tareasTotales", tpe.getTaskCount(),
                "threadsPoolSize", tpe.getPoolSize()
            );
        }
        return Map.of();
    }
    
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
     * 1. Guarda el mensaje del docente inmediatamente (no se encola)
     * 2. Encola el procesamiento de la respuesta de la IA en el sistema de cola
     * 3. El procesamiento incluye:
     *    - Obtener el historial completo de mensajes previos
     *    - Generar el contexto dinámico según el tipo de sesión
     *    - Construir los mensajes para la IA
     *    - Enviar la solicitud a OpenRouter API (máximo 3 simultáneas)
     *    - Guardar la respuesta de la IA
     * 
     * El historial de mensajes se carga automáticamente, permitiendo que la IA
     * tenga contexto completo de la conversación previa.
     * 
     * @param idSesion ID de la sesión de chat
     * @param request Request con el contenido del mensaje
     * @return DTO del mensaje de la IA generado
     */
    public ChatMensajeDTO crearMensaje(Integer idSesion, CrearMensajeRequest request) {
        logger.info("⏰ [COLA] Entrada: Creando mensaje para sesión ID: {}", idSesion);
        
        ChatSesion sesion = chatSesionRepository.findById(idSesion)
            .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        
        // Guardar mensaje del docente inmediatamente (no se encola)
        ChatMensaje mensajeDocente = new ChatMensaje();
        mensajeDocente.setChatSesion(sesion);
        mensajeDocente.setEmisor(ChatMensaje.Emisor.docente);
        mensajeDocente.setContenido(request.getContenido());
        chatMensajeRepository.save(mensajeDocente);
        logger.debug("Mensaje del docente guardado: ID {}", mensajeDocente.getIdMensaje());
        
        // Crear tarea para procesar la respuesta de la IA (se encolará)
        ProcesarMensajeIATask tarea = new ProcesarMensajeIATask(
            idSesion,
            mensajeDocente.getIdMensaje(),
            request.getContenido(),
            sesion
        );
        
        // Encolar la tarea en el sistema de cola
        logger.info("📥 [COLA] Encolando tarea para sesión ID: {} - Threads activos: {}, Tareas en cola: {}", 
            idSesion,
            ((ThreadPoolExecutor) executorService).getActiveCount(),
            ((ThreadPoolExecutor) executorService).getQueue().size());
        
        // Enviar la tarea a la cola (si está llena, esperará hasta que haya espacio)
        Future<ChatMensajeDTO> futuro = executorService.submit(tarea);
        
        try {
            // Esperar a que la tarea se complete (si la cola está llena, esperará aquí)
            logger.info("⏳ [COLA] Esperando procesamiento de tarea para sesión ID: {}", idSesion);
            ChatMensajeDTO resultado = futuro.get(); // Espera hasta que se complete
            logger.info("✅ [COLA] Salida: Tarea completada para sesión ID: {} - Mensaje IA ID: {}", 
                idSesion, resultado.getIdMensaje());
            return resultado;
            
        } catch (InterruptedException e) {
            logger.error("❌ [COLA] Error: Interrupción al procesar mensaje para sesión ID: {}", idSesion, e);
            Thread.currentThread().interrupt();
            return crearMensajeError(sesion, "El procesamiento fue interrumpido. Por favor, intenta nuevamente.");
            
        } catch (ExecutionException e) {
            logger.error("❌ [COLA] Error: Excepción al procesar mensaje para sesión ID: {}", idSesion, e);
            Throwable causa = e.getCause();
            if (causa != null) {
                logger.error("Causa del error: {}", causa.getMessage());
            }
            return crearMensajeError(sesion, obtenerMensajeError(causa));
        }
    }
    
    /**
     * Clase interna que representa una tarea para procesar un mensaje de IA
     * Esta tarea se ejecuta en el sistema de cola
     */
    private class ProcesarMensajeIATask implements Callable<ChatMensajeDTO> {
        private final Integer idSesion;
        private final Integer idMensajeDocente;
        private final String contenidoMensaje;
        private final ChatSesion sesion;
        
        public ProcesarMensajeIATask(Integer idSesion, Integer idMensajeDocente, 
                                     String contenidoMensaje, ChatSesion sesion) {
            this.idSesion = idSesion;
            this.idMensajeDocente = idMensajeDocente;
            this.contenidoMensaje = contenidoMensaje;
            this.sesion = sesion;
        }
        
        @Override
        public ChatMensajeDTO call() throws Exception {
            String threadName = Thread.currentThread().getName();
            logger.info("🚀 [COLA] Iniciando procesamiento - Thread: {} - Sesión ID: {}", threadName, idSesion);
            
            try {
                // Obtener historial completo de mensajes previos de la sesión
                // Esto incluye todos los mensajes anteriores (docente e IA) para mantener el contexto
                List<ChatMensajeDTO> historialMensajes = obtenerMensajesPorSesion(idSesion).stream()
                    .filter(m -> m.getIdMensaje() != idMensajeDocente) // Excluir el mensaje recién guardado
                    .collect(Collectors.toList());
                
                logger.info("📋 [COLA] Historial cargado: {} mensajes previos - Thread: {} - Sesión ID: {}", 
                    historialMensajes.size(), threadName, idSesion);
                
                // Generar contexto según el tipo de sesión
                String contexto;
                if (sesion.getAlumno() != null) {
                    // Chat individual (alumno)
                    logger.info("👤 [COLA] Generando contexto para alumno ID: {} - Thread: {} - Sesión ID: {}", 
                        sesion.getAlumno().getIdAlumno(), threadName, idSesion);
                    contexto = contextoIAService.generarContextoAlumno(sesion.getAlumno().getIdAlumno());
                } else if (sesion.getCurso() != null) {
                    // Chat grupal (curso)
                    logger.info("👥 [COLA] Generando contexto para curso ID: {} - Thread: {} - Sesión ID: {}", 
                        sesion.getCurso().getIdCurso(), threadName, idSesion);
                    contexto = contextoIAService.generarContextoCurso(sesion.getCurso().getIdCurso());
                } else {
                    // Fallback: contexto genérico (no debería pasar por validación anterior)
                    logger.warn("⚠️ [COLA] Sesión sin alumno ni curso, usando contexto genérico - Thread: {} - Sesión ID: {}", 
                        threadName, idSesion);
                    contexto = "Eres un asistente educativo. Ayuda al docente con sus consultas pedagógicas.";
                }
                
                // Construir mensajes para la IA (contexto + historial + nuevo mensaje)
                List<Map<String, String>> mensajesParaIA = contextoIAService.construirMensajesParaIA(
                    contexto,
                    contenidoMensaje,
                    historialMensajes
                );
                
                logger.info("📤 [COLA] Enviando {} mensajes a OpenRouter API - Thread: {} - Sesión ID: {}", 
                    mensajesParaIA.size(), threadName, idSesion);
                
                // Llamar a OpenRouter para obtener respuesta de la IA
                // Esta llamada se ejecuta dentro del thread pool (máximo 3 simultáneas)
                String respuestaIA = openRouterService.enviarMensaje(mensajesParaIA);
                
                logger.info("📥 [COLA] Respuesta de IA obtenida: {} caracteres - Thread: {} - Sesión ID: {}", 
                    respuestaIA.length(), threadName, idSesion);
                
                // Guardar respuesta de la IA
                ChatMensaje mensajeIA = new ChatMensaje();
                mensajeIA.setChatSesion(sesion);
                mensajeIA.setEmisor(ChatMensaje.Emisor.ia);
                mensajeIA.setContenido(respuestaIA);
                ChatMensaje mensajeIASaved = chatMensajeRepository.save(mensajeIA);
                
                logger.info("💾 [COLA] Mensaje de IA guardado: ID {} - Thread: {} - Sesión ID: {}", 
                    mensajeIASaved.getIdMensaje(), threadName, idSesion);
                
                logger.info("✅ [COLA] Procesamiento completado - Thread: {} - Sesión ID: {}", threadName, idSesion);
                
                return new ChatMensajeDTO(
                    mensajeIASaved.getIdMensaje(),
                    mensajeIASaved.getChatSesion().getIdSesion(),
                    mensajeIASaved.getEmisor().name(),
                    mensajeIASaved.getContenido(),
                    mensajeIASaved.getFechaHoraEnvio()
                );
                
            } catch (Exception e) {
                logger.error("❌ [COLA] Error en procesamiento - Thread: {} - Sesión ID: {}", threadName, idSesion, e);
                throw e; // Re-lanzar la excepción para que se maneje en crearMensaje
            }
        }
    }
    
    /**
     * Crea un mensaje de error cuando falla el procesamiento
     */
    private ChatMensajeDTO crearMensajeError(ChatSesion sesion, String mensajeError) {
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
    
    /**
     * Obtiene un mensaje de error amigable según el tipo de error
     */
    private String obtenerMensajeError(Throwable error) {
        String mensajeError = "Lo siento, hubo un error al procesar tu solicitud. ";
        
        if (error == null) {
            mensajeError += "Por favor, intenta nuevamente. Si el problema persiste, contacta al administrador.";
            return mensajeError;
        }
        
        String mensajeOriginal = error.getMessage();
        if (mensajeOriginal != null && mensajeOriginal.contains("API key")) {
            mensajeError += "La API key de OpenRouter no está configurada correctamente. Verifica la configuración del servidor.";
        } else if (mensajeOriginal != null && mensajeOriginal.contains("conexión")) {
            mensajeError += "No se pudo conectar con el servicio de IA. Verifica tu conexión a internet.";
        } else if (mensajeOriginal != null && mensajeOriginal.contains("Timeout")) {
            mensajeError += "El servicio de IA está tardando demasiado en responder. Por favor, intenta nuevamente.";
        } else {
            mensajeError += "Por favor, intenta nuevamente. Si el problema persiste, contacta al administrador.";
        }
        
        return mensajeError;
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

