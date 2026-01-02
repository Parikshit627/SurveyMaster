package com.insuretech.survey.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.JasperReportService;



@RestController
@RequestMapping("/report")
public class JasperReportController {

    Logger log = LoggerFactory.getLogger(JasperReportController.class);

    @Autowired
    JasperReportService jasperReportService;

    @PostMapping("/generatesSpotOrFinalReport")
	public ResponseModel generatesSpotOrFinalReport(@RequestParam Long insurenceGenId,
                                              @RequestParam String companyGenId) {
		String methodName = "generateJasperReport";
		try {
            log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                    + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                    + this.getClass());
			return jasperReportService.generatesSpotOrFinalReport(insurenceGenId, companyGenId);

		} catch (Exception e) {
			log.error("An error occurred while generating the Jasper report: ", e);
			return null;
		}
	}
    @PostMapping("/generateRiReport")
	public ResponseModel generateRiReport(@RequestParam Long insurenceGenId,
                                          @RequestParam String companyGenId) {
		String methodName = "generateRiReport";
		try {
            log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                    + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                    + this.getClass());
			return jasperReportService.generateRiReport(insurenceGenId, companyGenId);

		} catch (Exception e) {
			log.error("An error occurred while generating the Jasper report: ", e);
			return null;
		}
	}
    
    @PostMapping("/generateScrutinySheetReport")
   	public ResponseModel generateScrutinySheetReport(@RequestParam Long insurenceGenId,
                                             @RequestParam String companyGenId) {
   		String methodName = "generateScrutinySheetReport";
   		try {
               log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                       + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                       + this.getClass());
   			return jasperReportService.generateScrutinySheetReport(insurenceGenId, companyGenId);

   		} catch (Exception e) {
   			log.error("An error occurred while generating the Jasper report: ", e);
   			return null;
   		}
   	}
    
    @PostMapping("/generateBillReport")
   	public ResponseModel generateBillReport(@RequestParam String reference_number,@RequestParam Long insurenceGenId,
                                             @RequestParam String companyGenId) {
   		String methodName = "generateScrutinySheetReport";
   		try {
               log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                       + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                       + this.getClass());
   			return jasperReportService.generateBillReport(reference_number,insurenceGenId, companyGenId);

   		} catch (Exception e) {
   			log.error("An error occurred while generating the Jasper report: ", e);
   			return null;
   		}
   	}
    
    @PostMapping("/generateDetailedAssessmentReport")
   	public ResponseModel generateDetailedAssessmentReport(@RequestParam Long insurenceGenId,
                                             @RequestParam String companyGenId) {
   		String methodName = "generateDetailedAssessmentReport";
   		try {
               log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                       + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                       + this.getClass());
   			return jasperReportService.generateDetailedAssessmentReport(insurenceGenId, companyGenId);

   		} catch (Exception e) {
   			log.error("An error occurred while generating the Jasper report: ", e);
   			return null;
   		}
   	}
    
    @PostMapping("/generateComDetailedAssesReport")
   	public ResponseModel generateCommericalDetailedAssessmentReport(@RequestParam Long insurenceGenId,
                                             @RequestParam String companyGenId) {
   		String methodName = "generateCommericalDetailedAssessmentReport";
   		try {
               log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                       + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                       + this.getClass());
   			return jasperReportService.generateCommericalDetailedAssessmentReport(insurenceGenId, companyGenId);

   		} catch (Exception e) {
   			log.error("An error occurred while generating the Jasper report: ", e);
   			return null;
   		}
   	}
    
    @PostMapping("/generateComFinalReport")
   	public ResponseModel generateComFinalReport(@RequestParam Long insurenceGenId,
                                             @RequestParam String companyGenId) {
   		String methodName = "generateComFinalReport";
   		try {
               log.info("Request : Finding  All Details companyGenId , insurenceGenId, userId  " + "companyGenId : " + companyGenId
                                       + ", insurenceGenId : " + insurenceGenId +" Method Name" + methodName + " Class : "
                                       + this.getClass());
   			return jasperReportService.generateComFinalReport(insurenceGenId, companyGenId);

   		} catch (Exception e) {
   			log.error("An error occurred while generating the Jasper report: ", e);
   			return null;
   		}
   	}
}
