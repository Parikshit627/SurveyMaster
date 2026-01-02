package com.insuretech.survey.serviceImpl;

import java.io.ByteArrayInputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.helper.DmsServer;
import com.insuretech.survey.helper.FileSystem;
import com.insuretech.survey.model.ApacheModel;
import com.insuretech.survey.model.JasperReportDataModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.JasperReportService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class JasperReportServiceImpl extends AbstractMasterRepository implements JasperReportService {

	Logger log = LoggerFactory.getLogger(JasperReportServiceImpl.class);

	@Value("${REPORT}")
	private String reportPath;

	@Value("${COM_REPORT}")
	private String comReportPath;
	
	@Value("${COM_REPORT_SPOT_REPORT}")
	private String comSpotReportPath;
	
	@Value("${TITLE4}")
	private String titlePath4;

	@Value("${TITLE3}")
	private String titlePath;

	@Value("${SPORT_DETAILS_BAND}")
	private String sportDetailBandPath;

	@Value("${FINAL_DETAILS_BAND}")
	private String finalDetailBandPath;

	@Value("${DMS_SAVE_URL}")
	private String dmsSaveUrl;

	@Value("${RI_REPORT}")
	private String riReport;

	@Value("${RI_TITLE}")
	private String riTitle;

	@Value("${SCRUTINY_SHEET}")
	private String scrutinySheet;

	@Value("${BILL_REPORT}")
	private String bill_report;

	@Value("${ASSESSMENT_REPORT}")
	private String assessment_report;

	@Value("${COM_ASSESSMENT_REPORT}")
	private String com_assessment_report;
	
	@Autowired
	DmsServer dmsServer;

	@Autowired
	FileSystem fileSystem;

	@Override
	public ResponseModel generatesSpotOrFinalReport(Long insurenceGenId, String companyGenId) {
		String methodName = "generatesSpotOrFinalReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		InputStream logoInputStream = null;
		try {
			log.info("Request : Finding  All Details By companyGenId , insurenceGenId  " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapList = jasperDataRepo.getJasperBeanData(insurenceGenId, companyGenId);
			if (mapList != null && !mapList.isEmpty()) {
				JasperReportDataModel jasperBeanData = mapData(mapList.get(0));
				log.info("Request : Generating Report for companyGenId , insurenceGenId, userId  " + "companyGenId : "
						+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());

				// find observation
				List<String> observationList = observationsRepo.findDesByConclusionId((Long) mapList.get(0).get("conclusion_id"));
				if (observationList != null && !observationList.isEmpty()) {
					String observation_desc = IntStream.range(0, observationList.size())
							.mapToObj(i -> (i + 1) + ". " + observationList.get(i)).collect(Collectors.joining("\n"));
					jasperBeanData.setObservation_desc(observation_desc);
				}

				List<String> notesList = notesRepo.findDesByConclusionId((Long) mapList.get(0).get("conclusion_id"));
				if (notesList != null && !notesList.isEmpty()) {
					String note_desc = IntStream.range(0, notesList.size())
							.mapToObj(i -> (i + 1) + ". " + notesList.get(i)).collect(Collectors.joining("\n"));
					jasperBeanData.setNote_desc(note_desc);
				}

				try {
					if (jasperBeanData.getSignuuid() != null && !jasperBeanData.getSignuuid().trim().equals("")) {
                        String aab = jasperBeanData.getReference_no().split("/")[1];
						ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(jasperBeanData.getSignuuid(),
								                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
						if (dmsResponse != null) {
							log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name"
									+ methodName + " Class : " + this.getClass());
							ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

							byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
							signInputStream = new ByteArrayInputStream(decodedBytes);
						}
					}

				} catch (Exception e) {
					log.info("Respond :unsuccessfully fecth sign byteStream form DMS" + " Method Name" + methodName
							+ " Class : " + this.getClass());
				}
				
				try {
					if (mapList.get(0).get("logouuid") != null
							&& !mapList.get(0).get("logouuid").toString().trim().equals("")) {
						String aab = mapList.get(0).get("reference_no").toString().split("/")[1];
						ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapList.get(0).get("logouuid").toString(),
                                aab, CommonConstants.DOC_TYPE_FOLDER_LOGO);
						if (dmsResponse != null) {
							log.info("Respond :successfully fetch logo byteStream from DMS" + " Method Name"
									+ methodName + " Class : " + this.getClass());
							ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

							byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
							logoInputStream = new ByteArrayInputStream(decodedBytes);
						}
					}
				}catch(Exception e) {
					log.info("Respond :unsuccessfully fecth Logo byteStream form DMS" + " Method Name" + methodName
							+ " Class : " + this.getClass());
				}

				// Load jrxml file
				InputStream inputStreamReport = getClass().getResourceAsStream(reportPath);
				InputStream inputStreamTitle = getClass().getResourceAsStream(titlePath4);
				InputStream inputStreamDB2 = null;

//				jasperBeanData.setSurvey_type("final");
				if (jasperBeanData.getSurvey_type().equalsIgnoreCase("spot")) {
					inputStreamDB2 = getClass().getResourceAsStream(sportDetailBandPath);
				} else if (jasperBeanData.getSurvey_type().equalsIgnoreCase("final")) {
					inputStreamDB2 = getClass().getResourceAsStream(finalDetailBandPath);
				} else {
					log.info(
							"Respond : Survey Type missing or invalid in db --  companyGenId , insurenceGenId, userId  "
									+ "companyGenId : " + companyGenId + ", insurenceGenId : " + insurenceGenId
									+ " Method Name" + methodName + " Class : " + this.getClass());
					responseModel.setHttpStatus(HttpStatus.NOT_FOUND);
					responseModel.setMessage("Survey Type missing or invalid in db");
				}

				// Compile to .jasper
				JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);
				JasperReport jasperTitle = JasperCompileManager.compileReport(inputStreamTitle);
				JasperReport jasperDetailBand2 = JasperCompileManager.compileReport(inputStreamDB2);

//				jasperBeanData.setSurvey_type("final");

				// Set data source
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(jasperBeanData));
				JRBeanCollectionDataSource tableDataSource = null;
				List<Map<String, Object>> tableList = damageDetailsRepo
						.findPartNameAndDescByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);

				if (tableList != null && !tableList.isEmpty()) {
					tableDataSource = new JRBeanCollectionDataSource(tableList);
				}

				// Add parameters
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("titleParam", jasperTitle);
				parameters.put("detailBand2", jasperDetailBand2);
				parameters.put("tableDatasource", tableDataSource);
				parameters.put("sign", signInputStream);
				parameters.put("logo", logoInputStream);

				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

				byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);

				ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData, jasperBeanData.getReference_no(),
						"report");
				if (dmsResponse != null) {
					log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
							+ this.getClass());
					ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
					List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
							.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId,
									jasperBeanData.getSurvey_type().trim() + "_report");
					FinalDocumentSubmission doc = new FinalDocumentSubmission();
					if (docList == null || docList.isEmpty()) {
						doc = new FinalDocumentSubmission();
					} else {
						doc = docList.get(0);
					}
					doc.setDocName(jasperBeanData.getSurvey_type().trim() + "_report");
					doc.setDocType("pdf");
					doc.setDocUuid(apacheModel.getUuid());
					doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
					doc.setVehicleNumber(jasperBeanData.getReg_no());
					doc.setCompanyGenId(companyGenId);
					doc.setInsurenceGenId(insurenceGenId);
					doc.setReferenceNo(jasperBeanData.getReference_no());

					FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
					log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
							+ " Class : " + this.getClass());

					responseModel.setHttpStatus(HttpStatus.OK);
					responseModel.setMessage("Report Generated successfully");
					responseModel.setData(savedDoc);
				} else {
					log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
							+ this.getClass());
				}

			} else {
				log.info("Respond : No data found companyGenId , insurenceGenId, userId  " + "companyGenId : "
						+ companyGenId + ", insurenceGenId : " + insurenceGenId + " Method Name" + methodName
						+ " Class : " + this.getClass());
				responseModel.setHttpStatus(HttpStatus.NOT_FOUND);
				responseModel.setMessage("No data found for the given parameters");
			}
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the Jasper report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;
	}

	public JasperReportDataModel mapData(Map<String, Object> map) {
		String methodName = "mapData";
		log.info("Request :  Mapping data from result set to JasperReportDataModel " + " Method Name" + methodName
				+ " Class : " + this.getClass());
		JasperReportDataModel model = new JasperReportDataModel();
		try {
			model.setTitle_license_number((String) map.get("title_license_number"));
			model.setTitle_license_validity((java.sql.Date) map.get("title_license_validity"));
			model.setTitle_email_id((String) map.get("title_email_id"));
			model.setTitle_phone_no((Long) map.get("title_phone_no"));

			// policy_details (a)
			model.setInsurer((String) map.get("insurer"));
			model.setDeputing_address((String) map.get("deputing_address"));
			model.setDeputation_date((java.sql.Timestamp) map.get("deputation_date"));
			model.setDeputing_office_code((String) map.get("deputing_office_code"));
			model.setPolicy_no((String) map.get("policy_no"));
			model.setPolicy_to((String) map.get("policy_to"));
			model.setPolicy_from((String) map.get("policy_from"));
			model.setNcb((String) map.get("ncb"));
			model.setNil_dep((String) map.get("nil_dep"));
			model.setBreak_in((String) map.get("break_in"));
			model.setIdv((String) map.get("idv"));
			model.setClaim_sr((String) map.get("claim_sr"));
			model.setInsured_name((String) map.get("insured_name"));
			model.setPolicy_address((String) map.get("policy_address"));
			model.setHpa((String) map.get("hpa"));
			model.setInsurance_contact((String) map.get("insurance_contact"));
			model.setInsurance_comment((String) map.get("insurance_comment"));
			model.setClaim_no((String) map.get("claim_no"));
			model.setOther_ref_no((String) map.get("other_ref_no"));

			// user_surveyor_basic_details (b)
			model.setDate_of_intimation((java.sql.Date) map.get("date_of_intimation"));
			model.setSurvey_type((String) map.get("survey_type"));
			model.setAddress((String) map.get("address"));
			model.setUnderwriting_office_code((String) map.get("underwriting_office_code"));
			model.setUnderwriting_office_name((String) map.get("underwriting_office_name"));
			model.setUnderwriting_office_address((String) map.get("underwriting_office_address"));

			// driver_particulars_details (d)
			model.setEndorsement((String) map.get("endorsement"));
			model.setDriver_name((String) map.get("driver_name"));
			model.setDriver_address((String) map.get("driver_address"));
			model.setDl_no((String) map.get("dl_no"));
			model.setLicence_type((String) map.get("licence_type"));
			model.setIssuing_authority((String) map.get("issuing_authority"));
			model.setDob((java.sql.Timestamp) map.get("dob"));
			model.setIssued_on((java.sql.Timestamp) map.get("issued_on"));
			model.setValid_upto_nt((java.sql.Timestamp) map.get("valid_upto_nt"));
			model.setValid_upto_tv((java.sql.Timestamp) map.get("valid_upto_tv"));
			model.setReg_no((String) map.get("reg_no"));
			model.setReg_owner((String) map.get("reg_owner"));
			model.setMake_variant((String) map.get("make_variant"));
			model.setColour((String) map.get("colour"));
			model.setBody_type((String) map.get("body_type"));
			model.setChassis_no((String) map.get("chassis_no"));
			model.setMotor_no((String) map.get("motor_no"));
			model.setCubic_cap((String) map.get("cubic_cap"));
			model.setOdometer((String) map.get("odometer"));
			model.setLaden_wt((String) map.get("laden_wt"));
			model.setUnladen_wt((String) map.get("unladen_wt"));
			model.setPre_accident((String) map.get("pre_accident"));
			model.setRemark((String) map.get("remark"));
			model.setVehicle_type((String) map.get("vehicle_type"));
			model.setOwner_sr((String) map.get("owner_sr"));
			model.setFuel((String) map.get("fuel"));
			model.setSeating_capacity((String) map.get("seating_capacity"));
			model.setArea_of_operation((String) map.get("area_of_operation"));
			model.setPucc((java.sql.Timestamp) map.get("pucc"));
			model.setTax_upto((java.sql.Timestamp) map.get("tax_upto"));
			model.setFitness_upto((java.sql.Timestamp) map.get("fitness_upto"));
			model.setPermit_validity((java.sql.Timestamp) map.get("permit_validity"));
			model.setPermit_authorization((java.sql.Timestamp) map.get("permit_authorization"));
			model.setDor((String) map.get("dor"));
			model.setDate_of_loss((java.sql.Timestamp) map.get("date_of_loss"));
			model.setCause_of_loss((String) map.get("cause_of_loss"));
			model.setReported_to((String) map.get("reported_to"));
			model.setInjury((String) map.get("injury"));
			model.setLocation((String) map.get("location"));
			model.setSpot_survey((String) map.get("spot_survey"));
			model.setInsurance_comment((String) map.get("insurance_comment"));
			model.setClaim_no((String) map.get("claim_no"));
			model.setOther_ref_no((String) map.get("other_ref_no"));
			model.setInsurance_contact((String) map.get("insurance_contact"));
			model.setConclusion_remark((String) map.get("conclusion_remark"));
			model.setSurveyor((String) map.get("surveyor"));
			model.setReference_no((String) map.get("reference_no"));
			model.setVechicle_shifted_add((String) map.get("vechicle_shifted_add"));
			model.setWorkshop((String) map.get("workshop"));
			model.setCashless((String) map.get("cashless"));
			model.setVechicle_shifted_remark((String) map.get("vechicle_shifted_remark"));
			model.setEstimated((String) map.get("estimated"));
			model.setVechicle_shifted_date((java.sql.Timestamp) map.get("vechicle_shifted_date"));
			model.setCompulsory_clause((String) map.get("compulsory_clause"));
			model.setSalvage_charges((String) map.get("salvage_charges"));
			model.setAverage_clause((String) map.get("average_clause"));
			model.setOther_deductibles((String) map.get("other_deductibles"));
			model.setAssess_cashless((Boolean) map.get("assess_cashless"));
			model.setLessassessment((String) map.get("lessassessment"));
			model.setLess_metal_parts((String) map.get("less_metal_parts"));
			model.setParts_allowed_amt((String) map.get("parts_allowed_amt"));
			model.setParts_allowed_dep((String) map.get("parts_allowed_dep"));
			model.setParts_allowed_gst((String) map.get("parts_allowed_gst"));
			model.setPaint_amt((String) map.get("paint_amt"));
			model.setTotal_labour_amount((String) map.get("total_labour_amount"));
			model.setTotal_labourgst((String) map.get("total_labourgst"));
			model.setTowing_amt((String) map.get("towing_amt"));
			model.setTowinggst((String) map.get("towinggst"));
			model.setTitle_address((String) map.get("title_address"));
			model.setInsurer_abbreviation((String) map.get("insurer_abbreviation"));
			model.setFinal_conclusion_remark((String) map.get("final_conclusion_remark"));
			model.setObservation_desc((String) map.get("observation_desc"));
			model.setFinalObservationsandFinding((String) map.get("final_observationsand_finding"));
			model.setNote_desc((String) map.get("note_desc"));
			model.setConclusion_submit_date((java.sql.Timestamp) map.get("conclusion_submit_date"));
			model.setFinal_submit_date((java.sql.Timestamp) map.get("final_submit_date"));
			model.setFinal_less_metal_parts((String) map.get("final_less_metal_parts"));
			model.setFinal_lessassessment((String) map.get("final_lessassessment"));
			model.setFinal_salvage_charges((String) map.get("final_salvage_charges"));
			model.setFinal_average_clause((String) map.get("final_average_clause"));
			model.setFinal_compulsory_clause((String) map.get("final_compulsory_clause"));
			model.setFinal_other_deductibles((String) map.get("final_other_deductibles"));
			model.setLabour_part25percent_tax((String) map.get("labour_part25percent_tax"));
			model.setGross_loss_amount((String) map.get("gross_loss_amount"));
			model.setGross_loss_dep((String) map.get("gross_loss_dep"));
			model.setGross_loss_gst((String) map.get("gross_loss_gst"));
			model.setNet_loss_value((String) map.get("net_loss_value"));
			model.setTime_of_loss((String) map.get("time_of_loss"));
			model.setCompany((String) map.get("company"));
			model.setMfg_year((Long) map.get("mfg_year"));
			model.setModel((String) map.get("model"));
			model.setThird_party_policy((String) map.get("third_party_policy"));
			model.setPrevious_policy((String) map.get("previous_policy"));
			model.setSignuuid((String) map.get("signuuid"));
			model.setFinal_asassess_cashless((Boolean) map.get("final_asassess_cashless"));
			model.setDealer_type((String) map.get("dealer_type"));
			model.setFull_name((String) map.get("full_name"));
			model.setBill_date((String) map.get("bill_date"));
			model.setCurrent_status((Integer) map.get("current_status"));

			model.setTitle_gst_no((String) map.get("title_gst_no"));
			model.setTitle_pan_card((String) map.get("title_pan_card"));
			model.setLogouuid((String) map.get("logouuid"));
			model.setRemarks_loss((String) map.get("remarks_loss"));

			log.info("Respond :  Data Mapped from result set to JasperReportDataModel successfully " + " Method Name"
					+ methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("Respond : Error occured while  Data Mapping from result set to JasperReportDataModel "
					+ e.getMessage() + " Method Name" + methodName + " Class : " + this.getClass());
			e.printStackTrace();
			return null;
		}

		return model;
	}

	@Override
	public ResponseModel generateRiReport(Long insurenceGenId, String companyGenId) {
		String methodName = "generateRiReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		InputStream logoInputStream = null;		
		UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
				.findByInsuranceClaimIdAndCompanyGenId(insurenceGenId, companyGenId);
		try {
			log.info("Request : Finding  All Details By companyGenId , insurenceGenId, userId  " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapListBean = jasperDataRepo.getRiBeanData(insurenceGenId, companyGenId);

			// Load jrxml file
			InputStream inputStreamReport = getClass().getResourceAsStream(riReport);
			InputStream inputStreamTitle = getClass().getResourceAsStream(titlePath4);

			if (inputStreamReport == null || inputStreamTitle == null) {
				throw new FileNotFoundException("Jasper report template file not found");
			}
			// Compile to .jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);
			JasperReport jasperTitle = JasperCompileManager.compileReport(inputStreamTitle);

			try {
				if (mapListBean.get(0).get("signuuid") != null
						&& !mapListBean.get(0).get("signuuid").toString().trim().equals("")) {
					     String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
						ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("signuuid").toString(),
								                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name" + methodName
								+ " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						signInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}
				
				if (mapListBean.get(0).get("logouuid") != null
						&& !mapListBean.get(0).get("logouuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("logouuid").toString(),
                            aab, CommonConstants.DOC_TYPE_FOLDER_LOGO);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch logo byteStream from DMS" + " Method Name"
								+ methodName + " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						logoInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}

			} catch (Exception e) {
				log.info("Respond :unsuccessfully fecth sign byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}
			
			try {
				if (mapListBean.get(0).get("logouuid") != null
						&& !mapListBean.get(0).get("logouuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("logouuid").toString(),
                            aab, CommonConstants.DOC_TYPE_FOLDER_LOGO);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch logo byteStream from DMS" + " Method Name"
								+ methodName + " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						logoInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}
			}catch(Exception e) {
				log.info("Respond :unsuccessfully fecth Logo byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}
			// Add parameters
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("titleParam", jasperTitle);
			parameters.put("sign", signInputStream);
			parameters.put("logo", logoInputStream);

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mapListBean);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
			ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData,
					mapListBean.get(0).get("reference_no").toString(), "report");
			if (dmsResponse != null) {
				log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
				ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
				List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
						.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId, "ri_report");
				FinalDocumentSubmission doc = new FinalDocumentSubmission();
				if (docList == null || docList.isEmpty()) {
					doc = new FinalDocumentSubmission();
				} else {
					doc = docList.get(0);
				}
				doc.setDocName("ri_report");
				doc.setDocType("pdf");
				doc.setDocUuid(apacheModel.getUuid());
				doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
//				doc.setVehicleNumber(mapListBean.get(0).get("Reg_no").toString());
				doc.setCompanyGenId(companyGenId);
				doc.setInsurenceGenId(insurenceGenId);
				doc.setReferenceNo(mapListBean.get(0).get("reference_no").toString());

				FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
				userSurveyorBasicDetails.setCurrentStatus(CommonConstants.REPORT_DOC_UPLOADED);

				log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
						+ " Class : " + this.getClass());

				responseModel.setHttpStatus(HttpStatus.OK);
				responseModel.setMessage("Report Generated successfully");
				responseModel.setData(savedDoc);
			} else {
				log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
			}
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the RI report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;
	}

	@Override
	public ResponseModel generateScrutinySheetReport(Long insurenceGenId, String companyGenId) {
		String methodName = "generateScrutinySheetReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		try {
			log.info("Request : Finding  All Details By companyGenId , insurenceGenId " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapListBean = jasperDataRepo.getScrutinySheetBeanData(insurenceGenId,
					companyGenId);
			log.info("mapListBean : {}", mapListBean.toString());

			// Load jrxml file
			InputStream inputStreamReport = getClass().getResourceAsStream(scrutinySheet);

			if (inputStreamReport == null) {
				throw new FileNotFoundException("Jasper report template file not found");
			}

			try {
				if (mapListBean.get(0).get("signuuid") != null
						&& !mapListBean.get(0).get("signuuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("signuuid").toString(),
							                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name" + methodName
								+ " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						signInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}

			} catch (Exception e) {
				log.info("Respond :unsuccessfully fecth sign byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}
			// Compile to .jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("sign", signInputStream);

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mapListBean);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
			ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData,
					mapListBean.get(0).get("reference_no").toString(), "report");
			if (dmsResponse != null) {
				log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
				ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
				List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
						.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId,
								"scrutiny_sheet_report");
				FinalDocumentSubmission doc = new FinalDocumentSubmission();
				if (docList == null || docList.isEmpty()) {
					doc = new FinalDocumentSubmission();
				} else {
					doc = docList.get(0);
				}
				doc.setDocName("scrutiny_sheet_report");
				doc.setDocType("pdf");
				doc.setDocUuid(apacheModel.getUuid());
				doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
//				doc.setVehicleNumber(mapListBean.get(0).get("Reg_no").toString());
				doc.setCompanyGenId(companyGenId);
				doc.setInsurenceGenId(insurenceGenId);
				doc.setReferenceNo(mapListBean.get(0).get("reference_no").toString());

				FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
				log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
						+ " Class : " + this.getClass());

				responseModel.setHttpStatus(HttpStatus.OK);
				responseModel.setMessage("Report Generated successfully");
				responseModel.setData(savedDoc);
			} else {
				log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
			}
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the Scrutiny Sheet report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;
	}

	@Override
	public ResponseModel generateBillReport(String reference_number, Long insurenceGenId, String companyGenId) {
		String methodName = "generateBillReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		InputStream logoInputStream = null;
		try {

			log.info("Request : Finding  All Details By companyGenId , insurenceGenId " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapListBean = jasperDataRepo.getBillBeanData(reference_number, companyGenId);
			log.info("mapListBean : {}", mapListBean.toString());

			if (mapListBean == null || mapListBean.isEmpty()) {

				log.info("Respond : No data found companyGenId , insurenceGenId, userId  " + "companyGenId : "
						+ companyGenId + ", insurenceGenId : " + insurenceGenId + " Method Name" + methodName
						+ " Class : " + this.getClass());

				responseModel.setHttpStatus(HttpStatus.NO_CONTENT);
				responseModel.setMessage("No Data Found");
				return responseModel;
			}

			List<Map<String, Object>> mapTableBean = jasperDataRepo
					.getBillBeanTableData((Long) mapListBean.get(0).get("id"));

			// Load jrxml file
			InputStream inputStreamReport = getClass().getResourceAsStream(bill_report);

			if (inputStreamReport == null) {
				throw new FileNotFoundException("Jasper report template file not found");
			}

			// Compile to .jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mapListBean);
			JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(mapTableBean);

			try {
				if (mapListBean.get(0).get("signuuid") != null
						&& !mapListBean.get(0).get("signuuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("signuuid").toString(),
							                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name" + methodName
								+ " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						signInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}

			} catch (Exception e) {
				log.info("Respond :unsuccessfully fecth sign byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}
			
			try {
				if (mapListBean.get(0).get("logouuid") != null
						&& !mapListBean.get(0).get("logouuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("logouuid").toString(),
                            aab, CommonConstants.DOC_TYPE_FOLDER_LOGO);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch logo byteStream from DMS" + " Method Name"
								+ methodName + " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						logoInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}
			}catch(Exception e) {
				log.info("Respond :unsuccessfully fecth Logo byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}
			
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("tableDatasource", tableDataSource);
			parameters.put("sign", signInputStream);
			parameters.put("logo", logoInputStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
			ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData,
					mapListBean.get(0).get("reference_no").toString(), "report");
			if (dmsResponse != null) {
				log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
				ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
				List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
						.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId, "bill_report");

				UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
						.findByInsuranceClaimIdAndCompanyGenId(insurenceGenId, companyGenId);

				FinalDocumentSubmission doc = new FinalDocumentSubmission();
				if (docList == null || docList.isEmpty()) {
					doc = new FinalDocumentSubmission();
				} else {
					doc = docList.get(0);
				}
				doc.setDocName("bill_report");
				doc.setDocType("pdf");
				doc.setDocUuid(apacheModel.getUuid());
				doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
//				doc.setVehicleNumber(mapListBean.get(0).get("Reg_no").toString());
				doc.setCompanyGenId(companyGenId);
				doc.setInsurenceGenId(insurenceGenId);
				doc.setReferenceNo(mapListBean.get(0).get("reference_no").toString());

				FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
				userSurveyorBasicDetails.setCurrentStatus(CommonConstants.SURVEY_FEE_GENERATED);

				log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
						+ " Class : " + this.getClass());

				responseModel.setHttpStatus(HttpStatus.OK);
				responseModel.setMessage("Report Generated successfully");
				responseModel.setData(savedDoc);
			} else {
				log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
			}
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the Bill report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;
	}

	@Override
	public ResponseModel generateDetailedAssessmentReport(Long insurenceGenId, String companyGenId) {
		String methodName = "generateDetailedAssessmentReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		try {
			log.info("Request : Finding  All Details By companyGenId , insurenceGenId " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapListBean = jasperDataRepo.getAssessmentData(insurenceGenId, companyGenId);
			log.info("mapListBean : {}", mapListBean.toString());

			List<Map<String, Object>> mapPartListBean = jasperDataRepo.getPartListTableData(insurenceGenId,
					companyGenId);
			List<Map<String, Object>> mapLabourListBean = jasperDataRepo.getLabourListTableData(insurenceGenId,
					companyGenId);

			// Load jrxml file
			InputStream inputStreamReport = getClass().getResourceAsStream(assessment_report);

			if (inputStreamReport == null) {
				throw new FileNotFoundException("Jasper report template file not found");
			}

			// Compile to .jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mapListBean);
			JRBeanCollectionDataSource tablePartDatasource = new JRBeanCollectionDataSource(mapPartListBean);
			JRBeanCollectionDataSource tableLabourDatasource = new JRBeanCollectionDataSource(mapLabourListBean);

			String regBefore = null;
			Date dateOfLoss = (Date) mapListBean.get(0).get("dateOfLoss");
			String dor = (String) mapListBean.get(0).get("dor");

			if (dateOfLoss != null && dor != null && !dor.trim().isEmpty()) {
				LocalDate dorDate;

				String trimmedDor = dor.trim();

				try {
					if (trimmedDor.matches("\\d{4}-\\d{2}-\\d{2}")) {
						dorDate = LocalDate.parse(trimmedDor, DateTimeFormatter.ISO_LOCAL_DATE);
					} else {
						dorDate = Instant.parse(trimmedDor).atZone(ZoneId.systemDefault()).toLocalDate();
					}
				} catch (Exception e) {
					throw new RuntimeException("Invalid date format: " + trimmedDor, e);
				}

				LocalDate dolDate;
				if (dateOfLoss instanceof java.sql.Date) {
					dolDate = ((java.sql.Date) dateOfLoss).toLocalDate();
				} else {
					dolDate = dateOfLoss.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}

				Period period = Period.between(dorDate, dolDate);
				regBefore = period.getYears() + " years " + period.getMonths() + " months " + period.getDays()
						+ " days";
			}

			try {
				if (mapListBean.get(0).get("signuuid") != null
						&& !mapListBean.get(0).get("signuuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("signuuid").toString(),
							                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name" + methodName
								+ " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						signInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}

			} catch (Exception e) {
				log.info("Respond :unsuccessfully fecth sign byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("tablePartDatasource", tablePartDatasource);
			parameters.put("tableLabourDatasource", tableLabourDatasource);
			parameters.put("regBefore", regBefore);
			parameters.put("sign", signInputStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
//			 String base64Encoded = Base64.getEncoder().encodeToString(pdfData);

			ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData,
					mapListBean.get(0).get("reference_no").toString(), "report");
			if (dmsResponse != null) {
				log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
				ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
				List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
						.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId,
								"assessment_report");
				FinalDocumentSubmission doc = new FinalDocumentSubmission();
				if (docList == null || docList.isEmpty()) {
					doc = new FinalDocumentSubmission();
				} else {
					doc = docList.get(0);
				}
				doc.setDocName("assessment_report");
				doc.setDocType("pdf");
				doc.setDocUuid(apacheModel.getUuid());
				doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
//				doc.setVehicleNumber(mapListBean.get(0).get("Reg_no").toString());
				doc.setCompanyGenId(companyGenId);
				doc.setInsurenceGenId(insurenceGenId);
				doc.setReferenceNo(mapListBean.get(0).get("reference_no").toString());

				FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
				log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
						+ " Class : " + this.getClass());

				responseModel.setHttpStatus(HttpStatus.OK);
				responseModel.setMessage("Report Generated successfully");
				responseModel.setData(savedDoc);
			} else {
				log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
			}

//			responseModel.setHttpStatus(HttpStatus.OK);
//			responseModel.setMessage("Report Generated successfully");
//			responseModel.setData(savedDoc);
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the assessment report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;
	}

	@Override
	public ResponseModel generateCommericalDetailedAssessmentReport(Long insurenceGenId, String companyGenId) {
		String methodName = "generateCommericalDetailedAssessmentReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		try {
			log.info("Request : Finding  All Details By companyGenId , insurenceGenId " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapListBean = jasperDataRepo.getComAssessmentData(insurenceGenId, companyGenId);
			log.info("mapListBean : {}", mapListBean.toString());

			List<Map<String, Object>> mapPartListBean = jasperDataRepo.getComPartListTableData(insurenceGenId,
					companyGenId);
			List<Map<String, Object>> mapLabourListBean = jasperDataRepo.getComLabourListTableData(insurenceGenId,
					companyGenId);
			List<Map<String, Object>> mapImtListBean = jasperDataRepo.getComImtListTableData(insurenceGenId,
					companyGenId);

			// Load jrxml file
			InputStream inputStreamReport = getClass().getResourceAsStream(com_assessment_report);

			if (inputStreamReport == null) {
				throw new FileNotFoundException("Jasper report template file not found");
			}

			// Compile to .jasper
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mapListBean);
			JRBeanCollectionDataSource tablePartDatasource = new JRBeanCollectionDataSource(mapPartListBean);
			JRBeanCollectionDataSource tableLabourDatasource = new JRBeanCollectionDataSource(mapLabourListBean);
			JRBeanCollectionDataSource tableImtDatasource = new JRBeanCollectionDataSource(mapImtListBean);

			String regBefore = null;
			Date dateOfLoss = (Date) mapListBean.get(0).get("dateOfLoss");
			String dor = (String) mapListBean.get(0).get("dor");

			if (dateOfLoss != null && dor != null && !dor.trim().isEmpty()) {
				LocalDate dorDate;

				String trimmedDor = dor.trim();

				try {
					if (trimmedDor.matches("\\d{4}-\\d{2}-\\d{2}")) {
						dorDate = LocalDate.parse(trimmedDor, DateTimeFormatter.ISO_LOCAL_DATE);
					} else {
						dorDate = Instant.parse(trimmedDor).atZone(ZoneId.systemDefault()).toLocalDate();
					}
				} catch (Exception e) {
					throw new RuntimeException("Invalid date format: " + trimmedDor, e);
				}

				LocalDate dolDate;
				if (dateOfLoss instanceof java.sql.Date) {
					dolDate = ((java.sql.Date) dateOfLoss).toLocalDate();
				} else {
					dolDate = dateOfLoss.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}

				Period period = Period.between(dorDate, dolDate);
				regBefore = period.getYears() + " years " + period.getMonths() + " months " + period.getDays()
						+ " days";
			}

			try {
				if (mapListBean.get(0).get("signuuid") != null
						&& !mapListBean.get(0).get("signuuid").toString().trim().equals("")) {
					String aab = mapListBean.get(0).get("reference_no").toString().split("/")[1];
					ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapListBean.get(0).get("signuuid").toString(),
							                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
					if (dmsResponse != null) {
						log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name" + methodName
								+ " Class : " + this.getClass());
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

						byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
						signInputStream = new ByteArrayInputStream(decodedBytes);
					}
				}

			} catch (Exception e) {
				log.info("Respond :unsuccessfully fecth sign byteStream form DMS" + " Method Name" + methodName
						+ " Class : " + this.getClass());
			}

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("tablePartDatasource", tablePartDatasource);
			parameters.put("tableLabourDatasource", tableLabourDatasource);
			parameters.put("regBefore", regBefore);
			parameters.put("sign", signInputStream);
			parameters.put("tableImtDatasource", tableImtDatasource);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);
			String base64Encoded = Base64.getEncoder().encodeToString(pdfData);

			ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData,
					mapListBean.get(0).get("reference_no").toString(), "report");
			if (dmsResponse != null) {
				log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
				ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
				List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
						.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId,
								"commercial_assessment_report");
				FinalDocumentSubmission doc = new FinalDocumentSubmission();
				if (docList == null || docList.isEmpty()) {
					doc = new FinalDocumentSubmission();
				} else {
					doc = docList.get(0);
				}
				doc.setDocName("commercial_assessment_report");
				doc.setDocType("pdf");
				doc.setDocUuid(apacheModel.getUuid());
				doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
//				doc.setVehicleNumber(mapListBean.get(0).get("Reg_no").toString());
				doc.setCompanyGenId(companyGenId);
				doc.setInsurenceGenId(insurenceGenId);
				doc.setReferenceNo(mapListBean.get(0).get("reference_no").toString());

				FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
				log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
						+ " Class : " + this.getClass());

				responseModel.setHttpStatus(HttpStatus.OK);
				responseModel.setMessage("Report Generated successfully");
				responseModel.setData(savedDoc);
			} else {
				log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
						+ this.getClass());
			}

//			responseModel.setHttpStatus(HttpStatus.OK);
//			responseModel.setMessage("Report Generated successfully");
//			responseModel.setData(base64Encoded);
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the assessment report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;
	}

	@Override
	public ResponseModel generateComFinalReport(Long insurenceGenId, String companyGenId) {
		String methodName = "generateComFinalReport";
		ResponseModel responseModel = new ResponseModel();
		InputStream signInputStream = null;
		InputStream logoInputStream = null;
		try {
			log.info("Request : Finding  All Details By companyGenId , insurenceGenId  " + "companyGenId : "
					+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());
			List<Map<String, Object>> mapList = jasperDataRepo.getComReportData(insurenceGenId, companyGenId);
			if (mapList != null && !mapList.isEmpty()) {
//				JasperReportDataModel jasperBeanData = mapData(mapList.get(0));
				log.info("Request : Generating Report for companyGenId , insurenceGenId, userId  " + "companyGenId : "
						+ companyGenId + " Method Name" + methodName + " Class : " + this.getClass());

				 Map<String, Object> reportData = new HashMap<>(mapList.get(0));
 
				// find observation
				List<String> observationList = observationsRepo
						.findDesByConclusionId((Long) mapList.get(0).get("conclusion_id"));
				if (observationList != null && !observationList.isEmpty()) {
					String observation_desc = IntStream.range(0, observationList.size())
							.mapToObj(i -> (i+1)+". "+observationList.get(i)).collect(Collectors.joining("\n"));
//					jasperBeanData.setObservation_desc(observation_desc);
					reportData.put("observation_desc",observation_desc);
				}

				List<String> notesList = notesRepo.findDesByConclusionId((Long) mapList.get(0).get("conclusion_id"));
				if (notesList != null && !notesList.isEmpty()) {
					String note_desc = IntStream.range(0, notesList.size())
							.mapToObj(i -> (i+1)+". "+notesList.get(i)).collect(Collectors.joining("\n"));
//					jasperBeanData.setNote_desc(note_desc);
					reportData.put("note_desc",note_desc);
				}
				
				 mapList = List.of(reportData);

				try {
					if (mapList.get(0).get("signuuid") != null
							&& !mapList.get(0).get("signuuid").toString().trim().equals("")) {
						String aab = mapList.get(0).get("reference_no").toString().split("/")[1];
						ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapList.get(0).get("signuuid").toString(),
								                                             aab, CommonConstants.DOC_TYPE_FOLDER_SIGN);
						if (dmsResponse != null) {
							log.info("Respond :successfully fetch sign byteStream from DMS" + " Method Name"
									+ methodName + " Class : " + this.getClass());
							ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

							byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
							signInputStream = new ByteArrayInputStream(decodedBytes);
						}
					}

				} catch (Exception e) {
					log.info("Respond :unsuccessfully fetch sign byteStream form DMS" + " Method Name"
							+ methodName + " Class : " + this.getClass());
				}
				
				try {
					if (mapList.get(0).get("logouuid") != null
							&& !mapList.get(0).get("logouuid").toString().trim().equals("")) {
						String aab = mapList.get(0).get("reference_no").toString().split("/")[1];
						ResponseEntity<ApacheModel> dmsResponse = fileSystem.getFileDMS(mapList.get(0).get("logouuid").toString(),
                                aab, CommonConstants.DOC_TYPE_FOLDER_LOGO);
						if (dmsResponse != null) {
							log.info("Respond :successfully fetch logo byteStream from DMS" + " Method Name"
									+ methodName + " Class : " + this.getClass());
							ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();

							byte[] decodedBytes = Base64.getDecoder().decode(apacheModel.getBase64Data());
							logoInputStream = new ByteArrayInputStream(decodedBytes);
						}
					}
				}catch(Exception e) {
					log.info("Respond :unsuccessfully fecth Logo byteStream form DMS" + " Method Name" + methodName
							+ " Class : " + this.getClass());
				}

				// Load jrxml file
				InputStream inputStreamReport = null;
				InputStream inputStreamTitle = getClass().getResourceAsStream(titlePath4);
//				InputStream inputStreamDB2 = null;

				if (mapList.get(0).get("survey_type").toString().equalsIgnoreCase("spot")) {
					inputStreamReport = getClass().getResourceAsStream(comSpotReportPath);
				} else if (mapList.get(0).get("survey_type").toString().equalsIgnoreCase("final")) {
					inputStreamReport = getClass().getResourceAsStream(comReportPath);
				} else {
					log.info(
							"Respond : Survey Type missing or invalid in db --  companyGenId , insurenceGenId, userId  "
									+ "companyGenId : " + companyGenId + ", insurenceGenId : " + insurenceGenId
									 + " Method Name" + methodName + " Class : "
									+ this.getClass());
					responseModel.setHttpStatus(HttpStatus.NOT_FOUND);
					responseModel.setMessage("Survey Type missing or invalid in db");
					return responseModel;
				}

				// Compile to .jasper
				JasperReport jasperReport = JasperCompileManager.compileReport(inputStreamReport);
				JasperReport jasperTitle = JasperCompileManager.compileReport(inputStreamTitle);
//				JasperReport jasperDetailBand2 = JasperCompileManager.compileReport(inputStreamDB2);

//				jasperBeanData.setSurvey_type("final");

				// Set data source
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mapList);
				JRBeanCollectionDataSource tableDataSource = null;
				List<Map<String, Object>> tableList = damageDetailsRepo
						.findPartNameAndDescByInsurenceGenIdAndCompanyGenId(insurenceGenId, companyGenId);

				if (tableList != null && !tableList.isEmpty()) {
					tableDataSource = new JRBeanCollectionDataSource(tableList);
				}

				// Add parameters
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("titleParam", jasperTitle);
//				parameters.put("detailBand2", jasperDetailBand2);
				parameters.put("tableDatasource", tableDataSource);
				parameters.put("sign", signInputStream);
				parameters.put("logo", logoInputStream);

				// Fill the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

				byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);

				ResponseEntity<ApacheModel> dmsResponse = fileSystem.saveDMS(pdfData,
						mapList.get(0).get("reference_no").toString(), "report");
				if (dmsResponse != null) {
					log.info("Respond : Report saved in DMS successfully " + " Method Name" + methodName + " Class : "
							+ this.getClass());
					ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
					List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo
							.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId,
									mapList.get(0).get("survey_type").toString().trim() + "_report");
					FinalDocumentSubmission doc = new FinalDocumentSubmission();
					if (docList == null || docList.isEmpty()) {
						doc = new FinalDocumentSubmission();
					} else {
						doc = docList.get(0);
					}
					doc.setDocName(mapList.get(0).get("survey_type").toString().trim() + "_report");
					doc.setDocType("pdf");
					doc.setDocUuid(apacheModel.getUuid());
					doc.setDocUploadTime(new Timestamp(System.currentTimeMillis()));
					doc.setVehicleNumber(mapList.get(0).get("reg_no").toString());
					doc.setCompanyGenId(companyGenId);
					doc.setInsurenceGenId(insurenceGenId);
					doc.setReferenceNo(mapList.get(0).get("reference_no").toString());

					FinalDocumentSubmission savedDoc = finalDocumentSubmissionRepo.saveAndFlush(doc);
					log.info("Respond : report doc info saved in db successfully " + " Method Name" + methodName
							+ " Class : " + this.getClass());

					responseModel.setHttpStatus(HttpStatus.OK);
					responseModel.setMessage("Report Generated successfully");
					responseModel.setData(savedDoc);
				} else {
					log.info("Respond : Report saved in DMS unsuccessfully " + " Method Name" + methodName + " Class : "
							+ this.getClass());
				}

			} else {
				log.info("Respond : No data found companyGenId , insurenceGenId, userId  " + "companyGenId : "
						+ companyGenId + ", insurenceGenId : " + insurenceGenId + " Method Name" + methodName
						+ " Class : " + this.getClass());
				responseModel.setHttpStatus(HttpStatus.NOT_FOUND);
				responseModel.setMessage("No data found for the given parameters");
			}
		} catch (Exception e) {
			log.error("Respond : An error occurred while generating the Jasper report: ", e);
			responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setMessage("An error occurred while generating the Jasper report: " + " Method Name"
					+ methodName + " Class : " + this.getClass() + e.getMessage());
		}
		return responseModel;

	}

}
