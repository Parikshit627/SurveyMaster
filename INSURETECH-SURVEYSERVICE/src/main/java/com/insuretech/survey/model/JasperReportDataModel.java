package com.insuretech.survey.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JasperReportDataModel {

//company_details
private String title_license_number ;
private Date title_license_validity;
private String title_email_id;
private Long title_phone_no;
private String title_address;
private String full_name;
private String title_pan_card;
private String title_gst_no;
private String logouuid;

//registation
private String signuuid;

//policy details
private String insurer;
private String deputing_address;
private Timestamp deputation_date; 
private String deputing_office_code;
private String policy_no;
private String policy_to ;
private String policy_from ;
private String ncb ;
private String nil_dep ;
private String  break_in ;
private String idv ;
private String claim_sr ; 
private String insured_name;
private String policy_address;
private String hpa ;
private String insurance_contact;
private String insurance_comment;
private String claim_no;
private String other_ref_no;
private String third_party_policy;
private String previous_policy;


//basic_details
private Date date_of_intimation;
private String survey_type;
private String address;
private String underwriting_office_code;
private String underwriting_office_name ;
private String underwriting_office_address; 
private String surveyor;
private String reference_no;
private String insurer_abbreviation;
private String time_of_loss;
private Integer current_status;

//driver_particulars_details
private String endorsement;
private String driver_name ;
private String driver_address;
private String dl_no ;
private String licence_type ;
private String issuing_authority ;
private Timestamp dob ;
private Timestamp issued_on ;
private Timestamp valid_upto_nt ;
private Timestamp valid_upto_tv; 
private String rc_active;
private String dl_active;

//vehicle_details
private String reg_no; 
private String reg_owner;
private String make_variant;
private String colour;
private String body_type;
private String chassis_no;
private String motor_no;
private String cubic_cap;
private String odometer;
private String laden_wt;
private String unladen_wt;
private String pre_accident;
private String remark;
private String vehicle_type;
private String owner_sr;
private String fuel;
private String seating_capacity;
private String area_of_operation;
private Timestamp pucc;
private Timestamp tax_upto;
private Timestamp fitness_upto;
private Timestamp permit_validity;
private Timestamp permit_authorization;
private String dor;
private String company;
private Long mfg_year;
private String model;



//loss_details
private Timestamp date_of_loss;
private String cause_of_loss;
private String reported_to;
private String injury;
private String location;
private String spot_survey;
private String vechicle_shifted_add;
private String workshop;
private String cashless;
private String vechicle_shifted_remark;
private String estimated;
private Timestamp vechicle_shifted_date;
private String remarks_loss;

private String parts_allowed_amt;
private String parts_allowed_dep;
private String parts_allowed_gst;
private String paint_amt;
private String total_labour_amount;
private String total_labourgst;
private String towing_amt;
private String towinggst;
private String occupancy;

//conclusionn_details
private String conclusion_remark;
private Long conclusion_id;

//final_conclusion
private String final_conclusion_remark;

private String compulsory_clause;
private String salvage_charges;
private String average_clause;
private String other_deductibles;
private Boolean assess_cashless;
private String lessassessment;
private String less_metal_parts;
private String finalObservationsandFinding;
private String final_lessassessment;
private String final_less_metal_parts;
private String final_salvage_charges;
private String final_average_clause;
private String final_other_deductibles;
private String final_compulsory_clause;
private String labour_part25percent_tax;
private String gross_loss_amount;
private String gross_loss_dep;
private String gross_loss_gst;
private String net_loss_value;
private Boolean final_asassess_cashless;
private String dealer_type;


//observation_details
private String observation_desc;

//note_details
private String note_desc;


//not found field 
private String tp_policy;
private String issuing_auth_ab;
private String model_year;
private String premite_no;
private String auth_no;

private Timestamp conclusion_submit_date;
private Timestamp final_submit_date;

private String bill_date;


}
