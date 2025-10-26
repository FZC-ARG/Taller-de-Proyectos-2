package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.ResultadoTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoTestRepository extends JpaRepository<ResultadoTest, Integer> {
    @Query("SELECT r FROM ResultadoTest r WHERE r.intentoTest.alumno.idAlumno = :idAlumno ORDER BY r.intentoTest.fechaRealizacion DESC")
    List<ResultadoTest> findByAlumnoId(@Param("idAlumno") Integer idAlumno);
    
    @Query("SELECT r FROM ResultadoTest r WHERE r.intentoTest.idIntento = :idIntento")
    List<ResultadoTest> findByIntentoId(@Param("idIntento") Integer idIntento);
}

