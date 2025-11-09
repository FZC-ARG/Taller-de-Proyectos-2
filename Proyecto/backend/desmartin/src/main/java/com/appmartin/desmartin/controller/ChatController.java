package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @PostMapping("/sesiones")
    public ResponseEntity<ChatSesionDTO> crearSesion(@RequestBody CrearChatSesionRequest request) {
        return ResponseEntity.ok(chatService.crearSesion(request));
    }
    
    @GetMapping("/sesiones/docente/{idDocente}")
    public ResponseEntity<List<ChatSesionDTO>> obtenerSesionesPorDocente(@PathVariable Integer idDocente) {
        return ResponseEntity.ok(chatService.obtenerSesionesPorDocente(idDocente));
    }
    
    @GetMapping("/sesiones/{idSesion}")
    public ResponseEntity<ChatSesionDTO> obtenerSesion(@PathVariable Integer idSesion) {
        return ResponseEntity.ok(chatService.obtenerSesion(idSesion));
    }
    
    @PostMapping("/sesiones/{idSesion}/mensajes")
    public ResponseEntity<ChatMensajeDTO> crearMensaje(@PathVariable Integer idSesion, @RequestBody CrearMensajeRequest request) {
        return ResponseEntity.ok(chatService.crearMensaje(idSesion, request));
    }
    
    @PutMapping("/mensajes/{idMensaje}")
    public ResponseEntity<ChatMensajeDTO> actualizarMensaje(@PathVariable Integer idMensaje, @RequestBody CrearMensajeRequest request) {
        return ResponseEntity.ok(chatService.actualizarMensaje(idMensaje, request));
    }
    
    @DeleteMapping("/mensajes/{idMensaje}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Integer idMensaje) {
        chatService.eliminarMensaje(idMensaje);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/sesiones/{idSesion}/mensajes")
    public ResponseEntity<List<ChatMensajeDTO>> obtenerMensajesPorSesion(@PathVariable Integer idSesion) {
        return ResponseEntity.ok(chatService.obtenerMensajesPorSesion(idSesion));
    }
    
    // Nuevos endpoints para soporte de chats por curso
    
    @GetMapping("/sesiones/curso/{idCurso}")
    public ResponseEntity<List<ChatSesionDTO>> obtenerSesionesPorCurso(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(chatService.obtenerSesionesPorCurso(idCurso));
    }
    
    @GetMapping("/sesiones/alumno/{idAlumno}")
    public ResponseEntity<List<ChatSesionDTO>> obtenerSesionesPorAlumno(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(chatService.obtenerSesionesPorAlumno(idAlumno));
    }
    
    @GetMapping("/sesiones/docente/{idDocente}/alumno/{idAlumno}/ultima")
    public ResponseEntity<ChatSesionDTO> obtenerUltimaSesion(@PathVariable Integer idDocente, @PathVariable Integer idAlumno) {
        ChatSesionDTO sesion = chatService.obtenerUltimaSesion(idDocente, idAlumno);
        if (sesion != null) {
            return ResponseEntity.ok(sesion);
        } else {
            CrearChatSesionRequest request = new CrearChatSesionRequest(
                idDocente,
                idAlumno,
                null,
                null,
                true
            );
            ChatSesionDTO nuevaSesion = chatService.crearSesion(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSesion);
        }
    }
}

