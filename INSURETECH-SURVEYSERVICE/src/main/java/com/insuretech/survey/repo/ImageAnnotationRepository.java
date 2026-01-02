package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.ImageAnnotation;

import jakarta.transaction.Transactional;

@Repository
public interface ImageAnnotationRepository extends JpaRepository<ImageAnnotation,Long>{

	List<ImageAnnotation> findByInsuranceGenIdOrderByImageAnnotationIdAsc(String insuranceGenId);

	ImageAnnotation findByInsuranceGenIdAndUuidAndBboxUuid(String insurenceGenId, String uuid, String bboxUuid);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM ImageAnnotation a WHERE a.insuranceGenId = :insuranceGenId AND a.uuid = :uuid AND a.bboxUuid = :bboxUuid")
    int deleteByInsuranceGenIdAndUuidAndBboxUuid(@Param("insuranceGenId") String insuranceGenId, 
                                                @Param("uuid") String uuid, 
                                                @Param("bboxUuid") String bboxUuid);
	
}