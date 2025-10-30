package com.appmartin.desmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearAlumnoRequest {
    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}

