package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.MasterDocumentRequired;

@Repository
public interface MasterDocumentRequiredRepo extends JpaRepository<MasterDocumentRequired, Long>{
	
	List<MasterDocumentRequired> findByVehicleTypeIgnoreCaseAndActiveTrue(String vehicleType);



}
