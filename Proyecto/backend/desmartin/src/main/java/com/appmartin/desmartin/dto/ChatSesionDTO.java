package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSesionDTO {
    private Integer idSesion;
    private Integer idDocente;
    private Integer idAlumno;
    private Integer idCurso;
    private String tituloSesion;
    private LocalDateTime fechaCreacion;
}

