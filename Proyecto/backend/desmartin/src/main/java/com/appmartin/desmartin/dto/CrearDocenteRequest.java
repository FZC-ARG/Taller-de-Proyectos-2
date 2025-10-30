package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearDocenteRequest {
    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String apellido;
}

