package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "Administradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAdministrador")
    private Integer idAdministrador;
    
    @Column(name = "NombreCompleto", nullable = false, length = 100)
    private String nombreCompleto;
    
    @Column(name = "Usuario", nullable = false, unique = true, length = 50)
    private String usuario;
    
    @Column(name = "CorreoElectronico", nullable = false, unique = true, length = 150)
    private String correoElectronico;
    
    @Column(name = "ContrasenaHash", nullable = false, length = 255)
    private String contrasenaHash;
    
    @Column(name = "Activo", nullable = false)
    private Boolean activo = true;
    
    @Column(name = "FechaCreacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "UltimoAcceso")
    private LocalDateTime ultimoAcceso;
    
    @Column(name = "NivelPrivilegio", nullable = false)
    private Integer nivelPrivilegio = 1; // 1 = Administrador completo
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
