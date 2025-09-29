package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.PreguntaGardner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaGardnerRepository extends JpaRepository<PreguntaGardner, Integer> {
    
    /**
     * Find all active questions ordered by sequence
     */
    List<PreguntaGardner> findByActivoTrueOrderByOrdenSecuencia();
    
    /**
     * Find questions by intelligence type, paginated
     */
    Page<PreguntaGardner> findByTipoInteligenciaAndActivoTrue(PreguntaGardner.TipoInteligencia tipoInteligencia, Pageable pageable);
    
    /**
     * Find questions by intelligence type, ordered by sequence
     */
    List<PreguntaGardner> findByTipoInteligenciaAndActivoTrueOrderByOrdenSecuencia(PreguntaGardner.TipoInteligencia tipoInteligencia);
    
    /**
     * Get random questions by intelligence type for testing
     */
    @Query("SELECT p FROM PreguntaGardner p WHERE p.tipoInteligencia = :tipo AND p.activo = true")
    List<PreguntaGardner> findByTipoInteligenciaRandom(@Param("tipo") PreguntaGardner.TipoInteligencia tipo, Pageable pageable);
    
    /**
     * Count questions by intelligence type
     */
    Long countByTipoInteligenciaAndActivoTrue(PreguntaGardner.TipoInteligencia tipoInteligencia);
    
    /**
     * Find question by ID and ensure it's active
     */
    PreguntaGardner findByIdPreguntaAndActivoTrue(Integer idPregunta);
    
    /**
     * Get all intelligence types with question counts
     */
    @Query("SELECT p.tipoInteligencia, COUNT(p) FROM PreguntaGardner p WHERE p.activo = true GROUP BY p.tipoInteligencia")
    List<Object[]> countQuestionsByTipoInteligencia();
}
