package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.LoginRequest;
import com.prsanmartin.appmartin.dto.LoginResponse;
import com.prsanmartin.appmartin.entity.Administrador;
import com.prsanmartin.appmartin.repository.AdministradorRepository;
import com.prsanmartin.appmartin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse autenticarAdministrador(LoginRequest loginRequest) {
        try {
            // Buscar administrador por usuario
            Optional<Administrador> adminOpt = administradorRepository.findByUsuario(loginRequest.getUsuario());
            
            if (adminOpt.isEmpty()) {
                return new LoginResponse(null, null, null, null, null, null, null, null, 
                    "Usuario no encontrado", false);
            }

            Administrador admin = adminOpt.get();

            // Verificar si está activo
            if (!admin.getActivo()) {
                return new LoginResponse(null, null, null, null, null, null, null, null, 
                    "Cuenta de administrador desactivada", false);
            }

            // Verificar contraseña
            if (!passwordEncoder.matches(loginRequest.getContrasena(), admin.getContrasenaHash())) {
                return new LoginResponse(null, null, null, null, null, null, null, null, 
                    "Contraseña incorrecta", false);
            }

            // Generar token JWT
            String token = jwtUtil.generateToken(
                admin.getUsuario(), 
                admin.getIdAdministrador(), 
                admin.getNombreCompleto(), 
                admin.getNivelPrivilegio()
            );

            // Actualizar último acceso
            admin.setUltimoAcceso(LocalDateTime.now());
            administradorRepository.save(admin);

            return new LoginResponse(
                token,
                "Bearer",
                admin.getIdAdministrador(),
                admin.getNombreCompleto(),
                admin.getUsuario(),
                admin.getCorreoElectronico(),
                admin.getNivelPrivilegio(),
                admin.getUltimoAcceso(),
                "Autenticación exitosa",
                true
            );

        } catch (Exception e) {
            return new LoginResponse(null, null, null, null, null, null, null, null, 
                "Error interno del servidor: " + e.getMessage(), false);
        }
    }

    public List<Administrador> obtenerTodosLosAdministradores() {
        return administradorRepository.findByActivoTrue();
    }

    public Optional<Administrador> obtenerAdministradorPorId(Integer id) {
        return administradorRepository.findById(id);
    }

    public Administrador crearAdministrador(Administrador administrador) {
        // Encriptar contraseña
        administrador.setContrasenaHash(passwordEncoder.encode(administrador.getContrasenaHash()));
        administrador.setActivo(true);
        administrador.setNivelPrivilegio(1); // Administrador completo
        return administradorRepository.save(administrador);
    }

    public boolean existeUsuario(String usuario) {
        return administradorRepository.existsByUsuario(usuario);
    }

    public boolean existeCorreo(String correo) {
        return administradorRepository.existsByCorreoElectronico(correo);
    }
}
