package com.insuretech.survey.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.AssemblyDetails;
import com.insuretech.survey.entity.CalculationLabourAssessmentPro;
import com.insuretech.survey.entity.CalculationPartAssessmentPro;
import com.insuretech.survey.entity.CompanyRegistered;
import com.insuretech.survey.entity.Conclusion;
import com.insuretech.survey.entity.ConclusionAssessmentPro;
import com.insuretech.survey.entity.DamageDetails;
import com.insuretech.survey.entity.DriverParticularsDetails;
import com.insuretech.survey.entity.Enclosures;
import com.insuretech.survey.entity.FinalConclusion;
import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.LabourAssessmentPro;
import com.insuretech.survey.entity.LabourDetails;
import com.insuretech.survey.entity.LossDetails;
import com.insuretech.survey.entity.MetalCalculation;
import com.insuretech.survey.entity.Notes;
import com.insuretech.survey.entity.Observations;
import com.insuretech.survey.entity.PartAssessmentPro;
import com.insuretech.survey.entity.PhotoPendingAi;
import com.insuretech.survey.entity.PolicyDetails;
import com.insuretech.survey.entity.Registered;
import com.insuretech.survey.entity.ReportDocumentUpload;
import com.insuretech.survey.entity.Status;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.entity.VehicleDetails;
import com.insuretech.survey.model.AssemblyDetailsModel;
import com.insuretech.survey.model.CalculationLabourAssessmentProModel;
import com.insuretech.survey.model.CalculationPartAssessmentProModel;
import com.insuretech.survey.model.CommonMailModel;
import com.insuretech.survey.model.ConclusionAssessmentProModel;
import com.insuretech.survey.model.ConclusionModel;
import com.insuretech.survey.model.DamageDetailsModel;
import com.insuretech.survey.model.DocUUidModel;
import com.insuretech.survey.model.DocumentData;
import com.insuretech.survey.model.DriverParticularsDetailsModel;
import com.insuretech.survey.model.FinalConclusionModel;
import com.insuretech.survey.model.FinalSubmissionRequest;
import com.insuretech.survey.model.ImageData;
import com.insuretech.survey.model.LabourAssessmentProJson;
import com.insuretech.survey.model.LabourAssessmentProModel;
import com.insuretech.survey.model.LabourDetailsModel;
import com.insuretech.survey.model.LossDetailsModel;
import com.insuretech.survey.model.PartAssessmentProModel;
import com.insuretech.survey.model.PartsAssessmentProJson;
import com.insuretech.survey.model.PhotoWizardModel;
import com.insuretech.survey.model.PolicyDetailsModel;
import com.insuretech.survey.model.ReportDocumentUploadModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SurveyWizardModel;
import com.insuretech.survey.model.VehicleDetailsModel;
import com.insuretech.survey.model.VideoData;
import com.insuretech.survey.model.WorkflowModel;
import com.insuretech.survey.service.SaveSurveyWizadService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class SaveSurveyWizadServiceImp extends AbstractMasterRepository implements SaveSurveyWizadService {

	 @Value("${REMOVE_PHOTO_DAYS}")
	    private int removePhotoDays;
	 
	Logger log = LoggerFactory.getLogger(SaveSurveyWizadServiceImp.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	SimpleDateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public ResponseModel saveSurveyWizad(SurveyWizardModel surveyWizardModel) {
		String methodName = "saveSurveyWizad";
		ResponseModel response = new ResponseModel();
		Map<String, Object> map = new HashMap<>();
		String saveFor = "";
		try {
			String companyGenId = surveyWizardModel.getCompanyGenId();
			Long insurenceGenId = surveyWizardModel.getInsurenceGenId();
			String workedBySurveyName = surveyWizardModel.getWorkedBySurveyName();
			String workedByUserId = surveyWizardModel.getWorkedByUserId();

			if ((companyGenId == null || companyGenId.trim().equals(""))
					&& (insurenceGenId == null || insurenceGenId == 0)) {
				log.info("Respond : Invalid companyGenId or insurenceGenId, " + "companyGenId : " + companyGenId
						+ ", insurenceGenId : " + insurenceGenId + "Method Name" + methodName + " Class : "
						+ this.getClass());

				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid companyGenId or insurenceGenId");

				return response;
			}

			// Saving the status into main table for status
			log.info("Saving the status into UserSurveyorBasicDetails table for status: For insurenceGenId"
					+ insurenceGenId + " and companyGenId: " + companyGenId + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimIdAndCompanyGenId(insurenceGenId, companyGenId);
			userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));

			// Change -- Aman -- Start
			int currentStatus = userSurveyorBasicDetails.getCurrentStatus();
			// Change -- Aman -- End

			if (surveyWizardModel.getPolicyForm() != null && surveyWizardModel.getFlag() == 1) {
				log.info("Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_POLICY_DATA + "  Method Name"
						+ methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_POLICY_DATA;

				PolicyDetailsModel policyForm = surveyWizardModel.getPolicyForm();
				PolicyDetails policyDetails = policyDetailsRepo.findByInsurenceGenIdAndCompanyGenId(insurenceGenId,
						companyGenId);

				if (policyDetails == null) {
					policyDetails = new PolicyDetails();
					policyDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
				} else {
					policyDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
				}
				BeanUtils.copyProperties(policyForm, policyDetails, "sr");
				policyDetails.setInsurenceGenId(insurenceGenId);
				policyDetails.setCompanyGenId(companyGenId);
				policyDetails.setWorkedBySurveyName(workedBySurveyName);
				policyDetails.setWorkedByUserId(workedByUserId);
				PolicyDetails savePolicyDetails = policyDetailsRepo.saveAndFlush(policyDetails);
				map.put("policyForm", savePolicyDetails);
//				
//				// Change -- Aman -- Start
				if (currentStatus == CommonConstants.BASIC_INITIAL_DETAILS
						&& !userSurveyorBasicDetails.getDocSubmitted()) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.POLICY_DETAILS_FILLED);
				}
//				Change -- Aman -- End

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : " + CommonConstants.SAVE_FOR_POLICY_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());

			}
			if (surveyWizardModel.getVehicleForm() != null && surveyWizardModel.getFlag() == 2) {
				log.info("Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_VEHICLE_DATA + "  Method Name"
						+ methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_VEHICLE_DATA;

				VehicleDetailsModel vehicleForm = surveyWizardModel.getVehicleForm();
				VehicleDetails vehicleDetails = vehicleDetailsRepo.findByInsurenceGenIdAndCompanyGenId(insurenceGenId,
						companyGenId);

				if (vehicleDetails == null) {
					vehicleDetails = new VehicleDetails();
					vehicleDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
				} else {
					vehicleDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
				}
				BeanUtils.copyProperties(vehicleForm, vehicleDetails, "sr");
				vehicleDetails.setInsurenceGenId(insurenceGenId);
				vehicleDetails.setCompanyGenId(companyGenId);
				vehicleDetails.setWorkedBySurveyName(workedBySurveyName);
				vehicleDetails.setWorkedByUserId(workedByUserId);
				VehicleDetails savedVehicleDetails = vehicleDetailsRepo.saveAndFlush(vehicleDetails);

				map.put("vehicleForm", savedVehicleDetails);

				// Change -- Aman -- Start
				if (currentStatus == CommonConstants.POLICY_DETAILS_FILLED
						&& !userSurveyorBasicDetails.getDocSubmitted()) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.VEHICLE_DETAILS_FILLED);
				}
//				Change -- Aman -- End

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : " + CommonConstants.SAVE_FOR_VEHICLE_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());

			}
			if (surveyWizardModel.getDriverParticularsForm() != null && surveyWizardModel.getFlag() == 3) {
				log.info("Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_DRIVER_PARTICULARS_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_DRIVER_PARTICULARS_DATA;

				DriverParticularsDetailsModel driverParticularsForm = surveyWizardModel.getDriverParticularsForm();
				DriverParticularsDetails driverParticularsDetails = driverParticularsDetailsRepo
						.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);

				if (driverParticularsDetails == null) {
					driverParticularsDetails = new DriverParticularsDetails();
					driverParticularsDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
				} else {
					driverParticularsDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
				}
				BeanUtils.copyProperties(driverParticularsForm, driverParticularsDetails, "sr", "licenceType");
				driverParticularsDetails.setInsurenceGenId(insurenceGenId);
				driverParticularsDetails.setCompanyGenId(companyGenId);
				driverParticularsDetails.setWorkedBySurveyName(workedBySurveyName);
				driverParticularsDetails.setWorkedByUserId(workedByUserId);
				try {
					List<String> licenceTypeList = driverParticularsForm.getLicenceType();
					String licenceTypeStr = convertListToString(licenceTypeList);
					driverParticularsDetails.setLicenceType(licenceTypeStr);
				} catch (Exception e) {
					log.warn("error occur while save LicenceType MethodName :" + methodName);
				}
				DriverParticularsDetails savedDriverParticularsDetails = driverParticularsDetailsRepo
						.saveAndFlush(driverParticularsDetails);

				map.put("driverParticularsForm", savedDriverParticularsDetails);

				// Change -- Aman -- Start
				if (currentStatus == CommonConstants.VEHICLE_DETAILS_FILLED
						&& !userSurveyorBasicDetails.getDocSubmitted()) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.DRIVER_DETAILS_FILLED);
				}
//				Change -- Aman -- End

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : "
						+ CommonConstants.SAVE_FOR_DRIVER_PARTICULARS_DATA + "  Method Name" + methodName + " Class : "
						+ this.getClass());

			}
			if ((surveyWizardModel.getLossDetailsForm() != null || surveyWizardModel.getPannelCalculation() != null)
					&& (surveyWizardModel.getFlag() == 4 || surveyWizardModel.getFlag() == 5)) {
				log.info("Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_LOSS_DATA + "  Method Name"
						+ methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_LOSS_DATA;

				LossDetailsModel lossDetailsForm = surveyWizardModel.getLossDetailsForm();
				LossDetailsModel pannelDetails = surveyWizardModel.getPannelCalculation();
				LossDetails lossDetails = lossDetailsRepo.findByInsurenceGenIdAndCompanyGenId(insurenceGenId,
						companyGenId);

				if (lossDetails == null) {
					lossDetails = new LossDetails();
					lossDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
				} else {
					lossDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
				}

				if (surveyWizardModel.getLossDetailsForm() != null) {
//					BeanUtils.copyProperties(lossDetailsForm, lossDetails, "sr");

					lossDetails.setDateOfLoss(lossDetailsForm.getDateOfLoss());
					lossDetails.setTimeOfloss(lossDetailsForm.getTimeOfloss());
					lossDetails.setLocation(lossDetailsForm.getLocation());
					lossDetails.setSpotSurvey(lossDetailsForm.getSpotSurvey());
					lossDetails.setCauseOfLoss(lossDetailsForm.getCauseOfLoss());
					lossDetails.setReportedTo(lossDetailsForm.getReportedTo());
					lossDetails.setOccupancy(lossDetailsForm.getOccupancy());
					lossDetails.setInjury(lossDetailsForm.getInjury());
					lossDetails.setRemarksLoss(lossDetailsForm.getRemarksLoss());
					lossDetails.setWorkshop(lossDetailsForm.getWorkshop());
					lossDetails.setDated(lossDetailsForm.getDated());
					lossDetails.setEstimated(lossDetailsForm.getEstimated());
					lossDetails.setCashless(lossDetailsForm.getCashless());
					lossDetails.setAmountWords(lossDetailsForm.getAmountWords());
					lossDetails.setAddress(lossDetailsForm.getAddress());
					lossDetails.setRemark(lossDetailsForm.getRemark());
					lossDetails.setDealerType(lossDetailsForm.getDealerType());
					
					
					// Change -- Aman -- Start -- 12-11-2025
					lossDetails.setVehicleLoaded(lossDetailsForm.getVehicleLoaded());
					lossDetails.setLoadedWeight(lossDetailsForm.getLoadedWeight());
					lossDetails.setDestination(lossDetailsForm.getDestination());
					lossDetails.setOrigin(lossDetailsForm.getOrigin());
					// Change -- Aman -- End -- 12-11-2025
				}
				if (surveyWizardModel.getPannelCalculation() != null) {

					lossDetails.setAccidentalLabourTotal(formatToTwoDecimal(pannelDetails.getAccidentalLabourTotal()));
					lossDetails
							.setPaintingLabour75percent(formatToTwoDecimal(pannelDetails.getPaintingLabour75percent()));
					lossDetails
							.setTowingEstimateAnyToatl(formatToTwoDecimal(pannelDetails.getTowingEstimateAnyToatl()));
					lossDetails.setPaintEstimation25Percent(
							formatToTwoDecimal(pannelDetails.getPaintEstimation25Percent()));
					lossDetails.setTotalPaintAllowedCal(formatToTwoDecimal(pannelDetails.getTotalPaintAllowedCal()));
					lossDetails.setTotalPaintTaxCal(formatToTwoDecimal(pannelDetails.getTotalPaintTaxCal()));
					lossDetails.setTotalLabourAmount(formatToTwoDecimal(pannelDetails.getTotalLabourAmount()));
					lossDetails.setTotalLabourGST(formatToTwoDecimal(pannelDetails.getTotalLabourGST()));
					lossDetails.setTowingAmount(formatToTwoDecimal(pannelDetails.getTowingAmount()));
					lossDetails.setTowingGST(formatToTwoDecimal(pannelDetails.getTowingGST()));
					lossDetails.setGstSummaryAllowedPart(formatToTwoDecimal(pannelDetails.getGstSummaryAllowedPart()));
					lossDetails.setGstSummaryGstPart(formatToTwoDecimal(pannelDetails.getGstSummaryGstPart()));
					lossDetails.setGstSummaryDepPart(formatToTwoDecimal(pannelDetails.getGstSummaryDepPart()));
					lossDetails.setTowingEstimate(formatToTwoDecimal(pannelDetails.getTowingEstimate()));
					lossDetails.setDep50Percent(formatToTwoDecimal(pannelDetails.getDep50Percent()));
					lossDetails
							.setLabourPart25PercentTax(formatToTwoDecimal(pannelDetails.getLabourPart25PercentTax()));

					lossDetails.setTotalEstimated(formatToTwoDecimal(pannelDetails.getTotalEstimated()));
					lossDetails.setTotalReplace(formatToTwoDecimal(pannelDetails.getTotalReplace()));
					lossDetails.setTotalRepair(formatToTwoDecimal(pannelDetails.getTotalRepair()));
					lossDetails.setTotalPaintEstimate(formatToTwoDecimal(pannelDetails.getTotalPaintEstimate()));
					lossDetails.setTotalAllowed(formatToTwoDecimal(pannelDetails.getTotalAllowed()));
					lossDetails.setGstAllowed(formatToTwoDecimal(pannelDetails.getGstAllowed()));
					lossDetails.setGstEstimated(formatToTwoDecimal(pannelDetails.getGstEstimated()));
					lossDetails.setGstReplace(formatToTwoDecimal(pannelDetails.getGstReplace()));
					lossDetails.setGstRepair(formatToTwoDecimal(pannelDetails.getGstRepair()));
					lossDetails.setGstPaint(formatToTwoDecimal(pannelDetails.getGstPaint()));

				}

				lossDetails.setInsurenceGenId(insurenceGenId);
				lossDetails.setCompanyGenId(companyGenId);
				lossDetails.setWorkedBySurveyName(workedBySurveyName);
				lossDetails.setWorkedByUserId(workedByUserId);
				LossDetails savedLossDetails = lossDetailsRepo.saveAndFlush(lossDetails);

				map.put("lossDetailsForm", savedLossDetails);

				String metalDep = calculateMetalDep(insurenceGenId, companyGenId);

				// Change -- Aman -- Start
				if (currentStatus == CommonConstants.DOC_IMG_UPLOAD_APK) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.LOSS_DETAILS_FILLED);
				}
//				Change -- Aman -- End

				userSurveyorBasicDetails.setMetalDep(metalDep);

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : " + CommonConstants.SAVE_FOR_LOSS_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());
			}
			if (surveyWizardModel.getAssemblyForm() != null && !surveyWizardModel.getAssemblyForm().isEmpty()
					&& surveyWizardModel.getFlag() == 5) {

				log.info("Saving Survey Wizard for: " + CommonConstants.SAVE_FOR_ASSEMBLY_DATA + " Method: "
						+ methodName + " Class: " + this.getClass());

				saveFor = CommonConstants.SAVE_FOR_ASSEMBLY_DATA;

				List<AssemblyDetails> savedAssemblyDetailsList = new ArrayList<>();

				List<AssemblyDetailsModel> assemblyFormList = surveyWizardModel.getAssemblyForm();

				for (AssemblyDetailsModel assemblyForm : assemblyFormList) {

					// 1️⃣ Try to find existing record
					AssemblyDetails assemblyDetails = assemblyDetailsRepo.findBySrAndInsurenceGenIdAndCompanyGenId(
							assemblyForm.getSr(), insurenceGenId, companyGenId);

					if (assemblyDetails == null) {
						// 2️⃣ New record
						assemblyDetails = new AssemblyDetails();
						BeanUtils.copyProperties(assemblyForm, assemblyDetails);
						assemblyDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
					} else {
						// 3️⃣ Update existing record (excluding sr)
						BeanUtils.copyProperties(assemblyForm, assemblyDetails, "sr");
					}

					// 4️⃣ Common fields
					assemblyDetails.setInsurenceGenId(insurenceGenId);
					assemblyDetails.setCompanyGenId(companyGenId);
					assemblyDetails.setWorkedBySurveyName(workedBySurveyName);
					assemblyDetails.setWorkedByUserId(workedByUserId);
					assemblyDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));

					// 5️⃣ Save to DB
					AssemblyDetails savedAssemblyDetails = assemblyDetailsRepo.saveAndFlush(assemblyDetails);
					savedAssemblyDetailsList.add(savedAssemblyDetails);
				}

				map.put("assemblyForm", savedAssemblyDetailsList);

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data saved successfully");

				log.info("Responded: data saved successfully for Survey Wizard — "
						+ CommonConstants.SAVE_FOR_ASSEMBLY_DATA + " Method: " + methodName + " Class: "
						+ this.getClass());
			}

			if (surveyWizardModel.getLabourForm() != null && !surveyWizardModel.getLabourForm().isEmpty()
					&& (surveyWizardModel.getFlag() == 5)) {
				log.info("Request : Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_LABOUR_DATA + "  Method Name"
						+ methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_LABOUR_DATA;

				List<LabourDetailsModel> labourFormList = surveyWizardModel.getLabourForm();
				List<LabourDetails> labourDetailsList = labourDetailsRepo
						.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);

				LabourDetailsModel labourForm = null;
				LabourDetails labourDetails = null;

				List<LabourDetails> savedLabourDetailsList = new ArrayList<>();

				if (labourDetailsList != null && !labourDetailsList.isEmpty()) {
					int i;
					for (i = 0; i < labourDetailsList.size(); i++) {
						labourForm = labourFormList.get(i);
						labourDetails = labourDetailsList.get(i);
						BeanUtils.copyProperties(labourForm, labourDetails, "sr");
						labourDetails.setInsurenceGenId(insurenceGenId);
						labourDetails.setCompanyGenId(companyGenId);
						labourDetails.setWorkedBySurveyName(workedBySurveyName);
						labourDetails.setWorkedByUserId(workedByUserId);
						labourDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						LabourDetails savedLabourDetails = labourDetailsRepo.saveAndFlush(labourDetails);

						savedLabourDetailsList.add(savedLabourDetails);

					}

					for (; i < labourFormList.size(); i++) {
						labourForm = labourFormList.get(i);
						labourDetails = new LabourDetails();
						BeanUtils.copyProperties(labourForm, labourDetails, "sr");
						labourDetails.setInsurenceGenId(insurenceGenId);
						labourDetails.setCompanyGenId(companyGenId);
						labourDetails.setWorkedBySurveyName(workedBySurveyName);
						labourDetails.setWorkedByUserId(workedByUserId);
						labourDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
						LabourDetails savedLabourDetails = labourDetailsRepo.saveAndFlush(labourDetails);

						savedLabourDetailsList.add(savedLabourDetails);
					}

				} else {
					for (LabourDetailsModel labourForm1 : labourFormList) {

						labourDetails = new LabourDetails();
						BeanUtils.copyProperties(labourForm1, labourDetails, "sr");
						labourDetails.setInsurenceGenId(insurenceGenId);
						labourDetails.setCompanyGenId(companyGenId);
						labourDetails.setWorkedBySurveyName(workedBySurveyName);
						labourDetails.setWorkedByUserId(workedByUserId);
						labourDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
						LabourDetails savedLabourDetails = labourDetailsRepo.saveAndFlush(labourDetails);

						savedLabourDetailsList.add(savedLabourDetails);
					}
				}

				map.put("labourForm", savedLabourDetailsList);

				// email
//				try {
//					
//					sendAssessmentEmail(userSurveyorBasicDetails);
//				}catch(Exception e) {
//					log.error("An error occurred while sending email "+ e.getMessage(),
//							"  Method Name" + methodName + " Class : " + this.getClass());
//				}

//				userSurveyorBasicDetails.setCurrentStatus(CommonConstants.LOSS_ASS_DETAILS_FILLED);

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : " + CommonConstants.SAVE_FOR_LABOUR_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());
			}
			if ((surveyWizardModel.getDamageDetails() != null && !surveyWizardModel.getDamageDetails().isEmpty())
					&& surveyWizardModel.getFlag() == 5) {
				log.info("Request : Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_DAMAGE_DATA + "  Method Name"
						+ methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_DAMAGE_DATA;

				List<DamageDetailsModel> damageFormList = surveyWizardModel.getDamageDetails();
				List<DamageDetails> damageDetailsList = damageDetailsRepo
						.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);

				DamageDetailsModel damageForm = null;
				DamageDetails damageDetails = null;

				List<DamageDetails> savedDamageDetailsList = new ArrayList<>();

				if (damageDetailsList != null && !damageDetailsList.isEmpty()) {
					int i;
					for (i = 0; i < damageDetailsList.size(); i++) {
						damageForm = damageFormList.get(i);
						damageDetails = damageDetailsList.get(i);
						BeanUtils.copyProperties(damageForm, damageDetails, "sr");
						damageDetails.setInsurenceGenId(insurenceGenId);
						damageDetails.setCompanyGenId(companyGenId);
						damageDetails.setWorkedBySurveyName(workedBySurveyName);
						damageDetails.setWorkedByUserId(workedByUserId);
						damageDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						DamageDetails savedDamageDetails = damageDetailsRepo.saveAndFlush(damageDetails);

						savedDamageDetailsList.add(savedDamageDetails);

					}

					for (; i < damageFormList.size(); i++) {
						damageForm = damageFormList.get(i);
						damageDetails = new DamageDetails();
						BeanUtils.copyProperties(damageForm, damageDetails, "sr");
						damageDetails.setInsurenceGenId(insurenceGenId);
						damageDetails.setCompanyGenId(companyGenId);
						damageDetails.setWorkedBySurveyName(workedBySurveyName);
						damageDetails.setWorkedByUserId(workedByUserId);
						damageDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
						DamageDetails savedDamageDetails = damageDetailsRepo.saveAndFlush(damageDetails);
						savedDamageDetailsList.add(savedDamageDetails);
					}

				} else {
					for (DamageDetailsModel damageForm1 : damageFormList) {

						damageDetails = new DamageDetails();
						BeanUtils.copyProperties(damageForm1, damageDetails, "sr");
						damageDetails.setInsurenceGenId(insurenceGenId);
						damageDetails.setCompanyGenId(companyGenId);
						damageDetails.setWorkedBySurveyName(workedBySurveyName);
						damageDetails.setWorkedByUserId(workedByUserId);
						damageDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
						DamageDetails savedDamageDetails = damageDetailsRepo.saveAndFlush(damageDetails);

						savedDamageDetailsList.add(savedDamageDetails);
					}
				}

				map.put("damageDetails", savedDamageDetailsList);

				// Change -- Aman -- Start
				if (currentStatus == CommonConstants.LOSS_DETAILS_FILLED) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.DAMAGE_DETAILS_FILLED);
				}
//					Change -- Aman -- End

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : " + CommonConstants.SAVE_FOR_DAMAGE_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());
			}
			if (surveyWizardModel.getConclusionForm() != null) {
				log.info("Request : Saving Survey Wizad for : " + CommonConstants.SAVE_FOR_CONCLUSION_DATA
						+ "  Method Name" + methodName + " Class : " + this.getClass());
				saveFor = CommonConstants.SAVE_FOR_CONCLUSION_DATA;

				ConclusionModel conclusionModel = surveyWizardModel.getConclusionForm();
				Conclusion conclusionDetails = conclusionRepo.findByInsurenceGenIdAndCompanyGenId(insurenceGenId,
						companyGenId);

				if (conclusionDetails == null) {
					conclusionDetails = new Conclusion();
					conclusionDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
				} else {
					conclusionDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
				}
				conclusionDetails.setOfficerId(conclusionModel.getOfficerId());
				conclusionDetails.setOfficerName(conclusionModel.getOfficerName());
				conclusionDetails.setRemark(conclusionModel.getRemark());
				conclusionDetails.setInsurenceGenId(insurenceGenId);
				conclusionDetails.setCompanyGenId(companyGenId);
				conclusionDetails.setWorkedBySurveyName(workedBySurveyName);
				conclusionDetails.setWorkedByUserId(workedByUserId);
				Conclusion savedConclusion = conclusionRepo.save(conclusionDetails);

				List<String> enclosuresFormList = conclusionModel.getEnclosures();
				List<Enclosures> enclosuresDetailsList = enclosuresRepo.findByConclusionId(savedConclusion.getSr());

				if (enclosuresDetailsList != null && !enclosuresDetailsList.isEmpty()) {
					String enclosuresForm = null;
					Enclosures enclosuresDetails = null;
					int i;
					for (i = 0; i < enclosuresDetailsList.size(); i++) {
						enclosuresForm = enclosuresFormList.get(i);
						enclosuresDetails = enclosuresDetailsList.get(i);
						enclosuresDetails.setDescription(enclosuresForm);
						enclosuresDetails.setConclusionId(savedConclusion.getSr());
						enclosuresDetails.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						enclosuresRepo.saveAndFlush(enclosuresDetails);

					}

					for (; i < enclosuresFormList.size(); i++) {
						enclosuresForm = enclosuresFormList.get(i);
						enclosuresDetails = new Enclosures();
						enclosuresDetails.setDescription(enclosuresForm);
						enclosuresDetails.setConclusionId(savedConclusion.getSr());
						enclosuresDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
						enclosuresRepo.saveAndFlush(enclosuresDetails);
					}

				} else {
					for (String enclosuresForm1 : enclosuresFormList) {
						Enclosures enclosuresDetails = new Enclosures();
						enclosuresDetails.setDescription(enclosuresForm1);
						enclosuresDetails.setConclusionId(savedConclusion.getSr());
						enclosuresDetails.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
						enclosuresRepo.saveAndFlush(enclosuresDetails);
					}
				}

				// observattion
				List<String> observationsFormList = conclusionModel.getObservations();
				List<Observations> observationsDetailsList = observationsRepo
						.findByConclusionId(savedConclusion.getSr());

				if (observationsDetailsList != null && !observationsDetailsList.isEmpty()) {
					String observationsForm = null;
					Observations observationsDetail = null;
					int i;
					for (i = 0; i < observationsDetailsList.size(); i++) {
						observationsForm = observationsFormList.get(i);
						observationsDetail = observationsDetailsList.get(i);
						observationsDetail.setDescription(observationsForm);
						observationsDetail.setConclusionId(savedConclusion.getSr());
						observationsDetail.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						observationsRepo.saveAndFlush(observationsDetail);

					}

					for (; i < observationsFormList.size(); i++) {
						observationsForm = observationsFormList.get(i);
						observationsDetail = new Observations();
						observationsDetail.setDescription(observationsForm);
						observationsDetail.setConclusionId(savedConclusion.getSr());
						observationsDetail.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						observationsRepo.saveAndFlush(observationsDetail);
					}

				} else {
					for (String observationsForm1 : observationsFormList) {
						Observations observationsDetail = new Observations();
						observationsDetail.setDescription(observationsForm1);
						observationsDetail.setConclusionId(savedConclusion.getSr());
						observationsDetail.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						observationsRepo.saveAndFlush(observationsDetail);
					}
				}

				// notes
				List<String> notesFormList = conclusionModel.getNotes();
				List<Notes> notesDetailsList = notesRepo.findByConclusionId(savedConclusion.getSr());

				if (notesDetailsList != null && !notesDetailsList.isEmpty()) {
					String notesForm = null;
					Notes notesDetail = null;
					int i;
					for (i = 0; i < notesDetailsList.size(); i++) {
						notesForm = notesFormList.get(i);
						notesDetail = notesDetailsList.get(i);
						notesDetail.setDescription(notesForm);
						notesDetail.setConclusionId(savedConclusion.getSr());
						notesDetail.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						notesRepo.saveAndFlush(notesDetail);

					}

					for (; i < notesFormList.size(); i++) {
						notesForm = notesFormList.get(i);
						notesDetail = new Notes();
						notesDetail.setDescription(notesForm);
						notesDetail.setConclusionId(savedConclusion.getSr());
						notesDetail.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						notesRepo.saveAndFlush(notesDetail);
					}

				} else {
					for (String notesForm1 : notesFormList) {
						Notes notesDetail = new Notes();
						notesDetail.setDescription(notesForm1);
						notesDetail.setConclusionId(savedConclusion.getSr());
						notesDetail.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
						notesRepo.saveAndFlush(notesDetail);
					}
				}

				// send email
//				try {
//					 
//					sendFinalSubSurveyWizardEmail(userSurveyorBasicDetails);
//				}catch(Exception e) {
//					log.error("An error occurred while sending email "+ e.getMessage(),
//							"  Method Name" + methodName + " Class : " + this.getClass());
//				}

				// Change -- Aman -- Start
				if (currentStatus == CommonConstants.APPROVED_STATUS) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.CONCLUSION_FILLED);
				}
//				Change -- Aman -- End

				response.setData(map);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

				log.info("Respond : data save successfully Survey Wizad for  : "
						+ CommonConstants.SAVE_FOR_CONCLUSION_DATA + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			}

//			Change -- Aman -- Start
			if (surveyWizardModel.getFinalConclusionForm() != null && surveyWizardModel.getFlag() == 6) {
				try {
					FinalConclusionModel model = surveyWizardModel.getFinalConclusionForm();

					// Try to fetch existing FinalConclusion by insurenceGenId
					FinalConclusion finalConclusion = finalConclusionRepo.findByInsurenceGenId(insurenceGenId);

					boolean isNew = false;

					if (finalConclusion == null) {
						finalConclusion = new FinalConclusion();
						finalConclusion.setInsurenceGenId(insurenceGenId);
						finalConclusion.setCreatedDtm(new Timestamp(System.currentTimeMillis())); // set only when new
						isNew = true;
					}

					// Map fields from model to entity
					finalConclusion.setOfficerId(model.getOfficerId());
					finalConclusion.setOfficerName(model.getOfficerName());
					finalConclusion.setFinalRemark(model.getFinalRemark());
					finalConclusion.setLessMetalParts(model.getLessMetalParts());
					finalConclusion.setLessassessment(model.getLessassessment());
					finalConclusion.setSalvageCharges(model.getSalvageCharges());
					finalConclusion.setAverageClause(model.getAverageClause());
					finalConclusion.setCompulsoryClause(model.getCompulsoryClause());
					finalConclusion.setOtherDeductibles(model.getOtherDeductibles());
					finalConclusion.setCompanyGenId(companyGenId);
					finalConclusion.setCashless(model.getCashless());
					finalConclusion.setFinalNotes(model.getFinalNotes());
					finalConclusion.setFinalObservations(model.getFinalObservations());
					finalConclusion.setGrossLossDep(model.getGrossLossDep());
					finalConclusion.setGrossLossAmount(model.getGrossLossAmount());
					finalConclusion.setGrossLossGst(model.getGrossLossGst());
					finalConclusion.setLabourPaintTotal(model.getLabourPaintTotal());
					finalConclusion.setFinalTotalEstimate(model.getFinalTotalEstimate());
					finalConclusion.setNetLossValue(model.getNetLossValue());
					finalConclusion.setFinalObservationsandFinding(model.getFinalObservationsandFinding());
					// Handle enclosures
					try {
						List<String> finalEnclosuresList = model.getFinalenclosures();
						String finalEnclosuresString = String.join(", ", finalEnclosuresList);
						finalConclusion.setFinalenclosures(finalEnclosuresString);
					} catch (Exception e) {
						log.warn("Could not parse final enclosures list", e);
					}

					// Set updated timestamp always
					finalConclusion.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));

					// Save the entity (insert or update)
					FinalConclusion finalConcl = finalConclusionRepo.save(finalConclusion);

					// send email
					try {
						Timestamp created = finalConcl.getCreatedDtm();
						LocalDate createdDate = created.toLocalDateTime().toLocalDate();
						LocalDate today = LocalDate.now();

						if (createdDate.equals(today)) {
							sendFinalSubSurveyWizardEmail(userSurveyorBasicDetails);
						}

//						sendFinalSubSurveyWizardEmail(userSurveyorBasicDetails);

					} catch (Exception e) {
						log.error("An error occurred while sending email " + e.getMessage(),
								"  Method Name" + methodName + " Class : " + this.getClass());
					}

					// Change -- Aman -- Start -- 15-09-2025
					if (currentStatus == CommonConstants.APPROVED_STATUS) {
						userSurveyorBasicDetails.setCurrentStatus(CommonConstants.FINAL_CONCLUSION_FILLED);
					}
//					Change -- Aman -- End -- 15-09-2025

					log.info("Saved FinalConclusion [new=" + isNew + "] for insurenceGenId=" + insurenceGenId
							+ ", companyGenId=" + companyGenId + " in method: " + methodName + " | class: "
							+ this.getClass());

					response.setHttpStatus(HttpStatus.OK);

				} catch (Exception e) {
					log.error("Error while saving FinalConclusion for insurenceGenId=" + insurenceGenId
							+ ", companyGenId=" + companyGenId + " in method: " + methodName + " | class: "
							+ this.getClass(), e);
					response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
					response.setMessage("Unable to save FinalConclusion details.");
				}
			}

			if (surveyWizardModel.getSurveyType() != null && !surveyWizardModel.getSurveyType().trim().equals("")) {
				userSurveyorBasicDetails.setSurveyType(surveyWizardModel.getSurveyType());
			}

			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
			log.info("Status Upadated into UserSurveyorBasicDetails table: For insurenceGenId" + insurenceGenId
					+ " and companyGenId: " + companyGenId + "  Method Name" + methodName + " Class : "
					+ this.getClass());

		} catch (Exception e) {
			log.error("An error occurred while Saving Survey Wizad for : " + saveFor + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}

		return response;
	}

	@Override
	public ResponseModel getSurveyWizad(String companyGenId, Long insurenceGenId, String workedByUserId) {
		String methodName = "getSurveyWizad";
		ResponseModel response = new ResponseModel();
		Map<String, Object> map = new HashMap<>();

		try {

			if ((companyGenId == null || companyGenId.trim().equals(""))
					&& (insurenceGenId == null || insurenceGenId == 0)
					&& (workedByUserId == null || workedByUserId.trim().equals(""))) {
				log.info("Respond : Invalid companyGenId or insurenceGenId or workedByUserId, " + "companyGenId : "
						+ companyGenId + ", insurenceGenId : " + insurenceGenId + ", workedByUserId : " + workedByUserId
						+ "Method Name" + methodName + " Class : " + this.getClass());

				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid companyGenId or insurenceGenId or workedByUserId");

				return response;
			}

			log.info("Request : Finding all basic details by insurenceGenId: " + insurenceGenId + " And companyGenId: "
					+ companyGenId + "  Method Name" + methodName + " Class : " + this.getClass());

			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimIdAndCompanyGenId(insurenceGenId, companyGenId);

			if (userSurveyorBasicDetails == null) {
				log.info("Respond : No data Found for basic details by insurenceGenId: " + insurenceGenId
						+ " And companyGenId: " + companyGenId + "  Method Name" + methodName + " Class : "
						+ this.getClass());

				response.setMessage(" No data Found");
				response.setHttpStatus(HttpStatus.NO_CONTENT);

				return response;

			}

			Status status = new Status();
			status = statusRepo.findByStatusId(userSurveyorBasicDetails.getCurrentStatus());
			userSurveyorBasicDetails.setStatus(status.getDescription());
			map.put("userSurveyorBasicDetails", userSurveyorBasicDetails);

			log.info("Request : Finding all survey wizad details by companyGenId: " + companyGenId
					+ ", insurenceGenId: " + insurenceGenId + ", And workedByUserId: " + workedByUserId
					+ "  Method Name" + methodName + " Class : " + this.getClass());

//			Change -- Aman -- Start
//			workedByUserId = userSurveyorBasicDetails.getAssistantEmail();
			PolicyDetails policyDetails = policyDetailsRepo
					.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
			VehicleDetails vehicleDetails = vehicleDetailsRepo
					.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
			DriverParticularsDetails driverParticularsDetails = driverParticularsDetailsRepo
					.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
			MetalCalculation metalCalc = null;
			try {

				metalCalc = metalCalculationRepository.findByInsurenceGenIdAndCompanyGenId(insurenceGenId,
						companyGenId);
			} catch (Exception e) {
				// TODO: handle exception
			}
			LossDetails lossDetails = lossDetailsRepo
					.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
			try {
				lossDetails.setTotalEstimate(metalCalc.getTotalestimate());
				lossDetails.setTaxPaidToggle(metalCalc.getTaxPaidToggle());
				lossDetails.setGstPortion(metalCalc.getGstPortion());
				lossDetails.setSubtotal(metalCalc.getSubtotal());

			} catch (Exception e) {
				// TODO: handle exception
			}
			List<AssemblyDetails> assemblyDetails = assemblyDetailsRepo
					.findByInsurenceGenIdAndCompanyGenIdOrderByOrderSrAsc(insurenceGenId, companyGenId);
			List<LabourDetails> labourDetails = labourDetailsRepo
					.findByInsurenceGenIdAndCompanyGenIdOrderByOrderSrAsc(insurenceGenId, companyGenId);
			List<DamageDetails> damageDetails = damageDetailsRepo
					.findByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);
			
			CompanyRegistered companyDetails = companyRegisteredRepo.findByCompanyGenId(companyGenId);
			Registered register = null;
			try {
				register = registeredRepo.findByUserNameAndCompanyRegGenId(userSurveyorBasicDetails.getSurveyorEmail(),
						companyGenId);

			} catch (Exception e) {
				// TODO: handle exception
			}

			DocUUidModel docUUid = new DocUUidModel();
			BeanUtils.copyProperties(companyDetails, docUUid);
			if (register != null) {
				docUUid.setSignUUID(register.getSignUUID());
				docUUid.setSignName(register.getSignName());
			} else {
				log.warn("Signature not Uploaded " + "Respond : data founded successfully for " + "companyGenId : "
						+ companyGenId + ", insurenceGenId : " + insurenceGenId + ", workedByUserId : " + workedByUserId
						+ "Method Name" + methodName + " Class : " + this.getClass());

			}

//
			Conclusion conclusion = conclusionRepo.findByInsurenceGenIdAndCompanyGenId(insurenceGenId,companyGenId);

//			Change -- AMAN -- End
			
			
			FinalConclusion finalconclusion = finalConclusionRepo.findByInsurenceGenId(insurenceGenId);
			ConclusionModel conclusionModel = null;
			if (conclusion != null) {
				List<String> enclosures = enclosuresRepo.findByConclusionId(conclusion.getSr()).stream()
						.map(item -> item.getDescription()).collect(Collectors.toList());
				List<String> observations = observationsRepo.findByConclusionId(conclusion.getSr()).stream()
						.map(item -> item.getDescription()).collect(Collectors.toList());
				List<String> notes = notesRepo.findByConclusionId(conclusion.getSr()).stream()
						.map(item -> item.getDescription()).collect(Collectors.toList());
				conclusionModel = new ConclusionModel();
				conclusionModel.setOfficerId(conclusion.getOfficerId());
				conclusionModel.setOfficerName(conclusion.getOfficerName());
				conclusionModel.setRemark(conclusion.getRemark());
				conclusionModel.setEnclosures(enclosures);
				conclusionModel.setObservations(observations);
				conclusionModel.setNotes(notes);
			}

			map.put("policyForm", policyDetails);
			map.put("vehicleForm", vehicleDetails);
			map.put("driverParticularsForm", driverParticularsDetails);
			map.put("lossDetailsForm", lossDetails);
			map.put("assemblyForm", assemblyDetails);
			map.put("labourForm", labourDetails);
			map.put("damageDetails", damageDetails);
			map.put("conclusionForm", conclusionModel);
			map.put("finalconclusionForm", finalconclusion);
			map.put("companySignDetails", docUUid);

			response.setData(map);
			response.setMessage("Data found");
			response.setHttpStatus(HttpStatus.OK);

			log.info("Respond : data founded successfully for " + "companyGenId : " + companyGenId
					+ ", insurenceGenId : " + insurenceGenId + ", workedByUserId : " + workedByUserId + "Method Name"
					+ methodName + " Class : " + this.getClass());

		} catch (Exception e) {
			log.error("An error occurred while Finding all basic details by insuranceClaimId: " + e.getMessage()
					+ " And companyRegGenId: " + companyGenId + "  Method Name" + methodName + " Class : "
					+ this.getClass());
		}
		return response;
	}

	public ResponseModel finalDocumentSubmmsion(FinalSubmissionRequest finalSubmissionRequest) {
		String methodName = "finalDocumentSubmission";
		ResponseModel model = new ResponseModel();

		log.info("[{}] - Started final document submission process.", methodName);
		log.debug("[{}] - Received request: {}", methodName, finalSubmissionRequest);

		try {
			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimId(finalSubmissionRequest.getInsurenceGenId());

			if (userSurveyorBasicDetails == null) {
				log.warn("[{}] - No userSurveyorBasicDetails details found for InsuranceGenId: {}", methodName,
						finalSubmissionRequest.getInsurenceGenId());
				
				model.setHttpStatus(HttpStatus.NO_CONTENT);
				model.setMessage("No userSurveyorBasicDetails details found for InsuranceGenId: "+finalSubmissionRequest.getInsurenceGenId());
				return model;
				
			}

			List<ImageData> images = finalSubmissionRequest.getImages();
			List<VideoData> videos = finalSubmissionRequest.getVideo();
			List<DocumentData> documents = finalSubmissionRequest.getDocuments();
			
			finalSubmissionRequest.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
			finalSubmissionRequest.setReferenceNo(userSurveyorBasicDetails.getReferenceNo());
			if(userSurveyorBasicDetails.getAsset()!=null && !userSurveyorBasicDetails.getAsset().equals("")) {
				finalSubmissionRequest.setVehicleNumber(userSurveyorBasicDetails.getAsset());
			}
			
			
			Map<String, Object> map = new HashMap<>();

			// Images
			if (images != null && !images.isEmpty()) {
				log.info("[{}] - Processing {} images.", methodName, images.size());
				for (ImageData image : images) {
					FinalDocumentSubmission submission = buildDocumentSubmission(finalSubmissionRequest, image);
					finalDocumentSubmissionRepo.save(submission);
					
//					Change -- Aman -- Start --01-09-2025
					PhotoPendingAi photoPendingAiData = savingPhotoForAi(finalSubmissionRequest, image);
					photoPendingAiRepo.save(photoPendingAiData);
//					Change -- Aman -- End --01-09-2025
					
					log.debug("[{}] - Saved image: {}", methodName, image.getImgName());
				}
				map.put("images", images);
			}

			// Videos
			if (videos != null && !videos.isEmpty()) {
				log.info("[{}] - Processing {} videos.", methodName, videos.size());
				for (VideoData video : videos) {
					FinalDocumentSubmission submission = buildDocumentSubmission(finalSubmissionRequest, video);
					finalDocumentSubmissionRepo.save(submission);
					log.debug("[{}] - Saved video: {}", methodName, video.getVideoName());
				}
				map.put("videos", videos);
			}

			// Documents
			if (documents != null && !documents.isEmpty()) {
				log.info("[{}] - Processing {} documents.", methodName, documents.size());
				for (DocumentData doc : documents) {
					FinalDocumentSubmission submission = buildDocumentSubmission(finalSubmissionRequest, doc);
					finalDocumentSubmissionRepo.save(submission);
					log.debug("[{}] - Saved document: {}", methodName, doc.getDocName());
				}
				map.put("documents", documents);

				// Update userSurveyorBasicDetails status
				if (userSurveyorBasicDetails != null 
						&& finalSubmissionRequest.getAction().equalsIgnoreCase("submit")
						&& (userSurveyorBasicDetails.getCurrentStatus() == 1
						        || userSurveyorBasicDetails.getCurrentStatus() == 2
						        || userSurveyorBasicDetails.getCurrentStatus() == 3
						        || userSurveyorBasicDetails.getCurrentStatus() == 4)
						) {
					
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.DOC_IMG_UPLOAD_APK);
					userSurveyorBasicDetails.setDocSubmitted(true);
					userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
					log.info("[{}] - Updated surveyor status to DOC_IMG_UPLOAD_APK.", methodName);
				}
			}

			model.setData(map);
			model.setHttpStatus(HttpStatus.OK);
			model.setMessage("Data Saved");
			log.info("[{}] - Final document submission completed successfully.", methodName);

		} catch (Exception e) {
			log.error("[{}] - Exception occurred during final document submission: {}", methodName, e.getMessage(), e);
			model.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			model.setMessage("Failed to save data: " + e.getMessage());
		}

		return model;
	}

	// Helper method for Images
	private FinalDocumentSubmission buildDocumentSubmission(FinalSubmissionRequest request, ImageData image) {
		FinalDocumentSubmission submission = new FinalDocumentSubmission();
		submission.setVehicleNumber(request.getVehicleNumber());
		submission.setCompanyGenId(request.getCompanyGenId());
		submission.setInsurenceGenId(request.getInsurenceGenId());
		submission.setReferenceNo(request.getReferenceNo());
		submission.setDocName(image.getImgName());
		submission.setDocType(image.getImgType());
		submission.setDocUuid(image.getImgUuid());
		submission.setUploadType("photo");
		submission.setDocUploadTime(image.getImgUploadTime());
		return submission;
	}
	
//	Change -- Aman -- Start --01-09-2025
	private PhotoPendingAi savingPhotoForAi(FinalSubmissionRequest request, ImageData image) {
		PhotoPendingAi submission = new PhotoPendingAi();
		submission.setVehicleNumber(request.getVehicleNumber());
		submission.setCompanyGenId(request.getCompanyGenId());
		submission.setInsurenceGenId(request.getInsurenceGenId());
		submission.setReferenceNo(request.getReferenceNo());
		submission.setDocName(image.getImgName());
		submission.setDocType(image.getImgType());
		submission.setDocUuid(image.getImgUuid());
		submission.setUploadType("photo");
		submission.setDocUploadTime(image.getImgUploadTime());
		return submission;
	}
//	Change -- Aman -- End --01-09-2025

	// Helper method for Videos
	private FinalDocumentSubmission buildDocumentSubmission(FinalSubmissionRequest request, VideoData video) {
		FinalDocumentSubmission submission = new FinalDocumentSubmission();
		submission.setVehicleNumber(request.getVehicleNumber());
		submission.setCompanyGenId(request.getCompanyGenId());
		submission.setInsurenceGenId(request.getInsurenceGenId());
		submission.setReferenceNo(request.getReferenceNo());
		submission.setDocName(video.getVideoName());
		submission.setDocType(video.getVideoType());
		submission.setDocUuid(video.getVideoUUID());
		submission.setUploadType("video");
		submission.setDocUploadTime(video.getVideoUploadTime());
		return submission;
	}

	// Helper method for Documents
	private FinalDocumentSubmission buildDocumentSubmission(FinalSubmissionRequest request, DocumentData doc) {
		FinalDocumentSubmission submission = new FinalDocumentSubmission();
		submission.setVehicleNumber(request.getVehicleNumber());
		submission.setCompanyGenId(request.getCompanyGenId());
		submission.setInsurenceGenId(request.getInsurenceGenId());
		submission.setReferenceNo(request.getReferenceNo());
		submission.setDocName(doc.getDocName());
		submission.setDocType(doc.getDocType());
		submission.setDocUuid(doc.getDocUuid());
		submission.setUploadType("document");
		submission.setDocUploadTime(doc.getDocUploadTime());
		return submission;
	}

	public ResponseModel finalDocumentView(String companyGenId, Long insurenceGenId, String vehicleNumber,
			String requestFor) {
		final String methodName = "finalDocumentView";
		ResponseModel response = new ResponseModel();

		log.info("[{}] - Invoked with vehicleNumber: {}", methodName, vehicleNumber);

		if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
			log.warn("[{}] - Vehicle number is null or empty", methodName);
			response.setHttpStatus(HttpStatus.BAD_REQUEST);
			response.setMessage("Vehicle number must not be null or empty.");
			return response;
		}

		try {
//			change -- Aman -- Start -- 08-09-2025
			List<String> formatsToFetch = new ArrayList<>();
			if(requestFor.equalsIgnoreCase("images")) {
//				formatsToFetch = Arrays.asList("jpeg", "png", "jpg", "mp4", "avi", "mov", "mkv");
				formatsToFetch = Arrays.asList("photo", "video");
			}else if(requestFor.equalsIgnoreCase("documents")) {
//				formatsToFetch = Arrays.asList("pdf", "doc", "docx", "xls", "xlsx");
				formatsToFetch = Arrays.asList("document");
			}

			// Case-insensitive search
			List<FinalDocumentSubmission> documentList =
			        finalDocumentSubmissionRepo.findByInsurenceGenIdAndCompanyGenIdAndVehicleNumberLikeIgnoreCaseAndUploadTypeIn(
			                insurenceGenId,
			                companyGenId,
			                vehicleNumber.trim(),
			                formatsToFetch
			        );
			
//			change -- Aman -- End -- 08-09-2025

			if (documentList == null || documentList.isEmpty()) {
				log.info("[{}] - No documents found for vehicleNumber: {}", methodName, vehicleNumber);
				response.setHttpStatus(HttpStatus.NO_CONTENT);
				response.setMessage("No documents found for the provided vehicle number.");
			} else {
				log.info("[{}] - Found {} document(s) for vehicleNumber: {}", methodName, documentList.size(),
						vehicleNumber);
				response.setHttpStatus(HttpStatus.OK);
				response.setData(documentList);
			}

		} catch (Exception e) {
			log.error("[{}] - Error occurred while fetching documents for vehicleNumber: {} - {}", methodName,
					vehicleNumber, e.getMessage(), e);
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("An error occurred while fetching final document details.");
		}

		return response;
	}

	public ResponseModel reportDocumentsUpload(ReportDocumentUploadModel reportDocumentUpload) {
		String methodName = "reportDocumentsUpload";
		ResponseModel model = new ResponseModel();

		if (reportDocumentUpload == null) {
			model.setHttpStatus(HttpStatus.BAD_REQUEST);
			model.setMessage("Request body is null");
			log.warn("Null request received in method: {}", methodName);
			return model;
		}
		UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
				.findByInsuranceClaimIdAndCompanyGenId(reportDocumentUpload.getInsurenceGenId(),
						reportDocumentUpload.getCompanyGenId());
		try {
			ReportDocumentUpload reportDocument = new ReportDocumentUpload();
			BeanUtils.copyProperties(reportDocumentUpload, reportDocument);
			reportDocument.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
			reportDocumentUploadRepo.save(reportDocument);

			userSurveyorBasicDetails.setCurrentStatus(CommonConstants.REPORT_DOC_UPLOADED);
			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
			log.info("Report Document Uploaded and save Status Method Name :" + methodName + " class :" + getClass());
			model.setHttpStatus(HttpStatus.OK);
			model.setMessage("Report Documents uploaded successfully.");
			model.setData(reportDocument); // optional, include if you want to return saved data
			log.info("Report document uploaded successfully for ID: {}", reportDocument.getInsurenceGenId());
		} catch (Exception e) {
			log.error("Exception occurred in method {}: {}", methodName, e.getMessage(), e);
			model.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			model.setMessage("Error while uploading report documents.");
		}

		return model;
	}

//	Change -- Aman -- Start
	public ResponseModel getVehicleDetailsBeforeLogin(String vehicleNo, String refId) {
//		Change -- Aman -- End
		String methodName = "getVehicleDetailsBeforeLogin";
		ResponseModel model = new ResponseModel();

		try {
			// Log entry into the method
			log.info("Request: Retrieving vehicle details. Method Name: " + methodName + ", Vehicle No: " + vehicleNo
					+ ", Ref ID: " + refId);
			Map<String, Object> map = new HashMap<String, Object>();
			// Check if either vehicleNo or refId is provided
//			if (vehicleNo != null || refId != null) {

//				Change -- Aman -- Start
			UserSurveyorBasicDetails details = new UserSurveyorBasicDetails();

			if (!vehicleNo.equalsIgnoreCase("")) {
				details = userSurveyorBasicDetailsRepo.findByAsset(vehicleNo);
			} else if (refId != null) {

				details = userSurveyorBasicDetailsRepo.findByReferenceNo(refId);
			}
			// Fetch vehicle details from the repository using either of the parameters
			VehicleDetails vehicleDetails = vehicleDetailsRepo.findByInsurenceGenId(details.getInsuranceClaimId());

//				Change -- Aman -- End

			// If no details found, return an error response
			if (details == null) {
				log.warn("No vehicle details found for Vehicle No: " + vehicleNo + " or Ref ID: " + refId);
				model.setMessage("Vehicle details not found.");
				model.setHttpStatus("404 Not Found");
				model.setData(null); // Data is null since no vehicle was found.
				model.setName(methodName);
				return model;
			}
			map.put("vehicleDetails", vehicleDetails);
			map.put("details", details);
			// Set the success response with the retrieved data
			model.setMessage("Vehicle details retrieved successfully.");
			model.setHttpStatus("200 OK");
			model.setData(map); // You can set specific fields if needed (e.g., vehicleDetails.getMake())
			model.setName(methodName);
//			} else {
//				// If neither vehicleNo nor refId is provided, return an error response
//				log.error("Vehicle number and Ref ID are both null. Cannot fetch vehicle details.");
//				model.setMessage("Either Vehicle number or Ref ID is required.");
//				model.setHttpStatus("400 Bad Request");
//				model.setData(null);
//				model.setName(methodName);
//			}

		} catch (Exception e) {
			// Log exception details
			log.error("An error occurred while fetching vehicle details in method: " + methodName, e);

			// Return error response in case of an exception
			model.setMessage("An error occurred while retrieving vehicle details.");
			model.setHttpStatus("500 Internal Server Error");
			model.setData(e.getMessage()); // Include exception message for debugging purposes
			model.setName(methodName);
		}

		return model;
	}

	public ResponseModel getVehicleDetailsBeforeLog(String param) {
		String methodName = "getVehicleDetailsBeforeLogin";
		ResponseModel model = new ResponseModel();
		Map<String, Object> resultMap = new HashMap<>();

		try {
			if (param == null || param.isEmpty()) {
				model.setHttpStatus("400 Bad Request");
				model.setMessage("Input parameter is required.");
				model.setName(methodName);
				return model;
			}

			// Identify param type
			String regNo = null;
			String phoneNo = null;
			String policyNo = null;

			// Simple example using regex (feel free to refine it)
			if (param.matches("\\d{10}")) { // 10-digit phone number
				phoneNo = param;
			} else if (param.matches("[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}")) { // e.g., MH12AB1234
				regNo = param;
			} else {
				policyNo = param;
			}

			// Log the parsed parameters
			log.info("Parsed Param - RegNo: {}, PhoneNo: {}, PolicyNo: {}", regNo, phoneNo, policyNo);

			// Fetch from DB using available parameters
			VehicleDetails vehicleDetails = null;
			UserSurveyorBasicDetails userDetails = null;

			if (regNo != null) {
				vehicleDetails = vehicleDetailsRepo.findByRegNo(regNo);
				userDetails = userSurveyorBasicDetailsRepo.findByAsset(regNo);
			}
//	        else if (phoneNo != null) {
//	            userDetails = userSurveyorBasicDetailsRepo.findByInsuredMobile(phoneNo);
//	            vehicleDetails = vehicleDetailsRepo.findByAsset(userDetails.getAsset());
//	        } else if (policyNo != null) {
//	            vehicleDetails = vehicleDetailsRepo.findByPolicyNo(policyNo);
//	            userDetails = userSurveyorBasicDetailsRepo.findByPolicyNo(policyNo);
//	        }

			// Check if data found
			if (vehicleDetails == null && userDetails == null) {
				model.setHttpStatus("404 Not Found");
				model.setMessage("No vehicle details found.");
				model.setName(methodName);
				return model;
			}

			resultMap.put("vehicleDetails", vehicleDetails);
			resultMap.put("userDetails", userDetails);

			// Build success response
			model.setHttpStatus("200 OK");
			model.setMessage("Vehicle details retrieved successfully.");
			model.setData(resultMap);
			model.setName(methodName);

		} catch (Exception e) {
			log.error("An error occurred in {}", methodName, e);
			model.setHttpStatus("500 Internal Server Error");
			model.setMessage("An error occurred while retrieving vehicle details.");
			model.setData(e.getMessage());
			model.setName(methodName);
		}

		return model;
	}

//	public ResponseModel check() {
//
//		
//		ResponseModel model= new ResponseModel();
//		FinalConclusion file=	finalConclusionRepo.findBySr(25);
//		model.setData(file);
//		return model;
//	}

	public String calculateMetalDep(long insurenceGenId, String companyGenId) throws ParseException {
		String methodName = "calculateMetalDep";
		try {
			String sql = "select v.dor,l.date_of_loss\r\n"
					+ "from insuredb.vehicle_details v join insuredb.loss_details l\r\n"
					+ "on v.insurence_gen_id =l.insurence_gen_id\r\n" + "where v.insurence_gen_id='" + insurenceGenId
					+ "' and v.company_gen_id='" + companyGenId + "'";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			Object[] row = results.get(0);

			String dorStrng = (String) row[0];

			String dateStr = (String) row[0];
			Instant instant = Instant.parse(dateStr);
			Timestamp dor = Timestamp.from(instant);

			Timestamp dol = (Timestamp) row[1];

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
		} catch (Exception e) {
			log.error("An error occurred error :  " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return "";

	}

	public String sendFinalSubSurveyWizardEmail(UserSurveyorBasicDetails userSurveyorBasicDetails) {
		String methodName = "sendFinalSubSurveyWizardEmail";
		try {
			log.info("Request : Sending email for survey Wizard Final Submission  by email id: "
					+ userSurveyorBasicDetails.getCompanyGenId() + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			CommonMailModel commonMailModel = new CommonMailModel();
			Map<String, Object> param = new HashMap<>();
			param.put("reference_no", userSurveyorBasicDetails.getReferenceNo());
			param.put("insurer_name", userSurveyorBasicDetails.getInsurerName());
			param.put("policy_no", userSurveyorBasicDetails.getPolicyNo());
			param.put("claim_no", userSurveyorBasicDetails.getClaimNo());
			param.put("asset", userSurveyorBasicDetails.getAsset());
//			param.put("date_of_loss", userSurveyorBasicDetails.getDateOfloss().toString());
			param.put("date_of_loss", dateFormate.format(userSurveyorBasicDetails.getDateOfloss()));
			param.put("survey_location_address", userSurveyorBasicDetails.getSurveyLocationAddress());
			param.put("surveyor", userSurveyorBasicDetails.getSurveyor());
			param.put("insured_name", userSurveyorBasicDetails.getInsuredName());

			List<String> docUUIDList = new ArrayList<>();
			
			List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(
					userSurveyorBasicDetails.getCompanyGenId(),userSurveyorBasicDetails.getInsuranceClaimId(),"final_report");
			if(docList!=null && !docList.isEmpty()) {
				FinalDocumentSubmission finalDocumentSubmission=docList.get(0);
				docUUIDList.add(finalDocumentSubmission.getDocUuid().toString());
			}

//			List<Map<String, Object>> docMapList = finalDocumentSubmissionRepo.findByCompanyGenIdAndAndInsurenceGenId(
//					commonMailModel.getInsurenceGenId(), commonMailModel.getCompanyGenId());
//			if (docMapList != null && !docMapList.isEmpty()) {
//				for (Map<String, Object> map : docMapList) {
//					for (Map.Entry<String, Object> entry : map.entrySet()) {
//						if (entry.getValue() != null && entry.getValue().toString().equalsIgnoreCase("final_report")) {
//							Object docUuid = map.get("docUuid");
//							if (docUuid != null) {
//								docUUIDList.add(docUuid.toString());
//							}
//						}
//					}
//				}
//			}

			commonMailModel.setInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
			commonMailModel.setReferenceNo(userSurveyorBasicDetails.getReferenceNo());;
			commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
			commonMailModel.setTemplateId(CommonConstants.FINAL_SUBMISSION_EMAIL_TEMPLATE_ID);
			commonMailModel.setParam(param);
			commonMailModel.setAttachment(true);
			commonMailModel.setDocUUIDList(docUUIDList);

//			if (userSurveyorBasicDetails.getSurveyType().trim().equalsIgnoreCase("spot")) {
			commonMailModel.setEmailList(
					List.of(userSurveyorBasicDetails.getInsuredEmail(), userSurveyorBasicDetails.getCompanyGenId()));
//			}
//			else if(saveUserSurveyorBasicDetailsModel.getSurveyType().trim().equalsIgnoreCase("final")) {
//				commonMailModel.setEmailList(List.of(saveUserSurveyorBasicDetailsModel.getInsuredEmail(),
//	                                                 saveUserSurveyorBasicDetailsModel.getCompanyGenId(),
//	                                                 saveUserSurveyorBasicDetailsModel.getCompanyGenId()));
//			}

			ResponseModel emailResponse = emailServiceImpl.sendEmail(commonMailModel);

			if (emailResponse.getMessage().toString().equalsIgnoreCase("success")) {
				log.info("Response : Email Send successfully for survey Wizard Final Submission by email id: "
						+ userSurveyorBasicDetails.getCompanyGenId() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
				return "success";
			} else {
				log.error("Response : Email Send unsuccessfull for survey Wizard Final Submission by email id: "
						+ userSurveyorBasicDetails.getCompanyGenId() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			}
		} catch (Exception e) {
			log.error("An error occurred while sending email " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}

		return "failure";
	}

	public boolean deleteDocumentByUuid(String docUuid) {
		Optional<FinalDocumentSubmission> documentOpt = finalDocumentSubmissionRepo.findByDocUuid(docUuid);

		if (documentOpt.isPresent()) {
			FinalDocumentSubmission doc = documentOpt.get();

			// Optional logging
			log.info("Deleting document: {} with UUID: {}", doc.getDocName(), docUuid);

			finalDocumentSubmissionRepo.delete(doc);
			return true;
		}
		return false;
	}

	public String sendAssessmentEmail(UserSurveyorBasicDetails userSurveyorBasicDetails) {
		String methodName = "sendAssessmentEmail";
		try {
			log.info("Request : Sending email for survey Wizard Assessment  by email id: "
					+ userSurveyorBasicDetails.getCompanyGenId() + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			CommonMailModel commonMailModel = new CommonMailModel();
			Map<String, Object> param = new HashMap<>();
			param.put("reference_no", userSurveyorBasicDetails.getReferenceNo());
			param.put("insurer_name", userSurveyorBasicDetails.getInsurerName());
			param.put("policy_no", userSurveyorBasicDetails.getPolicyNo());
			param.put("claim_no", userSurveyorBasicDetails.getClaimNo());
			param.put("asset", userSurveyorBasicDetails.getAsset());
//			param.put("date_of_loss", userSurveyorBasicDetails.getDateOfloss().toString());
			param.put("date_of_loss", dateFormate.format(userSurveyorBasicDetails.getDateOfloss()));
			param.put("survey_location_address", userSurveyorBasicDetails.getSurveyLocationAddress());
			param.put("surveyor", userSurveyorBasicDetails.getSurveyor());
			param.put("insured_name", userSurveyorBasicDetails.getInsuredName());

			Timestamp timestampDeadline = Timestamp.valueOf(LocalDateTime.now().plusDays(3));

			// Format timestamp to dd-MM-yyyy
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedDate = timestampDeadline.toLocalDateTime().format(formatter);

			param.put("deadline", formattedDate);
			List<String> docUUIDList = new ArrayList<>();
			docUUIDList.add("ef5c8d03-e609-4387-b788-5ea55c2f03f4");

			commonMailModel.setInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
			commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
			commonMailModel.setTemplateId(CommonConstants.ASSESSMENT_SUBMISSION_EMAIL_TEMPLATE_ID);
			commonMailModel.setParam(param);
			commonMailModel.setAttachment(true);
			commonMailModel.setDocUUIDList(docUUIDList);

//			if(userSurveyorBasicDetails.getSurveyType().trim().equalsIgnoreCase("spot")) {
			commonMailModel.setEmailList(
					List.of(userSurveyorBasicDetails.getInsuredEmail(), userSurveyorBasicDetails.getCompanyGenId())); // --pending

//			}
//			else if(userSurveyorBasicDetails.getSurveyType().trim().equalsIgnoreCase("final")) {
//				commonMailModel.setEmailList(List.of(userSurveyorBasicDetails.getInsuredEmail(),
//						userSurveyorBasicDetails.getCompanyGenId(),
//						userSurveyorBasicDetails.getCompanyGenId()));
//			}

			ResponseModel emailResponse = emailServiceImpl.sendEmail(commonMailModel);

			if (emailResponse.getMessage().toString().equalsIgnoreCase("success")) {
				log.info("Response : Email Send successfully for survey Wizard Assessment by email id: "
						+ userSurveyorBasicDetails.getCompanyGenId() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
				return "success";
			} else {
				log.error("Response : Email Send unsuccessfull for survey Wizard Assessment by email id: "
						+ userSurveyorBasicDetails.getCompanyGenId() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			}
		} catch (Exception e) {
			log.error("An error occurred while sending email " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}

		return "failure";
	}

	public ResponseModel deleteAssemblyAndLabourEntry(Long sr, Long insurenceGenId) {
		final String methodName = "deleteAssemblyAndLabourEntry";
		ResponseModel response = new ResponseModel();

		log.info("[{}] - Deletion started. sr: {}, insurenceGenId: {}", methodName, sr, insurenceGenId);

		try {
			Optional<AssemblyDetails> assemblyOpt = assemblyDetailsRepo.findById(sr);

			if (assemblyOpt.isPresent()) {
				AssemblyDetails assembly = assemblyOpt.get();
				String assemblyName = assembly.getAssemblyName();

				// Step 1: Delete AssemblyDetails first
				assemblyDetailsRepo.deleteById(sr);
				log.info("[{}] - Deleted AssemblyDetails with sr: {}", methodName, sr);

				// Step 2: Find and delete LabourDetails by insurenceGenId + assemblyName
				Optional<LabourDetails> labourOpt = labourDetailsRepo
						.findByInsurenceGenIdAndLabourPartsNameIgnoreCase(insurenceGenId, assemblyName);

				if (labourOpt.isPresent()) {
					labourDetailsRepo.deleteById(labourOpt.get().getSr());
					log.info("[{}] - Deleted LabourDetails for insurenceGenId: {}, assemblyName: {}", methodName,
							insurenceGenId, assemblyName);
				} else {
					log.warn("[{}] - No LabourDetails found for insurenceGenId: {}, assemblyName: {}", methodName,
							insurenceGenId, assemblyName);
				}

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("AssemblyDetails deleted. Corresponding LabourDetails deleted if found.");

			} else {
				log.warn("[{}] - AssemblyDetails not found with sr: {}", methodName, sr);
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("AssemblyDetails not found.");
			}

		} catch (Exception e) {
			log.error("[{}] - Error during deletion: {}", methodName, e.getMessage(), e);
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("Error occurred while deleting records.");
		}

		return response;
	}

	private String formatToTwoDecimal(String value) {
		try {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(2, RoundingMode.FLOOR); // use RoundingMode.HALF_UP if you want rounding
			return bd.toString();
		} catch (NumberFormatException e) {
			return value; // or return "0.00"; if preferred
		}
	}

	public String convertListToString(List<String> licenceTypeList) {
		if (licenceTypeList == null || licenceTypeList.isEmpty()) {
			return "";
		}
		return String.join(", ", licenceTypeList);
	}

	@Override
	public ResponseModel workflowBySurveyor(WorkflowModel workflowModel) {
		String methodName = "workflowBySurveyor";
		ResponseModel response = new ResponseModel();

		try {
			log.info(workflowModel.getButtonActivity() + " Saving workflow By Surveyor loginId: "
					+ workflowModel.getUserId() + " for InsuranceClaimId: " + workflowModel.getInsuranceClaimId()
					+ " Method Name" + methodName + " Class : " + this.getClass());

			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimIdAndCompanyGenId(workflowModel.getInsuranceClaimId(),
							workflowModel.getCompanyGenId());
			if (userSurveyorBasicDetails != null) {
				userSurveyorBasicDetails.setRemarks(workflowModel.getComment());
				userSurveyorBasicDetails.setAssignTo(userSurveyorBasicDetails.getAssistantEmail());
				userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));

				if (workflowModel.getButtonActivity().equalsIgnoreCase(CommonConstants.BUTTON_APPROVED)) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.APPROVED_STATUS);
				} else if (workflowModel.getButtonActivity().equalsIgnoreCase(CommonConstants.BUTTON_REWORK)) {
					userSurveyorBasicDetails.setCurrentStatus(CommonConstants.REFRE_BACK_STATUS);
				}

				userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data updated Successfully");

				log.info("Respond : data " + workflowModel.getButtonActivity()
						+ " Saving workflow By Surveyor loginId: " + workflowModel.getUserId()
						+ " for InsuranceClaimId: " + workflowModel.getInsuranceClaimId() + " Method Name " + methodName
						+ " Class : " + this.getClass());
			}
		} catch (Exception e) {
			log.error("An error occurred while updating Saving workflow By Surveyor loginId: " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	
	@Override
	public ResponseModel saveAssesmentPro(PartsAssessmentProJson models) {
	    ResponseModel response = new ResponseModel();
	    try {
	        List<PartAssessmentPro> savedList = new ArrayList<>();

	        // 1. Save Parts list
	        for (PartAssessmentProModel model : models.getParts()) {
	            PartAssessmentPro entity =
	                    partAssessmentProRepo.findByCompanyGenIdAndInsurenceGenIdAndId(
	                            model.getCompanyGenId(), model.getInsurenceGenId(), model.getId());

	            if (entity == null) {
	                entity = new PartAssessmentPro();
	                entity.setCreatedDtm(Timestamp.from(Instant.now())); 
	            }

	            // map fields
	            entity.setCompanyGenId(model.getCompanyGenId());
	            entity.setInsurenceGenId(model.getInsurenceGenId());

	            // ✅ Corrected sNo handling
				try {
					if (model.getsNo() != null) {
						entity.setsNo(model.getsNo().stripTrailingZeros());
					}
					
				} catch (Exception e) {
//		            entity.setsNo(model.getsNo());
				}

	            entity.setHsn(model.getHsn());
	            entity.setPartsName(model.getPartsName());
	            entity.setType(model.getType());
	            entity.setDep(model.getDep());
	            entity.setEstimated(model.getEstimated());
	            entity.setQeQa(model.getQeQa());
	            entity.setRemarks(model.getRemarks());
	            entity.setAllowed(model.getAllowed());
	            entity.setGstRate(model.getGstRate());
	            entity.setGst(model.getGst());
	            entity.setBillSr(model.getBillSr());
	            entity.setWithTax(model.getWithTax());
	            entity.setDepAmount(model.getDepAmount());
	            entity.setAssessed(model.getAssessed());
	            entity.setImt(model.getImt());
	            entity.setTax(model.getTax());
	            entity.setTaxPaid(model.getTaxPaid());

	            entity.setMetalParts(model.getMetalParts());
	            entity.setMetalGst(model.getMetalGst());
	            entity.setMetalDep(model.getMetalDep());

	            entity.setPlasticParts(model.getPlasticParts());
	            entity.setPlasticGst(model.getPlasticGst());
	            entity.setPlasticDep(model.getPlasticDep());

	            entity.setGlassParts(model.getGlassParts());
	            entity.setGlassGst(model.getGlassGst());

	            entity.setSecondHand(model.getSecondHand());
	            entity.setSecondHandGst(model.getSecondHandGst());
	            entity.setSecondHandDep(model.getSecondHandDep());

	            entity.setOtherParts(model.getOtherParts());
	            entity.setOtherGst(model.getOtherGst());
	            entity.setOtherDep(model.getOtherDep());

	            entity.setTotalParts(model.getTotalParts());

	            entity.setGst18Percent(model.getGst18Percent());
	            entity.setTax18Percent(model.getTax18Percent());
	            entity.setDep18Percent(model.getDep18Percent());

	            entity.setGst28Percent(model.getGst28Percent());
	            entity.setTax28Percent(model.getTax28Percent());
	            entity.setDep28Percent(model.getDep28Percent());

	            entity.setGst0Percent(model.getGst0Percent());
	            entity.setTax0Percent(model.getTax0Percent());
	            entity.setDep0Percent(model.getDep0Percent());

	            entity.setGst5Percent(model.getGst5Percent());
	            entity.setTax5Percent(model.getTax5Percent());
	            entity.setDep5Percent(model.getDep5Percent());

	            entity.setMajorAssemble(model.getMajorAssemble());
	            entity.setIsSubRow(model.getIsSubRow());
	            entity.setParentSNo(model.getParentSNo());
	            entity.setSubRowIndex(model.getSubRowIndex());
	            entity.setExAllowed(model.getExAllowed());
	            entity.setExDep(model.getExDep());
	            entity.setExTax(model.getExTax());

	            entity.setUpdatedDtm(Timestamp.from(Instant.now()));

	            savedList.add(partAssessmentProRepo.save(entity));
	        }

	        // 2. Save Totals
	        if (models.getTotals() != null) {
	            log.info("Saving totals after saving part details for companyGenId: {}, insuranceGenId: {}",
	                    models.getTotals().getCompanyGenId(), models.getTotals().getInsurenceGenId());

	            ResponseModel totalResponse = savePartTotalAssessmentPro(models.getTotals() , models.getInvoice());

	         // build invoice map
	            Map<String, Object> invoiceMap = new HashMap<>();
	            PartsAssessmentProJson.Invoice invoice = models.getInvoice(); // or from totalResponse if it returns invoice details
	            if (invoice != null) {
	                invoiceMap.put("invoiceNumber", invoice.getInvoiceNumber());
	                invoiceMap.put("amount", invoice.getAmount());
	                invoiceMap.put("issuedOn", invoice.getIssuedOn());
	                invoiceMap.put("receivedOn", invoice.getReceivedOn());
	            }
	            
	            // merge response data (optional)
	            response.setData(Map.of(
	                "parts", savedList,
	                "totals", totalResponse.getData(),
		            "invoice", invoiceMap
	            ));
	        } else {
	            response.setData(savedList);
	        }
	        try {
				
	    		UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
	    				.findByInsuranceClaimIdAndCompanyGenId(models.getTotals().getInsurenceGenId(), models.getTotals().getCompanyGenId());
	    		userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));

	    		int currentStatus = userSurveyorBasicDetails.getCurrentStatus();
//	    		if (currentStatus == CommonConstants.BASIC_INITIAL_DETAILS
//	    				) {
	    		// Change -- Aman -- Start --- 16-09-2025
				if (currentStatus == CommonConstants.LOSS_DETAILS_FILLED) {
		    		userSurveyorBasicDetails.setCurrentStatus(CommonConstants.ASSESSMENT_PRO_FILLED);	
	    			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
				}
//					Change -- Aman -- End --- 16-09-2025
				
//	    			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
//	    		}
	        	} catch (Exception e) {
	    			// TODO: handle exception
	    		}

	        response.setMessage("Assessment Parts & Totals saved/updated successfully");
	        response.setHttpStatus(HttpStatus.OK);

	    } catch (Exception e) {
	        log.error("Error while saving assessment parts & totals", e);
	        response.setMessage("Error while saving assessment parts: " + e.getMessage());
	        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    return response;
	}

	
	


@Override
public ResponseModel ViewAssessmentPro(String companyGenId, Long insurenceGenId) {
    ResponseModel response = new ResponseModel();
    Map<String, Object> map = new HashMap<>();
    try {
        log.info("Fetching PartAssessmentPro for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);

    	
//    	Change -- Aman -- Start -- 05-09-2025
        CalculationPartAssessmentPro entity =
        		calculationPartAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(companyGenId,insurenceGenId);
    	
      map.put("policyType", entity.getPolicyType());
      map.put("gstOnEstimate", entity.getGstOnEstimate());
      
      Map<String, Object> invoiceMap = new HashMap<>();
      invoiceMap.put("invoiceNumber", entity.getInvoiceNumber());
      invoiceMap.put("amount", entity.getAmount());
      invoiceMap.put("issuedOn", entity.getIssuedOn());
      invoiceMap.put("receivedOn", entity.getReceivedOn());

      map.put("invoice", invoiceMap);
      

        List<PartAssessmentPro> allPartAssessmentPro =
                partAssessmentProRepo.findByCompanyGenIdAndInsurenceGenIdOrderBySNo(companyGenId, insurenceGenId);

        if (allPartAssessmentPro != null && !allPartAssessmentPro.isEmpty()) {
            log.info("Found {} PartAssessmentPro records", allPartAssessmentPro.size());

            // ✅ Fix sNo formatting
            allPartAssessmentPro.forEach(p -> {
                if (p.getsNo() != null) {
                    p.setsNo(p.getsNo().stripTrailingZeros());
                }
            });

            map.put("parts", allPartAssessmentPro);

            // ✅ Handle totals with safe exception block
//            try {
//                CalculationPartAssessmentPro totalCalcParts =
//                        calculationPartAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(companyGenId, insurenceGenId);
//
//                if (totalCalcParts != null) {
//                    map.put("totals", totalCalcParts); // return full entity
//                } else {
//                    log.warn("No totals found for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);
//                    map.put("totals", new HashMap<>()); // return empty object instead of null
//                }
//            } catch (Exception ex) {
//                log.error("Error fetching totals for companyGenId: {} and insurenceGenId: {}. Exception: {}",
//                        companyGenId, insurenceGenId, ex.getMessage(), ex);
//                map.put("totals", new HashMap<>()); // fallback empty object
//            }

            response.setData(map);
            response.setMessage("Data fetched successfully");
            response.setHttpStatus(HttpStatus.OK);
        } else {
            log.warn("No PartAssessmentPro records found for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);
            response.setData(Collections.emptyList());
            response.setMessage("No records found");
            response.setHttpStatus(HttpStatus.NO_CONTENT);
        }

    } catch (Exception e) {
        log.error("Error while fetching PartAssessmentPro for companyGenId: {} and insurenceGenId: {}. Exception: {}",
                companyGenId, insurenceGenId, e.getMessage(), e);
        response.setData(null);
        response.setMessage("Error: " + e.getMessage());
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return response;
}



@Override
public ResponseModel LabourAssessmentPro(LabourAssessmentProJson models) {
	 ResponseModel response = new ResponseModel();
	    try {
	        List<LabourAssessmentPro> savedList = new ArrayList<>();

	        for (LabourAssessmentProModel model : models.getLabour()) {
	          LabourAssessmentPro entity =
	                    labourAssessmentProRepo.findByCompanyGenIdAndInsurenceGenIdAndId(
	                            model.getCompanyGenId(), model.getInsurenceGenId(), model.getId());

	            if (entity == null) {
	                entity = new LabourAssessmentPro(); // new record
	                entity.setCreatedDtm(Timestamp.from(Instant.now())); // only once for new
	            }

	            // Map/update fields
	         BeanUtils.copyProperties(model, entity,"id");
	         try {
					if (model.getsNo() != null) {
						entity.setsNo(model.getsNo().stripTrailingZeros());
					}
					
				} catch (Exception e) {
//		            entity.setsNo(model.getsNo());
				}	   
	            // Always update this
	         
	            entity.setUpdatedDtm(Timestamp.from(Instant.now()));

	            savedList.add(labourAssessmentProRepo.save(entity));
	        }
	        
	        if (models.getTotals() != null) {
	            log.info("Saving totals after saving part details for companyGenId: {}, insuranceGenId: {}",
	                    models.getTotals().getCompanyGenId(), models.getTotals().getInsurenceGenId());

	            ResponseModel totalResponse = saveLabourTotalAssessmentPro(models.getTotals());

	            // merge response data (optional)
	            response.setData(Map.of(
	                "labour", savedList,
	                "totals", totalResponse.getData()
	            ));
	        }
	        else {
	        response.setData(savedList);}
	        response.setMessage("Assessment labour saved/updated successfully");

	        response.setHttpStatus(HttpStatus.OK);

	    } catch (Exception e) {
	        response.setMessage("Error while saving assessment labour: " + e.getMessage());
	        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    return response;
}


@Override
public ResponseModel ViewLabourAssessmentPro(String companyGenId, Long insurenceGenId) {
    ResponseModel response = new ResponseModel();
//    Map<String, Object> map = new HashMap<>();

    try {
        log.info("Fetching LabourAssessmentPro for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);
        CalculationLabourAssessmentPro entity=calculationLabourAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(companyGenId, insurenceGenId);
       boolean accidentLabour= entity.getAccidentalLabour();
       boolean paintLabour= entity.getPaintLabour();
       
       Map<String ,Object > map=new HashMap<>();
        List<LabourAssessmentPro> labourAssessmentProList =
                labourAssessmentProRepo.findByCompanyGenIdAndInsurenceGenIdOrderBySNo(companyGenId, insurenceGenId);

        if (labourAssessmentProList != null && !labourAssessmentProList.isEmpty()) {
            log.info("Found {} LabourAssessmentPro records", labourAssessmentProList.size());
            
            labourAssessmentProList.forEach(p -> {
                if (p.getsNo() != null) {
                    p.setsNo(p.getsNo().stripTrailingZeros());
                }
            });
            
//            map.put("labour", labourAssessmentProList);

            // ✅ Handle totals with safe exception block
//            try {
//                CalculationLabourAssessmentPro totalCalcParts =
//                        calculationLabourAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(companyGenId, insurenceGenId);
//
//                if (totalCalcParts != null) {
//                    map.put("totals", totalCalcParts); // return full entity
//                } else {
//                    log.warn("No totals found for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);
//                    map.put("totals", new HashMap<>()); // return empty object instead of null
//                }
//            } catch (Exception ex) {
//                log.error("Error fetching totals for companyGenId: {} and insurenceGenId: {}. Exception: {}",
//                        companyGenId, insurenceGenId, ex.getMessage(), ex);
//                map.put("totals", new HashMap<>()); // fallback empty object
//            }
            map.put("accidentLabour", accidentLabour);
            map.put("paintLabour", paintLabour);
            map.put("labour", labourAssessmentProList);
            response.setData(map);
            response.setMessage("Data fetched successfully");
            response.setHttpStatus(HttpStatus.OK);
        } else {
            log.warn("No LabourAssessmentPro records found for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);
            response.setData(Collections.emptyList());
            response.setMessage("No records found");
            response.setHttpStatus(HttpStatus.NO_CONTENT);
        }

    } catch (Exception e) {
        log.error("Error while fetching LabourAssessmentPro for companyGenId: {} and insurenceGenId: {}. Exception: {}",
                companyGenId, insurenceGenId, e.getMessage(), e);
        response.setData(null);
        response.setMessage("Error: " + e.getMessage());
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return response;
}

//@Override
//    @Transactional
//    public ResponseModel deletePartAndLabourAssessmentPro(Long id, Long insurenceGenId) {
//        ResponseModel response = new ResponseModel();
//        try {
//            log.info("Delete request received for PartAssessmentPro with id: {} and insurenceGenId: {}", id, insurenceGenId);
//
//            // Step 1: Find the PartAssessmentPro by id + insurenceGenId
//            Optional<PartAssessmentPro> optionalPart =
//                    partAssessmentProRepo.findByIdAndInsurenceGenId(id, insurenceGenId);
//
//            if (optionalPart.isPresent()) {
//                PartAssessmentPro partEntity = optionalPart.get();
//                String partName = partEntity.getPartsName();
//
//                log.debug("Part found: id={}, insurenceGenId={}, partName={}", id, insurenceGenId, partName);
//
//                // Step 2: Delete the part entry
//                partAssessmentProRepo.delete(partEntity);
//                log.info("Deleted PartAssessmentPro with id={} and insurenceGenId={}", id, insurenceGenId);
//
//                // Step 3: Find matching LabourAssessmentPro by partName + insurenceGenId
//                LabourAssessmentPro labourEntity =
//                        labourAssessmentProRepo.findByPartsNameAndInsurenceGenId(partName, insurenceGenId);
//
//                if (labourEntity != null) {
//                    labourAssessmentProRepo.delete(labourEntity);
//                    log.info("Deleted LabourAssessmentPro with partName={} and insurenceGenId={}", partName, insurenceGenId);
//                } else {
//                    log.warn("No LabourAssessmentPro found for partName={} and insurenceGenId={}", partName, insurenceGenId);
//                }
//
//                response.setMessage("Deleted Part and related Labour successfully");
//                response.setHttpStatus(HttpStatus.OK);
//            } else {
//                log.warn("No PartAssessmentPro found with id={} and insurenceGenId={}", id, insurenceGenId);
//                response.setMessage("No Part found with given id and insurenceGenId");
//                response.setHttpStatus(HttpStatus.NOT_FOUND);
//            }
//
//        } catch (Exception e) {
//            log.error("Error while deleting Part/Labour with id={} and insurenceGenId={}. Exception: {}", id, insurenceGenId, e.getMessage(), e);
//            response.setMessage("Error while deleting Part/Labour: " + e.getMessage());
//            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return response;
//    }


@Override
@Transactional
public ResponseModel deletePartAndLabourAssessmentPro(Long id, Long insurenceGenId) {
    ResponseModel response = new ResponseModel();
    try {
        log.info("Delete request received for PartAssessmentPro with id: {} and insurenceGenId: {}", id, insurenceGenId);

        Optional<PartAssessmentPro> optionalPart =
                partAssessmentProRepo.findByIdAndInsurenceGenId(id, insurenceGenId);

        if (optionalPart.isPresent()) {
            PartAssessmentPro partEntity = optionalPart.get();
            BigDecimal sNo = partEntity.getsNo();
            String partName = partEntity.getPartsName();

            // keep original string form to avoid BigDecimal normalization issue
            String sNoStr = sNo.stripTrailingZeros().toPlainString();

            log.debug("Part found: id={}, insurenceGenId={}, sNo={}, partName={}", id, insurenceGenId, sNoStr, partName);

            // Case 1: parent (no dot in sNo) -> delete parent + all children
            if (!sNoStr.contains(".")) {
                log.info("Deleting parent part with sNo={} and all its subchildren", sNoStr);

                // delete parent
                partAssessmentProRepo.delete(partEntity);

                // delete all children starting with "1." (1.1, 1.2, 1.10 etc.)
                String prefix = sNoStr + ".";
                List<PartAssessmentPro> subChildren =
                        partAssessmentProRepo.findChildrenByPrefix(insurenceGenId, prefix);
//                if (subChildren != null || !subChildren.isEmpty()) {

	

                for (PartAssessmentPro child : subChildren) {
                    partAssessmentProRepo.delete(child);
                    log.info("Deleted subchild PartAssessmentPro sNo={}, partName={}", child.getsNo(), child.getPartsName());

                    // delete labour for subchild
                    LabourAssessmentPro labourChild =
                            labourAssessmentProRepo.findByPartsNameAndInsurenceGenId(child.getPartsName(), insurenceGenId);
                    if (labourChild != null) {
                        labourAssessmentProRepo.delete(labourChild);
                        log.info("Deleted LabourAssessmentPro for subchild partName={}", child.getPartsName());
                    }
                }
//}
                // delete labour for parent
                LabourAssessmentPro labourParent =
                        labourAssessmentProRepo.findByPartsNameAndInsurenceGenId(partName, insurenceGenId);
                if (labourParent != null) {
                    labourAssessmentProRepo.delete(labourParent);
                    log.info("Deleted LabourAssessmentPro for parent partName={}", partName);
                }

            } else {
                // Case 2: child (like 1.1, 1.10) -> delete only that row
                log.info("Deleting only subchild part with sNo={}", sNoStr);
                partAssessmentProRepo.delete(partEntity);

                LabourAssessmentPro labourEntity =
                        labourAssessmentProRepo.findByPartsNameAndInsurenceGenId(partName, insurenceGenId);
                if (labourEntity != null) {
                    labourAssessmentProRepo.delete(labourEntity);
                    log.info("Deleted LabourAssessmentPro for subchild partName={}", partName);
                }
            }

            response.setMessage("Deleted Part(s) and related Labour successfully");
            response.setHttpStatus(HttpStatus.OK);

        } else {
            log.warn("No PartAssessmentPro found with id={} and insurenceGenId={}", id, insurenceGenId);
            response.setMessage("No Part found with given id and insurenceGenId");
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }

    } catch (Exception e) {
        log.error("Error while deleting Part/Labour with id={} and insurenceGenId={}. Exception: {}", id, insurenceGenId, e.getMessage(), e);
        response.setMessage("Error while deleting Part/Labour: " + e.getMessage());
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
}


@Override
public ResponseModel ViewAssessmentProSabRowNo(String companyGenId, Long insurenceGenId, Integer sabRowNo) {
	  ResponseModel response = new ResponseModel();

	    try {
	 List<PartAssessmentPro> allPartAssessmentPro =
             partAssessmentProRepo.findByCompanyGenIdAndInsurenceGenIdAndIsSubRowAndParentSNoOrderBySNo(companyGenId, insurenceGenId,true,sabRowNo);

     if (allPartAssessmentPro != null && !allPartAssessmentPro.isEmpty()) {
         log.info("Found {} PartAssessmentPro records", allPartAssessmentPro.size());
         response.setData(allPartAssessmentPro);
         response.setMessage("Data fetched successfully");
         response.setHttpStatus(HttpStatus.OK);
     } else {
         log.warn("No PartAssessmentPro records found for companyGenId: {} and insurenceGenId: {}", companyGenId, insurenceGenId);
         response.setData(Collections.emptyList());
         response.setMessage("No records found");
         response.setHttpStatus(HttpStatus.NO_CONTENT);
     }
	
	    } catch (Exception e) {
	        log.error("Error while fetching PartAssessmentPro for companyGenId: {} and insurenceGenId: {}. Exception: {}",
	                companyGenId, insurenceGenId, e.getMessage(), e);
	        response.setData(null);
	        response.setMessage("Error: " + e.getMessage());
	        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    return response;

}

//@Override
//public ResponseModel getAggregatedValues(String companyGenId, Long insurenceGenId) {
//	  List<PartAssessmentPro> list = partAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(companyGenId, insurenceGenId);
//	  ResponseModel model = new ResponseModel();
//      PartAssessmentPro result = new PartAssessmentPro();
//
//      // Loop through and add all values
//      for (PartAssessmentPro p : list) {
//          result.setEstimated(result.getEstimated() + p.getEstimated());
//          result.setAllowed(result.getAllowed() + p.getAllowed());
//          result.setGst(result.getGst() + p.getGst());
//          result.setWithTax(result.getWithTax() + p.getWithTax());
//          result.setDepAmount(result.getDepAmount() + p.getDepAmount());
//          result.setAssessed(result.getAssessed() + p.getAssessed());
//          result.setTax(result.getTax() + p.getTax());
//          result.setTaxPaid(result.getTaxPaid() + p.getTaxPaid());
//
//          result.setMetalParts(result.getMetalParts() + p.getMetalParts());
//          result.setMetalGst(result.getMetalGst() + p.getMetalGst());
//          result.setMetalDep(result.getMetalDep() + p.getMetalDep());
//
//          result.setPlasticParts(result.getPlasticParts() + p.getPlasticParts());
//          result.setPlasticGst(result.getPlasticGst() + p.getPlasticGst());
//          result.setPlasticDep(result.getPlasticDep() + p.getPlasticDep());
//
//          result.setGlassParts(result.getGlassParts() + p.getGlassParts());
//          result.setGlassGst(result.getGlassGst() + p.getGlassGst());
//
//          result.setSecondHand(result.getSecondHand() + p.getSecondHand());
//          result.setSecondHandGst(result.getSecondHandGst() + p.getSecondHandGst());
//          result.setSecondHandDep(result.getSecondHandDep() + p.getSecondHandDep());
//
//          result.setOtherParts(result.getOtherParts() + p.getOtherParts());
//          result.setOtherGst(result.getOtherGst() + p.getOtherGst());
//          result.setOtherDep(result.getOtherDep() + p.getOtherDep());
//
//          result.setTotalParts(result.getTotalParts() + p.getTotalParts());
//
//          result.setGst18Percent(result.getGst18Percent() + p.getGst18Percent());
//          result.setTax18Percent(result.getTax18Percent() + p.getTax18Percent());
//          result.setDep18Percent(result.getDep18Percent() + p.getDep18Percent());
//
//          result.setGst28Percent(result.getGst28Percent() + p.getGst28Percent());
//          result.setTax28Percent(result.getTax28Percent() + p.getTax28Percent());
//          result.setDep28Percent(result.getDep28Percent() + p.getDep28Percent());
//
//          result.setGst0Percent(result.getGst0Percent() + p.getGst0Percent());
//          result.setTax0Percent(result.getTax0Percent() + p.getTax0Percent());
//          result.setDep0Percent(result.getDep0Percent() + p.getDep0Percent());
//
//          result.setGst5Percent(result.getGst5Percent() + p.getGst5Percent());
//          result.setTax5Percent(result.getTax5Percent() + p.getTax5Percent());
//          result.setDep5Percent(result.getDep5Percent() + p.getDep5Percent());
//
//          result.setExDep((result.getExDep() == null ? 0 : result.getExDep()) + (p.getExDep() == null ? 0 : p.getExDep()));
//          result.setExAllowed((result.getExAllowed() == null ? 0 : result.getExAllowed()) + (p.getExAllowed() == null ? 0 : p.getExAllowed()));
//          result.setExTax((result.getExTax() == null ? 0 : result.getExTax()) + (p.getExTax() == null ? 0 : p.getExTax()));
//      }
//model.setData(result);
//      return model;
//  
//}




public ResponseModel savePartTotalAssessmentPro(CalculationPartAssessmentProModel partCalcModel, PartsAssessmentProJson.Invoice invoice) {
    ResponseModel response = new ResponseModel();
    try {
        log.info("Request received to save TotalAssessmentPro for companyGenId: {}, insurenceGenId: {}",
                partCalcModel.getCompanyGenId(), partCalcModel.getInsurenceGenId());

        Timestamp now = new Timestamp(System.currentTimeMillis());

        CalculationPartAssessmentPro entity =
            calculationPartAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(
                partCalcModel.getCompanyGenId(),
                partCalcModel.getInsurenceGenId()
            );

        if (entity == null) {
            log.info("No existing record found → creating new record.");
            entity = new CalculationPartAssessmentPro();

            // set fields from model → entity
            mapModelToEntity(partCalcModel, entity, invoice);
            entity.setCreatedDtm(now);

            response.setName("CREATE");
            response.setMessage("New record created successfully");
        } else {
            log.info("Existing record found (id: {}) → updating record.", entity.getId());

            mapModelToEntity(partCalcModel, entity, invoice); 
            // don’t overwrite created date
            entity.setCreatedDtm(entity.getCreatedDtm());

            response.setName("UPDATE");
            response.setMessage("Record updated successfully");
        }

        entity.setUpdatedDtm(now);

        CalculationPartAssessmentPro savedEntity = calculationPartAssessmentProRepo.save(entity);

        log.info("Record saved successfully with id: {}", savedEntity.getId());

        response.setData(savedEntity);
        response.setHttpStatus(200);
        


    } catch (Exception e) {
        log.error("Error occurred while saving TotalAssessmentPro: ", e);

        response.setName("ERROR");
        response.setMessage("Error occurred while saving: " + e.getMessage());
        response.setHttpStatus(500);
        response.setData(null);
    }

    return response;
}

private void mapModelToEntity(CalculationPartAssessmentProModel model, CalculationPartAssessmentPro entity, PartsAssessmentProJson.Invoice invoice) {
    entity.setTotalEstimated(model.getTotalEstimated());
    entity.setTotalAllowed(model.getTotalAllowed());
    entity.setTotalGst(model.getTotalGst());
    entity.setTotalWithTax(model.getTotalWithTax());
    entity.setTotalDepAmount(model.getTotalDepAmount());
    entity.setTotalAssessed(model.getTotalAssessed());
    entity.setTotalTax(model.getTotalTax());
    entity.setTotalTaxPaid(model.getTotalTaxPaid());

    entity.setTotalMetalParts(model.getTotalMetalParts());
    entity.setTotalMetalGst(model.getTotalMetalGst());
    entity.setTotalMetalDep(model.getTotalMetalDep());

    entity.setTotalPlasticParts(model.getTotalPlasticParts());
    entity.setTotalPlasticGst(model.getTotalPlasticGst());
    entity.setTotalPlasticDep(model.getTotalPlasticDep());

    entity.setTotalGlassParts(model.getTotalGlassParts());
    entity.setTotalGlassGst(model.getTotalGlassGst());

    entity.setTotalSecondHand(model.getTotalSecondHand());
    entity.setTotalSecondHandGst(model.getTotalSecondHandGst());
    entity.setTotalSecondHandDep(model.getTotalSecondHandDep());

    entity.setTotalOtherParts(model.getTotalOtherParts());
    entity.setTotalOtherGst(model.getTotalOtherGst());
    entity.setTotalOtherDep(model.getTotalOtherDep());

    entity.setTotalParts(model.getTotalParts());

    entity.setTotalGst18Percent(model.getTotalGst18Percent());
    entity.setTotalTax18Percent(model.getTotalTax18Percent());
    entity.setTotalDep18Percent(model.getTotalDep18Percent());

    entity.setTotalGst28Percent(model.getTotalGst28Percent());
    entity.setTotalTax28Percent(model.getTotalTax28Percent());
    entity.setTotalDep28Percent(model.getTotalDep28Percent());

    entity.setTotalGst0Percent(model.getTotalGst0Percent());
    entity.setTotalTax0Percent(model.getTotalTax0Percent());
    entity.setTotalDep0Percent(model.getTotalDep0Percent());

    entity.setTotalGst5Percent(model.getTotalGst5Percent());
    entity.setTotalTax5Percent(model.getTotalTax5Percent());
    entity.setTotalDep5Percent(model.getTotalDep5Percent());

    entity.setTotalExDep(model.getTotalExDep());
    entity.setTotalExAllowed(model.getTotalExAllowed());
    entity.setTotalExTax(model.getTotalExTax());
    entity.setPolicyType(model.getPolicyType());
    entity.setCompanyGenId(model.getCompanyGenId());
    entity.setInsurenceGenId(model.getInsurenceGenId());
    entity.setGstOnEstimate(model.getGstOnEstimate());
    
    
    entity.setInvoiceNumber(invoice.getInvoiceNumber());
    entity.setAmount(invoice.getAmount());
    entity.setIssuedOn(invoice.getIssuedOn());
    entity.setReceivedOn(invoice.getReceivedOn());
}

public ResponseModel saveLabourTotalAssessmentPro(CalculationLabourAssessmentProModel partCalcModel) {
    ResponseModel response = new ResponseModel();
    try {
        log.info("Request received to save Labour TotalAssessmentPro for companyGenId: {}, insurenceGenId: {}",
                partCalcModel.getCompanyGenId(), partCalcModel.getInsurenceGenId());

        Timestamp now = new Timestamp(System.currentTimeMillis());

        CalculationLabourAssessmentPro entity =
            calculationLabourAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(
                partCalcModel.getCompanyGenId(),
                partCalcModel.getInsurenceGenId()
            );

        if (entity == null) {
            log.info("No existing record found → creating new record.");
            entity = new CalculationLabourAssessmentPro();

            // set fields from model → entity
            mapModelToEntityLabour(partCalcModel, entity);
            entity.setCreatedDtm(now);

            response.setName("CREATE");
            response.setMessage("New record created successfully");
        } else {
            log.info("Existing record found (id: {}) → updating record.", entity.getId());

            mapModelToEntityLabour(partCalcModel, entity); 
            // don’t overwrite created date
            entity.setCreatedDtm(entity.getCreatedDtm());

            response.setName("UPDATE");
            response.setMessage("Record updated successfully");
        }

        entity.setUpdatedDtm(now);

        CalculationLabourAssessmentPro savedEntity = calculationLabourAssessmentProRepo.save(entity);

        log.info("Record saved successfully with id: {}", savedEntity.getId());

        response.setData(savedEntity);
        response.setHttpStatus(200);

    } catch (Exception e) {
        log.error("Error occurred while saving TotalAssessmentPro: ", e);

        response.setName("ERROR");
        response.setMessage("Error occurred while saving: " + e.getMessage());
        response.setHttpStatus(500);
        response.setData(null);
    }

    return response;
}

private void mapModelToEntityLabour(CalculationLabourAssessmentProModel model, CalculationLabourAssessmentPro entity) {
   
	
	 entity.setTotalEstimated(model.getTotalEstimated());
     entity.setTotalReplace(model.getTotalReplace());
     entity.setTotalRepair(model.getTotalRepair());
     entity.setTotalPaintEstimate(model.getTotalPaintEstimate());
     entity.setTotalLess(model.getTotalLess());
     entity.setTotalAllowed(model.getTotalAllowed());
     entity.setTotalLabGst(model.getTotalLabGst());
     entity.setTotalEstimateGst(model.getTotalEstimateGst());
     entity.setTotalReplaceGst(model.getTotalReplaceGst());
     entity.setTotalRepairGst(model.getTotalRepairGst());
     entity.setTotalPaintEstimateGst(model.getTotalPaintEstimateGst());
     entity.setTotalAllowedPaintGst(model.getTotalAllowedPaintGst());
     entity.setTotalAllowedLabourT(model.getTotalAllowedLabourT());
     entity.setTotalLabourGstT(model.getTotalLabourGstT());
     entity.setTotalPaint75Labour(model.getTotalPaint75Labour());
     entity.setTotalGstPaint75Labour(model.getTotalGstPaint75Labour());
     entity.setTotalPaint25Part(model.getTotalPaint25Part());
     entity.setTotalGstPaint25Part(model.getTotalGstPaint25Part());
     entity.setPaintLabour(model.getPaintLabour());
     entity.setAccidentalLabour(model.getAccidentalLabour());

     entity.setCompanyGenId(model.getCompanyGenId());
     entity.setInsurenceGenId(model.getInsurenceGenId());
     entity.setPolicyType(model.getPolicyType());
	
}

@Override
public ResponseModel conclusionAssessmentPro(ConclusionAssessmentProModel models) {
    ResponseModel response = new ResponseModel();
    try {
        ConclusionAssessmentPro conclusionAssessmentPro =
                conclusionAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(
                        models.getCompanyGenId(), models.getInsurenceGenId());

        if (conclusionAssessmentPro == null) {
            conclusionAssessmentPro = new ConclusionAssessmentPro();
            conclusionAssessmentPro.setCompanyGenId(models.getCompanyGenId());
            conclusionAssessmentPro.setInsurenceGenId(models.getInsurenceGenId());
            conclusionAssessmentPro.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
        }

        // update timestamps
        conclusionAssessmentPro.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));

        // map fields (BigDecimal + Strings)
        conclusionAssessmentPro.setTotalEstParts(models.getTotalEstParts());
        conclusionAssessmentPro.setPaintMaterial25(models.getPaintMaterial25());
        conclusionAssessmentPro.setAccidentalLabour(models.getAccidentalLabour());
        conclusionAssessmentPro.setPaintLabour75(models.getPaintLabour75());
        conclusionAssessmentPro.setTotalEstimate(models.getTotalEstimate());
        conclusionAssessmentPro.setTowingCharges(models.getTowingCharges());
        conclusionAssessmentPro.setGrandTotal(models.getGrandTotal());

        conclusionAssessmentPro.setTotalEstPartsGrossAllowed(models.getTotalEstPartsGrossAllowed());
        conclusionAssessmentPro.setTotalEstPartsDepreciation(models.getTotalEstPartsDepreciation());
        conclusionAssessmentPro.setTotalEstPartsActualAllowed(models.getTotalEstPartsActualAllowed());
        conclusionAssessmentPro.setTotalEstPartsAssessed(models.getTotalEstPartsAssessed());
        conclusionAssessmentPro.setTotalEstPartsGST(models.getTotalEstPartsGST());
        conclusionAssessmentPro.setPaintMaterial25GST(models.getPaintMaterial25GST());
        conclusionAssessmentPro.setAccidentalLabourGST(models.getAccidentalLabourGST());
        conclusionAssessmentPro.setPaintLabour75GST(models.getPaintLabour75GST());
        conclusionAssessmentPro.setHalfIMT21I(models.getHalfIMT21I());
        conclusionAssessmentPro.setHalfIMT21II(models.getHalfIMT21II());

        conclusionAssessmentPro.setPaintMaterial25GrossAllowed(models.getPaintMaterial25GrossAllowed());
        conclusionAssessmentPro.setPaintMaterial25Depreciation(models.getPaintMaterial25Depreciation());
        conclusionAssessmentPro.setPaintMaterial25ActualAllowed(models.getPaintMaterial25ActualAllowed());
        conclusionAssessmentPro.setPaintMaterial25Assessed(models.getPaintMaterial25Assessed());

        conclusionAssessmentPro.setAccidentalLabourGrossAllowed(models.getAccidentalLabourGrossAllowed());
        conclusionAssessmentPro.setAccidentalLabourDepreciation(models.getAccidentalLabourDepreciation());
        conclusionAssessmentPro.setAccidentalLabourActualAllowed(models.getAccidentalLabourActualAllowed());
        conclusionAssessmentPro.setAccidentalLabourAssessed(models.getAccidentalLabourAssessed());

        conclusionAssessmentPro.setPaintLabour75GrossAllowed(models.getPaintLabour75GrossAllowed());
        conclusionAssessmentPro.setPaintLabour75Depreciation(models.getPaintLabour75Depreciation());
        conclusionAssessmentPro.setPaintLabour75ActualAllowed(models.getPaintLabour75ActualAllowed());
        conclusionAssessmentPro.setPaintLabour75Assessed(models.getPaintLabour75Assessed());

        conclusionAssessmentPro.setTowingChargesFinal(models.getTowingChargesFinal());
        conclusionAssessmentPro.setImt23Deduction(models.getImt23Deduction());
        conclusionAssessmentPro.setSalvageCharges(models.getSalvageCharges());
        conclusionAssessmentPro.setSubTotal(models.getSubTotal());
        conclusionAssessmentPro.setAverageClause(models.getAverageClause());
        conclusionAssessmentPro.setCompulsoryExcess(models.getCompulsoryExcess());
        conclusionAssessmentPro.setVoluntaryImposedExcess(models.getVoluntaryImposedExcess());

        conclusionAssessmentPro.setSurveyInspectionDetails(models.getSurveyInspectionDetails());
        conclusionAssessmentPro.setNotes(models.getNotes());
        conclusionAssessmentPro.setConcludingRemark(models.getConcludingRemark());
        
        conclusionAssessmentPro.setObservation(models.getObservation());
        conclusionAssessmentPro.setCashlessStatus(models.getCashlessStatus());
        conclusionAssessmentPro.setNetLossAssessed(models.getNetLossAssessed());
        conclusionAssessmentPro.setAssessmentpercent(models.getAssessmentpercent());
        conclusionAssessmentPro.setMetalPartPercent(models.getMetalPartPercent());
        conclusionAssessmentPro.setGrossLossAssesedAssessed(models.getGrossLossAssesedAssessed());
        conclusionAssessmentPro.setGrossLossAssesedAppliedGST(models.getGrossLossAssesedAppliedGST());
        conclusionAssessmentPro.setGrossLossAssesedActualAllowed(models.getGrossLossAssesedActualAllowed());
        conclusionAssessmentPro.setGrossLossAssesedDepreciation(models.getGrossLossAssesedDepreciation());
        conclusionAssessmentPro.setGrossLossAssesedGrossAllowed(models.getGrossLossAssesedGrossAllowed());
        
        

        // save to DB
        conclusionAssessmentProRepo.save(conclusionAssessmentPro);

        response.setHttpStatus("SUCCESS");
        response.setMessage("Conclusion Assessment saved successfully.");
        response.setData(conclusionAssessmentPro);
        try {
			
    		UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
    				.findByInsuranceClaimIdAndCompanyGenId(models.getInsurenceGenId(), models.getCompanyGenId());
    		userSurveyorBasicDetails.setUpdateDate(new Timestamp(System.currentTimeMillis()));

    		int currentStatus = userSurveyorBasicDetails.getCurrentStatus();
    		// Change -- Aman -- Start --- 16-09-2025
			if (currentStatus == CommonConstants.APPROVED_STATUS) {
	    		userSurveyorBasicDetails.setCurrentStatus(CommonConstants.COMMERCIAL_CONCLUSION_FILLED);
    			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
			}
//				Change -- Aman -- End --- 16-09-2025
//    		if (currentStatus == CommonConstants.APPROVED_STATUS
//    				) {
//    		userSurveyorBasicDetails.setCurrentStatus(CommonConstants.COMMERCIAL_CONCLUSION_FILLED);
//    			userSurveyorBasicDetailsRepo.save(userSurveyorBasicDetails);
//    		}
        	} catch (Exception e) {
    			// TODO: handle exception
    		}
    } catch (Exception e) {
        response.setHttpStatus("FAILED");
        response.setMessage("Error while saving Conclusion Assessment: " + e.getMessage());
    }
    return response;
}

@Override
public ResponseModel viewconclusionAssessment(String companyGenId, Long insurenceGenId) {
    ResponseModel response = new ResponseModel();
    try {
        ConclusionAssessmentPro conclusionAssessmentPro =
                conclusionAssessmentProRepo.findByCompanyGenIdAndInsurenceGenId(companyGenId, insurenceGenId);

        if (conclusionAssessmentPro == null) {
            response.setHttpStatus("FAILED");
            response.setMessage("No record found for the given CompanyGenId and InsuranceGenId.");
            response.setData(null);
        } else {
            response.setHttpStatus("SUCCESS");
            response.setMessage("Conclusion Assessment retrieved successfully.");
            response.setData(conclusionAssessmentPro);
        }

    } catch (Exception e) {
        response.setHttpStatus("FAILED");
        response.setMessage("Error while fetching Conclusion Assessment: " + e.getMessage());
        response.setData(null);
    }
    return response;
}
//
//
//@Override
//public ResponseModel StatusPhotoWizard(PhotoWizardModel model) {
//    ResponseModel response = new ResponseModel();
//    try {
//        List<FinalDocumentSubmission> checkStatus =
//                finalDocumentSubmissionRepo.findByCompanyGenIdAndInsurenceGenIdAndUploadType(
//                        model.getCompanyGenId(), model.getInsurenceGenId(), "photo");
//
//        if (checkStatus != null && !checkStatus.isEmpty()) {
//            Set<Long> inputDocIds = new HashSet<>(model.getDocumentId());
//
//            for (FinalDocumentSubmission submission : checkStatus) {
//                if (inputDocIds.contains(submission.getDocumentId())) {
//                    // ✅ Marked as saved
//                    submission.setSaveStatus(true);
//                 //   submission.setSaveStatusChangedAt(null); // clear timestamp
//                } else {
//                    // ✅ Marked as NOT saved
//                    submission.setSaveStatus(false);
//                     // set timestamp
//                }
//                submission.setSaveStatusChangedAt(LocalDateTime.now());
//            }
//
//            finalDocumentSubmissionRepo.saveAll(checkStatus);
//        }
//
//        response.setHttpStatus("SUCCESS");
//        response.setMessage("Status updated successfully");
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        response.setHttpStatus("ERROR");
//        response.setMessage("Something went wrong: " + e.getMessage());
//    }
//
//    return response;
//}
@Override
public ResponseModel StatusPhotoWizard(PhotoWizardModel model) {
    ResponseModel response = new ResponseModel();
    try {
        List<FinalDocumentSubmission> checkStatus =
            finalDocumentSubmissionRepo.findByCompanyGenIdAndInsurenceGenIdAndUploadType(
                model.getCompanyGenId(), model.getInsurenceGenId(), "photo");

        if (checkStatus != null && !checkStatus.isEmpty()) {
            Set<Long> inputDocIds = new HashSet<>();
            // To avoid NullPointerException, check if model.getDocuments() is not null
            if (model.getDocuments() != null) {
                for (PhotoWizardModel.DocumentInfo docInfo : model.getDocuments()) {
                    inputDocIds.add(docInfo.getDocumentId());
                }
            }

            for (FinalDocumentSubmission submission : checkStatus) {
                if (inputDocIds.contains(submission.getDocumentId())) {
                    submission.setSaveStatus(true);
                    submission.setSaveStatusChangedAt(LocalDateTime.now());

                    // Find matching documentInfo to update page and slot
                    if (model.getDocuments() != null) {
                        for (PhotoWizardModel.DocumentInfo docInfo : model.getDocuments()) {
                            if (docInfo.getDocumentId().equals(submission.getDocumentId())) {
                                submission.setPage(docInfo.getPage());
                                submission.setSlot(docInfo.getSlot());
                                break;
                            }
                        }
                    }
                } else {
                    submission.setSaveStatus(false);
                    submission.setSaveStatusChangedAt(LocalDateTime.now());

                    // Optionally clear page and slot for non-matching docs
                    submission.setPage(null);
                    submission.setSlot(null);
                }
            }

            finalDocumentSubmissionRepo.saveAll(checkStatus);

            response.setHttpStatus("SUCCESS");
            response.setMessage("Status, page and slot updated successfully");
            response.setData(null);
            response.setName(null);
        } else {
            response.setHttpStatus("ERROR");
            response.setMessage("No matching documents found");
            response.setData(null);
            response.setName(null);
        }

    } catch (Exception e) {
        e.printStackTrace();
        response.setHttpStatus("ERROR");
        response.setMessage("Something went wrong: " + e.getMessage());
        response.setData(null);
        response.setName(null);
    }
    return response;
}


@Scheduled(cron = "0 0 0 * * ?") //  Roz subah 12 baje chalega
public void deleteExpiredUnsavedDocuments() {
    LocalDateTime threshold = LocalDateTime.now().minusDays(removePhotoDays); //  30 din purane
    int count = finalDocumentSubmissionRepo.deleteAllBySaveStatusFalseAndSaveStatusChangedAtBefore(threshold);
    System.out.println("🗑️ Auto-cleanup: Deleted " + count + " old unsaved documents.");
}

}
