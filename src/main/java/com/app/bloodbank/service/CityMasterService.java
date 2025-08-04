package com.app.bloodbank.service;

import com.app.bloodbank.criteria.CityMasterCriteria;
import com.app.bloodbank.criteria.StateMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.CityMaster;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.repository.CityMasterRepository;
import com.app.bloodbank.repository.UserRepository;
import com.app.bloodbank.specifications.CityMasterQueryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Service
public class CityMasterService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityMasterQueryService cityQueryService;

    @Autowired
    private CityMasterRepository citiMasterRepository;
    public PagedResponse<CityMaster> getCities(CityMasterCriteria criteria, Pageable pageable) {
        Page<CityMaster> page = cityQueryService.findByCriteria(criteria, pageable);
        return new PagedResponse<>(page);
    }

    public ResponseEntity<CityMaster> createCity(CityMaster citymaster){
        return ResponseEntity.ok(citiMasterRepository.save(citymaster));
    }


    @Transactional
    public ResponseEntity<Map<String, String>> deleteCity(Long id) {
        // 1. Find country
        CityMaster city = citiMasterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "City not found"));

        // 2. Check usage
        if (userRepository.existsByCity(city.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "City is in use by users");
        }

        // 3. Delete
        citiMasterRepository.delete(city);
        log.info("City deleted successfully", id);

        return ResponseEntity.ok(Map.of(
                "message", "City deleted successfully",
                "cityId", id.toString()
        ));
    }

}
