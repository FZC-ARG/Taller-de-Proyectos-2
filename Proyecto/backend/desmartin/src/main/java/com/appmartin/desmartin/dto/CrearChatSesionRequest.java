package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearChatSesionRequest {
    private Integer idDocente;
    private Integer idAlumno;
    private String tituloSesion;
}

