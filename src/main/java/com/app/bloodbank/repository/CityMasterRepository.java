package com.app.bloodbank.repository;

import com.app.bloodbank.model.CityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityMasterRepository extends JpaRepository<CityMaster, Long>, JpaSpecificationExecutor<CityMaster>{
    @Query(value = "select id from city where LOWER(name)= LOWER(:name) and LOWER(state_id) = LOWER(:stateId)", nativeQuery = true)
    Long findIdByNameIgnoreCaseAndStateId(String name, Long stateId);
}
