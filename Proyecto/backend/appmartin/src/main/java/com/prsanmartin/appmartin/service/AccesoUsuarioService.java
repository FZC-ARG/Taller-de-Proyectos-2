package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.entity.AccesoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccesoUsuarioService {
    Page<AccesoUsuario> listar(Pageable pageable, Integer idUsuario, String rolUsuario, String resultado);
}


