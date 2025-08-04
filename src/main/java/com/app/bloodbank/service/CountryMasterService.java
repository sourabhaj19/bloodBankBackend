package com.app.bloodbank.service;

import com.app.bloodbank.criteria.CountryMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.exception.CustomException;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.repository.CountryMasterRepository;
import com.app.bloodbank.repository.StateMasterRepository;
import com.app.bloodbank.repository.UserRepository;
import com.app.bloodbank.specifications.CountryMasterQueryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CountryMasterService {
    @Autowired
    private StateMasterRepository stateMasterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryMasterRepository countryMasterRepository;

    @Autowired
    private CountryMasterQueryService countryMasterQueryService;
    public PagedResponse<CountryMaster> getAllCountries(CountryMasterCriteria criteria, Pageable pageable) {
        Page<CountryMaster> page = countryMasterQueryService.findByCriteria(criteria, pageable);
        return new PagedResponse<>(page);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteCountry(Long id) {
        // 1. Find country
        CountryMaster country = countryMasterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Country not found"));

        // 2. Check usage
        if (userRepository.existsByCountry(country.getName())) {
            throw new CustomException("Country is in use by users", HttpStatus.CONFLICT );
        }

        if (stateMasterRepository.existsByCountryId(country.getId())) {
            throw new CustomException("Country is used in state", HttpStatus.CONFLICT);
        }

        // 3. Delete
        countryMasterRepository.delete(country);
        log.info("Country {} deleted successfully", id);

        return ResponseEntity.ok(Map.of(
                "message", "Country deleted successfully",
                "countryId", id.toString()
        ));
    }

    public CountryMaster createCountry(CountryMaster country){
        return countryMasterRepository.save(country);
    }

    public CountryMaster updateCountry(CountryMaster country){
        Optional<CountryMaster> countryOptional = countryMasterRepository.findById(country.getId());
        if(!countryOptional.isPresent()){
                throw new CustomException("Country not found", HttpStatus.NOT_FOUND);
        }
        return countryMasterRepository.save(country);
    }
}
