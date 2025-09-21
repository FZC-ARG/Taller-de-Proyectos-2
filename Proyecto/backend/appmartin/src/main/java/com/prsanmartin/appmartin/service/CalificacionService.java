package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.CalificacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CalificacionService {
    Page<CalificacionDTO> getAllCalificaciones(Pageable pageable);
    CalificacionDTO getCalificacionById(Long id);
    CalificacionDTO createCalificacion(CalificacionDTO calificacionDTO);
    CalificacionDTO updateCalificacion(Long id, CalificacionDTO calificacionDTO);
    void deleteCalificacion(Long id);
    Page<CalificacionDTO> getCalificacionesByAlumno(Long idAlumno, Pageable pageable);
    Page<CalificacionDTO> getCalificacionesByCurso(Long idCurso, Pageable pageable);
}