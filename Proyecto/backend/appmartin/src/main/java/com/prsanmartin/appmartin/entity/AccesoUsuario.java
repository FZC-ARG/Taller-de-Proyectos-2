package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "AccesosUsuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccesoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acceso")
    private Integer idAcceso;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "rolUsuario", length = 50)
    private String rolUsuario;

    @Column(name = "fecha_acceso")
    private LocalDateTime fechaAcceso;

    @Column(name = "resultado", length = 20)
    private String resultado;
}


