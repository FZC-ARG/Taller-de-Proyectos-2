package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.Alumno;
import com.prsanmartin.appmartin.entity.PreguntaGardner;
import com.prsanmartin.appmartin.entity.TestGardner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TestGardnerService {
    
    /**
     * Get paginated list of test questions
     */
    Page<PreguntaGardnerDTO> getQuestions(Pageable pageable);
    
    /**
     * Get questions by intelligence type
     */
    List<PreguntaGardnerDTO> getQuestionsByType(PreguntaGardner.TipoInteligencia tipoInteligencia);
    
    /**
     * Get all available questions (usually for complete test)
     */
    List<PreguntaGardnerDTO> getAllActiveQuestions();
    
    /**
     * Autosave test responses (idempotent)
     */
    AutosaveResponseDTO autosaveTest(Integer idAlumno, AutosaveRequestDTO request);
    
    /**
     * Final submit of test responses and calculate scores
     */
    TestResultDTO submitTest(Integer idAlumno, AutosaveRequestDTO request);
    
    /**
     * Get test history for a student
     */
    List<TestResultDTO> getTestHistory(Integer idAlumno);
    
    /**
     * Get latest test for a student
     */
    Optional<TestResultDTO> getLatestTest(Integer idAlumno);
    
    /**
     * Get specific test result by ID (with access control)
     */
    Optional<TestResultDTO> getTestResult(Integer idAlumno, Integer testId);
    
    /**
     * Calculate intelligence scores from responses
     */
    Map<String, Object> calculateIntelligenceScores(Map<Integer, Integer> responses);
    
    /**
     * Get intelligence type statistics
     */
    Map<String, Long> getIntelligenceTypeStatistics();
    
    /**
     * Validate test responses
     */
    boolean validateTestResponses(List<AutosaveRequestDTO.RespuestaTestDTO> respuestas);
    
    /**
     * Check if student can take test (not recently taken)
     */
    boolean canStudentTakeTest(Integer idAlumno);
}
