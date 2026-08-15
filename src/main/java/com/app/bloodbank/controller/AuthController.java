package com.app.bloodbank.controller;

import com.app.bloodbank.dto.ForgotPasswordRequestDto;
import com.app.bloodbank.dto.ResetPasswordDto;
import com.app.bloodbank.dto.Response;
import com.app.bloodbank.dto.VerifyOtpDto;
import com.app.bloodbank.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Response> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        authService.generateOtp(request);
        Response response = new Response();
        response.setMessage("OTP sent to your email!");
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto request) throws BadRequestException {
        // First verify OTP is valid
        VerifyOtpDto verifyOtpDto = new VerifyOtpDto();
        verifyOtpDto.setOtp(request.getOtp());
        verifyOtpDto.setEmail(request.getOtp());

        authService.verifyOtp(verifyOtpDto);

        // Then proceed with password reset
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpDto verifyOtpDto) throws BadRequestException {
        authService.verifyOtp(verifyOtpDto);
        return ResponseEntity.ok("OTP verified successfully");
    }
}