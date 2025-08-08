package com.app.bloodbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    @NotBlank
    @Size(min = 4, max = 4, message = "OTP must be 4 digits")
    private String otp;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    // Getters and Setters
}