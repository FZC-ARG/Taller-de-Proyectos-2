package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    
    List<Curso> findByDocenteIdDocente(Integer idDocente);
    
    @Query("SELECT c FROM Curso c WHERE c.docente.usuario.nombreUsuario = :nombreUsuario")
    List<Curso> findByDocenteUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT c FROM Curso c WHERE c.nombreCurso LIKE %:nombre%")
    List<Curso> findByNombreCursoContaining(String nombre);
    
    @Query("SELECT c FROM Curso c WHERE c.docente.especialidad = :especialidad")
    List<Curso> findByDocenteEspecialidad(String especialidad);
}
