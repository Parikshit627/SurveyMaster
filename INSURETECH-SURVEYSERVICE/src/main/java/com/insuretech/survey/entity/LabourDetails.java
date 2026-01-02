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
@Table(name = "labour_details", schema = "insuredb")
@NamedQuery(name = "LabourDetails.findAll", query = "SELECT a FROM LabourDetails a")
public class LabourDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sr;
    
    private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId;
	
    @Column(length=15)
	private String labourhsnCode;
    private String labourPartsName;
//    private Character partTypeLabour;
//    private String remarksLabour;
    private String loaborEstimated;
    private String replace;
    private String repair;
    private Integer paintEstimate;
    private String less;
    private String allowed;
    private String comment;
    private Long labGst;
    private Long orderSr;
    
    private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
