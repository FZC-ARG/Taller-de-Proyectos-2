package com.prsanmartin.appmartin.dto;

import java.time.LocalDateTime;

public class CursoDTO {
    private Long idCurso;
    private String nombre;
    private String descripcion;
    private Long idDocente;
    private Integer creditos;
    private String estado;
    
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructors
    public CursoDTO() {}
    
    public CursoDTO(Long idCurso, String nombre, String descripcion, Long idDocente, 
                   Integer creditos, String estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idDocente = idDocente;
        this.creditos = creditos;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters and Setters
    public Long getIdCurso() {
        return idCurso;
    }
    
    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Long getIdDocente() {
        return idDocente;
    }
    
    public void setIdDocente(Long idDocente) {
        this.idDocente = idDocente;
    }
    
    public Integer getCreditos() {
        return creditos;
    }
    
    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
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
