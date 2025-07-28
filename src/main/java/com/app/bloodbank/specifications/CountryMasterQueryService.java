package com.app.bloodbank.specifications;

import com.app.bloodbank.criteria.CountryMasterCriteria;
import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.model.CountryMaster_;
import com.app.bloodbank.repository.CountryMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
@Slf4j
@Service
@Transactional(readOnly = true)
public class CountryMasterQueryService extends QueryService<CountryMaster> {
    private final CountryMasterRepository countryMasterRepository;

    public CountryMasterQueryService(CountryMasterRepository countryMasterRepository) {
        this.countryMasterRepository = countryMasterRepository;
    }

    public Page<CountryMaster> findByCriteria(CountryMasterCriteria criteria, Pageable pageable) {
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        final Specification<CountryMaster> specification = createSpecification(criteria);
        return countryMasterRepository.findAll(specification, pageable);
    }

    protected Specification<CountryMaster> createSpecification(CountryMasterCriteria criteria) {
        Specification<CountryMaster> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CountryMaster_.id));
            }

        }

        return specification;
    }
}
