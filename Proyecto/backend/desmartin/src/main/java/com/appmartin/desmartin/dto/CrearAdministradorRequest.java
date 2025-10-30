package com.appmartin.desmartin.dto;

import lombok.Data;

@Data
public class CrearAdministradorRequest {
    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String apellido;

}