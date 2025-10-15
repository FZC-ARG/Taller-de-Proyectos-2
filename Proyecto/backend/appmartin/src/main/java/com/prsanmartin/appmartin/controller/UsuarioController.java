package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import com.prsanmartin.appmartin.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear usuario
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String nombreUsuario = request.get("nombreUsuario");
            String contrasena = request.get("contrasena");
            String rol = request.get("rol");

            // Validar campos requeridos
            if (nombreUsuario == null || contrasena == null || rol == null) {
                response.put("mensaje", "Los campos nombreUsuario, contrasena y rol son obligatorios");
                return ResponseEntity.badRequest().body(response);
            }

            // Verificar si el usuario ya existe
            if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
                response.put("mensaje", "El nombre de usuario ya existe");
                return ResponseEntity.badRequest().body(response);
            }

            // Buscar el rol
            Rol rolEntity = rolRepository.findByNombreRol(rol);
            if (rolEntity == null) {
                response.put("mensaje", "El rol especificado no existe. Roles disponibles: ADMIN, DOCENTE, ALUMNO");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            nuevoUsuario.setContrasenaHash(passwordEncoder.encode(contrasena));
            nuevoUsuario.setRol(rolEntity);
            nuevoUsuario.setFechaCreacion(LocalDateTime.now());
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setCorreoElectronico(nombreUsuario + "@sistema.com"); // Email dummy

            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            response.put("mensaje", "Usuario creado exitosamente");
            response.put("idUsuario", usuarioGuardado.getIdUsuario());
            response.put("nombreUsuario", usuarioGuardado.getNombreUsuario());
            response.put("rol", usuarioGuardado.getRol().getNombreRol());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al crear usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarUsuarios() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            
            response.put("mensaje", "Usuarios obtenidos exitosamente");
            response.put("usuarios", usuarios.stream().map(u -> {
                Map<String, Object> usuarioInfo = new HashMap<>();
                usuarioInfo.put("idUsuario", u.getIdUsuario());
                usuarioInfo.put("nombreUsuario", u.getNombreUsuario());
                usuarioInfo.put("rol", u.getRol().getNombreRol());
                usuarioInfo.put("activo", u.getActivo());
                usuarioInfo.put("fechaCreacion", u.getFechaCreacion());
                return usuarioInfo;
            }).toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al obtener usuarios: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuario(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isEmpty()) {
                response.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();
            response.put("mensaje", "Usuario obtenido exitosamente");
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("nombreUsuario", usuario.getNombreUsuario());
            response.put("rol", usuario.getRol().getNombreRol());
            response.put("activo", usuario.getActivo());
            response.put("fechaCreacion", usuario.getFechaCreacion());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al obtener usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Actualizar rol de usuario
    @PutMapping("/{id}/rol")
    public ResponseEntity<Map<String, Object>> actualizarRol(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String nuevoRol = request.get("rol");
            
            if (nuevoRol == null) {
                response.put("mensaje", "El campo 'rol' es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            if (usuarioOpt.isEmpty()) {
                response.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.notFound().build();
            }

            Rol rolEntity = rolRepository.findByNombreRol(nuevoRol);
            if (rolEntity == null) {
                response.put("mensaje", "El rol especificado no existe. Roles disponibles: ADMIN, DOCENTE, ALUMNO");
                return ResponseEntity.badRequest().body(response);
            }

            Usuario usuario = usuarioOpt.get();
            usuario.setRol(rolEntity);
            usuarioRepository.save(usuario);

            response.put("mensaje", "Rol actualizado exitosamente");
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("nombreUsuario", usuario.getNombreUsuario());
            response.put("rol", usuario.getRol().getNombreRol());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al actualizar rol: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isEmpty()) {
                response.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();
            usuarioRepository.delete(usuario);

            response.put("mensaje", "Usuario eliminado exitosamente");
            response.put("idUsuario", id);
            response.put("nombreUsuario", usuario.getNombreUsuario());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Obtener roles disponibles
    @GetMapping("/roles")
    public ResponseEntity<Map<String, Object>> obtenerRoles() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Rol> roles = rolRepository.findAll();
            
            response.put("mensaje", "Roles obtenidos exitosamente");
            response.put("roles", roles.stream().map(r -> {
                Map<String, Object> rolInfo = new HashMap<>();
                rolInfo.put("idRol", r.getIdRol());
                rolInfo.put("nombreRol", r.getNombreRol());
                return rolInfo;
            }).toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al obtener roles: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
