package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_sesiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSesion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Integer idSesion;
    
    @ManyToOne
    @JoinColumn(name = "id_docente_fk", nullable = false)
    private Docente docente;
    
    @ManyToOne
    @JoinColumn(name = "id_alumno_fk")
    private Alumno alumno;
    
    @Column(name = "titulo_sesion", nullable = false, length = 255)
    private String tituloSesion;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

