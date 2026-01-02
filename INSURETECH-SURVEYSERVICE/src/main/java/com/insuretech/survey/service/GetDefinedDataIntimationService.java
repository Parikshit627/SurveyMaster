package com.insuretech.survey.service;

import java.sql.Date;

import com.insuretech.survey.model.ResponseModel;

public interface GetDefinedDataIntimationService {

	ResponseModel getInsurerAllDetails(String abbreviation);

	ResponseModel getAllClaimProcessingOffice(String officeCode);

	ResponseModel getAllUnderWritingOffice(String officeCode);

	ResponseModel getAllWorkshopDetails(String workShopName);
	
	ResponseModel getVehicleMasterDetails(String companyId, String type, String model);
	
	ResponseModel getAllVehicleCompanyDetails(String company);
	
	ResponseModel getAllVehicleTypeDetails(String type);
	
	ResponseModel getAllExteriorCarParts(String partName,Long vehicleTypeId);
	
	ResponseModel getFinalDocumentSubmission(String companyGenId,Long insurenceGenId,String docName);
	
	ResponseModel getBranchMasterDetails(String companyGenId,String branchName);

	ResponseModel checkDuplicateInsureDetails(String param, String insurer);

	ResponseModel checkDuplicateUnderwritingOffice(String underwritingOfficeCode, String underwritingOfficeName);

	ResponseModel checkDuplicateClaimProcessingOffice(String claimProcessingOfficeCode, String claimProcessingOfficeName);

	ResponseModel getAllCompanyCars();

	ResponseModel getAllCarsModel();

	ResponseModel getCarsDetails(String select);

	ResponseModel checkDuplicateDOLAndVehicleNo(String vehicleNo, Date dol);
	

}
