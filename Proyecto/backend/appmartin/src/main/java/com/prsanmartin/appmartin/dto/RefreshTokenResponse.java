package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    
    private String accessToken;
    private String refreshToken;
    private String tipoToken = "Bearer";
    private String mensaje;
    private boolean exito;
}
