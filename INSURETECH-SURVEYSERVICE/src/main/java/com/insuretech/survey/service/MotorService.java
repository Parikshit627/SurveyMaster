package com.insuretech.survey.service;

import java.util.List;

import com.insuretech.survey.model.ResponseModel;

public interface MotorService {

//	Change -- Aman -- Start
	ResponseModel getMotorDashboard(String userSurveyorLoginId, List<String> roleId, String companyRegGenId,
			List<String> dashboardRange, String dashboardType, String branchId);
//	Change -- Aman -- End
	
}
