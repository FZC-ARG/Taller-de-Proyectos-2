package com.prsanmartin.appmartin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir la interfaz de pruebas integrada
 * Accesible en: http://localhost:8080/tester
 */
@Controller
public class TesterController {

    @GetMapping("/tester")
    public String tester() {
        return "forward:/tester.html";
    }

    // 🔧 Cambiado para evitar conflicto con "/"
    @GetMapping("/inicio")
    public String index() {
        return "redirect:/tester";
    }
}