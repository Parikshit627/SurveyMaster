package com.insuretech.survey.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ConclusionAssessmentProModel;
import com.insuretech.survey.model.FinalSubmissionRequest;
import com.insuretech.survey.model.LabourAssessmentProJson;
import com.insuretech.survey.model.PartsAssessmentProJson;
import com.insuretech.survey.model.PhotoWizardModel;
import com.insuretech.survey.model.ReportDocumentUploadModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SurveyWizardModel;
import com.insuretech.survey.model.WorkflowModel;
import com.insuretech.survey.service.SaveSurveyWizadService;
import com.insuretech.survey.serviceImpl.SaveSurveyWizadServiceImp;
//@CrossOrigin
@RestController
@RequestMapping("/surveyWizad")
public class SaveSurveyWizadController {
	
	Logger log = LoggerFactory.getLogger(SaveSurveyWizadController.class);
	
	@Autowired
	SaveSurveyWizadServiceImp service;
	
//	Chagne -- Aman -- Start
	@Autowired
	SaveSurveyWizadService saveSurveyWizadService;
//	Chagne -- Aman -- End
	
	@PostMapping("/saveSurveyWizadDetails")
	public ResponseModel saveSurveyWizad(@RequestBody SurveyWizardModel surveyWizardModel) {
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Saving Survey Wizad,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return service.saveSurveyWizad(surveyWizardModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving Saving Survey Wizad", e);
			return null;
		}
	}
	
	@GetMapping("/getSurveyWizadDetails")
	public ResponseModel getSurveyWizad(@RequestParam String companyGenId,@RequestParam Long insurenceGenId,@RequestParam String workedByUserId){
		String methodName = "saveDefineDataDetails";
		try {
			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return service.getSurveyWizad(companyGenId,insurenceGenId,workedByUserId);

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
	
	@PostMapping("/finalDocumentSubmission")
	public ResponseModel finalDocumentSubmission(@RequestBody FinalSubmissionRequest finalSubmissionRequest) {
		String methodName = "finalDocumentSubmission";
		ResponseModel model = new ResponseModel();
		try {
			if (finalSubmissionRequest.getInsurenceGenId() == null || finalSubmissionRequest.getInsurenceGenId() == 0L) {
				log.info("Response : Invalid InsuranceGenId " + "  Method Name" + methodName + " Class : " + this.getClass());

				model.setHttpStatus(HttpStatus.SC_BAD_REQUEST);
				model.setMessage("Invalid InsuranceGenId");
				return model;
			}
			
			
			log.info("Request : Saving Survey Wizad,  " + "  Method Name" + methodName + " Class : " + this.getClass());
			return service.finalDocumentSubmmsion(finalSubmissionRequest);

		} catch (Exception e) {
			log.error("An error occurred while Saving finalDocumentSubmission", e);
			return null;
		}
	}
	
//	Change -- Aman -- Start -- 08-09-2025
	@GetMapping("/finalDocumentView")
	public ResponseModel finalDocumentView(@RequestParam (required = false) String companyGenId, @RequestParam  Long insurenceGenId,@RequestParam String vehicleNumber, @RequestParam String requestFor){
		String methodName = "finalDocumentView";
		try {
			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return service.finalDocumentView(companyGenId ,insurenceGenId,vehicleNumber,requestFor);

		} catch (Exception e) {
			log.error("An error occurred while fetching survey wizard", e);
			return null;
		}
	}
//	Change -- Aman -- Start -- 08-09-2025
	

	@DeleteMapping("/deleteAssemblyAndLabour")
	public ResponseModel deleteAssemblyAndLabour(@RequestParam Long sr, @RequestParam Long insurenceGenId) {
		try {
	    return service.deleteAssemblyAndLabourEntry(sr, insurenceGenId);

		} catch (Exception e) {
			log.error("An error occurred while deleteAssemblyAndLabour", e);
			return null;
		}
	    
	}



	
	
	@PostMapping("/reportDocumentsUpload")
	public ResponseModel reportDocumentsUpload(@RequestBody ReportDocumentUploadModel reportDocumentUpload) {
	    String methodName = "reportDocumentsUpload";
	    try {
	        log.info("Request : Saving Survey Wizard Report Document Upload, Method Name: " + methodName + " Class: " + this.getClass());
	        return service.reportDocumentsUpload(reportDocumentUpload);
	    } catch (Exception e) {
	        log.error("An error occurred while saving reportDocumentsUpload", e);
	        
	        // Creating a custom error response model
	        ResponseModel errorResponse = new ResponseModel();
	        errorResponse.setHttpStatus("ERROR");
	        errorResponse.setMessage("An error occurred while processing the document upload. Please try again later.");
	        errorResponse.setMessage(e.getMessage());  // Optionally include the exception message for debugging
	        
	        return errorResponse;
	    }
	}

	
//	Change -- Aman -- Start
	@GetMapping("/getVehicleDetailsBeforeLogin")
	public ResponseModel getVehicleDetailsBeforeLogin(@RequestParam(required = false) String vehicleNo, @RequestParam(required = false) String refId) {

		try {
			log.error("Successfully getting the Vehicle Details");
			return service.getVehicleDetailsBeforeLogin(vehicleNo,refId);
//			Change -- Aman -- End
			
		} catch (Exception e) {
			log.error("An error occurred while getting  get Vehicle Details", e);
			 ResponseModel errorResponse = new ResponseModel();

		        errorResponse.setHttpStatus("ERROR");
		        errorResponse.setMessage("An error occurred while processing the get Vehicle Details. Please try again later.");
		        errorResponse.setMessage(e.getMessage());  // Optionally include the exception message for debugging
		        return errorResponse;
		}
	}
	
	@GetMapping("/getVehicleDetailsBeforeLog")
	public ResponseModel getVehicleDetailsBeforeLogin(@RequestParam(required = false) String param) {
	    try {
	        log.info("API call: getVehicleDetailsBeforeLogin with param = {}", param);
	        return service.getVehicleDetailsBeforeLog(param);
	    } catch (Exception e) {
	        log.error("Error while getting vehicle details", e);
	        ResponseModel errorResponse = new ResponseModel();
	        errorResponse.setHttpStatus("ERROR");
	        errorResponse.setMessage("An error occurred while processing the request. Please try again later.");
	        errorResponse.setData(e.getMessage());
	        return errorResponse;
	    }
	}

	@DeleteMapping("/deleteDocument/{docUuid}")
	public ResponseModel deleteDocument(@PathVariable String docUuid) {
	    ResponseModel response = new ResponseModel();
	    try {
	        boolean deleted = service.deleteDocumentByUuid(docUuid);
	        if (deleted) {
	            response.setHttpStatus(HttpStatus.SC_OK);
	            response.setMessage("Document deleted successfully.");
	        } else {
	            response.setHttpStatus(HttpStatus.SC_NOT_FOUND);
	            response.setMessage("Document not found with UUID: " + docUuid);
	        }
	    } catch (Exception e) {
	        log.error("Error deleting document with UUID {}: {}", docUuid, e.getMessage(), e);
	        response.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	        response.setMessage("Error deleting document.");
	    }
	    return response;
	}

	
//	Change -- Aman -- Start
	@PostMapping("/workflowBySurveyor")
	public ResponseModel workflowBySurveyor(@RequestBody WorkflowModel workflowModel) {
		String methodName = "workflowBySurveyor";
		try {
			log.info("Request : Saving workflow By Surveyor loginId: "
					+ workflowModel.getUserId() + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			return saveSurveyWizadService.workflowBySurveyor(workflowModel);

		} catch (Exception e) {
			log.error("An error occurred while Saving workflow By Surveyor loginId: "
					+ workflowModel.getUserId(), e);
			return null;
		}
	}
//	Change -- Aman -- End
	
	
	
//	@GetMapping("/check")
//	public ResponseModel finalDocumentView(){
//		String methodName = "finalDocumentView";
//		try {
//			log.info("Request : Fetching wurvey wizard data,  " + "  Method Name" + methodName + " Class : " + this.getClass());
//
//			return service.check();
//
//
//		} catch (Exception e) {
//			log.error("An error occurred while fetching survey wizard", e);
//			return null;
//		}
//	}
	
//	@PostMapping("/test")
//	public ResponseModel test(@RequestParam String companyGenId,@RequestParam Long insurenceGenId ) {
//	    String methodName = "reportDocumentsUpload";
//	    try {
//	        log.info("Request : Saving Survey Wizard Report Document Upload, Method Name: " + methodName + " Class: " + this.getClass());
//	        
//	        String res=service.calculateMetalDep(insurenceGenId,companyGenId);
//	        ResponseModel response = new ResponseModel();
//	        response.setHttpStatus("OK");
//	        response.setData(res);
//	        response.setMessage("success");
//	       
//	        return response;
//	    } catch (Exception e) {
//	        log.error("An error occurred while saving reportDocumentsUpload", e);
//	        
//	        // Creating a custom error response model
//	        ResponseModel errorResponse = new ResponseModel();
//	        errorResponse.setHttpStatus("ERROR");
//	        errorResponse.setMessage("An error occurred while processing the document upload. Please try again later.");
//	        errorResponse.setMessage(e.getMessage());  // Optionally include the exception message for debugging
//	        
//	        return errorResponse;
//	    }
//	}
	
	@PostMapping("/save/PartAssessmentPro")
	public ResponseModel saveAssessmentPro(@RequestBody PartsAssessmentProJson models) {
		String methodName = "saveAssesmentPro";
		try {

			return saveSurveyWizadService.saveAssesmentPro(models);

		} catch (Exception e) {
			log.error("An error occurred while Saving Saving Survey Wizad", e);
			return null;
		}
	}
	
	
	@GetMapping("/view/ViewPartsAssessmentPro")
	public ResponseModel ViewAssessmentPro(@RequestParam String companyGenId,@RequestParam Long insurenceGenId ) {
		String methodName = "saveAssesmentPro";
		try {
			log.info("Request : Saving Survey Wizad saveAssessmentPro,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			
			 
				  return saveSurveyWizadService.ViewAssessmentPro(companyGenId,insurenceGenId);
			        // sabRowNo missing or zero
			  

		} catch (Exception e) {
			log.error("An error occurred while Saving Saving Survey Wizad", e);
			return null;
		}
	}
	
	
	
	@PostMapping("/save/LabourAssessmentPro")
	public ResponseModel LabourAssessmentPro(@RequestBody LabourAssessmentProJson models) {
		String methodName = "saveAssesmentPro";
		try {
			log.info("Request : Saving Survey Wizad LabourAssessmentPro,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return saveSurveyWizadService.LabourAssessmentPro(models);

		} catch (Exception e) {
			log.error("An error occurred while Saving LabourAssessmentPro" + "  Method Name" + methodName + " Class : " + this.getClass(), e);
			return null;
		}
	}
	@GetMapping("/view/ViewLabourAssessmentPro")
	public ResponseModel ViewLabourAssessmentPro(@RequestParam String companyGenId,@RequestParam Long insurenceGenId ) {
		String methodName = "ViewLabourAssessmentPro";
		try {
			log.info("Request : Saving Survey Wizad ViewLabourAssessmentPro,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return saveSurveyWizadService.ViewLabourAssessmentPro(companyGenId,insurenceGenId);

		} catch (Exception e) {
			log.error("An error occurred while Saving Saving Survey Wizad", e);
			return null;
		}
	}
	
	@DeleteMapping("/delete/PartAndLabour/AssessmentPro")
	
	public ResponseModel deletePartAndLabourAssessmentPro(@RequestParam Long id, @RequestParam Long insurenceGenId) {
		try {
	    return saveSurveyWizadService.deletePartAndLabourAssessmentPro(id, insurenceGenId);

		} catch (Exception e) {
			log.error("An error occurred while deletePartAndLabourAssessmentPro", e);
			return null;
		}
	    
	}
	
	@PostMapping("/save/conclusionAssessmentPro")
	public ResponseModel conclusionAssessmentPro(@RequestBody ConclusionAssessmentProModel models) {
		String methodName = "conclusionAssessmentPro";
		try {
			log.info("Request : Saving Survey Wizad conclusionAssessmentPro,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return saveSurveyWizadService.conclusionAssessmentPro(models);

		} catch (Exception e) {
			log.error("An error occurred while Saving LabourAssessmentPro" + "  Method Name" + methodName + " Class : " + this.getClass(), e);
			return null;
		}
	}
	
	@GetMapping("/view/conclusionAssessment")
	public ResponseModel viewconclusionAssessment(@RequestParam String companyGenId,@RequestParam Long insurenceGenId ) {
		String methodName = "viewconclusionAssessment";
		try {
			log.info("Request : Saving Survey Wizad ViewLabourAssessmentPro,  " + "  Method Name" + methodName + " Class : " + this.getClass());

			return saveSurveyWizadService.viewconclusionAssessment(companyGenId,insurenceGenId);

		} catch (Exception e) {
			log.error("An error occurred while view conclusionAssessment Survey Wizad", e);
			return null;
		}
	}
//	
//    @GetMapping("/summary/calculation")
//    public ResponseModel getSummary(
//    		
//            @RequestParam String companyGenId,
//            @RequestParam Long insurenceGenId) {
//    	try {
//    	 return saveSurveyWizadService.getAggregatedValues(companyGenId, insurenceGenId);
//    	} catch (Exception e) {
//			log.error("An error occurred while deletePartAndLabourAssessmentPro", e);
//			return null;
//		}
//    }
	
	//Photo wizard 
	
	@PostMapping("/save/StatusPhotoWizard")
	public ResponseModel StatusPhotoWizard(@RequestBody PhotoWizardModel model) {
		String methodName = "StatusPhotoWizard";
		try {
			
			return saveSurveyWizadService.StatusPhotoWizard(model);

		} catch (Exception e) {
			log.error("An error occurred while Saving StatusPhotoWizard", e);
			return null;
		}
	}
	
	
}
