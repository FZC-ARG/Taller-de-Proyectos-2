package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    // Docentes
    @PostMapping("/docentes")
    public ResponseEntity<DocenteDTO> crearDocente(@RequestBody CrearDocenteRequest request) {
        return ResponseEntity.ok(adminService.crearDocente(request));
    }
    
    @PutMapping("/docentes/{id}")
    public ResponseEntity<DocenteDTO> actualizarDocente(@PathVariable Integer id, @RequestBody CrearDocenteRequest request) {
        return ResponseEntity.ok(adminService.actualizarDocente(id, request));
    }
    
    @DeleteMapping("/docentes/{id}")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Integer id) {
        adminService.eliminarDocente(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/docentes")
    public ResponseEntity<List<DocenteDTO>> listarDocentes() {
        return ResponseEntity.ok(adminService.listarDocentes());
    }
    
    // Alumnos
    @PostMapping("/alumnos")
    public ResponseEntity<AlumnoDTO> crearAlumno(@RequestBody CrearAlumnoRequest request) {
        return ResponseEntity.ok(adminService.crearAlumno(request));
    }
    
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoDTO> actualizarAlumno(@PathVariable Integer id, @RequestBody CrearAlumnoRequest request) {
        return ResponseEntity.ok(adminService.actualizarAlumno(id, request));
    }
    
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Integer id) {
        adminService.eliminarAlumno(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/alumnos")
    public ResponseEntity<List<AlumnoDTO>> listarAlumnos() {
        return ResponseEntity.ok(adminService.listarAlumnos());
    }
    
    // Logs
    @GetMapping("/logs")
    public ResponseEntity<List<LogAccesoDTO>> listarLogs() {
        return ResponseEntity.ok(adminService.listarLogs());
    }
}
