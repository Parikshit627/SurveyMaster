package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.FuelType;
import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.PolicyDetails;
import com.insuretech.survey.entity.VehicleDetails;
import com.insuretech.survey.entity.VehicleModel;
import com.insuretech.survey.entity.VehicleVariant;

@Repository
public interface VehicleVariantRepo extends JpaRepository<VehicleVariant,Long>{



//	VehicleVariant findByVariantNameIgnoreCase(String variant);

	List<VehicleVariant> findByTypeId(long typeId);


	List<VehicleVariant> findByVariantNameIgnoreCaseContaining(String searchKey);

	Optional<VehicleVariant> findByVariantNameIgnoreCaseAndTypeIdAndFuelIdAndModelId(String variant, Long typeId,
			Long fuelId, Long modelId);
	
}
