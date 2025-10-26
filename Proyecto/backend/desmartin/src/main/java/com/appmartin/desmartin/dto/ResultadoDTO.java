package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDTO {
    private Integer idResultado;
    private Integer idIntento;
    private LocalDateTime fechaRealizacion;
    private Integer idInteligencia;
    private String nombreInteligencia;
    private Float puntajeCalculado;
}

