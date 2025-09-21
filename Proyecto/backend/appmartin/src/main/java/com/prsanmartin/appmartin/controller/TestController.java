package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.RolRepository;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Probar conexión contando registros
            long rolCount = rolRepository.count();
            long usuarioCount = usuarioRepository.count();
            
            response.put("status", "success");
            response.put("message", "Conexión a la base de datos exitosa");
            response.put("rolCount", rolCount);
            response.put("usuarioCount", usuarioCount);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error de conexión: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> getAllRoles() {
        try {
            List<Rol> roles = rolRepository.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/create-test-user")
    public ResponseEntity<Map<String, Object>> createTestUser() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Buscar rol ADMIN
            Rol adminRol = rolRepository.findByNombreRol("ADMIN");
            if (adminRol == null) {
                response.put("status", "error");
                response.put("message", "No se encontró el rol ADMIN");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear usuario de prueba
            Usuario testUser = new Usuario();
            testUser.setNombreUsuario("test_user_" + System.currentTimeMillis());
            testUser.setCorreoElectronico("test@example.com");
            testUser.setContrasenaHash("password_hash_test");
            testUser.setRol(adminRol);
            testUser.setFechaCreacion(LocalDateTime.now());

            Usuario savedUser = usuarioRepository.save(testUser);

            response.put("status", "success");
            response.put("message", "Usuario de prueba creado exitosamente");
            response.put("usuario", savedUser);
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al crear usuario de prueba: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(response);
        }
    }
}

