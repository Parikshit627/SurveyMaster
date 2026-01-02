package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.FuelType;
import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.PolicyDetails;
import com.insuretech.survey.entity.VehicleCompanyDetails;
import com.insuretech.survey.entity.VehicleDetails;
import com.insuretech.survey.entity.VehicleMasterDetails;
import com.insuretech.survey.entity.VehicleModel;
import com.insuretech.survey.entity.VehicleTypeDetails;

@Repository
public interface VehicleModelRepo extends JpaRepository<VehicleModel,Long>{

	

	

	VehicleModel findByModelNameIgnoreCase(String model);

	List<VehicleModel> findByModelNameIgnoreCaseContaining(String searchKey);
	
}
