package com.insuretech.survey.model;


import lombok.Data;

@Data
public class InsuranceInfo {
	
	 private String reportRefNumber;
	    private String insuredName;
	    private String policyNumber;
	    private String policyStartDate;
	    private String claimNumber;
	    private String dateOfLoss;
}
