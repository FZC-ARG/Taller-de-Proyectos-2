package com.prsanmartin.appmartin;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/docentes")
public class DocentesController {
    private List<Docente> docentes = new ArrayList<>();
    private Long nextId = 1L;

    @GetMapping
    public List<Docente> listarDocentes() {
        return docentes;
    }

    @GetMapping("/{id}")
    public Docente obtenerDocente(@PathVariable Long id) {
        return docentes.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public Docente crearDocente(@RequestBody Docente docente) {
        docente.setId(nextId++);
        docentes.add(docente);
        return docente;
    }

    @PutMapping("/{id}")
    public Docente actualizarDocente(@PathVariable Long id, @RequestBody Docente docente) {
        for (int i = 0; i < docentes.size(); i++) {
            if (docentes.get(i).getId().equals(id)) {
                docente.setId(id);
                docentes.set(i, docente);
                return docente;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String eliminarDocente(@PathVariable Long id) {
        boolean removed = docentes.removeIf(d -> d.getId().equals(id));
        return removed ? "Docente eliminado" : "Docente no encontrado";
    }
}
