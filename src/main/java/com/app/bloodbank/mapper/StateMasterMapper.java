package com.app.bloodbank.mapper;

import com.app.bloodbank.dto.StateMasterDTO;
import com.app.bloodbank.model.StateMaster;
import org.springframework.stereotype.Component;

@Component
public class StateMasterMapper {

    public StateMasterDTO toDto(StateMaster state) {
        StateMasterDTO dto = new StateMasterDTO();
        dto.setId(state.getId());
        dto.setName(state.getName());

        if (state.getCountry() != null) {
            dto.setCountryId(state.getCountry().getId());
            dto.setCountryCode(state.getCountryCode());
        }

        return dto;
    }
}