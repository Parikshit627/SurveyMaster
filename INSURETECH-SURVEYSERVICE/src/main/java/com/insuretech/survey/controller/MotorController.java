package com.insuretech.survey.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.MotorService;

@RestController
@RequestMapping("/motor")
public class MotorController {

	Logger log = LoggerFactory.getLogger(MotorController.class);
	
	@Autowired
	MotorService motorService;

	
//	Change -- Aman -- Start
	@GetMapping("/getMotorDashboard")
	public ResponseModel getMotorDashboard(@RequestParam String userSurveyorLoginId, @RequestParam List<String> roleId, @RequestParam(required = false) String companyRegGenId, @RequestParam(required = false) List<String> dashboardRange, @RequestParam String dashboardType , @RequestParam String branchId) {
		String methodName = "getMotorDashboard";
		try {
			log.info("Request : Finding all motor request data by userSurveyorLoginId: " + userSurveyorLoginId
					+ " And companyRegGenId: "+ companyRegGenId + " And roleId: "+ roleId+ " dashboardRange: " + dashboardRange + " dashboardType: " + dashboardType 
                    + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

			return motorService.getMotorDashboard(userSurveyorLoginId,roleId,companyRegGenId, dashboardRange, dashboardType, branchId);

		} catch (Exception e) {
			log.error("An error occurred while Finding all motor request data by userSurveyorLoginId: "
					+ userSurveyorLoginId+ " And companyRegGenId: "+ companyRegGenId + " And roleId: "+ roleId
					+ e);
			return null;
		}
	}
//	Change -- Aman -- End

}
