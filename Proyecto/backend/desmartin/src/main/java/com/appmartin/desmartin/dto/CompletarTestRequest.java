package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletarTestRequest {
    private Integer idAlumno;
    private List<RespuestaRequest> respuestas;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RespuestaRequest {
        private Integer idPregunta;
        private Integer puntaje;
    }
}

