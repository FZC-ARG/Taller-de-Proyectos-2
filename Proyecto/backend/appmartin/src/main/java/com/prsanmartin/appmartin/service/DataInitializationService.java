package com.prsanmartin.appmartin.service;

import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        // Crear roles básicos si no existen
        if (rolRepository.count() == 0) {
            Rol adminRol = new Rol();
            adminRol.setNombreRol("ADMIN");
            rolRepository.save(adminRol);

            Rol docenteRol = new Rol();
            docenteRol.setNombreRol("DOCENTE");
            rolRepository.save(docenteRol);

            Rol alumnoRol = new Rol();
            alumnoRol.setNombreRol("ALUMNO");
            rolRepository.save(alumnoRol);

            System.out.println("Roles inicializados: ADMIN, DOCENTE, ALUMNO");
        } else {
            System.out.println("Roles ya existen en la base de datos");
        }
    }
}
