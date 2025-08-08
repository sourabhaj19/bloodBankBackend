package com.app.bloodbank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpDto {
    @NotBlank
    @Size(min = 4, max = 4, message = "OTP must be 4 digits")
    private String otp;

    @NotBlank
    @Email
    private String email;

    // Getters and Setters
}