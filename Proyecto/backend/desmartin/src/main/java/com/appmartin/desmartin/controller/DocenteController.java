package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.TestService;
import com.appmartin.desmartin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docente")
public class DocenteController {
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/alumnos/{idAlumno}/resultados")
    public ResponseEntity<List<ResultadoDTO>> obtenerResultadosAlumno(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(testService.obtenerHistorialResultados(idAlumno));
    }
    
    @GetMapping("/{idDocente}")
    public ResponseEntity<DocenteDTO> obtenerDocentePorId(@PathVariable Integer idDocente) {
        return ResponseEntity.ok(adminService.obtenerDocentePorId(idDocente));
    }
}

