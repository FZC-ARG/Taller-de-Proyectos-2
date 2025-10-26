package com.appmartin.desmartin.repository;

import com.appmartin.desmartin.model.LogAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAccesoRepository extends JpaRepository<LogAcceso, Integer> {
}

