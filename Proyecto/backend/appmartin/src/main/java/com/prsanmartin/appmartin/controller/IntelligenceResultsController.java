package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.TestResultDTO;
import com.prsanmartin.appmartin.dto.IntelligenceResultsDTO;
import com.prsanmartin.appmartin.service.TestGardnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/intelligence-results")
@CrossOrigin(origins = "*")
@Tag(name = "Intelligence Results", description = "Endpoints para consulta de resultados de inteligencias múltiples")
public class IntelligenceResultsController {
    
    @Autowired
    private TestGardnerService testGardnerService;
    
    @Operation(summary = "Obtener resultados de todas las inteligencias múltiples", 
               description = "Devuelve resultados completos de todas las inteligencias múltiples con actualización automática")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultados obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron resultados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/all")
    public ResponseEntity<IntelligenceResultsDTO> getAllIntelligenceResults() {
        try {
            IntelligenceResultsDTO results = testGardnerService.getAllIntelligenceResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener resultados de inteligencias por estudiante", 
               description = "Obtiene todos los resultados de inteligencias múltiples para un estudiante específico")
    @GetMapping("/student/{alumnoId}")
    public ResponseEntity<List<TestResultDTO>> getIntelligenceResultsByStudent(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer alumnoId) {
        try {
            List<TestResultDTO> results = testGardnerService.getTestHistory(alumnoId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener datos históricos del estudiante", 
               description = "Devuelve datos históricos completos del estudiante incluyendo evolución de inteligencias")
    @GetMapping("/student/{alumnoId}/historical")
    public ResponseEntity<Map<String, Object>> getStudentHistoricalData(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer alumnoId) {
        try {
            Map<String, Object> historicalData = testGardnerService.getStudentHistoricalData(alumnoId);
            return ResponseEntity.ok(historicalData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener último resultado de inteligencia del estudiante", 
               description = "Obtiene el resultado más reciente del test de inteligencias múltiples")
    @GetMapping("/student/{alumnoId}/latest")
    public ResponseEntity<TestResultDTO> getLatestIntelligenceResult(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer alumnoId) {
        try {
            Optional<TestResultDTO> latestResult = testGardnerService.getLatestTest(alumnoId);
            return latestResult.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estadísticas de inteligencias por tipo", 
               description = "Obtiene estadísticas de distribución de inteligencias por tipo")
    @GetMapping("/statistics/by-type")
    public ResponseEntity<Map<String, Long>> getIntelligenceTypeStatistics() {
        try {
            Map<String, Long> statistics = testGardnerService.getIntelligenceTypeStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estadísticas de inteligencias predominantes", 
               description = "Obtiene estadísticas de distribución de inteligencias predominantes")
    @GetMapping("/statistics/predominant")
    public ResponseEntity<Map<String, Long>> getPredominantIntelligenceStatistics() {
        try {
            Map<String, Long> statistics = testGardnerService.getPredominantIntelligenceStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener comparación de inteligencias entre estudiantes", 
               description = "Compara resultados de inteligencias entre múltiples estudiantes")
    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareIntelligenceResults(
            @Parameter(description = "Lista de IDs de estudiantes") 
            @RequestBody List<Integer> studentIds) {
        try {
            Map<String, Object> comparison = testGardnerService.compareIntelligenceResults(studentIds);
            return ResponseEntity.ok(comparison);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener evolución de inteligencia del estudiante", 
               description = "Muestra la evolución de las inteligencias múltiples del estudiante a lo largo del tiempo")
    @GetMapping("/student/{alumnoId}/evolution")
    public ResponseEntity<Map<String, Object>> getIntelligenceEvolution(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer alumnoId) {
        try {
            Map<String, Object> evolution = testGardnerService.getIntelligenceEvolution(alumnoId);
            return ResponseEntity.ok(evolution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Regenerar recomendaciones automáticamente", 
               description = "Regenera recomendaciones académicas basadas en los últimos resultados del test")
    @PostMapping("/regenerate-recommendations/{alumnoId}")
    public ResponseEntity<Map<String, String>> regenerateRecommendations(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer alumnoId) {
        try {
            String recommendations = testGardnerService.regenerateRecommendations(alumnoId);
            Map<String, String> response = Map.of("recommendations", recommendations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", "Error al regenerar recomendaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
