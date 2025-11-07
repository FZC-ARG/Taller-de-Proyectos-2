package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_accesos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAcceso {

    public enum TipoUsuario {
        admin, docente, alumno, sistema
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "tipo_usuario", length = 50)
    private String tipoUsuario;

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @Column(name = "entidad_afectada", length = 100)
    private String entidadAfectada;

    @Column(name = "id_entidad_afectada")
    private Integer idEntidadAfectada;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "origen", length = 255)
    private String origen;

    @Column(name = "nivel", length = 10)
    private String nivel;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario != null ? tipoUsuario.name() : null;
    }

    @PrePersist
    protected void onCreate() {
        if (accion == null) {
            accion = "LOGIN";
        }
        if (tipoUsuario == null) {
            tipoUsuario = TipoUsuario.sistema.name();
        }
        if (nivel == null) {
            nivel = "INFO";
        }
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}

