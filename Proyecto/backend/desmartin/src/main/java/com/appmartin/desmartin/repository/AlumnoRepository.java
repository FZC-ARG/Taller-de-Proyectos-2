package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    Optional<Alumno> findByNombreUsuario(String nombreUsuario);
}

