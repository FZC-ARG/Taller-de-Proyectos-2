package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.AlumnoCurso;
import com.appmartin.desmartin.model.AlumnoCursoId;
import com.appmartin.desmartin.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlumnoCursoRepository extends JpaRepository<AlumnoCurso, AlumnoCursoId> {
    List<AlumnoCurso> findByCurso(Curso curso);
}
