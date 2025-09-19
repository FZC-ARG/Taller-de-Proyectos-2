package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String token;
    private String tipoToken = "Bearer";
    private Integer idAdministrador;
    private String nombreCompleto;
    private String usuario;
    private String correoElectronico;
    private Integer nivelPrivilegio;
    private LocalDateTime ultimoAcceso;
    private String mensaje;
    private boolean exito;
}
