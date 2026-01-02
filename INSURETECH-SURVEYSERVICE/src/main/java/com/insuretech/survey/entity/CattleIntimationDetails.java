package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cattle_intimation_details", schema = "cattledb")
@NamedQuery(name = "CattleIntimationDetails.findAll", query = "SELECT a FROM CattleIntimationDetails a")
public class CattleIntimationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cattleIntimationGenId;

	@Column(name = "reference_no", unique = true, length = 50)
	private String referenceNo;

	@Column(name = "non_motor_type", length = 50)
	private String nonMotorType;

	@Column(name = "survey_type", length = 50)
	private String surveyType;

	@Column(name = "date_intimation")
	private Timestamp dateIntimation;

	@Column(name = "date_loss")
	private Timestamp dateLoss;

	@Column(name = "place_loss", length = 255)
	private String placeLoss;

	@Column(name = "asset_detail", length = 100)
	private String assetDetail;

	/* ---------- Policy Details ---------- */

	@Column(name = "policy_start")
	private Timestamp policyStart;

	@Column(name = "policy_time", length = 10)
	private String policyTime;

	@Column(name = "policy_end")
	private Timestamp policyEnd;

	@Column(name = "policy_no", length = 50)
	private String policyNo;

	@Column(name = "claim_no", length = 50)
	private String claimNo;

	@Column(name = "temp_ref_no", length = 50)
	private String tempRefNo;

	/* ---------- Insurer Details ---------- */

	@Column(name = "insurer", length = 20)
	private String insurer;

	@Column(name = "insurer_name", length = 150)
	private String insurerName;

	@Column(name = "deputing_office", length = 50)
	private String deputingOffice;

	@Column(name = "deputing_office_name", length = 150)
	private String deputingOfficeName;

	@Column(name = "deputing_address", length = 255)
	private String deputingAddress;

	@Column(name = "underwriting_office", length = 50)
	private String underwritingOffice;

	@Column(name = "underwriting_office_name", length = 150)
	private String underwritingOfficeName;

	@Column(name = "underwriting_address", length = 255)
	private String underwritingAddress;

	/* ---------- Insured Details ---------- */

	@Column(name = "insured_name", length = 150)
	private String insuredName;

	@Column(name = "insured_mobile", length = 15)
	private String insuredMobile;

	@Column(name = "insured_email", length = 100)
	private String insuredEmail;

	@Column(name = "insured_address", length = 255)
	private String insuredAddress;

	@Column(name = "insured_pan", length = 20)
	private String insuredPAN;

	@Column(name = "insured_aadhaar", length = 20)
	private String insuredAadhaar;

	@Column(name = "insured_gstn", length = 20)
	private String insuredGSTN;

	@Column(name = "insured_account_no", length = 30)
	private String insuredAccountNo;

	@Column(name = "insured_bank_name", length = 100)
	private String insuredBankName;

	@Column(name = "insured_ifsc", length = 20)
	private String insuredIFSC;

	/* ---------- Workshop Details ---------- */

	@Column(name = "workshop_name", length = 150)
	private String workshopName;

	@Column(name = "workshop_mobile", length = 15)
	private String workshopMobile;

	@Column(name = "workshop_email", length = 100)
	private String workshopEmail;

	@Column(name = "workshop_address", length = 255)
	private String workshopAddress;

	@Column(name = "workshop_pin_code", length = 10)
	private String workshopPinCode;

	@Column(name = "workshop_gstn", length = 20)
	private String workshopGSTN;

	@Column(name = "workshop_type", length = 50)
	private String workshopType;

	@Column(name = "workshop_account_no", length = 30)
	private String workshopAccountNo;

	@Column(name = "workshop_bank_name", length = 100)
	private String workshopBankName;

	@Column(name = "workshop_ifsc", length = 20)
	private String workshopIFSC;

	/* ---------- Claim Amount Details ---------- */

	@Column(name = "estimate_date")
	private Timestamp estimateDate;

	@Column(name = "estimated_amount", precision = 15, scale = 2)
	private BigDecimal estimatedAmount;

	@Column(name = "provisional_amount", precision = 15, scale = 2)
	private BigDecimal provisionalAmount;

	/* ---------- Officer & Surveyor Details ---------- */

	@Column(name = "branch_id")
	private Long branchId;
	
	@Column(name = "branch", length = 100)
	private String branch;

	@Column(name = "surveyor_reg_id")
	private Long surveyorRegId;
	
	@Column(name = "surveyor", length = 100)
	private String surveyor;

	@Column(name = "surveyor_mobile", length = 15)
	private String surveyorMobile;

	@Column(name = "surveyor_email", length = 100)
	private String surveyorEmail;

	@Column(name = "back_officer_reg_id")
	private Long backOfficerRegId;
	
	@Column(name = "back_officer", length = 100)
	private String backOfficer;

	@Column(name = "back_officer_mobile", length = 15)
	private String backOfficerMobile;

	@Column(name = "back_officer_email", length = 100)
	private String backOfficerEmail;

	@Column(name = "source", length = 50)
	private String source;
	
	@Column(name = "form_version", length = 50)
	private String formVersion;
	
	@Column(name = "assign_to")
	private String assignTo;
	
	@Column(name = "current_status")
	private Integer currentStatus;
	
	private String companyGenId;

	private Timestamp createdDtm = new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
}
