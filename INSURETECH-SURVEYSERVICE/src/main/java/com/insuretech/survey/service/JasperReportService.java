package com.insuretech.survey.service;

import com.insuretech.survey.model.ResponseModel;

public interface JasperReportService {

    ResponseModel generatesSpotOrFinalReport(Long insurenceGenId, String companyGenId);
    ResponseModel generateRiReport(Long insurenceGenId, String companyGenId);
    ResponseModel generateScrutinySheetReport(Long insurenceGenId, String companyGenId);
    ResponseModel generateBillReport(String reference_number,Long insurenceGenId,String companyGenId);
    ResponseModel generateDetailedAssessmentReport(Long insurenceGenId, String companyGenId);
    
    ResponseModel generateCommericalDetailedAssessmentReport(Long insurenceGenId, String companyGenId);
    
    ResponseModel generateComFinalReport(Long insurenceGenId, String companyGenId);

}
