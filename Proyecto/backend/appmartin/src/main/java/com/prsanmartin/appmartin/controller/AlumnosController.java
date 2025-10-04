package com.prsanmartin.appmartin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alumnos")
public class AlumnosController {
    // private final List<Docente> docentes = new ArrayList<>();
    private final List<Alumno> alumnos = new ArrayList<>();
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
