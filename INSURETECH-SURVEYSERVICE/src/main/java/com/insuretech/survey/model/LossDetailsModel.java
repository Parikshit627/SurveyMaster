package com.insuretech.survey.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LossDetailsModel {

    private Long sr;
    
    private String companyGenId;
    private Long insurenceGenId;
    private String workedBySurveyName;
    private String workedByUserId; 
    
    private Timestamp dateOfLoss;
    private String timeOfloss;
    private String location;
    private String spotSurvey;
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
    private String dealerType;
    
    
    
    private String accidentalLabourTotal;
    private String paintingLabour75percent;
    private String towingEstimateAnyToatl;
    private String paintEstimation25Percent;
    private String totalPaintAllowedCal;
    private String totalPaintTaxCal;
    private String totalLabourAmount;
    private String totalLabourGST;
    private String towingAmount;
    private String towingGST;
    private String gstSummaryAllowedPart;
    private String gstSummaryGstPart;
    private String gstSummaryDepPart;
    private String towingEstimate;
    private String dep50Percent;
    private String labourPart25PercentTax;
   
    private String totalEstimated;
    private String totalReplace;
    private String totalRepair;
    private String totalPaintEstimate;
    private String totalAllowed;
    private String gstEstimated;
    private String gstReplace;
    private String gstRepair;
    private String gstPaint;
    private String gstAllowed;


 // Change -- Aman -- Start -- 12-11-2025
    private String vehicleLoaded;
    private Long loadedWeight;
    private String destination;
    private String origin;

 // Change -- Aman -- End -- 12-11-2025
    
    
    
}
