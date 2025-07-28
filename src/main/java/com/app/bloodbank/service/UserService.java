package com.app.bloodbank.service;

import com.app.bloodbank.exception.CustomException;
import com.app.bloodbank.model.CityMaster;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.repository.CityMasterRepository;
import com.app.bloodbank.repository.CountryMasterRepository;
import com.app.bloodbank.repository.StateMasterRepository;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        return ResponseEntity.ok(userRepository.save(user));
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
}