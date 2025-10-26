package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    @Autowired
    private ChatSesionRepository chatSesionRepository;
    
    @Autowired
    private ChatMensajeRepository chatMensajeRepository;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    public ChatSesionDTO crearSesion(CrearChatSesionRequest request) {
        Docente docente = docenteRepository.findById(request.getIdDocente())
            .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        ChatSesion sesion = new ChatSesion();
        sesion.setDocente(docente);
        sesion.setTituloSesion(request.getTituloSesion());
        
        if (request.getIdAlumno() != null) {
            Alumno alumno = alumnoRepository.findById(request.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
            sesion.setAlumno(alumno);
        }
        
        ChatSesion saved = chatSesionRepository.save(sesion);
        return new ChatSesionDTO(
            saved.getIdSesion(),
            saved.getDocente().getIdDocente(),
            saved.getAlumno() != null ? saved.getAlumno().getIdAlumno() : null,
            saved.getTituloSesion(),
            saved.getFechaCreacion()
        );
    }
    
    public List<ChatSesionDTO> obtenerSesionesPorDocente(Integer idDocente) {
        return chatSesionRepository.findByDocente_IdDocente(idDocente).stream()
            .map(s -> new ChatSesionDTO(
                s.getIdSesion(),
                s.getDocente().getIdDocente(),
                s.getAlumno() != null ? s.getAlumno().getIdAlumno() : null,
                s.getTituloSesion(),
                s.getFechaCreacion()
            ))
            .collect(Collectors.toList());
    }
    
    public ChatSesionDTO obtenerSesion(Integer idSesion) {
        ChatSesion sesion = chatSesionRepository.findById(idSesion)
            .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        
        return new ChatSesionDTO(
            sesion.getIdSesion(),
            sesion.getDocente().getIdDocente(),
            sesion.getAlumno() != null ? sesion.getAlumno().getIdAlumno() : null,
            sesion.getTituloSesion(),
            sesion.getFechaCreacion()
        );
    }
    
    public ChatMensajeDTO crearMensaje(Integer idSesion, CrearMensajeRequest request) {
        ChatSesion sesion = chatSesionRepository.findById(idSesion)
            .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        
        // Guardar mensaje del docente
        ChatMensaje mensajeDocente = new ChatMensaje();
        mensajeDocente.setChatSesion(sesion);
        mensajeDocente.setEmisor(ChatMensaje.Emisor.docente);
        mensajeDocente.setContenido(request.getContenido());
        chatMensajeRepository.save(mensajeDocente);
        
        // TODO: Implementar llamada real a la API de DeepSeek aquí
        // Por ahora, simulamos la respuesta de la IA
        ChatMensaje mensajeIA = new ChatMensaje();
        mensajeIA.setChatSesion(sesion);
        mensajeIA.setEmisor(ChatMensaje.Emisor.ia);
        mensajeIA.setContenido("Esta es una respuesta simulada de la IA. El docente escribió: " + request.getContenido());
        ChatMensaje mensajeIASaved = chatMensajeRepository.save(mensajeIA);
        
        return new ChatMensajeDTO(
            mensajeIASaved.getIdMensaje(),
            mensajeIASaved.getChatSesion().getIdSesion(),
            mensajeIASaved.getEmisor().name(),
            mensajeIASaved.getContenido(),
            mensajeIASaved.getFechaHoraEnvio()
        );
    }
    
    public ChatMensajeDTO actualizarMensaje(Integer idMensaje, CrearMensajeRequest request) {
        ChatMensaje mensaje = chatMensajeRepository.findById(idMensaje)
            .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        
        mensaje.setContenido(request.getContenido());
        ChatMensaje saved = chatMensajeRepository.save(mensaje);
        
        return new ChatMensajeDTO(
            saved.getIdMensaje(),
            saved.getChatSesion().getIdSesion(),
            saved.getEmisor().name(),
            saved.getContenido(),
            saved.getFechaHoraEnvio()
        );
    }
    
    public void eliminarMensaje(Integer idMensaje) {
        chatMensajeRepository.deleteById(idMensaje);
    }
    
    public List<ChatMensajeDTO> obtenerMensajesPorSesion(Integer idSesion) {
        return chatMensajeRepository.findByChatSesion_IdSesionOrderByFechaHoraEnvioAsc(idSesion).stream()
            .map(m -> new ChatMensajeDTO(
                m.getIdMensaje(),
                m.getChatSesion().getIdSesion(),
                m.getEmisor().name(),
                m.getContenido(),
                m.getFechaHoraEnvio()
            ))
            .collect(Collectors.toList());
    }
}

