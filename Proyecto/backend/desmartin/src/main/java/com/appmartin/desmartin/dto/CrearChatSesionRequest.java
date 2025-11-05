package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearChatSesionRequest {
    private Integer idDocente;
    private Integer idAlumno;  // NULL para chats grupales
    private Integer idCurso;   // NULL para chats individuales
    private String tituloSesion;
}

