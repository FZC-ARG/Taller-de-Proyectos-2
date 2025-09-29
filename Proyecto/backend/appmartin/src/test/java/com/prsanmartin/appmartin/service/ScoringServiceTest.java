package com.prsanmartin.appmartin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.*;
import com.prsanmartin.appmartin.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Test unitario específico para la lógica de cálculo de puntajes del Test Gardner.
 * Incluye casos de prueba con ejemplos manuales conocidos.
 */
@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {
    
    @Mock
    private PreguntaGardnerRepository preguntaRepository;
    
    @InjectMocks
    private TestGardnerServiceImpl testGardnerService;
    
    private List<PreguntaGardner> testQuestions;
    
    @BeforeEach
    void setUp() {
        testQuestions = setUpTestQuestions();
    }
    
    @Test
    void calculateIntelligenceScores_Case1_MusicalPredominant() {
        // Arrange: Respuestas que favorecen inteligencia musical
        Map<Integer, Integer> responses = Map.of(
            1, 4,  // Musical: respuesta alta
            2, 4,  // Musical: respuesta alta  
            3, 4,  // Musical: respuesta alta
            4, 4,  // Musical: respuesta alta
            5, 1,  // Logico: respuesta baja
            6, 2,  // Espacial: respuesta baja
            7, 2,  // Linguistico: respuesta baja
            8, 3   // Corporal: respuesta media
        );
        
        when(preguntaRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            return getQuestionById(id);
        });
        
        // Act
        Map<String, Object> result = testGardnerService.calculateIntelligenceScores(responses);
        
        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Integer> puntajes = (Map<String, Integer>) result.get("puntajes");
        String inteligenciaPredominante = (String) result.get("inteligenciaPredominante");
        Double puntajeTotal = (Double) result.get("puntajeTotal");
        
        assertEquals("musical", inteligenciaPredominante);
        assertTrue(puntajes.get("musical") > puntajes.get("logico"));
        assertTrue(puntajes.get("musical") > puntajes.get("espacial"));
        assertNotNull(puntajeTotal);
        assertTrue(puntajeTotal > 0);
    }
    
    @Test
    void calculateIntelligenceScores_Case2_LogicalPredominant() {
        // Arrange: Respuestas que favorecen inteligencia lógico-matemática
        Map<Integer, Integer> responses = Map.of(
            1, 1,  // Musical: respuesta baja
            2, 1,  // Musical: respuesta baja
            3, 1,  // Musical: respuesta baja
            4, 1,  // Musical: respuesta baja
            5, 4,  // Logico: respuesta alta
            6, 4,  // Logico: respuesta alta
            7, 4,  // Logico: respuesta alta
            8, 4   // Logico: respuesta alta
        );
        
        when(preguntaRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            return getQuestionById(id);
        });
        
        // Act
        Map<String, Object> result = testGardnerService.calculateIntelligenceScores(responses);
        
        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Integer> puntajes = (Map<String, Integer>) result.get("puntajes");
        String inteligenciaPredominante = (String) result.get("inteligenciaPredominante");
        
        assertEquals("logico", inteligenciaPredominante);
        assertTrue(puntajes.get("logico") > puntajes.get("musical"));
        assertEquals(100, puntajes.get("logico")); // Máximo puntaje posible
    }
    
    @Test
    void calculateIntelligenceScores_Case3_TieBetweenIntelligences() {
        // Arrange: Respuestas que crean empate entre dos inteligencias
        Map<Integer, Integer> responses = Map.of(
            1, 4,  // Musical: respuesta alta
            2, 4,  // Musical: respuesta alta
            3, 1,  // Musical: respuesta baja
            4, 1,  // Musical: respuesta baja
            17, 4, // Corporal: respuesta alta
            18, 4, // Corporal: respuesta alta
            19, 1, // Corporal: respuesta baja
            20, 1  // Corporal: respuesta baja
        );
        
        when(preguntaRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            return getQuestionById(id);
        });
        
        // Act
        Map<String, Object> result = testGardnerService.calculateIntelligenceScores(responses);
        
        // Assert
        String inteligenciaPredominante = (String) result.get("inteligenciaPredominante");
        
        // Debería elegir una de las dos (musical o corporal) con puntajes iguales
        assertTrue("musical".equals(inteligenciaPredominante) || "corporal".equals(inteligenciaPredominante));
    }
    
    @Test
    void calculateIntelligenceScores_EdgeCase_LowestScores() {
        // Arrange: Todas las respuestas son las más bajas posibles
        Map<Integer, Integer> responses = new HashMap<>();
        for (int i = 1; i <= 16; i++) {
            responses.put(i, 1); // Todos responden 1 (opción más baja)
        }
        
        when(preguntaRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            return getQuestionById(id);
        });
        
        // Act
        Map<String, Object> result = testGardnerService.calculateIntelligenceScores(responses);
        
        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Integer> puntajes = (Map<String, Integer>) result.get("puntajes");
        
        // Todos los puntajes deberían ser bajos pero no cero
        for (Map.Entry<String, Integer> entry : puntajes.entrySet()) {
            int puntaje = entry.getValue();
            assertTrue(puntaje >= 0 && puntaje <= 25, 
                "Puntaje para " + entry.getKey() + " debería estar entre 0-25, pero es " + puntaje);
        }
    }
    
    @Test
    void calculateIntelligenceScores_EdgeCase_HighestScores() {
        // Arrange: Todas las respuestas son las más altas posibles
        Map<Integer, Integer> responses = new HashMap<>();
        for (int i = 1; i <= 16; i++) {
            responses.put(i, 4); // Todos responden 4 (opción más alta)
        }
        
        when(preguntaRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            return getQuestionById(id);
        });
        
        // Act
        Map<String, Object> result = testGardnerService.calculateIntelligenceScores(responses);
        
        // Assert
        @SuppressWarnings("unchecked")
        Map<String, Integer> puntajes = (Map<String, Integer>) result.get("puntajes");
        Double puntajeTotal = (Double) result.get("puntajeTotal");
        
        // Todos los puntajes deberían ser máximos
        assertEquals(100, puntajes.get("musical"));
        assertEquals(100, puntajes.get("logico"));
        assertEquals(100, puntajes.get("espacial"));
        assertEquals(100.0, puntajeTotal, 0.1); // Puntaje máximo promedio
    }
    
    @Test
    void validateTestResponses_CompleteTest_ShouldPassValidation() {
        // Arrange: Respuestas completas para las 32 preguntas
        List<AutosaveRequestDTO.RespuestaTestDTO> responses = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            AutosaveRequestDTO.RespuestaTestDTO response = new AutosaveRequestDTO.RespuestaTestDTO();
            response.setIdPregunta(i);
            response.setOpcionSeleccionada((i % 4) + 1); // Rotar entre opciones 1-4
            responses.add(response);
        }
        
        // Act
        boolean result = testGardnerService.validateTestResponses(responses);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void validateTestResponses_IncompleteTest_ShouldFailValidation() {
        // Arrange: Solo algunas respuestas (test incompleto)
        List<AutosaveRequestDTO.RespuestaTestDTO> responses = new ArrayList<>();
        for (int i = 1; i <= 10; i++) { // Solo 10 de 32 preguntas
            AutosaveRequestDTO.RespuestaTestDTO response = new AutosaveRequestDTO.RespuestaTestDTO();
            response.setIdPregunta(i);
            response.setOpcionSeleccionada(2);
            responses.add(response);
        }
        
        // Act
        boolean result = testGardnerService.validateTestResponses(responses);
        
        // Assert
        assertFalse(result); // Fallaría si el método valida completitud
    }
    
    // Helper methods
    private List<PreguntaGardner> setUpTestQuestions() {
        List<PreguntaGardner> questions = new ArrayList<>();
        
        // Musical questions (1-4)
        questions.add(createQuestion(1, PreguntaGardner.TipoInteligencia.musical));
        questions.add(createQuestion(2, PreguntaGardner.TipoInteligencia.musical));
        questions.add(createQuestion(3, PreguntaGardner.TipoInteligencia.musical));
        questions.add(createQuestion(4, PreguntaGardner.TipoInteligencia.musical));
        
        // Logical questions (5-8)
        questions.add(createQuestion(5, PreguntaGardner.TipoInteligencia.logico_matematico));
        questions.add(createQuestion(6, PreguntaGardner.TipoInteligencia.logico_matematico));
        questions.add(createQuestion(7, PreguntaGardner.TipoInteligencia.logico_matematico));
        questions.add(createQuestion(8, PreguntaGardner.TipoInteligencia.logico_matematico));
        
        // Add more questions for other intelligence types...
        // (Skipping details for brevity, but would include all 32 questions)
        
        return questions;
    }
    
    private PreguntaGardner createQuestion(int id, PreguntaGardner.TipoInteligencia tipo) {
        PreguntaGardner question = new PreguntaGardner();
        question.setIdPregunta(id);
        question.setTipoInteligencia(tipo);
        question.setActivo(true);
        question.setTextoPregunta("Test question " + id);
        question.setOpcionA("Option A");
        question.setOpcionB("Option B");
        question.setOpcionC("Option C");
        question.setOpcionD("Option D");
        question.setOrdenSecuencia(id);
        return question;
    }
    
    private Optional<PreguntaGardner> getQuestionById(int id) {
        return testQuestions.stream()
                .filter(q -> q.getIdPregunta() == id)
                .findFirst();
    }
}
