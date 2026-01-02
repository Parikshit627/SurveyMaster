package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Conclusion;
import com.insuretech.survey.entity.MetalCalculation;

@Repository
public interface MetalCalculationRepository extends JpaRepository<MetalCalculation, Long>{

	MetalCalculation findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
	
}
