package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.IntentoTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IntentoTestRepository extends JpaRepository<IntentoTest, Integer> {
    List<IntentoTest> findByAlumno_IdAlumnoOrderByFechaRealizacionDesc(Integer idAlumno);
    Optional<IntentoTest> findFirstByAlumno_IdAlumnoOrderByFechaRealizacionDesc(Integer idAlumno);
}

