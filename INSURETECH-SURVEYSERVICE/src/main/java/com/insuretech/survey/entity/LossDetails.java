package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "loss_details", schema = "insuredb")
@NamedQuery(name = "LossDetails.findAll", query = "SELECT a FROM LossDetails a")
public class LossDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sr;
    
    private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId; 
    private Timestamp dateOfLoss;
    private String timeOfloss;
    private String location;
    private String spotSurvey;
    @Column(length=1000)
    private String causeOfLoss;
    private String reportedTo;
    private String occupancy;
    private String injury;
    private String remarksLoss;
    private String workshop;
    private Timestamp dated;
    private String estimated;
    private String cashless;
    private String amountWords;
    private String address;
    private String remark;
    
    // update code save Calculation details 
    private String accidentalLabourTotal;
    private String paintingLabour75percent;
    private String towingEstimateAnyToatl;
    private String paintEstimation25Percent;
    private String totalPaintAllowedCal;
    private String totalPaintTaxCal;
    private String totalLabourAmount;
    private String totalLabourGST;
    private String towingAmount;
    private String towingEstimate;
    private String towingGST;
    private String gstSummaryAllowedPart;
    private String gstSummaryGstPart;
    private String gstSummaryDepPart;
    private String dealerType;
    
    @Column(length=15)
    private String labourPart25PercentTax;
    
    @Column(length=100)
    private String dep50Percent;
    
    @Column(length=15)
    private String totalEstimated;
    @Column(length=15)
    private String totalReplace;
    @Column(length=15)
    private String totalRepair;
    @Column(length=15)
    private String totalPaintEstimate;
    @Column(length=15)
    private String totalAllowed;
    @Column(length=15)
    private String gstEstimated;
    @Column(length=15)
    private String gstReplace;
    @Column(length=15)
    private String gstRepair;
    @Column(length=15)
    private String gstPaint;
    @Column(length=15)
    private String gstAllowed;
    
	@Transient
	private BigDecimal totalEstimate;
	
	@Transient
	private String taxPaidToggle;
	@Transient
	private BigDecimal subtotal;
	@Transient
	private BigDecimal gstPortion;
	
    private Timestamp createdDtm;
    private Timestamp updatedDtm;

    
// Change -- Aman -- Start -- 12-11-2025
    @Column(length=15)
    private String vehicleLoaded;
    
    @Column(length=15)
    private Long loadedWeight;
    
    @Column(length=15)
    private String destination;
    
    @Column(length=15)
    private String origin;
 // Change -- Aman -- End -- 12-11-2025
}
