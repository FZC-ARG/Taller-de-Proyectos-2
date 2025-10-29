package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.Curso;
import com.appmartin.desmartin.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByDocente(Docente docente);
    List<Curso> findByDocente_IdDocente(int idDocente);
}
