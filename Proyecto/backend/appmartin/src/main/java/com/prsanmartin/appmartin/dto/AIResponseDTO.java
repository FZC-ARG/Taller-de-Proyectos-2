package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIResponseDTO {
    
    private Integer idSolicitud;
    private Integer idRespuesta;
    private String respuesta;
    private String modeloIA;
    private Double confianza;
    private String estado;
    private LocalDateTime fechaRespuesta;
    private Map<String, Object> metadata;
    private String error;
    
    public static AIResponseDTO success(Integer idSolicitud, Integer idRespuesta, String respuesta, 
                                       String modeloIA, Double confianza) {
        AIResponseDTO response = new AIResponseDTO();
        response.setIdSolicitud(idSolicitud);
        response.setIdRespuesta(idRespuesta);
        response.setRespuesta(respuesta);
        response.setModeloIA(modeloIA);
        response.setConfianza(confianza);
        response.setEstado("COMPLETADA");
        response.setFechaRespuesta(LocalDateTime.now());
        return response;
    }
    
    public static AIResponseDTO error(String error) {
        AIResponseDTO response = new AIResponseDTO();
        response.setError(error);
        response.setEstado("ERROR");
        response.setFechaRespuesta(LocalDateTime.now());
        return response;
    }
}
