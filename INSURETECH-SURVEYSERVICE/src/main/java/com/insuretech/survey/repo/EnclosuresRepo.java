package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Enclosures;

@Repository
public interface EnclosuresRepo extends JpaRepository<Enclosures, Long>{

	List<Enclosures> findByConclusionId(Long conclusionId);
}
