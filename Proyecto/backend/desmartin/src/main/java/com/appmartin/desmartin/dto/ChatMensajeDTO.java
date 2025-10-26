package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMensajeDTO {
    private Integer idMensaje;
    private Integer idSesion;
    private String emisor;
    private String contenido;
    private LocalDateTime fechaHoraEnvio;
}

