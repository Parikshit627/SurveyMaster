package com.insuretech.survey.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VehicleDetailsModel {

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
    private Long mfgYear;
    private String cubicCap;
    private String preAccident;
    
//    Add 4 fields 
    private String useOfVehicle;
    private String authorizationNumber;
//    private String authorizationUpto;
    private String permitNumber;
    
    private String others;
    
//    Change -- Aman -- Start -- 05-09-2025
    private String imt3;
    private boolean identificationChecked;
//  Change -- Aman -- End -- 05-09-2025
}
