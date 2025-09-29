package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.PreguntaGardner;
import com.prsanmartin.appmartin.service.TestGardnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test-gardner")
@CrossOrigin(origins = "*")
@Tag(name = "Test Gardner", description = "Endpoints para el Test de Inteligencias Múltiples de Gardner")
public class TestGardnerController {
    
    @Autowired
    private TestGardnerService testGardnerService;
    
    @Operation(summary = "Obtener preguntas del test (paginado)", 
               description = "Permite obtener las preguntas del test Gardner de forma paginada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preguntas obtenidas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/questions")
    @PreAuthorize("hasRole('ALUMNO') or hasRole('DOCENTE')")
    public ResponseEntity<Page<PreguntaGardnerDTO>> getQuestions(
            @Parameter(description = "Parámetros de paginación") Pageable pageable) {
        try {
            Page<PreguntaGardnerDTO> questions = testGardnerService.getQuestions(pageable);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener todas las preguntas activas", 
               description = "Obtiene todas las preguntas activas del test Gardner")
    @GetMapping("/questions/all")
    @PreAuthorize("hasRole('ALUMNO') or hasRole('DOCENTE')")
    public ResponseEntity<List<PreguntaGardnerDTO>> getAllQuestions() {
        try {
            List<PreguntaGardnerDTO> questions = testGardnerService.getAllActiveQuestions();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener preguntas por tipo de inteligencia", 
               description = "Obtiene preguntas filtradas por tipo específico de inteligencia")
    @GetMapping("/questions/by-type/{tipoInteligencia}")
    @PreAuthorize("hasRole('ALUMNO') or hasRole('DOCENTE')")
    public ResponseEntity<List<PreguntaGardnerDTO>> getQuestionsByType(
            @Parameter(description = "Tipo de inteligencia") 
            @PathVariable PreguntaGardner.TipoInteligencia tipoInteligencia) {
        try {
            List<PreguntaGardnerDTO> questions = testGardnerService.getQuestionsByType(tipoInteligencia);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Autoguardado de respuestas", 
               description = "Permite guardar respuestas parciales del test de forma idempotente")
    @PostMapping("/{idAlumno}/autosave")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<AutosaveResponseDTO> autosaveTest(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer idAlumno,
            @Parameter(description = "Respuestas a guardar") 
            @Valid @RequestBody AutosaveRequestDTO request) {
        try {
            // Security: Ensure the request matches the path parameter
            if (!idAlumno.equals(request.getIdAlumno())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            AutosaveResponseDTO response = testGardnerService.autosaveTest(idAlumno, request);
            
            HttpStatus status = switch (response.getStatus()) { // Java 17+ switch expression
                case "saved" -> HttpStatus.OK;
                case "duplicate" -> HttpStatus.OK; // Still OK for duplicate (idempotent)
                case "error" -> HttpStatus.INTERNAL_SERVER_ERROR;
                default -> HttpStatus.INTERNAL_SERVER_ERROR;
            };
            
            return ResponseEntity.status(status).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            AutosaveResponseDTO errorResponse = AutosaveResponseDTO.error(e.getMessage(), 5);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "Envío final del test", 
               description = "Envía el test final y calcula las puntuaciones de inteligencias múltiples")
    @PostMapping("/{idAlumno}/submit")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<TestResultDTO> submitTest(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer idAlumno,
            @Parameter(description = "Respuestas finales del test") 
            @Valid @RequestBody AutosaveRequestDTO request) {
        try {
            // Security: Ensure the request matches the path parameter
            if (!idAlumno.equals(request.getIdAlumno())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            // Set final state
            request.setEstado("FINAL");
            
            TestResultDTO result = testGardnerService.submitTest(idAlumno, request);
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener historial de tests del alumno", 
               description = "Obtiene el historial completo de tests realizados por un alumno")
    @GetMapping("/{idAlumno}/history")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<List<TestResultDTO>> getTestHistory(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer idAlumno) {
        try {
            List<TestResultDTO> history = testGardnerService.getTestHistory(idAlumno);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener último test del alumno", 
               description = "Obtiene el resultado del último test realizado por el alumno")
    @GetMapping("/{idAlumno}/latest")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<TestResultDTO> getLatestTest(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer idAlumno) {
        try {
            Optional<TestResultDTO> latestTest = testGardnerService.getLatestTest(idAlumno);
            return latestTest.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener resultado específico del test", 
               description = "Obtiene el resultado de un test específico con control de acceso")
    @GetMapping("/{idAlumno}/results/{testId}")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<TestResultDTO> getTestResult(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer idAlumno,
            @Parameter(description = "ID del test") 
            @PathVariable Integer testId) {
        try {
            Optional<TestResultDTO> result = testGardnerService.getTestResult(idAlumno, testId);
            return result.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Verificar si el alumno puede tomar el test", 
               description = "Verifica si el alumno puede tomar el test (no tomado recientemente)")
    @GetMapping("/{idAlumno}/can-take")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<Map<String, Object>> canStudentTakeTest(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer idAlumno) {
        try {
            boolean canTake = testGardnerService.canStudentTakeTest(idAlumno);
            Map<String, Object> response = Map.of(
                "canTake", canTake,
                "message", canTake ? "Puede tomar el test" : "Ya tomó el test recientemente"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estadísticas de tipos de inteligencia", 
               description = "Obtiene estadísticas sobre la distribución de preguntas por tipo de inteligencia")
    @GetMapping("/statistics/intelligence-types")
    @PreAuthorize("hasRole('DOCENTE') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getIntelligenceTypeStatistics() {
        try {
            Map<String, Long> statistics = testGardnerService.getIntelligenceTypeStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
