package com.insuretech.survey.service;

import java.util.List;
import java.util.UUID;

import com.insuretech.survey.model.ComponentDetailsModel;
import com.insuretech.survey.model.ImageAnnotationModel;
import com.insuretech.survey.model.ResponseModel;

public interface AiDataService {

	ResponseModel saveAnnotation(List<ImageAnnotationModel> imageAnnotationModel);

	ResponseModel getAnnotationByInsuranceGenId(String insuranceGenId);

	ResponseModel deleteBoxByInsuranceGenIdAndBoxId(String insuranceGenId, String uuid, String boxId);
	
	ResponseModel saveComponentDetails(ComponentDetailsModel componentDetailsModel);
	
	ResponseModel getComponentDetails(Long insuranceGenId);
	
	
}
