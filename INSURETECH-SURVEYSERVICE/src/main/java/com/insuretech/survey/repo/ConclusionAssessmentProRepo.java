package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Conclusion;
import com.insuretech.survey.entity.ConclusionAssessmentPro;
import com.insuretech.survey.entity.FinalConclusion;

@Repository
public interface ConclusionAssessmentProRepo extends JpaRepository<ConclusionAssessmentPro, Long>{

	ConclusionAssessmentPro findByCompanyGenIdAndInsurenceGenId(String companyGenId, Long insurenceGenId);

	
}
