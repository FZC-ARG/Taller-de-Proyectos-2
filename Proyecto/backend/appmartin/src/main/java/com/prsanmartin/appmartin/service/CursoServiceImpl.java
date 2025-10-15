package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.CursoDTO;
import com.prsanmartin.appmartin.entity.Curso;
import com.prsanmartin.appmartin.entity.Docente;
import com.prsanmartin.appmartin.repository.CursoRepository;
import com.prsanmartin.appmartin.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Override
    public Page<CursoDTO> getAllCursos(Pageable pageable) {
        Page<Curso> page = cursoRepository.findAll(pageable);
        List<CursoDTO> content = page.getContent().stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public CursoDTO getCursoById(Long id) {
        if (id == null) return null;
        Optional<Curso> opt = cursoRepository.findById(id.intValue());
        return opt.map(this::toDto).orElse(null);
    }

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO) {
        if (cursoDTO == null) return null;
        Curso entity = new Curso();
        entity.setNombreCurso(cursoDTO.getNombreCurso());

        if (cursoDTO.getIdDocente() != null) {
            Docente docente = docenteRepository.findById(cursoDTO.getIdDocente().intValue()).orElse(null);
            if (docente != null) {
                entity.setDocente(docente);
            }
        }

        Curso saved = cursoRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public CursoDTO updateCurso(Long id, CursoDTO cursoDTO) {
        if (id == null || cursoDTO == null) return null;
        Optional<Curso> opt = cursoRepository.findById(id.intValue());
        if (opt.isEmpty()) return null;

        Curso entity = opt.get();
        if (cursoDTO.getNombreCurso() != null) {
            entity.setNombreCurso(cursoDTO.getNombreCurso());
        }
        if (cursoDTO.getIdDocente() != null) {
            docenteRepository.findById(cursoDTO.getIdDocente().intValue()).ifPresent(entity::setDocente);
        }

        Curso saved = cursoRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public void deleteCurso(Long id) {
        if (id == null) return;
        cursoRepository.findById(id.intValue()).ifPresent(curso -> cursoRepository.deleteById(curso.getIdCurso()));
    }

    @Override
    public Page<CursoDTO> getCursosByDocente(Long idDocente, Pageable pageable) {
        if (idDocente == null) {
            return Page.empty(pageable);
        }
        List<Curso> cursos = cursoRepository.findByDocenteIdDocente(idDocente.intValue());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), cursos.size());
        List<CursoDTO> content = cursos.subList(Math.min(start, end), end)
                .stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, cursos.size());
    }

    private CursoDTO toDto(Curso c) {
        if (c == null) return null;
        CursoDTO dto = new CursoDTO();
        dto.setIdCurso(c.getIdCurso() != null ? c.getIdCurso().longValue() : null);
        dto.setNombreCurso(c.getNombreCurso());
        dto.setDescripcion(null);
        dto.setIdDocente(c.getDocente() != null ? c.getDocente().getIdDocente().longValue() : null);
        dto.setCreditos(null);
        dto.setEstado(null);
        dto.setFechaCreacion(null);
        dto.setFechaActualizacion(null);
        return dto;
    }
}