package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Matriculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMatricula")
    private Integer idMatricula;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno", nullable = false)
    private Alumno alumno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCurso", nullable = false)
    private Curso curso;
    
    @Column(name = "FechaMatricula")
    private LocalDate fechaMatricula;
    
    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calificacion> calificaciones;
}
