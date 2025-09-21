package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.TestGardner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestGardnerRepository extends JpaRepository<TestGardner, Integer> {
    
    List<TestGardner> findByAlumnoIdAlumno(Integer idAlumno);
    
    @Query("SELECT t FROM TestGardner t WHERE t.alumno.usuario.nombreUsuario = :nombreUsuario")
    List<TestGardner> findByAlumnoUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT t FROM TestGardner t WHERE t.alumno.usuario.correoElectronico = :email")
    List<TestGardner> findByAlumnoUsuarioCorreoElectronico(String email);
    
    Optional<TestGardner> findFirstByAlumnoIdAlumnoOrderByFechaAplicacionDesc(Integer idAlumno);
}
