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

import com.insuretech.survey.model.ComponentDetailsModel;
import com.insuretech.survey.model.ImageAnnotationModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.AiDataService;

@RestController
@RequestMapping("/aiData")
public class AiDataController {

    Logger log = LoggerFactory.getLogger(AiDataController.class);

    @Autowired
    AiDataService aiDataService;

    @PostMapping("/saveAnnotation")
    public ResponseModel saveAnnotation(@RequestBody List<ImageAnnotationModel> imageAnnotationModel) {
        String methodName = "saveAnnotation";
        try {
            log.info("Request : Saving annotation data, Method Name: " + methodName + ", Class: " + this.getClass());
            return aiDataService.saveAnnotation(imageAnnotationModel);
        } catch (Exception e) {
            log.error("An error occurred while saving annotation", e);
            return null;
        }
    }

    @GetMapping("/getAnnotationByInsuranceGenId")
    public ResponseModel getAnnotationByInsuranceGenId(@RequestParam String insuranceGenId) {
        String methodName = "getAnnotationByInsuranceGenId";
        try {
            log.info("Request : Fetching annotation data for insuranceGenId: " + insuranceGenId + ", Method Name: " + methodName + ", Class: " + this.getClass());
            return aiDataService.getAnnotationByInsuranceGenId(insuranceGenId);
        } catch (Exception e) {
            log.error("An error occurred while fetching annotation for insuranceGenId: " + insuranceGenId, e);
            return null;
        }
    }
    
    @GetMapping("/deleteBoxByInsuranceGenIdAndBoxId")
    public ResponseModel deleteBoxByInsuranceGenIdAndBoxId(@RequestParam String insuranceGenId,@RequestParam String uuid, @RequestParam String boxId) {
        String methodName = "deleteBoxByInsuranceGenIdAndBoxId";
        try {
            log.info("Request : Deleting annotation data for insuranceGenId: " + insuranceGenId + " and BoxId: " + boxId + " and uuid: " + uuid + " Method Name: " + methodName + ", Class: " + this.getClass());
            return aiDataService.deleteBoxByInsuranceGenIdAndBoxId(insuranceGenId, uuid, boxId);
        } catch (Exception e) {
            log.error("An error occurred while Deleting annotation data for insuranceGenId: " + insuranceGenId + " and BoxId: " + " and uuid: " + uuid + boxId, e);
            return null;
        }
    }
    
    @PostMapping("/saveComponentDetails")
	public ResponseModel saveComponentDetails(@RequestBody ComponentDetailsModel componentDetailsModel) {
		String methodName = "saveComponentDetails";
		ResponseModel response = new ResponseModel();
		try {
             
			log.info("Resuest : save component Details by insurenceGenId : " + componentDetailsModel.getInsurenceGenId()
			+ ", companyGenId : " + componentDetailsModel.getCompanyGenId() + "  Method Name" + methodName
			+ " Class : " + this.getClass());
			
			if(componentDetailsModel.getInsurenceGenId()!=null && componentDetailsModel.getInsurenceGenId() > 0L){		
				response= aiDataService.saveComponentDetails(componentDetailsModel);
			}else {
				response.setMessage("Invalid parameter -- InsurenceGenId ");
				response.setHttpStatus(HttpStatus.BAD_REQUEST);
			}
				
            
		} catch (Exception e) {
			log.error(
					"Response : An error occurred while Saving component Details by insurenceGenId :  "
							+ componentDetailsModel.getInsurenceGenId() + ", companyGenId : "
							+ componentDetailsModel.getCompanyGenId() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
		
			response.setMessage("error : "+e.getLocalizedMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
		return response;
	}
    
    @GetMapping("/getComponentDetails")
    public ResponseModel getComponentDetails(@RequestParam Long insuranceGenId) {
   		String methodName = "getComponentDetails";
   		ResponseModel response = new ResponseModel();
   		try {
                
   			log.info("Resuest : fetch component Details by insurenceGenId : " + insuranceGenId
					 + "  Method Name" + methodName
					+ " Class : " + this.getClass());
   			
   			if(insuranceGenId!=null && insuranceGenId > 0L){		
   				response= aiDataService.getComponentDetails(insuranceGenId);
   			}else {
   				response.setMessage("Invalid parameter -- InsurenceGenId ");
   				response.setHttpStatus(HttpStatus.BAD_REQUEST);
   			}
   				
   		} catch (Exception e) {
   			log.error(
					"Response : An error occurred while fetching component Details by insurenceGenId :  "
							+ insuranceGenId  + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
   			e.printStackTrace();
   		
   			response.setMessage("error : "+e.getLocalizedMessage());
   			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
   		
   		}
   		
   		return response;
   	}
    
}