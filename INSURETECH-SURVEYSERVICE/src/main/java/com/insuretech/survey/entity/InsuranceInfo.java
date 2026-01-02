package com.insuretech.survey.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "insurance_info", schema = "insuredb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})


public class InsuranceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportRefNumber;
    private String insuredName;
    private String policyNumber;
    private String policyStartDate;
    private String claimNumber;
    private String dateOfLoss;

    // Getters and Setters
}
