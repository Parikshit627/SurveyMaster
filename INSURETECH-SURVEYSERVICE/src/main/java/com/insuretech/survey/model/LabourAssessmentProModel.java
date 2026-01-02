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
public class LabourAssessmentProModel {

    private Long id;

    private BigDecimal sNo;   // can handle 1, 1.1, etc.

    private String hsn;

    private String partsName;

    private String remarks;

    private BigDecimal estimated;

    private BigDecimal replace;

    private BigDecimal repair;  // ✅ added

    private BigDecimal paintEstimate;

    private BigDecimal less;

    private BigDecimal allowed;

    private String comment;

    private BigDecimal labGst;

    private BigDecimal estimateGst;

    private BigDecimal replaceGst;

    private BigDecimal repairGst;

    private BigDecimal allowedLabourT;   // ✅ added

    private BigDecimal labourGstT;       // ✅ added

    private BigDecimal paintEstimateGst;

    private BigDecimal allowedPaintGst;

    private BigDecimal paint75Labour;    // ✅ added

    private BigDecimal gstPaint75Labour; // ✅ added

    private BigDecimal paint25Part;      // ✅ added

    private BigDecimal gstPaint25Part;   // ✅ added

    private String companyGenId;

    private Long insurenceGenId; // JSON sends string, not number

	public BigDecimal getsNo() {
		return sNo;
	}

	public void setsNo(BigDecimal sNo) {
		this.sNo = sNo;
	}
    
    
}
