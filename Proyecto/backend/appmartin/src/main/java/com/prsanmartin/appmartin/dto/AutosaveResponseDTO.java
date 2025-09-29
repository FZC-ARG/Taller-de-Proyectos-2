package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutosaveResponseDTO {
    
    private String status; // "saved", "error", "duplicate"
    private Integer idTest;
    private Integer version;
    private String estado;
    private String message;
    private LocalDateTime timestamp;
    private String clientRequestId;
    private Integer retryAfter; // seconds to wait for retry (if applicable)
    
    public static AutosaveResponseDTO success(Integer idTest, Integer version, String estado, String clientRequestId) {
        AutosaveResponseDTO response = new AutosaveResponseDTO();
        response.setStatus("saved");
        response.setIdTest(idTest);
        response.setVersion(version);
        response.setEstado(estado);
        response.setTimestamp(LocalDateTime.now());
        response.setClientRequestId(clientRequestId);
        response.setMessage("Test guardado exitosamente");
        return response;
    }
    
    public static AutosaveResponseDTO error(String message, Integer retryAfter) {
        AutosaveResponseDTO response = new AutosaveResponseDTO();
        response.setStatus("error");
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        response.setRetryAfter(retryAfter);
        return response;
    }
    
    public static AutosaveResponseDTO duplicate(String clientRequestId, Integer idTest) {
        AutosaveResponseDTO response = new AutosaveResponseDTO();
        response.setStatus("duplicate");
        response.setIdTest(idTest);
        response.setTimestamp(LocalDateTime.now());
        response.setClientRequestId(clientRequestId);
        response.setMessage("Solicitud duplicada");
        return response;
    }
}
