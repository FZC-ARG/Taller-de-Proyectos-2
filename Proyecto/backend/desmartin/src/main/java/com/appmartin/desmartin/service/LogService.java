package com.appmartin.desmartin.service;

import com.appmartin.desmartin.model.LogAcceso;
import com.appmartin.desmartin.repository.LogAccesoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    private final LogAccesoRepository logAccesoRepository;

    public LogService(LogAccesoRepository logAccesoRepository) {
        this.logAccesoRepository = logAccesoRepository;
    }

    public void registrarEvento(Integer idUsuario,
                                String tipoUsuario,
                                String accion,
                                String entidad,
                                Integer idEntidad,
                                String descripcion,
                                String origen,
                                String nivel) {
        LogAcceso log = new LogAcceso();
        log.setIdUsuario(idUsuario);
        log.setTipoUsuario(tipoUsuario);
        log.setAccion(accion);
        log.setEntidadAfectada(entidad);
        log.setIdEntidadAfectada(idEntidad);
        log.setDescripcion(descripcion);
        log.setOrigen(origen);
        log.setNivel(nivel);
        log.setFechaHora(LocalDateTime.now());
        logAccesoRepository.save(log);
    }

    public List<LogAcceso> obtenerTodos() {
        return logAccesoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaHora"));
    }
}

