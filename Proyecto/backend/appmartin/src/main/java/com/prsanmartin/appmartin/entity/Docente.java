package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDocente")
    private Integer idDocente;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "Especialidad", nullable = false, length = 100)
    private String especialidad;
}
