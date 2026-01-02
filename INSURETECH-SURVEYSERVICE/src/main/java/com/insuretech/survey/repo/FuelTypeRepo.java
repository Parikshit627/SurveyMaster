package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.FuelType;
import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.PolicyDetails;
import com.insuretech.survey.entity.VehicleDetails;

@Repository
public interface FuelTypeRepo extends JpaRepository<FuelType,Long>{

	FuelType findByFuelNameIgnoreCase(String fule);

	List<FuelType> findByFuelNameIgnoreCaseContaining(String searchKey);
	
}
