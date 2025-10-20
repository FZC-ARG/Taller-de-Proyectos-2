package com.prsanmartin.appmartin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoDTO {
    
    private Integer idAlumno;
    
    @NotNull(message = "ID de usuario es obligatorio")
    private Integer idUsuario;
    
    @NotBlank(message = "Nombre de usuario es obligatorio")
    private String nombreUsuario;
    
    @NotBlank(message = "Correo electrónico es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    private String correoElectronico;
    
    @NotNull(message = "Año de ingreso es obligatorio")
    @Min(value = 2000, message = "Año de ingreso debe ser mayor a 2000")
    private Integer anioIngreso;
    
    private String especialidad; // For display purposes
    private LocalDateTime fechaCreacion;
    private Boolean activo;
    
    // Additional fields for display
    private String nombreCompleto;
    private Integer totalCursos;
    private Double promedioGeneral;
}
