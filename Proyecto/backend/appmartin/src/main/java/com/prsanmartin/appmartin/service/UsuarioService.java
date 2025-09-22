package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.UsuarioDto;
import com.prsanmartin.appmartin.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreoElectronico(String correoElectronico);
    Usuario createAdminFromDto(UsuarioDto dto);
    List<Usuario> findAllByRoleName(String roleName);
    Optional<Usuario> findByIdIfRoleName(Integer id, String roleName);
    Optional<Usuario> findById(Integer id);
}