package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // Crear curso
    @PostMapping
    public ResponseEntity<CursoDTO> crearCurso(@RequestBody CrearCursoRequest request) {
        return ResponseEntity.ok(cursoService.crearCurso(request));
    }

    // Actualizar curso
    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizarCurso(@PathVariable Integer id, @RequestBody CrearCursoRequest request) {
        return ResponseEntity.ok(cursoService.actualizarCurso(id, request));
    }

    // Eliminar curso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Integer id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.ok().build();
    }

    // Listar cursos
    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    // Listar cursos por docente
    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<List<CursoDTO>> listarCursosPorDocente(@PathVariable Integer idDocente) {
        return ResponseEntity.ok(cursoService.listarCursosPorDocente(idDocente));
    }

    // Matricular alumno en curso
    @PostMapping("/matricular")
    public ResponseEntity<?> matricularAlumno(@RequestBody MatricularAlumnoRequest request) {
        try {
            cursoService.matricularAlumno(request);
            return ResponseEntity.ok().body(
                    new ResponseMessage("Alumno matriculado correctamente")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage("Error: " + e.getMessage())
            );
        }
    }


    // Listar alumnos de un curso
    @GetMapping("/{idCurso}/alumnos")
    public ResponseEntity<List<String>> listarAlumnosPorCurso(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(cursoService.listarAlumnosPorCurso(idCurso));
    }

    // Listar cursos de un alumno (cursos en los que está matriculado)
    @GetMapping("/alumno/{idAlumno}/cursos")
    public ResponseEntity<List<CursoDTO>> listarCursosPorAlumno(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(cursoService.listarCursosPorAlumno(idAlumno));
    }

    // Listar alumnos de un docente (alumnos inscritos en cursos que dicta el docente)
    @GetMapping("/docente/{idDocente}/alumnos")
    public ResponseEntity<List<String>> listarAlumnosPorDocente(@PathVariable Integer idDocente) {
        return ResponseEntity.ok(cursoService.listarAlumnosPorDocente(idDocente));
    }

}
