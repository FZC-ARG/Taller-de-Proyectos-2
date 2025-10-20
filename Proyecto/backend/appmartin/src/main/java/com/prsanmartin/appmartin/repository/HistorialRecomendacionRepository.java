package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.HistorialRecomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialRecomendacionRepository extends JpaRepository<HistorialRecomendacion, Integer> {
    
    List<HistorialRecomendacion> findByAlumnoIdAlumno(Integer idAlumno);
    
    List<HistorialRecomendacion> findByTestIdTest(Integer idTest);
    
    List<HistorialRecomendacion> findByEstado(HistorialRecomendacion.EstadoRecomendacion estado);
    
    List<HistorialRecomendacion> findByTipoRecomendacion(HistorialRecomendacion.TipoRecomendacion tipoRecomendacion);
    
    @Query("SELECT h FROM HistorialRecomendacion h WHERE h.alumno.idAlumno = :idAlumno ORDER BY h.fechaGeneracion DESC")
    List<HistorialRecomendacion> findByAlumnoOrderByFechaGeneracionDesc(@Param("idAlumno") Integer idAlumno);
    
    @Query("SELECT h FROM HistorialRecomendacion h WHERE h.alumno.idAlumno = :idAlumno AND h.estado = :estado")
    List<HistorialRecomendacion> findByAlumnoAndEstado(@Param("idAlumno") Integer idAlumno, 
                                                      @Param("estado") HistorialRecomendacion.EstadoRecomendacion estado);
    
    @Query("SELECT h FROM HistorialRecomendacion h WHERE h.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin")
    List<HistorialRecomendacion> findByFechaGeneracionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                              @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT h.inteligenciaPredominante, COUNT(h) FROM HistorialRecomendacion h GROUP BY h.inteligenciaPredominante")
    List<Object[]> countByInteligenciaPredominante();
    
    @Query("SELECT h.tipoRecomendacion, COUNT(h) FROM HistorialRecomendacion h GROUP BY h.tipoRecomendacion")
    List<Object[]> countByTipoRecomendacion();
    
    @Query("SELECT h.estado, COUNT(h) FROM HistorialRecomendacion h GROUP BY h.estado")
    List<Object[]> countByEstado();
}
