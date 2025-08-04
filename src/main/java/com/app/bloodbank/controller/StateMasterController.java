package com.app.bloodbank.controller;

import com.app.bloodbank.criteria.StateMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.service.StateMasterService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/state")
public class StateMasterController {

    private final StateMasterService stateMasterService;

    public StateMasterController(StateMasterService stateMasterService){
        this.stateMasterService = stateMasterService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<StateMaster>> getStates(
            @Parameter(description = "Filter criteria") @Valid StateMasterCriteria criteria,
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok().body(stateMasterService.getAllStates(criteria, pageable));
    }

    @PostMapping
    public ResponseEntity<StateMaster> createStates(@Validated @RequestBody StateMaster stateMaster){
        return stateMasterService.createState(stateMaster);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteState(@PathVariable("id") Long id) {
        stateMasterService.deleteState(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "State Deleted Successfully!");

        return ResponseEntity.ok(response);
    }
}