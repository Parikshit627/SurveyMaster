package com.insuretech.survey.service;

import java.util.List;

import com.insuretech.survey.model.AIComponentDetailsModel;
import com.insuretech.survey.model.BranchMasterModel;
import com.insuretech.survey.model.CarsAddMasterModel;
import com.insuretech.survey.model.ComponentDetailsModel;
import com.insuretech.survey.model.ExteriorCarPartsModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.model.SaveDefineDataDetailsModel;

public interface SaveDefineDataIntimationService {

	ResponseModel saveDefineDataDetails(SaveDefineDataDetailsModel saveDefineDataDetailsModel);

	ResponseModel updateInsurerData(SaveDefineDataDetailsModel saveDefineDataDetailsModel);

	ResponseModel updateUnderWrittingOffice(SaveDefineDataDetailsModel saveDefineDataDetailsModel);

	ResponseModel updateClaimProcessing(SaveDefineDataDetailsModel saveDefineDataDetailsModel);

	ResponseModel updateWorkshop(SaveDefineDataDetailsModel saveDefineDataDetailsModel);
	
	ResponseModel saveExteriorCarParts(List<ExteriorCarPartsModel> ExteriorCarPartsModel);
	
	ResponseModel saveBranchMasterDetails(BranchMasterModel branchMasterModel);
	
	ResponseModel updateBranchMasterDetails(BranchMasterModel branchMasterModel);

	ResponseModel addCarsMaster(CarsAddMasterModel carsAddMasterModel);

	ResponseModel getCarsByType(String type);

	ResponseModel getViewAllModelTypeVarient(String param);
	
	
	
	
	
	
   
}
