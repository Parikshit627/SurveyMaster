package com.insuretech.survey.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

@Data
public class PartsAssessmentProJson {
    private List<PartAssessmentProModel> parts;
    private CalculationPartAssessmentProModel totals;
    
//    Change -- Aman -- Start -- 11-09-2025
    private Invoice invoice; // field for invoice

    @Data
    public static class Invoice {
    	private String invoiceNumber;
    	  private BigDecimal amount;
    	  private Timestamp issuedOn;
    	  private Timestamp receivedOn;
    }
//  Change -- Aman -- End -- 11-09-2025
}
