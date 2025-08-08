package com.app.bloodbank.repository;

import com.app.bloodbank.model.PasswordResetOtp;
import com.app.bloodbank.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findByOtpAndUsedFalse(String otp);
    List<PasswordResetOtp> findByUserAndUsedFalse(Users user);

    @Modifying
    @Query("DELETE FROM PasswordResetOtp o WHERE o.expiryTime < :now AND o.used = false")
    void deleteAllByExpiryTimeBeforeAndUsedFalse(@Param("now") LocalDateTime now);
}