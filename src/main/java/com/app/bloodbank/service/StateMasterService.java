package com.app.bloodbank.service;

import com.app.bloodbank.criteria.StateMasterCriteria;
import com.app.bloodbank.dto.PagedResponse;
import com.app.bloodbank.dto.StateMasterDTO;
import com.app.bloodbank.mapper.StateMasterMapper;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.specifications.StateMasterQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class StateMasterService {

    @Autowired
    private StateMasterQueryService stateMasterQueryService;

    @Autowired
    private StateMasterMapper stateMasterMapper;

    public PagedResponse<StateMasterDTO> getAllStates(StateMasterCriteria criteria, Pageable pageable) {
        Page<StateMaster> page = stateMasterQueryService.findByCriteria(criteria, pageable);

        // Convert Page<StateMaster> to Page<StateMasterDTO>
        Page<StateMasterDTO> dtoPage = page.map(stateMasterMapper::toDto);

        return new PagedResponse<>(dtoPage);
    }
}