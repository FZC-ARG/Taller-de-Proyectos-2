package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAlumnoRequest {
    private String nombreCompleto;
    private String nombreUsuario;
    private String contrasena;
}

