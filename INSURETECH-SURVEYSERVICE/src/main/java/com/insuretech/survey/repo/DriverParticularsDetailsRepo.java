package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.DriverParticularsDetails;
import com.insuretech.survey.entity.VehicleDetails;

@Repository
public interface DriverParticularsDetailsRepo extends JpaRepository<DriverParticularsDetails, Long>{
//	Change -- AMAN -- Start
	DriverParticularsDetails findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	DriverParticularsDetails findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
//	Change -- AMAN -- End
}
