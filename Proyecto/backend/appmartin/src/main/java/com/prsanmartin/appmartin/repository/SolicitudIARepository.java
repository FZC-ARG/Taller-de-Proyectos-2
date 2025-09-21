package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.SolicitudIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudIARepository extends JpaRepository<SolicitudIA, Integer> {
    
    List<SolicitudIA> findByDocenteIdDocente(Integer idDocente);
    
    List<SolicitudIA> findByCursoIdCurso(Integer idCurso);
    
    List<SolicitudIA> findByAlumnoIdAlumno(Integer idAlumno);
    
    List<SolicitudIA> findByEstado(String estado);
    
    @Query("SELECT s FROM SolicitudIA s WHERE s.docente.usuario.nombreUsuario = :nombreUsuario")
    List<SolicitudIA> findByDocenteUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT s FROM SolicitudIA s WHERE s.alumno.usuario.nombreUsuario = :nombreUsuario")
    List<SolicitudIA> findByAlumnoUsuarioNombreUsuario(String nombreUsuario);
}
