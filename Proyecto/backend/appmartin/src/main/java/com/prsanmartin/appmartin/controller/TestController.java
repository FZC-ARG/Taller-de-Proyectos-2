package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.RolRepository;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import com.prsanmartin.appmartin.service.TestGardnerService;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
@Tag(name = "Test Management", description = "Endpoints para gestión de tests y conexión")
public class TestController {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestGardnerService testGardnerService;

    // TEMP_DISABLED_FOR_TESTS: Método duplicado - usar TestGardnerController.getAllQuestions() en su lugar
    /*
    @Operation(summary = "Obtener preguntas del test",
               description = "Obtiene todas las preguntas del test de Gardner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Preguntas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/preguntas")
    public ResponseEntity<List<PreguntaGardnerDTO>> getPreguntas() {
        try {
            List<PreguntaGardnerDTO> preguntas = testGardnerService.getAllActiveQuestions();
            return ResponseEntity.ok(preguntas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    */

    // TEMP_DISABLED_FOR_TESTS: Método duplicado - usar TestGardnerController.submitTest() en su lugar
    /*
    @Operation(summary = "Enviar respuestas del test",
               description = "Recibe las respuestas del test y calcula los resultados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuestas procesadas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/respuestas")
    public ResponseEntity<TestResultDTO> submitRespuestas(
            @Parameter(description = "Respuestas del test")
            @Valid @RequestBody AutosaveRequestDTO request) {
        try {
            // Set final state for submission
            request.setEstado("FINAL");
            
            TestResultDTO result = testGardnerService.submitTest(request.getIdAlumno(), request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    */

    // TEMP_DISABLED_FOR_TESTS: Método duplicado - usar TestGardnerController.getLatestTest() en su lugar
    /*
    @Operation(summary = "Obtener resultados por alumno",
               description = "Obtiene los resultados del test para un alumno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultados obtenidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Alumno no encontrado o sin resultados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/resultados/{idAlumno}")
    public ResponseEntity<TestResultDTO> getResultados(
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
    */

    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Probar conexión contando registros
            long rolCount = rolRepository.count();
            long usuarioCount = usuarioRepository.count();
            
            response.put("status", "success");
            response.put("message", "Conexión a la base de datos exitosa");
            response.put("rolCount", rolCount);
            response.put("usuarioCount", usuarioCount);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error de conexión: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> getAllRoles() {
        try {
            List<Rol> roles = rolRepository.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/init-database")
    public ResponseEntity<Map<String, Object>> initDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Crear roles usando SQL nativo
            usuarioRepository.createRoleIfNotExists("ADMIN");
            usuarioRepository.createRoleIfNotExists("DOCENTE");
            usuarioRepository.createRoleIfNotExists("ALUMNO");

            response.put("status", "success");
            response.put("message", "Base de datos inicializada con roles básicos");
            response.put("roles", List.of("ADMIN", "DOCENTE", "ALUMNO"));
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al inicializar base de datos: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(response);
        }
    }
}

