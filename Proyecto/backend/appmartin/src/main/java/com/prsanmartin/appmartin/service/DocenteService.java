package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.DocenteDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DocenteService {
    List<DocenteDTO> getAllTeachers();
    Optional<DocenteDTO> getTeacherById(Integer id);
    DocenteDTO createTeacher(DocenteDTO docenteDTO);
    DocenteDTO updateTeacher(Integer id, DocenteDTO docenteDTO);
    boolean deleteTeacher(Integer id);
    List<DocenteDTO> getTeachersBySpecialty(String especialidad);
    Map<String, Object> getTeacherStatistics();
}
