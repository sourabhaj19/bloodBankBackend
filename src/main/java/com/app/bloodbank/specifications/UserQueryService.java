package com.app.bloodbank.specifications;

import com.app.bloodbank.criteria.UserCriteria;
import com.app.bloodbank.model.Users;
import com.app.bloodbank.model.Users_;
import com.app.bloodbank.repository.UserRepository;
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
public class UserQueryService extends QueryService<Users> {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<Users> findByCriteria(UserCriteria criteria, Pageable pageable) {
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        final Specification<Users> specification = createSpecification(criteria);
        return userRepository.findAll(specification, pageable);
    }

    protected Specification<Users> createSpecification(UserCriteria criteria) {
        Specification<Users> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Users_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Users_.fullName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Users_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildSpecification(criteria.getPhone(), Users_.phone));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), Users_.age));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGender(), Users_.gender));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Users_.state));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Users_.city));
            }
            if (criteria.getBloodGroup() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBloodGroup(), Users_.bloodGroup));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildSpecification(criteria.getRole(), Users_.role));
            }
            if (criteria.getIsAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAvailable(), Users_.isAvailable));
            }
            if (criteria.getIsAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAvailable(), Users_.isActive));
            }
            if (criteria.getLastDonationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastDonationDate(), Users_.lastDonationDate));
            }
        }

        return specification;
    }
}