package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.AiComponentDetails;

@Repository
public interface AIComponentDetailsRepo extends JpaRepository<AiComponentDetails,Long>{
    
	@Query(value = """
			SELECT 
			    c.ai_component_gen_id,
			    c.insurence_gen_id,
			    c.company_gen_id,
			    c.reference_no,
			    c.vechile_no,
			    c.name,
			    c.class_id,
			    c.labour,
			    c.paint,
			    c.price,
			    COALESCE(array_agg(DISTINCT d.damages) FILTER (WHERE d.damages IS NOT NULL), '{}') AS damages,
			    COALESCE(array_agg(DISTINCT i.images_uuid) FILTER (WHERE i.images_uuid IS NOT NULL), '{}') AS images
			FROM insuredb.ai_component_details c
			LEFT JOIN insuredb.ai_damages d ON c.ai_component_gen_id = d.ai_component_gen_id
			LEFT JOIN insuredb.ai_images i ON c.ai_component_gen_id = i.ai_component_gen_id
			WHERE c.insurence_gen_id = :insurenceGenId
			GROUP BY 
			    c.ai_component_gen_id,
			    c.insurence_gen_id,
			    c.company_gen_id,
			    c.reference_no,
			    c.vechile_no,
			    c.name,
			    c.class_id,
			    c.labour,
			    c.paint,
			    c.price
			""", nativeQuery = true)
	List<Object[]> findByInsurenceGenId(Long insurenceGenId);
}
