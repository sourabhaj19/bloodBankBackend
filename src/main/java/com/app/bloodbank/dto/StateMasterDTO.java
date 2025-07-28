package com.app.bloodbank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateMasterDTO {
    private Long id;
    private String name;
    private Long countryId;
    private String countryCode;

    // You can add more fields as needed
}