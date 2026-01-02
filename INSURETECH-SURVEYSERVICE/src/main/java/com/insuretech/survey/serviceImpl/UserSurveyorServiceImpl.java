package com.insuretech.survey.serviceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.DamageDetails;
import com.insuretech.survey.entity.Efficiency;
import com.insuretech.survey.entity.LinkGeneratedHistory;
import com.insuretech.survey.entity.PhotoPendingAi;
import com.insuretech.survey.entity.PolicyDetails;
import com.insuretech.survey.entity.ReferenceNoCounter;
import com.insuretech.survey.entity.Status;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.entity.VehicleDetails;
import com.insuretech.survey.model.AssignApprovalModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveUserSurveyorBasicDetailsModel;
import com.insuretech.survey.service.UserSurveyorService;

@Service
public class UserSurveyorServiceImpl extends AbstractMasterRepository implements UserSurveyorService {

    Logger log = LoggerFactory.getLogger(UserSurveyorServiceImpl.class);

    @Override
    public ResponseModel saveBasicDetails(SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel) {
        String methodName = "saveBasicDetails";
        ResponseModel response = new ResponseModel();

        try {
            log.info("Saving user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            UserSurveyorBasicDetails userSurveyorBasicDetails = new UserSurveyorBasicDetails();
            BeanUtils.copyProperties(saveUserSurveyorBasicDetailsModel, userSurveyorBasicDetails);
            String cleanAsset = userSurveyorBasicDetails.getAsset() != null
                    ? userSurveyorBasicDetails.getAsset().trim()
                    : null;
            
            userSurveyorBasicDetails.setAsset(cleanAsset);
            userSurveyorBasicDetails.setReferenceNo(generateRefrenceNo(saveUserSurveyorBasicDetailsModel));
            userSurveyorBasicDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            userSurveyorBasicDetails.setCurrentStatus(CommonConstants.BASIC_INITIAL_DETAILS);
            // Change -- Aman -- Start
            userSurveyorBasicDetails.setAssignTo(saveUserSurveyorBasicDetailsModel.getAssistantEmail());
            userSurveyorBasicDetails.setDocSubmitted(false);
            // Change -- Aman -- End
            userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);

            Efficiency efficiency = efficiencyRepo.findByUserName(userSurveyorBasicDetails.getCreatedBy());
            efficiency.setUsedCredit(efficiency.getUsedCredit() + 1);
            efficiencyRepo.save(efficiency);

            log.info("Request: Saving Surveyor Link details userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());
            SaveLinkDetais(userSurveyorBasicDetails);

            response.setData(userSurveyorBasicDetails);
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Data save Successfully");

            log.info(
                    "Respond : data save successfully for user Surveyor basic (initial) details by userSurveyorLoginId: "
                            + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName
                            + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Saving user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return response;
    }

    private String generateRefrenceNo(SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel) {
        String methodName = "generateRefrenceNo";
        String refrenceNo = "";
        try {
            log.info("Generateting RefrenceNo by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            // financial year
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int nextYearShort = (currentYear + 1) % 100;
            String financialYear = currentYear + "-" + String.format("%02d", nextYearShort);

            // code,abbrevation,counter
//          ReferenceNoCounter referenceNoCounter = referenceNoCounterRepo
//                  .findByRegCode(saveUserSurveyorBasicDetailsModel.getRegCode());
            
            // Change -- Aman -- Start
            ReferenceNoCounter referenceNoCounter = referenceNoCounterRepo.findByCreatedBy(saveUserSurveyorBasicDetailsModel.getCompanyGenId());
            // Change -- Aman -- End
            
            // Retrieve the top entry ordered by fileNo in descending order
//          UserSurveyorBasicDetails topEntry = userSurveyorBasicDetailsRepo.findFirstByOrderByInsuranceClaimIdDesc();

            if (referenceNoCounter != null) {
                String dateStr = currentYear + "-03-31 23:59:59";
                Timestamp march31EndTimestamp = Timestamp.valueOf(dateStr);
                Timestamp updatedDtm = referenceNoCounter.getUpdatedDtm();
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

                if (!updatedDtm.after(march31EndTimestamp) && currentTimestamp.after(march31EndTimestamp)) {
                    referenceNoCounter.setCounter(1);
                } else {
                    referenceNoCounter.setCounter(referenceNoCounter.getCounter() + 1);
                }

                // code/abbrevation/financial year/counter
                refrenceNo = referenceNoCounter.getRegCode() + "/" + referenceNoCounter.getRegAbbreviation() + "/"
                        + financialYear + "/" + referenceNoCounter.getCounter();
                
                referenceNoCounter.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
                referenceNoCounter.setUpdatedBy(saveUserSurveyorBasicDetailsModel.getCompanyGenId());
                referenceNoCounterRepo.save(referenceNoCounter);
                
                log.info("ReferenceNoCounter updated successfully " + "  Method Name" + methodName + " Class : "
                        + this.getClass());
                
            } else {
                log.error("No data found in db by  RegCode : " + saveUserSurveyorBasicDetailsModel.getRegCode() +
                        "  Method Name" + methodName + " Class : " + this.getClass());
            }

        } catch (Exception e) {
            log.error("An error occurred while generate RefrenceNo by userSurveyorLoginId: " + e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
            e.printStackTrace();
        }
        return refrenceNo;
    }

    private LinkGeneratedHistory SaveLinkDetais(UserSurveyorBasicDetails userSurveyorBasicDetails) {
        String methodName = "saveBasicDetails";
        LinkGeneratedHistory linkGeneratedHistory = new LinkGeneratedHistory();

        try {
            log.info("Saving Surveyor Link details userSurveyorLoginId: " + userSurveyorBasicDetails.getCreatedBy()
                    + "  Method Name" + methodName + " Class : " + this.getClass());

            linkGeneratedHistory.setCreatedBy(userSurveyorBasicDetails.getCreatedBy());
            linkGeneratedHistory.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
            linkGeneratedHistory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            linkGeneratedHistory.setInsuranceClaimId(userSurveyorBasicDetails.getInsuranceClaimId());
            linkGeneratedHistory.setLinkExpired(CommonConstants.LINK_EXPIRED_FALSE);
            linkGeneratedHistory.setLinkName(CommonConstants.LINK_TYPE);
            linkGeneratedHistory.setLink(CommonConstants.SURVEY_LINK);

            linkGeneratedHistory = linkGeneratedHistoryRepo.save(linkGeneratedHistory);

            log.info("Respond : data save successfully for Surveyor Link details userSurveyorLoginId: "
                    + userSurveyorBasicDetails.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while SSaving Surveyor Link details userSurveyorLoginId: " + e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return linkGeneratedHistory;
    }

    @Override
    public ResponseModel getBasicDetails(Long insuranceClaimId, String referenceNumber, String subjectMatter,
            String companyRegGenId) {
        String methodName = "getBasicDetails";
        ResponseModel response = new ResponseModel();

        try {
            UserSurveyorBasicDetails userSurveyorBasicDetails = new UserSurveyorBasicDetails();
            if (insuranceClaimId != null) {
                log.info("Finding all basic details by insuranceClaimId: " + insuranceClaimId + " And companyRegGenId: "
                        + companyRegGenId + "  Method Name" + methodName + " Class : " + this.getClass());

                userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
                        .findByInsuranceClaimIdAndCompanyGenId(insuranceClaimId, companyRegGenId);
            } else if (!referenceNumber.isEmpty()) {
                log.info("Finding all basic details by referenceNumber: " + referenceNumber + " And companyRegGenId: "
                        + companyRegGenId + "  Method Name" + methodName + " Class : " + this.getClass());

                userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
                        .findByReferenceNoAndCompanyGenId(referenceNumber, companyRegGenId);
            } else if (!subjectMatter.isEmpty() || subjectMatter != null) {
                log.info("Finding all basic details by asset: " + subjectMatter + " And companyRegGenId: "
                        + companyRegGenId + "  Method Name" + methodName + " Class : " + this.getClass());

                userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.findByAssetAndCompanyGenId(subjectMatter,
                        companyRegGenId);
            }

            log.info("Finding Link details by insuranceClaimId: " + userSurveyorBasicDetails.getInsuranceClaimId()
                    + " And companyRegGenId: " + companyRegGenId + "  Method Name" + methodName + " Class : "
                    + this.getClass());
            List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                    .findByInsuranceClaimIdAndCompanyGenId(insuranceClaimId, companyRegGenId);

            Status status = new Status();
            status = statusRepo.findByStatusId(CommonConstants.BASIC_INITIAL_DETAILS);
            userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));
            userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

            if (userSurveyorBasicDetails != null) {
                response.setMessage("Data found");
                response.setData(userSurveyorBasicDetails);
                response.setHttpStatus(HttpStatus.OK);
            }

            log.info("Respond : data founded successfully for insuranceClaimId: "
                    + userSurveyorBasicDetails.getInsuranceClaimId() + " And companyRegGenId: " + companyRegGenId
                    + "  Method Name" + methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Finding all basic details by insuranceClaimId: " + e.getMessage()
                    + " And companyRegGenId: " + companyRegGenId + "  Method Name" + methodName + " Class : "
                    + this.getClass());
        }
        return response;
    }

//  Change -- Aman -- Start
    @Override
    public ResponseModel getUserSurveyorDashboard(String userSurveyorLoginId, String companyRegGenId, List<String> roleId,
            List<String> dashboardRange, String dashboardType, String branchId) {
        String methodName = "getUserSurveyorDashboard";
        ResponseModel response = new ResponseModel();
        List<UserSurveyorBasicDetails> userSurveyorBasicDetailsListRespond = new ArrayList<>();

        try {
            
            boolean isAdmin = roleId.contains(CommonConstants.ADMIN_ROLE);
            boolean isInitiator = roleId.contains(CommonConstants.INITIATOR_ROLE);
            
            // Extract offset and limit from dashboardRange
            int offset = Integer.parseInt(dashboardRange.get(0)); // e.g., 0
            int limit = Integer.parseInt(dashboardRange.get(1));
            
            Long branchIdLong = null;
            if (!branchId.equalsIgnoreCase("null")) {
                    branchIdLong = Long.parseLong(branchId);
            }
            
            //Initiator
            if(isInitiator) {
                
                log.info("Request : Finding All Dashboard data for Initiator userSurveyorLoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: "+ companyRegGenId
                        + " and role Id: " + roleId + " dashboardRange: " + dashboardRange + " dashboardType: " + dashboardType 
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : "
                        + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByCreatedByAndCompanyGenIdAndDashboardAndForDashboardTypeRangeAndBranchIdOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId, dashboardType, offset, limit, branchIdLong);
                
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());
                        
                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));
                        
                        VehicleDetails vehicleDetail=vehicleDetailsRepo.findByInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId()); 
                        if(vehicleDetail!=null) {
                        	userSurveyorBasicDetails.setVehicleType(vehicleDetail.getVehicleType());
                        }
                        
                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
            //  Admin -- company-genId
            }if(isAdmin){
                log.info("Request : Finding All Dashboard data for Admin userSurveyorLoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: "+ companyRegGenId
                        + " and role Id: " + roleId + " dashboardRange: " + dashboardRange + " dashboardType: " + dashboardType 
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByCompanyGenIdAndDashboardAndForDashboardTypeRangeAndBranchIdOrderByInsuranceClaimIdDesc(companyRegGenId, dashboardType, offset, limit, branchIdLong);
                
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        VehicleDetails vehicleDetail=vehicleDetailsRepo.findByInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId()); 
                        if(vehicleDetail!=null) {
                        	userSurveyorBasicDetails.setVehicleType(vehicleDetail.getVehicleType());
                        }
                        
                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
//          Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
            }if (!isInitiator || !isAdmin) {

                log.info("Request: Finding all request for Assign dept by LoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: " + companyRegGenId 
                        + " and role Id: " + roleId + " dashboardRange: " + dashboardRange + " dashboardType: " + dashboardType 
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByAssignToAndCompanyGenIdAndForDashboardTypeRangeAndBranchIdOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId, dashboardType, offset, limit, branchIdLong);
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        VehicleDetails vehicleDetail=vehicleDetailsRepo.findByInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId()); 
                        if(vehicleDetail!=null) {
                        	userSurveyorBasicDetails.setVehicleType(vehicleDetail.getVehicleType());
                        }
                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));
 
                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
            }

            response.setMessage("Data found");
            response.setData(userSurveyorBasicDetailsListRespond);
            response.setHttpStatus(HttpStatus.OK);

            log.info("Respond : data founded successfully for by userSurveyorLoginId: " + userSurveyorLoginId
                    + " And companyRegGenId: " + companyRegGenId 
                    + " and role Id: " + roleId + " dashboardRange: " + dashboardRange + " dashboardType: " + dashboardType 
                    + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Finding all request by userSurveyorLoginId: " + e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return response;
    }
//  Change -- Aman -- End
    
    

    @Override
    public ResponseModel updateBasicDetails(SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel) {
        String methodName = "updateBasicDetails";
        ResponseModel response = new ResponseModel();

        try {
            log.info("Update user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
                    .findByReferenceNo(saveUserSurveyorBasicDetailsModel.getReferenceNo());
            if (userSurveyorBasicDetails != null) {
                BeanUtils.copyProperties(saveUserSurveyorBasicDetailsModel, userSurveyorBasicDetails,
                        "insuranceClaimId", "companyGenId", "createdBy");
                userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                try {
                    VehicleDetails vehicleNo = vehicleDetailsRepo.findByInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
                    vehicleNo.setRegNo(saveUserSurveyorBasicDetailsModel.getAsset());
                    vehicleDetailsRepo.save(vehicleNo);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    PolicyDetails policydetails=policyDetailsRepo.findByInsurenceGenIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),userSurveyorBasicDetails.getCompanyGenId() );
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // or your required format

                    if (saveUserSurveyorBasicDetailsModel.getPolicyStartDate() != null 
                            && !saveUserSurveyorBasicDetailsModel.getPolicyStartDate().toString().trim().isEmpty()) {
                        policydetails.setPolicyFrom(sdf.format(saveUserSurveyorBasicDetailsModel.getPolicyStartDate()));
                    }

                    if (saveUserSurveyorBasicDetailsModel.getPolicyEndDate() != null 
                            && !saveUserSurveyorBasicDetailsModel.getPolicyEndDate().toString().trim().isEmpty()) {
                        policydetails.setPolicyTo(sdf.format(saveUserSurveyorBasicDetailsModel.getPolicyEndDate()));
                    }
                    policydetails.setPolicyNo(saveUserSurveyorBasicDetailsModel.getPolicyNo());
                    policydetails.setClaimNo(saveUserSurveyorBasicDetailsModel.getClaimNo());
                    policydetails.setAddress(saveUserSurveyorBasicDetailsModel.getInsuredAddress());
                    policydetails.setInsuredName(saveUserSurveyorBasicDetailsModel.getInsuredName());
                    
                    policyDetailsRepo.save(policydetails);
                } catch (Exception e) {
                    
                    
                    
                    
                    // TODO: handle exception
                }
                // Change -- Aman -- Start
                userSurveyorBasicDetails.setAssignTo(saveUserSurveyorBasicDetailsModel.getAssistantEmail());
                // Change -- Aman -- End
                userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);

                response.setData(userSurveyorBasicDetails);
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Data updated Successfully");
                log.info(
                        "Respond : data updated successfully for user Surveyor basic (initial) details by userSurveyorLoginId: "
                                + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
            } else {
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("No data found in Db against Insurance Claim Id : "
                        + saveUserSurveyorBasicDetailsModel.getInsuranceClaimId());
                log.info("No data found in Db against Insurance Claim Id : "
                        + saveUserSurveyorBasicDetailsModel.getInsuranceClaimId() + "  Method Name" + methodName
                        + " Class : " + this.getClass());
            }

        } catch (Exception e) {
            log.error("An error occurred while updating user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());

        }
        return response;
    }

    @Override
    public ResponseModel assignForApproval(AssignApprovalModel assignApprovalModel) {
        String methodName = "assignForApproval";
        ResponseModel response = new ResponseModel();

        try {
            log.info("Update user Surveyor details for assign approbal by LoginId: "
                    + assignApprovalModel.getUserId() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.findByInsuranceClaimId(assignApprovalModel.getInsuranceClaimId());
            if (userSurveyorBasicDetails != null) {
                userSurveyorBasicDetails.setBranchId(assignApprovalModel.getBranchId());
                userSurveyorBasicDetails.setBranch(assignApprovalModel.getBranch());;
                userSurveyorBasicDetails.setSurveyor(assignApprovalModel.getSurveyor());
                userSurveyorBasicDetails.setSurveyorEmail(assignApprovalModel.getSurveyorEmail());
                userSurveyorBasicDetails.setAssignTo(assignApprovalModel.getSurveyorEmail());
                userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                
                userSurveyorBasicDetails.setCurrentStatus(CommonConstants.SUBMIT_FOR_APPROVAL);
                
                userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
        
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Data updated Successfully");
                
                log.info(
                        "Respond : data updated successfully for user Surveyor details for assign approbal by LoginId: "
                                +  assignApprovalModel.getUserId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
            }
        }catch (Exception e) {
            log.error("An error occurred while updating user Surveyor details for assign approbal by LoginId: "
                    + e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return response;
    }

    
    //Change -- Aman -- Start
    @Override
    public ResponseModel getToadyDashboardData(String userSurveyorLoginId, String companyRegGenId, List<String> roleId,
            List<String> dashboardRange, String branchId) {
        String methodName = "getToadyDashboardData";
        ResponseModel response = new ResponseModel();
        List<UserSurveyorBasicDetails> userSurveyorBasicDetailsListRespond = new ArrayList<>();

        try {
            
            boolean isAdmin = roleId.contains(CommonConstants.ADMIN_ROLE);
            boolean isInitiator = roleId.contains(CommonConstants.INITIATOR_ROLE);
            
            // Extract offset and limit from dashboardRange
            int offset = Integer.parseInt(dashboardRange.get(0)); // e.g., 0
            int limit = Integer.parseInt(dashboardRange.get(1));
            
            Long branchIdLong = null;
            if (!branchId.equalsIgnoreCase("null")) {
                    branchIdLong = Long.parseLong(branchId);
            }
            
            LocalDate today = LocalDate.now();
            
            //Initiator
            if(isInitiator) {
                
                log.info("Request : Finding All Today Dashboard data for Initiator userSurveyorLoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: "+ companyRegGenId
                        + " and role Id: " + roleId + " dashboardRange: " + dashboardRange
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : "
                        + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByCreatedByAndCompanyGenIdAndBranchIdAndCreatedTodayOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId, offset, limit, branchIdLong, today);
                
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
            //  Admin -- company-genId
            }if(isAdmin){
                log.info("Request : Finding All Today Dashboard data for Admin userSurveyorLoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: "+ companyRegGenId
                        + " and role Id: " + roleId + " dashboardRange: " + dashboardRange
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByCompanyGenIdAndBranchIdAndCreatedTodayOrderByInsuranceClaimIdDesc(companyRegGenId, offset, limit, branchIdLong, today);
                
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
//          Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
            }if (!isInitiator || !isAdmin) {

                log.info("Request: Finding all Today request for Assign dept by LoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: " + companyRegGenId 
                        + " and role Id: " + roleId + " dashboardRange: " + dashboardRange
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByAssignToAndCompanyGenIdAndBranchIdAndCreatedTodayOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId, offset, limit, branchIdLong, today);
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
            }

            response.setMessage("Data found");
            response.setData(userSurveyorBasicDetailsListRespond);
            response.setHttpStatus(HttpStatus.OK);

            log.info("Respond : data founded successfully for Today by userSurveyorLoginId: " + userSurveyorLoginId
                    + " And companyRegGenId: " + companyRegGenId 
                    + " and role Id: " + roleId + " dashboardRange: " + dashboardRange
                    + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Finding all Today request by userSurveyorLoginId: " + e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return response;
    }
    //Change -- Aman -- End

    
//  Change -- Aman -- Start -28-08-25
    @Override
    public ResponseModel getAllDashboardDataBySearch(String userSurveyorLoginId, String companyRegGenId,
            List<String> roleId, String branchId, String searchText) {
        String methodName = "getAllDashboardDataBySearch";
        ResponseModel response = new ResponseModel();
        List<UserSurveyorBasicDetails> userSurveyorBasicDetailsListRespond = new ArrayList<>();

        try {
            
            boolean isAdmin = roleId.contains(CommonConstants.ADMIN_ROLE);
            boolean isInitiator = roleId.contains(CommonConstants.INITIATOR_ROLE);
            
            Long branchIdLong = null;
            if (!branchId.equalsIgnoreCase("null")) {
                    branchIdLong = Long.parseLong(branchId);
            }
            
            //Initiator
            if(isInitiator) {
                
                log.info("Request : Finding All Dashboard Data By Search by for Initiator userSurveyorLoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: "+ companyRegGenId
                        + " and role Id: " + roleId + " for text: " + searchText
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : "
                        + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByCreatedByAndCompanyGenIdAndBranchIdOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId, branchIdLong, searchText);
                
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
            //  Admin -- company-genId
            }if(isAdmin){
                log.info("Request : Finding All Dashboard Data By Search by for Admin userSurveyorLoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: "+ companyRegGenId
                        + " and role Id: " + roleId + " for text: " + searchText
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByCompanyGenIdAndBranchIdOrderByInsuranceClaimIdDesc(companyRegGenId, branchIdLong, searchText);
                
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
//          Other than Initiator & Admin -- means Assign dept -- Backoffice && Surveyor
            }if (!isInitiator || !isAdmin) {

                log.info("Request: Finding All Dashboard Data By Search by for Assign dept by LoginId: " + userSurveyorLoginId
                        + " And companyRegGenId: " + companyRegGenId 
                        + " and role Id: " + roleId +  " for text: " + searchText
                        + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

                List<UserSurveyorBasicDetails> userSurveyorBasicDetailsList = userSurveyorBasicDetailsRepo
                        .findByAssignToAndCompanyGenIdAndBranchIdOrderByInsuranceClaimIdDesc(userSurveyorLoginId, companyRegGenId, branchIdLong, searchText);
                if (userSurveyorBasicDetailsList.size() > 0) {

                    for (UserSurveyorBasicDetails userSurveyorBasicDetails : userSurveyorBasicDetailsList) {
                        Status status = new Status();
                        status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());

                        userSurveyorBasicDetails.setStatus(String.valueOf(status.getDescription()));

                        log.info("Finding Link details by insuranceClaimId: "
                                + userSurveyorBasicDetails.getInsuranceClaimId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                        List<LinkGeneratedHistory> linkGeneratedHistory = linkGeneratedHistoryRepo
                                .findByInsuranceClaimIdAndCompanyGenId(userSurveyorBasicDetails.getInsuranceClaimId(),
                                        userSurveyorBasicDetails.getCompanyGenId());
                        userSurveyorBasicDetails.setLinkGeneratedHistory(linkGeneratedHistory);

                        userSurveyorBasicDetailsListRespond.add(userSurveyorBasicDetails);

                    }

                }
            }

            response.setMessage("Data found");
            response.setData(userSurveyorBasicDetailsListRespond);
            response.setHttpStatus(HttpStatus.OK);

            log.info("Respond : data founded successfully for All Dashboard Data By Search by userSurveyorLoginId: " + userSurveyorLoginId
                    + " And companyRegGenId: " + companyRegGenId 
                    + " and role Id: " + roleId +  " for text: " + searchText
                    + " and branchId: " + branchId + "  Method Name" + methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Finding All Dashboard Data By Search by userSurveyorLoginId: " + e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return response;
    }
//  Change -- Aman -- End -28-08-25
    
    
//  Change -- Aman -- Start -01-09-25 
    @Override
    public ResponseModel getAllPhotoForAiCaseWise(String startDate, String endDate) {
        String methodName = "getAllPhotoForAiCaseWise";
        ResponseModel response = new ResponseModel();

        try {
        	
       	 log.info("Request : Finding All Photo for Ai from PhotoPendingAi startDate: " + startDate + " endDate: " + endDate
            		+ methodName + " Class : " + this.getClass());
        	
        	// Parse String dates to Timestamp with time set to 00:00:00 ---- yyyy-MM-dd
       	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

       	Timestamp startDateTimestamp = null;
       	Timestamp endDateTimestamp = null;

       	if (startDate != null && !startDate.trim().isEmpty()) {
       	    LocalDate parsed = LocalDate.parse(startDate, formatter);
       	    startDateTimestamp = Timestamp.valueOf(parsed.atStartOfDay());
       	}

       	if (endDate != null && !endDate.trim().isEmpty()) {
       	    LocalDate parsed = LocalDate.parse(endDate, formatter);
       	    endDateTimestamp = Timestamp.valueOf(parsed.atTime(LocalTime.MAX));
       	}  
            // Fetch data from repository
            List<PhotoPendingAi> photoPendingAiDataList = photoPendingAiRepo
                    .findAllByUploadTimeRange(startDateTimestamp, endDateTimestamp);
      
            //  Change -- Aman -- Start -04-09-25   
            List<Map<String, Object>> result = photoPendingAiDataList.stream()
            	    .filter(p -> p.getInsurenceGenId() != null)
            	    .collect(Collectors.groupingBy(PhotoPendingAi::getInsurenceGenId))
            	    .entrySet()
            	    .stream()
            	    // ✅ Filter only allowed statuses before mapping
            	    .filter(entry -> {
            	        Long insurenceGenId = entry.getKey();
            	        UserSurveyorBasicDetails userSurveyorBasicDetails =
            	                userSurveyorBasicDetailsRepo.findByInsuranceClaimId(insurenceGenId);

            	        Set<Integer> allowedStatuses = Set.of(1, 2, 3, 4, 5, 6, 12);

            	        return userSurveyorBasicDetails != null &&
            	               !allowedStatuses.contains(userSurveyorBasicDetails.getCurrentStatus());
            	    })
            	    .map(entry -> {
            	        Map<String, Object> map = new LinkedHashMap<>();
            	        Long insurenceGenId = entry.getKey();
            	        System.out.println(insurenceGenId);

            	        UserSurveyorBasicDetails userSurveyorBasicDetails =
            	                userSurveyorBasicDetailsRepo.findByInsuranceClaimId(insurenceGenId);

            	        // Group level fields
            	        map.put("insurenceGenId", insurenceGenId);

            	        // Vehicle details
            	        VehicleDetails vehicleDetails = vehicleDetailsRepo.findByInsurenceGenId(insurenceGenId);
            	        map.put("vehicleDetails", vehicleDetails);

            	        // Damage details
            	        List<DamageDetails> damageDetailsList = damageDetailsRepo.findByInsurenceGenId(insurenceGenId);
            	        List<PartDescriptionDTO> partDescriptionList = new ArrayList<>();
            	        for (DamageDetails row : damageDetailsList) {
            	            partDescriptionList.add(new PartDescriptionDTO(row.getPartsName(), row.getDescription()));
            	        }
            	        map.put("DamageDetails", partDescriptionList);

            	        // Loss assessment (Final survey)
            	        List<Object[]> assemblyList = assemblyDetailsRepo.findByInsurenceGenId(insurenceGenId);
            	        List<AssemblyDTO> lossAssessmentList = new ArrayList<>();
            	        for (Object[] row : assemblyList) {
            	            lossAssessmentList.add(new AssemblyDTO(
            	                    (String) row[0],
            	                    (BigDecimal) row[1],
            	                    (BigDecimal) row[2]
            	            ));
            	        }
            	        map.put("lossAssessmentList", lossAssessmentList);

            	        // Photos (list of PhotoPendingAi)
            	        map.put("photos", entry.getValue());

            	        return map;
            	    })
            	    .toList();

     	    //  Change -- Aman -- End -04-09-25 

            response.setMessage("Data found");
            response.setData(result);
            response.setHttpStatus(HttpStatus.OK);

            log.info("Respond: Data retrieved successfully All Photo for Ai from PhotoPendingAi startDate: " + startDate + " endDate: " + endDate
            		+ methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while retrieving data All Photo for Ai from PhotoPendingAi", e.getMessage());
            response.setMessage("Error retrieving data");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
//  Change -- Aman -- End -01-09-25 
    
    
//  Change -- Aman -- Start -04-09-25     
    public class PartDescriptionDTO {
        private String partName;
        private String description;

        public PartDescriptionDTO(String partName, String description) {
            this.partName = partName;
            this.description = description;
        }
        
        public String getPartName() {
            return partName;
        }

        public String getDescription() {
            return description;
        }

    }
    
    public static class AssemblyDTO {
        private String assemblyName;
        private BigDecimal estimated;
        private BigDecimal assessed;

        public AssemblyDTO(String assemblyName, BigDecimal estimated, BigDecimal assessed) {
            this.assemblyName = assemblyName;
            this.estimated = estimated;
            this.assessed = assessed;
        }

        public String getAssemblyName() { return assemblyName; }
        public BigDecimal getEstimated() { return estimated; }
        public BigDecimal getAssessed() { return assessed; }
    }

//  Change -- Aman -- End -04-09-25 

	@Override
	public ResponseModel deleteAllPhotoAfterAiRecive(List<Long> insurenceGenIds) {
        String methodName = "deleteAllPhotoAfterAiRecive";
        ResponseModel response = new ResponseModel();

        try {  
        	
        	 log.info("Request : Deleting All Photo After Ai Recive for PhotoPendingAi insurenceGenIds: " + insurenceGenIds
             		+ methodName + " Class : " + this.getClass());
               
            // Deleting data from repository
        	 int deletedCount = photoPendingAiRepo.deleteByInsurenceGenIds(insurenceGenIds);

             response.setMessage("Deleted " + deletedCount + " records successfully");
             response.setHttpStatus(HttpStatus.OK);

            log.info("Respond : Data Deleted Successfully All Photo After Ai Recive for insurenceGenIds: " + insurenceGenIds
            		+ methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Deleting All Photo After Ai Recive for PhotoPendingAi insurenceGenIds: "+ insurenceGenIds
            		+ methodName + " Class : " + this.getClass());
            
            response.setMessage("Error deleting in data");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

	// change --parikshit -- start -15-09-2025
	
	@Override
	public ResponseModel checkDuplicateBasicDetailsByClaimNo(String claimNo) {
		String methodName = "checkDuplicateBasicDetailsByClaimNo";
        ResponseModel response = new ResponseModel();
       
        try {  
        	
        	 log.info("Request : find BasicDetails By ClaimNo : " + claimNo
             		+ methodName + " Class : " + this.getClass());
        	 claimNo=claimNo.trim(); 
        	 UserSurveyorBasicDetails userSurveyorBasicDetails= userSurveyorBasicDetailsRepo.findByClaimNo(claimNo);

        	 if(userSurveyorBasicDetails==null) {
        		 response.setMessage("No Data found by ClaimNo : "+claimNo);
                 response.setHttpStatus(HttpStatus.OK);

                log.info("Respond : No Duplicate Data found By ClaimNo " + claimNo
                		+ methodName + " Class : " + this.getClass());
        	 }else {
        		 response.setMessage("Claim Already Exist");
                 response.setHttpStatus(HttpStatus.CONFLICT);

                log.info("Respond : Duplicate BasicDetails found By ClaimNo " + claimNo
                		+ methodName + " Class : " + this.getClass());
        	 }
            

        } catch (Exception e) {
            log.error("An error occurred checking Duplicate BasicDetails By ClaimNo"+ claimNo
            		+ methodName + " Class : " + this.getClass());
            
            response.setMessage("Error deleting checking Duplicate BasicDetails");
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
	}
	// change --parikshit -- end -15-09-2025
	
//Change -- Aman -- Start
//  @Override
//  public ResponseModel linkGeneratedForSurvey(String companyRegGenId, Long insuranceClaimId) {
//      String methodName = "linkGeneratedForSurvey";
//      ResponseModel response = new ResponseModel();
//
//      try {
//          log.info("Saving data for link is generated for survey for companyRegGenId: " + companyRegGenId + " insuranceClaimId: " 
//                  + insuranceClaimId + " Method Name" + methodName + " Class : " + this.getClass());
//
//          UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.findByInsuranceClaimIdAndCompanyGenId(insuranceClaimId,companyRegGenId);
//          if (userSurveyorBasicDetails != null) {
//              
//              userSurveyorBasicDetails.setLinkGenerated(true);
//              userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));
//              
//              userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
//      
//              response.setHttpStatus(HttpStatus.OK);
//              response.setMessage("Data updated Successfully");
//              
//              log.info(
//                      "Respond : data updated successfully for link is generated for survey for companyRegGenId: "
//                              +  companyRegGenId + " insuranceClaimId: " + insuranceClaimId + "  Method Name" + methodName
//                              + " Class : " + this.getClass());
//          }
//      }catch (Exception e) {
//          log.error("An error occurred while Saving data for link is generated for survey for companyRegGenId: "
//                  + e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
//      }
//      return response;
//  }
    //Change -- Aman -- End

    

}