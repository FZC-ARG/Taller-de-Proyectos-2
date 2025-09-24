package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.entity.AccesoUsuario;
import com.prsanmartin.appmartin.repository.AccesoUsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AccesoUsuarioServiceImpl implements AccesoUsuarioService {

    private final AccesoUsuarioRepository accesoUsuarioRepository;

    public AccesoUsuarioServiceImpl(AccesoUsuarioRepository accesoUsuarioRepository) {
        this.accesoUsuarioRepository = accesoUsuarioRepository;
    }

    @Override
    public Page<AccesoUsuario> listar(Pageable pageable, Integer idUsuario, String rolUsuario, String resultado) {
        Specification<AccesoUsuario> spec = (root, query, cb) -> cb.conjunction();

        if (idUsuario != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("idUsuario"), idUsuario));
        }
        if (rolUsuario != null && !rolUsuario.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rolUsuario"), rolUsuario));
        }
        if (resultado != null && !resultado.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("resultado"), resultado));
        }

        return accesoUsuarioRepository.findAll(spec, pageable);
    }
}


