package com.prsanmartin.appmartin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.AIRequestDTO;
import com.prsanmartin.appmartin.dto.AIResponseDTO;
import com.prsanmartin.appmartin.entity.*;
import com.prsanmartin.appmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Transactional
public class AIServiceImpl implements AIService {
    
    @Autowired
    private SolicitudIARepository solicitudRepository;
    
    @Autowired
    private RespuestaIARepository respuestaRepository;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private TestGardnerRepository testGardnerRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    @Value("${ai.deepseek.api.url:https://api.deepseek.com/v1/chat/completions}")
    private String deepSeekApiUrl;
    
    @Value("${ai.deepseek.api.key:}")
    private String deepSeekApiKey;
    
    @Value("${ai.deepseek.timeout:50000}")
    private int timeoutMs;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Simple queue for handling overload
    private final ConcurrentLinkedQueue<AIRequestDTO> requestQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean processingQueue = false;
    
    @Override
    public AIResponseDTO processAIRequest(AIRequestDTO request) {
        try {
            // Validate entities exist
            Docente docente = docenteRepository.findById(request.getIdDocente())
                    .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
            
            // Validate curso exists
            cursoRepository.findById(request.getIdCurso())
                    .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
            
            // Validate alumno exists if provided
            if (request.getIdAlumno() != null) {
                alumnoRepository.findById(request.getIdAlumno())
                        .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
            }
            
            // Save request to database
            SolicitudIA solicitud = saveAIRequest(docente, request.getIdCurso(), request.getIdAlumno(), 
                    buildRequestData(request));
            
            // Process AI request
            String aiResponse = sendToDeepSeekAPI(request.getPregunta(), request.getContexto());
            
            // Save response to database
            RespuestaIA respuesta = saveAIResponse(solicitud.getIdSolicitud(), aiResponse, "DeepSeek", 0.85);
            
            // Audit trail
            auditoriaService.registrarAccion(
                "AI_REQUEST_PROCESSED",
                "SolicitudIA",
                solicitud.getIdSolicitud(),
                "Consulta IA procesada exitosamente",
                null,
                docente.getUsuario().getNombreUsuario()
            );
            
            return AIResponseDTO.success(solicitud.getIdSolicitud(), respuesta.getIdRespuesta(), 
                    aiResponse, "DeepSeek", 0.85);
            
        } catch (Exception e) {
            return AIResponseDTO.error("Error al procesar solicitud IA: " + e.getMessage());
        }
    }
    
    @Override
    public String sendToDeepSeekAPI(String prompt, String context) {
        if (!isAIServiceAvailable()) {
            throw new RuntimeException("Servicio de IA no disponible");
        }
        
        try {
            // Build request payload
            Map<String, Object> requestPayload = new HashMap<>();
            requestPayload.put("model", "deepseek-chat");
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Eres un asistente académico especializado en educación. " +
                    "Proporciona respuestas útiles, precisas y educativas. " +
                    "Si se proporciona contexto adicional, úsalo para personalizar tu respuesta.");
            messages.add(systemMessage);
            
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            String fullPrompt = context != null ? 
                    "Contexto: " + context + "\n\nPregunta: " + prompt : 
                    prompt;
            userMessage.put("content", fullPrompt);
            messages.add(userMessage);
            
            requestPayload.put("messages", messages);
            requestPayload.put("max_tokens", 1000);
            requestPayload.put("temperature", 0.7);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(deepSeekApiKey);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);
            
            // Send request with timeout
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    deepSeekApiUrl,
                    HttpMethod.POST,
                    entity,
                    (Class<Map<String, Object>>) (Class<?>) Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    return (String) message.get("content");
                }
            }
            
            throw new RuntimeException("Respuesta inválida del servicio de IA");
            
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error de autenticación o límite de API: " + e.getMessage());
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Timeout o error de conexión: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con DeepSeek API: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public SolicitudIA saveAIRequest(Docente docente, Integer cursoId, Integer alumnoId, String datosEntrada) {
        SolicitudIA solicitud = new SolicitudIA();
        solicitud.setDocente(docente);
        solicitud.setCurso(cursoRepository.findById(cursoId).orElse(null));
        if (alumnoId != null) {
            solicitud.setAlumno(alumnoRepository.findById(alumnoId).orElse(null));
        }
        solicitud.setDatosEntrada(datosEntrada);
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDateTime.now());
        
        return solicitudRepository.save(solicitud);
    }
    
    @Override
    @Transactional
    public RespuestaIA saveAIResponse(Integer solicitudId, String respuestaJSON, String modeloIA, Double confianza) {
        SolicitudIA solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        RespuestaIA respuesta = new RespuestaIA();
        respuesta.setSolicitud(solicitud);
        respuesta.setRespuestaJSON(respuestaJSON);
        respuesta.setModeloIA(modeloIA);
        respuesta.setConfianza(confianza != null ? java.math.BigDecimal.valueOf(confianza) : null);
        respuesta.setFechaRespuesta(LocalDateTime.now());
        
        // Update solicitud status
        solicitud.setEstado("COMPLETADA");
        solicitudRepository.save(solicitud);
        
        return respuestaRepository.save(respuesta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudIA> getAIRequestHistory(Integer docenteId) {
        return solicitudRepository.findByDocenteIdDocente(docenteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudIA> getAIRequestHistoryByStudent(Integer alumnoId) {
        return solicitudRepository.findByAlumnoIdAlumno(alumnoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RespuestaIA> getAIResponse(Integer solicitudId) {
        return respuestaRepository.findBySolicitudIdSolicitud(solicitudId).stream().findFirst();
    }
    
    @Override
    public AIResponseDTO processAIRequestWithProfile(AIRequestDTO request, Integer alumnoId) {
        try {
            // Get student's Gardner test results
            Optional<TestGardner> latestTest = testGardnerRepository.findFirstByAlumnoIdAlumnoOrderByFechaAplicacionDesc(alumnoId);
            
            if (latestTest.isPresent()) {
                TestGardner test = latestTest.get();
                String testContext = "Perfil del estudiante:\n" +
                        "- Inteligencia predominante: " + test.getInteligenciaPredominante() + "\n" +
                        "- Puntaje total: " + test.getPuntajeTotal() + "\n" +
                        "- Fecha del test: " + test.getFechaAplicacion();
                
                request.setContexto(testContext);
            }
            
            return processAIRequest(request);
            
        } catch (Exception e) {
            return AIResponseDTO.error("Error al procesar solicitud con perfil: " + e.getMessage());
        }
    }
    
    @Override
    public String generateAcademicRecommendations(Integer alumnoId, String inteligenciaPredominante) {
        try {
            String prompt = "Genera recomendaciones académicas específicas para un estudiante con " +
                    "inteligencia predominante " + inteligenciaPredominante + ". " +
                    "Incluye sugerencias de materias, actividades extracurriculares, " +
                    "y estrategias de aprendizaje personalizadas.";
            
            return sendToDeepSeekAPI(prompt, "Recomendaciones académicas basadas en test Gardner");
            
        } catch (Exception e) {
            return "No se pudieron generar recomendaciones personalizadas. Error: " + e.getMessage();
        }
    }
    
    @Override
    public boolean isAIServiceAvailable() {
        return deepSeekApiKey != null && !deepSeekApiKey.trim().isEmpty();
    }
    
    // Helper methods
    private String buildRequestData(AIRequestDTO request) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("pregunta", request.getPregunta());
            data.put("contexto", request.getContexto());
            data.put("tipoConsulta", request.getTipoConsulta());
            data.put("datosAdicionales", request.getDatosAdicionales());
            data.put("timestamp", request.getTimestamp());
            
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return "{\"pregunta\":\"" + request.getPregunta() + "\",\"error\":\"Error serializando datos\"}";
        }
    }
    
    // Queue processing for overload handling
    public void processQueueAsync() {
        if (processingQueue) return;
        
        processingQueue = true;
        CompletableFuture.runAsync(() -> {
            try {
                while (!requestQueue.isEmpty()) {
                    AIRequestDTO request = requestQueue.poll();
                    if (request != null) {
                        processAIRequest(request);
                        Thread.sleep(1000); // Rate limiting
                    }
                }
            } catch (Exception e) {
                // Log error
            } finally {
                processingQueue = false;
            }
        });
    }
    
    public void addToQueue(AIRequestDTO request) {
        requestQueue.offer(request);
        if (!processingQueue) {
            processQueueAsync();
        }
    }
}
