package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.EmailHistory;

@Repository
public interface EmailHistoryRepo extends JpaRepository<EmailHistory,Long>{
	
	@Query(value="SELECT a FROM EmailHistory a WHERE referenceNo=:referenceNo AND a.status = 'success'")
	List<EmailHistory> findByReferenceNo(String referenceNo);
	
	
}
