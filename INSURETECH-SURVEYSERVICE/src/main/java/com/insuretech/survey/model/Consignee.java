package com.insuretech.survey.model;


import lombok.Data;

@Data
public class Consignee {
	
	 private String insurerName;
	    private String officeName;
	    private String officeCode;
	    private String address;
	    private String gstn;
	    private int stateCode;
	    private String state;
}
