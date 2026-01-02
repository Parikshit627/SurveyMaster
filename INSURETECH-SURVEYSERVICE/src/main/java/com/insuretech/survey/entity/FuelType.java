package com.insuretech.survey.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "fuel_type" ,schema = "insuredb")
public class FuelType {


	

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long fuelId;

	    @Column(nullable = false, unique = true, length = 50)
	    private String fuelName;
//
//	    @OneToMany(mappedBy = "fuelType", cascade = CascadeType.ALL)
//	    private List<VehicleVariant> variants;

	    // Getters and Setters
	}
