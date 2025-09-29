package com.prsanmartin.appmartin.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutosaveRequestDTO {
    
    private String clientRequestId;
    
    @NotNull(message = "ID de alumno es obligatorio")
    private Integer idAlumno;
    
    @NotEmpty(message = "Respuestas son obligatorias")
    private List<RespuestaTestDTO> respuestas;
    
    private String estado; // BORRADOR o FINAL
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RespuestaTestDTO {
        
        @NotNull(message = "ID de pregunta es obligatorio")
        private Integer idPregunta;
        
        @NotNull(message = "Opción seleccionada es obligatoria")
        private Integer opcionSeleccionada; // 1=A, 2=B, 3=C, 4=D
        
        private String textoAdicional; // Campo opcional para comentarios
    }
}
