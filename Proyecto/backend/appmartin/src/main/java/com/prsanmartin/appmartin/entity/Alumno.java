package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Alumnos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAlumno")
    private Integer idAlumno;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "AnioIngreso", nullable = false)
    private Integer anioIngreso;
}
