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
@Table(name = "consigner", schema = "insuredb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Consigner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;
    private String address;
    private String email;
    private String contact;
    private String phone;
    private String gstin;
    private String state;
    private String stateCode;

    // Getters and Setters
}
