package com.insuretech.survey.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.CommonMailModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.serviceImpl.EmailServiceImpl;

@RestController
@RequestMapping("/email")
public class EmailController {

	Logger log = LoggerFactory.getLogger(EmailController.class);

	@Autowired
	EmailServiceImpl emailServiceImpl;

	@PostMapping("/sendRegistationEmail")
	public ResponseModel sendRegistationEmail(@RequestBody CommonMailModel commonMailModel){
		String methodName = "sendRegistationEmail";
		try {
			log.info("Request : sending Registation email ,comapnayGenId : " + commonMailModel.getCompanyGenId()
			+ "  Method Name" + methodName + " Class : " + this.getClass());
			return emailServiceImpl.sendEmail(commonMailModel);

		} catch (Exception e) {
			log.error("An error occurred while generating the Jasper report: ", e);
			return null;
		}
	}
	
	@PostMapping("/sendDocRequirementEmail")
	public ResponseModel sendDocRequirementEmail(@RequestBody CommonMailModel commonMailModel){
		String methodName = "sendDocRequirementEmail";
		try {
			log.info("Request : sending email ,comapnayGenId : " + commonMailModel.getCompanyGenId()
			+ "  Method Name" + methodName + " Class : " + this.getClass());
			return emailServiceImpl.sendDocRequirementEmail(commonMailModel);

		} catch (Exception e) { 
			log.error("An error occurred while generating the Jasper report: ", e);
			return null;
		}
	}
	
	@GetMapping("/getEmailHistoryByReferenceNo")
	public ResponseModel getEmailHistoryByReferenceNo(@RequestParam String referenceNo){
		String methodName = "getEmailHistoryByReferenceNo";
		try {
			log.info("Request: get email history for reference No : "
					+  referenceNo + ", Method Name: " + methodName + " Class: " + this.getClass());
			return emailServiceImpl.getEmailHistoryByReferenceNo(referenceNo);

		} catch (Exception e) { 
			log.info("Respond: Error occurred while fetching email history for reference No : "
					+ referenceNo + ", Method Name: " + methodName + " Class: " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/sendDirectEmail")
	public ResponseModel sendDirectEmail(@RequestBody CommonMailModel commonMailModel){
		String methodName = "sendDirectEmail";
		try {
			log.info("Request: Sending direct email to Email Id: " + commonMailModel.getTo() + " subject: " + commonMailModel.getSubject() + " Method Name: "
					+ methodName + " Class: " + this.getClass());
			
			return emailServiceImpl.sendDirectEmail(commonMailModel);

		} catch (Exception e) { 
			log.info("Respond: Error occurred while sending direct email " + " subject: " + commonMailModel.getSubject() + " Method Name: "
					+ methodName + " Class: " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/sendFinalSubSurveyWizardEmail")
	public ResponseModel sendFinalSubSurveyWizardEmail(@RequestBody CommonMailModel commonMailModel){
		String methodName = "sendFinalSubSurveyWizardEmail";
		try {
			log.info("Request : Sending email for survey Wizard Final Submission  by email id: "
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			
			return emailServiceImpl.sendFinalSubSurveyWizardEmail(commonMailModel);

		} catch (Exception e) { 
			log.error("An error occurred while sending email " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/sendAssessmentEmail")
	public ResponseModel sendAssessmentEmail(@RequestBody CommonMailModel commonMailModel){
		String methodName = "sendFinalSubSurveyWizardEmail";
		try {
			log.info("Request : Sending email for survey Wizard Assessment  by email id: "
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : " + this.getClass());
			
			return emailServiceImpl.sendAssessmentEmail(commonMailModel);

		} catch (Exception e) { 
			log.error("An error occurred while sending email " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			return null;
		}
	}
	
	@PostMapping("/sendIntimationEmail")
	public ResponseModel sendIntimationEmail(@RequestBody CommonMailModel commonMailModel){
		String methodName = "sendIntimationEmail";
		try {
			log.info("Request : Sending email for Intimation form by email id: "
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName
					+ " Class : " + this.getClass());
			
			return emailServiceImpl.sendIntimationEmail(commonMailModel);

		} catch (Exception e) { 
			log.error("Response :error --  Email Send unsuccessfull for Intimation form by email id: "
					+ commonMailModel.getCompanyGenId() + "error :"+e.getLocalizedMessage()+"  Method Name" + methodName
					+ " Class : " + this.getClass());
			return null;
		}
	}
}
