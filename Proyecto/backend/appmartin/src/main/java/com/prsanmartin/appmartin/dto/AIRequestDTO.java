package com.prsanmartin.appmartin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequestDTO {
    
    @NotNull(message = "ID del docente es obligatorio")
    private Integer idDocente;
    
    @NotNull(message = "ID del curso es obligatorio")
    private Integer idCurso;
    
    private Integer idAlumno; // Optional - for student-specific requests
    
    @NotBlank(message = "Pregunta es obligatoria")
    private String pregunta;
    
    private String contexto; // Additional context for the AI
    
    private String tipoConsulta; // Type of query: "academica", "recomendacion", "evaluacion", etc.
    
    private Map<String, Object> datosAdicionales; // Additional data like test results, grades, etc.
    
    private LocalDateTime timestamp = LocalDateTime.now();
}
