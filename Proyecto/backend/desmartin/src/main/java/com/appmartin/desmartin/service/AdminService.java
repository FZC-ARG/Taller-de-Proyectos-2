package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private LogAccesoRepository logAccesoRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AdministradorRepository administradorRepository;

    // Crear
    public AdministradorDTO crearAdministrador(CrearAdministradorRequest request) {
        Administrador admin = new Administrador();
        admin.setNombreUsuario(request.getNombreUsuario());
        admin.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        Administrador saved = administradorRepository.save(admin);
        return new AdministradorDTO(saved.getIdAdmin(), saved.getNombreUsuario());
    }

    // Actualizar
    public AdministradorDTO actualizarAdministrador(Integer id, CrearAdministradorRequest request) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        admin.setNombreUsuario(request.getNombreUsuario());
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            admin.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        }
        Administrador saved = administradorRepository.save(admin);
        return new AdministradorDTO(saved.getIdAdmin(), saved.getNombreUsuario());
    }

    // Eliminar
    public void eliminarAdministrador(Integer id) {
        administradorRepository.deleteById(id);
    }

    // Listar
    public List<AdministradorDTO> listarAdministradores() {
        return administradorRepository.findAll()
                .stream()
                .map(a -> new AdministradorDTO(a.getIdAdmin(), a.getNombreUsuario()))
                .collect(Collectors.toList());
    }

    // Docentes
    public DocenteDTO crearDocente(CrearDocenteRequest request) {
        Docente docente = new Docente();
        docente.setNombreUsuario(request.getNombreUsuario());
        docente.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        
        Docente saved = docenteRepository.save(docente);
        return new DocenteDTO(saved.getIdDocente(), saved.getNombreUsuario());
    }
    
    public DocenteDTO actualizarDocente(Integer id, CrearDocenteRequest request) {
        Docente docente = docenteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        docente.setNombreUsuario(request.getNombreUsuario());
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            docente.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        }
        
        Docente saved = docenteRepository.save(docente);
        return new DocenteDTO(saved.getIdDocente(), saved.getNombreUsuario());
    }
    
    public void eliminarDocente(Integer id) {
        docenteRepository.deleteById(id);
    }
    
    public List<DocenteDTO> listarDocentes() {
        return docenteRepository.findAll().stream()
            .map(d -> new DocenteDTO(d.getIdDocente(), d.getNombreUsuario()))
            .collect(Collectors.toList());
    }
    
    // Alumnos
    public AlumnoDTO crearAlumno(CrearAlumnoRequest request) {
        Alumno alumno = new Alumno();
        alumno.setNombreUsuario(request.getNombreUsuario());
        alumno.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        
        Alumno saved = alumnoRepository.save(alumno);
        return new AlumnoDTO(saved.getIdAlumno(), saved.getNombreUsuario());
    }
    
    public AlumnoDTO actualizarAlumno(Integer id, CrearAlumnoRequest request) {
        Alumno alumno = alumnoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        
        alumno.setNombreUsuario(request.getNombreUsuario());
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            alumno.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        }
        
        Alumno saved = alumnoRepository.save(alumno);
        return new AlumnoDTO(saved.getIdAlumno(), saved.getNombreUsuario());
    }
    
    public void eliminarAlumno(Integer id) {
        alumnoRepository.deleteById(id);
    }
    
    public List<AlumnoDTO> listarAlumnos() {
        return alumnoRepository.findAll().stream()
            .map(a -> new AlumnoDTO(a.getIdAlumno(), a.getNombreUsuario()))
            .collect(Collectors.toList());
    }
    
    // Logs
    public List<LogAccesoDTO> listarLogs() {
        return logAccesoRepository.findAll().stream()
            .map(log -> new LogAccesoDTO(
                log.getIdLog(),
                log.getIdUsuario(),
                log.getTipoUsuario().name(),
                log.getFechaHoraAcceso()
            ))
            .collect(Collectors.toList());
    }
}
