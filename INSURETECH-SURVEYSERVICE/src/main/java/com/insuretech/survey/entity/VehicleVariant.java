package com.insuretech.survey.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "vehicle_variant"  ,schema = "insuredb")
public class VehicleVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantId;

    @Column(nullable = false, length = 100)
    private String variantName;

//    @ManyToOne
//    @JoinColumn(name = "model_id", nullable = false)
//    private VehicleModel model;

//    @ManyToOne
//    @JoinColumn(name = "fuel_id", nullable = false)
//    private FuelType fuelType;

	private Integer cubicCapacity;
	private Integer seatingCapacity;
	private String ladenWeight;
	private String unladenWeight;
	private long modelId;
	private long typeId;
	private long fuelId;
	private long companyId;

    // Getters and Setters
}

