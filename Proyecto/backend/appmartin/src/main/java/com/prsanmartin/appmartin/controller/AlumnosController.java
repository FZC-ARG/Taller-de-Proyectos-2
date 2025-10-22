package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.AlumnoDTO;
import com.prsanmartin.appmartin.dto.DocenteDTO;
import com.prsanmartin.appmartin.service.AlumnoService;
import com.prsanmartin.appmartin.service.DocenteService;
import com.prsanmartin.appmartin.service.AuditoriaService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
@Tag(name = "Students Management", description = "Endpoints para gestión completa de estudiantes")
public class AlumnosController {
    
    @Autowired
    private AlumnoService alumnoService;
    
    @Operation(summary = "Obtener todos los estudiantes", 
               description = "Obtiene la lista completa de estudiantes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<AlumnoDTO>> getAllStudents() {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.getAllStudents();
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estudiantes filtrados por docente y curso", 
               description = "Obtiene estudiantes filtrados por docente específico y curso")
    @GetMapping("/filter")
    public ResponseEntity<List<AlumnoDTO>> getStudentsByTeacherAndCourse(
            @Parameter(description = "ID del docente") 
            @RequestParam Integer docenteId,
            @Parameter(description = "ID del curso") 
            @RequestParam Integer cursoId) {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.getStudentsByTeacherAndCourse(docenteId, cursoId);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estudiante por ID", 
               description = "Obtiene un estudiante específico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> getStudentById(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer id) {
        try {
            Optional<AlumnoDTO> estudiante = alumnoService.getStudentById(id);
            return estudiante.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Crear nuevo estudiante", 
               description = "Crea un nuevo registro de estudiante")
    @PostMapping
    public ResponseEntity<AlumnoDTO> createStudent(
            @Parameter(description = "Datos del estudiante") 
            @Valid @RequestBody AlumnoDTO alumnoDTO) {
        try {
            AlumnoDTO nuevoEstudiante = alumnoService.createStudent(alumnoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstudiante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Actualizar estudiante", 
               description = "Actualiza los datos de un estudiante existente")
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDTO> updateStudent(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del estudiante") 
            @Valid @RequestBody AlumnoDTO alumnoDTO) {
        try {
            AlumnoDTO estudianteActualizado = alumnoService.updateStudent(id, alumnoDTO);
            return ResponseEntity.ok(estudianteActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Eliminar estudiante (soft delete)", 
               description = "Elimina un estudiante de forma lógica (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer id) {
        try {
            boolean eliminado = alumnoService.deleteStudent(id);
            if (eliminado) {
                Map<String, String> response = Map.of("message", "Estudiante eliminado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of("error", "No se pudo eliminar el estudiante");
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
    
    @Operation(summary = "Eliminar estudiante permanentemente", 
               description = "Elimina un estudiante de forma permanente (irreversible)")
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, String>> permanentDeleteStudent(
            @Parameter(description = "ID del estudiante") 
            @PathVariable Integer id) {
        try {
            boolean eliminado = alumnoService.permanentDeleteStudent(id);
            if (eliminado) {
                Map<String, String> response = Map.of("message", "Estudiante eliminado permanentemente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of("error", "No se pudo eliminar el estudiante");
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
    
    @Operation(summary = "Buscar estudiantes por nombre (exacto)", 
               description = "Busca estudiantes por nombre de usuario exacto")
    @GetMapping("/search/exact")
    public ResponseEntity<List<AlumnoDTO>> searchStudentsByName(
            @Parameter(description = "Nombre de usuario") 
            @RequestParam String nombre) {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.searchStudentsByName(nombre);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Buscar estudiantes por nombre (parcial)", 
               description = "Busca estudiantes por nombre de usuario parcial")
    @GetMapping("/search/partial")
    public ResponseEntity<List<AlumnoDTO>> searchStudentsByNamePartial(
            @Parameter(description = "Nombre de usuario parcial") 
            @RequestParam String nombre) {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.searchStudentsByNamePartial(nombre);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estudiantes por año de ingreso", 
               description = "Obtiene estudiantes filtrados por año de ingreso")
    @GetMapping("/year/{anioIngreso}")
    public ResponseEntity<List<AlumnoDTO>> getStudentsByYear(
            @Parameter(description = "Año de ingreso") 
            @PathVariable Integer anioIngreso) {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.getStudentsByYear(anioIngreso);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estudiantes por curso", 
               description = "Obtiene estudiantes matriculados en un curso específico")
    @GetMapping("/course/{cursoId}")
    public ResponseEntity<List<AlumnoDTO>> getStudentsByCourse(
            @Parameter(description = "ID del curso") 
            @PathVariable Integer cursoId) {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.getStudentsByCourse(cursoId);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estudiantes por docente", 
               description = "Obtiene estudiantes de todos los cursos de un docente")
    @GetMapping("/teacher/{docenteId}")
    public ResponseEntity<List<AlumnoDTO>> getStudentsByTeacher(
            @Parameter(description = "ID del docente") 
            @PathVariable Integer docenteId) {
        try {
            List<AlumnoDTO> estudiantes = alumnoService.getStudentsByTeacher(docenteId);
            return ResponseEntity.ok(estudiantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "Obtener estadísticas de estudiantes",
               description = "Obtiene estadísticas generales de los estudiantes")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStudentStatistics() {
        try {
            Map<String, Object> estadisticas = alumnoService.getStudentStatistics();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

// TEMP_DISABLED_FOR_TESTS: Controller adicional para endpoints con rutas específicas solicitadas
// CONFLICTO RESUELTO: Este controlador duplicaba rutas ya existentes en otros controladores
// Las rutas /api/alumno/{id} y /api/docente/{id} ya están disponibles en AlumnosController y DocentesController
/*
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Specific Endpoints", description = "Endpoints con rutas específicas solicitadas")
class SpecificEndpointsController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Operation(summary = "Obtener alumno por ID",
               description = "Obtiene un alumno específico por su ID con ruta /alumno/{id}")
    @GetMapping("/alumno/{id}")
    public ResponseEntity<AlumnoDTO> getAlumnoById(
            @Parameter(description = "ID del alumno")
            @PathVariable Integer id) {
        try {
            Optional<AlumnoDTO> alumno = alumnoService.getStudentById(id);
            return alumno.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener docente por ID",
               description = "Obtiene un docente específico por su ID con ruta /docente/{id}")
    @GetMapping("/docente/{id}")
    public ResponseEntity<DocenteDTO> getDocenteById(
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

    @Operation(summary = "Registrar actividad individual",
               description = "Registra una actividad específica en el sistema de auditoría")
    @PostMapping("/actividades/registrar")
    public ResponseEntity<Map<String, Object>> registrarActividad(
            @Parameter(description = "Datos de la actividad a registrar")
            @RequestBody Map<String, Object> actividadData) {
        try {
            String accion = (String) actividadData.get("accion");
            String entidad = (String) actividadData.get("entidad");
            String descripcion = (String) actividadData.get("descripcion");
            String usuario = (String) actividadData.get("usuario");

            auditoriaService.registrarAccion(
                accion != null ? accion : "ACTIVIDAD_REGISTRADA",
                entidad != null ? entidad : "SISTEMA",
                null,
                descripcion != null ? descripcion : "Actividad registrada manualmente",
                null,
                usuario != null ? usuario : "SYSTEM"
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Actividad registrada exitosamente");
            response.put("timestamp", java.time.LocalDateTime.now());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al registrar actividad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
*/
