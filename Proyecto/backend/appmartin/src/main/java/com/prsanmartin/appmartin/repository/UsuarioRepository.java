package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Añadir estas firmas para que Spring Data genere la implementación
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByCorreoElectronico(String correoElectronico);

    // si no existe, también es útil:
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    @Query("SELECT u FROM Usuario u WHERE u.rol.nombreRol = :roleName")
    List<Usuario> findAllByRoleName(String roleName);

    // Método para crear roles usando SQL nativo
    @Modifying
    @Query(value = "INSERT INTO roles (NombreRol) VALUES (:nombreRol) ON DUPLICATE KEY UPDATE NombreRol = NombreRol", nativeQuery = true)
    void createRoleIfNotExists(@Param("nombreRol") String nombreRol);
}

