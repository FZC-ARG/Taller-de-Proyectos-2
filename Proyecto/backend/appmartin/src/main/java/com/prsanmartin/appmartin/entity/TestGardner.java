package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TestsGardner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestGardner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTest")
    private Integer idTest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno", nullable = false)
    private Alumno alumno;
    
    @Column(name = "Respuestas", columnDefinition = "TEXT")
    private String respuestas;
    
    @Column(name = "Puntajes", columnDefinition = "TEXT")
    private String puntajes;
    
    @Column(name = "FechaAplicacion")
    private LocalDateTime fechaAplicacion;
}
