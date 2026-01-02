package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.VehicleMasterDetails;

@Repository
public interface VehicleMasterDetailsRepo extends JpaRepository<VehicleMasterDetails,Long>{

	List<VehicleMasterDetails> findByType(String select);

    
	
}
