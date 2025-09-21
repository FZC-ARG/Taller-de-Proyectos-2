package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCurso")
    private Integer idCurso;
    
    @Column(name = "NombreCurso", nullable = false, length = 150)
    private String nombreCurso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente", nullable = false)
    private Docente docente;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Matricula> matriculas;
}
