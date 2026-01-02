package com.insuretech.survey.controller;

import java.util.List;

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

import com.insuretech.survey.model.BranchMasterModel;
import com.insuretech.survey.model.CarsAddMasterModel;
import com.insuretech.survey.model.ComponentDetailsModel;
import com.insuretech.survey.model.ExteriorCarPartsModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveDefineDataDetailsModel;
import com.insuretech.survey.service.SaveDefineDataIntimationService;

@RestController
@RequestMapping("/saveDefineDataIntimation")
public class SaveDefineDataIntimation {

	Logger log = LoggerFactory.getLogger(UserSurveyorController.class);
	
	@Autowired
	SaveDefineDataIntimationService saveDefineDataIntimationService;

	@PostMapping("/saveDefineDataDetails")
	public ResponseModel saveDefineDataDetails(@RequestBody SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Saving Define Data Details for : " + saveDefineDataDetailsModel.getSave_for()
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			return saveDefineDataIntimationService.saveDefineDataDetails(saveDefineDataDetailsModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving Define Data Details for : "
					+ saveDefineDataDetailsModel.getSave_for(), e);
			return null;
		}
	}
	
	@PostMapping("/updateInsurerData")
	public ResponseModel updateInsurerData(@RequestBody SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Saving Define Data Details for : " + saveDefineDataDetailsModel.getSave_for()
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			return saveDefineDataIntimationService.updateInsurerData(saveDefineDataDetailsModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving Define Data Details for : "
					+ saveDefineDataDetailsModel.getSave_for(), e);
			return null;
		}
	}
	
	@PostMapping("/updateUnderWrittingOffice")
	public ResponseModel updateUnderWrittingOffice(@RequestBody SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Saving Define Data Details for : " + saveDefineDataDetailsModel.getSave_for()
					+ "  Method Name" + methodName + " Class : " + this.getClass());
			return saveDefineDataIntimationService.updateUnderWrittingOffice(saveDefineDataDetailsModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving Define Data Details for : "
					+ saveDefineDataDetailsModel.getSave_for(), e);
			return null;
		}
	}
	
	@PostMapping("/updateClaimProcessing")
	public ResponseModel updateClaimProcessing(@RequestBody SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Saving Define Data Details for : " + saveDefineDataDetailsModel.getSave_for()
					+ "  Method Name" + methodName + " Class : " + this.getClass());
			return saveDefineDataIntimationService.updateClaimProcessing(saveDefineDataDetailsModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving Define Data Details for : "
					+ saveDefineDataDetailsModel.getSave_for(), e);
			return null;
		}
	}
	
	@PostMapping("/updateWorkshop")
	public ResponseModel updateWorkshop(@RequestBody SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Saving Define Data Details for : " + saveDefineDataDetailsModel.getSave_for()
					+ "  Method Name" + methodName + " Class : " + this.getClass());
			return saveDefineDataIntimationService.updateWorkshop(saveDefineDataDetailsModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving Define Data Details for : "
					+ saveDefineDataDetailsModel.getSave_for(), e);
			return null;
		}
	}
	
	@PostMapping("/saveExteriorCarParts")
	public ResponseModel saveExteriorCarParts(@RequestBody List<ExteriorCarPartsModel> exteriorCarPartsModel) {
		String methodName = "saveExteriorCarParts";
		try {
			log.info("Request : save Exterior Car Parts Details  " + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return saveDefineDataIntimationService.saveExteriorCarParts(exteriorCarPartsModel);

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Exterior Car Parts Details "+ e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/saveBranchMasterDetails")
	public ResponseModel saveBranchMasterDetails(@RequestBody BranchMasterModel branchMasterModel) {
		String methodName = "saveBranchMasterDetails";
		try {
			log.info("Request : save Branch Master Details By companyGenId : " + branchMasterModel.getCompanyGenId()
			+ "  Method Name" + methodName + " Class : " + this.getClass());

			return saveDefineDataIntimationService.saveBranchMasterDetails(branchMasterModel);

		} catch (Exception e) {
			log.error("An error occurred whileBranch Master Details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/updateBranchMasterDetails")
	public ResponseModel updateBranchMasterDetails(@RequestBody BranchMasterModel branchMasterModel) {
		String methodName = "updateBranchMasterDetails";
		try {
			log.info("Updating Define Data Details for Branch Master by BranchGenId : "
					+ branchMasterModel.getBranchGenId() + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			return saveDefineDataIntimationService.updateBranchMasterDetails(branchMasterModel);

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for Branch Master -- BranchGenId: "
							+ branchMasterModel.getBranchGenId() + ", " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/addCarsMaster")
	public ResponseModel addCarsMaster(@RequestBody CarsAddMasterModel carsAddMasterModel) {
		String methodName = "addCarsMaster";
		try {

			return saveDefineDataIntimationService.addCarsMaster(carsAddMasterModel);

		} catch (Exception e) {
			
			return null;
		}
	}
	
	
	@GetMapping("/getCarsByType")
	public ResponseModel getCarsByType(@RequestParam String type) {
	    try {
	        return saveDefineDataIntimationService.getCarsByType(type);
	    } catch (Exception e) {
	        e.printStackTrace();
	        ResponseModel response = new ResponseModel();
	        response.setMessage("Error: " + e.getMessage());
	        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	        return response;
	    }
	}
	
	@GetMapping("/getViewAllModelTypeVarient")
	public ResponseModel getViewAllModelTypeVarient(@RequestParam String param) {
	    try {
	        return saveDefineDataIntimationService.getViewAllModelTypeVarient(param);
	    } catch (Exception e) {
	        e.printStackTrace();
	        ResponseModel response = new ResponseModel();
	        response.setMessage("Error: " + e.getMessage());
	        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	        return response;
	    }
	}
	

}
