package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.AIRequestDTO;
import com.prsanmartin.appmartin.dto.AIResponseDTO;
import com.prsanmartin.appmartin.entity.Docente;
import com.prsanmartin.appmartin.entity.SolicitudIA;
import com.prsanmartin.appmartin.entity.RespuestaIA;

import java.util.List;
import java.util.Optional;

public interface AIService {
    
    /**
     * Process AI request and return response
     */
    AIResponseDTO processAIRequest(AIRequestDTO request);
    
    /**
     * Send request to DeepSeek API
     */
    String sendToDeepSeekAPI(String prompt, String context);
    
    /**
     * Save AI request and response to database
     */
    SolicitudIA saveAIRequest(Docente docente, Integer cursoId, Integer alumnoId, String datosEntrada);
    
    /**
     * Save AI response to database
     */
    RespuestaIA saveAIResponse(Integer solicitudId, String respuestaJSON, String modeloIA, Double confianza);
    
    /**
     * Get AI request history for a teacher
     */
    List<SolicitudIA> getAIRequestHistory(Integer docenteId);
    
    /**
     * Get AI request history for a student
     */
    List<SolicitudIA> getAIRequestHistoryByStudent(Integer alumnoId);
    
    /**
     * Get AI response by request ID
     */
    Optional<RespuestaIA> getAIResponse(Integer solicitudId);
    
    /**
     * Process AI request with student profile integration
     */
    AIResponseDTO processAIRequestWithProfile(AIRequestDTO request, Integer alumnoId);
    
    /**
     * Generate academic recommendations based on Gardner test results
     */
    String generateAcademicRecommendations(Integer alumnoId, String inteligenciaPredominante);
    
    /**
     * Check if AI service is available
     */
    boolean isAIServiceAvailable();
}
