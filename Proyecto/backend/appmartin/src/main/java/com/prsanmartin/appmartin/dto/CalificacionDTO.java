package com.prsanmartin.appmartin.dto;

import java.time.LocalDateTime;

public class CalificacionDTO {
    private Long idCalificacion;
    private Long idAlumno;
    private Long idCurso;
    private Double calificacion;
    private String tipoEvaluacion;
    private LocalDateTime fechaEvaluacion;
    
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructors
    public CalificacionDTO() {}
    
    public CalificacionDTO(Long idCalificacion, Long idAlumno, Long idCurso, Double calificacion, 
                          String tipoEvaluacion, LocalDateTime fechaEvaluacion, String observaciones, 
                          LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idCalificacion = idCalificacion;
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.calificacion = calificacion;
        this.tipoEvaluacion = tipoEvaluacion;
        this.fechaEvaluacion = fechaEvaluacion;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters and Setters
    public Long getIdCalificacion() {
        return idCalificacion;
    }
    
    public void setIdCalificacion(Long idCalificacion) {
        this.idCalificacion = idCalificacion;
    }
    
    public Long getIdAlumno() {
        return idAlumno;
    }
    
    public void setIdAlumno(Long idAlumno) {
        this.idAlumno = idAlumno;
    }
    
    public Long getIdCurso() {
        return idCurso;
    }
    
    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }
    
    public Double getCalificacion() {
        return calificacion;
    }
    
    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }
    
    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }
    
    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }
    
    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }
    
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
