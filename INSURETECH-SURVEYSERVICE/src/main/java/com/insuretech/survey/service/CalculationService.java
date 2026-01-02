package com.insuretech.survey.service;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import com.insuretech.survey.model.CalculationPartAssessmentProModel;
import com.insuretech.survey.model.DashboardStatusmodel;
import com.insuretech.survey.model.MetalCalculationModel;
import com.insuretech.survey.model.ResponseModel;

public interface CalculationService {

	ResponseModel calculateMaterialType(
			String metalCode ,
			BigDecimal  amount , 
			Integer  depreciationPercent,
			Integer  gst,
			String companyGenId,
			Long insurenceGenId);
	ResponseModel calculateLabourCalculation(
			Long paintEstmate , 
			Integer lessPaintPercent ,
			Long replaceAmount ,
			Long repairAmount ,
			Integer labourPercent);
	ResponseModel getpannelcalculation(String companyGenId, Long insurenceGenId);
	ResponseModel getpannelcalculation(MetalCalculationModel metalModel);
//	Change -- Aman -- Start
	ResponseModel dashboardRequestStatus(String userLoginId, String companyRegGenId, List<String> roleId,
			String branchId);
//	Change -- Aman -- End
	DashboardStatusmodel dashboardRequestStatusEmail(String email);
//	ResponseModel saveTotalAssessmentPro(CalculationPartAssessmentProModel partCalcModel);
	ResponseModel FindCalculationTotalAssessmentPro(String companyGenId, Long insurenceGenId);
	ResponseModel CalculationLabourTotalAssessmentPro(String companyGenId, Long insurenceGenId);

}
