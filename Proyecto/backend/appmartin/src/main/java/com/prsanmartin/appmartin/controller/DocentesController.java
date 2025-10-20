package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.DocenteDTO;
import com.prsanmartin.appmartin.service.DocenteService;
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
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
@Tag(name = "Teachers Management", description = "Endpoints para gestión completa de docentes")
public class DocentesController {

    @Autowired
    private DocenteService docenteService;

    @Operation(summary = "Obtener todos los docentes",
               description = "Obtiene la lista completa de docentes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de docentes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<DocenteDTO>> getAllTeachers() {
        try {
            List<DocenteDTO> docentes = docenteService.getAllTeachers();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener docente por ID",
               description = "Obtiene un docente específico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> getTeacherById(
            @Parameter(description = "ID del docente")
            @PathVariable Integer id) {
        try {
            Optional<DocenteDTO> docente = docenteService.getTeacherById(id);
            return docente.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear nuevo docente",
               description = "Crea un nuevo registro de docente")
    @PostMapping
    public ResponseEntity<DocenteDTO> createTeacher(
            @Parameter(description = "Datos del docente")
            @Valid @RequestBody DocenteDTO docenteDTO) {
        try {
            DocenteDTO nuevoDocente = docenteService.createTeacher(docenteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDocente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar docente",
               description = "Actualiza los datos de un docente existente")
    @PutMapping("/{id}")
    public ResponseEntity<DocenteDTO> updateTeacher(
            @Parameter(description = "ID del docente")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del docente")
            @Valid @RequestBody DocenteDTO docenteDTO) {
        try {
            DocenteDTO docenteActualizado = docenteService.updateTeacher(id, docenteDTO);
            return ResponseEntity.ok(docenteActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar docente",
               description = "Elimina un docente del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTeacher(
            @Parameter(description = "ID del docente")
            @PathVariable Integer id) {
        try {
            boolean eliminado = docenteService.deleteTeacher(id);
            if (eliminado) {
                Map<String, String> response = Map.of("message", "Docente eliminado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of("error", "No se pudo eliminar el docente");
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

    @Operation(summary = "Obtener docentes por especialidad",
               description = "Obtiene docentes filtrados por especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<DocenteDTO>> getTeachersBySpecialty(
            @Parameter(description = "Especialidad del docente")
            @PathVariable String especialidad) {
        try {
            List<DocenteDTO> docentes = docenteService.getTeachersBySpecialty(especialidad);
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener estadísticas de docentes",
               description = "Obtiene estadísticas generales de los docentes")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getTeacherStatistics() {
        try {
            Map<String, Object> estadisticas = docenteService.getTeacherStatistics();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
