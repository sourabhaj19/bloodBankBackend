package com.app.bloodbank.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@Entity
@Table(name = "password_reset_otp")
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String otp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private boolean used = false;

    // Constructor
    public PasswordResetOtp() {
        this.expiryTime = LocalDateTime.now().plusMinutes(15); // 15 minutes expiry
    }

    public PasswordResetOtp(Users user) {
        this();
        this.user = user;
        this.otp = generateRandomOtp();
    }

    private String generateRandomOtp() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

}