package com.app.bloodbank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDonorDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String phonePrefix;
    private String bloodGroup;
    private Double latitude;
    private Double longitude;

    @JsonProperty("distance")
    private Double distanceKm;

    @JsonProperty("distance_unit")
    private String distanceUnit;
}
