package com.insuretech.survey.service;

import java.time.LocalDateTime;

import java.util.List;

import com.insuretech.survey.model.AssignApprovalModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveUserSurveyorBasicDetailsModel;

public interface UserSurveyorService {

    ResponseModel getBasicDetails(Long insuranceClaimId, String referenceNumber, String subjectMatter,
            String companyRegGenId);

    ResponseModel saveBasicDetails(SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel);

//  Change -- Aman -- Start
    ResponseModel getUserSurveyorDashboard(String userSurveyorLoginId, String companyRegGenId, List<String> roleId,
            List<String> dashboardRange, String dashboardType, String branchId);

    ResponseModel getToadyDashboardData(String userSurveyorLoginId, String companyRegGenId, List<String> roleId,
            List<String> dashboardRange, String branchId);

//  ResponseModel linkGeneratedForSurvey(String companyRegGenId, Long insuranceClaimId);
//  Change -- Aman -- End
    
    ResponseModel updateBasicDetails(SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel);

    ResponseModel assignForApproval(AssignApprovalModel assignApprovalModel);

//  Change -- Aman -- Start -28-08-25
    ResponseModel getAllDashboardDataBySearch(String userSurveyorLoginId, String companyRegGenId, List<String> roleId,
            String branchId, String searchText);
//  Change -- Aman -- End -28-08-25 

//  Change -- Aman -- End -01-09-25 
	ResponseModel getAllPhotoForAiCaseWise(String startDate, String endDate);
	
	ResponseModel deleteAllPhotoAfterAiRecive(List<Long> insurenceGenIds);
//  Change -- Aman -- End -01-09-25 

// change --parikshit -- start -15-09-2025
	 ResponseModel checkDuplicateBasicDetailsByClaimNo(String claimNo);
	// change --parikshit -- end -15-09-2025
	

}

