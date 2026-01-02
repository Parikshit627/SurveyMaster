package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.AssemblyDetails;
import com.insuretech.survey.entity.LossDetails;

@Repository
public interface AssemblyDetailsRepo extends JpaRepository<AssemblyDetails,Long>{
	List<AssemblyDetails> findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	List<AssemblyDetails> findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);

//	Change -- AMAN -- Start
	List<AssemblyDetails> findByInsurenceGenIdAndCompanyGenIdOrderByOrderSrAsc(Long insurenceGenId,
			String companyGenId);
	
//	List<AssemblyDetails> findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserIdOrderByOrderSrAsc(
//		    Long insurenceGenId,
//		    String companyGenId,
//		    String workedByUserId
//		);

//	Change -- AMAN -- End
	
	AssemblyDetails findBySrAndInsurenceGenIdAndCompanyGenId(Long sr, Long insurenceGenId, String companyGenId);

//	Change -- AMAN -- Start --- 02-09-2025
	@Query("SELECT assemblyName, estimated, assessed FROM AssemblyDetails p " +
		       "WHERE p.insurenceGenId = :insurenceGenId")
	List<Object[]> findByInsurenceGenId(@Param("insurenceGenId") Long insurenceGenId);
//	Change -- AMAN -- End --- 02-09-2025

}
