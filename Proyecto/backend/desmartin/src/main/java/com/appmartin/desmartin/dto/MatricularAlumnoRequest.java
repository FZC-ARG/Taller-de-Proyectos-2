package com.appmartin.desmartin.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MatricularAlumnoRequest {
    private Integer idAlumnoFk;
    private Integer idCursoFk;

    public Integer getIdAlumnoFk() {
        return idAlumnoFk;
    }

    public void setIdAlumnoFk(Integer idAlumnoFk) {
        this.idAlumnoFk = idAlumnoFk;
    }

    public Integer getIdCursoFk() {
        return idCursoFk;
    }

    public void setIdCursoFk(Integer idCursoFk) {
        this.idCursoFk = idCursoFk;
    }
}
