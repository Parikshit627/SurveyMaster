package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Notes;

@Repository
public interface NotesRepo extends JpaRepository<Notes, Long>{

	List<Notes> findByConclusionId(Long conclusionId);
	
	@Query("SELECT a.description FROM Notes a WHERE a.conclusionId=:conclusionId")
	List<String> findDesByConclusionId(@Param("conclusionId")Long conclusionId);
}
