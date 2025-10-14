package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.CursoDTO;
import com.prsanmartin.appmartin.service.CursoService;
import com.prsanmartin.appmartin.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CursosController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ALUMNO')")
    public ResponseEntity<Page<CursoDTO>> getAllCursos(Pageable pageable) {
        try {
            Page<CursoDTO> cursos = cursoService.getAllCursos(pageable);
            // auditoriaService.registrarEvento("CONSULTA", "CURSOS", "Consulta de todos los cursos", "127.0.0.1");
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            // auditoriaService.registrarEvento("ERROR", "CURSOS", "Error al consultar cursos: " + e.getMessage(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE') or hasRole('ALUMNO')")
    public ResponseEntity<CursoDTO> getCursoById(@PathVariable Long id) {
        try {
            CursoDTO curso = cursoService.getCursoById(id);
            if (curso != null) {
                // auditoriaService.registrarEvento("CONSULTA", "CURSOS", "Consulta de curso por ID: " + id, "127.0.0.1");
                return ResponseEntity.ok(curso);
            } else {
                // auditoriaService.registrarEvento("ERROR", "CURSOS", "Curso no encontrado con ID: " + id, "127.0.0.1");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // auditoriaService.registrarEvento("ERROR", "CURSOS", "Error al consultar curso: " + e.getMessage(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<CursoDTO> createCurso(@RequestBody CursoDTO cursoDTO) {
        try {
            CursoDTO createdCurso = cursoService.createCurso(cursoDTO);
            // auditoriaService.registrarEvento("CREACION", "CURSOS", "Curso creado: " + createdCurso.getNombreCurso(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurso);
        } catch (Exception e) {
            // auditoriaService.registrarEvento("ERROR", "CURSOS", "Error al crear curso: " + e.getMessage(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<CursoDTO> updateCurso(@PathVariable Long id, @RequestBody CursoDTO cursoDTO) {
        try {
            CursoDTO updatedCurso = cursoService.updateCurso(id, cursoDTO);
            if (updatedCurso != null) {
                // auditoriaService.registrarEvento("ACTUALIZACION", "CURSOS", "Curso actualizado: " + updatedCurso.getNombreCurso(), "127.0.0.1");
                return ResponseEntity.ok(updatedCurso);
            } else {
                // auditoriaService.registrarEvento("ERROR", "CURSOS", "Curso no encontrado para actualizar con ID: " + id, "127.0.0.1");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // auditoriaService.registrarEvento("ERROR", "CURSOS", "Error al actualizar curso: " + e.getMessage(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        try {
            cursoService.deleteCurso(id);
            // auditoriaService.registrarEvento("ELIMINACION", "CURSOS", "Curso eliminado con ID: " + id, "127.0.0.1");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // auditoriaService.registrarEvento("ERROR", "CURSOS", "Error al eliminar curso: " + e.getMessage(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/docente/{idDocente}")
    // TEMPORAL: Desactivar autenticación para pruebas
    // @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<Page<CursoDTO>> getCursosByDocente(@PathVariable Long idDocente, Pageable pageable) {
        try {
            Page<CursoDTO> cursos = cursoService.getCursosByDocente(idDocente, pageable);
            // auditoriaService.registrarEvento("CONSULTA", "CURSOS", "Consulta de cursos por docente ID: " + idDocente, "127.0.0.1");
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            // auditoriaService.registrarEvento("ERROR", "CURSOS", "Error al consultar cursos por docente: " + e.getMessage(), "127.0.0.1");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

