package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping("/preguntas")
    public ResponseEntity<List<PreguntaDTO>> obtenerPreguntas() {
        return ResponseEntity.ok(testService.obtenerPreguntas());
    }
    
    @PostMapping("/completar")
    public ResponseEntity<String> completarTest(@RequestBody CompletarTestRequest request) {
        testService.completarTest(request);
        return ResponseEntity.ok("Test completado exitosamente");
    }
    
    @PostMapping("/resultados")
    public ResponseEntity<String> crearResultados(@RequestBody CrearResultadosRequest request) {
        testService.crearResultados(request);
        return ResponseEntity.ok("Resultados guardados exitosamente");
    }
}

