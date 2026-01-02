package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.CattleComments;
import com.insuretech.survey.model.CattleCommentsModel;

@Repository
public interface CattleCommentsRepo extends JpaRepository<CattleComments, Long> {

	@Query("""
			   SELECT new com.insuretech.survey.model.CattleCommentsModel(
			       c.cattleCommentsGenId, c.comments
			   )
			   FROM CattleComments c
			   WHERE c.cattleIntimationGenId = :cattleIntimationGenId
			""")
	List<CattleCommentsModel> findByCattleIntimationGenId(@Param("cattleIntimationGenId") Long cattleIntimationGenId);

}
