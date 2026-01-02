package com.insuretech.survey.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.insuretech.survey.constant.CommonConstants;
import com.insuretech.survey.entity.AiComponentDetails;
import com.insuretech.survey.entity.AiDamages;
import com.insuretech.survey.entity.AiImages;
import com.insuretech.survey.entity.BranchMaster;
import com.insuretech.survey.entity.ClaimProcessingOffice;
import com.insuretech.survey.entity.ExteriorCarParts;
import com.insuretech.survey.entity.FuelType;
import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.UnderwritingOffice;
import com.insuretech.survey.entity.VehicleCompanyDetails;
import com.insuretech.survey.entity.VehicleModel;
import com.insuretech.survey.entity.VehicleTypeDetails;
import com.insuretech.survey.entity.VehicleVariant;
import com.insuretech.survey.entity.WorkShopDetails;
import com.insuretech.survey.model.AIComponentDetailsModel;
import com.insuretech.survey.model.BranchMasterModel;
import com.insuretech.survey.model.CarsAddMasterModel;
import com.insuretech.survey.model.ComponentDetailsModel;
import com.insuretech.survey.model.ExteriorCarPartsModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveDefineDataDetailsModel;
import com.insuretech.survey.service.SaveDefineDataIntimationService;

@Service
public class SaveDefineDataIntimationServiceImpl extends AbstractMasterRepository
		implements SaveDefineDataIntimationService {

	Logger log = LoggerFactory.getLogger(UserSurveyorServiceImpl.class);

	@Override
	public ResponseModel saveDefineDataDetails(SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "saveDefineDataDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Saving Define Data Details for : " + saveDefineDataDetailsModel.getSave_for() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			if (saveDefineDataDetailsModel.getSave_for().equalsIgnoreCase(CommonConstants.SAVE_FOR_INSURER_DATA)) {
				InsurerDetails insurerDetails = new InsurerDetails();
				insurerDetails.setAbbreviation(saveDefineDataDetailsModel.getInsurerAbbreviation());
				insurerDetails.setInsurer(saveDefineDataDetailsModel.getInsurerName());
				insurerDetails.setAddress(
						saveDefineDataDetailsModel.getLocation() + saveDefineDataDetailsModel.getLandMark());
				insurerDetails.setState(saveDefineDataDetailsModel.getState());
				insurerDetails.setCity(saveDefineDataDetailsModel.getDistrict());
				insurerDetails.setPinCode(String.valueOf(saveDefineDataDetailsModel.getPinCode()));
				insurerDetails.setAdded_date(new Timestamp(System.currentTimeMillis()));
				insurerDetailsRepo.save(insurerDetails);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

			} else if (saveDefineDataDetailsModel.getSave_for()
					.equalsIgnoreCase(CommonConstants.SAVE_FOR_UNDER_WRITING_OFFICE)) {
				UnderwritingOffice underwritingOffice = new UnderwritingOffice();
				BeanUtils.copyProperties(saveDefineDataDetailsModel, underwritingOffice);
				underwritingOffice.setUnderwritingOfficeCode(saveDefineDataDetailsModel.getOfficeCode());
				underwritingOffice.setUnderwritingOfficeName(saveDefineDataDetailsModel.getOfficeName());
				underwritingOffice.setAdded_date(new Timestamp(System.currentTimeMillis()));
				underWritingOfficeRepo.save(underwritingOffice);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

			} else if (saveDefineDataDetailsModel.getSave_for()
					.equalsIgnoreCase(CommonConstants.SAVE_FOR_CLAIM_PROCESSIG_OFFICE)) {
				ClaimProcessingOffice claimProcessingOffice = new ClaimProcessingOffice();
				BeanUtils.copyProperties(saveDefineDataDetailsModel, claimProcessingOffice);
				claimProcessingOffice.setClaimProcessingOfficeCode(saveDefineDataDetailsModel.getOfficeCode());
				claimProcessingOffice.setClaimProcessingOfficeName(saveDefineDataDetailsModel.getOfficeName());
				claimProcessingOffice.setAdded_date(new Timestamp(System.currentTimeMillis()));
				claimProcessingOfficeRepo.save(claimProcessingOffice);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");

			} else if (saveDefineDataDetailsModel.getSave_for().equalsIgnoreCase(CommonConstants.SAVE_FOR_WORKSHOP)) {
				WorkShopDetails workshopDetails = new WorkShopDetails();
				BeanUtils.copyProperties(saveDefineDataDetailsModel, workshopDetails);
				workshopDetails.setWorkshopName(saveDefineDataDetailsModel.getOfficeName());
				workshopDetails.setAdded_date(new Timestamp(System.currentTimeMillis()));
				workShopDetailsRepo.save(workshopDetails);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data get Successfully");
			}

			log.info("Respond : data save successfully Define Data Details for  : "
					+ saveDefineDataDetailsModel.getSave_for() + "  Method Name" + methodName + " Class : "
					+ this.getClass());

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for : "
							+ saveDefineDataDetailsModel.getSave_for() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel updateInsurerData(SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "updateInsurerData";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Updating Define Data Details for : " + saveDefineDataDetailsModel.getSave_for() + "  Method Name"
					+ methodName + " Class : " + this.getClass());
			InsurerDetails insurerDetails = insurerDetailsRepo.findById(saveDefineDataDetailsModel.getSr())
					.orElse(null);
			if (insurerDetails != null) {
				insurerDetails.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
//				BeanUtils.copyProperties(saveDefineDataDetailsModel, insurerDetails);
				insurerDetails.setAbbreviation(saveDefineDataDetailsModel.getInsurerAbbreviation());
				insurerDetails.setInsurer(saveDefineDataDetailsModel.getInsurerName());
				insurerDetails.setAddress(saveDefineDataDetailsModel.getLocation());
				insurerDetails.setState(saveDefineDataDetailsModel.getState());
				insurerDetails.setCity(saveDefineDataDetailsModel.getDistrict());
				insurerDetails.setPinCode(String.valueOf(saveDefineDataDetailsModel.getPinCode()));
				insurerDetails.setGstNumber(saveDefineDataDetailsModel.getGstNumber());
				insurerDetailsRepo.save(insurerDetails);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data updated Successfully for " + saveDefineDataDetailsModel.getSave_for());

				log.info("Respond : data updated successfully Define Data Details for  : "
						+ saveDefineDataDetailsModel.getSave_for() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			} else {
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr());
				log.info("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr() + "  Method Name"
						+ methodName + " Class : " + this.getClass());
			}

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for : "
							+ saveDefineDataDetailsModel.getSave_for() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel updateUnderWrittingOffice(SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "updateUnderWrittingOffice";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Updating Define Data Details for : " + saveDefineDataDetailsModel.getSave_for() + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			UnderwritingOffice underwritingOffice = underWritingOfficeRepo.findById(saveDefineDataDetailsModel.getSr())
					.orElse(null);
			if (underwritingOffice != null) {

				underwritingOffice.setDistrict(saveDefineDataDetailsModel.getDistrict());
				underwritingOffice.setGstNumber(saveDefineDataDetailsModel.getGstNumber());
				underwritingOffice.setInsurerAbbreviation(saveDefineDataDetailsModel.getInsurerAbbreviation());
				underwritingOffice.setInsurerName(saveDefineDataDetailsModel.getInsurerName());
				underwritingOffice.setLocation(saveDefineDataDetailsModel.getLocation());
				underwritingOffice.setUnderwritingOfficeCode(saveDefineDataDetailsModel.getOfficeCode());
				underwritingOffice.setUnderwritingOfficeName(saveDefineDataDetailsModel.getOfficeName());
				underwritingOffice.setPinCode(saveDefineDataDetailsModel.getPinCode());
				underwritingOffice.setState(saveDefineDataDetailsModel.getState());
				underwritingOffice.setStateCode(saveDefineDataDetailsModel.getStateCode());
				underwritingOffice.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

				underWritingOfficeRepo.save(underwritingOffice);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data updated Successfully for " + saveDefineDataDetailsModel.getSave_for());

				log.info("Respond : data updated successfully Define Data Details for  : "
						+ saveDefineDataDetailsModel.getSave_for() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			} else {
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr());
				log.info("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr() + "  Method Name"
						+ methodName + " Class : " + this.getClass());
			}

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for : "
							+ saveDefineDataDetailsModel.getSave_for() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel updateClaimProcessing(SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "updateClaimProcessing";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Updating Define Data Details for : " + saveDefineDataDetailsModel.getSave_for() + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			ClaimProcessingOffice claimProcessingOffice = claimProcessingOfficeRepo
					.findById(saveDefineDataDetailsModel.getSr()).orElse(null);
			if (claimProcessingOffice != null) {
				claimProcessingOffice.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
//				BeanUtils.copyProperties(saveDefineDataDetailsModel, claimProcessingOffice);
				claimProcessingOffice.setDistrict(saveDefineDataDetailsModel.getDistrict());
				claimProcessingOffice.setGstNumber(saveDefineDataDetailsModel.getGstNumber());
				claimProcessingOffice.setInsurerAbbreviation(saveDefineDataDetailsModel.getInsurerAbbreviation());
				claimProcessingOffice.setInsurerName(saveDefineDataDetailsModel.getInsurerName());
				claimProcessingOffice.setLocation(saveDefineDataDetailsModel.getLocation());
				claimProcessingOffice.setClaimProcessingOfficeCode(saveDefineDataDetailsModel.getOfficeCode());
				claimProcessingOffice.setClaimProcessingOfficeName(saveDefineDataDetailsModel.getOfficeName());
				claimProcessingOffice.setPinCode(saveDefineDataDetailsModel.getPinCode());
				claimProcessingOffice.setState(saveDefineDataDetailsModel.getState());
				claimProcessingOffice.setStateCode(saveDefineDataDetailsModel.getStateCode());

				claimProcessingOfficeRepo.save(claimProcessingOffice);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data updated Successfully for " + saveDefineDataDetailsModel.getSave_for());

				log.info("Respond : data updated successfully Define Data Details for  : "
						+ saveDefineDataDetailsModel.getSave_for() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			} else {
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setMessage("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr());
				log.info("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr() + "  Method Name"
						+ methodName + " Class : " + this.getClass());
			}

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for : "
							+ saveDefineDataDetailsModel.getSave_for() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel updateWorkshop(SaveDefineDataDetailsModel saveDefineDataDetailsModel) {
		String methodName = "updateWorkshop";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Updating Define Data Details for : " + saveDefineDataDetailsModel.getSave_for() + "  Method Name"
					+ methodName + " Class : " + this.getClass());

			WorkShopDetails workshopDetails = workShopDetailsRepo.findById(saveDefineDataDetailsModel.getSr())
					.orElse(null);
			if (workshopDetails != null) {
				workshopDetails.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
//				BeanUtils.copyProperties(saveDefineDataDetailsModel, workshopDetails);
				workshopDetails.setDistrict(saveDefineDataDetailsModel.getDistrict());
				workshopDetails.setGstNumber(saveDefineDataDetailsModel.getGstNumber());
				workshopDetails.setLocation(saveDefineDataDetailsModel.getLocation());
				workshopDetails.setWorkshopName(saveDefineDataDetailsModel.getOfficeName());
				workshopDetails.setPinCode(saveDefineDataDetailsModel.getPinCode());
				workshopDetails.setState(saveDefineDataDetailsModel.getState());
				workshopDetails.setStateCode(saveDefineDataDetailsModel.getStateCode());
				workShopDetailsRepo.save(workshopDetails);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data updated Successfully for " + saveDefineDataDetailsModel.getSave_for());

				log.info("Respond : data updated successfully Define Data Details for  : "
						+ saveDefineDataDetailsModel.getSave_for() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			} else {
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr());
				log.info("No data found in Db against sr : " + saveDefineDataDetailsModel.getSr() + "  Method Name"
						+ methodName + " Class : " + this.getClass());
			}

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for : "
							+ saveDefineDataDetailsModel.getSave_for() + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel saveExteriorCarParts(List<ExteriorCarPartsModel> exteriorCarPartsModelList) {
		String methodName = "saveExteriorCarParts";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Request : save Exterior Car Parts Details  " + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			for (ExteriorCarPartsModel exteriorCarPartsModel : exteriorCarPartsModelList) {
				String partName = "";
				Long vehicleTypeId = 0L;
				ExteriorCarParts exteriorCarParts = null;

				if (exteriorCarPartsModel.getPartName() != null && exteriorCarPartsModel.getVehicleTypeId() != 0) {
					partName = exteriorCarPartsModel.getPartName();
					vehicleTypeId = exteriorCarPartsModel.getVehicleTypeId();
					exteriorCarParts = partsMasterDetailsRepo.findByPartNameAndVehicleTypeId(partName.trim(),
							vehicleTypeId);
				}

				if (exteriorCarParts == null) {
					exteriorCarParts = new ExteriorCarParts();
				}
				BeanUtils.copyProperties(exteriorCarPartsModel, exteriorCarParts);

				VehicleTypeDetails vehicleTypeDetails = vehicleTypeDetailsRepo.findByTypeId(vehicleTypeId);
				if (vehicleTypeDetails != null) {
					exteriorCarParts.setVehicleTypeName(vehicleTypeDetails.getType());
				}
				partsMasterDetailsRepo.save(exteriorCarParts);

			}
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("Data saved Successfully ");

			log.info("Respond : data saved successfully Exterior Car Parts Details " + "  Method Name" + methodName
					+ " Class : " + this.getClass());

		} catch (Exception e) {
			log.error("An error occurred while Saving Exterior Car Parts Details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel saveBranchMasterDetails(BranchMasterModel branchMasterModel) {
		String methodName = "saveBranchMasterDetails";
		ResponseModel response = new ResponseModel();
		try {
			if (branchMasterModel == null || branchMasterModel.getCompanyGenId() == null
					|| branchMasterModel.getCompanyGenId().trim().equals("")
					|| branchMasterModel.getBrancheEmail() == null
					|| branchMasterModel.getBrancheEmail().trim().equals("")) {

				response.setHttpStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("Invaid paramater");

				log.info("Respond : Invalid Parameter " + "  Method Name" + methodName + " Class : " + this.getClass());

				return response;
			} else {
				log.info("Request : save Branch Master Details By companyGenId : " + branchMasterModel.getCompanyGenId()
						+ "  Method Name" + methodName + " Class : " + this.getClass());

				BranchMaster branchMaster = branchMasterRepo.findByBrancheEmailAndCompanyGenId(
						branchMasterModel.getBrancheEmail(), branchMasterModel.getCompanyGenId());
				if (branchMaster == null) {
					branchMaster = new BranchMaster();
					BeanUtils.copyProperties(branchMasterModel, branchMaster, "branchGenId");
					branchMaster.setCreatedDtm(new Timestamp(System.currentTimeMillis()));

					branchMasterRepo.saveAndFlush(branchMaster);

					response.setHttpStatus(HttpStatus.OK);
					response.setData(branchMaster);
					response.setMessage("Data saved Successfully");

					log.info("Respond : data saved successfully Branch Master Details By companyGenId : "
							+ branchMasterModel.getCompanyGenId() + "  Method Name" + methodName + " Class : "
							+ this.getClass());

				} else {

					response.setHttpStatus(HttpStatus.BAD_REQUEST);
					response.setMessage("Branch with email id already registered");

					log.info("Respond :Branch with email id already registered, email id "
							+ branchMasterModel.getBrancheEmail() + "  Method Name" + methodName + " Class : "
							+ this.getClass());
					return response;
				}

			}

		} catch (Exception e) {
			log.error("An error occurred whileBranch Master Details " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel updateBranchMasterDetails(BranchMasterModel branchMasterModel) {
		String methodName = "updateBranchMasterDetails";
		ResponseModel response = new ResponseModel();
		try {
			log.info("Updating Define Data Details for Branch Master by BranchGenId : "
					+ branchMasterModel.getBranchGenId() + "  Method Name" + methodName + " Class : "
					+ this.getClass());

			BranchMaster branchMaster = branchMasterRepo.findById(branchMasterModel.getBranchGenId()).orElse(null);
			if (branchMaster != null) {
				branchMaster.setUpdatedDtm(new Timestamp(System.currentTimeMillis()));
//				branchMaster.setBranchName(branchMasterModel.getBranchName());
//				branchMaster.setBranchAbbreviation(branchMasterModel.getBranchAbbreviation());
				branchMaster.setBranchAddress(branchMasterModel.getBranchAddress());
//				branchMaster.setBrancheEmail(branchMasterModel.getBrancheEmail());
				branchMaster.setDistrict(branchMasterModel.getDistrict());
				branchMaster.setState(branchMasterModel.getState());
				branchMaster.setPinCode(branchMasterModel.getPinCode());
				branchMaster.setLocation(branchMasterModel.getLocation());
				branchMaster.setStateCode(branchMasterModel.getStateCode());
				branchMaster.setContactNumbers(branchMasterModel.getContactNumbers());
				branchMasterRepo.save(branchMaster);

				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("Data updated Successfully for Branch Master");

				log.info("Respond : data updated successfully Define Data Details for  branch Master by BranchGenId : "
						+ branchMasterModel.getBranchGenId() + "  Method Name" + methodName + " Class : "
						+ this.getClass());
			} else {
				response.setHttpStatus(HttpStatus.OK);
				response.setMessage("No data found in Db against BranchGenId : " + branchMasterModel.getBranchGenId());
				log.info("No data found in Db against BranchGenId : " + branchMasterModel.getBranchGenId()
						+ "  Method Name" + methodName + " Class : " + this.getClass());
			}

		} catch (Exception e) {
			log.error(
					"An error occurred while Saving Define Data Details for Branch Master -- BranchGenId: "
							+ branchMasterModel.getBranchGenId() + ", " + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
		return response;
	}

	@Override
	public ResponseModel addCarsMaster(CarsAddMasterModel carsAddMasterModel) {
		ResponseModel response = new ResponseModel();
		try {
			// 1️⃣ Company
			VehicleCompanyDetails carCompany = vehicleCompanyDetailsRepo
					.findByCompnayIgnoreCase(carsAddMasterModel.getCompany());
			if (carCompany == null) {
				VehicleCompanyDetails newComp = new VehicleCompanyDetails();
				newComp.setCompnay(carsAddMasterModel.getCompany());
				carCompany = vehicleCompanyDetailsRepo.save(newComp); // ✅ save the new one
			}
			Long companyId = carCompany.getCompanyId();

			// 2️⃣ Model
			VehicleModel carModel = vehicleModelRepo.findByModelNameIgnoreCase(carsAddMasterModel.getModel());
			if (carModel == null) {
				VehicleModel newModel = new VehicleModel();
				newModel.setModelName(carsAddMasterModel.getModel());
				newModel.setCompanyId(companyId);
				carModel = vehicleModelRepo.save(newModel);
			}
			Long modelId = carModel.getModelId();

			// 3️⃣ Type
			VehicleTypeDetails carType = vehicleTypeDetailsRepo.findByTypeIgnoreCase(carsAddMasterModel.getType());
			if (carType == null) {
				VehicleTypeDetails newType = new VehicleTypeDetails();
				newType.setType(carsAddMasterModel.getType());
				carType = vehicleTypeDetailsRepo.save(newType);
			}
			Long typeId = carType.getTypeId();

			// 4️⃣ Fuel
			FuelType carFuel = fuelTypeRepo.findByFuelNameIgnoreCase(carsAddMasterModel.getFule());
			if (carFuel == null) {
				FuelType newFuel = new FuelType();
				newFuel.setFuelName(carsAddMasterModel.getFule());
				carFuel = fuelTypeRepo.save(newFuel);
			}
			Long fuelId = carFuel.getFuelId();

			// 5️⃣ Variant
			Optional<VehicleVariant> carVariant = vehicleVariantRepo
					.findByVariantNameIgnoreCaseAndTypeIdAndFuelIdAndModelId(carsAddMasterModel.getVariant(), typeId,
							fuelId, modelId);

			if (carVariant.isPresent()) {
				response.setMessage("Car variant already registered. Car Name: " + carsAddMasterModel.getModel()
						+ " | Variant: " + carsAddMasterModel.getVariant());
				response.setHttpStatus(HttpStatus.CONFLICT); // 409 -> already exists
			} else {
				VehicleVariant newVariant = new VehicleVariant();
				newVariant.setVariantName(carsAddMasterModel.getVariant());
				newVariant.setCubicCapacity(carsAddMasterModel.getCubicCapacity());
				newVariant.setLadenWeight(carsAddMasterModel.getLadenWeight());
				newVariant.setSeatingCapacity(carsAddMasterModel.getSeatingCapacity());
				newVariant.setUnladenWeight(carsAddMasterModel.getUnladenWeight());
				newVariant.setCompanyId(companyId);
				newVariant.setFuelId(fuelId);
				newVariant.setTypeId(typeId);
				newVariant.setModelId(modelId);

				vehicleVariantRepo.save(newVariant);

				response.setMessage("Car added successfully. Car Name: " + carsAddMasterModel.getModel()
						+ " | Variant: " + carsAddMasterModel.getVariant());
				response.setHttpStatus(HttpStatus.OK);
			}

			// 6️⃣ (Optional) Save main car record with all IDs

		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Error: " + e.getMessage());
			return response;
		}
		return response;
	}

	@Override
	public ResponseModel getCarsByType(String type) {
		ResponseModel response = new ResponseModel();

		try {
			// 1️⃣ Get the type details
			VehicleTypeDetails typeDetails = vehicleTypeDetailsRepo.findByTypeIgnoreCase(type);

			if (typeDetails == null) {
				response.setMessage("No cars found for type: " + type);
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				return response;
			}

			// 2️⃣ Get all variants for this type
			List<VehicleVariant> variants = vehicleVariantRepo.findByTypeId(typeDetails.getTypeId());

			if (variants.isEmpty()) {
				response.setMessage("No variants found for type: " + type);
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				return response;
			}

			// 3️⃣ Convert each variant into a DTO for output
			List<CarsAddMasterModel> cars = new ArrayList();
			for (VehicleVariant variant : variants) {

				// Fetch related entities by IDs
				VehicleModel model = vehicleModelRepo.findById(variant.getModelId()).orElse(null);
				VehicleCompanyDetails company = vehicleCompanyDetailsRepo.findById(variant.getCompanyId()).orElse(null);
				FuelType fuel = fuelTypeRepo.findById(variant.getFuelId()).orElse(null);

				// Create DTO object
				CarsAddMasterModel dto = new CarsAddMasterModel();
				dto.setCompany(company != null ? company.getCompnay() : null);
				dto.setModel(model != null ? model.getModelName() : null);
				dto.setType(typeDetails.getType());
				dto.setFule(fuel != null ? fuel.getFuelName() : null);
				dto.setVariant(variant.getVariantName());
				dto.setCubicCapacity(variant.getCubicCapacity());
				dto.setLadenWeight(variant.getLadenWeight());
				dto.setUnladenWeight(variant.getUnladenWeight());
				dto.setSeatingCapacity(variant.getSeatingCapacity());

				// Add to result list
				cars.add(dto);
			}

			// 4️⃣ Prepare success response
			response.setMessage("Cars fetched successfully for type: " + type);
			response.setData(cars);
			response.setHttpStatus(HttpStatus.OK);

		} catch (Exception e) {
			// 5️⃣ Handle unexpected errors
			e.printStackTrace();
			response.setMessage("Error: " + e.getMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@Override
	public ResponseModel getViewAllModelTypeVarient(String param) {
		ResponseModel response = new ResponseModel();

		try {
			String searchKey = param.trim().toLowerCase();

			// If user explicitly types keyword "fuel" → return all fuels
			if (searchKey.contains("fuel")) {
				List<FuelType> fuels = fuelTypeRepo.findAll();
				response.setData(fuels);
				response.setMessage("All fuel list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// If user explicitly types keyword "type" → return all types
			if (searchKey.contains("type")) {
				List<VehicleTypeDetails> types = vehicleTypeDetailsRepo.findAll();
				response.setData(types);
				response.setMessage("All type list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// If user explicitly types keyword "company" → return all companies
			if (searchKey.contains("company")) {
				List<VehicleCompanyDetails> companies = vehicleCompanyDetailsRepo.findAll();
				response.setData(companies);
				response.setMessage("All company list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// If user explicitly types keyword "model" → return all models
//	        if (searchKey.contains("model")) {
//	            List<VehicleModel> models = vehicleModelRepo.findAll();
//	            response.setData(models);
//	            response.setMessage("All model list found");
//	            response.setHttpStatus(HttpStatus.OK);
//	            return response;
//	        }

			// If user explicitly types keyword "variant" → return all variants
//	        if (searchKey.contains("variant")) {
//	            List<VehicleVariant> variants = vehicleVariantRepo.findAll();
//	            response.setData(variants);
//	            response.setMessage("All variant list found");
//	            response.setHttpStatus(HttpStatus.OK);
//	            return response;
//	        }

			// Otherwise → search across tables but stop at first match
			List<Map<String, String>> fuels = fuelTypeRepo.findByFuelNameIgnoreCaseContaining(searchKey).stream()
					.map(fuel -> Map.of("fuelName", fuel.getFuelName())).toList();
			if (!fuels.isEmpty()) {
				response.setData(fuels);
				response.setMessage("Fuel list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// 2️⃣ Type
			List<Map<String, String>> types = vehicleTypeDetailsRepo.findByTypeIgnoreCaseContaining(searchKey).stream()
					.map(type -> Map.of("type", type.getType())).toList();
			if (!types.isEmpty()) {
				response.setData(types);
				response.setMessage("Type list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// 3️⃣ Company
			List<Map<String, String>> companies = vehicleCompanyDetailsRepo.findByCompnayIgnoreCaseContaining(searchKey)
					.stream().map(company -> Map.of("companyName", company.getCompnay())).toList();
			if (!companies.isEmpty()) {
				response.setData(companies);
				response.setMessage("Company list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// 4️⃣ Model
			List<Map<String, String>> models = vehicleModelRepo.findByModelNameIgnoreCaseContaining(searchKey).stream()
					.map(model -> Map.of("modelName", model.getModelName())).toList();
			if (!models.isEmpty()) {
				response.setData(models);
				response.setMessage("Model list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// 5️⃣ Variant
			List<Map<String, String>> variants = vehicleVariantRepo.findByVariantNameIgnoreCaseContaining(searchKey)
					.stream().map(variant -> Map.of("variantName", variant.getVariantName())).distinct() // 🔥 removes
																											// duplicates
					.toList();

			if (!variants.isEmpty()) {
				response.setData(variants);
				response.setMessage("Variant list found");
				response.setHttpStatus(HttpStatus.OK);
				return response;
			}

			// ❌ Nothing found
			response.setMessage("No records found for param: " + param);
			response.setHttpStatus(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			response.setMessage("Error occurred while fetching details: " + e.getMessage());
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	

}
