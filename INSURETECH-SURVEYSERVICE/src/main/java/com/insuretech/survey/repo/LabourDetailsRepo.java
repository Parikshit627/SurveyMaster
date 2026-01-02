package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.AssemblyDetails;
import com.insuretech.survey.entity.LabourDetails;

@Repository
public interface LabourDetailsRepo extends JpaRepository<LabourDetails,Long>{
	
	List<LabourDetails> findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	List<LabourDetails> findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
	
//	Change -- AMAN -- Start
//	List<LabourDetails> findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserIdOrderByOrderSrAsc(
//		    Long insurenceGenId,
//		    String companyGenId,
//		    String workedByUserId
//		);
	
	List<LabourDetails> findByInsurenceGenIdAndCompanyGenIdOrderByOrderSrAsc(Long insurenceGenId, String companyGenId);
//	Change -- AMAN -- End
	Optional<LabourDetails> findByInsurenceGenIdAndLabourPartsNameIgnoreCase(Long insurenceGenId, String assemblyName);


}
