package com.appmartin.desmartin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servicio para interactuar con OpenRouter API (DeepSeek)
 * 
 * Este servicio maneja las llamadas HTTP a OpenRouter para obtener
 * respuestas de IA conversacional usando el modelo DeepSeek.
 * 
 * @author Desmartin Team
 * @version 1.0
 */
@Service
public class OpenRouterService {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouterService.class);
    private static final int MAX_RETRIES = 3;
    private static final Duration TIMEOUT = Duration.ofSeconds(90);
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    @Value("${openrouter.api.url}")
    private String apiUrl;
    
    @Value("${openrouter.api.key}")
    private String apiKey;
    
    @Value("${openrouter.api.model}")
    private String model;
    
    @Value("${openrouter.api.http-referer:https://desmartin.app}")
    private String httpReferer;
    
    @Value("${openrouter.api.x-title:Desmartin}")
    private String xTitle;
    
    public OpenRouterService() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();
        this.objectMapper = new ObjectMapper();
        
    }
    
    @PostConstruct
    public void init() {
        logger.info("=== Configuración OpenRouter ===");
        logger.info("URL: {}", apiUrl);
        logger.info("Model: {}", model);
        logger.info("API Key configurada: {}", apiKey != null && !apiKey.isEmpty() ? "SÍ" : "NO");
        logger.info("HTTP-Referer: {}", httpReferer);
        logger.info("X-Title: {}", xTitle);
        logger.info("================================");
        logger.info("OpenRouterService inicializado - URL: {}", apiUrl != null ? apiUrl : "NO CONFIGURADO");
    }
    
    /**
     * Envía un mensaje a la IA y obtiene una respuesta
     * 
     * @param mensajes Lista de mensajes en formato de conversación
     *                  Cada mensaje debe tener: role ("user", "assistant", "system") y content
     * @return Respuesta de la IA como String
     * @throws Exception Si hay error al comunicarse con la API
     */
    public String enviarMensaje(List<Map<String, String>> mensajes) throws Exception {
        if (mensajes == null || mensajes.isEmpty()) {
            throw new IllegalArgumentException("La lista de mensajes no puede estar vacía");
        }
        
        logger.info("Enviando {} mensajes a OpenRouter API", mensajes.size());
        logger.debug("URL: {}, Model: {}", apiUrl, model);
        
        // Validar que la API key existe
        if (apiKey == null || apiKey.isEmpty()) {
            logger.error("API key de OpenRouter no está configurada");
            throw new RuntimeException("API key de OpenRouter no está configurada. Verifica application.properties");
        }
        
        // Construir el cuerpo de la petición
        String requestBody = construirRequestBody(mensajes);
        logger.debug("Request body: {}", requestBody);
        
        // Crear la petición HTTP
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .header("HTTP-Referer", httpReferer)
            .header("X-Title", xTitle)
            .timeout(TIMEOUT)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
        
        // Intentar enviar con retry logic
        int intentos = 0;
        Exception ultimoError = null;
        
        while (intentos < MAX_RETRIES) {
            try {
                logger.info("Intento {} de {} - Enviando petición a OpenRouter...", intentos + 1, MAX_RETRIES);
                
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                
                int statusCode = response.statusCode();
                String responseBody = response.body();
                
                logger.info("Respuesta de OpenRouter - Status: {}", statusCode);
                logger.debug("Response body: {}", responseBody);
                
                if (statusCode == 200) {
                    return extraerContenidoRespuesta(responseBody);
                } else {
                    logger.error("Error en OpenRouter API - Status: {}, Body: {}", statusCode, responseBody);
                    
                    // Si es un error 401, no reintentar (API key inválida)
                    if (statusCode == 401) {
                        throw new RuntimeException("API key inválida o expirada. Verifica tu API key en application.properties");
                    }
                    
                    // Si es un error 429, esperar más tiempo
                    if (statusCode == 429) {
                        logger.warn("Rate limit alcanzado, esperando antes de reintentar...");
                        if (intentos < MAX_RETRIES - 1) {
                            Thread.sleep(5000); // Esperar 5 segundos adicionales
                        }
                    }
                    
                    throw new RuntimeException("Error en OpenRouter API: " + statusCode + " - " + responseBody);
                }
                
            } catch (java.net.http.HttpTimeoutException e) {
                ultimoError = e;
                intentos++;
                logger.warn("Intento {} fallido - Timeout al comunicarse con OpenRouter: {}", intentos, e.getMessage());
                
                if (intentos < MAX_RETRIES) {
                    long delay = (long) Math.pow(2, intentos) * 1000;
                    logger.info("Esperando {}ms antes de reintentar...", delay);
                    Thread.sleep(delay);
                }
                
            } catch (java.net.ConnectException e) {
                ultimoError = e;
                intentos++;
                logger.warn("Intento {} fallido - Error de conexión con OpenRouter: {}", intentos, e.getMessage());
                logger.warn("Verifica tu conexión a internet o que la URL de OpenRouter sea correcta");
                
                if (intentos < MAX_RETRIES) {
                    long delay = (long) Math.pow(2, intentos) * 1000;
                    logger.info("Esperando {}ms antes de reintentar...", delay);
                    Thread.sleep(delay);
                }
                
            } catch (Exception e) {
                ultimoError = e;
                intentos++;
                logger.warn("Intento {} fallido al comunicarse con OpenRouter: {} - Tipo: {}", 
                    intentos, e.getMessage(), e.getClass().getSimpleName());
                logger.debug("Stack trace completo:", e);
                
                if (intentos < MAX_RETRIES) {
                    long delay = (long) Math.pow(2, intentos) * 1000;
                    logger.info("Esperando {}ms antes de reintentar...", delay);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupción durante el retry", ie);
                    }
                }
            }
        }
        
        // Si llegamos aquí, todos los intentos fallaron
        logger.error("Todos los intentos fallaron al comunicarse con OpenRouter");
        
        String mensajeError = "No se pudo comunicar con OpenRouter después de " + MAX_RETRIES + " intentos. ";
        if (ultimoError != null) {
            logger.error("Último error: {} - {}", ultimoError.getClass().getSimpleName(), ultimoError.getMessage());
            mensajeError += "Error: " + ultimoError.getClass().getSimpleName() + " - " + ultimoError.getMessage();
            
            // Mensajes de ayuda según el tipo de error
            if (ultimoError instanceof java.net.ConnectException) {
                mensajeError += "\n\nVerifica:\n- Tu conexión a internet\n- Que la URL de OpenRouter sea correcta\n- Que no haya firewall bloqueando la conexión";
            } else if (ultimoError instanceof java.net.http.HttpTimeoutException) {
                mensajeError += "\n\nEl servidor de OpenRouter está tardando demasiado en responder. Intenta nuevamente más tarde.";
            }
        } else {
            mensajeError += "Error desconocido.";
        }
        
        throw new RuntimeException(mensajeError, ultimoError);
    }
    
    /**
     * Envía un mensaje simple (sin historial)
     * 
     * @param mensajeUsuario Mensaje del usuario
     * @return Respuesta de la IA
     * @throws Exception Si hay error al comunicarse con la API
     */
    public String enviarMensajeSimple(String mensajeUsuario) throws Exception {
        List<Map<String, String>> mensajes = new ArrayList<>();
        Map<String, String> mensaje = Map.of(
            "role", "user",
            "content", mensajeUsuario
        );
        mensajes.add(mensaje);
        
        return enviarMensaje(mensajes);
    }
    
    /**
     * Construye el cuerpo JSON de la petición
     */
    private String construirRequestBody(List<Map<String, String>> mensajes) {
        try {
            // Validar que todos los mensajes tengan role y content
            for (Map<String, String> mensaje : mensajes) {
                if (!mensaje.containsKey("role") || !mensaje.containsKey("content")) {
                    throw new IllegalArgumentException("Cada mensaje debe tener 'role' y 'content'");
                }
                if (mensaje.get("role") == null || mensaje.get("content") == null) {
                    throw new IllegalArgumentException("Los valores 'role' y 'content' no pueden ser null");
                }
            }
            
            Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", mensajes
            );
            
            String json = objectMapper.writeValueAsString(requestBody);
            
            // Validar que el JSON sea válido intentando parsearlo
            try {
                objectMapper.readTree(json);
                logger.debug("JSON válido generado: {} caracteres", json.length());
            } catch (Exception e) {
                logger.error("JSON generado no es válido: {}", json);
                throw new RuntimeException("Error al validar el JSON generado", e);
            }
            
            return json;
        } catch (Exception e) {
            logger.error("Error al construir el cuerpo de la petición", e);
            throw new RuntimeException("Error al construir el cuerpo de la petición: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extrae el contenido de la respuesta de OpenRouter
     * 
     * La respuesta de OpenRouter tiene esta estructura:
     * {
     *   "choices": [
     *     {
     *       "message": {
     *         "content": "Respuesta de la IA..."
     *       }
     *     }
     *   ]
     * }
     */
    private String extraerContenidoRespuesta(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode choices = rootNode.get("choices");
            
            if (choices == null || !choices.isArray() || choices.size() == 0) {
                logger.error("Respuesta de OpenRouter sin choices: {}", responseBody);
                throw new RuntimeException("Respuesta inválida de OpenRouter: sin choices");
            }
            
            JsonNode firstChoice = choices.get(0);
            JsonNode message = firstChoice.get("message");
            
            if (message == null) {
                logger.error("Respuesta de OpenRouter sin message: {}", responseBody);
                throw new RuntimeException("Respuesta inválida de OpenRouter: sin message");
            }
            
            JsonNode content = message.get("content");
            
            if (content == null) {
                logger.error("Respuesta de OpenRouter sin content: {}", responseBody);
                throw new RuntimeException("Respuesta inválida de OpenRouter: sin content");
            }
            
            String respuesta = content.asText();
            logger.info("Respuesta de IA obtenida exitosamente ({} caracteres)", respuesta.length());
            
            return respuesta;
            
        } catch (Exception e) {
            logger.error("Error al extraer contenido de la respuesta: {}", responseBody, e);
            throw new RuntimeException("Error al procesar la respuesta de OpenRouter", e);
        }
    }
    
    /**
     * Verifica la conectividad con OpenRouter API
     * 
     * @return true si la API está disponible, false en caso contrario
     */
    public boolean verificarConectividad() {
        try {
            String mensajePrueba = "Hola";
            enviarMensajeSimple(mensajePrueba);
            logger.info("Conexión con OpenRouter verificada exitosamente");
            return true;
        } catch (Exception e) {
            logger.error("Error al verificar conectividad con OpenRouter", e);
            return false;
        }
    }
    public void probarConexion() {
        System.out.println("API Key: " + apiKey); // Solo para comprobar
    } 
}

