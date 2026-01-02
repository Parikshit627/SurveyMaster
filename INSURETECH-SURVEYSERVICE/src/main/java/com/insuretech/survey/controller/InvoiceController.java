package com.insuretech.survey.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insuretech.survey.entity.Invoice;
import com.insuretech.survey.model.InvoiceModel;
import com.insuretech.survey.model.ResponseModel;
import com.insuretech.survey.service.InvoiceService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

	@Autowired
	InvoiceService invoiceService;
	  // ✅ Save a new Invoice
    @PostMapping("/save/Invoice")
    public ResponseModel saveInvoice(@RequestBody InvoiceModel invoice) {
    	ResponseModel  saved = invoiceService.save(invoice);
        return saved;
    }

//    @GetMapping("/flat/{id}")
//    public ResponseModel getFlatInvoiceById(@PathVariable Long id) {
//        ResponseModel response = invoiceService.findFlatById(id);
//            return response;
//        
//    }
    
    @GetMapping("/getAllInvoiceByCompanyGenIdAndReferenceNumberOrsubjectMatter")
    public ResponseModel getAllInvoiceByCompanyGenIdAndReferenceNumberOrsubjectMatter(
            @RequestParam String companyGenId, @RequestParam(required = false) String referenceNumber, @RequestParam(required = false) String subjectMatter) {
        String methodName = "getAllInvoiceByCompanyGenIdAndReferenceNumberOrsubjectMatter";
        ResponseModel model = new ResponseModel();
		try {
			log.info("Request : Finding All Invoice By CompanyGenId" + companyGenId + "  Method Name" + methodName + " Class : "
					+ this.getClass());
			model=invoiceService.getAllInvoiceByCompanyGenIdAndReferenceNumberOrsubjectMatter(companyGenId, referenceNumber, subjectMatter);

		} catch (Exception e) {
			log.error("An error occurred while Finding All Invoice By CompanyGenId " + companyGenId + e.getMessage(),
					"  Method Name" + methodName + " Class : " + this.getClass());
		}
        return model;
	}
    
    @GetMapping("/deleteItemByRefrenceIdAndCompanyGenIdAndSNo")
    public ResponseModel deleteItemByRefrenceIdAndCompanyGenIdAndSNo(@RequestParam String referenceId,  @RequestParam String companyGenId,
        @RequestParam String sNo) {
        ResponseModel response = invoiceService.deleteItemByRefrenceIdAndCompanyGenIdAndSNo(referenceId, companyGenId, sNo);
        return response;
    }
	
}
