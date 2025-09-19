package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.entity.Administrador;
import com.prsanmartin.appmartin.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping("/administradores")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Administrador>> obtenerTodosLosAdministradores() {
        List<Administrador> administradores = administradorService.obtenerTodosLosAdministradores();
        return ResponseEntity.ok(administradores);
    }

    @GetMapping("/administradores/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Administrador> obtenerAdministradorPorId(@PathVariable Integer id) {
        Optional<Administrador> admin = administradorService.obtenerAdministradorPorId(id);
        return admin.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/administradores")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> crearAdministrador(@RequestBody Administrador administrador) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar si el usuario ya existe
            if (administradorService.existeUsuario(administrador.getUsuario())) {
                response.put("exito", false);
                response.put("mensaje", "El nombre de usuario ya existe");
                return ResponseEntity.badRequest().body(response);
            }

            // Verificar si el correo ya existe
            if (administradorService.existeCorreo(administrador.getCorreoElectronico())) {
                response.put("exito", false);
                response.put("mensaje", "El correo electrónico ya existe");
                return ResponseEntity.badRequest().body(response);
            }

            Administrador nuevoAdmin = administradorService.crearAdministrador(administrador);
            
            response.put("exito", true);
            response.put("mensaje", "Administrador creado exitosamente");
            response.put("administrador", nuevoAdmin);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al crear administrador: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        try {
            List<Administrador> administradores = administradorService.obtenerTodosLosAdministradores();
            
            dashboard.put("totalAdministradores", administradores.size());
            dashboard.put("administradoresActivos", administradores.stream()
                .filter(Administrador::getActivo)
                .count());
            dashboard.put("mensaje", "Panel de administración accesible");
            dashboard.put("privilegios", "Administrador completo");
            
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            dashboard.put("error", "Error al acceder al dashboard: " + e.getMessage());
            return ResponseEntity.status(500).body(dashboard);
        }
    }

    @GetMapping("/privilegios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, Object>> obtenerPrivilegios() {
        Map<String, Object> privilegios = new HashMap<>();
        
        privilegios.put("nivel", 1);
        privilegios.put("descripcion", "Administrador completo");
        privilegios.put("permisos", List.of(
            "Gestionar usuarios",
            "Gestionar configuraciones del sistema",
            "Acceso completo a la base de datos",
            "Crear y modificar administradores",
            "Ver logs del sistema"
        ));
        
        return ResponseEntity.ok(privilegios);
    }
}
