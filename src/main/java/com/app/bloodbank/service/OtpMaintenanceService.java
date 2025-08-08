package com.app.bloodbank.service;

import com.app.bloodbank.repository.PasswordResetOtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpMaintenanceService {

    private final PasswordResetOtpRepository otpRepository;

    @Scheduled(cron = "0 */30 * * * *") // Every 30 minutes
    @Transactional
    public void cleanupExpiredOtps() {
        otpRepository.deleteAllByExpiryTimeBeforeAndUsedFalse(LocalDateTime.now());
    }
}