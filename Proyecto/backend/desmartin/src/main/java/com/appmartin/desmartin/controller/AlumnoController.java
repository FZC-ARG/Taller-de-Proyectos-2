package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.TestService;
import com.appmartin.desmartin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumno")
public class AlumnoController {
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/{idAlumno}/resultados/ultimo")
    public ResponseEntity<List<ResultadoDTO>> obtenerUltimoResultado(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(testService.obtenerUltimoResultado(idAlumno));
    }
    
    @GetMapping("/{idAlumno}/resultados/historial")
    public ResponseEntity<List<ResultadoDTO>> obtenerHistorialResultados(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(testService.obtenerHistorialResultados(idAlumno));
    }
    
    @GetMapping("/{idAlumno}")
    public ResponseEntity<AlumnoDTO> obtenerAlumnoPorId(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(adminService.obtenerAlumnoPorId(idAlumno));
    }
}

