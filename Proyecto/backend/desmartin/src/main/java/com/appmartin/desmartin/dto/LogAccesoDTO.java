package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAccesoDTO {
    private Integer idLog;
    private Integer idUsuario;
    private String tipoUsuario;
    private LocalDateTime fechaHoraAcceso;
}
