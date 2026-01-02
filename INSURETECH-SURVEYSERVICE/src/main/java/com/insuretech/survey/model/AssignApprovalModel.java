package com.insuretech.survey.model;

import lombok.Data;

@Data
public class AssignApprovalModel {

	private Long branchId;
	private String branch;
	private String surveyor;
	private String surveyorEmail;
	private String companyGenId;
	private String userId;
	private Long insuranceClaimId;
}
