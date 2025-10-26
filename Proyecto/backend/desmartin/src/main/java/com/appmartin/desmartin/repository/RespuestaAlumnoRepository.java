package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.RespuestaAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaAlumnoRepository extends JpaRepository<RespuestaAlumno, Integer> {
}

