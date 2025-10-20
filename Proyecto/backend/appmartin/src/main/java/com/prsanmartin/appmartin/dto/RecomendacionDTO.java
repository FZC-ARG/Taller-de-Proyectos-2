package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionDTO {
    
    private Integer idHistorial;
    private Integer idAlumno;
    private String nombreAlumno;
    private Integer idTest;
    private String inteligenciaPredominante;
    private Integer puntajeInteligencia;
    private String recomendacion;
    private String tipoRecomendacion;
    private LocalDateTime fechaGeneracion;
    private String estado;
    private String notas;
    
    // Additional fields for display
    private String descripcionInteligencia;
    private String plantillaUsada;
    private Double confianza;
}
