package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.ChatSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSesionRepository extends JpaRepository<ChatSesion, Integer> {
    List<ChatSesion> findByDocente_IdDocente(Integer idDocente);
}

