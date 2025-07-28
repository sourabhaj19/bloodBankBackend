package com.app.bloodbank.repository;

import com.app.bloodbank.model.CountryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryMasterRepository extends JpaRepository<CountryMaster, Long>, JpaSpecificationExecutor<CountryMaster> {

    @Query(value = "select id from country where LOWER(name)= LOWER(:name) and LOWER(code) = LOWER(:code)", nativeQuery = true)
    Long findIdByNameIgnoreCaseAndCodeIgnoreCase(@Param("name") String name, @Param("code")String code);

}
