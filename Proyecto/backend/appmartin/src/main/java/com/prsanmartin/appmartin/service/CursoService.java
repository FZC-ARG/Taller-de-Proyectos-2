package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.CursoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CursoService {
    Page<CursoDTO> getAllCursos(Pageable pageable);
    CursoDTO getCursoById(Long id);
    CursoDTO createCurso(CursoDTO cursoDTO);
    CursoDTO updateCurso(Long id, CursoDTO cursoDTO);
    void deleteCurso(Long id);
    Page<CursoDTO> getCursosByDocente(Long idDocente, Pageable pageable);
}