package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.WorkShopDetails;

public interface WorkShopDetailsRepo extends JpaRepository<WorkShopDetails, Integer> {
	
}
