package com.insuretech.survey.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ComponentDetailsModel {
	
	private Long insurenceGenId;

	private String companyGenId;

	private String referenceNo;

	private String vechileNo;
	
	private List<AIComponentDetailsModel> componentDetails;
	
	private Integer classId;

	private BigDecimal prices;
	

}
