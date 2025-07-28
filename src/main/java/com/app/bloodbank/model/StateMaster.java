package com.app.bloodbank.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "state", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "country_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class StateMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnoreProperties(value = {"states"}) // Add this to break cycle
    private CountryMaster country;
    public StateMaster(String name, CountryMaster country) {
        this.name = name;
        this.country = country;
    }
}