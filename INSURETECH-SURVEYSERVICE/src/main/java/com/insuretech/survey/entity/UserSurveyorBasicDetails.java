package com.insuretech.survey.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "user_surveyor_basic_details", schema = "insuredb")
@NamedQuery(name = "UserSurveyorBasicDetails.findAll", query = "SELECT a FROM UserSurveyorBasicDetails a")
public class UserSurveyorBasicDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long insuranceClaimId;

	@Column(name = "reference_no")
	private String referenceNo;

	@Column(name = "survey_type")
	private String surveyType;

	@Column(name = "date_of_intimation")
	private Date dateOfIntimation;

	@Column(name = "date_of_loss")
	private Date dateOfloss;
	
	@Column(name = "time_of_loss")
	private String timeOfloss;
	
	@Column(name = "assesment")
	private String assesment;

	@Column(name = "aadhar_no")
	private long addharNo;

	@Column(name = "gst_no")
	private String gstNo;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "account_no")
	private long accountNo;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "pan_no")
	private String panNo;

	@Column(name = "department")
	private String department;

	@Column(name = "insurer_abbreviation")
	private String insurerAbbreviation;

	@Column(name = "insurer_name")
	private String insurerName;

	@Column(name = "deputing_office_code")
	private String deputingOfficeCode;

	@Column(name = "deputing_office_name")
	private String deputingOfficeName;

	@Column(name = "deputing_office_address")
	private String deputingOfficeAddress;

	@Column(name = "underwriting_office_code")
	private String underwritingOfficeCode;
	@Column
	private String surveyorEmail;

	@Column(name = "underwriting_office_name")
	private String underwritingOfficeName;

	@Column(name = "underwriting_office_address")
	private String underwritingOfficeAddress;

	@Column(name = "asset")
	private String asset;

	@Column(name = "address")
	private String address;

	@Column(name = "claim_no")
	private String claimNo;

	@Column(name = "policy_no")
	private String policyNo;

	@Column(name = "policy_start_date")
	private Date policyStartDate;
	
	@Column(name = "policy_end_date")
	private Date policyEndDate;

	@Column(name = "insured_name")
	private String insuredName;

	@Column(name = "insured_address")
	private String insuredAddress;

	@Column(name = "insured_mobile")
	private long insuredMobile;

	@Column(name = "insured_email")
	private String insuredEmail;

	@Column(name = "survey_location_name")
	private String surveyLocationName;

	@Column(name = "survey_location_address")
	private String surveyLocationAddress;

	@Column(name = "survey_location_mobile")
	private long surveyLocationMobile;

	@Column(name = "survey_location_email")
	private String surveyLocationEmail;

	@Column(name = "branch")
	private String branch;

	@Column(name = "surveyor")
	private String surveyor;

	@Column(name = "officer")
	private String officer;

	@Column(name = "estimate_amount")
	private long estimateAmount;

	@Column(name = "provision_amount")
	private long provisionAmount;

	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "company_gen_id")
	private String companyGenId;

	@Column(name = "created_date")
	private Timestamp createdDate; 

	@Column(name = "current_status")
	private int currentStatus;

	@Transient
	private String Status;

	@Transient
	private List<LinkGeneratedHistory> linkGeneratedHistory;
	
	@Transient
	private String vehicleType ;
	@Transient
	private String type ;
	
	@Transient
	private String insured ;
	
	@Column(name = "update_date")
	private Timestamp updateDate;
	
	@Column(name = "metal_dep")
	private String metalDep;
	
	@Column(name = "branch_id")
	private Long branchId;
	
	@Column(name = "surveyor_reg_id")
	private Long surveyorRegId;
	
	@Column(name = "officer_reg_id")
	private Long officerRegId;
	
	@Column(name = "reg_code")
	private String regCode;
	
	// Change -- Aman -- Start
	
	@Column(name = "self_survey", columnDefinition = "BOOLEAN DEFAULT false")
	private boolean selfSurvey;
	
	@Column(name = "assistant_email")
	private String assistantEmail;
	
	@Column(name = "assign_to")
	private String assignTo;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "link_generated", columnDefinition = "BOOLEAN DEFAULT false")
	private boolean linkGenerated;
	
	
	@Column(name = "doc_submitted", columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean docSubmitted;
	
	
	@Column(name = "ai_photo_mark", columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean aiPhotoMark;

	// Change -- Aman -- End
	
}
