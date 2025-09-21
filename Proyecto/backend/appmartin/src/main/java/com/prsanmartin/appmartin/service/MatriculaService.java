package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.MatriculaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MatriculaService {
    Page<MatriculaDTO> getAllMatriculas(Pageable pageable);
    MatriculaDTO getMatriculaById(Long id);
    MatriculaDTO createMatricula(MatriculaDTO matriculaDTO);
    MatriculaDTO updateMatricula(Long id, MatriculaDTO matriculaDTO);
    void deleteMatricula(Long id);
    Page<MatriculaDTO> getMatriculasByAlumno(Long idAlumno, Pageable pageable);
    Page<MatriculaDTO> getMatriculasByCurso(Long idCurso, Pageable pageable);
}