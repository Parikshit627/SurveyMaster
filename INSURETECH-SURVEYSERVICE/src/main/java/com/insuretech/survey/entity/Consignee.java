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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Table(name = "consignee", schema = "insuredb")
public class Consignee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String insurerName;
    private String officeName;
    private String officeCode;
    private String address;
    private String gstn;
    private String state;
    private int stateCode;

    // Getters and Setters
}
