package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.ChatMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMensajeRepository extends JpaRepository<ChatMensaje, Integer> {
    List<ChatMensaje> findByChatSesion_IdSesionOrderByFechaHoraEnvioAsc(Integer idSesion);
}

