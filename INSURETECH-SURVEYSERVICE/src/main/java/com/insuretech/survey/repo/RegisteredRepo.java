package com.insuretech.survey.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insuretech.survey.entity.Registered;
import com.insuretech.survey.model.ResponseModel;

public interface RegisteredRepo extends JpaRepository<Registered, Long> {

	Registered findByEmailId(String emailId);

	Registered findByPhoneNo(long phoneNo);

	Registered findByUserName(String userName);

	Registered findByUserNameAndCompanyRegGenId(String userName, String companyRegGenId );
	
	List<Registered> findByRegAbbreviationIgnoreCase(String regAbbreviation);
	
	Registered findByCompanyRegGenIdAndRegCodeIsNotNull(String companyRegGenId);

	Registered findByCompanyRegGenId(String companyRegGenId);


		
	 @Query(value = "SELECT a.full_name AS column1, a.email_id AS column2 " +
             "FROM insuredb.registered a " +  
             "WHERE a.company_reg_gen_id = :companyGenId " +  
             "AND a.registration_by = :companyGenId",  
     nativeQuery = true)
List<Map<String, Object>> findByCompanyRegGenIdAndRegistrationBy(@Param("companyGenId") String companyGenId);

//	List<Object[]> findByCompanyRegGenIdAndRegistrationBy(String companyGenId, String companyGenId2);

}
