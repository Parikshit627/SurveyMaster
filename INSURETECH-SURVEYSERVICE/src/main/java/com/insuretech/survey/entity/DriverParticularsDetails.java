package com.insuretech.survey.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "driver_particulars_details", schema = "insuredb")
@NamedQuery(name = "DriverParticularsDetails.findAll", query = "SELECT a FROM DriverParticularsDetails a")
public class DriverParticularsDetails {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     private String licenceType;
     private String endorsement;
     private String address;
     private String issuingAuthority;
     private String remarkAccident;
     private String remark;
     @Column(length=15)
     private String rcActive;

     
     private Timestamp createdDtm;
     private Timestamp updatedDtm;

}
