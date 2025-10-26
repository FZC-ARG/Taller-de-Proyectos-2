package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.PreguntaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreguntaTestRepository extends JpaRepository<PreguntaTest, Integer> {
}

