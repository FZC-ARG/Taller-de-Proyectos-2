package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private AdministradorRepository administradorRepository;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private LogAccesoRepository logAccesoRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public AdministradorDTO loginAdmin(LoginRequest request) {
        Optional<Administrador> adminOpt = administradorRepository.findByNombreUsuario(request.getNombreUsuario());
        
        if (adminOpt.isPresent() && bCryptPasswordEncoder.matches(request.getContrasena(), adminOpt.get().getContrasena())) {
            Administrador admin = adminOpt.get();
            

            
            return new AdministradorDTO(admin.getIdAdmin(), admin.getNombreUsuario(),admin.getNombre(),admin.getApellido());
        }
        
        return null;
    }
    
    public DocenteDTO loginDocente(LoginRequest request) {
        Optional<Docente> docenteOpt = docenteRepository.findByNombreUsuario(request.getNombreUsuario());
        
        if (docenteOpt.isPresent() && bCryptPasswordEncoder.matches(request.getContrasena(), docenteOpt.get().getContrasena())) {
            Docente docente = docenteOpt.get();
            

            
            return new DocenteDTO(docente.getIdDocente(), docente.getNombreUsuario(),docente.getNombre(),docente.getApellido());
        }
        
        return null;
    }
    
    public AlumnoDTO loginAlumno(LoginRequest request) {
        Optional<Alumno> alumnoOpt = alumnoRepository.findByNombreUsuario(request.getNombreUsuario());
        
        if (alumnoOpt.isPresent() && bCryptPasswordEncoder.matches(request.getContrasena(), alumnoOpt.get().getContrasena())) {
            Alumno alumno = alumnoOpt.get();
            


            return new AlumnoDTO(alumno.getIdAlumno(), alumno.getNombreUsuario(),alumno.getNombre(),alumno.getApellido(),alumno.getFechaNacimiento());
        }
        
        return null;
    }
}

