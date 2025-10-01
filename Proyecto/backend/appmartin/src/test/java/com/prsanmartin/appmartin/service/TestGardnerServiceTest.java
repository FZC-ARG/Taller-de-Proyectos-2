package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.*;
import com.prsanmartin.appmartin.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestGardnerServiceTest {
    
    @Mock
    private PreguntaGardnerRepository preguntaRepository;
    
    @Mock
    private TestGardnerRepository testRepository;
    
    @Mock
    private AlumnoRepository alumnoRepository;
    
    @Mock
    private AuditoriaService auditoriaService;
    
    @InjectMocks
    private TestGardnerServiceImpl testGardnerService;
    
    private Alumno mockAlumno;
    private Usuario mockUsuario;
    private PreguntaGardner mockPreguntaMusical;
    private PreguntaGardner mockPreguntaLogico;
    private List<PreguntaGardner> mockQuestions;
    
    @BeforeEach
    void setUp() {
        // Setup mock usuario
        mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(1);
        mockUsuario.setNombreUsuario("testuser");
        mockUsuario.setCorreoElectronico("test@example.com");
        
        // Setup mock alumno
        mockAlumno = new Alumno();
        mockAlumno.setIdAlumno(1);
        mockAlumno.setUsuario(mockUsuario);
        mockAlumno.setAnioIngreso(2023);
        
        // Setup mock preguntas
        mockPreguntaMusical = new PreguntaGardner();
        mockPreguntaMusical.setIdPregunta(1);
        mockPreguntaMusical.setTextoPregunta("¿Cómo prefieres trabajar?");
        mockPreguntaMusical.setOpcionA("Con música");
        mockPreguntaMusical.setOpcionB("En silencio");
        mockPreguntaMusical.setOpcionC("Con ruido ambiente");
        mockPreguntaMusical.setOpcionD("Da igual");
        mockPreguntaMusical.setTipoInteligencia(PreguntaGardner.TipoInteligencia.musical);
        mockPreguntaMusical.setOrdenSecuencia(1);
        mockPreguntaMusical.setActivo(true);
        
        mockPreguntaLogico = new PreguntaGardner();
        mockPreguntaLogico.setIdPregunta(2);
        mockPreguntaLogico.setTextoPregunta("¿Cómo resuelves problemas?");
        mockPreguntaLogico.setOpcionA("Analítico");
        mockPreguntaLogico.setOpcionB("Intuitivo");
        mockPreguntaLogico.setOpcionC("Sistemático");
        mockPreguntaLogico.setOpcionD("Experimentando");
        mockPreguntaLogico.setTipoInteligencia(PreguntaGardner.TipoInteligencia.logico_matematico);
        mockPreguntaLogico.setOrdenSecuencia(2);
        mockPreguntaLogico.setActivo(true);
        
        mockQuestions = Arrays.asList(mockPreguntaMusical, mockPreguntaLogico);
    }
    
    @Test
    void getAllActiveQuestions_ShouldReturnAllActiveQuestions() {
        // Given
        when(preguntaRepository.findByActivoTrueOrderByOrdenSecuencia())
                .thenReturn(mockQuestions);
        
        // When
        List<PreguntaGardnerDTO> result = testGardnerService.getAllActiveQuestions();
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockPreguntaMusical.getIdPregunta(), result.get(0).getIdPregunta());
        assertEquals(mockPreguntaLogico.getIdPregunta(), result.get(1).getIdPregunta());
    }
    
    @Test
    void getQuestionsByType_ShouldReturnFilteredQuestions() {
        // Given
        List<PreguntaGardner> musicalQuestions = Arrays.asList(mockPreguntaMusical);
        when(preguntaRepository.findByTipoInteligenciaAndActivoTrueOrderByOrdenSecuencia(
                PreguntaGardner.TipoInteligencia.musical))
                .thenReturn(musicalQuestions);
        
        // When
        List<PreguntaGardnerDTO> result = testGardnerService.getQuestionsByType(
                PreguntaGardner.TipoInteligencia.musical);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PreguntaGardner.TipoInteligencia.musical, result.get(0).getTipoInteligencia());
    }
    
    @Test
    void autosaveTest_WithNewDraft_ShouldCreateNewTest() {
        // Given
        when(alumnoRepository.findById(1)).thenReturn(Optional.of(mockAlumno));
        when(testRepository.findLatestVersionByAlumno(1)).thenReturn(Optional.empty());
        when(testRepository.findByClientRequestId(anyString())).thenReturn(Optional.empty());
        
        TestGardner savedTest = new TestGardner();
        savedTest.setIdTest(1);
        savedTest.setAlumno(mockAlumno);
        savedTest.setVersionGuardado(1);
        savedTest.setEstadoGuardado(TestGardner.EstadoGuardado.BORRADOR);
        savedTest.setClientRequestId("test-uuid");
        
        when(testRepository.save(any(TestGardner.class))).thenReturn(savedTest);
        
        AutosaveRequestDTO request = createValidAutosaveRequest();
        
        // When
        AutosaveResponseDTO result = testGardnerService.autosaveTest(1, request);
        
        // Then
        assertNotNull(result);
        assertEquals("saved", result.getStatus());
        assertEquals(1, result.getIdTest());
        assertEquals(1, result.getVersion());
        assertEquals("BORRADOR", result.getEstado());
        
        verify(testRepository).save(any(TestGardner.class));
        verify(auditoriaService).registrarAccion(eq("AUTOSAVE_TEST"), eq("TestGardner"), 
                any(), any(), any(), any());
    }
    
    @Test
    void autosaveTest_WithDuplicateRequestId_ShouldReturnDuplicate() {
        // Given
        TestGardner existingTest = new TestGardner();
        existingTest.setIdTest(1);
        
        when(testRepository.findByClientRequestId("duplicate-uuid")).thenReturn(Optional.of(existingTest));
        
        AutosaveRequestDTO request = createValidAutosaveRequest();
        request.setClientRequestId("duplicate-uuid");
        
        // When
        AutosaveResponseDTO result = testGardnerService.autosaveTest(1, request);
        
        // Then
        assertNotNull(result);
        assertEquals("duplicate", result.getStatus());
        assertEquals(1, result.getIdTest());
        assertEquals("duplicate-uuid", result.getClientRequestId());
        
        verify(testRepository, never()).save(any());
    }
    
    @Test
    void submitTest_ShouldCalculateScoresAndSaveFinalTest() {
        // Given
        when(alumnoRepository.findById(1)).thenReturn(Optional.of(mockAlumno));
        when(preguntaRepository.findById(1)).thenReturn(Optional.of(mockPreguntaMusical));
        when(preguntaRepository.findById(2)).thenReturn(Optional.of(mockPreguntaLogico));
        
        TestGardner submittedTest = new TestGardner();
        submittedTest.setIdTest(1);
        submittedTest.setAlumno(mockAlumno);
        submittedTest.setVersionGuardado(1);
        submittedTest.setEstadoGuardado(TestGardner.EstadoGuardado.CALCULADO);
        submittedTest.setInteligenciaPredominante("musical");
        submittedTest.setPuntajeTotal(java.math.BigDecimal.valueOf(85.5));
        submittedTest.setPuntajes("{\"musical\": 90, \"logico\": 80}");
        submittedTest.setClientRequestId("test-uuid");
        
        when(testRepository.save(any(TestGardner.class))).thenReturn(submittedTest);
        
        AutosaveRequestDTO request = createValidAutosaveRequest();
        request.setEstado("FINAL");
        
        // When
        TestResultDTO result = testGardnerService.submitTest(1, request);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getIdTest());
        assertEquals(1, result.getIdAlumno());
        assertEquals("musical", result.getInteligenciaPredominante());
        assertEquals(85.5, result.getPuntajeTotal());
        assertEquals("CALCULADO", result.getEstado());
        
        verify(testRepository).save(any(TestGardner.class));
        verify(auditoriaService).registrarAccion(eq("TEST_FINALIZADO"), eq("TestGardner"), 
                any(), any(), any(), any());
    }
    
    @Test
    void calculateIntelligenceScores_ShouldReturnCorrectScores() {
        // Given
        when(preguntaRepository.findById(1)).thenReturn(Optional.of(mockPreguntaMusical));
        when(preguntaRepository.findById(2)).thenReturn(Optional.of(mockPreguntaLogico));
        
        Map<Integer, Integer> responses = new HashMap<>();
        responses.put(1, 4); // Musical question, answer D (highest score)
        responses.put(2, 3); // Logico question, answer C
        
        // When
        Map<String, Object> result = testGardnerService.calculateIntelligenceScores(responses);
        
        // Then
        assertNotNull(result);
        assertTrue(result.containsKey("puntajes"));
        assertTrue(result.containsKey("puntajesBrutos"));
        assertTrue(result.containsKey("inteligenciaPredominante"));
        assertTrue(result.containsKey("puntajeTotal"));
        
        @SuppressWarnings("unchecked")
        Map<String, Integer> puntajes = (Map<String, Integer>) result.get("puntajes");
        assertTrue(puntajes.containsKey("musical"));
        assertTrue(puntajes.containsKey("logico"));
        
        String inteligenciaPredominante = (String) result.get("inteligenciaPredominante");
        assertEquals("musical", inteligenciaPredominante); // Should be musical due to higher score
    }
    
    @Test
    void validateTestResponses_WithValidResponses_ShouldReturnTrue() {
        // Given
        List<AutosaveRequestDTO.RespuestaTestDTO> validResponses = Arrays.asList(
                createValidResponse(1, 1),
                createValidResponse(2, 2)
        );
        
        // When
        boolean result = testGardnerService.validateTestResponses(validResponses);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void validateTestResponses_WithInvalidResponses_ShouldReturnFalse() {
        // Given
        List<AutosaveRequestDTO.RespuestaTestDTO> invalidResponses = Arrays.asList(
                createValidResponse(1, 1),
                createInvalidResponse(2, 5) // Invalid: option 5 doesn't exist
        );
        
        // When
        boolean result = testGardnerService.validateTestResponses(invalidResponses);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    void canStudentTakeTest_WithNoRecentTests_ShouldReturnTrue() {
        // Given
        when(testRepository.findByAlumnoCompletedTests(1)).thenReturn(new ArrayList<>());
        
        // When
        boolean result = testGardnerService.canStudentTakeTest(1);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void canStudentTakeTest_WithRecentTest_ShouldReturnFalse() {
        // Given
        TestGardner recentTest = new TestGardner();
        recentTest.setFechaAplicacion(LocalDateTime.now().minusMonths(3)); // Within 6 months
        
        when(testRepository.findByAlumnoCompletedTests(1)).thenReturn(Arrays.asList(recentTest));
        
        // When
        boolean result = testGardnerService.canStudentTakeTest(1);
        
        // Then
        assertFalse(result);
    }    
    // Helper methods
    private AutosaveRequestDTO createValidAutosaveRequest() {
        AutosaveRequestDTO request = new AutosaveRequestDTO();
        request.setClientRequestId("test-uuid");
        request.setIdAlumno(1);
        request.setEstado("BORRADOR");
        
        List<AutosaveRequestDTO.RespuestaTestDTO> responses = Arrays.asList(
                createValidResponse(1, 1),
                createValidResponse(2, 2)
        );
        request.setRespuestas(responses);
        
        return request;
    }
    
    private AutosaveRequestDTO.RespuestaTestDTO createValidResponse(int preguntaId, int opcion) {
        AutosaveRequestDTO.RespuestaTestDTO response = new AutosaveRequestDTO.RespuestaTestDTO();
        response.setIdPregunta(preguntaId);
        response.setOpcionSeleccionada(opcion);
        return response;
    }
    
    private AutosaveRequestDTO.RespuestaTestDTO createInvalidResponse(int preguntaId, int opcion) {
        AutosaveRequestDTO.RespuestaTestDTO response = new AutosaveRequestDTO.RespuestaTestDTO();
        response.setIdPregunta(preguntaId);
        response.setOpcionSeleccionada(opcion);
        return response;
    }
}
