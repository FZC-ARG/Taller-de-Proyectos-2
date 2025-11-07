package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.LogAcceso;
import com.appmartin.desmartin.service.AuthService;
import com.appmartin.desmartin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private LogService logService;
    
    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        try {
            AdministradorDTO admin = authService.loginAdmin(request);

            if (admin != null) {
                logService.registrarEvento(
                        admin.getIdAdmin(),
                        "admin",
                        "LOGIN_OK",
                        "Administrador",
                        admin.getIdAdmin(),
                        "Inicio de sesión exitoso",
                        "AuthController.loginAdmin",
                        "INFO"
                );
                return ResponseEntity.ok(admin);
            } else {
                logService.registrarEvento(
                        null,
                        "admin",
                        "LOGIN_FAIL",
                        "Administrador",
                        null,
                        "Credenciales inválidas",
                        "AuthController.loginAdmin",
                        "WARN"
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
        } catch (Exception e) {
            logService.registrarEvento(
                    null,
                    "admin",
                    "LOGIN_ERROR",
                    "Administrador",
                    null,
                    e.getMessage(),
                    "AuthController.loginAdmin",
                    "ERROR"
            );
            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(e);
        }
    }
    
    @PostMapping("/login/docente")
    public ResponseEntity<?> loginDocente(@RequestBody LoginRequest request) {
        try {
            DocenteDTO docente = authService.loginDocente(request);

            if (docente != null) {
                logService.registrarEvento(
                        docente.getIdDocente(),
                        "docente",
                        "LOGIN_OK",
                        "Docente",
                        docente.getIdDocente(),
                        "Inicio de sesión exitoso",
                        "AuthController.loginDocente",
                        "INFO"
                );
                return ResponseEntity.ok(docente);
            } else {
                logService.registrarEvento(
                        null,
                        "docente",
                        "LOGIN_FAIL",
                        "Docente",
                        null,
                        "Credenciales inválidas",
                        "AuthController.loginDocente",
                        "WARN"
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
        } catch (Exception e) {
            logService.registrarEvento(
                    null,
                    "docente",
                    "LOGIN_ERROR",
                    "Docente",
                    null,
                    e.getMessage(),
                    "AuthController.loginDocente",
                    "ERROR"
            );
            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(e);
        }
    }
    
    @PostMapping("/login/alumno")
    public ResponseEntity<?> loginAlumno(@RequestBody LoginRequest request) {
        try {
            AlumnoDTO alumno = authService.loginAlumno(request);

            if (alumno != null) {
                logService.registrarEvento(
                        alumno.getIdAlumno(),
                        "alumno",
                        "LOGIN_OK",
                        "Alumno",
                        alumno.getIdAlumno(),
                        "Inicio de sesión exitoso",
                        "AuthController.loginAlumno",
                        "INFO"
                );
                return ResponseEntity.ok(alumno);
            } else {
                logService.registrarEvento(
                        null,
                        "alumno",
                        "LOGIN_FAIL",
                        "Alumno",
                        null,
                        "Credenciales inválidas",
                        "AuthController.loginAlumno",
                        "WARN"
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
        } catch (Exception e) {
            logService.registrarEvento(
                    null,
                    "alumno",
                    "LOGIN_ERROR",
                    "Alumno",
                    null,
                    e.getMessage(),
                    "AuthController.loginAlumno",
                    "ERROR"
            );
            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<LogAcceso>> listarLogs() {
        return ResponseEntity.ok(logService.obtenerTodos());
    }
}

