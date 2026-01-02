package com.insuretech.survey.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AIComponentDetailsModel {

	private Long aiComponentGenId;
	
	private String name;

	private List<String> damages;
	
	private List<String> images;
	
	private Integer classId;

	private Map<String,BigDecimal> prices;
}
