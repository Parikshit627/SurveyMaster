package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Conclusion;

@Repository
public interface ConclusionRepo extends JpaRepository<Conclusion, Long>{

//	Change -- Aman -- Start
	Conclusion findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	Conclusion findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
//	Change -- Aman -- End
}
