package com.insuretech.survey.model;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SaveUserSurveyorBasicDetailsModel {

	private String surveyType;
	private Date dateOfIntimation;
	private Date dateOfloss;
	private String timeOfloss;
	private String assesment;
	private long addharNo;
	private String gstNo;
	private String bankName;
	private long accountNo;
	private String ifscCode;
	private String panNo;
	private String department;
	private String insurerAbbreviation;
	private String insurerName;
	private String deputingOfficeCode;
	private String deputingOfficeName;
	private String deputingOfficeAddress;
	private String underwritingOfficeCode;
	private String underwritingOfficeName;
	private String underwritingOfficeAddress;
	private String asset;
	private String address;
	private String claimNo;
	private String policyNo;
	private Date policyStartDate;
	private Date policyEndDate;
	private String insuredName;
	private String insuredAddress;
	private long insuredMobile;
	private String insuredEmail;
	private String surveyLocationName;
	private String surveyLocationAddress;
	private long surveyLocationMobile;
	private String surveyLocationEmail;
	private String branch;
	private String surveyor;
	private String surveyorEmail;
	private String officer;
	private long estimateAmount;
	private long provisionAmount;
	private String createdBy;
	private String companyGenId;
	private String referenceNo;

	private Long insuranceClaimId;

	private Long branchId;

	private Long surveyorRegId;

	private Long officerRegId;

    private String regCode;
 // Change -- Aman -- Start
	private Boolean selfSurvey;
	private String assistantEmail;
	// Change -- Aman -- End
}
