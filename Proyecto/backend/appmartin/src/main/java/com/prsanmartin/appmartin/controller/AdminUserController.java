package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.PasswordResetRequest;
import com.prsanmartin.appmartin.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.prsanmartin.appmartin.service.UserService;
import com.prsanmartin.appmartin.dto.TeacherUpdateRequest;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    
    private final PasswordService passwordService;
    private final UserService userService;

    public AdminUserController(PasswordService passwordService, UserService userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }

    @PutMapping("/reset-password/{userId}")
    public ResponseEntity<?> resetUserPassword(
            @PathVariable Long userId,
            @RequestBody PasswordResetRequest request) {
        
        // Validar la nueva contraseña
        String validationError = passwordService.validatePasswordStrength(request.getNewPassword());
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        // Verificar que las contraseñas coincidan
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
        }

        try {
            userService.resetPassword(userId, request.getNewPassword());
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
            userService.updateTeacherCredentials(teacherId, request);
            return ResponseEntity.ok("Credenciales del docente actualizadas exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar credenciales: " + e.getMessage());
        }
    }
}
