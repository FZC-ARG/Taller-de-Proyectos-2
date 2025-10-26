package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_mensajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;
    
    @ManyToOne
    @JoinColumn(name = "id_sesion_fk", nullable = false)
    private ChatSesion chatSesion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "emisor", nullable = false)
    private Emisor emisor;
    
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @Column(name = "fecha_hora_envio")
    private LocalDateTime fechaHoraEnvio;
    
    @PrePersist
    protected void onCreate() {
        fechaHoraEnvio = LocalDateTime.now();
    }
    
    public enum Emisor {
        docente, ia
    }
}

