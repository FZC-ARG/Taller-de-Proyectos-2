package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "intentos_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntentoTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_intento")
    private Integer idIntento;
    
    @ManyToOne
    @JoinColumn(name = "id_alumno_fk", nullable = false)
    private Alumno alumno;
    
    @Column(name = "fecha_realizacion")
    private LocalDateTime fechaRealizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaRealizacion = LocalDateTime.now();
    }
}

