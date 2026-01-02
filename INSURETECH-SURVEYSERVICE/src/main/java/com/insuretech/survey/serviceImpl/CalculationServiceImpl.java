package com.insuretech.survey.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.CalculationLabourAssessmentPro;
import com.insuretech.survey.entity.CalculationPartAssessmentPro;
import com.insuretech.survey.entity.LossDetails;
import com.insuretech.survey.entity.MetalCalculation;
import com.insuretech.survey.entity.MetalType;
import com.insuretech.survey.model.CalculationPartAssessmentProModel;
import com.insuretech.survey.model.DashboardStatusmodel;
import com.insuretech.survey.model.MetalCalculationModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.CalculationService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CalculationServiceImpl extends AbstractMasterRepository implements CalculationService {
    
    @Autowired
    private EntityManager entityManager;
    
    @Override
    public ResponseModel calculateMaterialType(
            String metalCode , BigDecimal  amount , 
            Integer  depreciationPercent, 
            Integer  gst,
            String companyGenId,
            Long insurenceGenId) {
        ResponseModel model =new ResponseModel();
        Map< String, Object> map =new HashMap<String, Object>();        
        try {
            if (amount != null  && depreciationPercent != null) {
//              BigDecimal amount = BigDecimal.valueOf(amount);
                BigDecimal bdGst = BigDecimal.valueOf(gst);
                BigDecimal bdDepreciation = BigDecimal.valueOf(depreciationPercent);

                BigDecimal sum = amount.multiply(bdGst)
                                         .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                BigDecimal totalAmount = amount.add(sum);

                BigDecimal depreciationAmount = totalAmount.multiply(bdDepreciation)
                                                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                
                
                BigDecimal assessedAmount = totalAmount.subtract(depreciationAmount);
                map.put("totalAmount", totalAmount);
                map.put("depreciationAmount", depreciationAmount);
                map.put("assessedAmount", assessedAmount);
                model.setData(map);
                model.setHttpStatus("OK");
                // Use these values or set them to response
                log.info("sum "+ sum );
                log.info("totalAmount "+ totalAmount );
                log.info("depreciationAmount "+ depreciationAmount );
            }
            else if (metalCode==null || metalCode.isEmpty() ||metalCode.isBlank()) {
                
                List<String> material= metalTypeRepo.findAllCode();
                model.setData(material);
                model.setHttpStatus("OK");
            }
            else if(metalCode!=null) {
                
                if (metalCode.equalsIgnoreCase("M")) {
                    String metalDep=calculateMetalDep(insurenceGenId,companyGenId);
                    log.info("metalDep : "+metalDep);
                    Map<String, String> map1 = new HashMap<>();
                        map1.put("materialType", metalDep)  ;   
                        model.setData(map1);
                    model.setHttpStatus("OK");
                }
                else {
//          else {
                MetalType type= metalTypeRepo.findByCode(metalCode);
                model.setData(type);
                model.setHttpStatus("OK");
                }
                
            }
        

        } catch (Exception e) {
            // TODO: handle exception
        }

        return model;
    }

    @Override
    public ResponseModel calculateLabourCalculation(
            Long paintEstmate, 
            Integer lessPaintPercent, 
            Long replaceAmount,
            Long repairAmount, 
            Integer labourPercent) {
        ResponseModel model = new ResponseModel();
        Map< String, Object> map =new HashMap<String, Object>();        
        try {
            BigDecimal lessPaintAmount = BigDecimal.ZERO;
            BigDecimal allowedPaintAmount = BigDecimal.ZERO;
            BigDecimal repairGst = BigDecimal.ZERO;
            BigDecimal replaceGst = BigDecimal.ZERO;

            if (paintEstmate != null && lessPaintPercent != null) {
                // Convert Long and Integer to BigDecimal
                BigDecimal bdPaintEstimate = BigDecimal.valueOf(paintEstmate);
                BigDecimal bdLessPaintPercent = BigDecimal.valueOf(lessPaintPercent);

                // Perform calculation
                lessPaintAmount = bdPaintEstimate.multiply(bdLessPaintPercent)
                                                 .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                allowedPaintAmount = bdPaintEstimate.subtract(lessPaintAmount);
                
                map.put("lessPaintAmount", lessPaintAmount);
                map.put("allowedPaintAmount", allowedPaintAmount);
                model.setHttpStatus("OK");

                model.setData(map);
            }
            else if ((replaceAmount!=null || repairAmount!=null )&&labourPercent!=null) {
                BigDecimal bdlabourPercent = BigDecimal.valueOf(labourPercent);
                if (replaceAmount!=null) {
                    
                     BigDecimal bdreplaceAmount = BigDecimal.valueOf(replaceAmount);
                    replaceGst=bdreplaceAmount.multiply(bdlabourPercent)
                                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    
                    map.put("replaceGst", replaceGst);
                    map.put("remarks", CommonConstants.SAVE_REMARK_REPLACE);
                    
                    model.setData(map);

                    model.setHttpStatus("OK");

                }
                else if (repairAmount!=null) {
                    BigDecimal bdrepairAmount = BigDecimal.valueOf(repairAmount);
                    repairGst=bdrepairAmount.multiply(bdlabourPercent)
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    map.put("repairGst", repairGst);
                    map.put("remarks", CommonConstants.SAVE_REMARK_REPAIR);
                    model.setHttpStatus("OK");
                    model.setData(map);



                }
            } 
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        return model;
    }

    @Override
    public ResponseModel getpannelcalculation(String companyGenId, Long insurenceGenId) {
        String methodName ="getpannelcalculation";
        ResponseModel model = new ResponseModel();
        try {
            
            
            LossDetails lossDetails = lossDetailsRepo.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
            try {
            MetalCalculation metal= metalCalculationRepository.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
            lossDetails.setTaxPaidToggle(metal.getTaxPaidToggle());
            lossDetails.setTotalEstimate(metal.getTotalestimate());
            lossDetails.setGstPortion(metal.getGstPortion());
            lossDetails.setSubtotal(metal.getSubtotal());
            } catch (Exception e) {
                // TODO: handle exception
            }
            model.setData(lossDetails);
            model.setHttpStatus("OK");  
            } catch (Exception e) {
                model.setHttpStatus("Error Occur while fetch the LossDetails... Method Name : "+methodName + "class :" + getClass());
        }

        return model;
    }

    @Override
    public ResponseModel getpannelcalculation(MetalCalculationModel metalModel) {
        ResponseModel response = new ResponseModel();

        try {
            MetalCalculation existingRecord =
                metalCalculationRepository.findByInsurenceGenIdAndCompanyGenId(
                    metalModel.getInsurenceGenId(),
                    metalModel.getCompanyGenId()
                );

            if (existingRecord != null) {
                // ✅ Update existing
                BeanUtils.copyProperties(existingRecord,metalModel);
                existingRecord.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
                MetalCalculation saved = metalCalculationRepository.save(existingRecord);

                response.setData(saved);
                response.setMessage("Updated successfully");
                response.setHttpStatus(HttpStatus.OK);
                response.setName("getpannelcalculation");
                
                log.info("Updated MetalCalculation for Company: " + metalModel.getCompanyGenId() + ", InsuranceId: " + metalModel.getInsurenceGenId());
            } else {
                // ✅ Create new
                MetalCalculation metalCalculation = new MetalCalculation();
                BeanUtils.copyProperties(metalCalculation,metalModel);
                metalCalculation.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
                MetalCalculation saved = metalCalculationRepository.save(metalCalculation);

                response.setData(saved);
                response.setMessage("Created successfully");
                response.setHttpStatus(201);
                response.setName("getpannelcalculation");
                
                log.info("Created new MetalCalculation for Company: " + metalModel.getCompanyGenId() + ", InsuranceId: " + metalModel.getInsurenceGenId());
            }
        } catch (Exception e) {
            log.error("Error in getpannelcalculation for Company: " + metalModel.getCompanyGenId() 
                        + ", InsuranceId: " + metalModel.getInsurenceGenId(), e);
            response.setData(null);
            response.setMessage("Error occurred while processing MetalCalculation");
            response.setHttpStatus(500);
            response.setName("getpannelcalculation");
        }

        return response;
    }

    @Override
    public DashboardStatusmodel dashboardRequestStatusEmail(String email) {
        DashboardStatusmodel model = new DashboardStatusmodel();
try {
    long totalCount = userSurveyorBasicDetailsRepo.count();
    long count = userSurveyorBasicDetailsRepo.countByCurrentStatusInAndSurveyorEmail(Arrays.asList(7,9),email);
    log.info("Total Request In DATABASE: "+totalCount + "     Toatal Approved Conclusion:  "+count);
    model.setTotalConclusion(count);
    model.setTotalRequest(totalCount);
    
} catch (Exception e) {
     

}
        
        return model;
    }


    
    
    
//  Change -- Aman -- Start
    @Override
    public ResponseModel dashboardRequestStatus(String userLoginId, String companyRegGenId, List<String> roleId,
            String branchId) {
//      DashboardStatusmodel model = new DashboardStatusmodel();
        String methodName = "dashboardRequestStatus";
        ResponseModel response = new ResponseModel();

        try {
            
            boolean isAdmin = roleId.contains(CommonConstants.ADMIN_ROLE);
            boolean isInitiator = roleId.contains(CommonConstants.INITIATOR_ROLE);
            
            Long branchIdLong = null;
            if (!branchId.equalsIgnoreCase("null")) {
                    branchIdLong = Long.parseLong(branchId);
            }
            
            LocalDate today = LocalDate.now();
            LocalDateTime startOfToday = today.atStartOfDay();
            LocalDateTime startOfTomorrow = today.plusDays(1).atStartOfDay();

         // 1. Initialize with defaults
            Map<String, Long> userSurveyorBasicDetailsList = new HashMap<>();
            userSurveyorBasicDetailsList.put("cancelled_count", 0L);
            userSurveyorBasicDetailsList.put("closed_count", 0L);
            userSurveyorBasicDetailsList.put("pending_count", 0L);
            userSurveyorBasicDetailsList.put("today_count", 0L);
            userSurveyorBasicDetailsList.put("all_count", 0L);

            // 2. Merge function that handles nulls
            BiConsumer<String, Long> safeMerge = (k, v) -> 
                userSurveyorBasicDetailsList.merge(k, (v == null ? 0L : v), Long::sum);

            // isInitiator
            if (isInitiator) {
                Map<String, Long> initiatorData = userSurveyorBasicDetailsRepo
                    .countAllPendingClosedCancelledByCreatedByAndCompanyGenIdAndBranchId(
                        userLoginId, companyRegGenId, branchIdLong, startOfToday, startOfTomorrow
                    );
                if (initiatorData != null) initiatorData.forEach(safeMerge);
            }

            // isAdmin
            if (isAdmin) {
                Map<String, Long> adminData = userSurveyorBasicDetailsRepo
                    .countAllPendingClosedCancelledByCompanyGenIdAndBranchId(
                        companyRegGenId, branchIdLong, startOfToday, startOfTomorrow
                    );
                if (adminData != null) adminData.forEach(safeMerge);
            }

            // Other than Initiator & Admin
            if (!isInitiator && !isAdmin) {
                Map<String, Long> assignData = userSurveyorBasicDetailsRepo
                    .countAllPendingClosedCancelledByAssignToAndCompanyGenIdAndBranchId(
                        userLoginId, companyRegGenId, branchIdLong, startOfToday, startOfTomorrow
                    );
                if (assignData != null) assignData.forEach(safeMerge);
            }

            // 3. (Optional) Recalculate total if you want it derived
            long allCount = userSurveyorBasicDetailsList.getOrDefault("cancelled_count", 0L)
                           + userSurveyorBasicDetailsList.getOrDefault("closed_count", 0L)
                           + userSurveyorBasicDetailsList.getOrDefault("pending_count", 0L);
            userSurveyorBasicDetailsList.put("all_count", allCount);

            
            response.setMessage("Data found");
            response.setData(userSurveyorBasicDetailsList);
            response.setHttpStatus(HttpStatus.OK);

            log.info("Respond : data founded successfully for by userLoginId: " + userLoginId
                    + " And companyRegGenId: " + companyRegGenId 
                    + " and role Id: " + roleId + " and branchId: " + branchId 
                    + "  Method Name" + methodName + " Class : " + this.getClass());

        } catch (Exception e) {
            log.error("An error occurred while Finding all request by userLoginId: " + e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
        }
        return response;
    }

//  long totalCount = userSurveyorBasicDetailsRepo.countByCreatedBy(createdBy);
//  long count = userSurveyorBasicDetailsRepo.countByCurrentStatusesAndCreatedBy(Arrays.asList(7,9),createdBy);
//    log.info("Total Request In DATABASE: "+totalCount + "     Toatal Approved Conclusion:  "+count);
//    model.setTotalConclusion(count);
//    model.setTotalRequest(totalCount);
//    
//} catch (Exception e) {
//   
//
//}
//      
//      return model;
//  }
//  Change -- Aman -- End
    

    public String calculateMetalDep(long insurenceGenId, String companyGenId) throws ParseException {
        String methodName = "calculateMetalDep"; 
        try {
            String sql = "select  v.dor,b.date_of_loss\r\n"
                    + "from insuredb.user_surveyor_basic_details b\r\n"
                    + "join  insuredb.vehicle_details v \r\n"
                    + "on b.insurance_claim_id =v.insurence_gen_id "
                    + "where v.insurence_gen_id='" + insurenceGenId
                    + "' and v.company_gen_id='" + companyGenId + "'";

            Query query = entityManager.createNativeQuery(sql);
            // Retrieve the result list
            List<Object[]> results = query.getResultList();
            if(results!=null && !results.isEmpty()) {
                Object[] row = results.get(0);

//              String dorStrng = (String) row[0];
//
//              String dateStr = (String) row[0];
//              Instant instant = Instant.parse(dateStr);
//              Timestamp dor = Timestamp.from(instant);
                String dateStr = String.valueOf(row[0]).trim();
                Timestamp dor;

                if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    // Date only
                    LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                    dor = Timestamp.valueOf(localDate.atStartOfDay());
                } else {
                    // ISO instant
                    Instant instant = Instant.parse(dateStr);
                    dor = Timestamp.from(instant);
                }
                
                Timestamp dol = new Timestamp(((Date) row[1]).getTime());

                if (dol == null || dor == null || dol.before(dor)) {
                    return "";
                }

                long diffInMillis = dol.getTime() - dor.getTime();
                long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                if (diffInDays <= 183) {
                    return CommonConstants.MAP_METAL_DEP.get("NOT_EXCEEDING_SIX_MONTHS");
                } else if (diffInDays > 183 && diffInDays <= 365) {
                    return CommonConstants.MAP_METAL_DEP.get("SIX_MONTHS_TO_ONE_YEAR");
                } else if (diffInDays > 365 && diffInDays <= 730) {
                    return CommonConstants.MAP_METAL_DEP.get("ONE_YEAR_TO_TWO_YEAR");
                } else if (diffInDays > 730 && diffInDays <= 1095) {
                    return CommonConstants.MAP_METAL_DEP.get("TWO_YEAR_TO_THREE_YEAR");
                } else if (diffInDays > 1095 && diffInDays <= 1460) {
                    return CommonConstants.MAP_METAL_DEP.get("THREE_YEAR_TO_FOUR_YEAR");
                } else if (diffInDays > 1460 && diffInDays <= 1825) {
                    return CommonConstants.MAP_METAL_DEP.get("FOUR_YEAR_TO_FIVE_YEAR");
                } else if (diffInDays > 1825 && diffInDays <= 3650) {
                    return CommonConstants.MAP_METAL_DEP.get("FIVE_YEAR_TO_TEN_YEAR");
                } else {
                    return CommonConstants.MAP_METAL_DEP.get("MORE_THEN_TEN_YEAR");
                }
            }
            
         }catch(Exception e) {
             log.error("An error occurred error :  "+ e.getMessage(),
                    "  Method Name" + methodName + " Class : " + this.getClass());
         }
        return "";
        
    }



  

    @Override
    public ResponseModel FindCalculationTotalAssessmentPro(String companyGenId, Long insurenceGenId) {
        ResponseModel response = new ResponseModel();

        try {
            log.info("Request received to fetch TotalAssessmentPro for companyGenId: {}, insurenceGenId: {}",
                    companyGenId, insurenceGenId);

            CalculationPartAssessmentPro entity =
                    calculationPartAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(
                            companyGenId,
                            insurenceGenId
                    );

            if (entity != null) {
                log.info("Record found with id: {}", entity.getId());

                response.setName("SUCCESS");
                response.setMessage("Record fetched successfully");
                response.setData(entity);
                response.setHttpStatus(200);
            } else {
                log.warn("No record found for companyGenId: {}, insurenceGenId: {}", companyGenId, insurenceGenId);

                response.setName("NOT_FOUND");
                response.setMessage("No record found for given identifiers");
                response.setData(null);
                response.setHttpStatus(404);
            }

        } catch (Exception e) {
            log.error("Error occurred while fetching TotalAssessmentPro: ", e);

            response.setName("ERROR");
            response.setMessage("Error occurred while fetching: " + e.getMessage());
            response.setHttpStatus(500);
            response.setData(null);
        }

        return response;
    }

	@Override
	public ResponseModel CalculationLabourTotalAssessmentPro(String companyGenId, Long insurenceGenId) {
	      ResponseModel response = new ResponseModel();

	        try {
	            log.info("Request received to fetch Labour TotalAssessmentPro for companyGenId: {}, insurenceGenId: {}",
	                    companyGenId, insurenceGenId);

	            CalculationLabourAssessmentPro entity =
	                    calculationLabourAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(
	                            companyGenId,
	                            insurenceGenId
	                    );

	            if (entity != null) {
	                log.info("Record found with id: {}", entity.getId());

	                response.setName("SUCCESS");
	                response.setMessage("Record fetched successfully");
	                response.setData(entity);
	                response.setHttpStatus(200);
	            } else {
	                log.warn("No record found for companyGenId: {}, insurenceGenId: {}", companyGenId, insurenceGenId);

	                response.setName("NOT_FOUND");
	                response.setMessage("No record found for given identifiers");
	                response.setData(null);
	                response.setHttpStatus(404);
	            }

	        } catch (Exception e) {
	            log.error("Error occurred while fetching TotalAssessmentPro: ", e);

	            response.setName("ERROR");
	            response.setMessage("Error occurred while fetching: " + e.getMessage());
	            response.setHttpStatus(500);
	            response.setData(null);
	        }

	        return response;
	}



}
