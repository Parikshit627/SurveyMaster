package com.insuretech.survey.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.model.ApplicationDashboardModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationServiceImpl extends AbstractMasterRepository implements ApplicationService {
	
	

@Override
public ResponseModel apkDashboard() {
 String methodName="apkDashboard";

    try {
    	log.info("Fetching dashboard data...");

    	List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo.findAll();
    	log.debug("Retrieved {} surveyor records.", userSurveyorBasicDetailsList.size());

    	// Map each UserSurveyorBasicDetails to ApplicationDashboardModel
    	List<ApplicationDashboardModel> dashboardModels = userSurveyorBasicDetailsList.stream()
    	        .map(surveyor -> {
    	            ApplicationDashboardModel model = new ApplicationDashboardModel();
    	            BeanUtils.copyProperties(surveyor, model);
    	            return model;
    	        })
    	        .collect(Collectors.toList());

    	log.info("Dashboard data prepared successfully. method Name :"+methodName+" class :"+getClass());

    	// Wrap in ResponseModel
    	ResponseModel response = new ResponseModel();
    	response.setHttpStatus(HttpStatus.SC_OK);  // Use 200 instead of SC_SUCCESS
    	response.setMessage("Dashboard data fetched successfully.");
    	response.setData(dashboardModels);  // Return the list

    	return response;

    } catch (Exception e) {
        log.error("Error fetching dashboard data.", e);

        ResponseModel response = new ResponseModel();
        response.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);  // 500 error
        response.setMessage("Failed to fetch dashboard data: " + e.getMessage());
        response.setData(null);

        return response;
    }
}

	

}
