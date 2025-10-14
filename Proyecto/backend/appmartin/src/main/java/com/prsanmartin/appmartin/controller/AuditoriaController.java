package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.entity.Auditoria;
import com.prsanmartin.appmartin.service.AuditoriaService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
// @Tag(name = "Auditoría", description = "Endpoints para consultar registros de auditoría")
// @SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    // @Operation(summary = "Obtener todos los registros de auditoría", 
    //            description = "Retorna todos los registros de auditoría del sistema")
    public ResponseEntity<Map<String, Object>> obtenerAuditoria(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "fechaEvento") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            List<Auditoria> auditoria = auditoriaService.obtenerTodaLaAuditoria();
            
            response.put("exito", true);
            response.put("data", auditoria);
            response.put("total", auditoria.size());
            response.put("mensaje", "Registros de auditoría obtenidos exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al obtener auditoría: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/usuario/{nombreUsuario}")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    // @Operation(summary = "Obtener auditoría por usuario", 
    //            description = "Retorna los registros de auditoría de un usuario específico")
    public ResponseEntity<Map<String, Object>> obtenerAuditoriaPorUsuario(@PathVariable String nombreUsuario) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Auditoria> auditoria = auditoriaService.obtenerAuditoriaPorUsuario(nombreUsuario);
            
            response.put("exito", true);
            response.put("data", auditoria);
            response.put("total", auditoria.size());
            response.put("mensaje", "Auditoría del usuario obtenida exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al obtener auditoría del usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/entidad/{entidad}")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    // @Operation(summary = "Obtener auditoría por entidad", 
    //            description = "Retorna los registros de auditoría de una entidad específica")
    public ResponseEntity<Map<String, Object>> obtenerAuditoriaPorEntidad(@PathVariable String entidad) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Auditoria> auditoria = auditoriaService.obtenerAuditoriaPorEntidad(entidad);
            
            response.put("exito", true);
            response.put("data", auditoria);
            response.put("total", auditoria.size());
            response.put("mensaje", "Auditoría de la entidad obtenida exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al obtener auditoría de la entidad: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/estadisticas")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    // @Operation(summary = "Obtener estadísticas de auditoría", 
    //            description = "Retorna estadísticas generales de los registros de auditoría")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Auditoria> auditoria = auditoriaService.obtenerTodaLaAuditoria();
            
            Map<String, Long> acciones = new HashMap<>();
            Map<String, Long> entidades = new HashMap<>();
            Map<String, Long> usuarios = new HashMap<>();
            
            for (Auditoria registro : auditoria) {
                // Contar acciones
                acciones.merge(registro.getAccion(), 1L, Long::sum);
                
                // Contar entidades
                entidades.merge(registro.getEntidad(), 1L, Long::sum);
                
                // Contar usuarios
                if (registro.getUsuario() != null) {
                    usuarios.merge(registro.getUsuario().getNombreUsuario(), 1L, Long::sum);
                }
            }
            
            response.put("exito", true);
            response.put("totalRegistros", auditoria.size());
            response.put("acciones", acciones);
            response.put("entidades", entidades);
            response.put("usuarios", usuarios);
            response.put("mensaje", "Estadísticas obtenidas exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
