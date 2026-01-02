package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.ClaimProcessingOffice;

public interface ClaimProcessingOfficeRepo extends JpaRepository<ClaimProcessingOffice, Integer> {
	 boolean existsByClaimProcessingOfficeCodeAndClaimProcessingOfficeName(String claimProcessingOfficeCode, String claimProcessingOfficeName);
	    boolean existsByClaimProcessingOfficeCode(String claimProcessingOfficeCode);
	    boolean existsByClaimProcessingOfficeName(String claimProcessingOfficeName);
}
