package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.InsurerDetails;
import com.insuretech.survey.entity.VehicleTypeDetails;

@Repository
public interface VehicleTypeDetailsRepo extends JpaRepository<VehicleTypeDetails,Long>{

	VehicleTypeDetails findByTypeId(Long type);

	VehicleTypeDetails findByTypeIgnoreCase(String type);

	List<VehicleTypeDetails> findByTypeIgnoreCaseContaining(String searchKey);
}
