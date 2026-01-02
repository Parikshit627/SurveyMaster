package com.insuretech.survey.model;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceModel {
	  private String userId;
	    private String companyGenId;
	    private String invoiceNo;
	    private String date;
	    private String referenceNumber;
	    private String billToOption;
	    private String shippedToOption;
	    private String subjectMatter;

	    private Consigner consigner;
	    private Receiver receiver;
	    private Consignee consignee;

	    private InsuranceInfo insuranceInfo;

	    private Double estimate;
	    private Double assessment;

	    private List<Item> items;

	    private Double subtotal;
	    private Double sgst;
	    private Double cgst;
	    private Double igst;
	    private Double totalValue;
	    private Double roundoff;

	    private String totalValueWords;

	    private String paymentInfo;
	
}
