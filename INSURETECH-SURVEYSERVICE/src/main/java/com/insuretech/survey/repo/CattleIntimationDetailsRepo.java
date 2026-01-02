package com.insuretech.survey.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.CattleIntimationDetails;
import com.insuretech.survey.model.CattleDashboardModel;

@Repository
public interface CattleIntimationDetailsRepo extends JpaRepository<CattleIntimationDetails, Long> {

	CattleIntimationDetails findByReferenceNo(String referenceNo);

	@Query(
		    value = """
		        SELECT new com.insuretech.survey.model.CattleDashboardModel(
		            c.cattleIntimationGenId,
		            c.referenceNo,
		            c.dateIntimation,
		            c.dateLoss,
		            c.placeLoss,
		            c.assetDetail,
		            c.policyNo,
		            c.claimNo,
		            c.insurer,
		            c.insurerName,
		            c.workshopName,
		            c.estimatedAmount,
		            c.provisionalAmount,
		            c.branch,
		            c.surveyor,
		            c.backOfficer,
		            c.createdBy,
		            c.currentStatus,
		            c.assignTo,
		            s.description
		        )
		        FROM CattleIntimationDetails c
		        LEFT JOIN Status s 
		            ON s.statusId = c.currentStatus
		        WHERE c.assignTo = :assignTo
		          AND c.companyGenId = :companyGenId
		        ORDER BY c.cattleIntimationGenId DESC
		    """,
		    countQuery = """
		        SELECT COUNT(c)
		        FROM CattleIntimationDetails c
		        WHERE c.assignTo = :assignTo
		          AND c.companyGenId = :companyGenId
		    """
		)
	Page<CattleDashboardModel> findCattleDashboardByAssignTo(@Param("assignTo") String assignTo, @Param("companyGenId") String companyGenId, Pageable pageable);

	@Query(
		    value = """
		        SELECT new com.insuretech.survey.model.CattleDashboardModel(
		            c.cattleIntimationGenId,
		            c.referenceNo,
		            c.dateIntimation,
		            c.dateLoss,
		            c.placeLoss,
		            c.assetDetail,
		            c.policyNo,
		            c.claimNo,
		            c.insurer,
		            c.insurerName,
		            c.workshopName,
		            c.estimatedAmount,
		            c.provisionalAmount,
		            c.branch,
		            c.surveyor,
		            c.backOfficer,
		            c.createdBy,
		            c.currentStatus,
		            c.assignTo,
		            s.description
		        )
		        FROM CattleIntimationDetails c
		        LEFT JOIN Status s 
		            ON s.statusId = c.currentStatus
		        WHERE c.companyGenId = :companyGenId
		        ORDER BY c.cattleIntimationGenId DESC
		    """,
		    countQuery = """
		        SELECT COUNT(c)
		        FROM CattleIntimationDetails c
		        WHERE c.companyGenId = :companyGenId
		    """
		)
	Page<CattleDashboardModel> findCattleDashboard(@Param("companyGenId") String companyGenId, Pageable pageable);

}
