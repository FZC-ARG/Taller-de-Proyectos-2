package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    
    Optional<Docente> findByUsuarioIdUsuario(Integer idUsuario);
    
    @Query("SELECT d FROM Docente d WHERE d.usuario.correoElectronico = :email")
    Optional<Docente> findByUsuarioCorreoElectronico(String email);
    
    List<Docente> findByEspecialidad(String especialidad);
    
    @Query("SELECT d FROM Docente d WHERE d.usuario.nombreUsuario = :nombreUsuario")
    Optional<Docente> findByUsuarioNombreUsuario(String nombreUsuario);
}
