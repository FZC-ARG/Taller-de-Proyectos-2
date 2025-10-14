// UsuarioServiceImpl.java (implementación)
package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.UsuarioDto;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
        // implementar según tu DTO
        return null;
    }

    @Override
    public List<Usuario> findAllByRoleName(String roleName) {
        // implementar si tu repo tiene findAllByRolNombre or similar
        return null;
    }

    @Override
    public Optional<Usuario> findByIdIfRoleName(Integer id, String roleName) {
        // implementación según tu lógica
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
            // TODO: castear teacherUpdateRequest al DTO real y aplicar cambios sobre 'u'
            usuarioRepository.save(u);
        });
    }
}