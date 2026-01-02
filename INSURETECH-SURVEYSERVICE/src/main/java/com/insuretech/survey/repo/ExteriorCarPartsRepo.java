package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.ExteriorCarParts;

@Repository
public interface ExteriorCarPartsRepo extends JpaRepository<ExteriorCarParts,Long>{
	
	@Query("SELECT e FROM ExteriorCarParts e WHERE e.vehicleTypeId =:vehicleTypeId AND  LOWER(e.partName) = LOWER(:partName)")
	ExteriorCarParts findByPartNameAndVehicleTypeId(@Param("partName") String partName,@Param("vehicleTypeId") Long vehicleTypeId);


}
