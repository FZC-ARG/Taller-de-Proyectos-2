package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.ChatSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSesionRepository extends JpaRepository<ChatSesion, Integer> {
    // Consultas por docente
    List<ChatSesion> findByDocente_IdDocente(Integer idDocente);
    
    // Consultas por alumno (chats individuales)
    List<ChatSesion> findByAlumno_IdAlumno(Integer idAlumno);
    
    // Consultas por curso (chats grupales)
    List<ChatSesion> findByCurso_IdCurso(Integer idCurso);
    
    // Consultas combinadas: docente y alumno
    List<ChatSesion> findByDocente_IdDocenteAndAlumno_IdAlumno(Integer idDocente, Integer idAlumno);
    
    // Consultas combinadas: docente y curso
    List<ChatSesion> findByDocente_IdDocenteAndCurso_IdCurso(Integer idDocente, Integer idCurso);
}

