package com.insuretech.survey.controller;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.GetDefinedDataIntimationService;

@RestController
@RequestMapping("/defineDataIntimation")
public class GetDefinedDataIntimation {

	Logger log = LoggerFactory.getLogger(UserSurveyorController.class);

	@Autowired
	GetDefinedDataIntimationService getDefinedDataIntimationService;

	@GetMapping("/getInsurerAllDetails")
	public ResponseModel getInsurerAllDetails(@RequestParam String abbreviation) {
		String methodName = "getInsurerAllDetails";
		try {
			log.info("Request : Finding Insurer All Details by abbreviation: " + abbreviation + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getInsurerAllDetails(abbreviation);

		} catch (Exception e) {
			log.error("An error occurred while Finding Insurer All Details by abbreviation: " + abbreviation, e);
			return null;
		}
	}

	@GetMapping("/getAllClaimProcessingOffice")
	public ResponseModel getAllClaimProcessingOffice(@RequestParam String officeCode) {
		String methodName = "getAllClaimProcessingOffice";
		try {
			log.info("Request : Finding All Claim Processing Office by officeCode: " + officeCode + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getAllClaimProcessingOffice(officeCode);

		} catch (Exception e) {
			log.error("An error occurred while Finding All Claim Processing Office by officeCode: " + officeCode, e);
			return null;
		}
	}

	@GetMapping("/getAllUnderWritingOffice")
	public ResponseModel getAllUnderWritingOffice(@RequestParam String officeCode) {
		String methodName = "getAllUnderWritingOffice";
		try {
			log.info("Request : Finding All Under Writing Office by officeCode: " + officeCode + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getAllUnderWritingOffice(officeCode);

		} catch (Exception e) {
			log.error("An error occurred while Finding All Under Writing Office by officeCode: " + officeCode, e);
			return null;
		}
	}

	@GetMapping("/getAllWorkshopDetails")
	public ResponseModel getAllWorkshopDetails(@RequestParam String workShopName) {
		String methodName = "getAllWorkshopDetails";
		try {
			log.info("Request : Finding All Workshop Details by workShopName: " + workShopName + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getAllWorkshopDetails(workShopName);

		} catch (Exception e) {
			log.error("An error occurred while Finding All Workshop Details by workShopName: " + workShopName, e);
			return null;
		}
	}

	@GetMapping("/getVehicleMasterDetails")
	public ResponseModel getVehicleMasterDetails(@RequestParam String companyId, @RequestParam String type,
			                                     @RequestParam String model) {
		String methodName = "getVehicleMasterDetails";
		try {
			log.info("Request : Finding Vehicle Master Details "+ "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getVehicleMasterDetails( companyId, type, model);

		} catch (Exception e) {
			log.info("Respond : Invliad Parameters" + "  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@GetMapping("/getAllExteriorCarParts")
	public ResponseModel getAllExteriorCarParts(@RequestParam String partName,@RequestParam Long vehicleTypeId) {
		String methodName = "getAllExteriorCarParts";
		try {
			log.info("Request : Finding All Exterior Car Parts " + partName  + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getAllExteriorCarParts(partName,vehicleTypeId);

		} catch (Exception e) {
			log.info("Respond : An error occurred while Finding  All Exterior Car Parts " + partName+ "  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@GetMapping("/getAllVehicleCompanyDetails")
	public ResponseModel getAllVehicleCompanyDetails(@RequestParam String company) {
		String methodName = "getAllVehicleCompanyDetails";
		try {
			log.info("Request : Finding All Vehicle Company Detalis by company name " + company + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			return getDefinedDataIntimationService.getAllVehicleCompanyDetails(company);

		} catch (Exception e) {
			log.error("An error occurred while Finding All Vehicle Company Detalis by company name  " + company 
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@GetMapping("/getAllVehicleTypeDetails")
	public ResponseModel getAllVehicleTypeDetails(@RequestParam String type) {
		String methodName = "getAllVehicleTypeDetails";
		try {
			log.info("Request : Finding All Vehicle Type Detalis by Type name " + type + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			return getDefinedDataIntimationService.getAllVehicleTypeDetails(type);

		} catch (Exception e) {
			log.info("Respond : data founded successfully All Vehicle Type Detalis by Type name  " + type 
					+ "  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@GetMapping("/getFinalDocumentSubmission")
	public ResponseModel getFinalDocumentSubmission(@RequestParam String companyGenId,@RequestParam Long insurenceGenId,@RequestParam String docName) {
		String methodName = "getAllVehicleTypeDetails";
		try {
			log.info("Request : Finding Document by companyGenId " + companyGenId + ", insurenceGenId : "
					+ insurenceGenId +" , docName : " +docName+ "  Method Name" + methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getFinalDocumentSubmission(companyGenId,insurenceGenId,docName);

		} catch (Exception e) {
			log.info("Respond :  An error occurred while finding documents ,error : " + e.getMessage() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@GetMapping("/getBranchMasterDetails")
	public ResponseModel  getBranchMasterDetails( @RequestParam String companyGenId,@RequestParam String branchName) {
		String methodName = "getBranchMasterDetails";
		try {
			log.info("Request : fetch all Branch Master Details By companyGenId : " + companyGenId
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			return getDefinedDataIntimationService.getBranchMasterDetails(companyGenId,branchName);

		} catch (Exception e) {
			log.error("An error occurred while fetching Branch Master Details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@GetMapping("/checkDuplicateInsureDetails")
	public ResponseModel checkDuplicateInsureDetails(@RequestParam  (required = false) String abbreviation,@RequestParam  (required = false) String insurer) {
		 
		
		return getDefinedDataIntimationService.checkDuplicateInsureDetails(abbreviation , insurer);
	}
	
	@GetMapping("/checkDuplicateUnderwritingOffice")
	public ResponseModel checkDuplicateUnderwritingOffice(@RequestParam(required = false) String underwritingOfficeCode,
	                                                @RequestParam(required = false) String underwritingOfficeName) {
	    return getDefinedDataIntimationService.checkDuplicateUnderwritingOffice(underwritingOfficeCode, underwritingOfficeName);
	}
	
	@GetMapping("/checkDuplicateClaimProcessingOffice")
	public ResponseModel checkDuplicateClaimProcessingOffice(@RequestParam(required = false) String claimProcessingOfficeCode,
	                                                  @RequestParam(required = false) String claimProcessingOfficeName) {
	    return getDefinedDataIntimationService.checkDuplicateClaimProcessingOffice(claimProcessingOfficeCode, claimProcessingOfficeName);
	}
	
	
	@GetMapping("/getAllCompanyCars")
	public ResponseModel getAllCompanyCars() {
	    return getDefinedDataIntimationService.getAllCompanyCars();
	}

	
	@GetMapping("/getAllCarsModel")
	public ResponseModel getAllCarsModel() {
	    return getDefinedDataIntimationService.getAllCarsModel();
	}

	@GetMapping("/getCarsDetails")
	public ResponseModel getCarsDetails(@RequestParam String select) {
	    return getDefinedDataIntimationService.getCarsDetails(select);
	}
	
	@GetMapping("/checkDuplicateDOLAndVehicleNo")
	public ResponseModel checkDuplicateDOLAndVehicleNo(@RequestParam String vehicleNo , @RequestParam Date dol) {
	    return getDefinedDataIntimationService.checkDuplicateDOLAndVehicleNo(vehicleNo ,dol);
	}
}
