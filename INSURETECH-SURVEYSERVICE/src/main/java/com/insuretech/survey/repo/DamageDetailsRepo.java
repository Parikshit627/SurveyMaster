package com.insuretech.survey.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.DamageDetails;

@Repository
public interface DamageDetailsRepo extends JpaRepository<DamageDetails,Long>{

//	Change -- Aman -- Start
	List<DamageDetails> findByInsurenceGenIdAndCompanyGenId(Long insurenceGenId,String companyGenId);
//	List<DamageDetails> findByInsurenceGenIdAndCompanyGenIdAndWorkedByUserId(Long insurenceGenId,String companyGenId,String workedByUserId);
//	Change -- Aman -- End
	
	@Query("SELECT a.partsName as column1,a.description as column2 FROM DamageDetails a WHERE a.insurenceGenId=:insurenceGenId AND a.companyGenId=:companyGenId")
	List<Map<String,Object>> findPartNameAndDescByInsurenceGenIdAndCompanyGenId(@Param("insurenceGenId")Long insurenceGenId,@Param("companyGenId")String companyGenId);

	    //  Change -- Aman -- Start -04-09-25 
	List<DamageDetails> findByInsurenceGenId(Long insurenceGenId);
	    //  Change -- Aman -- End -04-09-25 
}
