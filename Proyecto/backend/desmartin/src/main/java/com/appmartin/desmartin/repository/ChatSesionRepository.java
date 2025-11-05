package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.ChatSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    
    /**
     * Obtiene la última sesión activa entre un docente y un alumno
     * Ordenada por fecha de creación descendente (más reciente primero)
     * 
     * @param idDocente ID del docente
     * @param idAlumno ID del alumno
     * @return La última sesión activa o null si no existe
     */
    @Query("SELECT s FROM ChatSesion s WHERE s.docente.idDocente = :idDocente AND s.alumno.idAlumno = :idAlumno AND s.curso IS NULL ORDER BY s.fechaCreacion DESC")
    List<ChatSesion> findUltimaSesionActivaPorDocenteYAlumno(@Param("idDocente") Integer idDocente, @Param("idAlumno") Integer idAlumno);
    
    // Consultas combinadas: docente y curso
    List<ChatSesion> findByDocente_IdDocenteAndCurso_IdCurso(Integer idDocente, Integer idCurso);
    
    /**
     * Elimina sesiones anteriores a la fecha límite
     * Nota: Los mensajes asociados se eliminarán automáticamente por CASCADE
     * @param fechaLimite Fecha límite (sesiones anteriores a esta fecha serán eliminadas)
     * @return Número de sesiones eliminadas
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatSesion s WHERE s.fechaCreacion < :fechaLimite")
    int deleteByFechaCreacionBefore(@Param("fechaLimite") LocalDateTime fechaLimite);
}

