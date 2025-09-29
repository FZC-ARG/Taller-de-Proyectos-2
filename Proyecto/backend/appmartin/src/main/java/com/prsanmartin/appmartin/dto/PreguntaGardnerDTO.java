package com.prsanmartin.appmartin.dto;

import com.prsanmartin.appmartin.entity.PreguntaGardner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaGardnerDTO {
    
    private Integer idPregunta;
    private String textoPregunta;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private PreguntaGardner.TipoInteligencia tipoInteligencia;
    private Integer ordenSecuencia;
    private Boolean activo;
    
    // Constructor from entity
    public PreguntaGardnerDTO(PreguntaGardner pregunta) {
        this.idPregunta = pregunta.getIdPregunta();
        this.textoPregunta = pregunta.getTextoPregunta();
        this.opcionA = pregunta.getOpcionA();
        this.opcionB = pregunta.getOpcionB();
        this.opcionC = pregunta.getOpcionC();
        this.opcionD = pregunta.getOpcionD();
        this.tipoInteligencia = pregunta.getTipoInteligencia();
        this.ordenSecuencia = pregunta.getOrdenSecuencia();
        this.activo = pregunta.getActivo();
    }
}
