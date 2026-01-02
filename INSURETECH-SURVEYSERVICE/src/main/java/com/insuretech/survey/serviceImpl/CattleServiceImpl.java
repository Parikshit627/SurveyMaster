package com.insuretech.survey.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.CattleComments;
import com.insuretech.survey.entity.CattleIntimationDetails;
import com.insuretech.survey.entity.ReferenceNoCounter;
import com.insuretech.survey.entity.Status;
import com.insuretech.survey.model.CattleCommentsModel;
import com.insuretech.survey.model.CattleDashboardModel;
import com.insuretech.survey.model.CattleIntimationDetailsModel;
import com.insuretech.survey.model.MetaDataModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.CattelService;

import jakarta.transaction.Transactional;

@Service
public class CattleServiceImpl extends AbstractMasterRepository implements CattelService {

	Logger log = LoggerFactory.getLogger(CattleServiceImpl.class);

	@Override
	@Transactional(rollbackOn = Exception.class)
	public ResponseModel saveCattelIntimationDetails(CattleIntimationDetailsModel cattelIntimationDetailsModel) {
		String methodName = "saveCattelIntimationDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Saving cattel intimation details by  CreatedBy: "
					+ cattelIntimationDetailsModel.getMetaData().getCreatedBy() + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			CattleIntimationDetails cattleIntimationDetails = null;

			if (cattelIntimationDetailsModel.getMetaData().getCattleIntimationGenId() != null
					&& cattelIntimationDetailsModel.getMetaData().getCattleIntimationGenId() != 0L) {

				cattleIntimationDetails = cattleIntimationDetailsRepo
						.findById(cattelIntimationDetailsModel.getMetaData().getCattleIntimationGenId()).orElse(null);
				if (cattleIntimationDetails != null) {
					cattleIntimationDetails.setUpdatedBy(cattelIntimationDetailsModel.getMetaData().getUpdatedBy());
				} else {
					log.info("Response : No Data Found in db : cattel intimation details by  cattelIntimationGenId: "
							+ cattelIntimationDetailsModel.getMetaData().getCattleIntimationGenId() + "  Method Name"
							+ methodName + " Class : " + this.getClass());

					response.setHttpStatus(HttpStatus.NO_CONTENT);
					response.setMessage(" No Data Found in db : cattel intimation details by  cattelIntimationGenId:"
							+ cattelIntimationDetailsModel.getMetaData().getCattleIntimationGenId());
					return response;

				}

			} else {
				cattleIntimationDetails = new CattleIntimationDetails();
				cattleIntimationDetails.setReferenceNo(generateRefrenceNo(cattelIntimationDetailsModel));
				cattleIntimationDetails.setCreatedBy(cattelIntimationDetailsModel.getMetaData().getCreatedBy());
			}

			BeanUtils.copyProperties(cattelIntimationDetailsModel, cattleIntimationDetails, "cattleIntimationGenId");
			cattleIntimationDetails.setCompanyGenId(cattelIntimationDetailsModel.getMetaData().getCompanyGenId());
			cattleIntimationDetails.setSource(cattelIntimationDetailsModel.getMetaData().getSource());
			cattleIntimationDetails.setFormVersion(cattelIntimationDetailsModel.getMetaData().getFormVersion());
			cattleIntimationDetails.setCurrentStatus(CommonConstants.BASIC_INITIAL_DETAILS);
			cattleIntimationDetails.setAssignTo(cattelIntimationDetailsModel.getBackOfficerEmail());

			CattleIntimationDetails savedCattleIntimationDetails = cattleIntimationDetailsRepo
					.save(cattleIntimationDetails);

			cattelIntimationDetailsModel.getMetaData().setReferenceNo(savedCattleIntimationDetails.getReferenceNo());
			cattelIntimationDetailsModel.getMetaData()
					.setCattleIntimationGenId(savedCattleIntimationDetails.getCattleIntimationGenId());
			cattelIntimationDetailsModel.setAssignTo(savedCattleIntimationDetails.getAssignTo());
			cattelIntimationDetailsModel.setCurrentStatus(savedCattleIntimationDetails.getCurrentStatus());

			// CattleComments
			if (cattelIntimationDetailsModel.getCattleCommentsModel() != null
					&& !cattelIntimationDetailsModel.getCattleCommentsModel().isEmpty()) {

				List<CattleCommentsModel> cattleCommentsModelList = new ArrayList<>();
				;

				for (CattleCommentsModel cattleCommentsModel : cattelIntimationDetailsModel.getCattleCommentsModel()) {

					CattleComments cattleComments = null;
					if (cattleCommentsModel.getCattleCommentsGenId() != null
							&& cattleCommentsModel.getCattleCommentsGenId() != 0L) {

						cattleComments = cattleCommentsRepo.findById(cattleCommentsModel.getCattleCommentsGenId())
								.orElse(null);

						if (cattleComments != null) {

							cattleComments.setComments(cattleCommentsModel.getComments());
							cattleComments
									.setCattleIntimationGenId(savedCattleIntimationDetails.getCattleIntimationGenId());
							cattleComments.setUpdatedBy(savedCattleIntimationDetails.getUpdatedBy());
							cattleComments.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));

							CattleComments savedCattleComments = cattleCommentsRepo.save(cattleComments);

							cattleCommentsModel.setCattleCommentsGenId(savedCattleComments.getCattleCommentsGenId());
							cattleCommentsModelList.add(cattleCommentsModel);

						} else {
							log.info("Response : No Data Found in db : cattle Comments by cattleCommentsGenId: "
									+ cattleCommentsModel.getCattleCommentsGenId() + "  Method Name" + methodName
									+ " Class : " + this.getClass());
						}
					} else {

						cattleComments = new CattleComments();
						cattleComments.setComments(cattleCommentsModel.getComments());
						cattleComments
								.setCattleIntimationGenId(savedCattleIntimationDetails.getCattleIntimationGenId());
						cattleComments.setCreatedBy(savedCattleIntimationDetails.getCreatedBy());

						CattleComments savedCattleComments = cattleCommentsRepo.save(cattleComments);

						cattleCommentsModel.setCattleCommentsGenId(savedCattleComments.getCattleCommentsGenId());
						cattleCommentsModelList.add(cattleCommentsModel);

					}

				}

				cattelIntimationDetailsModel.setCattleCommentsModel(cattleCommentsModelList);
				log.info("Response : Data save Successfully : cattle Comments by CattleIntimationGenId: "
						+ savedCattleIntimationDetails.getCattleIntimationGenId() + "total comments : "
						+ cattelIntimationDetailsModel.getCattleCommentsModel().size() + "  Method Name" + methodName
						+ " Class : " + this.getClass());
			}

			response.setData(cattelIntimationDetailsModel);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data save Successfully");

			log.info("Respond : data save successfully for cattel intimation details by CreatedBy: "
					+ cattelIntimationDetailsModel.getMetaData().getCreatedBy() + "  Method Name" + methodName
					+ " Class : " + this.getClass());

		} catch (Exception e) {
			log.error("Respond : An error occurred while Saving cattel intimation details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
			response.setMessage("Error : " + e.getLocalizedMessage());
		}
		return response;
	}

	private String generateRefrenceNo(CattleIntimationDetailsModel cattelIntimationDetailsModel) {
		String methodName = "generateRefrenceNo";
		String refrenceNo = "";
		try {
			log.info("Generateting RefrenceNo by userSurveyorLoginId: "
					+ cattelIntimationDetailsModel.getMetaData().getCreatedBy() + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			// financial year
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			int nextYearShort = (currentYear + 1) % 100;
			String financialYear = currentYear + "-" + String.format("%02d", nextYearShort);

			// code,abbrevation,counter
//          ReferenceNoCounter referenceNoCounter = referenceNoCounterRepo
//                  .findByRegCode(saveUserSurveyorBasicDetailsModel.getRegCode());

			// Change -- Aman -- Start
			ReferenceNoCounter referenceNoCounter = referenceNoCounterRepo
					.findByCreatedBy(cattelIntimationDetailsModel.getMetaData().getCompanyGenId());
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
				referenceNoCounter.setUpdatedBy(cattelIntimationDetailsModel.getMetaData().getCompanyGenId());
				referenceNoCounterRepo.save(referenceNoCounter);

				log.info("ReferenceNoCounter updated successfully " + "  Method Name" + methodName + " Class : "
						+ this.getClass());

			} else {
				log.error("No data found in db by  RegCode : " + cattelIntimationDetailsModel.getMetaData().getRegCode()
						+ "  Method Name" + methodName + " Class : " + this.getClass());
			}

		} catch (Exception e) {
			log.error("An error occurred while generate RefrenceNo by userSurveyorLoginId: " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
		}
		return refrenceNo;
	}

	@Override
	public ResponseModel getCattelIntimationDetails(Long cattleIntimationGenId, String referenceNo) {
		String methodName = "saveCattelIntimationDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Finding cattel intimation details by  cattleIntimationGenId : " + cattleIntimationGenId
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			CattleIntimationDetailsModel cattelIntimationDetailsModel = new CattleIntimationDetailsModel();
			MetaDataModel metaDataModel = new MetaDataModel();
			CattleIntimationDetails cattleIntimationDetails = null;

			if (cattleIntimationGenId != null && cattleIntimationGenId != 0L) {

				cattleIntimationDetails = cattleIntimationDetailsRepo.findById(cattleIntimationGenId).orElse(null);
				if (cattleIntimationDetails != null) {
					log.info(
							"Respond : data fetched successfully for cattel intimation details by cattleIntimationGenId: "
									+ cattleIntimationGenId + "  Method Name" + methodName + " Class : "
									+ this.getClass());

					BeanUtils.copyProperties(cattleIntimationDetails, cattelIntimationDetailsModel);
					metaDataModel.setCattleIntimationGenId(cattleIntimationDetails.getCattleIntimationGenId());
					metaDataModel.setCompanyGenId(cattleIntimationDetails.getCompanyGenId());
					metaDataModel.setCreatedBy(cattleIntimationDetails.getCreatedBy());
					metaDataModel.setReferenceNo(cattleIntimationDetails.getReferenceNo());
					metaDataModel.setSource(cattleIntimationDetails.getSource());
					metaDataModel.setFormVersion(cattleIntimationDetails.getFormVersion());
					metaDataModel.setUpdatedBy(cattleIntimationDetails.getUpdatedBy());

					Status status = statusRepo.findByStatusId(cattleIntimationDetails.getCurrentStatus());
					if (status != null) {
						cattelIntimationDetailsModel.setCurrentStatusDes(status.getDescription());
					}

					cattelIntimationDetailsModel.setMetaData(metaDataModel);

				} else {
					log.info("Response : No Data Found in db : cattel intimation details by  cattleIntimationGenId: "
							+ cattleIntimationGenId + "  Method Name" + methodName + " Class : " + this.getClass());

					response.setHttpStatus(HttpStatus.NO_CONTENT);
					response.setMessage(" No Data Found in db : cattel intimation details by  cattelIntimationGenId:"
							+ cattleIntimationGenId);

				}

			} else {

				cattleIntimationDetails = cattleIntimationDetailsRepo.findByReferenceNo(referenceNo);
				if (cattleIntimationDetails != null) {
					log.info("Respond : data fetched successfully for cattel intimation details by referenceNo: "
							+ referenceNo + "  Method Name" + methodName + " Class : " + this.getClass());

					BeanUtils.copyProperties(cattleIntimationDetails, cattelIntimationDetailsModel);
					metaDataModel.setCattleIntimationGenId(cattleIntimationDetails.getCattleIntimationGenId());
					metaDataModel.setCompanyGenId(cattleIntimationDetails.getCompanyGenId());
					metaDataModel.setCreatedBy(cattleIntimationDetails.getCreatedBy());
					metaDataModel.setReferenceNo(cattleIntimationDetails.getReferenceNo());
					metaDataModel.setSource(cattleIntimationDetails.getSource());
					metaDataModel.setFormVersion(cattleIntimationDetails.getFormVersion());
					metaDataModel.setUpdatedBy(cattleIntimationDetails.getUpdatedBy());

					Status status = statusRepo.findByStatusId(cattleIntimationDetails.getCurrentStatus());
					if (status != null) {
						cattelIntimationDetailsModel.setCurrentStatusDes(status.getDescription());
					}

					cattelIntimationDetailsModel.setMetaData(metaDataModel);

				} else {
					log.info("Response : No Data Found in db : cattel intimation details by  referenceNo: "
							+ referenceNo + "  Method Name" + methodName + " Class : " + this.getClass());

					response.setHttpStatus(HttpStatus.NO_CONTENT);
					response.setMessage(
							" No Data Found in db : cattel intimation details by  referenceNo:" + referenceNo);

				}

			}

			// CattleComments
			List<CattleCommentsModel> CattleCommentsList = cattleCommentsRepo
					.findByCattleIntimationGenId(cattleIntimationGenId);

			cattelIntimationDetailsModel.setCattleCommentsModel(CattleCommentsList);

			response.setData(cattelIntimationDetailsModel);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data fetched Successfully");

		} catch (Exception e) {
			log.error("Respond : An error occurred while fetching cattel intimation details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
			response.setMessage("Error : " + e.getLocalizedMessage());
		}
		return response;
	}

	@Override
	public ResponseModel geCatteltDashboardData(String userSurveyorLoginId, String companyRegGenId, String roleId,
			int page, int size) {
		String methodName = "geCatteltDashboardData";
		ResponseModel response = new ResponseModel();

		try {
			log.info("Request : Finding cattel dashboard data by  userSurveyorLoginId : " + userSurveyorLoginId
					+ ", companyRegGenId : " + companyRegGenId + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			boolean isAdmin = roleId.contains(CommonConstants.ADMIN_ROLE);

			Page<CattleDashboardModel> result = null;
			Pageable pageable = PageRequest.of(page, size);

			if (isAdmin) {
				result = cattleIntimationDetailsRepo.findCattleDashboard(companyRegGenId, pageable);

			} else {
				result = cattleIntimationDetailsRepo.findCattleDashboardByAssignTo(userSurveyorLoginId, companyRegGenId,
						pageable);
			}

			if (result != null && !result.isEmpty()) {
				Map<String, Object> map = new HashMap<>();
				map.put("requests", result.getContent());
				map.put("totalRequests", result.getTotalElements());
				map.put("totalPages", result.getTotalPages());
				map.put("currentPage", result.getNumber());
				map.put("pageSize", result.getSize());
				
				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data fetched Successfully");
				
			} else {
				log.info("Respond : No Data found -- cattel dashboard data by  userSurveyorLoginId : " + userSurveyorLoginId
						+ ", companyRegGenId : " + companyRegGenId + "  Method Name" + methodName + " Class : "
						+ this.getClass());

				response.setHttpStatus(HttpStatus.NO_CONTENT);
				response.setMessage(" No Data Found in db ");
			}

		} catch (Exception e) {
			log.error("Respond : An error occurred while finding cattel dashboard data ,error : " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.EXPECTATION_FAILED);
			response.setMessage("Error : " + e.getLocalizedMessage());
		}
		return response;
	}

}
