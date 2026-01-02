package com.insuretech.survey.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "vehicle_model" ,schema = "insuredb")
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @Column(nullable = false, length = 100)
    private String modelName;
    
    private long companyId;

//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    private VehicleCompanyDetails company;
//
//    @ManyToOne
//    @JoinColumn(name = "car_type_id", nullable = false)
//    private VehicleTypeDetails carType;
//
//    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
//    private List<VehicleVariant> variants;

    // Getters and Setters
}
