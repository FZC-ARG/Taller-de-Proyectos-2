package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    // Búsqueda insensible a mayúsculas/minúsculas
    @Query("SELECT r FROM Rol r WHERE UPPER(r.nombreRol) = UPPER(:nombreRol)")
    Rol findByNombreRol(@Param("nombreRol") String nombreRol);
}
