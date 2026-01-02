package com.insuretech.survey.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "vehicle_details", schema = "insuredb")
@NamedQuery(name = "VehicleDetails.findAll", query = "SELECT a FROM VehicleDetails a")
public class VehicleDetails {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long sr;
     
     private String companyGenId;
     private Long insurenceGenId;
     private String workedBySurveyName;
     private String workedByUserId; 
     
     private String regNo;
     private String company;
     private String dor;
     private String vehicleType;
     private String regOwner;
     private String ownerSr;
     private String model;
     private String makeVariant;
     private String colour;
     private String bodyType;
     private String fuel;
     private String chassisNo;
     private String motorNo;
     private String seatingCapacity;
     private String areaOfOperation;
     private Timestamp pucc;
     private String odometer;
     private Timestamp fitnessUpto;
     private Timestamp taxUpto;
     private String unladenWt;
     private String ladenWt;
     private Timestamp permitValidity;
     private Timestamp permitAuthorization;
     private String remark;
     
     private String cubicCap;
     private String preAccident;
     private Long mfgYear;

     
     private Timestamp createdDtm;
     private Timestamp updatedDtm;
//     Add 4 fields
     private String useOfVehicle;
     private String authorizationNumber;
     @Column(name="authorization_upto")
     private String permitNumber;
     private String others;	
     
//   Change -- Aman -- Start -- 05-09-2025
   private String imt3;
   
   @Column(name = "identification_checked")
   private boolean identificationChecked = false;
// Change -- Aman -- End -- 05-09-2025

}
