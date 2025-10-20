package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.PlantillaRecomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantillaRecomendacionRepository extends JpaRepository<PlantillaRecomendacion, Integer> {
    
    List<PlantillaRecomendacion> findByTipoInteligenciaAndActivoTrue(PlantillaRecomendacion.TipoInteligencia tipoInteligencia);
    
    List<PlantillaRecomendacion> findByTipoRecomendacionAndActivoTrue(PlantillaRecomendacion.TipoRecomendacion tipoRecomendacion);
    
    @Query("SELECT p FROM PlantillaRecomendacion p WHERE p.tipoInteligencia = :tipoInteligencia " +
           "AND p.puntajeMinimo <= :puntaje AND p.puntajeMaximo >= :puntaje AND p.activo = true")
    List<PlantillaRecomendacion> findByTipoInteligenciaAndPuntajeRange(
            @Param("tipoInteligencia") PlantillaRecomendacion.TipoInteligencia tipoInteligencia,
            @Param("puntaje") Integer puntaje);
    
    List<PlantillaRecomendacion> findByActivoTrue();
    
    @Query("SELECT p FROM PlantillaRecomendacion p WHERE p.activo = true ORDER BY p.tipoInteligencia, p.puntajeMinimo")
    List<PlantillaRecomendacion> findActiveTemplatesOrdered();
}
