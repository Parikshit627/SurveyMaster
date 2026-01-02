package com.insuretech.survey.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.PolicyDetails;

@Repository
public interface JasperDataRepo extends JpaRepository<PolicyDetails, Long> {

	
    @Query(value = "SELECT p.insurer, p.deputing_address, p.deputation_date, p.policy_no, p.policy_from, p.ncb, p.nil_dep, p.break_in, p.idv, p.claim_sr, p.policy_to,p.third_party_policy,p.previous_policy, " +
            "p.insured_name, p.address AS policy_address, p.hpa, p.contact AS insurance_contact, p.comment AS insurance_comment, p.claim_no, p.other_ref_no, p.deputing_office_code, " +
            "b.date_of_intimation, b.survey_type, b.address, b.underwriting_office_code, b.underwriting_office_name, b.underwriting_office_address,b.reference_no,b.surveyor,b.insurer_abbreviation,b.time_of_loss, b.current_status, " +
            "d.endorsement, d.driver_name, d.address AS driver_address, d.dl_no, d.licence_type, d.issuing_authority, d.dob, d.issued_on, d.valid_upto_nt, d.valid_upto_tv,d.rc_active,d.dl_active, " +
            "v.reg_no, v.reg_owner, v.make_variant, v.colour, v.body_type, v.chassis_no, v.motor_no, v.cubic_cap, v.odometer, v.laden_wt, v.unladen_wt, " +
            "v.pre_accident, v.remark, v.vehicle_type, v.owner_sr, v.fuel, v.seating_capacity, v.area_of_operation, v.pucc, v.tax_upto, v.fitness_upto, " +
            "v.permit_validity, v.permit_authorization, v.dor,v.company,v.mfg_year,v.model, " +
            "l.date_of_loss, l.cause_of_loss, l.reported_to, l.injury, l.location, l.spot_survey,l.address as vechicle_shifted_add,l.workshop,l.cashless,l.remark as vechicle_shifted_remark,l.estimated,\r\n"
            + "l.dated as vechicle_shifted_date,l.gst_summary_allowed_part as parts_allowed_amt,l.gst_summary_dep_part as  parts_allowed_dep,l.gst_summary_gst_part as parts_allowed_gst,l.dep50percent as paint_amt,l.total_labour_amount,l.total_labourgst,\r\n"
            + "l.towing_amount as towing_amt,l.towinggst,l.labour_part25percent_tax,l.dealer_type,l.occupancy,l.remarks_loss,  " +
            "c.full_name,c.license_number AS title_license_number,c.logouuid ,c.pan_number as title_pan_card,c.gst_number as title_gst_no, c.license_validity AS title_license_validity, c.email_id AS title_email_id, c.phone_no AS title_phone_no, CONCAT(c.location,', ', c.state, '-', c.pin_code) AS title_address," +
            "con.remark as conclusion_remark,con.sr as conclusion_id,con.compulsory_clause,con.salvage_charges,con.average_clause,con.other_deductibles,con.cashless as assess_cashless,con.lessassessment,con.less_metal_parts,con.created_dtm as conclusion_submit_date, "+
            "fc.final_remark as final_conclusion_remark,fc.final_observations as observation_desc,fc.final_notes as note_desc,fc.created_dtm as final_submit_date,fc.final_observationsand_finding,fc.lessassessment as final_lessassessment, fc.less_metal_parts as final_less_metal_parts,"
            + "fc.salvage_charges as final_salvage_charges, fc.average_clause as final_average_clause, fc.other_deductibles as final_other_deductibles,\r\n"
            + "fc.compulsory_clause as final_compulsory_clause,fc.gross_loss_amount,fc.gross_loss_dep,fc.gross_loss_gst,fc.net_loss_value,fc.cashless as final_asassess_cashless ,r.signuuid,inv.date as bill_date "+
            "FROM insuredb.user_surveyor_basic_details b " +
            "LEFT JOIN insuredb.policy_details p ON b.insurance_claim_id = p.insurence_gen_id " +
            "LEFT JOIN insuredb.driver_particulars_details d ON b.insurance_claim_id = d.insurence_gen_id " +
            "LEFT JOIN insuredb.vehicle_details v ON b.insurance_claim_id = v.insurence_gen_id " +
            "LEFT JOIN insuredb.loss_details l ON b.insurance_claim_id = l.insurence_gen_id " +
            "LEFT JOIN insuredb.company_registered c ON p.company_gen_id = c.company_gen_id " +
            "LEFT JOIN insuredb.conclusion_details con on b.insurance_claim_id=con.insurence_gen_id " +
            "LEFT JOIN insuredb.final_conclusion_details fc on b.insurance_claim_id=fc.insurence_gen_id " +
            "LEFT JOIN insuredb.registered r ON b.surveyor_email= r.email_id "+
            "LEFT JOIN insuredb.invoice inv ON b.reference_no = inv.reference_number "+
            "WHERE b.insurance_claim_id = :insurenceGenId AND b.company_gen_id = :companyGenId",
            nativeQuery = true)
    List<Map<String, Object>>  getJasperBeanData(@Param("insurenceGenId") Long insurenceGenId,
                                        @Param("companyGenId") String companyGenId);
    
    @Query(value="select b.date_of_intimation ,b.reference_no,b.surveyor,b.current_status,\r\n"
    		+ "p.insurer,p.deputing_address,p.deputation_date,p.insured_name,p.claim_no,p.policy_no,p.idv,p.insurer,\r\n"
    		+ "v.reg_no,v.make_variant,v.chassis_no,v.motor_no,v.company,v.mfg_year,v.model,\r\n"
    		+ "l.date_of_loss,l.workshop,r.signuuid,\r\n"
    		+ "fc.created_dtm as final_submit_date,\r\n"
//    		+ "c.full_name,c.license_number as title_license_number ,c.license_validity as title_license_validity,c.email_id as title_email_id,c.phone_no as title_phone_no,\r\n"
//    		+ "c.land_mark as title_land_mark,c.location as title_location,c.district as title_district,c.pin_code as title_pincode\r\n"
            +"c.full_name,c.license_number AS title_license_number,c.logouuid ,c.pan_number as title_pan_card,c.gst_number as title_gst_no, c.license_validity AS title_license_validity, c.email_id AS title_email_id, c.phone_no AS title_phone_no, CONCAT(c.location,', ', c.state, '-', c.pin_code) AS title_address\r\n"
    		+ "from insuredb.user_surveyor_basic_details b \r\n"
    		+ "left join insuredb.policy_details p on b.insurance_claim_id=p.insurence_gen_id\r\n"
    		+ "left join insuredb.vehicle_details v on b.insurance_claim_id=v.insurence_gen_id\r\n"
    		+ "left join insuredb.loss_details l on b.insurance_claim_id=l.insurence_gen_id\r\n"
    		+ "left join insuredb.company_registered c on p.company_gen_id=c.company_gen_id\r\n"
    		+ "left join insuredb.final_conclusion_details fc  on b.insurance_claim_id=fc.insurence_gen_id\r\n"
    		+"LEFT JOIN insuredb.registered r ON b.surveyor_email= r.email_id "
    		+ "WHERE b.insurance_claim_id = :insurenceGenId AND b.company_gen_id = :companyGenId",nativeQuery=true)
    List<Map<String, Object>>  getRiBeanData(@Param("insurenceGenId") Long insurenceGenId,
                                             @Param("companyGenId") String companyGenId);
    
    @Query(value="select p.claim_no ,p.hpa,p.insured_name,p.policy_no,p.policy_from,p.policy_to,p.nil_dep as policy_type,p.idv,p.deputation_date,p.insurer, \r\n"
    		+ "v.reg_no,v.make_variant,v.cubic_cap,v.company,v.mfg_year,v.model,\r\n"
    		+ "l.date_of_loss,l.address as loss_place,l.spot_survey,l.workshop,l.painting_labour75percent,l.total_labour_amount,l.gst_summary_allowed_part,\r\n"
    		+ "l.gst_summary_dep_part,l.towing_amount,l.dep50percent,l.gst_summary_gst_part,\r\n"
    		+ "b.date_of_intimation,b.created_date,b.surveyor,b.current_status,b.reference_no, \r\n"
    		+ "m.glass_allowed,m.glass_dep,m.metal_allowed,m.metal_dep,m.plastic_allowed,m.plastic_dep,m.ii_hand_allowed,m.ii_hand_dep,\r\n"
    		+ "m.others_allowed,m.others_dep,m.metalgst,m.plasticgst,m.glassgst,m.othersgst,m.ii_handgst,\r\n"
    		+ "f.salvage_charges,f.other_deductibles,f.average_clause,f.compulsory_clause,f.gross_loss_amount,f.net_loss_value,r.signuuid\r\n"
    		+ "from insuredb.user_surveyor_basic_details b \r\n"
    		+ "left join insuredb.policy_details p on b.insurance_claim_id=p.insurence_gen_id\r\n"
    		+ "left join insuredb.vehicle_details v on b.insurance_claim_id=v.insurence_gen_id\r\n"
    		+ "left join insuredb.loss_details l on b.insurance_claim_id=l.insurence_gen_id\r\n"
    		+ "left join insuredb.metal_calculation m on  b.insurance_claim_id=m.insurence_gen_id\r\n"
    		+ "left join insuredb.final_conclusion_details f on b.insurance_claim_id=f.insurence_gen_id "
    		+"LEFT JOIN insuredb.registered r ON b.surveyor_email= r.email_id "
    		+ "WHERE b.insurance_claim_id = :insurenceGenId AND b.company_gen_id = :companyGenId",nativeQuery=true)
    List<Map<String, Object>>  getScrutinySheetBeanData(@Param("insurenceGenId") Long insurenceGenId,
                                                        @Param("companyGenId") String companyGenId);
    
    @Query(value="select i.id,i.invoice_no,i.date,i.estimate,i.assessment,i.payment_info,i.subtotal,i.sgst,i.cgst,i.igst,i.total_value,i.roundoff,i.total_value_words,i.subject_matter,\r\n"
    		+ "    		cr.name as consigner_name,cr.address as consigner_address,cr.email as consigner_email,cr.phone as consigner_phone,cr.contact as consigner_contact,cr.gstin as consigner_gstin,cr.state as consigner_state,cr.state_code as consigner_state_code,\r\n"
    		+ "    		r.insurer_name as receiver_insurer_name,r.office_name as receiver_office_name,r.office_code as receiver_office_code,r.state as receiver_state,r.address as receiver_address,r.pan as receiver_pan,r.gstn as receiver_gstn,r.state_code as receiver_state_code,\r\n"
    		+ "    		ce.insurer_name as consignee_insurer_name,ce.office_name as consignee_office_name,ce.office_code as consignee_office_code,ce.state as consignee_state,ce.address as consignee_address,ce.gstn as consignee_gstn,ce.state_code as consignee_state_code,\r\n"
    		+ "    		isur.report_ref_number,isur.insured_name,isur.policy_number,isur.claim_number,isur.date_of_loss,\r\n"
    		+ "    		reg.signuuid,\r\n"
    		+ "    		b.surveyor,b.current_status,b.reference_no,\r\n"
    		+ "    		c.full_name\r\n"
    		+ "    		from insuredb.invoice i\r\n"
    		+ "    		left join insuredb.consigner cr on i.consigner_id=cr.id\r\n"
    		+ "    		left join insuredb.receiver r on i.receiver_id=r.id\r\n"
    		+ "    		left join insuredb.consignee ce on i.consignee_id=ce.id\r\n"
    		+ "    		left join insuredb.insurance_info isur on i.insurance_info_id=isur.id\r\n"
    		+ "    		LEFT JOIN insuredb.user_surveyor_basic_details b ON b.reference_no= i.reference_number\r\n"
    		+ "    		LEFT JOIN insuredb.registered reg ON b.surveyor_email= reg.email_id \r\n"
    		+ "    		left join insuredb.company_registered c on i.company_gen_id=c.company_gen_id"
    		+ "         WHERE i.company_gen_id = :companyGenId AND i.reference_number = :reference_number",nativeQuery=true)
    List<Map<String, Object>>  getBillBeanData(@Param("reference_number") String reference_number,
                                               @Param("companyGenId") String companyGenId);
    
    @Query(value="select it.description,it.hsn,it.qty,it.price,it.taxable,it.gst,it.total from insuredb.item it  where it.invoice_id = :invoice_id",nativeQuery=true)
    List<Map<String, Object>>  getBillBeanTableData(@Param("invoice_id") Long invoice_id);
    
    
    @Query(value="select b.reference_no,b.asset,b.date_of_loss,b.metal_dep,b.surveyor,b.current_status,\r\n"
    		+ "m.gst_portion,m.subtotal,m.totalestimate,\r\n"
    		+ "l.total_paint_allowed_cal,l.total_paint_tax_cal,l.dep50percent,l.gst_summary_allowed_part,l.gst_summary_gst_part,l.gst_summary_dep_part,\r\n"
    		+ "l.total_estimated,l.total_repair,l.total_replace,l.gst_estimated,l.gst_repair,l.gst_replace,l.total_paint_estimate,l.gst_paint,\r\n"
    		+ "v.dor,c.full_name,\r\n"
    		+ "p.nil_dep,r.signuuid,\r\n"
    		+ "fc.gross_loss_amount,fc.gross_loss_dep,fc.gross_loss_gst,fc.net_loss_value,fc.salvage_charges,fc.compulsory_clause\r\n"
    		+ "from insuredb.user_surveyor_basic_details b \r\n"
    		+ "left join insuredb.loss_details l on b.insurance_claim_id=l.insurence_gen_id\r\n"
    		+ "left join insuredb.metal_calculation m on b.insurance_claim_id=m.insurence_gen_id\r\n"
    		+ "left join insuredb.vehicle_details v on b.insurance_claim_id=v.insurence_gen_id\r\n"
    		+ "left join insuredb.policy_details p on b.insurance_claim_id=p.insurence_gen_id\r\n"
    		+ "left join insuredb.final_conclusion_details fc  on b.insurance_claim_id=fc.insurence_gen_id\r\n"
    		+"LEFT JOIN insuredb.registered r ON b.surveyor_email= r.email_id "
    		+"LEFT JOIN insuredb.company_registered c ON b.company_gen_id = c.company_gen_id " 
    		+ "where b.insurance_claim_id =:insurenceGenId and b.company_gen_id=:companyGenId ",nativeQuery=true)
    List<Map<String, Object>>  getAssessmentData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId);
    
    @Query(value="select ap.order_sr,ap.assembly_name,ap.part_type,ap.bill_sr,ap.remarks ,ap.estimated,ap.gst,ap.allowed,\r\n"
    		+ "lb.loabor_estimated,lb.replace,lb.repair \r\n"
    		+ "from insuredb.assembly_details ap \r\n"
    		+ "LEFT JOIN insuredb.labour_details lb ON ap.insurence_gen_id = lb.insurence_gen_id\r\n"
    		+ "where lb.order_sr = (\r\n"
    		+ "    SELECT MAX(ad.order_sr)\r\n"
    		+ "    FROM insuredb.assembly_details ad\r\n"
    		+ "    WHERE ad.insurence_gen_id = ap.insurence_gen_id) + ap.order_sr and \r\n"
    		+ "    ap.insurence_gen_id =:insurenceGenId and ap.company_gen_id=:companyGenId order by ap.order_sr  ",nativeQuery=true)
    List<Map<String, Object>> getPartListTableData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId); 
    
    @Query(value="select lb.order_sr,lb.labour_parts_name,lb.paint_estimate,lb.allowed from insuredb.labour_details lb \r\n"
    		+ "where lb.insurence_gen_id =:insurenceGenId and lb.company_gen_id=:companyGenId order by lb.order_sr ",nativeQuery=true)
    List<Map<String, Object>>  getLabourListTableData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId);
    
    @Query(value="SELECT \r\n"
    		+ "ap.parts_name,ap.type,ap.bill_sr,ap.remarks,ap.estimated,ap.gst,ap.allowed,lb.allowed_labourt,lb.estimated as loabor_estimated\r\n"
    		+ "FROM insuredb.part_assessment_pro ap\r\n"
    		+ "LEFT JOIN insuredb.labour_assessment_pro lb \r\n"
    		+ "ON ap.insurence_gen_id = lb.insurence_gen_id and lb.s_no = ap.s_no "
    		+ " where ap.insurence_gen_id =:insurenceGenId and ap.company_gen_id=:companyGenId order by ap.s_no  ",nativeQuery=true)
    List<Map<String, Object>> getComPartListTableData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId); 
    
    
    @Query(value="select lb.s_no,lb.parts_name,lb.paint_estimate,lb.allowed,lb.paint25part,lb.paint75labour,lb.lab_gst from insuredb.labour_assessment_pro lb \r\n"
    		+ "where lb.insurence_gen_id =:insurenceGenId and lb.company_gen_id=:companyGenId order by lb.s_no ",nativeQuery=true)
    List<Map<String, Object>>  getComLabourListTableData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId);
    
    
    @Query(value="SELECT ap.s_no,ap.parts_name,ap.type,ap.bill_sr,ap.remarks,ap.estimated,ap.gst,ap.allowed,ap.assessed,ap.ex_allowed\r\n"
    		+ "FROM insuredb.part_assessment_pro ap \r\n"
    		+ "where ap.insurence_gen_id =:insurenceGenId and ap.company_gen_id=:companyGenId and LOWER(ap.imt)=LOWER('yes') order by ap.s_no ",nativeQuery=true)
    List<Map<String, Object>>  getComImtListTableData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId);
    
    @Query(value="select b.reference_no,b.asset,b.date_of_loss,b.metal_dep,b.surveyor,b.current_status,\r\n"
    		+ "cap.total_allowed,cap.total_ex_dep,cap.total_ex_allowed,cap.total_ex_tax,cap.total_assessed,cap.total_estimated,cap.total_gst,cap.total_tax_paid as imt23Total,\r\n"
    		+ "cap.total_dep0percent,cap.total_dep18percent,cap.total_dep28percent,cap.total_gst0percent,cap.total_gst18percent,cap.total_gst28percent,\r\n"
    		+ "cl.total_estimated as labour_estimated,cl.total_estimate_gst as labour_est_gst, cl.total_allowed_labourt,cl.total_labour_gstt,\r\n"
    		+ "cl.total_paint_estimate,cl.total_paint_estimate_gst,cl.total_allowed as total_allowed_paint,cl.total_allowed_paint_gst,\r\n"
    		+ "cl.total_paint25part,cl.total_gst_paint25part,cl.total_paint75labour,cl.total_gst_paint75labour,\r\n"
    		+ "v.dor,c.full_name,\r\n"
    		+ "p.nil_dep,r.signuuid,cc.accidental_labour_depreciation,cc.accidental_labourgst,cc.accidental_labour_actual_allowed,cc.accidental_labour_assessed,\r\n"
    		+ "cc.towing_charges_final,cc.imt23deduction,cc.salvage_charges,cc.sub_total,cc.average_clause,cc.compulsory_excess,cc.paint_labour75depreciation,cc.paint_labour75gst,cc.paint_labour75actual_allowed,cc.paint_labour75assessed,\r\n"
    		+ "cc.voluntary_imposed_excess,cc.gross_loss_assesed_gross_allowed,cc.net_loss_assessed,cc.paint_material25depreciation,cc.paint_material25gst,cc.paint_material25actual_allowed,cc.paint_material25assessed,\r\n"
    		+ "cap.invoice_number,cap.amount,cap.issued_on,cap.received_on,cap.total_tax0percent,cap.total_tax18percent,cap.total_tax28percent \r\n"
    		+ "from insuredb.user_surveyor_basic_details b\r\n"
    		+ "left join  insuredb.calculation_part_assessment_pro cap on b.insurance_claim_id=cap.insurence_gen_id\r\n"
    		+ "left join insuredb.calculation_labour_assessment_pro cl on b.insurance_claim_id=cl.insurence_gen_id \r\n"
    		+ "left join insuredb.vehicle_details v on b.insurance_claim_id=v.insurence_gen_id\r\n"
    		+ "LEFT JOIN insuredb.company_registered c ON b.company_gen_id = c.company_gen_id \r\n"
    		+ "left join insuredb.policy_details p on b.insurance_claim_id=p.insurence_gen_id\r\n"
    		+ "LEFT JOIN insuredb.registered r ON b.surveyor_email= r.email_id \r\n"
    		+ "left join insuredb.conclusion_assessment_pro cc on b.insurance_claim_id=cc.insurence_gen_id\r\n"
    		+ "left join insuredb.invoice i on b.reference_no=i.reference_number " 
    		+ "where b.insurance_claim_id =:insurenceGenId and b.company_gen_id=:companyGenId ",nativeQuery=true)
    List<Map<String, Object>>  getComAssessmentData(@Param("insurenceGenId") Long insurenceGenId,
                                                     @Param("companyGenId") String companyGenId);
    
    @Query(value = "SELECT p.insurer, p.deputing_address, p.deputation_date, p.policy_no, p.policy_from, p.ncb, p.nil_dep, p.break_in, p.idv, p.claim_sr, p.policy_to,p.third_party_policy,p.previous_policy, " +
            "p.insured_name, p.address AS policy_address, p.hpa, p.contact AS insurance_contact, p.comment AS insurance_comment, p.claim_no, p.other_ref_no, p.deputing_office_code, " +
            "b.date_of_intimation, b.survey_type, b.address, b.underwriting_office_code, b.underwriting_office_name, b.underwriting_office_address,b.reference_no,b.surveyor,b.insurer_abbreviation,b.time_of_loss, b.current_status, " +
            "d.endorsement, d.driver_name, d.address AS driver_address, d.dl_no, d.licence_type, d.issuing_authority, d.dob, d.issued_on, d.valid_upto_nt, d.valid_upto_tv,d.rc_active,d.dl_active, " +
            "v.reg_no, v.reg_owner, v.make_variant, v.colour, v.body_type, v.chassis_no, v.motor_no, v.cubic_cap, v.odometer, v.laden_wt, v.unladen_wt, " +
            "v.pre_accident, v.remark, v.vehicle_type, v.owner_sr, v.fuel, v.seating_capacity, v.area_of_operation, v.pucc, v.tax_upto, v.fitness_upto, " +
            "v.permit_validity, v.permit_authorization, v.dor,v.company,v.mfg_year,v.model,v.authorization_upto as premite_no ,v.authorization_number as auth_no, " +
            "l.date_of_loss, l.cause_of_loss, l.reported_to, l.injury, l.location, l.spot_survey,l.address as vechicle_shifted_add,l.workshop,l.cashless,l.remark as vechicle_shifted_remark,l.estimated,\r\n"
            +"l.dated as vechicle_shifted_date,l.gst_summary_allowed_part as parts_allowed_amt,l.gst_summary_dep_part as  parts_allowed_dep,l.gst_summary_gst_part as parts_allowed_gst,l.dep50percent as paint_amt,l.total_labour_amount,l.total_labourgst,\r\n"
            +"l.towing_amount as towing_amt,l.towinggst,l.labour_part25percent_tax,l.dealer_type,l.occupancy,l.remarks_loss,  " +
            "c.full_name,c.license_number AS title_license_number,c.logouuid ,c.pan_number as title_pan_card,c.gst_number as title_gst_no, c.license_validity AS title_license_validity, c.email_id AS title_email_id, c.phone_no AS title_phone_no, CONCAT(c.location,', ', c.state, '-', c.pin_code) AS title_address," +
            "con.remark as conclusion_remark,con.sr as conclusion_id,con.compulsory_clause,con.salvage_charges,con.average_clause,con.other_deductibles,con.cashless as assess_cashless,con.lessassessment,con.less_metal_parts,con.created_dtm as conclusion_submit_date, "+
            "cp.concluding_remark as final_conclusion_remark,cp.notes as note_desc,cp.survey_inspection_details,cp.voluntary_imposed_excess as final_other_deductibles,cp.observation as observation_desc,cp.accidental_labour_depreciation,cp.accidental_labour_actual_allowed,cp.accidental_labour_assessed,cp.paint_labour75gross_allowed,cp.paint_labour75depreciation,cp.paint_labour75gst,cp.paint_labour75actual_allowed,cp.paint_labour75assessed,\r\n"
            + "cp.salvage_charges as final_salvage_charges,cp.average_clause as final_average_clause,cp.compulsory_excess as final_compulsory_clause,r.signuuid,inv.date as bill_date, cp.net_loss_assessed,cp.metal_part_percent as final_less_metal_parts,cp.assessmentpercent as final_lessassessment,cp.towing_charges_final,cp.imt23deduction,cp.sub_total,"
            +"cp.total_est_parts_gross_allowed,cp.total_est_parts_depreciation,cp.total_est_partsgst,cp.total_est_parts_actual_allowed,cp.total_est_parts_assessed,cp.paint_material25gross_allowed,cp.paint_material25depreciation,cp.paint_material25gst,cp.paint_material25actual_allowed,cp.paint_material25assessed,cp.gross_loss_assesed_gross_allowed,cp.gross_loss_assesed_depreciation,cp.gross_loss_assesed_appliedgst "+
            "FROM insuredb.user_surveyor_basic_details b " +
            "LEFT JOIN insuredb.policy_details p ON b.insurance_claim_id = p.insurence_gen_id " +
            "LEFT JOIN insuredb.driver_particulars_details d ON b.insurance_claim_id = d.insurence_gen_id " +
            "LEFT JOIN insuredb.vehicle_details v ON b.insurance_claim_id = v.insurence_gen_id " +
            "LEFT JOIN insuredb.loss_details l ON b.insurance_claim_id = l.insurence_gen_id " +
            "LEFT JOIN insuredb.company_registered c ON p.company_gen_id = c.company_gen_id " +
            "LEFT JOIN insuredb.conclusion_details con on b.insurance_claim_id=con.insurence_gen_id " +
            "LEFT JOIN insuredb.conclusion_assessment_pro cp on b.insurance_claim_id=cp.insurence_gen_id " +
            "LEFT JOIN insuredb.registered r ON b.surveyor_email= r.email_id "+
            "LEFT JOIN insuredb.invoice inv ON b.reference_no = inv.reference_number "+
            "WHERE b.insurance_claim_id = :insurenceGenId AND b.company_gen_id = :companyGenId",
            nativeQuery = true)
    List<Map<String, Object>>  getComReportData(@Param("insurenceGenId") Long insurenceGenId,
                                        @Param("companyGenId") String companyGenId);
    
  
}
