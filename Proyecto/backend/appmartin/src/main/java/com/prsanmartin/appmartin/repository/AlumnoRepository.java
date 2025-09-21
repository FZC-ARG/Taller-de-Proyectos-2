package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    
    Optional<Alumno> findByUsuarioIdUsuario(Integer idUsuario);
    
    @Query("SELECT a FROM Alumno a WHERE a.usuario.correoElectronico = :email")
    Optional<Alumno> findByUsuarioCorreoElectronico(String email);
    
    List<Alumno> findByAnioIngreso(Integer anioIngreso);
    
    @Query("SELECT a FROM Alumno a WHERE a.usuario.nombreUsuario = :nombreUsuario")
    Optional<Alumno> findByUsuarioNombreUsuario(String nombreUsuario);
}
