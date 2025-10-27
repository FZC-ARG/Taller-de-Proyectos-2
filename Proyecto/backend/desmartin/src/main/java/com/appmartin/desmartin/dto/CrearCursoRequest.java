package com.appmartin.desmartin.dto;

import lombok.Data;

@Data
public class CrearCursoRequest {
    private String nombreCurso;
    private String descripcion;
    private Integer idDocente;
}
