package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // Additional methods for student management
    @Query("SELECT a FROM Alumno a WHERE a.usuario.nombreUsuario LIKE %:nombre%")
    List<Alumno> findByUsuarioNombreUsuarioContainingIgnoreCase(@Param("nombre") String nombre);
    
    @Query("SELECT a FROM Alumno a WHERE a.usuario.activo = true")
    List<Alumno> findActiveStudents();
    
    @Query("SELECT COUNT(a) FROM Alumno a WHERE a.usuario.activo = true")
    long countActiveStudents();
    
    @Query("SELECT a.anioIngreso, COUNT(a) FROM Alumno a GROUP BY a.anioIngreso")
    List<Object[]> countStudentsByYear();
    
    // Methods for filtering by teacher and course
    @Query("SELECT DISTINCT a FROM Alumno a " +
           "JOIN Matricula m ON a.idAlumno = m.alumno.idAlumno " +
           "JOIN Curso c ON m.curso.idCurso = c.idCurso " +
           "WHERE c.docente.idDocente = :docenteId AND c.idCurso = :cursoId")
    List<Alumno> findByTeacherAndCourse(@Param("docenteId") Integer docenteId, @Param("cursoId") Integer cursoId);
    
    @Query("SELECT DISTINCT a FROM Alumno a " +
           "JOIN Matricula m ON a.idAlumno = m.alumno.idAlumno " +
           "JOIN Curso c ON m.curso.idCurso = c.idCurso " +
           "WHERE c.idCurso = :cursoId")
    List<Alumno> findByCourseId(@Param("cursoId") Integer cursoId);
    
    @Query("SELECT DISTINCT a FROM Alumno a " +
           "JOIN Matricula m ON a.idAlumno = m.alumno.idAlumno " +
           "JOIN Curso c ON m.curso.idCurso = c.idCurso " +
           "WHERE c.docente.idDocente = :docenteId")
    List<Alumno> findByTeacherId(@Param("docenteId") Integer docenteId);
}
