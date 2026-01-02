package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.AiImages;

@Repository
public interface AiImagesRepo extends JpaRepository<AiImages,Long>{
	
	List<AiImages> findByAiComponentGenId(Long aiComponentGenId); 

}
