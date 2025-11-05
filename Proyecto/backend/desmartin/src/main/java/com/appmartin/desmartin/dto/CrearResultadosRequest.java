package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearResultadosRequest {
    private Integer idAlumno; // opcional para validar pertenencia del intento
    private Integer idIntento;
    private List<ResultadoRequest> resultados;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultadoRequest {
        private Integer idInteligencia;
        private Float puntajeCalculado;
    }
}

