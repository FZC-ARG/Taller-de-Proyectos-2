package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.CursoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {
    @Override
    public Page<CursoDTO> getAllCursos(Pageable pageable) {
        // implementación aquí
        return Page.empty();
    }

    @Override
    public CursoDTO getCursoById(Long id) {
        // implementación aquí
        return null;
    }

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO) {
        // implementación aquí
        return null;
    }

    @Override
    public CursoDTO updateCurso(Long id, CursoDTO cursoDTO) {
        // implementación aquí
        return null;
    }

    @Override
    public void deleteCurso(Long id) {
        // implementación aquí
    }

    @Override
    public Page<CursoDTO> getCursosByDocente(Long idDocente, Pageable pageable) {
        // implementación aquí
        return Page.empty();
    }
}