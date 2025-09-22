package com.prsanmartin.appmartin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDto {
    @NotBlank
    @Size(min = 3, max = 80)
    private String nombreUsuario;

    @NotBlank
    @Email
    private String correoElectronico;

    @NotBlank
    @Size(min = 8, max = 255)
    private String contrasena;

    // Puedes agregar getters y setters si los necesitas
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }
}
