package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    Optional<Docente> findByNombreUsuario(String nombreUsuario);
}

