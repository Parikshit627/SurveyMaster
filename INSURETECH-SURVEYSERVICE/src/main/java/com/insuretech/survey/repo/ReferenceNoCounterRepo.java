package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.ReferenceNoCounter;

@Repository
public interface ReferenceNoCounterRepo extends JpaRepository<ReferenceNoCounter,Long>{
	
	ReferenceNoCounter findByRegCode(String regCode);

	ReferenceNoCounter findByCreatedBy(String companyGenId);

}
