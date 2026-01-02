package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Observations;

@Repository
public interface ObservationsRepo extends JpaRepository<Observations, Long>{

	List<Observations> findByConclusionId(Long conclusionId);
	
	@Query("SELECT a.description FROM Observations a WHERE a.conclusionId=:conclusionId")
	List<String> findDesByConclusionId(@Param("conclusionId")Long conclusionId);
}
