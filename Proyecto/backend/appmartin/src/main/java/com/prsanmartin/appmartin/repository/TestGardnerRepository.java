package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.TestGardner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // New methods for autosave functionality
    Optional<TestGardner> findByClientRequestId(String clientRequestId);
    
    List<TestGardner> findByAlumnoIdAlumnoAndEstadoGuardado(Integer idAlumno, TestGardner.EstadoGuardado estado);
    
    @Query("SELECT t FROM TestGardner t WHERE t.alumno.idAlumno = :idAlumno AND t.estadoGuardado IN ('FINAL', 'CALCULADO') ORDER BY t.fechaAplicacion DESC")
    List<TestGardner> findByAlumnoCompletedTests(@Param("idAlumno") Integer idAlumno);
    
    @Query("SELECT t FROM TestGardner t WHERE t.alumno.idAlumno = :idAlumno ORDER BY t.versionGuardado DESC")
    Optional<TestGardner> findLatestVersionByAlumno(@Param("idAlumno") Integer idAlumno);
    
    // Methods for access control and security
    @Query("SELECT t FROM TestGardner t WHERE t.alumno.usuario.idUsuario = :userId")
    List<TestGardner> findByUsuarioId(@Param("userId") Integer userId);
    
    @Query("SELECT t FROM TestGardner t WHERE t.alumno.idAlumno = :idAlumno AND t.idTest = :testId")
    Optional<TestGardner> findByAlumnoAndTestId(@Param("idAlumno") Integer idAlumno, @Param("testId") Integer testId);
}
