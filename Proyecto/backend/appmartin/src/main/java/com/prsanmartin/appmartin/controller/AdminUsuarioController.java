package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.UsuarioDto;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Listar todos los administradores (usuarios con rol ADMIN)
    @GetMapping("/administradores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> obtenerAdmins() {
        List<Usuario> admins = usuarioService.findAllByRoleName("ADMIN");
        return ResponseEntity.ok(admins);
    }

    // Obtener admin por id (filtro por usuario y rol)
    @GetMapping("/administradores/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> obtenerAdminPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.findByIdIfRoleName(id, "ADMIN");
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Crear admin (crea un Usuario y le asigna el rol ADMIN)
    @PostMapping("/administradores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearAdmin(@Valid @RequestBody UsuarioDto dto) {
        Map<String, Object> resp = new HashMap<>();
        try {
            if (usuarioService.existsByNombreUsuario(dto.getNombreUsuario())) {
                resp.put("exito", false);
                resp.put("mensaje", "El nombre de usuario ya existe");
                return ResponseEntity.badRequest().body(resp);
            }
            if (usuarioService.existsByCorreoElectronico(dto.getCorreoElectronico())) {
                resp.put("exito", false);
                resp.put("mensaje", "El correo electrónico ya existe");
                return ResponseEntity.badRequest().body(resp);
            }

            Usuario creado = usuarioService.createAdminFromDto(dto);
            resp.put("exito", true);
            resp.put("mensaje", "Administrador creado exitosamente");
            resp.put("administrador", creado);
            return ResponseEntity.created(URI.create("/api/admin/administradores/" + creado.getIdUsuario())).body(resp);
        } catch (Exception e) {
            resp.put("exito", false);
            resp.put("mensaje", "Error al crear administrador: " + e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

    // Dashboard simple
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        List<Usuario> admins = usuarioService.findAllByRoleName("ADMIN");
        dashboard.put("totalAdministradores", admins.size());
        long activos = admins.stream().filter(u -> Boolean.TRUE.equals(u.getActivo())).count();        dashboard.put("administradoresActivos", activos);
        dashboard.put("mensaje", "Panel de administración accesible");
        return ResponseEntity.ok(dashboard);
    }
    

    @GetMapping("/privilegios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtenerPrivilegios() {
        Map<String, Object> p = new HashMap<>();
        p.put("nivel", 1);
        p.put("descripcion", "Administrador completo");
        p.put("permisos", List.of(
            "Gestionar usuarios",
            "Gestionar configuraciones del sistema",
            "Acceso completo a la base de datos",
            "Crear y modificar administradores",
            "Ver logs del sistema"
        ));
        return ResponseEntity.ok(p);
    }
    private Boolean activo;

    public Boolean getActivo() {
        return activo;
    }
}
