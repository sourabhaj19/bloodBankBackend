package com.app.bloodbank.service;

import com.app.bloodbank.config.EmailService;
import com.app.bloodbank.dto.ChangePasswordDTO;
import com.app.bloodbank.dto.Response;
import com.app.bloodbank.exception.CustomException;
import com.app.bloodbank.model.CityMaster;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.repository.CityMasterRepository;
import com.app.bloodbank.repository.CountryMasterRepository;
import com.app.bloodbank.repository.StateMasterRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.app.bloodbank.criteria.UserCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.Users;
import com.app.bloodbank.repository.UserRepository;
import com.app.bloodbank.security.JwtService;
import com.app.bloodbank.specifications.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private CountryMasterRepository countryMasterRepository;

    @Autowired
    private StateMasterRepository stateMasterRepository;

    @Autowired
    private CityMasterRepository cityMasterRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private EmailService emailService;


    public PagedResponse<Users> getUsers(UserCriteria criteria, Pageable pageable) {
        Page<Users> page = userQueryService.findByCriteria(criteria, pageable);
        return new PagedResponse<>(page);
    }

    public Users updateUsers(Users user) {
        Users existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(existing.getPassword()); // preserve
        return userRepository.save(user);
    }

    public String deletUser(Long id) {
        userRepository.deleteById(id);
        return "User Deleted Successfully!";
    }


    public List<String> getAvailableBlood() {
        return userRepository.findDistinctBloodGroups();
    }

    public ResponseEntity<Users> postUser(Users user) {
        if(StringUtils.isBlank(user.getFullName())){
            throw new CustomException("Full name must not be blank", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // ✅ 1. Country
        Long countryId = countryMasterRepository.findIdByNameIgnoreCaseAndCodeIgnoreCase(user.getCountry(), user.getCountryCode());
        CountryMaster country;
        if (countryId == null) {
            country = new CountryMaster(user.getCountry(), user.getCountryCode());
            countryMasterRepository.save(country);
        } else {
            country = countryMasterRepository.findById(countryId).orElseThrow();
        }

// ✅ 2. State
        Long stateId = stateMasterRepository.findIdByNameIgnoreCaseAndCountryId(user.getState(), country.getId());
        StateMaster state;
        if (stateId == null) {
            state = new StateMaster(user.getState(), country);
            stateMasterRepository.save(state);
        } else {
            state = stateMasterRepository.findById(stateId).orElseThrow();
        }

// ✅ 3. City
        Long cityId = cityMasterRepository.findIdByNameIgnoreCaseAndStateId(user.getCity(), state.getId());
        CityMaster city;
        if (cityId == null) {
            city = new CityMaster(user.getCity(), state);
            cityMasterRepository.save(city);
        } else {
            city = cityMasterRepository.findById(cityId).orElseThrow();
        }

        try{
            Map<String, Object> variables = new HashMap<>();
            variables.put("subject", "Welcome to Our Platform");
            variables.put("title", "Welcome to Our Platform");
            variables.put("name", user.getFullName());
            variables.put("username", user.getEmail());
            variables.put("registrationDate", new Date());
            variables.put("message", "Thank you for joining our community!");

            emailService.sendTemplateEmail(
                    user.getEmail(),
                    "Welcome to Our Platform",
                    "email/welcome-email", // Template path (without .html)
                    variables
            );
            return ResponseEntity.ok(userRepository.save(user));

        } catch (MessagingException e) {
            throw new CustomException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> login(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(userDetails);
        Optional<Users> user = userRepository.findByEmail(email);


        if(!user.get().getIsActive()){
            throw new CustomException("User is not active, Enable to login", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", user.orElse(null)
        ));
    }

    public ResponseEntity<Users> getUser(Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Response<String>> changePassword(ChangePasswordDTO payload) {
        // Create response object
        Response<String> response = new Response<>();

        try {
            // Input validation
            if (payload.getNewPassword() == null || payload.getNewPassword().trim().isEmpty()) {
                response.setSuccess(false);
                response.setMessage("New password cannot be empty");
                return ResponseEntity.badRequest().body(response);
            }

            Users user = userRepository.findById(payload.getUserId())
                    .orElseThrow(() -> {
                        response.setSuccess(false);
                        response.setMessage("User not found");
                        return new EntityNotFoundException("User not found");
                    });

            if (!passwordEncoder.matches(payload.getOldPassword(), user.getPassword())) {
                response.setSuccess(false);
                response.setMessage("Current password is incorrect");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (passwordEncoder.matches(payload.getNewPassword(), user.getPassword())) {
                response.setSuccess(false);
                response.setMessage("New password must be different from current password");
                return ResponseEntity.badRequest().body(response);
            }

            // Update password
            user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
            userRepository.save(user);

            // Success response
            response.setSuccess(true);
            response.setMessage("Password changed successfully");
            response.setData(null); // or you could return some user data if needed
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log the exception
            response.setSuccess(false);
            response.setMessage("An error occurred while changing password");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}