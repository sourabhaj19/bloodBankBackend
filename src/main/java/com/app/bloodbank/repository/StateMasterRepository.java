package com.app.bloodbank.repository;

import com.app.bloodbank.model.StateMaster;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StateMasterRepository extends JpaRepository<StateMaster, Long>, JpaSpecificationExecutor<StateMaster> {

    @Query(value = "select id from state where LOWER(name)= LOWER(:name) and LOWER(country_id) = LOWER(:countryId)", nativeQuery = true)
    Long findIdByNameIgnoreCaseAndCountryId(String name, Long countryId);

    boolean existsByCountryId(Long id);
}
