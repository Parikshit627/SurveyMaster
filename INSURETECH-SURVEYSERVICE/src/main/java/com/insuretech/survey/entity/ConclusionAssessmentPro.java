package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.insuretech.survey.entity.VehicleMasterDetails;

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
@Table(name = "conclusion_assessment_pro", schema = "insuredb")
public class ConclusionAssessmentPro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Timestamp createdDtm;
	private Timestamp updatedDtm;


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
	    
	    
	    
	    private BigDecimal metalPartPercent ;
	    private BigDecimal assessmentpercent ;
	    private BigDecimal netLossAssessed ;
	    private String  cashlessStatus ;
	    
	    
	    
	    private BigDecimal halfIMT21I ;
	    private BigDecimal halfIMT21II ;
	    private BigDecimal towingChargesFinal;
	    private BigDecimal imt23Deduction;
	    private BigDecimal salvageCharges;
	    private BigDecimal subTotal;
	    private BigDecimal averageClause;
	    private BigDecimal compulsoryExcess;
	    private BigDecimal voluntaryImposedExcess;

	 
	    
	    
	    

		private String companyGenId;
		private Long insurenceGenId;
		
		@Column(length = 2000)
		private String observation;

	    @Column(length = 2000)
	    private String surveyInspectionDetails;
	    
	    @Column(length = 2000)
	    private String notes;
	    
	    @Column(length = 2000)
	    private String concludingRemark;
}
