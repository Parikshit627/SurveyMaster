package com.insuretech.survey.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.FinalDocumentSubmission;
import org.springframework.data.jpa.repository.Modifying; 
import org.springframework.transaction.annotation.Transactional; 

@Repository
public interface FinalDocumentSubmissionRepo extends JpaRepository<FinalDocumentSubmission, Long>{

	List<FinalDocumentSubmission> findByCompanyGenIdAndAndInsurenceGenIdAndDocName(String companyGenId,Long insurenceGenId,String docName);
//	@Query("""
//		    SELECT f
//		    FROM FinalDocumentSubmission f
//		    WHERE f.insurenceGenId = :insurenceGenId
//		    AND (:companyGenId IS NULL OR :companyGenId = '' OR f.companyGenId = :companyGenId)
//		    AND LOWER(f.vehicleNumber) = LOWER(:vehicleNumber)
//		    """)
//		List<FinalDocumentSubmission> findByInsurenceGenIdAndCompanyGenIdAndVehicleNumberIgnoreCase(
//		    @Param("insurenceGenId") Long insurenceGenId,
//		    @Param("companyGenId") String companyGenId,
//		    @Param("vehicleNumber") String vehicleNumber
//		);
	
	
//	change -- Aman -- Start -- 08-09-2025
//	@Query("""
//		    SELECT f
//		    FROM FinalDocumentSubmission f
//		    WHERE f.insurenceGenId = :insurenceGenId
//		    AND (:companyGenId IS NULL OR :companyGenId = '' OR f.companyGenId = :companyGenId)
//		    AND LOWER(f.vehicleNumber) LIKE LOWER(CONCAT('%', :vehicleNumber, '%'))
//		    """)
//		List<FinalDocumentSubmission> findByInsurenceGenIdAndCompanyGenIdAndVehicleNumberLikeIgnoreCase(
//		    @Param("insurenceGenId") Long insurenceGenId,
//		    @Param("companyGenId") String companyGenId,
//		    @Param("vehicleNumber") String vehicleNumber
//		);
//	change -- Aman -- End -- 08-09-2025
	
	

	Optional<FinalDocumentSubmission> findByDocUuid(String docUuid);
	Optional<FinalDocumentSubmission> findByDocumentIdAndInsurenceGenId(Long documentId, Long insurenceGenId);
	
	@Query(value="Select f.docName as docName , f.docUuid as docUuid from FinalDocumentSubmission f WHERE f.companyGenId = :companyGenId AND f.insurenceGenId = :insurenceGenId")
	List<Map<String,Object>> findByCompanyGenIdAndAndInsurenceGenId(Long insurenceGenId,String companyGenId);

	List<FinalDocumentSubmission> findByInsurenceGenIdAndCompanyGenIdAndVehicleNumberLikeIgnoreCaseAndUploadTypeIn(
			Long insurenceGenId, String companyGenId, String trim, List<String> formatsToFetch);
	
	@Query(value = "SELECT * FROM insuredb.final_document_submission f " +
            "WHERE f.company_gen_id = :companyGenId " +
            "AND f.insurence_gen_id = :insurenceGenId " +
            "AND f.upload_type = :uploadType",
    nativeQuery = true)
List<FinalDocumentSubmission> findByCompanyGenIdAndInsurenceGenIdAndUploadType(
     @Param("companyGenId") String companyGenId,
     @Param("insurenceGenId") Long insurenceGenId,
     @Param("uploadType") String uploadType
);
	 @Modifying
	    @Transactional
	    @Query("DELETE FROM FinalDocumentSubmission f WHERE f.saveStatus = false AND f.saveStatusChangedAt <= :thresholdTime")
	    int deleteAllBySaveStatusFalseAndSaveStatusChangedAtBefore(@Param("thresholdTime") LocalDateTime thresholdTime);

}
