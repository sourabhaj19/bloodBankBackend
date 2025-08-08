package com.app.bloodbank.service;

import com.app.bloodbank.config.EmailService;
import com.app.bloodbank.dto.ForgotPasswordRequestDto;
import com.app.bloodbank.dto.ResetPasswordDto;
import com.app.bloodbank.dto.VerifyOtpDto;
import com.app.bloodbank.exception.ResourceNotFoundException;
import com.app.bloodbank.model.PasswordResetOtp;
import com.app.bloodbank.model.Users;
import com.app.bloodbank.repository.PasswordResetOtpRepository;
import com.app.bloodbank.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void generateOtp(ForgotPasswordRequestDto request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Invalidate previous unused OTPs
        otpRepository.findByUserAndUsedFalse(user)
                .forEach(otp -> otp.setUsed(true));
        otpRepository.saveAll(otpRepository.findByUserAndUsedFalse(user));

        // Generate new OTP
        PasswordResetOtp otp = new PasswordResetOtp(user);
        otpRepository.save(otp);

        // Send email
        emailService.sendOtpEmail(user.getEmail(), user.getFullName(), otp.getOtp());
    }

    @Transactional
    public void resetPassword(ResetPasswordDto request) throws BadRequestException {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        PasswordResetOtp otp = otpRepository.findByOtpAndUsedFalse(request.getOtp())
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));

        if (otp.isExpired()) {
            throw new BadRequestException("OTP has expired");
        }

        Users user = otp.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        otp.setUsed(true);
        otpRepository.save(otp);
    }


    public boolean verifyOtp(VerifyOtpDto verifyOtpDto) throws BadRequestException {
        Users user = userRepository.findByEmail(verifyOtpDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PasswordResetOtp otp = otpRepository.findByOtpAndUsedFalse(verifyOtpDto.getOtp())
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));

        if (!otp.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("OTP does not match user");
        }

        if (otp.isExpired()) {
            throw new BadRequestException("OTP has expired");
        }

        return true;
    }
}