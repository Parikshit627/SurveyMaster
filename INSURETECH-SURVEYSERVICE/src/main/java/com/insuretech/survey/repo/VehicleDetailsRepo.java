package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.PolicyDetails;
import com.insuretech.survey.entity.VehicleDetails;

@Repository
public interface VehicleDetailsRepo extends JpaRepository<VehicleDetails,Long>{
//	Change -- AMAN -- Start
	VehicleDetails findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	VehicleDetails findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
//	Change -- AMAN -- End
	VehicleDetails findByInsurenceGenIdOrRegNo(Long insuranceGenId, String regNo);
	VehicleDetails findByRegNo(String regNo);
	VehicleDetails findByInsurenceGenId(Long insuranceClaimId);
}
