package com.app.bloodbank.specifications;
import com.app.bloodbank.criteria.CityMasterCriteria;
import com.app.bloodbank.model.CityMaster_;
import com.app.bloodbank.model.CityMaster;
import com.app.bloodbank.repository.CityMasterRepository;
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
public class CityMasterQueryService extends QueryService<CityMaster> {

    private final CityMasterRepository cityMasterRepository;


    public CityMasterQueryService(CityMasterRepository cityMasterRepository) {
        this.cityMasterRepository = cityMasterRepository;
    }


    public Page<CityMaster> findByCriteria(CityMasterCriteria criteria, Pageable pageable) {
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        final Specification<CityMaster> specification = createSpecification(criteria);
        return cityMasterRepository.findAll(specification, pageable);
    }

    protected Specification<CityMaster> createSpecification(CityMasterCriteria criteria) {
        Specification<CityMaster> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CityMaster_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CityMaster_.name));
            }
            if (criteria.getStateId() != null) {
                specification = specification.and(
                        buildSpecification(criteria.getStateId(), root -> root.join("state").get("id")));
            }
        }

        return specification;
    }
}
// No changes needed if metamodel is generated and annotation processing is enabled.
// If not, replace Cities_.stateId with "stateId", etc.
