package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.UnderwritingOffice;

public interface UnderWritingOfficeRepo extends JpaRepository<UnderwritingOffice, Integer> {
	
	
	 boolean existsByUnderwritingOfficeCodeAndUnderwritingOfficeName(String underwritingOfficeCode, String underwritingOfficeName);
	    boolean existsByUnderwritingOfficeCode(String underwritingOfficeCode);
	    boolean existsByUnderwritingOfficeName(String underwritingOfficeName);
}
