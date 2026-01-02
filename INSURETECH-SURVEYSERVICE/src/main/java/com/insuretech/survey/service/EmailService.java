package com.insuretech.survey.service;

import java.util.List;

import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.model.CommonMailModel;
import com.insuretech.survey.model.ResponseModel;

public interface EmailService {
	 public String sendSimpleEmail(List<String> toEmailList, String subject, String body);
	 public ResponseModel sendEmail(CommonMailModel commonMailModel);
	 public String sendEmailWithAttachments(List<String> toEmailList, String subject, String body,List<String> attachmentsUUID);
	 public ResponseModel sendDocRequirementEmail(CommonMailModel commonMailModel);
	 public String saveEmailHistory(CommonMailModel commonMailModel,String status);
	 public ResponseModel getEmailHistoryByReferenceNo(String referenceNo);
	 public ResponseModel sendDirectEmail(CommonMailModel commonMailModel);
	 
	 public CommonMailModel fillEmailData(CommonMailModel commonMailModel);
	 
	 public ResponseModel sendFinalSubSurveyWizardEmail(CommonMailModel commonMailModel);
	 
	 public ResponseModel sendAssessmentEmail(CommonMailModel commonMailModel);
	 
	 public ResponseModel sendIntimationEmail(CommonMailModel commonMailModel);
	 
	 public ResponseModel getDocumentRequired(String vehicleType);
	 
}
