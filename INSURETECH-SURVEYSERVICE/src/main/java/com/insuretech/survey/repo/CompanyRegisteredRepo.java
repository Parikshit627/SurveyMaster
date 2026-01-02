package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import com.insuretech.survey.entity.CompanyRegistered;

public interface CompanyRegisteredRepo extends JpaRepository<CompanyRegistered, Long> {


	 CompanyRegistered findByCompanyGenId(String companyGenId);

//	 @Query("SELECT c.signName AS signName, c.signUUID AS signUUID FROM CompanyRegistered c WHERE c.companyGenId = :companyGenId")
//	 SignDetailsProjection findSignDetailsByCompanyGenId(@Param("companyGenId") String companyGenId);
}
