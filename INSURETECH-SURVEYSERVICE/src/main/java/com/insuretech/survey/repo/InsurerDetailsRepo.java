package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.InsurerDetails;

public interface InsurerDetailsRepo extends JpaRepository<InsurerDetails, Integer> {
    boolean existsByAbbreviationAndInsurer(String abbreviation, String insurer);
    boolean existsByAbbreviation(String abbreviation);
    boolean existsByInsurer(String insurer);
}
