package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.RespuestaIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaIARepository extends JpaRepository<RespuestaIA, Integer> {
    
    List<RespuestaIA> findBySolicitudIdSolicitud(Integer idSolicitud);
    
    List<RespuestaIA> findByModeloIA(String modeloIA);
    
    @Query("SELECT r FROM RespuestaIA r WHERE r.solicitud.docente.usuario.nombreUsuario = :nombreUsuario")
    List<RespuestaIA> findBySolicitudDocenteUsuarioNombreUsuario(String nombreUsuario);
    
    @Query("SELECT r FROM RespuestaIA r WHERE r.solicitud.alumno.usuario.nombreUsuario = :nombreUsuario")
    List<RespuestaIA> findBySolicitudAlumnoUsuarioNombreUsuario(String nombreUsuario);
}
