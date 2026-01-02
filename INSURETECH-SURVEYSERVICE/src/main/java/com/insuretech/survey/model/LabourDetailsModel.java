package com.insuretech.survey.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LabourDetailsModel {
	
	private Long sr;
	private Long orderSr;
	
	private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId;
    
    private String labourhsnCode;
    private String labourPartsName;
//    private Character partTypeLabour;
//    private String remarksLabour;
    private String loaborEstimated;
    private String replace;
    private String repair;
    private Integer paintEstimate;
    private String less;
    private String allowed;
    private String comment;
    private Long labGst;
}
