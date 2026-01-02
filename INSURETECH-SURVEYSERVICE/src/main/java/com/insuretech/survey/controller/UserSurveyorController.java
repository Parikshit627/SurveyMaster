package com.insuretech.survey.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.model.AssignApprovalModel;
import com.insuretech.survey.model.CommonMailModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveUserSurveyorBasicDetailsModel;
import com.insuretech.survey.service.UserSurveyorService;
import com.insuretech.survey.serviceImpl.EmailServiceImpl;

@RestController
@RequestMapping("/userSurveyor")
public class UserSurveyorController {

    Logger log = LoggerFactory.getLogger(UserSurveyorController.class);

    @Autowired
    UserSurveyorService userSurveyorService;
    
    @Autowired
    EmailServiceImpl emailServiceImpl;

    @PostMapping("/saveBasicDetails")
    public ResponseModel saveBasicDetails(
            @RequestBody SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel) {
        String methodName = "saveBasicDetails";
        ResponseModel response = new ResponseModel();
        SimpleDateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");
        try {
            log.info("Request : Saving user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            response = userSurveyorService.saveBasicDetails(saveUserSurveyorBasicDetailsModel);
            UserSurveyorBasicDetails userSurveyorBasicDetails = (UserSurveyorBasicDetails) response.getData();
            if (response.getHttpStatus() == HttpStatus.OK) {
                try {
                    
                    log.info("Request : Sending email for Intimation form by email id: "
                            + saveUserSurveyorBasicDetailsModel.getCompanyGenId() + "  Method Name" + methodName
                            + " Class : " + this.getClass());
                    CommonMailModel commonMailModel = new CommonMailModel();
                    Map<String, Object> param = new HashMap<>();
                    param.put("reference_no", userSurveyorBasicDetails.getReferenceNo());
                    param.put("insurer_name", userSurveyorBasicDetails.getInsurerName());
                    param.put("policy_no", userSurveyorBasicDetails.getPolicyNo());
                    param.put("claim_no", userSurveyorBasicDetails.getClaimNo());
                    String cleanAsset = userSurveyorBasicDetails.getAsset() != null
                            ? userSurveyorBasicDetails.getAsset().trim()
                            : null;
                    param.put("asset", cleanAsset);//                  param.put("date_of_loss", userSurveyorBasicDetails.getDateOfloss().toString());
                    param.put("date_of_loss", dateFormate.format(userSurveyorBasicDetails.getDateOfloss()));
                    param.put("survey_location_address", userSurveyorBasicDetails.getSurveyLocationAddress());
                    param.put("surveyor", userSurveyorBasicDetails.getSurveyor());
                    param.put("insured_name", userSurveyorBasicDetails.getInsuredName());

                    commonMailModel.setInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
                    commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
                    commonMailModel.setReferenceNo(userSurveyorBasicDetails.getReferenceNo());
                    
                    if(saveUserSurveyorBasicDetailsModel.getSurveyType().trim().equalsIgnoreCase("spot")) {
                        commonMailModel.setEmailList(List.of(saveUserSurveyorBasicDetailsModel.getInsuredEmail(),
                                                             saveUserSurveyorBasicDetailsModel.getCompanyGenId()));
                    }
                    else if(saveUserSurveyorBasicDetailsModel.getSurveyType().trim().equalsIgnoreCase("final")) {
                        commonMailModel.setEmailList(List.of(userSurveyorBasicDetails.getInsuredEmail(),
                                userSurveyorBasicDetails.getCompanyGenId()));
                    }
                    
                    commonMailModel.setTemplateId(CommonConstants.INTIMATION_EMAIL_TEMPLATE_ID);
                    commonMailModel.setParam(param);
                      
                    ResponseModel emailResponse=emailServiceImpl.sendEmail(commonMailModel);

                    if (emailResponse.getMessage().toString().equalsIgnoreCase("success")) {
                        log.info("Response : Email Send successfully for Intimation form by email id: "
                                + saveUserSurveyorBasicDetailsModel.getCompanyGenId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                    } else {
                        log.error("Response : Email Send unsuccessfull for Intimation form by email id: "
                                + saveUserSurveyorBasicDetailsModel.getCompanyGenId() + "  Method Name" + methodName
                                + " Class : " + this.getClass());
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                    log.error("Response :error --  Email Send unsuccessfull for Intimation form by email id: "
                            + saveUserSurveyorBasicDetailsModel.getCompanyGenId() + "error :"+e.getLocalizedMessage()+"  Method Name" + methodName
                            + " Class : " + this.getClass());
                }
                
            }
            return response;

        } catch (Exception e) {
            log.error("An error occurred while Saving user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy(), e);
            return null;
        }
    }

    @GetMapping("/getBasicDetails")
    public ResponseModel getBasicDetails(
            @RequestParam(required = false) Long insuranceClaimId, 
            @RequestParam(required = false) String referenceNumber, 
            @RequestParam(required = false) String subjectMatter, @RequestParam(required = false) String companyRegGenId) {
        
        String methodName = "getBasicDetails";
        try {
            log.info("Request: Finding user Surveyor basic (initial) details with insuranceClaimId: " + insuranceClaimId
                    + ", Reference Number: " + referenceNumber 
                    + ", Subject Matter: " + subjectMatter
                    + " And companyRegGenId: "+ companyRegGenId
                    + " Method Name: " + methodName 
                    + " Class: " + this.getClass());

            return userSurveyorService.getBasicDetails(insuranceClaimId, referenceNumber, subjectMatter,companyRegGenId);

        } catch (Exception e) {
            log.error("An error occurred while finding user Surveyor basic (initial) details with insuranceClaimId: "
                    + insuranceClaimId + ", Reference Number: " + referenceNumber 
                    + ", Subject Matter: " + subjectMatter+ " And companyRegGenId: "+ companyRegGenId
                    +  e);
            return null;
        }
    }


//  Change -- Aman -- Start
    @GetMapping("/getUserSurveyorDashboard")
    public ResponseModel getUserSurveyorDashboard(@RequestParam String userSurveyorLoginId, @RequestParam(required = false) String companyRegGenId, @RequestParam List<String> roleId, @RequestParam(required = false) List<String> dashboardRange, @RequestParam String dashboardType , @RequestParam String branchId) {
        String methodName = "getUserSurveyorDashboard";
        try {
            log.info("Request : Finding user Surveyor all request data by userSurveyorLoginId: " + userSurveyorLoginId
                    + " And companyRegGenId: "+ companyRegGenId
                    + " and role Id: " + roleId + " dashboardRange: " + dashboardRange + " dashboardType: " + dashboardType 
                    + " and branchId: " + branchId + " Method Name" + methodName + " Class : " + this.getClass());

            return userSurveyorService.getUserSurveyorDashboard(userSurveyorLoginId,companyRegGenId, roleId, dashboardRange, dashboardType, branchId);

        } catch (Exception e) {
            log.error("An error occurred while Finding user Surveyor all request data by userSurveyorLoginId: "
                    + userSurveyorLoginId+ " And companyRegGenId: "+ companyRegGenId
                    + e);
            return null;
        }
    }
//  Change -- Aman -- End
    
    @PostMapping("/updateBasicDetails")
    public ResponseModel updateBasicDetails(
            @RequestBody SaveUserSurveyorBasicDetailsModel saveUserSurveyorBasicDetailsModel) {
        String methodName = "saveBasicDetails";
        try {
            log.info("Request : Saving user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            return userSurveyorService.updateBasicDetails(saveUserSurveyorBasicDetailsModel);

        } catch (Exception e) {
            log.error("An error occurred while Saving user Surveyor basic (initial) details by userSurveyorLoginId: "
                    + saveUserSurveyorBasicDetailsModel.getCreatedBy(), e);
            return null;
        }
    }
    
    
//  Change -- Aman -- Start
    @PostMapping("/assignForApproval")
    public ResponseModel assignForApproval(
            @RequestBody AssignApprovalModel assignApprovalModel) {
        String methodName = "assignForApproval";
        try {
            log.info("Request : Saving user Surveyor details assign For Approval by loginId: "
                    + assignApprovalModel.getUserId() + "  Method Name" + methodName + " Class : "
                    + this.getClass());

            return userSurveyorService.assignForApproval(assignApprovalModel);

        } catch (Exception e) {
            log.error("An error occurred while Saving user Surveyor details assign For Approval by loginId: "
                    + assignApprovalModel.getUserId(), e);
            return null;
        }
    }
    
//  Change -- Aman -- Start
    @GetMapping("/getToadyDashboardData")
    public ResponseModel getToadyDashboardData(@RequestParam String userSurveyorLoginId, @RequestParam(required = false) String companyRegGenId, @RequestParam List<String> roleId, @RequestParam(required = false) List<String> dashboardRange, @RequestParam String branchId) {
        String methodName = "getToadyDashboardData";
        try {
            log.info("Request : Finding Toady Dashboard Data by userSurveyorLoginId: " + userSurveyorLoginId
                    + " And companyRegGenId: "+ companyRegGenId
                    + " and role Id: " + roleId + " dashboardRange: " + dashboardRange
                    + " and branchId: " + branchId + " Method Name" + methodName + " Class : " + this.getClass());

            return userSurveyorService.getToadyDashboardData(userSurveyorLoginId,companyRegGenId, roleId, dashboardRange, branchId);

        } catch (Exception e) {
            log.error("An error occurred while Finding Toady Dashboard Data by userSurveyorLoginId: "
                    + userSurveyorLoginId+ " And companyRegGenId: "+ companyRegGenId
                    + e);
            return null;
        }
    }
//  Change -- Aman -- End
    
    
//  Change -- Aman -- Start -28-08-25
    @GetMapping("/getAllDashboardDataBySearch")
    public ResponseModel getAllDashboardDataBySearch(@RequestParam String userSurveyorLoginId, @RequestParam(required = false) String companyRegGenId, @RequestParam List<String> roleId, @RequestParam String branchId, @RequestParam String searchText) {
        String methodName = "getAllDashboardDataBySearch";
        try {
            log.info("Request : Finding All Dashboard Data By Search by userSurveyorLoginId: " + userSurveyorLoginId
                    + " And companyRegGenId: "+ companyRegGenId
                    + " and role Id: " + roleId + " for text: " + searchText
                    + " and branchId: " + branchId + " Method Name" + methodName + " Class : " + this.getClass());

            return userSurveyorService.getAllDashboardDataBySearch(userSurveyorLoginId,companyRegGenId, roleId, branchId, searchText);

        } catch (Exception e) {
            log.error("An error occurred while Finding All Dashboard Data By Search by userSurveyorLoginId: "
                    + userSurveyorLoginId+ " And companyRegGenId: "+ companyRegGenId
                    + e);
            return null;
        }
    }
//  Change -- Aman -- End -28-08-25
    
    
//  Change -- Aman -- Start -01-09-25
    @GetMapping("/getAllPhotoForAiCaseWise")
    public ResponseModel getAllPhotoForAiCaseWise(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        String methodName = "getAllPhotoForAiCaseWise";
        try {
            log.info("Request : Finding All Photo For Ai CaseWise: Whose photo is uploaded from startDate: " + startDate + " endDate: " + endDate + " Method Name" 
            		+ methodName + " Class : " + this.getClass());

            return userSurveyorService.getAllPhotoForAiCaseWise(startDate, endDate);

        } catch (Exception e) {
            log.error("An error occurred while Finding All Photo For Ai CaseWise: Whose photo is uploaded.... " + startDate + " endDate: " + endDate
                    + e);
            return null;
        }
    }
    
    @GetMapping("/checkDuplicateBasicDetailsByClaimNo")
    public ResponseModel checkDuplicateBasicDetailsByClaimNo(@RequestParam String claimNo) {
        String methodName = "checkDuplicateBasicDetailsByClaimNo";
        try {
        	log.info("Request : find BasicDetails By ClaimNo : " + claimNo
             		+ methodName + " Class : " + this.getClass());

            return userSurveyorService.checkDuplicateBasicDetailsByClaimNo(claimNo);

        } catch (Exception e) {
        	 log.error("An error occurred checking Duplicate BasicDetails By ClaimNo"+ claimNo
             		+ methodName + " Class : " + this.getClass());
            return null;
        }
    }
//  Change -- Aman -- End -01-09-25
    
    
    @GetMapping("/deleteAllPhotoAfterAiRecive")
    public ResponseModel deleteAllPhotoAfterAiRecive(@RequestParam List<Long> insurenceGenIds) {
        String methodName = "deleteAllPhotoAfterAiRecive";
        try {
            log.info("Request : Deleting All Photo After Ai Recive for insurenceGenIds: " + insurenceGenIds
            		+ methodName + " Class : " + this.getClass());

            return userSurveyorService.deleteAllPhotoAfterAiRecive(insurenceGenIds);

        } catch (Exception e) {
            log.error("An error occurred while Deleting All Photo After Ai Recive for insurenceGenIds: " + insurenceGenIds + e);
            return null;
        }
    }
    
//  @GetMapping("/linkGeneratedForSurvey")
//  public ResponseModel linkGeneratedForSurvey(@RequestParam String companyRegGenId, @RequestParam Long insuranceClaimId) {
//      String methodName = "linkGeneratedForSurvey";
//      try {
//          log.info("Request : Saving data for link is generated for survey for companyRegGenId: " + companyRegGenId + " insuranceClaimId: " + insuranceClaimId
//                  +" Method Name" + methodName + " Class : " + this.getClass());
//
//          return userSurveyorService.linkGeneratedForSurvey(companyRegGenId,insuranceClaimId);
//
//      } catch (Exception e) {
//          log.error("An error occurred while Saving data for link is generated for survey for companyRegGenId: "
//                  + companyRegGenId + " insuranceClaimId: " + insuranceClaimId + " And companyRegGenId: "+ companyRegGenId
//                  + e);
//          return null;
//      }
//  }
//  Change -- Aman -- End
    
    

}


