package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.UsuarioDto;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import com.prsanmartin.appmartin.repository.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    @Override
    public boolean existsByNombreUsuario(String nombreUsuario) {
        // si tu repo tiene existsByNombreUsuario usarlo, otherwise fallback:
        try {
            return usuarioRepository.existsByNombreUsuario(nombreUsuario);
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean existsByCorreoElectronico(String correoElectronico) {
        try {
            return usuarioRepository.existsByCorreoElectronico(correoElectronico);
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Usuario createAdminFromDto(UsuarioDto dto) {
        if (dto == null) return null;
        Usuario u = new Usuario();
        u.setNombreUsuario(dto.getNombreUsuario());
        u.setCorreoElectronico(dto.getCorreoElectronico());
        u.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        Rol admin = rolRepository.findByNombreRol("ADMIN");
        if (admin != null) {
            u.setRol(admin);
        }
        u.setActivo(true);
        return usuarioRepository.save(u);
    }

    @Override
    public List<Usuario> findAllByRoleName(String roleName) {
        return usuarioRepository.findAllByRoleName(roleName);
    }

    @Override
    public Optional<Usuario> findByIdIfRoleName(Integer id, String roleName) {
        if (id == null) return Optional.empty();
        Optional<Usuario> u = usuarioRepository.findById(id);
        if (u.isPresent() && u.get().getRol() != null && roleName.equalsIgnoreCase(u.get().getRol().getNombreRol())) {
            return u;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        if (userId == null || newPassword == null) return;
        Integer id = userId.intValue();
        usuarioRepository.findById(id).ifPresent(u -> {
            // Ajusta el nombre del setter de contraseña según tu entidad
            u.setContrasenaHash(passwordEncoder.encode(newPassword));
            usuarioRepository.save(u);
        });
    }

    @Override
    public void updateTeacherCredentials(Long teacherId, Object teacherUpdateRequest) {
        if (teacherId == null) return;
        Integer id = teacherId.intValue();
        usuarioRepository.findById(id).ifPresent(u -> {
            if (teacherUpdateRequest instanceof com.prsanmartin.appmartin.dto.TeacherUpdateRequest req) {
                if (req.getUsername() != null && !req.getUsername().isBlank()) {
                    u.setNombreUsuario(req.getUsername());
                }
                if (req.getEmail() != null && !req.getEmail().isBlank()) {
                    u.setCorreoElectronico(req.getEmail());
                }
            }
            usuarioRepository.save(u);
        });
    }
}