package com.insuretech.survey.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CalculationLabourAssessmentProModel {

	    private BigDecimal totalEstimated;
	    private BigDecimal totalReplace;
	    private BigDecimal totalRepair;
	    private BigDecimal totalPaintEstimate;
	    private BigDecimal totalLess;
	    private BigDecimal totalAllowed;
	    private BigDecimal totalLabGst;
	    private BigDecimal totalEstimateGst;
	    private BigDecimal totalReplaceGst;
	    private BigDecimal totalRepairGst;
	    private BigDecimal totalPaintEstimateGst;
	    private BigDecimal totalAllowedPaintGst;
	    private BigDecimal totalAllowedLabourT;
	    private BigDecimal totalLabourGstT;
	    private BigDecimal totalPaint75Labour;
	    private BigDecimal totalGstPaint75Labour;
	    private BigDecimal totalPaint25Part;
	    private BigDecimal totalGstPaint25Part;
	    private Boolean accidentalLabour;
	    private Boolean paintLabour;


	    private String companyGenId;
	    private Long insurenceGenId;
	    private String policyType;
	
	  
}
