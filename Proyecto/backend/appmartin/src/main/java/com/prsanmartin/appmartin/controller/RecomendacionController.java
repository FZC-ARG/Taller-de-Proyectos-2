package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.RecomendacionDTO;
import com.prsanmartin.appmartin.dto.RecomendacionHistorialDTO;
import com.prsanmartin.appmartin.entity.PlantillaRecomendacion;
import com.prsanmartin.appmartin.entity.HistorialRecomendacion;
import com.prsanmartin.appmartin.service.RecomendacionService;
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

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
@Tag(name = "Recommendation System", description = "Endpoints para el sistema de recomendaciones académicas")
public class RecomendacionController {
    
    @Autowired
    private RecomendacionService recomendacionService;
    
    @Operation(summary = "Generar recomendaciones para un estudiante", 
               description = "Genera recomendaciones basadas en los resultados del test Gardner")
    @PostMapping("/generate/{idAlumno}/{idTest}")
    public ResponseEntity<List<RecomendacionDTO>> generateRecommendations(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer idAlumno,
            @Parameter(description = "ID del test") 
            @PathVariable Integer idTest) {
        try {
            List<RecomendacionDTO> recomendaciones = recomendacionService.generateRecommendations(idAlumno, idTest);
            return ResponseEntity.ok(recomendaciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Regenerar recomendaciones para un estudiante", 
               description = "Regenera recomendaciones basadas en el último test del estudiante")
    @PostMapping("/regenerate/{idAlumno}")
    public ResponseEntity<List<RecomendacionDTO>> regenerateRecommendations(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer idAlumno) {
        try {
            List<RecomendacionDTO> recomendaciones = recomendacionService.regenerateRecommendations(idAlumno);
            return ResponseEntity.ok(recomendaciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener historial de recomendaciones de un estudiante", 
               description = "Obtiene el historial completo de recomendaciones para un estudiante")
    @GetMapping("/history/student/{idAlumno}")
    public ResponseEntity<List<RecomendacionHistorialDTO>> getRecommendationHistory(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer idAlumno) {
        try {
            List<RecomendacionHistorialDTO> historial = recomendacionService.getRecommendationHistory(idAlumno);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener historial de recomendaciones para docentes", 
               description = "Obtiene el historial de recomendaciones de todos los estudiantes de un docente")
    @GetMapping("/history/teacher/{docenteId}")
    public ResponseEntity<List<RecomendacionHistorialDTO>> getRecommendationHistoryForTeachers(
            @Parameter(description = "ID del docente") 
            @PathVariable Integer docenteId) {
        try {
            List<RecomendacionHistorialDTO> historial = recomendacionService.getRecommendationHistoryForTeachers(docenteId);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Actualizar estado de recomendación", 
               description = "Actualiza el estado de una recomendación (aplicada, descartada, etc.)")
    @PutMapping("/{idHistorial}/status")
    public ResponseEntity<Map<String, String>> updateRecommendationStatus(
            @Parameter(description = "ID del historial de recomendación") 
            @PathVariable Integer idHistorial,
            @Parameter(description = "Nuevo estado") 
            @RequestParam HistorialRecomendacion.EstadoRecomendacion estado,
            @Parameter(description = "Notas adicionales") 
            @RequestParam(required = false) String notas) {
        try {
            boolean updated = recomendacionService.updateRecommendationStatus(idHistorial, estado, notas);
            if (updated) {
                Map<String, String> response = Map.of("message", "Estado actualizado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of("error", "No se pudo actualizar el estado");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @Operation(summary = "Obtener plantillas de recomendaciones", 
               description = "Obtiene todas las plantillas de recomendaciones activas")
    @GetMapping("/templates")
    public ResponseEntity<List<PlantillaRecomendacion>> getRecommendationTemplates() {
        try {
            List<PlantillaRecomendacion> plantillas = recomendacionService.getRecommendationTemplates();
            return ResponseEntity.ok(plantillas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Crear nueva plantilla de recomendación", 
               description = "Crea una nueva plantilla de recomendación")
    @PostMapping("/templates")
    public ResponseEntity<PlantillaRecomendacion> createRecommendationTemplate(
            @Parameter(description = "Datos de la plantilla") 
            @Valid @RequestBody PlantillaRecomendacion plantilla) {
        try {
            PlantillaRecomendacion nuevaPlantilla = recomendacionService.createRecommendationTemplate(plantilla);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPlantilla);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Actualizar plantilla de recomendación", 
               description = "Actualiza una plantilla de recomendación existente")
    @PutMapping("/templates/{idPlantilla}")
    public ResponseEntity<PlantillaRecomendacion> updateRecommendationTemplate(
            @Parameter(description = "ID de la plantilla") 
            @PathVariable Integer idPlantilla,
            @Parameter(description = "Datos actualizados de la plantilla") 
            @Valid @RequestBody PlantillaRecomendacion plantilla) {
        try {
            PlantillaRecomendacion plantillaActualizada = recomendacionService.updateRecommendationTemplate(idPlantilla, plantilla);
            return ResponseEntity.ok(plantillaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Eliminar plantilla de recomendación", 
               description = "Elimina una plantilla de recomendación (soft delete)")
    @DeleteMapping("/templates/{idPlantilla}")
    public ResponseEntity<Map<String, String>> deleteRecommendationTemplate(
            @Parameter(description = "ID de la plantilla") 
            @PathVariable Integer idPlantilla) {
        try {
            boolean deleted = recomendacionService.deleteRecommendationTemplate(idPlantilla);
            if (deleted) {
                Map<String, String> response = Map.of("message", "Plantilla eliminada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of("error", "No se pudo eliminar la plantilla");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            Map<String, String> response = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @Operation(summary = "Obtener estadísticas de recomendaciones", 
               description = "Obtiene estadísticas generales del sistema de recomendaciones")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getRecommendationStatistics() {
        try {
            Map<String, Object> estadisticas = recomendacionService.getRecommendationStatistics();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener recomendaciones por tipo", 
               description = "Obtiene recomendaciones filtradas por tipo específico")
    @GetMapping("/by-type/{tipoRecomendacion}")
    public ResponseEntity<List<RecomendacionHistorialDTO>> getRecommendationsByType(
            @Parameter(description = "Tipo de recomendación") 
            @PathVariable HistorialRecomendacion.TipoRecomendacion tipoRecomendacion) {
        try {
            List<RecomendacionHistorialDTO> recomendaciones = recomendacionService.getRecommendationsByType(tipoRecomendacion);
            return ResponseEntity.ok(recomendaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
