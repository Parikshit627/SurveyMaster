package com.insuretech.survey.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.CattleIntimationDetailsModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.CattelService;

@RestController
@RequestMapping("/cattle")
public class CattleController {

	Logger log = LoggerFactory.getLogger(CattleController.class);

	@Autowired
	CattelService cattelService;

	@PostMapping("/saveCattelIntimationDetails")
	public ResponseModel saveCattelIntimationDetails(
			@RequestBody CattleIntimationDetailsModel cattelIntimationDetailsModel) {
		String methodName = "saveCattelIntimationDetails";
		ResponseModel response = new ResponseModel();

		try {
			log.info("Request : Saving cattel intimation details by  CreatedBy: "
					+ cattelIntimationDetailsModel.getMetaData().getCreatedBy() + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			if (cattelIntimationDetailsModel.getMetaData().getCompanyGenId() == null
					|| (cattelIntimationDetailsModel.getMetaData().getCompanyGenId()).equalsIgnoreCase("")) {

				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid parameter : companyGenId");

				log.info("Response : Invalid companyGenId" + "  Method Name" + methodName + " Class : "
						+ this.getClass());

			} else {
				response = cattelService.saveCattelIntimationDetails(cattelIntimationDetailsModel);
			}

		} catch (Exception e) {
			log.error("Response : An error occurred while Saving cattel intimation details  " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
			response.setMessage("Error : " + e.getLocalizedMessage());
		}

		return response;
	}

	@GetMapping("/getCattelIntimationDetails")
	public ResponseModel getCattelIntimationDetails(@RequestParam(required = false) Long cattleIntimationGenId,
			@RequestParam(required = false) String referenceNo) {
		String methodName = "getCattelIntimationDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Finding cattel intimation details by  cattleIntimationGenId : " + cattleIntimationGenId
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			if ((cattleIntimationGenId == null || cattleIntimationGenId == 0L)
					&& (referenceNo == null || referenceNo.equalsIgnoreCase(""))) {

				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid parameter ");

				log.info("Response : Invalid parameter - cattleIntimationGenId : " + cattleIntimationGenId
						+ ",referenceNo : " + referenceNo + "  Method Name" + methodName + " Class : "
						+ this.getClass());

			} else {
				response = cattelService.getCattelIntimationDetails(cattleIntimationGenId, referenceNo);
			}

		} catch (Exception e) {
			log.error("Respond : An error occurred while fetching cattel intimation details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
			response.setMessage("Error : " + e.getLocalizedMessage());
		}
		return response;
	}

	@GetMapping("/geCatteltDashboardData")
	public ResponseModel geCatteltDashboardData(@RequestParam String userSurveyorLoginId,
			                                    @RequestParam  String companyRegGenId,   
			                                    @RequestParam  String roleId, 
			                                    @RequestParam(defaultValue = "0") int page, 
			                                    @RequestParam(defaultValue = "10") int size) {
		String methodName = "getCattelIntimationDetails";
		ResponseModel response = new ResponseModel();
		
		try {
			
			log.info("Request : Finding cattel dashboard data by  userSurveyorLoginId : " + userSurveyorLoginId
					+ ", companyRegGenId : " + companyRegGenId + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			
			if(userSurveyorLoginId !=null && !userSurveyorLoginId.isBlank() 
			   && companyRegGenId!=null && !companyRegGenId.isBlank() 
			   && roleId!=null && !roleId.isBlank() ) {
				
				response=cattelService.geCatteltDashboardData(userSurveyorLoginId, companyRegGenId, roleId, page, size);
				
			}else {
				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid parameter ");
				
				log.info("Request : Invalid parameter -- cattel dashboard data by  userSurveyorLoginId : " + userSurveyorLoginId
						+ ", companyRegGenId : " + companyRegGenId + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			}
			
			
		}catch(Exception e){
			log.error("Respond : An error occurred while finding cattel dashboard data ,error : " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
			response.setMessage("Error : " + e.getLocalizedMessage());
		}
		
		return response;
	}

}
