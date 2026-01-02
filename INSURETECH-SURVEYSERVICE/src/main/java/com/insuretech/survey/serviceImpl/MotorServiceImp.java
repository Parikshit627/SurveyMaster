package com.insuretech.survey.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.LinkGeneratedHistory;
import com.insuretech.survey.entity.Status;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.entity.VehicleDetails;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.MotorService;

import jakarta.persistence.EntityManager;

@Service
public class MotorServiceImp extends AbstractMasterRepository implements MotorService {

	Logger log = LoggerFactory.getLogger(MotorServiceImp.class);

	@Autowired
	private EntityManager entityManager;

//	Change -- Aman -- Start
	@Override
	public ResponseModel getMotorDashboard(String userSurveyorLoginId, List<String> roleId, String companyRegGenId,
			List<String> dashboardRange, String dashboardType, String branchId) {
//		Change -- Aman -- End
		String methodName = "getMotorDashboard";
		ResponseModel response = new ResponseModel();
		List<UserSurveyorBasicDetails> userSurveyorBasicDetailsListRespond = new ArrayList<>();

		try {
			log.info("Finding Finding all motor request data by userSurveyorLoginId: " + userSurveyorLoginId
					+ " And companyRegGenId: " + companyRegGenId + " And roleId: " + roleId + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			
//			Change -- Aman -- Start
			boolean isAdmin = roleId.contains(CommonConstants.ADMIN_ROLE);
			boolean isInitiator = roleId.contains(CommonConstants.INITIATOR_ROLE);
			
			// Extract offset and limit from dashboardRange
	        int offset = Integer.parseInt(dashboardRange.get(0)); // e.g., 0
	        int limit = Integer.parseInt(dashboardRange.get(1));
	        
	        Long branchIdLong = null;
	        if (!branchId.equalsIgnoreCase("null")) {
	        		branchIdLong = Long.parseLong(branchId);
	        }
//			Change -- Aman -- End
			
			
			List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList =  new ArrayList<>();
			
//			Change -- Aman -- Start
			//Admin
			if(isAdmin) {
				userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
						.findByCompanyGenIdAndDepartmentIgnoreCaseOrderByInsuranceClaimIdDesc(companyRegGenId,CommonConstants.DEPARTMENT_TYPE, dashboardType, offset, limit, branchIdLong);
			//isInitiator
			}if(isInitiator) {
			userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
					.findByCreatedByAndCompanyGenIdAndDepartmentIgnoreCaseOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId,CommonConstants.DEPARTMENT_TYPE, dashboardType, offset, limit, branchIdLong);
//			Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
			}if (!isInitiator && !isAdmin) {
				userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
						.findByAssignToAndCompanyGenIdAndDepartmentIgnoreCaseOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId,CommonConstants.DEPARTMENT_TYPE, dashboardType, offset, limit, branchIdLong);
			}
//			Change -- Aman -- End
			if (userSurveyorBasicDetailsList.size() > 0) {

				for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
					Status status = new Status();
//					Change -- Aman -- Start
					status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());
//					Change -- Aman -- End
					userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

					log.info("Finding Link details by insuranceClaimId: "
							+ userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
							+ " Class : " + this.getClass());
					List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
							.findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
									userSurveyorBasicDetails.getCompanyGenId());
					userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

					userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);
					try {
						VehicleDetails  vehicleDetails = vehicleDetailsRepo.findByInsurenceGenIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
										userSurveyorBasicDetails.getCompanyGenId());
						userSurveyorBasicDetails.setVehicleType(vehicleDetails.getMakeVariant());
						userSurveyorBasicDetails.setType(vehicleDetails.getVehicleType());
						userSurveyorBasicDetails.setInsured(vehicleDetails.getRegOwner());

					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				response.setMessage("Data found");
				response.setData(userSurveyorBasicDetailsListRespond);
				response.setHttpStatus(HttpStatus.OK);
			}

			log.info("Respond : data founded successfully for by userSurveyorLoginId: " + userSurveyorLoginId
					+ " And companyRegGenId: " + companyRegGenId + " And roleId: " + roleId + "  Method Name"
					+ methodName + " Class : " + this.getClass());

		} catch (Exception e) {
			log.error(
					"An error occurred while Finding all request by userSurveyorLoginId: " + userSurveyorLoginId + " And companyRegGenId: "
							+ companyRegGenId + " And roleId: " + roleId + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

}
