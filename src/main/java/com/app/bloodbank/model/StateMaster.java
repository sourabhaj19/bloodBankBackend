package com.app.bloodbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "state", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "country_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StateMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "State name is required")
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryMaster country;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CityMaster> cities;

    public StateMaster(String name, CountryMaster country) {
        this.name = name;
        this.country = country;
    }

    // Helper method to add city
    public void addCity(CityMaster city) {
        cities.add(city);
        city.setState(this);
    }

    // Helper method to remove city
    public void removeCity(CityMaster city) {
        cities.remove(city);
        city.setState(null);
    }

    // Helper method to get country code
    public String getCountryCode() {
        return country != null ? country.getCode() : null;
    }
}