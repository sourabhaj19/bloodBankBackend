package com.app.bloodbank.repository;

import com.app.bloodbank.model.CountryMaster;
import com.app.bloodbank.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT u.id, u.full_name, u.email, u.phone, u.phone_prefix, u.latitude, u.longitude, u.blood_group, " +
            "(6371 * acos( cos( radians(:lat) ) * cos( radians(u.latitude) ) * cos( radians(u.longitude) - radians(:lng) ) " +
            "+ sin( radians(:lat) ) * sin( radians(u.latitude) ) ) ) AS distance_km " +
            "FROM users u " +
            "WHERE u.latitude IS NOT NULL AND u.longitude IS NOT NULL " +
            "AND u.is_active = true AND u.is_available = true AND u.role = 'ROLE_USER' " +
            "HAVING distance_km <= :radiusKm " +
            "ORDER BY distance_km",
            nativeQuery = true)
    List<Object[]> findNearbyDonorsRaw(@Param("lat") double lat,
                                      @Param("lng") double lng,
                                      @Param("radiusKm") double radiusKm);
}