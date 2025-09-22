package com.prsanmartin.appmartin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank(message = "El nombre de usuario es requerido")
    private String usuario;
    
    @NotBlank(message = "La contraseña es requerida")
    private String contrasena;
}
