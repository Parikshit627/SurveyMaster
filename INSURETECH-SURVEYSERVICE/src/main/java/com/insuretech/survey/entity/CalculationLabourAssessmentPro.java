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
@Table(name = "calculation_labour_assessment_pro", schema = "insuredb")
public class CalculationLabourAssessmentPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  
    private BigDecimal totalEstimated;
    private BigDecimal totalReplace;
    private BigDecimal totalRepair;
    private BigDecimal totalPaintEstimate;
    private BigDecimal totalLess;
    private BigDecimal totalAllowed;
    private BigDecimal totalLabGst;
    private BigDecimal totalEstimateGst;
    private BigDecimal totalReplaceGst;
    private BigDecimal totalRepairGst;
    private BigDecimal totalPaintEstimateGst;
    private BigDecimal totalAllowedPaintGst;
    private BigDecimal totalAllowedLabourT;
    private BigDecimal totalLabourGstT;
    private BigDecimal totalPaint75Labour;
    private BigDecimal totalGstPaint75Labour;
    private BigDecimal totalPaint25Part;
    private BigDecimal totalGstPaint25Part;
    private Boolean accidentalLabour;
    private Boolean paintLabour;

    private String companyGenId;
    private Long insurenceGenId;
    private String policyType;


    private Timestamp createdDtm;
    private Timestamp updatedDtm;

    
}
