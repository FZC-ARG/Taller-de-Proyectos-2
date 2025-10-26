package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_accesos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAcceso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;
    
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;
    
    @Column(name = "fecha_hora_acceso")
    private LocalDateTime fechaHoraAcceso;
    
    @PrePersist
    protected void onCreate() {
        fechaHoraAcceso = LocalDateTime.now();
    }
    
    public enum TipoUsuario {
        admin, docente, alumno
    }
}

