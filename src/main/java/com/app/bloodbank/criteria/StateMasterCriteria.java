package com.app.bloodbank.criteria;

import lombok.Getter;
import lombok.Setter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
@Getter
@Setter
public class StateMasterCriteria {
    private LongFilter id;
    private StringFilter name;

    private LongFilter countryId;
}
