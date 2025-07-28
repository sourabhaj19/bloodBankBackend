package com.app.bloodbank.service;

import com.app.bloodbank.criteria.CityMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.model.CityMaster;
import com.app.bloodbank.specifications.CityMasterQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class CityMasterService {

    @Autowired
    private CityMasterQueryService cityQueryService;
    public PagedResponse<CityMaster> getCities(CityMasterCriteria criteria, Pageable pageable) {
        Page<CityMaster> page = cityQueryService.findByCriteria(criteria, pageable);
        return new PagedResponse<>(page);
    }
}
