package com.appmartin.desmartin.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlumnoCursoId implements Serializable {
    private Integer alumno;
    private Integer curso;
}
