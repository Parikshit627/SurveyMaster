package com.insuretech.survey.repo;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.PhotoPendingAi;

import jakarta.transaction.Transactional;

@Repository
public interface PhotoPendingAiRepo extends JpaRepository<PhotoPendingAi,Long>{

	@Query("SELECT p FROM PhotoPendingAi p " +
		       "WHERE p.docUploadTime BETWEEN COALESCE(:startDate, p.docUploadTime) " +
		       "AND COALESCE(:endDate, p.docUploadTime)")
		List<PhotoPendingAi> findAllByUploadTimeRange(
		        @Param("startDate") Timestamp startDate,
		        @Param("endDate") Timestamp endDate
		);



//	List<PhotoPendingAi> findByDocUploadTimeBetweenOrderByInsurenceGenIdDesc(
//	        Timestamp startDate, Timestamp endDate);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM PhotoPendingAi p WHERE p.insurenceGenId IN :insurenceGenIds")
    int deleteByInsurenceGenIds(@Param("insurenceGenIds") List<Long> insurenceGenIds);



}
