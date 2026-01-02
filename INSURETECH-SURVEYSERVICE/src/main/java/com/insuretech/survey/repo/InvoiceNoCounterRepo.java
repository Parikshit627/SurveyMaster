package com.insuretech.survey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insuretech.survey.entity.InvoiceNoCounter;

public interface InvoiceNoCounterRepo extends JpaRepository<InvoiceNoCounter,Long>{
    
//	InvoiceNoCounter findByRegCodeAndCompanyGenId(String regCode,String companyGenId);
	
	InvoiceNoCounter findByCompanyGenId(String companyGenId);

}
