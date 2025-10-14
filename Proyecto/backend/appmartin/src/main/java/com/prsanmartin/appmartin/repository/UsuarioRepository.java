package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Añadir estas firmas para que Spring Data genere la implementación
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreoElectronico(String correoElectronico);

    // si no existe, también es útil:
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
}

