package com.insuretech.survey.entity;

import java.sql.Timestamp;

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
@Table(name = "policy_details", schema = "insuredb")
@NamedQuery(name = "PolicyDetails.findAll", query = "SELECT a FROM PolicyDetails a")
public class PolicyDetails {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long sr;
     
     private String companyGenId;
     private Long insurenceGenId;
     private String workedBySurveyName;
     private String workedByUserId; 
     
	 private String insurer;
	 private String asPerPolicy;
     private Timestamp deputationDate;
     private String claimNo;
     private String otherRefNo;
     private String underwriting;
     private String policyNo;
     private String policyFromTo;
     private String idv;
     private String deputingAddress;
     private String nilDep;
     private String insuredName;
     private String breakIn;
     private String address;
     private String surveyType;
     private String contact;
     private String email;
     private String ncb;
     private String hpa;
     private String claimSr;
     private String comment;
     private String deputingOfficeCode;
     private String policyFrom;
     private String policyTo;
     private String previousPolicy;
     private String thirdPartyPolicy;
     
     private Timestamp createdDtm;
     private Timestamp updatedDtm;
	
}
