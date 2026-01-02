package com.insuretech.survey.model;


import com.insuretech.survey.entity.Receiver;

import lombok.Data;

@Data

public class InvoiceFlatResponse {
	 private Long id;
	    private String userId;
	    private String companyGenId;
	    private String invoiceNo;
	    private String date;

	    // Consigner fields
	    private String consignerName;
	    private Object consigner;
	    private Object invoice;
	    private String consignerEmail;

	    // Receiver fields
//	    private String receiverInsurerName;
	    private Object receiver;

	    // Consignee fields
	    private String consigneeInsurerName;
	    private Object consignee;

	    // Insurance Info fields
//	    private String insuranceReportRefNumber;
	    private Object insuranceInfo;

	    // Totals
	    private Double totalValue;

	    // Getters and Setters
}
