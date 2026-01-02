package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.Efficiency;

public interface EfficiencyRepo extends JpaRepository<Efficiency, Long> {

	Efficiency findByUserName(String lowerCase);
}
