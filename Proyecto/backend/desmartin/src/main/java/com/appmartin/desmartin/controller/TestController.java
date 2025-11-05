package com.appmartin.desmartin.controller;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controlador de los resultados del test
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
        try {
            testService.crearResultados(request);
            return ResponseEntity.ok("Resultados guardados exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/resultados/alumno/{idAlumno}")
    public ResponseEntity<CrearResultadosRequest> obtenerResultadosPorAlumno(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(testService.obtenerResultadosPorAlumno(idAlumno));
    }
}

