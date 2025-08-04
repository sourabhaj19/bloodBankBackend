package com.app.bloodbank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be valid (example: user@example.com)"
    )
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password = "User@1234";

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    @Column(nullable = false, length = 10)
    private String phone;

    @NotBlank(message = "Phone prefix is required")
    @Pattern(regexp = "^\\+[0-9]{1,3}$", message = "Phone prefix must be in format '+XX'")
    @Column(nullable = false)
    private String phonePrefix;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    @Column(nullable = false)
    private Integer age;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dob;

    @NotBlank(message = "Gender is required")
    @Column(nullable = false)
    private String gender;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "Country is required")
    @Column(nullable = false)
    private String country;


    @NotBlank(message = "countryCode is required")
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank(message = "Blood group is required")
    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @NotNull(message = "Latitude is required")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Column(nullable = false)
    private Double longitude;

    @Column(name = "last_donation_date")
    private LocalDate lastDonationDate;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}