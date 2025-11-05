package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AlumnoCursoRepository alumnoCursoRepository;

    // Crear curso
    public CursoDTO crearCurso(CrearCursoRequest request) {
        if (request.getIdDocente() == null) {
            throw new IllegalArgumentException("El idDocente no puede ser nulo");
        }
        Docente docente = docenteRepository.findById(request.getIdDocente())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado con ID: " + request.getIdDocente()));

        Curso curso = new Curso();
        curso.setNombreCurso(request.getNombreCurso());
        curso.setDescripcion(request.getDescripcion());
        curso.setDocente(docente);

        Curso saved = cursoRepository.save(curso);
        return new CursoDTO(saved.getIdCurso(), saved.getNombreCurso(), saved.getDescripcion(), docente.getNombreUsuario());
    }

    // Actualizar curso
    public CursoDTO actualizarCurso(Integer id, CrearCursoRequest request) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        curso.setNombreCurso(request.getNombreCurso());
        curso.setDescripcion(request.getDescripcion());

        if (request.getIdDocente() != null) {
            Docente docente = docenteRepository.findById(request.getIdDocente())
                    .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
            curso.setDocente(docente);
        }

        Curso saved = cursoRepository.save(curso);
        return new CursoDTO(saved.getIdCurso(), saved.getNombreCurso(), saved.getDescripcion(), saved.getDocente().getNombreUsuario());
    }

    // Eliminar curso
    public void eliminarCurso(Integer id) {
        cursoRepository.deleteById(id);
    }

    // Listar todos los cursos
    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(c -> new CursoDTO(c.getIdCurso(), c.getNombreCurso(), c.getDescripcion(), c.getDocente().getNombreUsuario()))
                .collect(Collectors.toList());
    }

    // Listar cursos por docente
    public List<CursoDTO> listarCursosPorDocente(Integer idDocente) {
        Docente docente = docenteRepository.findById(idDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        return cursoRepository.findByDocente(docente).stream()
                .map(c -> new CursoDTO(c.getIdCurso(), c.getNombreCurso(), c.getDescripcion(), docente.getNombreUsuario()))
                .collect(Collectors.toList());
    }

    // Matricular alumno
    public void matricularAlumno(MatricularAlumnoRequest request) {
        if (request.getIdAlumnoFk() == null || request.getIdCursoFk() == null) {
            throw new IllegalArgumentException("El idAlumnoFk y el idCursoFk no pueden ser nulos");
        }

        Alumno alumno = alumnoRepository.findById(request.getIdAlumnoFk())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + request.getIdAlumnoFk()));

        Curso curso = cursoRepository.findById(request.getIdCursoFk())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + request.getIdCursoFk()));

        AlumnoCurso matricula = new AlumnoCurso(alumno, curso, null);
        alumnoCursoRepository.save(matricula);
    }


    // Listar alumnos por curso
    public List<String> listarAlumnosPorCurso(Integer idCurso) {
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        return alumnoCursoRepository.findByCurso(curso).stream()
                .map(ac -> ac.getAlumno().getNombreUsuario())
                .collect(Collectors.toList());
    }

    // Listar cursos por alumno (cursos en los que está matriculado)
    public List<CursoDTO> listarCursosPorAlumno(Integer idAlumno) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        return alumnoCursoRepository.findByAlumno(alumno).stream()
                .map(ac -> {
                    Curso curso = ac.getCurso();
                    return new CursoDTO(curso.getIdCurso(), curso.getNombreCurso(), curso.getDescripcion(), curso.getDocente().getNombreUsuario());
                })
                .collect(Collectors.toList());
    }

    // Listar alumnos por docente (alumnos inscritos en cursos que dicta el docente)
    public List<String> listarAlumnosPorDocente(Integer idDocente) {
        Docente docente = docenteRepository.findById(idDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        List<Curso> cursos = cursoRepository.findByDocente(docente);
        
        return cursos.stream()
                .flatMap(curso -> alumnoCursoRepository.findByCurso(curso).stream())
                .map(ac -> ac.getAlumno().getNombreUsuario())
                .distinct()
                .collect(Collectors.toList());
    }
}
