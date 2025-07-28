package com.app.bloodbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@Table(name = "country", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"code"})
})
@NoArgsConstructor
public class CountryMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Country code is required")
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StateMaster> states;

    public CountryMaster(String name, String code) {
        this.name = name;
        this.code = code;
    }

    // Helper method to add state
    public void addState(StateMaster state) {
        states.add(state);
        state.setCountry(this);
    }

    // Helper method to remove state
    public void removeState(StateMaster state) {
        states.remove(state);
        state.setCountry(null);
    }
}