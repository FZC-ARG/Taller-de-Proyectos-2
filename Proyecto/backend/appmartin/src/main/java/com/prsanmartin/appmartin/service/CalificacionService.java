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
    boolean deleteCalificacion(Long id);
    List<CalificacionDTO> getCalificacionesByAlumno(Long alumnoId);
    List<CalificacionDTO> getCalificacionesByCurso(Long cursoId);
    List<CalificacionDTO> getCalificacionesByAlumnoAndCurso(Long alumnoId, Long cursoId);
    Double getPromedioByAlumno(Long alumnoId);
    Double getPromedioByCurso(Long cursoId);
}
