package com.insuretech.survey.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.BranchMaster;

@Repository
public interface BranchMasterRepo extends JpaRepository<BranchMaster,Long> {
	
	BranchMaster findByBrancheEmailAndCompanyGenId(String brancheEmail,String companyGenId);

}
