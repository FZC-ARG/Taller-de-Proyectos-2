package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaDTO {
    private Integer idPregunta;
    private Integer idInteligencia;
    private String nombreInteligencia;
    private String textoPregunta;
}

