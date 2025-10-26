package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.TipoInteligencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoInteligenciaRepository extends JpaRepository<TipoInteligencia, Integer> {
}

