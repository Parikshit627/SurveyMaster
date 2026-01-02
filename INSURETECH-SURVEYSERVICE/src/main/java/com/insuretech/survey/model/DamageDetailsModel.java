package com.insuretech.survey.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DamageDetailsModel {
	
	private Long sr;
	
	private String companyGenId;
	private Long insurenceGenId;
	private String workedBySurveyName;
	private String workedByUserId;

	private String partsName;
	private String description;


}
