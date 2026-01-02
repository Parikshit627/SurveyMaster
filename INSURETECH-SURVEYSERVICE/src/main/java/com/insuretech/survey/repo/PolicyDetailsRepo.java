package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.PolicyDetails;

@Repository
public interface PolicyDetailsRepo extends JpaRepository<PolicyDetails, Long>{

//	Change -- AMAN -- Start
	PolicyDetails findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);

//	PolicyDetails findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
//	Change -- AMAN -- End
}
