package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.entity.AccesoUsuario;
import com.prsanmartin.appmartin.service.AccesoUsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accesos")
@CrossOrigin(origins = "*")
public class AccesosUsuariosController {

    private final AccesoUsuarioService accesoUsuarioService;

    public AccesosUsuariosController(AccesoUsuarioService accesoUsuarioService) {
        this.accesoUsuarioService = accesoUsuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AccesoUsuario>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "fechaAcceso") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) String rolUsuario,
            @RequestParam(required = false) String resultado
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AccesoUsuario> pageResult = accesoUsuarioService.listar(pageable, idUsuario, rolUsuario, resultado);
        return ResponseEntity.ok(pageResult);
    }
}


