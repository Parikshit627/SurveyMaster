package com.insuretech.survey.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insuretech.survey.entity.PartAssessmentPro;

public interface PartAssessmentProRepo extends JpaRepository<PartAssessmentPro, Long> {

//	PartAssessmentPro findByCompanyGenIdAndInsurenceGenId(String companyGenId, Long insurenceGenId);



	PartAssessmentPro findByCompanyGenIdAndInsurenceGenIdAndId(String companyGenId, Long insurenceGenId, Long id);

//	List<PartAssessmentPro> findByCompanyGenIdAndInsurenceGenId(String companyGenId, Long insurenceGenId);

	Optional<PartAssessmentPro> findByIdAndInsurenceGenId(Long id, Long insurenceGenId);

	List<PartAssessmentPro> findByCompanyGenIdAndInsurenceGenIdAndIsSubRowOrderBySNo(String companyGenId, Long insurenceGenId,
			boolean b);
//
//	List<PartAssessmentPro> findByCompanyGenIdAndInsurenceGenIdAndIsSubRowAndParentSNoOrderBySubRowIndex(
//			String companyGenId, Long insurenceGenId, boolean b, Integer sabRowNo);

	List<PartAssessmentPro> findByCompanyGenIdAndInsurenceGenIdAndIsSubRowAndParentSNoOrderBySNo(String companyGenId,
			Long insurenceGenId, boolean b, Integer sabRowNo);

	List<PartAssessmentPro> findByCompanyGenIdAndInsurenceGenIdOrderBySNo(String companyGenId, Long insurenceGenId);


	@Query("SELECT p FROM PartAssessmentPro p " +
		       "WHERE p.insurenceGenId = :insurenceGenId " +
		       "AND str(p.sNo) LIKE CONCAT(:prefix, '%')")
		List<PartAssessmentPro> findChildrenByPrefix(@Param("insurenceGenId") Long insurenceGenId,
		                                             @Param("prefix") String prefix);
	// List<PartAssessmentPro> findByCompanyGenIdAndInsurenceGenId(String companyGenId, Long insurenceGenId);

	
	
}
