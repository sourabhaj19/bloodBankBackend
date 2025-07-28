package com.app.bloodbank.controller;

import com.app.bloodbank.criteria.CityMasterCriteria;
import com.app.bloodbank.criteria.StateMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.CityMaster;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.service.CityMasterService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/city")
public class CityMasterController {

    private final CityMasterService cityMasterService;

    public CityMasterController(CityMasterService cityMasterService) {
        this.cityMasterService = cityMasterService ;
    }

//    @GetMapping()
//    public ResponseEntity<PagedResponse<CityMaster>> getCities(
//            @Parameter(description = "Filter criteria") @Valid CityMasterCriteria criteria,
//            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20) Pageable pageable) {
//        return ResponseEntity.ok(cityMasterService.getCities(criteria, pageable));
//    }

    @GetMapping
    public ResponseEntity<PagedResponse<CityMaster>> getCities(
            @Parameter(description = "Filter criteria") @Valid CityMasterCriteria criteria,
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok().body(cityMasterService.getCities(criteria, pageable));
    }
}
