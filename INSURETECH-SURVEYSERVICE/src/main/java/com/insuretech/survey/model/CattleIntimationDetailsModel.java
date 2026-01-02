package com.insuretech.survey.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CattleIntimationDetailsModel {

	private String nonMotorType;

	private String surveyType;

	private Timestamp dateIntimation;

	private Timestamp dateLoss;

	private String placeLoss;

	private String assetDetail;

	/* ---------- Policy Details ---------- */

	private Timestamp policyStart;

	private String policyTime;

	private Timestamp policyEnd;

	private String policyNo;

	private String claimNo;

	private String tempRefNo;

	/* ---------- Insurer Details ---------- */

	private String insurer;

	private String insurerName;

	private String deputingOffice;

	private String deputingOfficeName;

	private String deputingAddress;

	private String underwritingOffice;

	private String underwritingOfficeName;

	private String underwritingAddress;

	/* ---------- Insured Details ---------- */

	private String insuredName;

	private String insuredMobile;

	private String insuredEmail;

	private String insuredAddress;

	private String insuredPAN;

	private String insuredAadhaar;

	private String insuredGSTN;

	private String insuredAccountNo;

	private String insuredBankName;

	private String insuredIFSC;

	/* ---------- Workshop Details ---------- */

	private String workshopName;

	private String workshopMobile;

	private String workshopEmail;

	private String workshopAddress;

	private String workshopPinCode;

	private String workshopGSTN;

	private String workshopType;

	private String workshopAccountNo;

	private String workshopBankName;

	private String workshopIFSC;

	/* ---------- Claim Amount Details ---------- */

	private Timestamp estimateDate;

	private BigDecimal estimatedAmount;

	private BigDecimal provisionalAmount;

	/* ---------- Officer & Surveyor Details ---------- */

	private Long branchId;
	
	private String branch;

	private Long surveyorRegId;
	
	private String surveyor;

	private String surveyorMobile;

	private String surveyorEmail;

	private Long backOfficerRegId;
	
	private String backOfficer;

	private String backOfficerMobile;

	private String backOfficerEmail;

	private String assignTo;

	private Integer currentStatus;
	
	private String currentStatusDes;
	

	private List<CattleCommentsModel> cattleCommentsModel;
	
	private MetaDataModel metaData;
	
	
}
