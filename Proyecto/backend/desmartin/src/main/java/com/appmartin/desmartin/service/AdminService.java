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

    // ================= ADMINISTRADORES =================
    public AdministradorDTO crearAdministrador(CrearAdministradorRequest request) {
        Administrador admin = new Administrador();
        admin.setNombreUsuario(request.getNombreUsuario());
        admin.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        admin.setNombre(request.getNombre());
        admin.setApellido(request.getApellido());

        Administrador saved = administradorRepository.save(admin);
        return new AdministradorDTO(
                saved.getIdAdmin(),
                saved.getNombreUsuario(),
                saved.getNombre(),
                saved.getApellido()
        );
    }

    public AdministradorDTO actualizarAdministrador(Integer id, CrearAdministradorRequest request) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        admin.setNombreUsuario(request.getNombreUsuario());
        admin.setNombre(request.getNombre());
        admin.setApellido(request.getApellido());


        Administrador saved = administradorRepository.save(admin);
        return new AdministradorDTO(
                saved.getIdAdmin(),
                saved.getNombreUsuario(),
                saved.getNombre(),
                saved.getApellido()
        );
    }

    public void eliminarAdministrador(Integer id) {
        administradorRepository.deleteById(id);
    }

    public List<AdministradorDTO> listarAdministradores() {
        return administradorRepository.findAll().stream()
                .map(a -> new AdministradorDTO(
                        a.getIdAdmin(),
                        a.getNombreUsuario(),
                        a.getNombre(),
                        a.getApellido()
                ))
                .collect(Collectors.toList());
    }

    public AdministradorDTO obtenerAdministradorPorId(Integer id) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        return new AdministradorDTO(
                admin.getIdAdmin(),
                admin.getNombreUsuario(),
                admin.getNombre(),
                admin.getApellido()
        );
    }


    // ================= DOCENTES =================
    public DocenteDTO crearDocente(CrearDocenteRequest request) {
        Docente docente = new Docente();
        docente.setNombreUsuario(request.getNombreUsuario());
        docente.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        docente.setNombre(request.getNombre());
        docente.setApellido(request.getApellido());

        Docente saved = docenteRepository.save(docente);
        return new DocenteDTO(
                saved.getIdDocente(),
                saved.getNombreUsuario(),
                saved.getNombre(),
                saved.getApellido()
        );
    }

    public DocenteDTO actualizarDocente(Integer id, CrearDocenteRequest request) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        docente.setNombreUsuario(request.getNombreUsuario());
        docente.setNombre(request.getNombre());
        docente.setApellido(request.getApellido());


        Docente saved = docenteRepository.save(docente);
        return new DocenteDTO(
                saved.getIdDocente(),
                saved.getNombreUsuario(),
                saved.getNombre(),
                saved.getApellido()
        );
    }

    public void eliminarDocente(Integer id) {
        docenteRepository.deleteById(id);
    }

    public List<DocenteDTO> listarDocentes() {
        return docenteRepository.findAll().stream()
                .map(d -> new DocenteDTO(
                        d.getIdDocente(),
                        d.getNombreUsuario(),
                        d.getNombre(),
                        d.getApellido()
                ))
                .collect(Collectors.toList());
    }

    public DocenteDTO obtenerDocentePorId(Integer id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        return new DocenteDTO(
                docente.getIdDocente(),
                docente.getNombreUsuario(),
                docente.getNombre(),
                docente.getApellido()
        );
    }


    // ================= ALUMNOS =================
    public AlumnoDTO crearAlumno(CrearAlumnoRequest request) {
        Alumno alumno = new Alumno();
        alumno.setNombreUsuario(request.getNombreUsuario());
        alumno.setContrasena(bCryptPasswordEncoder.encode(request.getContrasena()));
        alumno.setNombre(request.getNombre());
        alumno.setApellido(request.getApellido());
        alumno.setFechaNacimiento(request.getFechaNacimiento());

        Alumno saved = alumnoRepository.save(alumno);
        return new AlumnoDTO(
                saved.getIdAlumno(),
                saved.getNombreUsuario(),
                saved.getNombre(),
                saved.getApellido(),
                saved.getFechaNacimiento()
        );
    }

    public AlumnoDTO actualizarAlumno(Integer id, CrearAlumnoRequest request) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        alumno.setNombreUsuario(request.getNombreUsuario());
        alumno.setNombre(request.getNombre());
        alumno.setApellido(request.getApellido());
        alumno.setFechaNacimiento(request.getFechaNacimiento());


        Alumno saved = alumnoRepository.save(alumno);
        return new AlumnoDTO(
                saved.getIdAlumno(),
                saved.getNombreUsuario(),
                saved.getNombre(),
                saved.getApellido(),
                saved.getFechaNacimiento()
        );
    }

    public void eliminarAlumno(Integer id) {
        alumnoRepository.deleteById(id);
    }

    public List<AlumnoDTO> listarAlumnos() {
        return alumnoRepository.findAll().stream()
                .map(a -> new AlumnoDTO(
                        a.getIdAlumno(),
                        a.getNombreUsuario(),
                        a.getNombre(),
                        a.getApellido(),
                        a.getFechaNacimiento()
                ))
                .collect(Collectors.toList());
    }

    public AlumnoDTO obtenerAlumnoPorId(Integer id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        return new AlumnoDTO(
                alumno.getIdAlumno(),
                alumno.getNombreUsuario(),
                alumno.getNombre(),
                alumno.getApellido(),
                alumno.getFechaNacimiento()
        );
    }

    
    // Logs
    public List<LogAccesoDTO> listarLogs() {
        return logAccesoRepository.findAll().stream()
            .map(log -> new LogAccesoDTO(
                log.getIdLog() != null ? log.getIdLog().intValue() : null,
                log.getIdUsuario(),
                log.getTipoUsuario(),
                log.getFechaHora()
            ))
            .collect(Collectors.toList());
    }
}