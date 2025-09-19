package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    
    Optional<Administrador> findByUsuario(String usuario);
    
    Optional<Administrador> findByCorreoElectronico(String correoElectronico);
    
    List<Administrador> findByActivoTrue();
    
    @Query("SELECT a FROM Administrador a WHERE a.activo = true AND a.nivelPrivilegio >= :nivel")
    List<Administrador> findByActivoTrueAndNivelPrivilegioGreaterThanEqual(Integer nivel);
    
    boolean existsByUsuario(String usuario);
    
    boolean existsByCorreoElectronico(String correoElectronico);
}
