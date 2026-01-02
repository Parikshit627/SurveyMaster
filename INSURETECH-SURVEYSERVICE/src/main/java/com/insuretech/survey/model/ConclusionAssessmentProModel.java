package com.insuretech.survey.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConclusionAssessmentProModel {

	
	  private BigDecimal totalEstParts;
	    private BigDecimal paintMaterial25;
	    private BigDecimal accidentalLabour;
	    private BigDecimal paintLabour75;
	    private BigDecimal totalEstimate;
	    private BigDecimal towingCharges;
	    private BigDecimal grandTotal;

	    private BigDecimal totalEstPartsGrossAllowed;
	    private BigDecimal totalEstPartsDepreciation;
	    private BigDecimal totalEstPartsActualAllowed;
	    private BigDecimal totalEstPartsAssessed;
	    private BigDecimal totalEstPartsGST;

	    private BigDecimal paintMaterial25GrossAllowed;
	    private BigDecimal paintMaterial25Depreciation;
	    private BigDecimal paintMaterial25ActualAllowed;
	    private BigDecimal paintMaterial25Assessed;
	    private BigDecimal paintMaterial25GST;

	    private BigDecimal accidentalLabourGrossAllowed;
	    private BigDecimal accidentalLabourDepreciation;
	    private BigDecimal accidentalLabourActualAllowed;
	    private BigDecimal accidentalLabourAssessed;
	    private BigDecimal accidentalLabourGST;

	    private BigDecimal paintLabour75GrossAllowed;
	    private BigDecimal paintLabour75Depreciation;
	    private BigDecimal paintLabour75ActualAllowed;
	    private BigDecimal paintLabour75Assessed;
	    private BigDecimal paintLabour75GST;
	    
	    
	    private BigDecimal grossLossAssesedGrossAllowed ;
	    private BigDecimal grossLossAssesedDepreciation  ;
	    private BigDecimal grossLossAssesedActualAllowed ;
	    private BigDecimal grossLossAssesedAppliedGST ;
	    private BigDecimal grossLossAssesedAssessed ;
	    
	    
	    
	    private BigDecimal halfIMT21I ;
	    private BigDecimal halfIMT21II ;
	    private BigDecimal metalPartPercent ;
	    private BigDecimal assessmentpercent ;
	    private BigDecimal netLossAssessed ;
	    private String  cashlessStatus ;
	    
	    
	    
	    
	    private BigDecimal towingChargesFinal;
	    private BigDecimal imt23Deduction;
	    private BigDecimal salvageCharges;
	    private BigDecimal subTotal;
	    private BigDecimal averageClause;
	    private BigDecimal compulsoryExcess;
	    private BigDecimal voluntaryImposedExcess;

	    
	    private String surveyInspectionDetails;
	    private String notes;
	    private String concludingRemark;
	    private String observation;
	    
	    
	    

		private String companyGenId;
		private Long insurenceGenId;

	
}
