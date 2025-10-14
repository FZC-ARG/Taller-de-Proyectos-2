package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.PasswordResetRequest;
import com.prsanmartin.appmartin.service.PasswordService;
import com.prsanmartin.appmartin.service.UsuarioService; // <- corregido
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.prsanmartin.appmartin.dto.TeacherUpdateRequest;

@RestController
@RequestMapping("/api/admin/users")
// TEMPORAL: Desactivar autenticación para pruebas
// @PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final PasswordService passwordService;
    private final UsuarioService usuarioService; // <- corregido

    public AdminUserController(PasswordService passwordService, UsuarioService usuarioService) {
        this.passwordService = passwordService;
        this.usuarioService = usuarioService;
    }

    @PutMapping("/reset-password/{userId}")
    public ResponseEntity<?> resetUserPassword(
            @PathVariable Long userId,
            @RequestBody PasswordResetRequest request) {

        String validationError = passwordService.validatePasswordStrength(request.getNewPassword());
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
        }

        try {
            usuarioService.resetPassword(userId, request.getNewPassword()); // <- uso corregido
            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }

    @PutMapping("/update-teacher/{teacherId}")
    public ResponseEntity<?> updateTeacherCredentials(
            @PathVariable Long teacherId,
            @RequestBody TeacherUpdateRequest request) {
        try {
            usuarioService.updateTeacherCredentials(teacherId, request); // <- uso corregido
            return ResponseEntity.ok("Credenciales del docente actualizadas exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar credenciales: " + e.getMessage());
        }
    }
}
