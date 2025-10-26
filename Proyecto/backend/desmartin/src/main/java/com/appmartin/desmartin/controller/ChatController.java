package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
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
}

