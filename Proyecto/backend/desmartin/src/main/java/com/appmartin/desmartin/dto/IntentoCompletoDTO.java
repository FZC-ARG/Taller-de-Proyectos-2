package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntentoCompletoDTO {
    private Integer idIntento;
    private Integer numeroIntento;
    private LocalDateTime fechaRealizacion;
    private List<ResultadoIntentoDTO> resultados;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultadoIntentoDTO {
        private Integer idInteligencia;
        private String nombreInteligencia;
        private Float puntajeCalculado;
    }
}

