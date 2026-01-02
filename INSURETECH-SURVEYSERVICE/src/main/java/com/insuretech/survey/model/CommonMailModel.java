package com.insuretech.survey.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CommonMailModel {
	
	private String templateId;
	private String templateName;
	private String to;
	private String cc;
	private String subject;
	private String body;
	private Long insurenceGenId;
	private String referenceNo;
	private String companyGenId;
	private String createdBy;
	private List<String> emailList;
	private Map<String,Object> param;
	private boolean attachment;
	private List<String> docUUIDList;
	

}
