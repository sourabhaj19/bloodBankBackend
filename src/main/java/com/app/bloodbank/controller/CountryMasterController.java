package com.app.bloodbank.controller;

import com.app.bloodbank.criteria.CountryMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.service.CountryMasterService;
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
@RequestMapping("/api/country")
public class CountryMasterController {

    private final CountryMasterService countryMasterService;

    // ✅ Constructor injection to properly initialize the service
    public CountryMasterController(CountryMasterService countryMasterService) {
        this.countryMasterService = countryMasterService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CountryMaster>> getCountries(
            @Parameter(description = "Filter criteria") @Valid CountryMasterCriteria criteria,
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok().body(countryMasterService.getAllCountries(criteria, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCountry(@PathVariable("id") Long id) {
        countryMasterService.deleteCountry(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Country Deleted Successfully!");

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CountryMaster> deleteCountry(@Validated @RequestBody CountryMaster country) {
        return ResponseEntity.ok(countryMasterService.updateCountry(country));
    }

    @PostMapping
    public ResponseEntity<CountryMaster> createCountry(@Validated @RequestBody CountryMaster country) {
        return ResponseEntity.ok(countryMasterService.createCountry(country));
    }
}
