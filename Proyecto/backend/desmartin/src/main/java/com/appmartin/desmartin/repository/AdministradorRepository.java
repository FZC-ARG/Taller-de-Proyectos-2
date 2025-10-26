package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByNombreUsuario(String nombreUsuario);
}

