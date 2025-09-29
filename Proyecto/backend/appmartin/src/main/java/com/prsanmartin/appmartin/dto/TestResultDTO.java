package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDTO {
    
    private Integer idTest;
    private Integer idAlumno;
    private Map<String, Integer> puntajesBrutos;
    private Map<String, Integer> puntajesPonderados;
    private String inteligenciaPredominante;
    private String inteligenciaSecundaria;
    private Double puntajeTotal;
    private LocalDateTime fechaCompletado;
    private Integer version;
    private String estado;
    
    // Descriptive analysis
    private String descripcionInteligencia;
    private String recomendacionesAcademicas;
    private String fortalezasIdentificadas;
    private String areasDesarrollo;
    
    public static TestResultDTO fromTestGardner(com.prsanmartin.appmartin.entity.TestGardner test) {
        TestResultDTO result = new TestResultDTO();
        result.setIdTest(test.getIdTest());
        result.setIdAlumno(test.getAlumno().getIdAlumno());
        result.setInteligenciaPredominante(test.getInteligenciaPredominante());
        result.setPuntajeTotal(test.getPuntajeTotal());
        result.setFechaCompletado(test.getFechaAplicacion());
        result.setVersion(test.getVersionGuardado());
        result.setEstado(test.getEstadoGuardado().name());
        return result;
    }
}
