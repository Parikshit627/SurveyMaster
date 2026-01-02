package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.FinalDocumentSubmission;
import com.insuretech.survey.entity.LabourAssessmentPro;
import com.insuretech.survey.entity.PartAssessmentPro;

public interface LabourAssessmentProRepo extends JpaRepository<LabourAssessmentPro, Long> {

	LabourAssessmentPro findByCompanyGenIdAndInsurenceGenIdAndId(String companyGenId, Long insurenceGenId, Long id);


	List<LabourAssessmentPro> findByCompanyGenIdAndInsurenceGenIdOrderBySNo(String companyGenId, Long insurenceGenId);


	LabourAssessmentPro findByPartsNameAndInsurenceGenId(String partName, Long insurenceGenId);


	
	
}
