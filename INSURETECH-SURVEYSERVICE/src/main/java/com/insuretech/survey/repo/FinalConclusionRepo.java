package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Conclusion;
import com.insuretech.survey.entity.FinalConclusion;

@Repository
public interface FinalConclusionRepo extends JpaRepository<FinalConclusion, Long>{

	FinalConclusion findBySr(int i);

	FinalConclusion findByInsurenceGenId(Long insurenceGenId);


	
}
