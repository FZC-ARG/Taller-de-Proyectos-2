package com.prsanmartin.appmartin;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/alumnos")
public class AlumnosController {
    private List<Alumno> alumnos = new ArrayList<>();
    private Long nextId = 1L;

    @GetMapping
    public List<Alumno> listarAlumnos() {
        return alumnos;
    }

    @GetMapping("/{id}")
    public Alumno obtenerAlumno(@PathVariable Long id) {
        return alumnos.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    public Alumno crearAlumno(@RequestBody Alumno alumno) {
        alumno.setId(nextId++);
        alumnos.add(alumno);
        return alumno;
    }

    @PutMapping("/{id}")
    public Alumno actualizarAlumno(@PathVariable Long id, @RequestBody Alumno alumno) {
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getId().equals(id)) {
                alumno.setId(id);
                alumnos.set(i, alumno);
                return alumno;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String eliminarAlumno(@PathVariable Long id) {
        boolean removed = alumnos.removeIf(a -> a.getId().equals(id));
        return removed ? "Alumno eliminado" : "Alumno no encontrado";
    }
}
