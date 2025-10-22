package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.AlumnoDTO;
import com.prsanmartin.appmartin.entity.Alumno;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.entity.Matricula;
import com.prsanmartin.appmartin.entity.Calificacion;
import com.prsanmartin.appmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private MatriculaRepository matriculaRepository;
    
    @Autowired
    private CalificacionRepository calificacionRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> getAllStudents() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        return alumnos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> getStudentsByTeacherAndCourse(Integer docenteId, Integer cursoId) {
        List<Alumno> alumnos = alumnoRepository.findByTeacherAndCourse(docenteId, cursoId);
        return alumnos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AlumnoDTO> getStudentById(Integer id) {
        return alumnoRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional
    public AlumnoDTO createStudent(AlumnoDTO alumnoDTO) {
        // Validate that user doesn't exist
        if (usuarioRepository.findByNombreUsuario(alumnoDTO.getNombreUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        if (usuarioRepository.findByCorreoElectronico(alumnoDTO.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya existe");
        }
        
        // Get ALUMNO role
        Rol rolAlumno = rolRepository.findByNombreRol("ALUMNO");
        if (rolAlumno == null) {
            throw new IllegalArgumentException("Rol ALUMNO no encontrado");
        }
        
        // Create user
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(alumnoDTO.getNombreUsuario());
        usuario.setCorreoElectronico(alumnoDTO.getCorreoElectronico());
        usuario.setContrasenaHash("$2a$12$defaultPasswordHash"); // Default password hash
        usuario.setRol(rolAlumno);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        
        // Create student
        Alumno alumno = new Alumno();
        alumno.setUsuario(savedUsuario);
        alumno.setAnioIngreso(alumnoDTO.getAnioIngreso());
        
        Alumno savedAlumno = alumnoRepository.save(alumno);
        
        // Audit trail
        try {
            auditoriaService.registrarAccion(
                "ALUMNO_CREADO",
                "Alumno",
                savedAlumno.getIdAlumno(),
                "Alumno creado: " + alumnoDTO.getNombreUsuario(),
                null,
                "SYSTEM"
            );
        } catch (Exception e) {
            // Log error but don't fail the transaction
            System.err.println("Error en auditoría: " + e.getMessage());
        }
        
        return convertToDTO(savedAlumno);
    }
    
    @Override
    @Transactional
    public AlumnoDTO updateStudent(Integer id, AlumnoDTO alumnoDTO) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        
        Usuario usuario = alumno.getUsuario();
        
        // Check if username is being changed and if it's available
        if (!usuario.getNombreUsuario().equals(alumnoDTO.getNombreUsuario())) {
            if (usuarioRepository.findByNombreUsuario(alumnoDTO.getNombreUsuario()).isPresent()) {
                throw new IllegalArgumentException("El nombre de usuario ya existe");
            }
            usuario.setNombreUsuario(alumnoDTO.getNombreUsuario());
        }
        
        // Check if email is being changed and if it's available
        if (!usuario.getCorreoElectronico().equals(alumnoDTO.getCorreoElectronico())) {
            if (usuarioRepository.findByCorreoElectronico(alumnoDTO.getCorreoElectronico()).isPresent()) {
                throw new IllegalArgumentException("El correo electrónico ya existe");
            }
            usuario.setCorreoElectronico(alumnoDTO.getCorreoElectronico());
        }
        
        // Update student data
        alumno.setAnioIngreso(alumnoDTO.getAnioIngreso());
        
        usuarioRepository.save(usuario);
        Alumno savedAlumno = alumnoRepository.save(alumno);
        
        // Audit trail
        auditoriaService.registrarAccion(
            "ALUMNO_ACTUALIZADO",
            "Alumno",
            savedAlumno.getIdAlumno(),
            "Alumno actualizado: " + alumnoDTO.getNombreUsuario(),
            null,
            "SYSTEM"
        );
        
        return convertToDTO(savedAlumno);
    }
    
    @Override
    @Transactional
    public boolean deleteStudent(Integer id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        
        Usuario usuario = alumno.getUsuario();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        // Audit trail
        auditoriaService.registrarAccion(
            "ALUMNO_ELIMINADO",
            "Alumno",
            alumno.getIdAlumno(),
            "Alumno eliminado (soft delete): " + usuario.getNombreUsuario(),
            null,
            "SYSTEM"
        );
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean permanentDeleteStudent(Integer id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        
        Usuario usuario = alumno.getUsuario();
        
        // Delete related data first (matriculas, calificaciones, etc.)
        List<Matricula> matriculas = matriculaRepository.findByAlumnoIdAlumno(id);
        for (Matricula matricula : matriculas) {
            List<Calificacion> calificaciones = calificacionRepository.findByMatriculaIdMatricula(matricula.getIdMatricula());
            calificacionRepository.deleteAll(calificaciones);
        }
        matriculaRepository.deleteAll(matriculas);
        
        // Delete student and user
        alumnoRepository.delete(alumno);
        usuarioRepository.delete(usuario);
        
        // Audit trail
        auditoriaService.registrarAccion(
            "ALUMNO_ELIMINADO_PERMANENTE",
            "Alumno",
            id,
            "Alumno eliminado permanentemente: " + usuario.getNombreUsuario(),
            null,
            "SYSTEM"
        );
        
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> searchStudentsByName(String nombre) {
        List<Alumno> alumnos = alumnoRepository.findByUsuarioNombreUsuarioContainingIgnoreCase(nombre);
        return alumnos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> searchStudentsByNamePartial(String nombre) {
        // Same as exact search for now, but could be enhanced
        return searchStudentsByName(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> getStudentsByYear(Integer anioIngreso) {
        List<Alumno> alumnos = alumnoRepository.findByAnioIngreso(anioIngreso);
        return alumnos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> getStudentsByCourse(Integer cursoId) {
        List<Alumno> alumnos = alumnoRepository.findByCourseId(cursoId);
        return alumnos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AlumnoDTO> getStudentsByTeacher(Integer docenteId) {
        List<Alumno> alumnos = alumnoRepository.findByTeacherId(docenteId);
        return alumnos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStudentStatistics() {
        long totalStudents = alumnoRepository.count();
        long activeStudents = alumnoRepository.countActiveStudents();
        
        Map<String, Long> studentsByYear = alumnoRepository.countStudentsByYear()
                .stream()
                .collect(Collectors.toMap(
                    arr -> arr[0].toString(),
                    arr -> (Long) arr[1]
                ));
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalStudents", totalStudents);
        statistics.put("activeStudents", activeStudents);
        statistics.put("inactiveStudents", totalStudents - activeStudents);
        statistics.put("studentsByYear", studentsByYear);
        
        return statistics;
    }
    
    // Helper method to convert entity to DTO
    private AlumnoDTO convertToDTO(Alumno alumno) {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setIdAlumno(alumno.getIdAlumno());
        dto.setIdUsuario(alumno.getUsuario().getIdUsuario());
        dto.setNombreUsuario(alumno.getUsuario().getNombreUsuario());
        dto.setCorreoElectronico(alumno.getUsuario().getCorreoElectronico());
        dto.setAnioIngreso(alumno.getAnioIngreso());
        dto.setFechaCreacion(alumno.getUsuario().getFechaCreacion());
        dto.setActivo(alumno.getUsuario().getActivo());
        
        // Calculate additional fields
        List<Matricula> matriculas = matriculaRepository.findByAlumnoIdAlumno(alumno.getIdAlumno());
        dto.setTotalCursos(matriculas.size());
        
        // Calculate average grade
        List<Calificacion> calificaciones = matriculas.stream()
                .flatMap(matricula -> calificacionRepository.findByMatriculaIdMatricula(matricula.getIdMatricula()).stream())
                .collect(Collectors.toList());
        
        if (!calificaciones.isEmpty()) {
            double promedio = calificaciones.stream()
                    .mapToDouble(calificacion -> calificacion.getNota().doubleValue())
                    .average()
                    .orElse(0.0);
            dto.setPromedioGeneral(promedio);
        } else {
            dto.setPromedioGeneral(0.0);
        }
        
        return dto;
    }
}
