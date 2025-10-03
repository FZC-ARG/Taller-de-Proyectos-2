package com.prsanmartin.appmartin.service;

import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class PasswordService {
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public boolean isValidPassword(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    public String validatePasswordStrength(String password) {
        if (password.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "La contraseña debe contener al menos una mayúscula";
        }
        if (!password.matches(".*[a-z].*")) {
            return "La contraseña debe contener al menos una minúscula";
        }
        if (!password.matches(".*[0-9].*")) {
            return "La contraseña debe contener al menos un número";
        }
        if (!password.matches(".*[@#$%^&+=].*")) {
            return "La contraseña debe contener al menos un símbolo especial";
        }
        return null;
    }
}
