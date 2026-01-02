package com.insuretech.survey.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AssemblyDetailsModel {

   private Long sr;
   private Long orderSr;
    
    private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId; 
    
    
//    private String repairReplace;
//    private Integer repairEstimated;
//    private Integer repairAssessed;
//    private Integer repairBilled;
//    private Integer refitEstimated;
//    private Integer refitAssessed;
//    private Integer refitBilled;
//    private Integer paintEstimated;
//    private Integer paintAssessed;
//    private Integer paintBilled;
//    
//    private String workshopBillNo;
//    private String partInvoiceNo;
//    private String labourInvoiceNo;
//    private String paintInvoiceNo;
//    
//    
//    private String addonDesc;
    
    private BigDecimal allowed;
    private String assemblyName;
    private BigDecimal assessed;
    private String billSr;
    private BigDecimal depreciated;
    private BigDecimal estimated;
	private String depreciation;

    private String gst;
    private String hsnCode;
    private Character partType;
    private String remarks;
    private BigDecimal withTaxwithTax;
    
    
}
