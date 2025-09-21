package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {
    
    List<Auditoria> findByUsuarioIdUsuario(Integer idUsuario);
    
    List<Auditoria> findByEntidad(String entidad);
    
    List<Auditoria> findByAccion(String accion);
    
    @Query("SELECT a FROM Auditoria a WHERE a.fechaEvento BETWEEN :fechaInicio AND :fechaFin")
    List<Auditoria> findByFechaEventoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT a FROM Auditoria a WHERE a.usuario.nombreUsuario = :nombreUsuario")
    List<Auditoria> findByUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT a FROM Auditoria a WHERE a.direccionIP = :direccionIP")
    List<Auditoria> findByDireccionIP(String direccionIP);
}
