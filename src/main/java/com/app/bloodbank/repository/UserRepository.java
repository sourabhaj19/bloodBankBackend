package com.app.bloodbank.repository;

import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> , JpaSpecificationExecutor<Users> {
    Optional<Users> findByEmail(String email);

    @Query("SELECT DISTINCT bloodGroup FROM Users")
    List<String> findDistinctBloodGroups();

    boolean existsByCountry(String countryName);

    boolean existsByCity(String name);

    boolean existsByState(String name);
}