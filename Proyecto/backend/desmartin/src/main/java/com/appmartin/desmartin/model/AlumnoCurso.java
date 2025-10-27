package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alumnos_cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AlumnoCursoId.class)
public class AlumnoCurso {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_alumno_fk")
    private Alumno alumno;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_curso_fk")
    private Curso curso;

    @Column(name = "fecha_matricula")
    private LocalDateTime fechaMatricula = LocalDateTime.now();
}
