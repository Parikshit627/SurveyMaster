package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.AssemblyDetails;
import com.insuretech.survey.entity.LabourDetails;
import com.insuretech.survey.entity.MetalType;

@Repository
public interface MetalTypeRepo extends JpaRepository<MetalType,Long>{

	MetalType findByCode(String metalCode);

	@Query("SELECT m.code FROM MetalType m")
	List<String> findAllCode();


	
	
	
	
}
