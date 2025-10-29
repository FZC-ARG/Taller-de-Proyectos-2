package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "nombre_curso", nullable = false)
    private String nombreCurso;

    @Column(name = "descripcion")
    private String descripcion;

    // Relación con docente
    @ManyToOne
    @JoinColumn(name = "id_docente_fk", nullable = false)
    private Docente docente;

}
