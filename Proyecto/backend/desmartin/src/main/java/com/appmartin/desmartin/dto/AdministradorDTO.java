package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorDTO {
    private Integer idAdmin;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
}
