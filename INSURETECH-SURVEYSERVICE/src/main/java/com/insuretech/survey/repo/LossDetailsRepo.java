package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.LossDetails;
import com.insuretech.survey.entity.VehicleDetails;

@Repository
public interface LossDetailsRepo extends JpaRepository<LossDetails,Long>{
//	Change -- AMAN -- Start
	LossDetails findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	LossDetails findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
//	Change -- AMAN -- End
}
