package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.AIRequestDTO;
import com.prsanmartin.appmartin.dto.AIResponseDTO;
import com.prsanmartin.appmartin.entity.SolicitudIA;
import com.prsanmartin.appmartin.entity.RespuestaIA;
import com.prsanmartin.appmartin.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
@Tag(name = "AI Integration", description = "Endpoints para integración con servicios de IA")
public class AIController {
    
    @Autowired
    private AIService aiService;
    
    @Operation(summary = "Procesar solicitud de IA", 
               description = "Envía una solicitud al servicio de IA y devuelve la respuesta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud procesada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/process")
    public ResponseEntity<AIResponseDTO> processAIRequest(
            @Parameter(description = "Datos de la solicitud de IA") 
            @Valid @RequestBody AIRequestDTO request) {
        try {
            AIResponseDTO response = aiService.processAIRequest(request);
            
            if ("ERROR".equals(response.getEstado())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            AIResponseDTO errorResponse = AIResponseDTO.error("Error al procesar solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "Procesar solicitud con perfil del estudiante", 
               description = "Procesa una solicitud de IA integrando el perfil del estudiante (test Gardner)")
    @PostMapping("/process-with-profile/{alumnoId}")
    public ResponseEntity<AIResponseDTO> processAIRequestWithProfile(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer alumnoId,
            @Parameter(description = "Datos de la solicitud de IA") 
            @Valid @RequestBody AIRequestDTO request) {
        try {
            AIResponseDTO response = aiService.processAIRequestWithProfile(request, alumnoId);
            
            if ("ERROR".equals(response.getEstado())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            AIResponseDTO errorResponse = AIResponseDTO.error("Error al procesar solicitud con perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "Generar recomendaciones académicas", 
               description = "Genera recomendaciones académicas basadas en los resultados del test Gardner")
    @PostMapping("/recommendations/{alumnoId}")
    public ResponseEntity<Map<String, String>> generateAcademicRecommendations(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer alumnoId,
            @Parameter(description = "Inteligencia predominante") 
            @RequestParam String inteligenciaPredominante) {
        try {
            String recommendations = aiService.generateAcademicRecommendations(alumnoId, inteligenciaPredominante);
            Map<String, String> response = Map.of("recommendations", recommendations);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of("error", "Error al generar recomendaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "Obtener historial de solicitudes de IA por docente", 
               description = "Obtiene el historial de solicitudes de IA realizadas por un docente")
    @GetMapping("/history/teacher/{docenteId}")
    public ResponseEntity<List<SolicitudIA>> getAIRequestHistoryByTeacher(
            @Parameter(description = "ID del docente") 
            @PathVariable Integer docenteId) {
        try {
            List<SolicitudIA> history = aiService.getAIRequestHistory(docenteId);
            return ResponseEntity.ok(history);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener historial de solicitudes de IA por estudiante", 
               description = "Obtiene el historial de solicitudes de IA relacionadas con un estudiante")
    @GetMapping("/history/student/{alumnoId}")
    public ResponseEntity<List<SolicitudIA>> getAIRequestHistoryByStudent(
            @Parameter(description = "ID del alumno") 
            @PathVariable Integer alumnoId) {
        try {
            List<SolicitudIA> history = aiService.getAIRequestHistoryByStudent(alumnoId);
            return ResponseEntity.ok(history);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener respuesta de IA por solicitud", 
               description = "Obtiene la respuesta de IA para una solicitud específica")
    @GetMapping("/response/{solicitudId}")
    public ResponseEntity<RespuestaIA> getAIResponse(
            @Parameter(description = "ID de la solicitud") 
            @PathVariable Integer solicitudId) {
        try {
            Optional<RespuestaIA> response = aiService.getAIResponse(solicitudId);
            return response.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Verificar disponibilidad del servicio de IA", 
               description = "Verifica si el servicio de IA está disponible")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkAIServiceStatus() {
        try {
            boolean available = aiService.isAIServiceAvailable();
            Map<String, Object> response = Map.of(
                "available", available,
                "message", available ? "Servicio de IA disponible" : "Servicio de IA no disponible",
                "timestamp", java.time.LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "available", false,
                "error", "Error al verificar servicio: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @Operation(summary = "Procesar solicitud en cola (para sobrecarga)", 
               description = "Añade una solicitud a la cola de procesamiento para manejar sobrecarga")
    @PostMapping("/queue")
    public ResponseEntity<Map<String, String>> addToQueue(
            @Parameter(description = "Datos de la solicitud de IA") 
            @Valid @RequestBody AIRequestDTO request) {
        try {
            // This would be implemented in the service for queue management
            Map<String, String> response = Map.of(
                "status", "added_to_queue",
                "message", "Solicitud añadida a la cola de procesamiento"
            );
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = Map.of(
                "error", "Error al añadir a la cola: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
