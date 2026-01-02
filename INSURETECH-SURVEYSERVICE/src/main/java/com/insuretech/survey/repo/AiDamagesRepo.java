package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.AiDamages;

@Repository
public interface AiDamagesRepo extends JpaRepository<AiDamages,Long> {
	
	List<AiDamages> findByAiComponentGenId(Long aiComponentGenId); 
  
}
