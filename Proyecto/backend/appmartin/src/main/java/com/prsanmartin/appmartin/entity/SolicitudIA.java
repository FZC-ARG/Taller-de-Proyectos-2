package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "SolicitudesIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudIA {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSolicitud")
    private Integer idSolicitud;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente", nullable = false)
    private Docente docente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCurso", nullable = false)
    private Curso curso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno")
    private Alumno alumno;
    
    @Column(name = "DatosEntrada", nullable = false, columnDefinition = "TEXT")
    private String datosEntrada;
    
    @Column(name = "Estado", length = 20)
    private String estado = "PENDIENTE";
    
    @Column(name = "FechaSolicitud")
    private LocalDateTime fechaSolicitud;
    
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<RespuestaIA> respuestas;
}
