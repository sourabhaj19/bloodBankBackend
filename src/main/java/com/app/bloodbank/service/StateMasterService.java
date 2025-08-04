package com.app.bloodbank.service;

import com.app.bloodbank.criteria.StateMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.exception.CustomException;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.repository.CityMasterRepository;
import com.app.bloodbank.repository.StateMasterRepository;
import com.app.bloodbank.repository.UserRepository;
import com.app.bloodbank.specifications.StateMasterQueryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StateMasterService {

    @Autowired
    private StateMasterQueryService stateMasterQueryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StateMasterRepository stateMasterRepository;
    @Autowired
    private CityMasterRepository cityMasterRepository;


    public PagedResponse<StateMaster> getAllStates(StateMasterCriteria criteria, Pageable pageable) {
        Page<StateMaster> page = stateMasterQueryService.findByCriteria(criteria, pageable);
        return new PagedResponse<>(page);
    }

    public ResponseEntity<StateMaster> createState(StateMaster state) {
        return ResponseEntity.ok(stateMasterRepository.save(state));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteState(Long id) {
        // 1. Find country
        StateMaster state = stateMasterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "State not found"));

        // 2. Check usage
        if (userRepository.existsByState(state.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "State is in use by users");
        }


        if (cityMasterRepository.existsByStateId(state.getId())) {
            throw new CustomException("State is used in city", HttpStatus.CONFLICT);
        }

        // 3. Delete
        stateMasterRepository.delete(state);
        return ResponseEntity.ok(Map.of(
                "message", "State deleted successfully",
                "StateId", id.toString()
        ));
    }

}