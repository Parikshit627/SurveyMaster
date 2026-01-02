package com.insuretech.survey.serviceImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.UserSurveyorBasicDetails;
import com.insuretech.survey.entity.VehicleCompanyDetails;
import com.insuretech.survey.entity.VehicleMasterDetails;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.GetDefinedDataIntimationService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class GetDefinedDataIntimationServiceImpl extends AbstractMasterRepository
		implements GetDefinedDataIntimationService {

	Logger log = LoggerFactory.getLogger(UserSurveyorServiceImpl.class);

	@Autowired
	private EntityManager entityManager;

	@Override
	public ResponseModel getInsurerAllDetails(String abbreviation) {
		String methodName = "getInsurerAllDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Finding Insurer All Details by abbreviation: " + abbreviation + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			String sql = "SELECT abbreviation, insurer, address, city, state, gst_number, pin_code, sr"
					+ " FROM insuredb.insurer_details" + " WHERE LOWER(abbreviation) LIKE LOWER('" + abbreviation
					+ "%');";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> InsurerAllDetails = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> InsurerDetails = new HashMap<>();
				InsurerDetails.put("abbreviation", (String) row[0]);
				InsurerDetails.put("insurer", (String) row[1]);
//				InsurerDetails.put("type", (String) row[3]);
				InsurerDetails.put("address", (String) row[2]);
				InsurerDetails.put("city", (String) row[3]);
				InsurerDetails.put("state", (String) row[4]);
				InsurerDetails.put("gstn", (String) row[5]);
				InsurerDetails.put("pincode", (String) row[6]);
				InsurerDetails.put("sr", row[7]);
				InsurerAllDetails.add(InsurerDetails);
			}

			response.setData(InsurerAllDetails);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully Insurer All Details by abbreviation: " + abbreviation
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding Insurer All Details by abbreviation: " + abbreviation
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getAllClaimProcessingOffice(String officeCode) {
		String methodName = "getAllClaimProcessingOffice";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Finding All Claim Processing Office by officeCode: " + officeCode + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			String sql = "SELECT claim_processing_office_code, claim_processing_office_name, location, land_mark, district, pin_code, state, state_code, gst_number, insurer_abbreviation, insurer_name, sr " +
		             "FROM insuredb.claim_processing_office " +
		             "WHERE ('" + officeCode + "' = '' " +
		             "OR LOWER(insurer_abbreviation) = LOWER('" + officeCode + "') " +
		             "OR claim_processing_office_code = '" + officeCode + "')";




			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> AllClaimProcessingOffice = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> claimProcessingOffice = new HashMap<>();
//				claimProcessingOffice.put("underwriting_office", (String) row[1]);
				claimProcessingOffice.put("office_code", (String) row[0]);
				claimProcessingOffice.put("office_name", (String) row[1]);
				claimProcessingOffice.put("address", (String) row[2] + (String) row[3]);
				claimProcessingOffice.put("city", (String) row[4]);
				claimProcessingOffice.put("pincode", row[5]);
				claimProcessingOffice.put("state", (String) row[6]);
				claimProcessingOffice.put("state_code", row[7]);
				claimProcessingOffice.put("gstn", (String) row[8]);
				claimProcessingOffice.put("insurer_abbreviation", (String) row[9]);
				claimProcessingOffice.put("insurer_name", (String) row[10]);
				claimProcessingOffice.put("sr", row[11]);

				AllClaimProcessingOffice.add(claimProcessingOffice);
			}

			response.setData(AllClaimProcessingOffice);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Claim Processing Office by officeCode: " + officeCode
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Claim Processing Office by officeCode: " + officeCode
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getAllUnderWritingOffice(String officeCode) {
		String methodName = "getAllClaimProcessingOffice";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Finding All Under Writing Office by officeCode: " + officeCode + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			String sql = "SELECT underwriting_office_code, underwriting_office_name, land_mark, location, district, state, pin_code, state_code, gst_number, insurer_abbreviation, insurer_name, sr " +
		             "FROM insuredb.underwriting_office " +
		             "WHERE ('" + officeCode + "' = '' " +
		             "OR LOWER(insurer_abbreviation) = LOWER('" + officeCode + "') " +
		             "OR underwriting_office_code = '" + officeCode + "')";


			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> allUnderWritingOffice = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> underWritingOfficeData = new HashMap<>();
//				underWritingOfficeData.put("underwriting_office", (String) row[0]);
				underWritingOfficeData.put("office_code", (String) row[0]);
				underWritingOfficeData.put("office_name", (String) row[1]);
				underWritingOfficeData.put("address", (String) row[2] + (String) row[3]);
				underWritingOfficeData.put("city", (String) row[4]);
				underWritingOfficeData.put("state", (String) row[5]);
				underWritingOfficeData.put("pincode", row[6]);
				underWritingOfficeData.put("state_code", row[7]);
				underWritingOfficeData.put("gstn", (String) row[8]);
				underWritingOfficeData.put("insurer_abbreviation", (String) row[9]);
				underWritingOfficeData.put("insurer_name", (String) row[10]);
				underWritingOfficeData.put("sr", row[11]);

				allUnderWritingOffice.add(underWritingOfficeData);
			}

			response.setData(allUnderWritingOffice);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Under Writing Office by officeCode: " + officeCode
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Under Writing Office by officeCode: " + officeCode
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getAllWorkshopDetails(String workShopName) {
		String methodName = "getAllWorkshopDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Finding All Workshop Details by workShopName: " + workShopName + "  Method Name" + methodName
					+ " Class : " + this.getClass());

			String sql = "SELECT workshop_name, land_mark, location, district, state,pin_code,state_code, gst_number, sr,contact_numbers,email_id"
					+ " FROM insuredb.work_shop_details WHERE LOWER(workshop_name) LIKE LOWER('" + workShopName + "%')";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> allWorkshopDetails = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> workshopDetails = new HashMap<>();
//				underWritingOfficeData.put("underwriting_office", (String) row[0]);
				workshopDetails.put("workshop_name", (String) row[0]);
				workshopDetails.put("address", (String) row[1] + (String) row[2]);
				workshopDetails.put("city", (String) row[3]);
				workshopDetails.put("state", (String) row[4]);
				workshopDetails.put("pincode", row[5]);
				workshopDetails.put("state_code", row[6]);
				workshopDetails.put("gstn", (String) row[7]);
				workshopDetails.put("sr", row[8]);
				workshopDetails.put("phnNo", row[9]);
				workshopDetails.put("email", (String) row[10]);


				allWorkshopDetails.add(workshopDetails);
			}

			response.setData(allWorkshopDetails);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Workshop Details by workShopName: " + workShopName
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Workshop Details by workShopName: " + workShopName
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

//	@Override
//	public ResponseModel getVehicleMasterDetails(String vehicleId, String model, String variant, String company) {
//		String methodName = "getVehicleMasterDetails";
//		ResponseModel response = new ResponseModel();
//		String sql = "";
//		try {
//			if (vehicleId != null && !vehicleId.trim().equals("")) {
//				log.info("Request : Finding Vehicle Master Details by vehicleId: " + vehicleId + "  Method Name"
//						+ methodName + " Class : " + this.getClass());
//
//				sql = "select vm.vehicle_id,vm.company,vm.type,vv.model,vv.variant,vv.cubic_capacity,vv.fule,vv.laden_weight,vv.mfg_year,vv.seating_capacity,vv.unladen_weight\r\n"
//						+ "from insuredb.vehicle_master_details vm inner join insuredb.vehicle_variant_details vv\r\n"
//						+ "on vm.vehicle_id=vv.vehicle_id where LOWER(vm.vehicle_id) LIKE LOWER('%" + vehicleId + "%')";
//
//			}
//
//			else if (model != null && !model.trim().equals("")) {
//				log.info("Request : Finding Vehicle Master Details by model: " + model + "  Method Name" + methodName
//						+ " Class : " + this.getClass());
//
//				sql = "select vm.vehicle_id,vm.company,vm.type,vv.model,vv.variant,vv.cubic_capacity,vv.fule,vv.laden_weight,vv.mfg_year,vv.seating_capacity,vv.unladen_weight\r\n"
//						+ "from insuredb.vehicle_master_details vm inner join insuredb.vehicle_variant_details vv\r\n"
//						+ "on vm.vehicle_id=vv.vehicle_id where LOWER(vv.model) LIKE LOWER('%" + model + "%')";
//
//			}
//
//			else if (variant != null && !variant.trim().equals("")) {
//				log.info("Request : Finding Vehicle Master Details by variant: " + variant + "  Method Name"
//						+ methodName + " Class : " + this.getClass());
//
//				sql = "select vm.vehicle_id,vm.company,vm.type,vv.model,vv.variant,vv.cubic_capacity,vv.fule,vv.laden_weight,vv.mfg_year,vv.seating_capacity,vv.unladen_weight\r\n"
//						+ "from insuredb.vehicle_master_details vm inner join insuredb.vehicle_variant_details vv\r\n"
//						+ "on vm.vehicle_id=vv.vehicle_id where LOWER(vv.variant) LIKE LOWER('%" + variant + "%')";
//
//			}
//
//			else if (company != null && !company.trim().equals("")) {
//				log.info("Request : Finding Vehicle Master Details by company: " + company + "  Method Name"
//						+ methodName + " Class : " + this.getClass());
//
//				sql = "select vm.vehicle_id,vm.company,vm.type,vv.model,vv.variant,vv.cubic_capacity,vv.fule,vv.laden_weight,vv.mfg_year,vv.seating_capacity,vv.unladen_weight\r\n"
//						+ "from insuredb.vehicle_master_details vm inner join insuredb.vehicle_variant_details vv\r\n"
//						+ "on vm.vehicle_id=vv.vehicle_id where LOWER(vm.company) LIKE LOWER('%" + company + "%')";
//
//			} else {
//				log.info("Respond : Invliad Parameters" + "  Method Name" + methodName + " Class : " + this.getClass());
//
//				response.setHttpStatus(HttpStatus.OK);
//				response.setMessage("Invalid Parameter");
//
//				return response;
//
//			}
//
//			Query query = entityManager.createNativeQuery(sql);
//			// Retrieve the result list
//			List<Object[]> results = query.getResultList();
//			List<Object> vehicleMasterList = new ArrayList<>();
//
//			// Iterate through the results and extract the necessary details
//			for (Object[] row : results) {
//				Map<String, Object> vehicleMaster = new HashMap<>();
//				vehicleMaster.put("vehicle_id", row[0]);
//				vehicleMaster.put("company", row[1]);
//				vehicleMaster.put("type", row[2]);
//				vehicleMaster.put("model", row[3]);
//				vehicleMaster.put("variant", row[4]);
//				vehicleMaster.put("cubic_capacity", row[5]);
//				vehicleMaster.put("fule", row[6]);
//				vehicleMaster.put("laden_weight", row[7]);
//				vehicleMaster.put("mfg_year", row[8]);
//				vehicleMaster.put("seating_capacity", row[9]);
//				vehicleMaster.put("unladen_weight", row[10]);
//				vehicleMaster.put("vehicle_variant", row[1]+" "+row[3]+" "+row[4]);
//
//				vehicleMasterList.add(vehicleMaster);
//			}
//			response.setData(vehicleMasterList);
//			response.setHttpStatus(HttpStatus.OK);
//			response.setMessage("Data get Successfully");
//
//			log.info("Respond : data founded successfully Vehicle Master Details  " + "  Method Name" + methodName
//					+ " Class : " + this.getClass());
//		} catch (Exception e) {
//			log.error("An error occurred while Finding Vehicle Master Details" + e.getMessage(),
//					"  Method Name" + methodName + " Class : " + this.getClass());
//		}
//		return response;
//	}

	@Override
	public ResponseModel getVehicleMasterDetails(String companyId, String type, String model) {
		String methodName = "getVehicleMasterDetails";
		ResponseModel response = new ResponseModel();
		String sql = "";
		try {
			if ((companyId == null && companyId.trim().equals("")) && (type == null && type.trim().equals(""))) {
				log.info("Respond : Invliad Parameters" + "  Method Name" + methodName + " Class : " + this.getClass());
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Invalid Parameter");

				return response;
			} else {
				log.info("Request : Finding Vehicle Master Details by companyId : " + companyId + " ,typeId" + type
						+ " ,model " + model + " - Method Name" + methodName + " Class : " + this.getClass());
				model = model == null ? "" : model.trim();
				sql = "select model,variant,cubic_capacity,fule,laden_weight,seating_capacity,unladen_weight from insuredb.vehicle_master_details\r\n"
						+ "where company_id='" + companyId + "' AND type='" + type
						+ "' AND LOWER(model) like LOWER('" + model + "%')";
			}

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> vehicleMasterList = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> vehicleMaster = new HashMap<>();
				vehicleMaster.put("model", row[0]);
				vehicleMaster.put("variant", row[1]);
				vehicleMaster.put("cubic_capacity", row[2]);
				vehicleMaster.put("fule", row[3]);
				vehicleMaster.put("laden_weight", row[4]);
				vehicleMaster.put("seating_capacity", row[5]);
				vehicleMaster.put("unladen_weight", row[6]);
				vehicleMasterList.add(vehicleMaster);
			}
			response.setData(vehicleMasterList);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully Vehicle Master Details  " + "  Method Name" + methodName
					+ " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding Vehicle Master Details" + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getAllExteriorCarParts(String partName, Long vehicleTypeId) {
		String methodName = "getAllExteriorCarParts";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Finding All Exterior Car Parts " + partName + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			if (partName == null || partName.trim().equals("")) {
				log.info("Respond : Invliad Parameters" + "  Method Name" + methodName + " Class : " + this.getClass());
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Invalid Parameter");
				return response;
			}
			String sql = "SELECT part_name,vehicle_type_id,vehicle_type_name,hsn,type,gst,cost"
					+ " FROM insuredb.exterior_car_parts WHERE vehicle_type_id='" + vehicleTypeId
					+ "' AND LOWER(part_name) LIKE LOWER('%" + partName + "%')";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> allPartsMasterDetails = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> partsMasterDetails = new HashMap<>();
				partsMasterDetails.put("part_name", (String) row[0]);
				partsMasterDetails.put("vehicle_type_id", row[1]);
				partsMasterDetails.put("vehicle_type_name", (String) row[2]);
				partsMasterDetails.put("hsn", (String) row[3]);
				partsMasterDetails.put("type", (String) row[4]);
				partsMasterDetails.put("gst", (String) row[5]);
				partsMasterDetails.put("cost", (String) row[6]);

				allPartsMasterDetails.add(partsMasterDetails);
			}
			response.setData(allPartsMasterDetails);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Exterior Car Parts " + partName + "  Method Name"
					+ methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Exterior Car Parts " + partName + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getAllVehicleCompanyDetails(String company) {
		String methodName = "getAllVehicleCompanyDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Finding All Vehicle Company Detalis by company name " + company + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			if (company == null || company.trim().equals("")) {
				log.info("Respond : Invliad Parameters" + "  Method Name" + methodName + " Class : " + this.getClass());
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Invalid Parameter");
				return response;
			}
			String sql = "select company_id,compnay from insuredb.vehicle_company_details where LOWER(compnay) LIKE LOWER('"
					+ company + "%')";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> allVehicleCompanyDetalis = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> vehicleCompanyDetalis = new HashMap<>();
				vehicleCompanyDetalis.put("company_id", row[0]);
				vehicleCompanyDetalis.put("company", (String) row[1]);

				allVehicleCompanyDetalis.add(vehicleCompanyDetalis);
			}
			response.setData(allVehicleCompanyDetalis);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Vehicle Company Detalis by company name  " + company
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Vehicle Company Detalis by company name  " + company
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getAllVehicleTypeDetails(String type) {
		String methodName = "getAllVehicleTypeDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Finding All Vehicle Type Detalis by Type name " + type + "  Method Name" + methodName
					+ " Class : " + this.getClass());
			type = type == null ? "" : type.trim();
			String sql = "select type_id,type from insuredb.vehicle_type_details where LOWER(type) LIKE LOWER('" + type
					+ "%')";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> allVehicleTypeDetalis = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> vehicleTypeDetalis = new HashMap<>();
				vehicleTypeDetalis.put("type_id", row[0]);
				vehicleTypeDetalis.put("type", (String) row[1]);

				allVehicleTypeDetalis.add(vehicleTypeDetalis);
			}
			response.setData(allVehicleTypeDetalis);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Vehicle Type Detalis by Type name  " + type
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Vehicle Type Detalis by Type name  " + type + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getFinalDocumentSubmission(String companyGenId, Long insurenceGenId, String docName) {
		String methodName = "getFinalDocumentSubmission";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : Finding Document by companyGenId " + companyGenId + ", insurenceGenId : "
					+ insurenceGenId + " , docName : " + docName + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			FinalDocumentSubmission doc = finalDocumentSubmissionRepo
					.findByCompanyGenIdAndAndInsurenceGenIdAndDocName(companyGenId, insurenceGenId, docName).get(0);
			response.setData(doc);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond :  Document founded successfully by companyGenId " + companyGenId + ", insurenceGenId : "
					+ insurenceGenId + " , docName : " + docName + "  Method Name" + methodName + " Class : "
					+ this.getClass());
		} catch (Exception e) {

			log.info("Respond :  An error occurred while finding documents ,error : " + e.getMessage() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getBranchMasterDetails(String companyGenId, String branchName) {
		String methodName = "getBranchMasterDetails";
		ResponseModel response = new ResponseModel();
		try {
			if (companyGenId == null || companyGenId.trim().equals("companyGenId")) {

				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invaid paramater");

				log.info("Respond : Invalid Parameter " + "  Method Name" + methodName + " Class : " + this.getClass());

				return response;
			} else {
				log.info("Request : fetch all Branch Master Details By companyGenId : " + companyGenId + "  Method Name"
						+ methodName + " Class : " + this.getClass());
				String sql = "";
				if (branchName != null && !branchName.trim().equals("")) {
					sql = "select branch_gen_id,branch_abbreviation,branch_name,branche_email,branch_address,company_gen_id,district,location,pin_code,state,state_code,contact_numbers from "
							+ "insuredb.branch_master where company_gen_id='" + companyGenId
							+ "' AND LOWER(branch_name) like (LOWER('" + branchName + "%'))";
				} else {
					sql = "select branch_gen_id,branch_abbreviation,branch_name,branche_email,branch_address,company_gen_id,district,location,pin_code,state,state_code,contact_numbers from "
							+ "insuredb.branch_master where company_gen_id='" + companyGenId + "'";
				}

				Query query = entityManager.createNativeQuery(sql);
				// Retrieve the result list
				List<Object[]> results = query.getResultList();
				List<Object> branchMasterList = new ArrayList<>();

				// Iterate through the results and extract the necessary details
				for (Object[] row : results) {
					Map<String, Object> branchMaster = new HashMap<>();
					branchMaster.put("branchGenId", row[0]);
					branchMaster.put("branchAbbreviation", (String) row[1]);
					branchMaster.put("branchName", (String) row[2]);
					branchMaster.put("brancheEmail", (String) row[3]);
					branchMaster.put("branchAddress", (String) row[4]);
					branchMaster.put("companyGenId", (String) row[5]);
					branchMaster.put("district", (String) row[6]);
//					branchMaster.put("landMark", (String) row[7]);
					branchMaster.put("location", (String) row[7]);
					branchMaster.put("pinCode", row[8]);
					branchMaster.put("state", (String) row[9]);
					branchMaster.put("stateCode", row[10]);
					branchMaster.put("contactNumbers", row[11]);
					branchMasterList.add(branchMaster);
				}
				if (branchMasterList != null && !branchMasterList.isEmpty()) {
					response.setHttpStatus(HttpStatus.OK);
					response.setData(branchMasterList);
					response.setMessage("Data fetched Successfully");

					log.info("Respond : Successfully fetched Branch Master Details By companyGenId : " + companyGenId
							+ "  Method Name" + methodName + " Class : " + this.getClass());

				} else {

					response.setHttpStatus(HttpStatus.NO_CONTENT);
					response.setMessage("no data found -- Branch Master Details");

					log.info("Respond :no data found -- Branch Master Details By companyGenId " + companyGenId
							+ "  Method Name" + methodName + " Class : " + this.getClass());
					return response;
				}

			}

		} catch (Exception e) {
			log.error("An error occurred while fetching Branch Master Details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel checkDuplicateInsureDetails(String abbreviation, String insurer) {
	    ResponseModel response = new ResponseModel();

	    if (abbreviation != null && insurer != null) {
	        boolean exists = insurerDetailsRepo.existsByAbbreviationAndInsurer(abbreviation, insurer);
	        response.setMessage(exists
	            ? "Duplicate Found: Abbreviation (" + abbreviation + ") and Insurer (" + insurer + ") already exist."
	            : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK); // org.springframework.http.HttpStatus
	    } else if (abbreviation != null) {
	        boolean exists = insurerDetailsRepo.existsByAbbreviation(abbreviation);
	        response.setMessage(exists
	            ? "Duplicate Found: Abbreviation (" + abbreviation + ") already exists."
	            : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);
	    } else if (insurer != null) {
	        boolean exists = insurerDetailsRepo.existsByInsurer(insurer);
	        response.setMessage(exists
	            ? "Duplicate Found: Insurer (" + insurer + ") already exists."
	            : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);
	    } else {
	        response.setMessage("Error: No search parameter (abbreviation or insurer) was provided.");
	        response.setHttpStatus(HttpStatus.BAD_REQUEST);
	    }

	    response.setData(null);  // or you can include additional data if needed
	    response.setName("CheckDuplicateInsureDetails");

	    return response;
	}


	@Override
	public ResponseModel checkDuplicateUnderwritingOffice(String underwritingOfficeCode, String underwritingOfficeName) {
	    ResponseModel response = new ResponseModel();

	    if (underwritingOfficeCode != null && underwritingOfficeName != null) {
	        boolean exists = underWritingOfficeRepo.existsByUnderwritingOfficeCodeAndUnderwritingOfficeName(
	                underwritingOfficeCode, underwritingOfficeName);
	        response.setMessage(exists
	                ? "Duplicate Found: UnderwritingOfficeCode (" + underwritingOfficeCode + ") and Name (" + underwritingOfficeName + ") already exist."
	                : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);

	    } else if (underwritingOfficeCode != null) {
	        boolean exists = underWritingOfficeRepo.existsByUnderwritingOfficeCode(underwritingOfficeCode);
	        response.setMessage(exists
	                ? "Duplicate Found: UnderwritingOfficeCode (" + underwritingOfficeCode + ") already exists."
	                : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);

	    } else if (underwritingOfficeName != null) {
	        boolean exists = underWritingOfficeRepo.existsByUnderwritingOfficeName(underwritingOfficeName);
	        response.setMessage(exists
	                ? "Duplicate Found: UnderwritingOfficeName (" + underwritingOfficeName + ") already exists."
	                : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);

	    } else {
	        response.setMessage("Error: No parameter (underwritingOfficeCode or underwritingOfficeName) was provided.");
	        response.setHttpStatus(HttpStatus.BAD_REQUEST);
	    }

	    response.setData(null);  // You can attach any relevant data if needed
	    response.setName("CheckDuplicateUnderwritingOffice");

	    return response;
	}
	
	@Override
	public ResponseModel checkDuplicateClaimProcessingOffice(String claimProcessingOfficeCode, String claimProcessingOfficeName) {
	    ResponseModel response = new ResponseModel();

	    if (claimProcessingOfficeCode != null && claimProcessingOfficeName != null) {
	        boolean exists = claimProcessingOfficeRepo.existsByClaimProcessingOfficeCodeAndClaimProcessingOfficeName(
	                claimProcessingOfficeCode, claimProcessingOfficeName);
	        response.setMessage(exists
	            ? "Duplicate Found: ClaimProcessingOfficeCode (" + claimProcessingOfficeCode + ") and Name (" + claimProcessingOfficeName + ") already exist."
	            : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);

	    } else if (claimProcessingOfficeCode != null) {
	        boolean exists = claimProcessingOfficeRepo.existsByClaimProcessingOfficeCode(claimProcessingOfficeCode);
	        response.setMessage(exists
	            ? "Duplicate Found: ClaimProcessingOfficeCode (" + claimProcessingOfficeCode + ") already exists."
	            : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);

	    } else if (claimProcessingOfficeName != null) {
	        boolean exists = claimProcessingOfficeRepo.existsByClaimProcessingOfficeName(claimProcessingOfficeName);
	        response.setMessage(exists
	            ? "Duplicate Found: ClaimProcessingOfficeName (" + claimProcessingOfficeName + ") already exists."
	            : "No Duplicate Found.");
	        response.setHttpStatus(exists ? HttpStatus.CONFLICT : HttpStatus.OK);

	    } else {
	        response.setMessage("Error: No parameter (claimProcessingOfficeCode or claimProcessingOfficeName) was provided.");
	        response.setHttpStatus(HttpStatus.BAD_REQUEST);
	    }

	    response.setData(null);  // Attach relevant data here if needed
	    response.setName("CheckDuplicateClaimProcessingOffice");

	    return response;
	}

	@Override
	public ResponseModel getAllCompanyCars() {
		String methodName = "getAllCompanyCars";
		ResponseModel response = new ResponseModel();
	
		try {
			log.info("Request : Finding All Vehicle Company Detalis by company name "  + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			
			String sql = "select company_id,compnay from insuredb.vehicle_company_details";

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> allVehicleCompanyDetalis = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> vehicleCompanyDetalis = new HashMap<>();
				vehicleCompanyDetalis.put("company_id", row[0]);
				vehicleCompanyDetalis.put("company", (String) row[1]);

				allVehicleCompanyDetalis.add(vehicleCompanyDetalis);
			}
			response.setData(allVehicleCompanyDetalis);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully All Vehicle Company Detalis by company name  " 
					+ "  Method Name" + methodName + " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding All Vehicle Company Detalis by company name  " 
					+ e.getMessage(), "  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;

}

	@Override
	public ResponseModel getAllCarsModel() {
		String methodName = "getVehicleMasterDetails";
		ResponseModel response = new ResponseModel();
		String sql = "";
		try {
			
				
				sql = "select model,variant,cubic_capacity,fule,laden_weight,seating_capacity,unladen_weight from insuredb.vehicle_master_details\r\n";
			

			Query query = entityManager.createNativeQuery(sql);
			// Retrieve the result list
			List<Object[]> results = query.getResultList();
			List<Object> vehicleMasterList = new ArrayList<>();

			// Iterate through the results and extract the necessary details
			for (Object[] row : results) {
				Map<String, Object> vehicleMaster = new HashMap<>();
				vehicleMaster.put("model", row[0]);
				vehicleMaster.put("variant", row[1]);
				vehicleMaster.put("cubic_capacity", row[2]);
				vehicleMaster.put("fule", row[3]);
				vehicleMaster.put("laden_weight", row[4]);
				vehicleMaster.put("seating_capacity", row[5]);
				vehicleMaster.put("unladen_weight", row[6]);
				vehicleMasterList.add(vehicleMaster);
			}
			response.setData(vehicleMasterList);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data get Successfully");

			log.info("Respond : data founded successfully Vehicle Master Details  " + "  Method Name" + methodName
					+ " Class : " + this.getClass());
		} catch (Exception e) {
			log.error("An error occurred while Finding Vehicle Master Details" + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel getCarsDetails(String select) {
	    ResponseModel response = new ResponseModel();
	    List<Map<String, Object>> vehicleMasterList = new ArrayList<>();

	    try {
	        // Step 1: Get all VehicleMasterDetails by type
	        List<VehicleMasterDetails> detailsList = vehicleMasterDetailsRepo.findByType(select);

	        if (detailsList == null || detailsList.isEmpty()) {
	            response.setMessage("No vehicle details found for type: " + select);
	            return response;
	        }

	        // Step 2: Extract all company IDs
	        Set<Long> companyIds = detailsList.stream()
	                .map(VehicleMasterDetails::getCompanyId)
	                .filter(Objects::nonNull)
	                .collect(Collectors.toSet());

	        // Step 3: Bulk fetch all companies
	        List<VehicleCompanyDetails> companyList = vehicleCompanyDetailsRepo.findByCompanyIdIn(companyIds);

	        // Step 4: Map companyId -> CompanyDetails
	        Map<Long, VehicleCompanyDetails> companyMap = companyList.stream()
	                .collect(Collectors.toMap(VehicleCompanyDetails::getCompanyId, c -> c));

	        // Step 5: Build response list
	        for (VehicleMasterDetails details : detailsList) {
	            Map<String, Object> vehicleMap = new HashMap<>();
	            VehicleCompanyDetails compDetails = companyMap.get(details.getCompanyId());

	            vehicleMap.put("model", details.getModel());
	            vehicleMap.put("variant", details.getVariant());
	            vehicleMap.put("cubic_capacity", details.getCubicCapacity());
	            vehicleMap.put("fuel", details.getFule());
	            vehicleMap.put("type", details.getType());
	            vehicleMap.put("unladen_weight", details.getUnladenWeight());
	            vehicleMap.put("laden_weight", details.getLadenWeight());
	            vehicleMap.put("seating_capacity", details.getSeatingCapacity());

	            vehicleMap.put("company", compDetails != null ? compDetails.getCompnay() : null);

	            vehicleMasterList.add(vehicleMap);
	        }

	        // Step 6: Set response
	        response.setHttpStatus(HttpStatus.OK);
	        response.setMessage("Vehicle details fetched successfully");
	        response.setData(vehicleMasterList);

	    } catch (Exception e) {
	        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);

	        response.setMessage("An error occurred while fetching vehicle details: " + e.getMessage());
	    }

	    return response;
	}

	@Override
	public ResponseModel checkDuplicateDOLAndVehicleNo(String vehicleNo, Date dol) {
	    ResponseModel response = new ResponseModel();
	    try {
	        UserSurveyorBasicDetails userCheck = userSurveyorBasicDetailsRepo.findByAssetAndDateOfloss(vehicleNo, dol);

	        if (userCheck != null) {
	            // Duplicate found
	            response.setHttpStatus(HttpStatus.CONFLICT);
	            response.setMessage("Duplicate entry found for Vehicle No: " + vehicleNo + " and Date of Loss: " + dol);
	        } else {
	            // No duplicate
	            response.setHttpStatus(HttpStatus.OK);
	            response.setMessage("No duplicate found for Vehicle No: " + vehicleNo + " and Date of Loss: " + dol);
	        }
	    } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	        response.setMessage("Exception occurred while checking duplicate: " + e.getMessage());
	    }

	    return response;
	}

}
