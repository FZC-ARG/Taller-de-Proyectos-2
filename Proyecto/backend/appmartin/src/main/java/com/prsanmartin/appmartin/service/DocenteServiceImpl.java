package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.dto.DocenteDTO;
import com.prsanmartin.appmartin.entity.Docente;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.DocenteRepository;
import com.prsanmartin.appmartin.repository.RolRepository;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocenteServiceImpl implements DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Override
    @Transactional(readOnly = true)
    public List<DocenteDTO> getAllTeachers() {
        return docenteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocenteDTO> getTeacherById(Integer id) {
        return docenteRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public DocenteDTO createTeacher(DocenteDTO docenteDTO) {
        // Check if user already exists
        if (usuarioRepository.findByNombreUsuario(docenteDTO.getNombreUsuario()).isPresent() ||
            usuarioRepository.findByCorreoElectronico(docenteDTO.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre de usuario o correo electrónico.");
        }

        // Find DOCENTE role
        Rol rolDocente = rolRepository.findByNombreRol("DOCENTE");
        if (rolDocente == null) {
            throw new IllegalArgumentException("Rol 'DOCENTE' no encontrado.");
        }

        // Create user
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(docenteDTO.getNombreUsuario());
        usuario.setCorreoElectronico(docenteDTO.getCorreoElectronico());
        usuario.setContrasenaHash("password123"); // Default password - should be changed
        usuario.setRol(rolDocente);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        // Create teacher
        Docente docente = new Docente();
        docente.setUsuario(usuario);
        docente.setEspecialidad(docenteDTO.getEspecialidad());

        Docente savedDocente = docenteRepository.save(docente);

        // Audit trail
        auditoriaService.registrarAccion(
            "CREATE_DOCENTE",
            "Docente",
            savedDocente.getIdDocente(),
            "Nuevo docente creado: " + savedDocente.getUsuario().getNombreUsuario(),
            null,
            "SYSTEM"
        );

        return convertToDTO(savedDocente);
    }

    @Override
    public DocenteDTO updateTeacher(Integer id, DocenteDTO docenteDTO) {
        Docente existingDocente = docenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado con ID: " + id));

        Usuario existingUsuario = existingDocente.getUsuario();

        // Update user details
        existingUsuario.setNombreUsuario(docenteDTO.getNombreUsuario());
        existingUsuario.setCorreoElectronico(docenteDTO.getCorreoElectronico());
        usuarioRepository.save(existingUsuario);

        // Update teacher details
        existingDocente.setEspecialidad(docenteDTO.getEspecialidad());
        Docente updatedDocente = docenteRepository.save(existingDocente);

        // Audit trail
        auditoriaService.registrarAccion(
            "UPDATE_DOCENTE",
            "Docente",
            updatedDocente.getIdDocente(),
            "Docente actualizado: " + updatedDocente.getUsuario().getNombreUsuario(),
            null,
            "SYSTEM"
        );

        return convertToDTO(updatedDocente);
    }

    @Override
    public boolean deleteTeacher(Integer id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado con ID: " + id));

        Usuario usuario = docente.getUsuario();
        usuario.setActivo(false); // Soft delete
        usuarioRepository.save(usuario);

        // Audit trail
        auditoriaService.registrarAccion(
            "SOFT_DELETE_DOCENTE",
            "Docente",
            docente.getIdDocente(),
            "Docente eliminado lógicamente: " + docente.getUsuario().getNombreUsuario(),
            null,
            "SYSTEM"
        );
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocenteDTO> getTeachersBySpecialty(String especialidad) {
        return docenteRepository.findByEspecialidad(especialidad).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTeacherStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTeachers", docenteRepository.count());
        stats.put("activeTeachers", docenteRepository.countActiveTeachers());
        stats.put("teachersBySpecialty", docenteRepository.countTeachersBySpecialty());
        return stats;
    }

    private DocenteDTO convertToDTO(Docente docente) {
        DocenteDTO dto = new DocenteDTO();
        dto.setIdDocente(docente.getIdDocente());
        dto.setIdUsuario(docente.getUsuario().getIdUsuario());
        dto.setNombreUsuario(docente.getUsuario().getNombreUsuario());
        dto.setCorreoElectronico(docente.getUsuario().getCorreoElectronico());
        dto.setEspecialidad(docente.getEspecialidad());
        dto.setActivo(docente.getUsuario().getActivo());
        return dto;
    }
}
