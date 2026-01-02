package com.insuretech.survey.model;

import lombok.Data;

@Data
public class BranchMasterModel {
	
	private long branchGenId;
	private String companyGenId;
	private String branchName;
	private String branchAbbreviation;
	private String brancheEmail;
	private String branchAddress;
	private String contactNumbers;
	private String district;
    private String state;
    private long pinCode;
    private String location;
//    private String landMark;
    private long stateCode;

}
