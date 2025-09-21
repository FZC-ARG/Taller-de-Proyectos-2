package com.prsanmartin.appmartin.dto;

import java.time.LocalDateTime;

public class CalificacionDTO {
    private Long idCalificacion;
    private Long idAlumno;
    private Long idCurso;
    private Double calificacion;
    private String tipoCalificacion;
    private String observaciones;
    private LocalDateTime fechaCalificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Constructors
    public CalificacionDTO() {}

    public CalificacionDTO(Long idCalificacion, Long idAlumno, Long idCurso, Double calificacion, 
                         String tipoCalificacion, String observaciones, LocalDateTime fechaCalificacion, 
                         LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idCalificacion = idCalificacion;
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.calificacion = calificacion;
        this.tipoCalificacion = tipoCalificacion;
        this.observaciones = observaciones;
        this.fechaCalificacion = fechaCalificacion;
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

    public String getTipoCalificacion() {
        return tipoCalificacion;
    }

    public void setTipoCalificacion(String tipoCalificacion) {
        this.tipoCalificacion = tipoCalificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDateTime fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
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