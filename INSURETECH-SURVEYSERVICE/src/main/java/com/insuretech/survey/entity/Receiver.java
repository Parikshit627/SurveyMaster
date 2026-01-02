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
@Table(name = "receiver", schema = "insuredb")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})


public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String insurerName;
    private String officeName;
    private String officeCode;
    private String address;
    private String gstn;
    private int stateCode;
    private String state;
    private String pan;
    private String gst;

    // Getters and Setters
}
