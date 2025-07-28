package com.app.bloodbank.specifications;

import com.app.bloodbank.criteria.StateMasterCriteria;
import com.app.bloodbank.model.StateMaster_;
import com.app.bloodbank.model.StateMaster;
import com.app.bloodbank.repository.StateMasterRepository;
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
public class StateMasterQueryService extends QueryService<StateMaster> {

    private final StateMasterRepository stateMasterRepository;

    public StateMasterQueryService(StateMasterRepository stateMasterRepository) {
        this.stateMasterRepository = stateMasterRepository;
    }
    public Page<StateMaster> findByCriteria(StateMasterCriteria criteria, Pageable pageable) {
        final Specification<StateMaster> specification = createSpecification(criteria);
        return stateMasterRepository.findAll(specification, pageable);
    }

    protected Specification<StateMaster> createSpecification(StateMasterCriteria criteria) {
        Specification<StateMaster> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StateMaster_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), StateMaster_.name));
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                        buildSpecification(criteria.getCountryId(), root -> root.join("country").get("id")));
            }
        }

        return specification;
    }

}
