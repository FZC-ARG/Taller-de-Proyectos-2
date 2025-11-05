package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.ChatMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMensajeRepository extends JpaRepository<ChatMensaje, Integer> {
    List<ChatMensaje> findByChatSesion_IdSesionOrderByFechaHoraEnvioAsc(Integer idSesion);
    
    /**
     * Elimina mensajes anteriores a la fecha límite
     * @param fechaLimite Fecha límite (mensajes anteriores a esta fecha serán eliminados)
     * @return Número de mensajes eliminados
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMensaje m WHERE m.fechaHoraEnvio < :fechaLimite")
    int deleteByFechaHoraEnvioBefore(@Param("fechaLimite") LocalDateTime fechaLimite);
}

