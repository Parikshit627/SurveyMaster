package com.insuretech.survey.service;

import java.util.List;

import com.insuretech.survey.model.ConclusionAssessmentProModel;
import com.insuretech.survey.model.FinalSubmissionRequest;
import com.insuretech.survey.model.LabourAssessmentProJson;
import com.insuretech.survey.model.LabourAssessmentProModel;
import com.insuretech.survey.model.PartAssessmentProModel;
import com.insuretech.survey.model.PartsAssessmentProJson;
import com.insuretech.survey.model.PhotoWizardModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SurveyWizardModel;
import com.insuretech.survey.model.WorkflowModel;

public interface SaveSurveyWizadService {

	ResponseModel saveSurveyWizad(SurveyWizardModel surveyWizardModel);
	
	ResponseModel getSurveyWizad(String companyGenId,Long insurenceGenId,String workedByUserId );
	
	ResponseModel finalDocumentSubmmsion(FinalSubmissionRequest finalSubmissionRequest);

	ResponseModel workflowBySurveyor(WorkflowModel workflowModel);

	ResponseModel ViewAssessmentPro(String companyGenId, Long insurenceGenId);

	ResponseModel LabourAssessmentPro(LabourAssessmentProJson models);

	ResponseModel ViewLabourAssessmentPro(String companyGenId, Long insurenceGenId);

	ResponseModel deletePartAndLabourAssessmentPro(Long id, Long insurenceGenId);

	ResponseModel ViewAssessmentProSabRowNo(String companyGenId, Long insurenceGenId, Integer sabRowNo);

	ResponseModel saveAssesmentPro(PartsAssessmentProJson models);

	ResponseModel conclusionAssessmentPro(ConclusionAssessmentProModel models);

	ResponseModel viewconclusionAssessment(String companyGenId, Long insurenceGenId);

	ResponseModel StatusPhotoWizard(PhotoWizardModel model);

//	ResponseModel getAggregatedValues(String companyGenId, Long insurenceGenId);
	
}
