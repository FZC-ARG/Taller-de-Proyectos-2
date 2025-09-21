package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RespuestasIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaIA {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRespuesta")
    private Integer idRespuesta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdSolicitud", nullable = false)
    private SolicitudIA solicitud;
    
    @Column(name = "RespuestaJSON", nullable = false, columnDefinition = "TEXT")
    private String respuestaJSON;
    
    @Column(name = "ModeloIA", length = 100)
    private String modeloIA;
    
    @Column(name = "Confianza", precision = 5, scale = 2)
    private BigDecimal confianza;
    
    @Column(name = "FechaRespuesta")
    private LocalDateTime fechaRespuesta;
}
