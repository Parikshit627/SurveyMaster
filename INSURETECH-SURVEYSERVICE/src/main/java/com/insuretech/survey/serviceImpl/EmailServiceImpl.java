package com.insuretech.survey.serviceImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.EmailHistory;
import com.insuretech.survey.entity.EmailTemplate;
import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.MasterDocumentRequired;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.helper.DmsServer;
import com.insuretech.survey.model.ApacheModel;
import com.insuretech.survey.model.CommonMailModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl extends AbstractMasterRepository implements EmailService {

	private final ApplicationServiceImpl applicationServiceImpl;

	@Autowired
	private JavaMailSender mailSender;

	Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	DmsServer dmsServer;

	SimpleDateFormat dateFormate = new SimpleDateFormat("dd/MM/yyyy");

	EmailServiceImpl(ApplicationServiceImpl applicationServiceImpl) {
		this.applicationServiceImpl = applicationServiceImpl;
	}

	@Override
	public String sendSimpleEmail(List<String> toEmailList, String subject, String body) {
		String methodName = "sendSimpleEmail";
		String currect_email = "";
		String[] toCC= {};
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			String toEmail = toEmailList.get(0);
			if(toEmailList.size() > 1) {
				List<String> ccList = toEmailList.subList(1, toEmailList.size());
				toCC= ccList.toArray(new String[0]);
			}
			
			log.info("Request : sending email to Email Id : " + toEmail + " subject : " + subject + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			helper.setTo(toEmail);
			if (toCC != null && toCC.length != 0) {
				helper.setCc(toCC);
			}
			helper.setSubject(subject);
			helper.setText(body, true);

			mailSender.send(message);

			log.info("Respond : successfully send email to Email Id : " + toEmail + " subject : " + subject
					+ "  Method Name" + methodName + " Class : " + this.getClass());

			return "success";

		} catch (Exception e) {
			log.info("Respond : error occureed while sending  email " + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			e.printStackTrace();
		}
		return "failure";
	}

	public static String processTemplate(String template, Map<String, Object> values) {
		StringSubstitutor sub = new StringSubstitutor(values);
		return sub.replace(template);
	}

	@Override
	public ResponseModel sendEmail(CommonMailModel commonMailModel) {
		String methodName = "sendSimpleEmail";
		ResponseModel response = new ResponseModel();
		log.info("Request : sending  email ,comapnayGenId : " + commonMailModel.getCompanyGenId() + "  Method Name"
				+ methodName + " Class : " + this.getClass());
		try {
			log.info("Request : Finding email template ,templateId : " + commonMailModel.getTemplateId()
					+ "  Method Name" + methodName + " Class : " + this.getClass());
			if (commonMailModel.getTemplateId() == null || commonMailModel.getTemplateId().trim().equals("")) {
				log.info("Respond :Invalid Email template Id" + "  Method Name" + methodName + " Class : "
						+ this.getClass());
				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid Email template Id ");
				return response;
			}
			EmailTemplate emailTemplate = emailTemplateRepo.findByEmailTempId(commonMailModel.getTemplateId());
			if (emailTemplate == null) {
				log.info("Respond :No Data found in db by  template Id : " + commonMailModel.getTemplateId()
						+ "  Method Name" + methodName + " Class : " + this.getClass());
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No Data found in db by  template Id : " + commonMailModel.getTemplateId());
				return response;
			}
			String subject = processTemplate(emailTemplate.getSubject(), commonMailModel.getParam());
			String body = processTemplate(emailTemplate.getBody(), commonMailModel.getParam());
			String result = "";
			if (commonMailModel.isAttachment()) {
				result = sendEmailWithAttachments(commonMailModel.getEmailList(), subject, body,
						commonMailModel.getDocUUIDList());
			} else {
				result = sendSimpleEmail(commonMailModel.getEmailList(), subject, body);
			}

			if (result.equalsIgnoreCase("success")) {
				log.info("Respond : successfully send  email ,comapnayGenId : " + commonMailModel.getCompanyGenId()
						+ "  Method Name" + methodName + " Class : " + this.getClass());

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("success");
			} else {
				log.info("Respond : Unsuccessfull in sending  email ,comapnayGenId : "
						+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
				response.setHttpStatus(HttpStatus.CONFLICT);
				response.setMessage("failure");
			}

			commonMailModel.setTo(commonMailModel.getEmailList().get(0));
			commonMailModel.setCc(
					String.join(",", commonMailModel.getEmailList().subList(1, commonMailModel.getEmailList().size())));
			commonMailModel.setSubject(subject);
			commonMailModel.setBody(body);
			commonMailModel.setTemplateName(emailTemplate.getTemplateName());
			String emailHistoryResult = saveEmailHistory(commonMailModel, result);

		} catch (Exception e) {
			log.error("Respond : error occureed while sending  email  ,comapnayGenId :"
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("error : " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String sendEmailWithAttachments(List<String> toEmailList, String subject, String body,
			List<String> attachmentsUUID) {
		String methodName = "sendSimpleEmail";
		String[] toCC= {};
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			String toEmail = toEmailList.get(0);
			if(toEmailList.size() > 1) {
				List<String> ccList = toEmailList.subList(1, toEmailList.size());
				toCC= ccList.toArray(new String[0]);
			}

			log.info("Request: Sending email to Email Id: " + toEmail + " subject: " + subject + " Method Name: "
					+ methodName + " Class: " + this.getClass());

			helper.setTo(toEmail);

			if (toCC != null && toCC.length > 0) {
				helper.setCc(toCC);
			}

			helper.setSubject(subject);
			helper.setText(body, true);
			for (String docUUID : attachmentsUUID) {
				ResponseEntity<ApacheModel> dmsResponse = dmsServer.getFileDMS(docUUID);
				if (dmsResponse != null) {
					ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
					String attachmentName = "";
					if (apacheModel.getFileName() != null && !apacheModel.getFileName().trim().equals("")) {
						attachmentName = apacheModel.getFileName();
					} else {
						attachmentName = "Attachment_File.pdf";
					}
					byte[] attachment = null;
					if (apacheModel.getBase64Data() != null) {
						attachment = Base64.getDecoder().decode(apacheModel.getBase64Data());
					}
					if (attachment != null) {
						helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
						;
					}
				}
			}

			mailSender.send(message);

			log.info("Respond: Successfully sent email to Email Id: " + toEmail + " subject: " + subject
					+ " Method Name: " + methodName + " Class: " + this.getClass());

			return "success";

		} catch (Exception e) {
			log.info("Respond: Error occurred while sending email " + " subject: " + subject + " Method Name: "
					+ methodName + " Class: " + this.getClass());
			e.printStackTrace();
			return "failure";
		}

	}

	@Override
	public ResponseModel sendDocRequirementEmail(CommonMailModel commonMailModel) {
		String methodName = "sendDocRequirementEmail";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request: Sending email data for Documents requirement for insurenceGenId : "
					+ commonMailModel.getInsurenceGenId() + ", companyGenId :" + commonMailModel.getCompanyGenId()
					+ ", Method Name: " + methodName + " Class: " + this.getClass());

			if (commonMailModel != null) {

				Map<String, Object> param = new HashMap<>();
//				String doc = IntStream.range(0, docList.size()).mapToObj(i -> (i + 1) + ". " + docList.get(i))
//						.collect(Collectors.joining("<br>"));
//				param.put("docList", doc);

				UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
						.findByInsuranceClaimIdAndCompanyGenId(commonMailModel.getInsurenceGenId(),
								commonMailModel.getCompanyGenId());

				if (userSurveyorBasicDetails == null) {
					log.info("Respond: No Data Found  for insurenceGenId : " + commonMailModel.getInsurenceGenId()
							+ ", companyGenId :" + commonMailModel.getCompanyGenId() + ", Method Name: " + methodName
							+ " Class: " + this.getClass());
					response.setHttpStatus(HttpStatus.NOT_FOUND);
					response.setMessage("No Data Found  for insurenceGenId :" + commonMailModel.getInsurenceGenId()
							+ ", companyGenId :" + commonMailModel.getCompanyGenId());
					return response;
				}

				param.put("reference_no", userSurveyorBasicDetails.getReferenceNo());
				param.put("insurer_name", userSurveyorBasicDetails.getInsurerName());
				param.put("policy_no", userSurveyorBasicDetails.getPolicyNo());
				param.put("claim_no", userSurveyorBasicDetails.getClaimNo());
				param.put("asset", userSurveyorBasicDetails.getAsset());
				param.put("date_of_loss", dateFormate.format(userSurveyorBasicDetails.getDateOfloss()));
				param.put("survey_location_address", userSurveyorBasicDetails.getSurveyLocationAddress());
				param.put("surveyor", userSurveyorBasicDetails.getSurveyor());
				param.put("insured_name", userSurveyorBasicDetails.getInsuredName());

				commonMailModel.setInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
				commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
				commonMailModel.setTemplateId(CommonConstants.DOC_REQUIREMENT_EMAIL_TEMPLATE_ID);
				commonMailModel.setParam(param);
				commonMailModel.setAttachment(false);

				commonMailModel = fillEmailData(commonMailModel);

				response.setData(commonMailModel);
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("success");

				log.info("Request: successfull -- email data for Documents requirement for insurenceGenId : "
						+ commonMailModel.getInsurenceGenId() + ", companyGenId :" + commonMailModel.getCompanyGenId()
						+ ", Method Name: " + methodName + " Class: " + this.getClass());

			}

		} catch (Exception e) {
			log.info("Respond: Error occurred while sending email for Documents requirement for insurenceGenId : "
					+ commonMailModel.getInsurenceGenId() + ", companyGenId :" + commonMailModel.getCompanyGenId()
					+ ", Method Name: " + methodName + " Class: " + this.getClass());
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String saveEmailHistory(CommonMailModel commonMailModel, String status) {
		String methodName = "saveEmailHistory";
		try {
			log.info("Request: save email history for reference No : " + commonMailModel.getReferenceNo()
					+ ", companyGenId :" + commonMailModel.getCompanyGenId() + ", Method Name: " + methodName
					+ " Class: " + this.getClass());

			EmailHistory emailHistory = new EmailHistory();
			emailHistory.setEmailTo(commonMailModel.getTo());
			emailHistory.setCc(commonMailModel.getCc());
			emailHistory.setSubject(commonMailModel.getSubject());
			emailHistory.setBody(commonMailModel.getBody());
			emailHistory.setReferenceNo(commonMailModel.getReferenceNo());
			emailHistory.setCompanyGenId(commonMailModel.getCompanyGenId());
			emailHistory.setCreatedBy(commonMailModel.getCreatedBy());
			emailHistory.setCreatedDtm(new Timestamp(System.currentTimeMillis()));
			emailHistory.setStatus(status);
			emailHistory.setTemplateName(commonMailModel.getTemplateName());
			emailHistoryRepo.save(emailHistory);

			log.info("Respond: email history saved Successfully for reference No : " + commonMailModel.getReferenceNo()
					+ ", companyGenId :" + commonMailModel.getCompanyGenId() + ", Method Name: " + methodName
					+ " Class: " + this.getClass());

			return "success";

		} catch (Exception e) {
			log.info("Respond: Error occurred while ave email history for reference No : "
					+ commonMailModel.getReferenceNo() + ", companyGenId :" + commonMailModel.getCompanyGenId()
					+ ", Method Name: " + methodName + " Class: " + this.getClass());
			e.printStackTrace();
		}
		return "failure";
	}

	@Override
	public ResponseModel getEmailHistoryByReferenceNo(String referenceNo) {
		String methodName = "saveEmailHistory";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request: get email history for reference No : " + referenceNo + ", Method Name: " + methodName
					+ " Class: " + this.getClass());

			if (referenceNo == null || referenceNo.trim().equals("")) {
				log.info("Response : Invalid parameter reference No : " + referenceNo + ", Method Name: " + methodName
						+ " Class: " + this.getClass());
				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid parameter");
				return response;
			}

			List<EmailHistory> emailHistoryList = emailHistoryRepo.findByReferenceNo(referenceNo);

			if (emailHistoryList == null || emailHistoryList.isEmpty()) {
				log.info("Response : No data found  reference No : " + referenceNo + ", Method Name: " + methodName
						+ " Class: " + this.getClass());
				response.setHttpStatus(HttpStatus.NO_CONTENT);
				response.setMessage("No data found");
				return response;
			}

			response.setData(emailHistoryList);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("success");

			log.info("Response : data fetched successfully for reference No : " + referenceNo + ", Method Name: "
					+ methodName + " Class: " + this.getClass());

			return response;

		} catch (Exception e) {
			log.info("Respond: Error occurred while fetching email history for reference No : " + referenceNo
					+ ", Method Name: " + methodName + " Class: " + this.getClass());
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public ResponseModel sendDirectEmail(CommonMailModel commonMailModel) {
		String methodName = "sendDirectEmail";
		String[] toCC= {};
		ResponseModel response = new ResponseModel();
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
           if(commonMailModel.getTo()==null || commonMailModel.getTo().trim().equals("")) {
        	   throw new IllegalArgumentException("to address are invalid");
           }
			String[] toEmail = commonMailModel.getTo().split("\\s*,\\s*");
			
			if(commonMailModel.getCc()!=null && !commonMailModel.getCc().trim().equals("")) {
				toCC = commonMailModel.getCc().split("\\s*,\\s*");
	           }
			
			log.info("Request: Sending direct email to Email Id: " + toEmail + " subject: "
					+ commonMailModel.getSubject() + " Method Name: " + methodName + " Class: " + this.getClass());

			helper.setTo(toEmail);

			if (toCC != null && toCC.length > 0) {
				helper.setCc(toCC);
			}

			helper.setSubject(commonMailModel.getSubject());
			helper.setText(commonMailModel.getBody(), true);
			if (commonMailModel.getDocUUIDList() != null && !commonMailModel.getDocUUIDList().isEmpty()) {
				for (String docUUID : commonMailModel.getDocUUIDList()) {
					ResponseEntity<ApacheModel> dmsResponse = dmsServer.getFileDMS(docUUID);
					if (dmsResponse != null) {
						ApacheModel apacheModel = (ApacheModel) dmsResponse.getBody();
						String attachmentName = "";
						if (apacheModel.getFileName() != null && !apacheModel.getFileName().trim().equals("")) {
							attachmentName = apacheModel.getFileName();
						} else {
							attachmentName = "Attachment_File.pdf";
						}
						byte[] attachment = null;
						if (apacheModel.getBase64Data() != null) {
							attachment = Base64.getDecoder().decode(apacheModel.getBase64Data());
						}
						if (attachment != null) {
							helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
							;
						}
					}
				}
			}

			mailSender.send(message);

			log.info("Respond: Successfully sent direct email to Email Id: " + toEmail + " subject: "
					+ commonMailModel.getSubject() + " Method Name: " + methodName + " Class: " + this.getClass());

			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("success");

		} catch (Exception e) {
			log.info("Respond: Error occurred while sending direct email " + " subject: " + commonMailModel.getSubject()
					+ " Method Name: " + methodName + " Class: " + this.getClass());
			e.printStackTrace();

			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("failure");
		}

		saveEmailHistory(commonMailModel, response.getMessage().toString());
		return response;
	}

	@Override
	public CommonMailModel fillEmailData(CommonMailModel commonMailModel) {
		String methodName = "fillEmailData";
		ResponseModel response = new ResponseModel();
		log.info("Request : fill Email Data ,comapnayGenId : " + commonMailModel.getCompanyGenId() + "  Method Name"
				+ methodName + " Class : " + this.getClass());
		try {
			log.info("Request : Finding email template ,templateId : " + commonMailModel.getTemplateId()
					+ "  Method Name" + methodName + " Class : " + this.getClass());
			if (commonMailModel.getTemplateId() == null || commonMailModel.getTemplateId().trim().equals("")) {
				log.info("Respond :Invalid Email template Id" + "  Method Name" + methodName + " Class : "
						+ this.getClass());
				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invalid Email template Id ");
				return commonMailModel;
			}
			EmailTemplate emailTemplate = emailTemplateRepo.findByEmailTempId(commonMailModel.getTemplateId());
			if (emailTemplate == null) {
				log.info("Respond :No Data found in db by  template Id : " + commonMailModel.getTemplateId()
						+ "  Method Name" + methodName + " Class : " + this.getClass());
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No Data found in db by  template Id : " + commonMailModel.getTemplateId());
				return commonMailModel;
			}
			String subject = processTemplate(emailTemplate.getSubject(), commonMailModel.getParam());
			String body = processTemplate(emailTemplate.getBody(), commonMailModel.getParam());

			commonMailModel.setSubject(subject);
			commonMailModel.setBody(body);
			commonMailModel.setTemplateName(emailTemplate.getTemplateName());

		} catch (Exception e) {
			log.error("Respond : error occureed while fill Email Data  ,comapnayGenId :"
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("error : " + e.getMessage());
			e.printStackTrace();
		}
		return commonMailModel;
	}

	@Override
	public ResponseModel sendFinalSubSurveyWizardEmail(CommonMailModel commonMailModel) {
		String methodName = "sendFinalSubSurveyWizardEmail";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Sending email for survey Wizard Final Submission  by email id: "
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : " + this.getClass());

			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimIdAndCompanyGenId(commonMailModel.getInsurenceGenId(),
							commonMailModel.getCompanyGenId());

			if (userSurveyorBasicDetails == null) {
				log.info("Respond: No Data Found  for insurenceGenId : " + commonMailModel.getInsurenceGenId()
						+ ", companyGenId :" + commonMailModel.getCompanyGenId() + ", Method Name: " + methodName
						+ " Class: " + this.getClass());
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No Data Found  for insurenceGenId :" + commonMailModel.getInsurenceGenId()
						+ ", companyGenId :" + commonMailModel.getCompanyGenId());
				return response;
			}
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
					commonMailModel.getCompanyGenId(),commonMailModel.getInsurenceGenId(),"final_report");
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
			commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
			commonMailModel.setTemplateId(CommonConstants.FINAL_SUBMISSION_EMAIL_TEMPLATE_ID);
			commonMailModel.setParam(param);
			commonMailModel.setAttachment(true);
			commonMailModel.setDocUUIDList(docUUIDList);

			commonMailModel = fillEmailData(commonMailModel);
			response.setData(commonMailModel);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("success");

		} catch (Exception e) {
			log.error("An error occurred while sending email " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("failure");
		}

		return response;
	}

	@Override
	public ResponseModel sendAssessmentEmail(CommonMailModel commonMailModel) {
		String methodName = "sendAssessmentEmail";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Sending email for survey Wizard Assessment  by email id: "
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName + " Class : " + this.getClass());

			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimIdAndCompanyGenId(commonMailModel.getInsurenceGenId(),
							commonMailModel.getCompanyGenId());

			if (userSurveyorBasicDetails == null) {
				log.info("Respond: No Data Found  for insurenceGenId : " + commonMailModel.getInsurenceGenId()
						+ ", companyGenId :" + commonMailModel.getCompanyGenId() + ", Method Name: " + methodName
						+ " Class: " + this.getClass());
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No Data Found  for insurenceGenId :" + commonMailModel.getInsurenceGenId()
						+ ", companyGenId :" + commonMailModel.getCompanyGenId());
				return response;
			}

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

			List<FinalDocumentSubmission> docList = finalDocumentSubmissionRepo.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(
					commonMailModel.getCompanyGenId(),commonMailModel.getInsurenceGenId(),"assessment_report");
			if(docList!=null && !docList.isEmpty()) {
				FinalDocumentSubmission finalDocumentSubmission=docList.get(0);
				docUUIDList.add(finalDocumentSubmission.getDocUuid().toString());
			}
//			if (docMapList != null && !docMapList.isEmpty()) {
//				for (Map<String, Object> map : docMapList) {
//					for (Map.Entry<String, Object> entry : map.entrySet()) {
//						if (entry.getValue() != null && entry.getValue().toString().equalsIgnoreCase("assessment_report")) {
//							Object docUuid = map.get("docUuid");
//							if (docUuid != null) {
//								docUUIDList.add(docUuid.toString());
//							}
//						}
//					}
//				}
//			}
			

			commonMailModel.setInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
			commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
			commonMailModel.setTemplateId(CommonConstants.ASSESSMENT_SUBMISSION_EMAIL_TEMPLATE_ID);
			commonMailModel.setParam(param);
			commonMailModel.setAttachment(true);
			commonMailModel.setDocUUIDList(docUUIDList);
			
			commonMailModel = fillEmailData(commonMailModel);
			response.setData(commonMailModel);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("success");

		} catch (Exception e) {
			log.error("An error occurred while sending email " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}

		return response;
	}

	@Override
	public ResponseModel sendIntimationEmail(CommonMailModel commonMailModel) {
		String methodName = "sendIntimationEmail";
		ResponseModel response = new ResponseModel();
		try {
			
			log.info("Request : Sending email for Intimation form by email id: "
					+ commonMailModel.getCompanyGenId() + "  Method Name" + methodName
					+ " Class : " + this.getClass());
			
			UserSurveyorBasicDetails userSurveyorBasicDetails = userSurveyorBasicDetailsRepo
					.findByInsuranceClaimIdAndCompanyGenId(commonMailModel.getInsurenceGenId(),
							commonMailModel.getCompanyGenId());

			if (userSurveyorBasicDetails == null) {
				log.info("Respond: No Data Found  for insurenceGenId : " + commonMailModel.getInsurenceGenId()
						+ ", companyGenId :" + commonMailModel.getCompanyGenId() + ", Method Name: " + methodName
						+ " Class: " + this.getClass());
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No Data Found  for insurenceGenId :" + commonMailModel.getInsurenceGenId()
						+ ", companyGenId :" + commonMailModel.getCompanyGenId());
				return response;
			}

			
			Map<String, Object> param = new HashMap<>();
			param.put("reference_no", userSurveyorBasicDetails.getReferenceNo());
			param.put("insurer_name", userSurveyorBasicDetails.getInsurerName());
			param.put("policy_no", userSurveyorBasicDetails.getPolicyNo());
			param.put("claim_no", userSurveyorBasicDetails.getClaimNo());
			param.put("asset", userSurveyorBasicDetails.getAsset());
			param.put("date_of_loss", dateFormate.format(userSurveyorBasicDetails.getDateOfloss()));
//			param.put("date_of_loss", userSurveyorBasicDetails.getDateOfloss().toString());
			param.put("survey_location_address", userSurveyorBasicDetails.getSurveyLocationAddress());
			param.put("surveyor", userSurveyorBasicDetails.getSurveyor());
			param.put("insured_name", userSurveyorBasicDetails.getInsuredName());

			
			commonMailModel.setInsurenceGenId(userSurveyorBasicDetails.getInsuranceClaimId());
			commonMailModel.setCompanyGenId(userSurveyorBasicDetails.getCompanyGenId());
			commonMailModel.setTemplateId(CommonConstants.INTIMATION_EMAIL_TEMPLATE_ID);
			commonMailModel.setParam(param);
			
			commonMailModel = fillEmailData(commonMailModel);
			response.setData(commonMailModel);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("success");
              
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Response :error --  Email Send unsuccessfull for Intimation form by email id: "
					+ commonMailModel.getCompanyGenId() + "error :"+e.getLocalizedMessage()+"  Method Name" + methodName
					+ " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getDocumentRequired(String vehicleType) {
		String methodName = "getDocumentRequired";
		ResponseModel response = new ResponseModel();
		log.info("Request : final required document by vehicleType : " + vehicleType + "  Method Name"
				+ methodName + " Class : " + this.getClass());
		try {
			 
			List<MasterDocumentRequired> docList=masterDocumentRequiredRepo.findByVehicleTypeIgnoreCaseAndActiveTrue(vehicleType);
			
			
			

		} catch (Exception e) {
			log.error("Respond : error occureed while finding vehicleType : "+vehicleType+ "  Method Name" + methodName + " Class : " + this.getClass());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage("error : " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

}
