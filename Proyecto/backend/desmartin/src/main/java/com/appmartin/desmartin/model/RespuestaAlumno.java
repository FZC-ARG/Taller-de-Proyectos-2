package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "respuestas_alumno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaAlumno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Integer idRespuesta;
    
    @ManyToOne
    @JoinColumn(name = "id_intento_fk", nullable = false)
    private IntentoTest intentoTest;
    
    @ManyToOne
    @JoinColumn(name = "id_pregunta_fk", nullable = false)
    private PreguntaTest preguntaTest;
    
    @Column(name = "puntaje", nullable = false)
    private Byte puntaje;
}

