package com.insuretech.survey.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommonConstants {
	public static final int BASIC_INITIAL_DETAILS = 1;
	public static final int POLICY_DETAILS_FILLED = 2;
	public static final int VEHICLE_DETAILS_FILLED = 3;
	public static final int DRIVER_DETAILS_FILLED = 4;
	public static final int LOSS_DETAILS_FILLED = 5;
	public static final int DAMAGE_DETAILS_FILLED = 6;
//	public static final int LOSS_ASS_DETAILS_FILLED = 7;
	public static final int FINAL_CONCLUSION_FILLED = 8;
	public static final int REFERENCE_ID_GENERATED = 9;
	public static final int DOC_IMG_UPLOAD_APK = 12;
	public static final int REPORT_DOC_UPLOADED = 19;
	public static final int SURVEY_FEE_GENERATED = 21;
//	public static final int  = 14;
	public static final int CONCLUSION_FILLED  = 7;
	
//	Change -- Aman -- Start
	public static final int SUBMIT_FOR_APPROVAL  = 10;
	public static final int APPROVED_STATUS  = 11;
	public static final int REFRE_BACK_STATUS  = 25;
	public static final int CLOSED_STATUS  = 26;
	public static final int COMMERCIAL_CONCLUSION_FILLED = 33;
	public static final int ASSESSMENT_PRO_FILLED = 32;

	
	public static final String BUTTON_APPROVED  = "Approved";
	public static final String BUTTON_REWORK  = "Rework";
//	Chagne -- Aman -- End
	
	
	public static final String SURVEY_LINK = "https://motosurvey.in/survey?vehicleNumber=";
	public static final String LINK_TYPE = "Survey Link";
	public static final Boolean LINK_EXPIRED_FALSE = false;

	// SAVING THE DATA ON MASTER TABLE FOR INTEMATION FORM
	public static final String SAVE_FOR_INSURER_DATA = "Add Insurer Data";
	public static final String SAVE_FOR_UNDER_WRITING_OFFICE = "Add Under Writing Office";
	public static final String SAVE_FOR_CLAIM_PROCESSIG_OFFICE = "Add Claim Processing Office";
	public static final String SAVE_FOR_WORKSHOP = "Add Workshop";

	public static final String USER_ROLE = "Admin";

	public static final String DEPARTMENT_TYPE = "motor";

	public static final String SAVE_FOR_POLICY_DATA = "Add Policy Data";
	public static final String SAVE_FOR_VEHICLE_DATA = "Add Vehicle Data";
	public static final String SAVE_FOR_DRIVER_PARTICULARS_DATA = "Add Driver Particulars Data";
	public static final String SAVE_FOR_LOSS_DATA = "Add Loss Data";
	public static final String SAVE_FOR_ASSEMBLY_DATA = "Add Assembly Data";
	public static final String SAVE_FOR_LABOUR_DATA = "Add Labour Data";
	public static final String SAVE_FOR_DAMAGE_DATA = "Add Damage Data";
	public static final String SAVE_FOR_CONCLUSION_DATA = "Add Conclusion Data";
	public static final String SAVE_REMARK_REPAIR = "Repair";
	public static final String SAVE_REMARK_REPLACE = "Replace";

	// EMAIL TEMPLATES
	public static final String REGISTATION_EMAIL_TEMPLATE_ID = "101";
	public static final String INTIMATION_EMAIL_TEMPLATE_ID = "102";
	public static final String FINAL_SUBMISSION_EMAIL_TEMPLATE_ID = "103";
	public static final String DOC_REQUIREMENT_EMAIL_TEMPLATE_ID = "104";
	public static final String ASSESSMENT_SUBMISSION_EMAIL_TEMPLATE_ID = "105";
	
	
	//metal dep tariff
	public static final Map<String,String> MAP_METAL_DEP;
	static {
	    Map<String, String> tempMap = new HashMap<>();
	    tempMap.put("NOT_EXCEEDING_SIX_MONTHS", "0%");
	    tempMap.put("SIX_MONTHS_TO_ONE_YEAR", "5%");
	    tempMap.put("ONE_YEAR_TO_TWO_YEAR", "10%");
	    tempMap.put("TWO_YEAR_TO_THREE_YEAR", "15%");
	    tempMap.put("THREE_YEAR_TO_FOUR_YEAR", "25%");
	    tempMap.put("FOUR_YEAR_TO_FIVE_YEAR", "35%");
	    tempMap.put("FIVE_YEAR_TO_TEN_YEAR", "40%");
	    tempMap.put("MORE_THEN_TEN_YEAR", "50%");
	    MAP_METAL_DEP = Collections.unmodifiableMap(tempMap);
	}
	
	public static final Integer TICKET_PENDING = 24;
	public static final Integer TICKET_CLOSED = 25;
	
	public static final String TICKET_PENDING_STATUS ="Ticket Pending" ;
	public static final String TICKET_CLOSED_STATUS = "Ticket Closed";
	
	
//	Change -- Aman -- Start
//	public static final String USER_ROLE = "002";
	public static final String ADMIN_ROLE = "001";
	public static final String INITIATOR_ROLE = "003";
	public static final String REPORTING_USER = "004";
	public static final String SURVEYOR_ROLE = "005";
	public static final String BACK_OFFICE_ROLE = "006";
//Change -- Aman -- End
	
	public static final String DOC_TYPE_FOLDER_LOGO = "logo";
	public static final String DOC_TYPE_FOLDER_SIGN = "sign";
	public static final String DOC_TYPE_FOLDER_REPORT = "report";
	
	// ai constant
	public static final String PRICE_TYPE_LABOUR = "LABOUR";
	public static final String PRICE_TYPE_PAINT = "PAINT";
	public static final String PRICE_TYPE_PRICE = "PRICE";
	
	
}
