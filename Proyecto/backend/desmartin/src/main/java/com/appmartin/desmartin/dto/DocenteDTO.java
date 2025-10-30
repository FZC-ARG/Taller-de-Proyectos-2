package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocenteDTO {
    private Integer idDocente;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
}

