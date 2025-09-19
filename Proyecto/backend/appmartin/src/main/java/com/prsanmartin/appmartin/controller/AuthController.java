package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.LoginRequest;
import com.prsanmartin.appmartin.dto.LoginResponse;
import com.prsanmartin.appmartin.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = administradorService.autenticarAdministrador(loginRequest);
        
        if (response.isExito()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // En JWT, el logout se maneja del lado del cliente eliminando el token
        return ResponseEntity.ok("Logout exitoso");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Servicio de autenticación activo");
    }
}
