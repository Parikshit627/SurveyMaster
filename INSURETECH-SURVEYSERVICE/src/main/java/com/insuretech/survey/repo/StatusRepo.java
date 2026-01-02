package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.Status;

public interface StatusRepo extends JpaRepository<Status, Integer> {

	Status findByDescription(String string);

	Status findByStatusId(int int1);
}
