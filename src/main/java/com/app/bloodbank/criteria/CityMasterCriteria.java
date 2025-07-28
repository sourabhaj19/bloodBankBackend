package com.app.bloodbank.criteria;

import lombok.Getter;
import lombok.Setter;
import tech.jhipster.service.filter.*;
import java.io.Serializable;

@Getter
@Setter
public class CityMasterCriteria implements Serializable {
    private LongFilter id;
    private StringFilter name;
    private LongFilter stateId;

}