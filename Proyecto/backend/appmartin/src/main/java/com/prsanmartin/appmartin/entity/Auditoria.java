package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAuditoria")
    private Integer idAuditoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;
    
    @Column(name = "Accion", nullable = false, length = 150)
    private String accion;
    
    @Column(name = "Entidad", nullable = false, length = 50)
    private String entidad;
    
    @Column(name = "IdEntidad")
    private Integer idEntidad;
    
    @Column(name = "Detalles", columnDefinition = "TEXT")
    private String detalles;
    
    @Column(name = "DireccionIP", length = 50)
    private String direccionIP;
    
    @Column(name = "FechaEvento")
    private LocalDateTime fechaEvento;
}
