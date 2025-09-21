package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Calificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCalificacion")
    private Integer idCalificacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMatricula", nullable = false)
    private Matricula matricula;
    
    @Column(name = "Periodo", nullable = false, length = 20)
    private String periodo;
    
    @Column(name = "Nota", nullable = false, precision = 5, scale = 2)
    private BigDecimal nota;
    
    @Column(name = "FechaRegistro")
    private LocalDateTime fechaRegistro;
}
