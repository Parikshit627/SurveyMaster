package com.insuretech.survey.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetalCalculationModel {

    
    private String companyGenId;
    private Long insurenceGenId;
    private BigDecimal metalAllowed;
    private BigDecimal metalGST;
    private BigDecimal metalDep;

    private BigDecimal plasticAllowed;
    private BigDecimal plasticGST;
    private BigDecimal plasticDep;

    private BigDecimal glassAllowed;
    private BigDecimal glassGST;
    private BigDecimal glassDep;

    private BigDecimal iiHandAllowed;
    private BigDecimal iiHandGST;
    private BigDecimal iiHandDep;

    private BigDecimal othersAllowed;
    private BigDecimal othersGST;
    private BigDecimal othersDep;
	private String taxPaidToggle;

    
    
    private BigDecimal totalestimate;
    private BigDecimal gstPortion;
    private BigDecimal subtotal;
    
    
    
}
