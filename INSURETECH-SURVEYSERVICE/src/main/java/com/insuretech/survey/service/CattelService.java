package com.insuretech.survey.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.insuretech.survey.model.CattleIntimationDetailsModel;
import com.insuretech.survey.model.ResponseModel;

public interface CattelService {

	ResponseModel saveCattelIntimationDetails(CattleIntimationDetailsModel cattelIntimationDetailsModel);

	ResponseModel getCattelIntimationDetails(Long cattleIntimationGenId, String referenceNo);

	ResponseModel geCatteltDashboardData(String userSurveyorLoginId, String companyRegGenId,String roleId,int page,int size);

}
