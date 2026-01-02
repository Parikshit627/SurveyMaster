package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "calculation_part_assessment_pro", schema = "insuredb")
public class CalculationPartAssessmentPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  
    private BigDecimal totalEstimated;
    private BigDecimal totalAllowed;
    private BigDecimal totalGst;
    private BigDecimal totalWithTax;
    private BigDecimal totalDepAmount;
    private BigDecimal totalAssessed;
    private BigDecimal totalTax;
    private BigDecimal totalTaxPaid;

    private BigDecimal totalMetalParts;
    private BigDecimal totalMetalGst;
    private BigDecimal totalMetalDep;

    private BigDecimal totalPlasticParts;
    private BigDecimal totalPlasticGst;
    private BigDecimal totalPlasticDep;

    private BigDecimal totalGlassParts;
    private BigDecimal totalGlassGst;

    private BigDecimal totalSecondHand;
    private BigDecimal totalSecondHandGst;
    private BigDecimal totalSecondHandDep;

    private BigDecimal totalOtherParts;
    private BigDecimal totalOtherGst;
    private BigDecimal totalOtherDep;

    private BigDecimal totalParts;

    private BigDecimal totalGst18Percent;
    private BigDecimal totalTax18Percent;
    private BigDecimal totalDep18Percent;

    private BigDecimal totalGst28Percent;
    private BigDecimal totalTax28Percent;
    private BigDecimal totalDep28Percent;

    private BigDecimal totalGst0Percent;
    private BigDecimal totalTax0Percent;
    private BigDecimal totalDep0Percent;

    private BigDecimal totalGst5Percent;
    private BigDecimal totalTax5Percent;
    private BigDecimal totalDep5Percent;

    private BigDecimal totalExDep;
    private BigDecimal totalExAllowed;
    private BigDecimal totalExTax;
    
    private String companyGenId;
    private Long insurenceGenId;
    private String policyType;

    private Timestamp createdDtm;

    private Timestamp updatedDtm;
    
    
    
//  Change -- Aman -- Start -- 05-09-2025
  private Boolean gstOnEstimate;
//   Change -- Aman -- End -- 05-09-2025
  
  private String invoiceNumber;
  private BigDecimal amount;
  private Timestamp issuedOn;
  private Timestamp receivedOn;

    
}
