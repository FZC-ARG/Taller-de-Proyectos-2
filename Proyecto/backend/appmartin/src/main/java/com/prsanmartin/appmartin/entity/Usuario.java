package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer idUsuario;
    
    @Column(name = "NombreUsuario", nullable = false, unique = true, length = 80)
    private String nombreUsuario;
    
    @Column(name = "CorreoElectronico", nullable = false, unique = true, length = 150)
    private String correoElectronico;
    
    @Column(name = "ContrasenaHash", nullable = false, length = 255)
    private String contrasenaHash;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRol", nullable = false)
    private Rol rol;
    
    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;
}
