package com.prsanmartin.appmartin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.*;
import com.prsanmartin.appmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestGardnerServiceImpl implements TestGardnerService {
    
    @Autowired
    private PreguntaGardnerRepository preguntaRepository;
    
    @Autowired
    private TestGardnerRepository testRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Intelligence type mappings for scoring
    private static final Map<PreguntaGardner.TipoInteligencia, String> INTELIGENCE_MAPPING = Map.of(
        PreguntaGardner.TipoInteligencia.musical, "musical",
        PreguntaGardner.TipoInteligencia.logico_matematico, "logico",
        PreguntaGardner.TipoInteligencia.espacial, "espacial",
        PreguntaGardner.TipoInteligencia.linguistico, "linguistico",
        PreguntaGardner.TipoInteligencia.corporal_cinestesico, "corporal",
        PreguntaGardner.TipoInteligencia.interpersonal, "interpersonal",
        PreguntaGardner.TipoInteligencia.intrapersonal, "intrapersonal",
        PreguntaGardner.TipoInteligencia.naturalista, "naturalista"
    );
    
    @Override
    @Transactional(readOnly = true)
    public Page<PreguntaGardnerDTO> getQuestions(Pageable pageable) {
        List<PreguntaGardner> activeQuestions = preguntaRepository.findByActivoTrueOrderByOrdenSecuencia();
        
        // Simple pagination implementation
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), activeQuestions.size());
        
        List<PreguntaGardner> pageContent = activeQuestions.subList(start, end);
        List<PreguntaGardnerDTO> dtoContent = pageContent.stream()
                .map(PreguntaGardnerDTO::new)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoContent, pageable, activeQuestions.size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PreguntaGardnerDTO> getQuestionsByType(PreguntaGardner.TipoInteligencia tipoInteligencia) {
        return preguntaRepository.findByTipoInteligenciaAndActivoTrueOrderByOrdenSecuencia(tipoInteligencia)
                .stream()
                .map(PreguntaGardnerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PreguntaGardnerDTO> getAllActiveQuestions() {
        return preguntaRepository.findByActivoTrueOrderByOrdenSecuencia()
                .stream()
                .map(PreguntaGardnerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    public AutosaveResponseDTO autosaveTest(Integer idAlumno, AutosaveRequestDTO request) {
        try {
            // Validate student exists
            Alumno alumno = alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
            
            // Check for duplicate request (idempotent operation)
            if (request.getClientRequestId() != null) {
                Optional<TestGardner> existingTest = testRepository.findByClientRequestId(request.getClientRequestId());
                if (existingTest.isPresent()) {
                    return AutosaveResponseDTO.duplicate(request.getClientRequestId(), existingTest.get().getIdTest());
                }
            }
            
            // Validate responses
            if (!validateTestResponses(request.getRespuestas())) {
                return AutosaveResponseDTO.error("Respuestas inválidas", 5);
            }
            
            // Generate client request ID if not provided
            String clientId = request.getClientRequestId() != null ? 
                    request.getClientRequestId() : UUID.randomUUID().toString();
            
            // Check if student has an existing draft
            Optional<TestGardner> existingDraft = testRepository.findLatestVersionByAlumno(idAlumno);
            
            TestGardner testGardner;
            boolean isUpdate = false;
            
            if (existingDraft.isPresent() && existingDraft.get().getEstadoGuardado() == TestGardner.EstadoGuardado.BORRADOR) {
                testGardner = existingDraft.get();
                isUpdate = true;
            } else {
                testGardner = new TestGardner();
                testGardner.setAlumno(alumno);
                testGardner.setTiempoInicio(LocalDateTime.now());
            }
            
            // Set test data
            testGardner.setClientRequestId(clientId);
            testGardner.setVersionGuardado(testGardner.getVersionGuardado() + (isUpdate ? 1 : 0));
            testGardner.setModificadoPor(alumno.getUsuario().getNombreUsuario());
            
            // Convert responses to JSON
            String respuestasJson = convertResponsesToJson(request.getRespuestas());
            testGardner.setRespuestas(respuestasJson);
            
            // Determine state
            String estado = request.getEstado() != null ? request.getEstado() : "BORRADOR";
            if ("FINAL".equals(estado)) {
                testGardner.setEstadoGuardado(TestGardner.EstadoGuardado.FINAL);
                testGardner.setTiempoFin(LocalDateTime.now());
            } else {
                testGardner.setEstadoGuardado(TestGardner.EstadoGuardado.BORRADOR);
            }
            
            // Save test
            TestGardner savedTest = testRepository.save(testGardner);
            
            // Audit trail
            auditoriaService.registrarAccion(
                "AUTOSAVE_TEST",
                "TestGardner",
                savedTest.getIdTest(),
                "Test guardado: version " + savedTest.getVersionGuardado(),
                null,
                alumno.getUsuario().getNombreUsuario()
            );
            
            return AutosaveResponseDTO.success(
                savedTest.getIdTest(),
                savedTest.getVersionGuardado(),
                savedTest.getEstadoGuardado().name(),
                clientId
            );
            
        } catch (Exception e) {
            return AutosaveResponseDTO.error("Error al guardar test: " + e.getMessage(), 10);
        }
    }
    
    @Override
    @Transactional
    public TestResultDTO submitTest(Integer idAlumno, AutosaveRequestDTO request) {
        try {
            // Validate student exists
            Alumno alumno = alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
            
            // Validate complete responses
            if (!validateTestResponses(request.getRespuestas())) {
                throw new IllegalArgumentException("Respuestas incompletas o inválidas");
            }
            
            // Check for existing test with same client request ID
            TestGardner testGardner = null;
            if (request.getClientRequestId() != null) {
                Optional<TestGardner> existingTest = testRepository.findByClientRequestId(request.getClientRequestId());
                if (existingTest.isPresent()) {
                    testGardner = existingTest.get();
                    // Update existing test to final
                    testGardner.setEstadoGuardado(TestGardner.EstadoGuardado.FINAL);
                    testGardner.setTiempoFin(LocalDateTime.now());
                }
            }
            
            // If no existing test, get latest draft or create new
            if (testGardner == null) {
                Optional<TestGardner> draftTest = testRepository.findLatestVersionByAlumno(idAlumno);
                if (draftTest.isPresent() && draftTest.get().getEstadoGuardado() == TestGardner.EstadoGuardado.BORRADOR) {
                    testGardner = draftTest.get();
                } else {
                    testGardner = new TestGardner();
                    testGardner.setAlumno(alumno);
                    testGardner.setTiempoInicio(LocalDateTime.now());
                }
            }
            
            // Set final data
            String respuestasJson = convertResponsesToJson(request.getRespuestas());
            testGardner.setRespuestas(respuestasJson);
            testGardner.setTiempoFin(LocalDateTime.now());
            testGardner.setEstadoGuardado(TestGardner.EstadoGuardado.FINAL);
            testGardner.setClientRequestId(request.getClientRequestId());
            
            // Calculate intelligence scores
            Map<String, Object> scoresResult = calculateIntelligenceScores(
                convertResponsesToMap(request.getRespuestas())
            );
            
            // Set scores and results
            testGardner.setPuntajes(objectMapper.writeValueAsString(scoresResult.get("puntajes")));
            testGardner.setInteligenciaPredominante((String) scoresResult.get("inteligenciaPredominante"));
            Double total = (Double) scoresResult.get("puntajeTotal");
            testGardner.setPuntajeTotal(total != null ? BigDecimal.valueOf(total).setScale(2, java.math.RoundingMode.HALF_UP) : null);
            testGardner.setEstadoGuardado(TestGardner.EstadoGuardado.CALCULADO);
            
            // Save final test
            TestGardner savedTest = testRepository.save(testGardner);
            
            // Audit trail
            auditoriaService.registrarAccion(
                "TEST_FINALIZADO",
                "TestGardner",
                savedTest.getIdTest(),
                "Test completado y calculado: " + testGardner.getInteligenciaPredominante(),
                null,
                alumno.getUsuario().getNombreUsuario()
            );
            
            // Create result DTO
            TestResultDTO result = TestResultDTO.fromTestGardner(savedTest);
            @SuppressWarnings("unchecked")
            Map<String, Integer> puntajesBrutos = (Map<String, Integer>) scoresResult.get("puntajesBrutos");
            @SuppressWarnings("unchecked")
            Map<String, Integer> puntajesPonderados = (Map<String, Integer>) scoresResult.get("puntajes");
            result.setPuntajesBrutos(puntajesBrutos);
            result.setPuntajesPonderados(puntajesPonderados);
            result.setDescripcionInteligencia(getIntelligenceDescription(testGardner.getInteligenciaPredominante()));
            result.setRecomendacionesAcademicas(getAcademicRecommendations(testGardner.getInteligenciaPredominante()));
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar test final: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TestResultDTO> getTestHistory(Integer idAlumno) {
        List<TestGardner> tests = testRepository.findByAlumnoCompletedTests(idAlumno);
        return tests.stream()
                .map(test -> {
                    try {
                        TestResultDTO result = TestResultDTO.fromTestGardner(test);
                        if (test.getPuntajes() != null) {
                            Map<String, Integer> puntajes = objectMapper.readValue(
                                test.getPuntajes(),
                                new TypeReference<Map<String, Integer>>() {}
                            );
                            result.setPuntajesPonderados(puntajes);
                        }
                        return result;
                    } catch (Exception e) {
                        return TestResultDTO.fromTestGardner(test);
                    }
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TestResultDTO> getLatestTest(Integer idAlumno) {
        return testRepository.findFirstByAlumnoIdAlumnoOrderByFechaAplicacionDesc(idAlumno)
                .map(test -> {
                    TestResultDTO result = TestResultDTO.fromTestGardner(test);
                    try {
                        if (test.getPuntajes() != null) {
                            Map<String, Integer> puntajes = objectMapper.readValue(
                                test.getPuntajes(),
                                new TypeReference<Map<String, Integer>>() {}
                            );
                            result.setPuntajesPonderados(puntajes);
                        }
                    } catch (Exception e) {
                        // Log error but continue
                    }
                    return result;
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TestResultDTO> getTestResult(Integer idAlumno, Integer testId) {
        // Security check: ensure student can only access their own tests
        return testRepository.findByAlumnoAndTestId(idAlumno, testId)
                .map(test -> {
                    TestResultDTO result = TestResultDTO.fromTestGardner(test);
                    try {
                        if (test.getPuntajes() != null) {
                            Map<String, Integer> puntajes = objectMapper.readValue(
                                test.getPuntajes(),
                                new TypeReference<Map<String, Integer>>() {}
                            );
                            result.setPuntajesPonderados(puntajes);
                        }
                    } catch (Exception e) {
                        // Log error but continue
                    }
                    return result;
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> calculateIntelligenceScores(Map<Integer, Integer> responses) {
        Map<String, Integer> puntosPorInteligencia = new HashMap<>();
        Map<String, Integer> puntajesBrutos = new HashMap<>();
        
        // Initialize counters
        for (String inteligencia : INTELIGENCE_MAPPING.values()) {
            puntosPorInteligencia.put(inteligencia, 0);
            puntajesBrutos.put(inteligencia, 0);
        }
        
        // Calculate raw scores for each intelligence type
        for (Map.Entry<Integer, Integer> response : responses.entrySet()) {
            try {
                PreguntaGardner pregunta = preguntaRepository.findById(response.getKey())
                        .orElse(null);
                
                if (pregunta != null && pregunta.getActivo()) {
                    String inteligencia = INTELIGENCE_MAPPING.get(pregunta.getTipoInteligencia());
                    Integer respuestaValue = response.getValue();
                    
                    // Valores base dependiendo de la respuesta
                    Integer puntosRespuesta = respuestaValue; // 1=A, 2=B, 3=C, 4=D
                    
                    // Reinversión para Gardner (respuesta más alta = más puntos)
                    Integer puntosFinales = Math.min(puntosRespuesta, 4);
                    
                    puntosPorInteligencia.merge(inteligencia, puntosFinales, Integer::sum);
                    puntajesBrutos.merge(inteligencia, 1, Integer::sum);
                }
            } catch (Exception e) {
                // Skip invalid responses
                continue;
            }
        }
        
        // Calculate percentages (scaled to 0-100)
        Map<String, Integer> puntajesPonderados = new HashMap<>();
        for (Map.Entry<String, Integer> entry : puntosPorInteligencia.entrySet()) {
            String inteligencia = entry.getKey();
            Integer puntos = entry.getValue();
            Integer totalPreguntas = puntajesBrutos.get(inteligencia);
            
            if (totalPreguntas > 0) {
                // Escalar a 0-100 basado en el máximo posible (4 puntos por pregunta)
                Integer puntajeFinal = (puntos * 100) / (totalPreguntas * 4);
                puntajesPonderados.put(inteligencia, puntajeFinal);
            } else {
                puntajesPonderados.put(inteligencia, 0);
            }
        }
        
        // Find dominant intelligence
        String inteligenciaPredominante = puntosPorInteligencia.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("linguistico");
        
        // Calculate total score
        Double puntajeTotal = puntajesPonderados.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        
        Map<String, Object> result = new HashMap<>();
        result.put("puntajes", puntajesPonderados);
        result.put("puntajesBrutos", puntosPorInteligencia);
        result.put("inteligenciaPredominante", inteligenciaPredominante);
        result.put("puntajeTotal", puntajeTotal);
        
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getIntelligenceTypeStatistics() {
        List<Object[]> stats = preguntaRepository.countQuestionsByTipoInteligencia();
        return stats.stream()
                .collect(Collectors.toMap(
                    arr -> ((PreguntaGardner.TipoInteligencia) arr[0]).name(),
                    arr -> (Long) arr[1]
                ));
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateTestResponses(List<AutosaveRequestDTO.RespuestaTestDTO> respuestas) {
        if (respuestas == null || respuestas.isEmpty()) {
            return false;
        }
        
        // Check that each response has valid values
        return respuestas.stream().allMatch(respuesta -> 
            respuesta.getIdPregunta() != null && 
            respuesta.getOpcionSeleccionada() != null &&
            respuesta.getOpcionSeleccionada() >= 1 &&
            respuesta.getOpcionSeleccionada() <= 4
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canStudentTakeTest(Integer idAlumno) {
        // Check if student has taken the test recently (within last 6 months)
        List<TestGardner> recentTests = testRepository.findByAlumnoCompletedTests(idAlumno);
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        
        return recentTests.stream()
                .noneMatch(test -> test.getFechaAplicacion().isAfter(sixMonthsAgo));
    }
    
    // Helper methods
    private String convertResponsesToJson(List<AutosaveRequestDTO.RespuestaTestDTO> respuestas) throws JsonProcessingException {
        Map<Integer, Integer> responseMap = respuestas.stream()
                .collect(Collectors.toMap(
                    AutosaveRequestDTO.RespuestaTestDTO::getIdPregunta,
                    AutosaveRequestDTO.RespuestaTestDTO::getOpcionSeleccionada
                ));
        return objectMapper.writeValueAsString(responseMap);
    }
    
    private Map<Integer, Integer> convertResponsesToMap(List<AutosaveRequestDTO.RespuestaTestDTO> respuestas) {
        return respuestas.stream()
                .collect(Collectors.toMap(
                    AutosaveRequestDTO.RespuestaTestDTO::getIdPregunta,
                    AutosaveRequestDTO.RespuestaTestDTO::getOpcionSeleccionada
                ));
    }
    
    private String getIntelligenceDescription(String inteligencia) {
        Map<String, String> descriptions = Map.of(
            "musical", "Inteligencia Musical-Rítmica: Capacidad de percibir, discriminar, transformar y expresar las formas musicales.",
            "logico", "Inteligencia Lógico-Matemática: Capacidad de usar los números de manera efectiva y razonar bien.",
            "espacial", "Inteligencia Espacial: Capacidad de percibir de manera precisa el mundo visual-espacial.",
            "linguistico", "Inteligencia Lingüística: Capacidad de usar las palabras de manera efectiva.",
            "corporal", "Inteligencia Corporal-Cinestésica: Capacidad para usar todo el cuerpo para expresar ideas.",
            "interpersonal", "Inteligencia Interpersonal: Capacidad de percibir y establecer distinciones de estados de ánimo.",
            "intrapersonal", "Inteligencia Intrapersonal: Capacidad de autocomprensión y la comprensión de los propios sentimientos.",
            "naturalista", "Inteligencia Naturalista: Capacidad de reconocer y clasificar las numerosas especies del ambiente."
        );
        return descriptions.getOrDefault(inteligencia, "Inteligencia no especificada");
    }
    
    private String getAcademicRecommendations(String inteligencia) {
        Map<String, String> recommendations = Map.of(
            "musical", "Prueba actividades relacionadas con música, instrumentos, composición musical y expresión artística.",
            "logico", "Explora matemáticas avanzadas, programación, ciencias experimentales y proyectos de investigación.",
            "espacial", "Participa en actividades de arte visual, diseño gráfico, arquitectura y proyectos espaciales.",
            "linguistico", "Dedica tiempo a escritura creativa, literatura, debates y actividades comunicativas.",
            "corporal", "Involúc rate en deportes, teatro, danza, actividades manuales y proyectos que requieran movimiento.",
            "interpersonal", "Participa en trabajos grupales, liderazgo estudiantil y actividades colaborativas.",
            "intrapersonal", "Dedica tiempo a la reflexión personal, autodisciplina y proyectos individuales.",
            "naturalista", "Explora actividades relacionadas con biología, medio ambiente, investigación natural y actividades outdoor."
        );
        return recommendations.getOrDefault(inteligencia, "Explora diferentes áreas para encontrar tu pasión académica.");
    }
    
    @Override
    @Transactional(readOnly = true)
    public IntelligenceResultsDTO getAllIntelligenceResults() {
        try {
            IntelligenceResultsDTO results = new IntelligenceResultsDTO();
            
            // Get total tests and students
            long totalTests = testRepository.count();
            long totalStudents = alumnoRepository.count();
            
            // Get intelligence distribution
            Map<String, Long> intelligenceDistribution = getIntelligenceTypeStatistics();
            
            // Get predominant intelligence statistics
            Map<String, Long> predominantStatsLong = getPredominantIntelligenceStatistics();
            
            // Convert Long to Integer for the DTO
            Map<String, Integer> predominantStats = predominantStatsLong.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().intValue()
                    ));
            
            // Get recent results (last 10)
            List<TestGardner> recentTests = testRepository.findTop10ByOrderByFechaAplicacionDesc();
            List<TestResultDTO> recentResults = recentTests.stream()
                    .map(TestResultDTO::fromTestGardner)
                    .collect(Collectors.toList());
            
            // Calculate average scores
            Map<String, Double> averageScores = calculateAverageIntelligenceScores();
            
            // Get top intelligences
            List<String> topIntelligences = predominantStatsLong.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            
            results.setTotalTests((int) totalTests);
            results.setTotalStudents((int) totalStudents);
            results.setLastUpdate(LocalDateTime.now());
            results.setIntelligenceDistribution(intelligenceDistribution);
            results.setAverageScores(averageScores);
            results.setRecentResults(recentResults);
            results.setPredominantIntelligenceCount(predominantStats);
            results.setTopIntelligences(topIntelligences);
            results.setSystemStatus("ACTIVE");
            
            return results;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener resultados de inteligencias: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStudentHistoricalData(Integer idAlumno) {
        try {
            Map<String, Object> historicalData = new HashMap<>();
            
            // Get all tests for the student
            List<TestGardner> tests = testRepository.findByAlumnoCompletedTests(idAlumno);
            
            if (tests.isEmpty()) {
                historicalData.put("message", "No se encontraron datos históricos para el estudiante");
                return historicalData;
            }
            
            // Get student info
            Alumno alumno = alumnoRepository.findById(idAlumno).orElse(null);
            if (alumno != null) {
                historicalData.put("studentName", alumno.getUsuario().getNombreUsuario());
                historicalData.put("yearOfEntry", alumno.getAnioIngreso());
            }
            
            // Process test results
            List<Map<String, Object>> testHistory = new ArrayList<>();
            Map<String, List<Integer>> intelligenceEvolution = new HashMap<>();
            
            for (TestGardner test : tests) {
                Map<String, Object> testData = new HashMap<>();
                testData.put("testId", test.getIdTest());
                testData.put("date", test.getFechaAplicacion());
                testData.put("predominantIntelligence", test.getInteligenciaPredominante());
                testData.put("totalScore", test.getPuntajeTotal());
                testData.put("version", test.getVersionGuardado());
                
                // Parse scores if available
                if (test.getPuntajes() != null) {
                    try {
                        Map<String, Integer> scores = objectMapper.readValue(
                            test.getPuntajes(),
                            new TypeReference<Map<String, Integer>>() {}
                        );
                        testData.put("scores", scores);
                        
                        // Track evolution
                        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                            intelligenceEvolution.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                                    .add(entry.getValue());
                        }
                    } catch (Exception e) {
                        // Skip if parsing fails
                    }
                }
                
                testHistory.add(testData);
            }
            
            historicalData.put("testHistory", testHistory);
            historicalData.put("intelligenceEvolution", intelligenceEvolution);
            historicalData.put("totalTests", tests.size());
            historicalData.put("firstTestDate", tests.get(tests.size() - 1).getFechaAplicacion());
            historicalData.put("lastTestDate", tests.get(0).getFechaAplicacion());
            
            return historicalData;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener datos históricos: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getPredominantIntelligenceStatistics() {
        List<Object[]> stats = testRepository.countPredominantIntelligence();
        return stats.stream()
                .collect(Collectors.toMap(
                    arr -> (String) arr[0],
                    arr -> (Long) arr[1]
                ));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> compareIntelligenceResults(List<Integer> studentIds) {
        try {
            Map<String, Object> comparison = new HashMap<>();
            Map<String, List<TestResultDTO>> studentResults = new HashMap<>();
            
            for (Integer studentId : studentIds) {
                List<TestResultDTO> results = getTestHistory(studentId);
                Alumno alumno = alumnoRepository.findById(studentId).orElse(null);
                String studentName = alumno != null ? alumno.getUsuario().getNombreUsuario() : "Estudiante " + studentId;
                studentResults.put(studentName, results);
            }
            
            comparison.put("studentResults", studentResults);
            comparison.put("comparisonDate", LocalDateTime.now());
            comparison.put("totalStudents", studentIds.size());
            
            return comparison;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al comparar resultados: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getIntelligenceEvolution(Integer idAlumno) {
        try {
            Map<String, Object> evolution = new HashMap<>();
            
            List<TestGardner> tests = testRepository.findByAlumnoCompletedTests(idAlumno);
            if (tests.isEmpty()) {
                evolution.put("message", "No se encontraron tests para el estudiante");
                return evolution;
            }
            
            // Sort by date
            tests.sort((t1, t2) -> t1.getFechaAplicacion().compareTo(t2.getFechaAplicacion()));
            
            Map<String, List<Map<String, Object>>> intelligenceData = new HashMap<>();
            
            for (TestGardner test : tests) {
                if (test.getPuntajes() != null) {
                    try {
                        Map<String, Integer> scores = objectMapper.readValue(
                            test.getPuntajes(),
                            new TypeReference<Map<String, Integer>>() {}
                        );
                        
                        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                            String intelligence = entry.getKey();
                            Map<String, Object> dataPoint = new HashMap<>();
                            dataPoint.put("date", test.getFechaAplicacion());
                            dataPoint.put("score", entry.getValue());
                            dataPoint.put("testId", test.getIdTest());
                            
                            intelligenceData.computeIfAbsent(intelligence, k -> new ArrayList<>())
                                    .add(dataPoint);
                        }
                    } catch (Exception e) {
                        // Skip if parsing fails
                    }
                }
            }
            
            evolution.put("intelligenceEvolution", intelligenceData);
            evolution.put("totalTests", tests.size());
            evolution.put("evolutionPeriod", Map.of(
                "start", tests.get(0).getFechaAplicacion(),
                "end", tests.get(tests.size() - 1).getFechaAplicacion()
            ));
            
            return evolution;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener evolución de inteligencias: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String regenerateRecommendations(Integer idAlumno) {
        try {
            Optional<TestGardner> latestTest = testRepository.findFirstByAlumnoIdAlumnoOrderByFechaAplicacionDesc(idAlumno);
            
            if (latestTest.isPresent()) {
                TestGardner test = latestTest.get();
                String inteligenciaPredominante = test.getInteligenciaPredominante();
                return getAcademicRecommendations(inteligenciaPredominante);
            } else {
                return "No se encontraron resultados de test para generar recomendaciones";
            }
            
        } catch (Exception e) {
            return "Error al regenerar recomendaciones: " + e.getMessage();
        }
    }
    
    // Helper method to calculate average intelligence scores
    private Map<String, Double> calculateAverageIntelligenceScores() {
        List<TestGardner> allTests = testRepository.findAll();
        Map<String, List<Integer>> intelligenceScores = new HashMap<>();
        
        for (TestGardner test : allTests) {
            if (test.getPuntajes() != null) {
                try {
                    Map<String, Integer> scores = objectMapper.readValue(
                        test.getPuntajes(),
                        new TypeReference<Map<String, Integer>>() {}
                    );
                    
                    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                        intelligenceScores.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                                .add(entry.getValue());
                    }
                } catch (Exception e) {
                    // Skip if parsing fails
                }
            }
        }
        
        Map<String, Double> averages = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : intelligenceScores.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
            averages.put(entry.getKey(), average);
        }
        
        return averages;
    }
}
