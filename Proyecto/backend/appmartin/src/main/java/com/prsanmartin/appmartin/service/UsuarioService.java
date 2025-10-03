package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.UsuarioDto;
import com.prsanmartin.appmartin.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreoElectronico(String correoElectronico);
    Usuario createAdminFromDto(UsuarioDto dto);
    List<Usuario> findAllByRoleName(String roleName);
    Optional<Usuario> findByIdIfRoleName(Integer id, String roleName);
    Optional<Usuario> findById(Integer id);

    // Métodos para gestionar usuarios
    public void resetPassword(Long userId, String newPassword) {
        // Lógica para restablecer la contraseña
    }

    public void updateTeacherCredentials(Long teacherId, TeacherUpdateRequest request) {
        // Lógica para actualizar credenciales del docente
    }
}