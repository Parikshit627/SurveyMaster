package com.insuretech.survey.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.CalculationPartAssessmentProModel;
import com.insuretech.survey.model.DashboardStatusmodel;
import com.insuretech.survey.model.MetalCalculationModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.CalculationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/calc")
public class CalculationController {

	@Autowired
	CalculationService calculationService;

	@GetMapping("/getDepreciation")
	public ResponseModel Calculation(@RequestParam(required = false) String metalCode,
			@RequestParam(required = false) BigDecimal  amount, @RequestParam(required = false) Integer depreciationPercent,
			@RequestParam(required = false) Integer gst,@RequestParam(required = false) String companyGenId,@RequestParam(required = false) Long insurenceGenId ) {
		return calculationService.calculateMaterialType(metalCode, amount, depreciationPercent, gst,companyGenId,insurenceGenId);
	}

	@GetMapping("/getLabourCalculation")
	public ResponseModel getLabourCalculation(@RequestParam(required = false) Long paintEstmate,
			@RequestParam(required = false) Integer lessPaintPercent,
			@RequestParam(required = false) Long replaceAmount, @RequestParam(required = false) Long repairAmount,
			@RequestParam(required = false) Integer labourPercent) {
		return calculationService.calculateLabourCalculation(paintEstmate, lessPaintPercent, replaceAmount,
				repairAmount, labourPercent);
	}

	@GetMapping("/getpannelcalculation")
	public ResponseModel getpannelcalculation(@RequestParam String companyGenId, @RequestParam Long insurenceGenId) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			return calculationService.getpannelcalculation(companyGenId, insurenceGenId);

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
	
	@PostMapping("/saveMetalAllowDepGst")
	public ResponseModel saveMetalAllowDepGst(@RequestBody MetalCalculationModel metalModel) {
		String methodName="saveMetalAllowDepGst";
		//TODO: process POST request
		try {
			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			return calculationService.getpannelcalculation(metalModel);

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
	
	
//	Change -- Aman -- Start
	@GetMapping("/dashboardRequestStatus")
	public ResponseModel dashboardRequestStatus(@RequestParam String userLoginId, @RequestParam(required = false) String companyRegGenId, @RequestParam List<String> roleId, @RequestParam String branchId) {
		String methodName = "dashboardRequestStatus";
		try {
			log.info("Request : Finding All Dashboard data for LoginId: " + userLoginId
					+ " And companyRegGenId: "+ companyRegGenId
					+ " and role Id: " + roleId + " and branchId: " + branchId + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			return calculationService.dashboardRequestStatus(userLoginId,companyRegGenId, roleId, branchId);

		} catch (Exception e) {
			log.error("An error occurred while Finding All Dashboard data for dashboardRequestStatus: ", e);
			return null;
		}
	}
//	Change -- Aman -- End
	
	
	@GetMapping("/dashboardRequestStatusEmail")
	public DashboardStatusmodel dashboardRequestStatusEmail(@RequestParam(required = false) String email) {
		try {
			

			return calculationService.dashboardRequestStatusEmail(email);

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
	
	

	@GetMapping("/Find/FindCalculationTotalAssessmentPro")
	public ResponseModel FindCalculationTotalAssessmentPro(@RequestParam String companyGenId,
	                                            @RequestParam Long insurenceGenId) {
	    String methodName = "FindCalculationTotalAssessmentPro";
	    try {
	        log.info("Request received → Method: {}, Class: {}, companyGenId: {}, insurenceGenId: {}",
	                methodName, this.getClass().getSimpleName(), companyGenId, insurenceGenId);

	        return calculationService.FindCalculationTotalAssessmentPro(companyGenId, insurenceGenId);

	    } catch (Exception e) {
	        log.error("Error in method: {}, Class: {}", methodName, this.getClass().getSimpleName(), e);
	        return null;
	    }
	}

	@GetMapping("/Find/CalculationLabourTotalAssessmentPro")
	public ResponseModel CalculationLabourTotalAssessmentPro(@RequestParam String companyGenId,
	                                            @RequestParam Long insurenceGenId) {
	    String methodName = "CalculationLabourTotalAssessmentPro";
	    try {
	        log.info("Request received → Method: {}, Class: {}, companyGenId: {}, insurenceGenId: {}",
	                methodName, this.getClass().getSimpleName(), companyGenId, insurenceGenId);

	        return calculationService.CalculationLabourTotalAssessmentPro(companyGenId, insurenceGenId);

	    } catch (Exception e) {
	        log.error("Error in method: {}, Class: {}", methodName, this.getClass().getSimpleName(), e);
	        return null;
	    }
	}
}
