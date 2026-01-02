package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.CalculationLabourAssessmentPro;
import com.insuretech.survey.entity.CalculationPartAssessmentPro;
import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.PartAssessmentPro;

public interface CalculationLabourAssessmentProRepo extends JpaRepository<CalculationLabourAssessmentPro, Long> {

	CalculationLabourAssessmentPro findByCompanyGenIdAndInsurenceGenId(String companyGenId, Long insurenceGenId);


	
}
