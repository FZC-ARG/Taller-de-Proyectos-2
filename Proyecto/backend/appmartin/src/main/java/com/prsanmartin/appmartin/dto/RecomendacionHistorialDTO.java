package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionHistorialDTO {
    
    private Integer idHistorial;
    private Integer idAlumno;
    private String nombreAlumno;
    private Integer idTest;
    private LocalDateTime fechaTest;
    private String inteligenciaPredominante;
    private Integer puntajeInteligencia;
    private String recomendacionGenerada;
    private String tipoRecomendacion;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaAplicacion;
    private String estado;
    private String notas;
    private String plantillaUsada;
    
    // Additional fields for teacher view
    private String cursoNombre;
    private String docenteNombre;
}
