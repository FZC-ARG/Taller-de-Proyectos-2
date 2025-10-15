package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.LoginRequest;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar usuario por nombre de usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(loginRequest.getUsuario());
            
            if (usuarioOpt.isEmpty()) {
                response.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(401).body(response);
            }

            Usuario usuario = usuarioOpt.get();
            
            // Verificar contraseña
            if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getContrasenaHash())) {
                response.put("mensaje", "Contraseña incorrecta");
                return ResponseEntity.status(401).body(response);
            }

            // Login exitoso
            response.put("mensaje", "Inicio de sesión exitoso");
            response.put("rol", usuario.getRol().getNombreRol());
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("nombreUsuario", usuario.getNombreUsuario());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("mensaje", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
