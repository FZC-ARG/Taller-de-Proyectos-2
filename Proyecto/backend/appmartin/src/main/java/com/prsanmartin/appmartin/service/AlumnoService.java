package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.AlumnoDTO;
import com.prsanmartin.appmartin.entity.Alumno;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.entity.Docente;
import com.prsanmartin.appmartin.entity.Curso;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AlumnoService {
    
    /**
     * Get all students
     */
    List<AlumnoDTO> getAllStudents();
    
    /**
     * Get students filtered by teacher and course
     */
    List<AlumnoDTO> getStudentsByTeacherAndCourse(Integer docenteId, Integer cursoId);
    
    /**
     * Get student by ID
     */
    Optional<AlumnoDTO> getStudentById(Integer id);
    
    /**
     * Create new student
     */
    AlumnoDTO createStudent(AlumnoDTO alumnoDTO);
    
    /**
     * Update student
     */
    AlumnoDTO updateStudent(Integer id, AlumnoDTO alumnoDTO);
    
    /**
     * Delete student (soft delete)
     */
    boolean deleteStudent(Integer id);
    
    /**
     * Permanent delete student
     */
    boolean permanentDeleteStudent(Integer id);
    
    /**
     * Search students by name (exact match)
     */
    List<AlumnoDTO> searchStudentsByName(String nombre);
    
    /**
     * Search students by name (partial match)
     */
    List<AlumnoDTO> searchStudentsByNamePartial(String nombre);
    
    /**
     * Get students by year of entry
     */
    List<AlumnoDTO> getStudentsByYear(Integer anioIngreso);
    
    /**
     * Get students enrolled in a specific course
     */
    List<AlumnoDTO> getStudentsByCourse(Integer cursoId);
    
    /**
     * Get students by teacher
     */
    List<AlumnoDTO> getStudentsByTeacher(Integer docenteId);
    
    /**
     * Get student statistics
     */
    Map<String, Object> getStudentStatistics();
}
