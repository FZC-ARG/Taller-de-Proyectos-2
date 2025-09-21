package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.entity.Auditoria;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.AuditoriaRepository;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void registrarAccion(String accion, String entidad, Integer idEntidad, String detalles, String direccionIP, String nombreUsuario) {
        Auditoria auditoria = new Auditoria();
        
        if (nombreUsuario != null) {
            Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
            usuario.ifPresent(auditoria::setUsuario);
        }
        
        auditoria.setAccion(accion);
        auditoria.setEntidad(entidad);
        auditoria.setIdEntidad(idEntidad);
        auditoria.setDetalles(detalles);
        auditoria.setDireccionIP(direccionIP);
        auditoria.setFechaEvento(LocalDateTime.now());
        
        auditoriaRepository.save(auditoria);
    }

    public void registrarLogin(String nombreUsuario, String direccionIP, boolean exito) {
        String accion = exito ? "LOGIN_EXITOSO" : "LOGIN_FALLIDO";
        String detalles = exito ? "Usuario autenticado correctamente" : "Credenciales inválidas";
        registrarAccion(accion, "USUARIO", null, detalles, direccionIP, nombreUsuario);
    }

    public void registrarLogout(String nombreUsuario, String direccionIP) {
        registrarAccion("LOGOUT", "USUARIO", null, "Usuario cerró sesión", direccionIP, nombreUsuario);
    }

    public void registrarCreacion(String entidad, Integer idEntidad, String nombreUsuario, String direccionIP) {
        registrarAccion("CREAR", entidad, idEntidad, "Entidad creada", direccionIP, nombreUsuario);
    }

    public void registrarActualizacion(String entidad, Integer idEntidad, String nombreUsuario, String direccionIP) {
        registrarAccion("ACTUALIZAR", entidad, idEntidad, "Entidad actualizada", direccionIP, nombreUsuario);
    }

    public void registrarEliminacion(String entidad, Integer idEntidad, String nombreUsuario, String direccionIP) {
        registrarAccion("ELIMINAR", entidad, idEntidad, "Entidad eliminada", direccionIP, nombreUsuario);
    }

    public List<Auditoria> obtenerAuditoriaPorUsuario(String nombreUsuario) {
        return auditoriaRepository.findByUsuarioNombreUsuario(nombreUsuario);
    }

    public List<Auditoria> obtenerAuditoriaPorEntidad(String entidad) {
        return auditoriaRepository.findByEntidad(entidad);
    }

    public List<Auditoria> obtenerTodaLaAuditoria() {
        return auditoriaRepository.findAll();
    }
}
