package com.insuretech.survey.model;

import java.util.List;

import com.insuretech.survey.entity.Enclosures;
import com.insuretech.survey.entity.Notes;
import com.insuretech.survey.entity.Observations;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FinalConclusionModel {

	private long sr;

	private String officerId;
	private String officerName;
	private String companyGenId;
    private Long insurenceGenId;
	private String finalRemark;
	private String lessMetalParts;
	private String lessassessment;
	private String salvageCharges;
	private String averageClause;
	private String compulsoryClause;
	private String otherDeductibles;
	private String finalObservations;
	private String finalNotes;
	private Boolean cashless;
	private String grossLossAmount;
	private String grossLossDep;
	private String grossLossGst;
	private String labourPaintTotal;
	private String finalTotalEstimate;
	private String netLossValue;
	private String finalObservationsandFinding;
	
	private List<String> finalenclosures;
	
	
}
