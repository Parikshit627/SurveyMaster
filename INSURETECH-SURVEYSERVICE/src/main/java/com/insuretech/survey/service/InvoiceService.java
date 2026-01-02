package com.insuretech.survey.service;

import com.insuretech.survey.model.InvoiceModel;
import com.insuretech.survey.model.ResponseModel;

public interface InvoiceService {

	ResponseModel save(InvoiceModel invoice);
//	ResponseModel findFlatById(Long id);
	ResponseModel getAllInvoiceByCompanyGenIdAndReferenceNumberOrsubjectMatter(String companyGenId, String referenceNumber, String subjectMatter);
	
	InvoiceModel generateInvoiceNo(InvoiceModel invoiceModel);
	
	ResponseModel deleteItemByRefrenceIdAndCompanyGenIdAndSNo(String referenceId, String companyGenId, String sNo);

}
