package com.prsanmartin.appmartin.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
