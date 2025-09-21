package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    
    List<Matricula> findByAlumnoIdAlumno(Integer idAlumno);
    
    List<Matricula> findByCursoIdCurso(Integer idCurso);
    
    @Query("SELECT m FROM Matricula m WHERE m.alumno.usuario.nombreUsuario = :nombreUsuario")
    List<Matricula> findByAlumnoUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT m FROM Matricula m WHERE m.curso.docente.usuario.nombreUsuario = :nombreUsuario")
    List<Matricula> findByCursoDocenteUsuarioNombreUsuario(String nombreUsuario);
    
    Optional<Matricula> findByAlumnoIdAlumnoAndCursoIdCurso(Integer idAlumno, Integer idCurso);
    
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.curso.idCurso = :idCurso")
    Long countByCursoIdCurso(Integer idCurso);
}
