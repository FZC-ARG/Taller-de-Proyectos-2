package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    
    List<Calificacion> findByMatriculaIdMatricula(Integer idMatricula);
    
    @Query("SELECT c FROM Calificacion c WHERE c.matricula.alumno.usuario.nombreUsuario = :nombreUsuario")
    List<Calificacion> findByMatriculaAlumnoUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT c FROM Calificacion c WHERE c.matricula.curso.docente.usuario.nombreUsuario = :nombreUsuario")
    List<Calificacion> findByMatriculaCursoDocenteUsuarioNombreUsuario(String nombreUsuario);
    
    List<Calificacion> findByPeriodo(String periodo);
    
    @Query("SELECT AVG(c.nota) FROM Calificacion c WHERE c.matricula.alumno.idAlumno = :idAlumno")
    Double findPromedioByAlumnoIdAlumno(Integer idAlumno);
}
