package com.insuretech.survey.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DriverParticularsDetailsModel {

	private Long sr;
    
    private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId;
    
    private String dlNo;
    private Timestamp issuedOn;
    private String dlActive;
    private String driverName;
    private Timestamp dob;
    private Timestamp validUptoNt;
    private Timestamp validUptoTv;
    private List<String> licenceType;    
    private String endorsement;
    private String address;
    private String issuingAuthority;
    private String remarkAccident;
    private String remark;
    private String rcActive;
}
