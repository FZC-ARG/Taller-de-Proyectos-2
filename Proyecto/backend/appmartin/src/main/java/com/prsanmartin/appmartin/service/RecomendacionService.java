package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.RecomendacionDTO;
import com.prsanmartin.appmartin.dto.RecomendacionHistorialDTO;
import com.prsanmartin.appmartin.entity.*;

import java.util.List;
import java.util.Map;

public interface RecomendacionService {
    
    /**
     * Generate recommendations based on Gardner test results
     */
    List<RecomendacionDTO> generateRecommendations(Integer idAlumno, Integer idTest);
    
    /**
     * Get recommendation history for a student
     */
    List<RecomendacionHistorialDTO> getRecommendationHistory(Integer idAlumno);
    
    /**
     * Get recommendation history for teachers
     */
    List<RecomendacionHistorialDTO> getRecommendationHistoryForTeachers(Integer docenteId);
    
    /**
     * Update recommendation status
     */
    boolean updateRecommendationStatus(Integer idHistorial, HistorialRecomendacion.EstadoRecomendacion estado, String notas);
    
    /**
     * Get recommendation templates
     */
    List<PlantillaRecomendacion> getRecommendationTemplates();
    
    /**
     * Create new recommendation template
     */
    PlantillaRecomendacion createRecommendationTemplate(PlantillaRecomendacion plantilla);
    
    /**
     * Update recommendation template
     */
    PlantillaRecomendacion updateRecommendationTemplate(Integer idPlantilla, PlantillaRecomendacion plantilla);
    
    /**
     * Delete recommendation template
     */
    boolean deleteRecommendationTemplate(Integer idPlantilla);
    
    /**
     * Get recommendation statistics
     */
    Map<String, Object> getRecommendationStatistics();
    
    /**
     * Regenerate recommendations for a student
     */
    List<RecomendacionDTO> regenerateRecommendations(Integer idAlumno);
    
    /**
     * Get recommendations by type
     */
    List<RecomendacionHistorialDTO> getRecommendationsByType(HistorialRecomendacion.TipoRecomendacion tipoRecomendacion);
}
