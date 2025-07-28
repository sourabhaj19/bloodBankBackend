package com.app.bloodbank.criteria;

import lombok.Getter;
import lombok.Setter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

@Getter
@Setter
public class CountryMasterCriteria {
    private LongFilter id;
    private StringFilter name;
    private StringFilter code;
}
