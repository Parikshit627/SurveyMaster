package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.LinkGeneratedHistory;

public interface LinkGeneratedHistoryRepo extends JpaRepository<LinkGeneratedHistory, Long> {

	List<LinkGeneratedHistory> findByInsuranceClaimIdAndCompanyGenId(Long insuranceClaimId, String companyGenId);
	
}
