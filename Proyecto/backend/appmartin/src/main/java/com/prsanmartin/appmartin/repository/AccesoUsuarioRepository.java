package com.prsanmartin.appmartin.repository;

import com.prsanmartin.appmartin.entity.AccesoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccesoUsuarioRepository extends JpaRepository<AccesoUsuario, Integer>, JpaSpecificationExecutor<AccesoUsuario> {
}


