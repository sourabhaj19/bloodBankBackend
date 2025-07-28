package com.app.bloodbank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "city", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "state_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CityMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required")
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    @JsonIgnoreProperties("states")
    private StateMaster state;

    public CityMaster(String name, StateMaster state) {
        this.name = name;
        this.state = state;
    }

}