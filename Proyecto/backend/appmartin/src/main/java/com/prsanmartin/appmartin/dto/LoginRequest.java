package com.prsanmartin.appmartin.dto;

import lombok.Data;

@Data
public class LoginRequest {
    
    private String usuario;
    private String contrasena;
}
