package com.insuretech.survey.serviceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.AiComponentDetails;
import com.insuretech.survey.entity.AiDamages;
import com.insuretech.survey.entity.AiImages;
import com.insuretech.survey.entity.ImageAnnotation;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.model.AIComponentDetailsModel;
import com.insuretech.survey.model.ComponentDetailsModel;
import com.insuretech.survey.model.ImageAnnotationModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.AiDataService;

@Service
public class AiDataServiceImpl  extends AbstractMasterRepository implements AiDataService {
	
	Logger log = LoggerFactory.getLogger(AiDataServiceImpl.class);

	
	@Override
    public ResponseModel saveAnnotation(List<ImageAnnotationModel> imageAnnotationModel) {
        String methodName = "saveAnnotation";
        ResponseModel responseModel = new ResponseModel();
        try {
            log.info("Request : Saving annotation data, Method Name: " + methodName + ", Class: " + this.getClass());
            
            for(ImageAnnotationModel imageAnnotation: imageAnnotationModel) {
            	
            	log.info("Saving status true to aiPhotoMark column in UserSurveyorBasicDetails by insureceGneId: " + imageAnnotation.getInsurenceGenId() + " for ImageAnnotation UUID: " + imageAnnotation.getUuid());
                
            	UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo.findByInsuranceClaimId(Long.parseLong(imageAnnotation.getInsurenceGenId()));
                		
            	if(userSurveyorBasicDetails != null) {
            	
            	
                ImageAnnotation annotation = imageAnnotationRepository.findByInsuranceGenIdAndUuidAndBboxUuid(imageAnnotation.getInsurenceGenId(), imageAnnotation.getUuid(), imageAnnotation.getBboxUuid());
                
                // Check if ImageAnnotation already exists
                if(annotation != null) {
                    log.info("Bounding Box is alread save..  " + imageAnnotation.getInsurenceGenId() + " for ImageAnnotation UUID: " + imageAnnotation.getUuid()+ " and imageAnnotation BboxUuid: " + imageAnnotation.getBboxUuid());

                }else if(annotation == null) {
                    log.info("Adding all details to ImageAnnotation new work...  " + imageAnnotation.getInsurenceGenId() + " for ImageAnnotation UUID: " + imageAnnotation.getUuid() + " and imageAnnotation BboxUuid: " + imageAnnotation.getBboxUuid());

                	annotation = new ImageAnnotation();
                    annotation.setImage(imageAnnotation.getImage());
                    annotation.setClassId(imageAnnotation.getClassId());
                    annotation.setLabel(imageAnnotation.getLabel());
                    annotation.setInsuranceGenId(imageAnnotation.getInsurenceGenId());
                    annotation.setUuid(imageAnnotation.getUuid());
                    
                    annotation.setX(imageAnnotation.getBbox().get(0));
                    annotation.setY(imageAnnotation.getBbox().get(1));
                    annotation.setWidth(imageAnnotation.getBbox().get(2));
                    annotation.setHeight(imageAnnotation.getBbox().get(3));
                    annotation.setBboxUuid(imageAnnotation.getBboxUuid());
                    
                    annotation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    
                    annotation = imageAnnotationRepository.save(annotation);

                    log.info("Successfully saved new ImageAnnotation with UUID: " + annotation.getUuid() + " and auto-generated ID: " + annotation.getImageAnnotationId()+ " and imageAnnotation BboxUuid: " + imageAnnotation.getBboxUuid());
                }
            	}
    			userSurveyorBasicDetails.setAiPhotoMark(true);
    			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
            	
    			log.info("Respond: All data save successfully by insureceGneId: " + imageAnnotation.getInsurenceGenId() + " for ImageAnnotation UUID: " + imageAnnotation.getUuid());

    		}
            

            responseModel.setMessage("Data save successfully");
            responseModel.setHttpStatus(HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("An error occurred while saving annotation", e);
            throw e;
        }
        
        return responseModel;
    }
  
	@Override
	public ResponseModel getAnnotationByInsuranceGenId(String insuranceGenId) {
	    String methodName = "getAnnotationByInsuranceGenId";
	    ResponseModel responseModel = new ResponseModel();

	    try {
	        log.info("Request : Fetching annotation data for insuranceGenId: {}, Method: {}, Class: {}", 
	                 insuranceGenId, methodName, this.getClass());

	        List<ImageAnnotation> annotationsList = 
	                imageAnnotationRepository.findByInsuranceGenIdOrderByImageAnnotationIdAsc(insuranceGenId);

	        if (!annotationsList.isEmpty()) {
	            List<Map<String, Object>> transformedData = new ArrayList<>();

	            for (ImageAnnotation annotation : annotationsList) {
	                Map<String, Object> map = new HashMap<>();

	                List<Double> bbox = Arrays.asList(annotation.getX(), annotation.getY(),
	                                                  annotation.getWidth(), annotation.getHeight());

	                map.put("image", annotation.getImage());
	                map.put("classId", annotation.getClassId());
	                map.put("label", annotation.getLabel());
	                map.put("bbox", bbox);
                     	map.put("insurenceGenId", annotation.getInsuranceGenId());
	                map.put("uuid", annotation.getUuid());
	                map.put("bboxUuid", annotation.getBboxUuid());

	                transformedData.add(map);
	            }

	            // 🔹 remove duplicate maps if they exist
	            List<Map<String, Object>> uniqueData = transformedData.stream()
	                    .distinct()
	                    .toList();

	            responseModel.setData(uniqueData);
	            responseModel.setMessage("Data found successfully");
	            responseModel.setHttpStatus(HttpStatus.OK);

	            log.info("Successfully mapped {} annotations for insuranceGenId: {}", uniqueData.size(), insuranceGenId);
	        } else {
	            log.warn("No annotations found for insuranceGenId: {}", insuranceGenId);
	            responseModel.setMessage("No data found for insuranceGenId: " + insuranceGenId);
	            responseModel.setHttpStatus(HttpStatus.NOT_FOUND);
	        }

	        return responseModel;
	    } catch (Exception e) {
	        log.error("Error while fetching annotation for insuranceGenId: {}", insuranceGenId, e);
	        throw e;
	    }
	}

	@Override
	public ResponseModel deleteBoxByInsuranceGenIdAndBoxId(String insuranceGenId, String uuid, String boxId) {
	     String methodName = "deleteBoxByInsuranceGenIdAndBoxId";
        ResponseModel responseModel = new ResponseModel();
        
        try {
            log.info("Request : Deleting Box By InsuranceGenId And BoxId annotation data for insuranceGenId: " + insuranceGenId + " And boxId: " + boxId + " and uuid: " + uuid + " Method Name: " + methodName + ", Class: " + this.getClass());
           
            // Delete BoundingBox using JPA query
            int deletedRows = imageAnnotationRepository.deleteByInsuranceGenIdAndUuidAndBboxUuid(insuranceGenId, uuid, boxId);
            	if (deletedRows == 0) {
            		log.warn("No BoundingBox found with ID: " + boxId);
                	responseModel.setMessage("No BoundingBox found with ID");
            	} else {
                	responseModel.setMessage("Data Deleted successfully");
            	}
            	
                responseModel.setHttpStatus(HttpStatus.OK);
          
                log.info("Respond : Deleted Box By InsuranceGenId And BoxId annotation data for insuranceGenId: " + insuranceGenId + " And boxId: " + boxId + " and uuid: " + uuid + " Method Name: " + methodName + ", Class: " + this.getClass());

            return responseModel;
            
        }catch (Exception e) {
            log.error("An error occurred while Deleting Box By InsuranceGenI dAnd BoxId annotation data for insuranceGenId: " + insuranceGenId + " And boxId: " + boxId + " and uuid: " + uuid, e);
            throw e;
		}
        
	}
	
	@Override
	public ResponseModel saveComponentDetails(ComponentDetailsModel componentDetailsModel) {
		String methodName = "saveComponentDetails";
		ResponseModel response = new ResponseModel();

		try {
			log.info("Resuest : save component Details by insurenceGenId : " + componentDetailsModel.getInsurenceGenId()
					+ ", companyGenId : " + componentDetailsModel.getCompanyGenId() + "  Method Name" + methodName
					+ " Class : " + this.getClass());
			
			for(AIComponentDetailsModel request: componentDetailsModel.getComponentDetails()) {
				
				AiComponentDetails aiComponentDetails=new AiComponentDetails();
				aiComponentDetails.setInsurenceGenId(componentDetailsModel.getInsurenceGenId());
				aiComponentDetails.setCompanyGenId(componentDetailsModel.getCompanyGenId());
				aiComponentDetails.setReferenceNo(componentDetailsModel.getReferenceNo());
				aiComponentDetails.setVechileNo(componentDetailsModel.getVechileNo());
				
				aiComponentDetails.setName(request.getName());
				aiComponentDetails.setClassId(request.getClassId());
				if(request.getPrices()!=null) {
					aiComponentDetails.setLabour(request.getPrices().get(CommonConstants.PRICE_TYPE_LABOUR));
					aiComponentDetails.setPaint(request.getPrices().get(CommonConstants.PRICE_TYPE_PAINT));
					aiComponentDetails.setPrice(request.getPrices().get(CommonConstants.PRICE_TYPE_PRICE));
				}
				aiComponentDetails.setCreatedBy(componentDetailsModel.getCompanyGenId());
				
				AiComponentDetails savedAIComponentDetails=aiComponentDetailsRepo.save(aiComponentDetails);
				
				if(savedAIComponentDetails!=null) {
					
					log.info("Resuest :saved AIComponentDetails Details  successfully by insurenceGenId : " + componentDetailsModel.getInsurenceGenId()
					+ ", companyGenId : " + componentDetailsModel.getCompanyGenId() + "  Method Name" + methodName
					+ " Class : " + this.getClass());
					
					if(request.getDamages()!=null) {
						for(String damage :  request.getDamages()) {
							
							AiDamages aiDamages=new AiDamages();
							aiDamages.setAiComponentGenId(savedAIComponentDetails.getAiComponentGenId());
							aiDamages.setDamages(damage);
							aiDamages.setCreatedBy(componentDetailsModel.getCompanyGenId());
							
							aiDamagesRepo.save(aiDamages);
						}
					}
					
					if(request.getImages()!=null) {
						for(String imagesUUid :  request.getImages()) {

							AiImages aiImages=new AiImages();
							aiImages.setAiComponentGenId(savedAIComponentDetails.getAiComponentGenId());
							aiImages.setImagesUuid(imagesUUid);
							aiImages.setCreatedBy(componentDetailsModel.getCompanyGenId());
							
							aiImagesRepo.save(aiImages);
						}
					}
				} 
				
				log.info("Response :saved ComponentDetails Details  successfully by insurenceGenId : " + componentDetailsModel.getInsurenceGenId()
				+ ", companyGenId : " + componentDetailsModel.getCompanyGenId() + "  Method Name" + methodName
				+ " Class : " + this.getClass());
			
				response.setMessage("Data saved successfully");
				response.setHttpStatus(HttpStatus.OK);
			}
			

		} catch (Exception e) {
			log.error(
					"Response : An error occurred while Saving component Details by insurenceGenId :  "
							+ componentDetailsModel.getInsurenceGenId() + ", companyGenId : "
							+ componentDetailsModel.getCompanyGenId() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
		
			response.setMessage("error : "+e.getLocalizedMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;
	}

	@Override
	public ResponseModel getComponentDetails(Long insuranceGenId) {
		String methodName = "getComponentDetails";
		ResponseModel response = new ResponseModel();

		try {
			log.info("Resuest : fetch component Details by insurenceGenId : " + insuranceGenId
					 + "  Method Name" + methodName
					+ " Class : " + this.getClass());
			     
			     List<Object[]> request = aiComponentDetailsRepo.findByInsurenceGenId(insuranceGenId);
			     ComponentDetailsModel componentDetailsModel=new ComponentDetailsModel();
			     List<AIComponentDetailsModel> components = new ArrayList<>();
			     
			     if(request!=null && !request.isEmpty()) {
			    	 for(Object[] row : request) {
			    		 componentDetailsModel.setInsurenceGenId((Long)row[1]);
			    		 componentDetailsModel.setCompanyGenId((String) row[2]);
				    	 componentDetailsModel.setReferenceNo((String) row[3]);
				    	 componentDetailsModel.setVechileNo((String) row[4]);
				    	
				    	 AIComponentDetailsModel comp = new AIComponentDetailsModel();
				    	 comp.setAiComponentGenId((Long) row[0]);
				         comp.setName((String) row[5]);
				         comp.setClassId((Integer) row[6]); 
                         
				         if(row[7] !=null || row[8] !=null || row[9]!=null) {
				        	 Map<String, BigDecimal> priceMap = new HashMap<>();
					         priceMap.put(CommonConstants.PRICE_TYPE_LABOUR, (BigDecimal) row[7]);
					         priceMap.put(CommonConstants.PRICE_TYPE_PAINT, (BigDecimal) row[8]);
					         priceMap.put(CommonConstants.PRICE_TYPE_PRICE, (BigDecimal) row[9]);
					         comp.setPrices(priceMap);
				         }
				         

				         comp.setDamages(Arrays.asList((String[]) row[10]));
				         comp.setImages(Arrays.asList((String[]) row[11]));

				         components.add(comp);
			    	 }
			    	 
			    	 componentDetailsModel.setComponentDetails(components);
			    	 
			    	 log.info("Response : ComponentDetails Details fetched successfully by insurenceGenId : " + insuranceGenId
							 + "  Method Name" + methodName
							+ " Class : " + this.getClass());
						
			    	        response.setData(componentDetailsModel);
							response.setMessage("Data fetched successfully");
							response.setHttpStatus(HttpStatus.OK);
			    	 
			     }else {
			    	 
			    	 log.info("Response : No ComponentDetails Details found by insurenceGenId : " + insuranceGenId
							 + "  Method Name" + methodName
							+ " Class : " + this.getClass());
						
							response.setMessage("No Data Found");
							response.setHttpStatus(HttpStatus.NO_CONTENT);
			     }
			

		} catch (Exception e) {
			log.error(
					"Response : An error occurred while fetching component Details by insurenceGenId :  "
							+ insuranceGenId  + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
		
			response.setMessage("error : "+e.getLocalizedMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return response;
	}

}