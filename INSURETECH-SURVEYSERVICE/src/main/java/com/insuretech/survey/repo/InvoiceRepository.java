package com.insuretech.survey.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insuretech.survey.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	
	List<Invoice> findByCompanyGenId(String companyGenId);

	Invoice findByInvoiceNo(String invoiceNo);

	List<Invoice> findByCompanyGenIdAndReferenceNumber(String companyGenId, String referenceNumber);

	Invoice findByReferenceNumber(String referenceNumber);

	List<Invoice> findByCompanyGenIdAndSubjectMatter(String companyGenId, String subjectMatter);

	List<Invoice> findByCompanyGenIdOrderByIdAsc(String companyGenId);

}
