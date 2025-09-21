package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByNombreRol(String nombreRol);
}

