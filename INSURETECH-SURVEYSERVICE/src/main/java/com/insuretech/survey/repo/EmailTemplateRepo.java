package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.EmailTemplate;

@Repository
public interface EmailTemplateRepo extends JpaRepository<EmailTemplate,Long>{
	
	EmailTemplate findByEmailTempId(String emailTempId);

}
