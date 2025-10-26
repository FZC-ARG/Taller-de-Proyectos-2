package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        AdministradorDTO admin = authService.loginAdmin(request);
        
        if (admin != null) {
            return ResponseEntity.ok(admin);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
    
    @PostMapping("/login/docente")
    public ResponseEntity<?> loginDocente(@RequestBody LoginRequest request) {
        DocenteDTO docente = authService.loginDocente(request);
        
        if (docente != null) {
            return ResponseEntity.ok(docente);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
    
    @PostMapping("/login/alumno")
    public ResponseEntity<?> loginAlumno(@RequestBody LoginRequest request) {
        AlumnoDTO alumno = authService.loginAlumno(request);
        
        if (alumno != null) {
            return ResponseEntity.ok(alumno);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
}

