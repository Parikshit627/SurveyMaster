package com.insuretech.survey.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PartAssessmentProModel {

	  private BigDecimal sNo;
	    private String hsn;
	    private String partsName;
	    private String type;
	    private String dep;
	    private BigDecimal estimated;

	    private String qeQa;

	    private String remarks;
	    private BigDecimal allowed;
	    private String gstRate;
	    private BigDecimal gst;
	    private String billSr;
	    private BigDecimal withTax;
	    private BigDecimal depAmount;
	    private BigDecimal assessed;
	    private String imt;
	    private BigDecimal tax;
	    private BigDecimal taxPaid;

	    private BigDecimal metalParts;
	    private BigDecimal metalGst;
	    private BigDecimal metalDep;

	    private BigDecimal plasticParts;
	    private BigDecimal plasticGst;
	    private BigDecimal plasticDep;

	    private BigDecimal glassParts;
	    private BigDecimal glassGst;

	    private BigDecimal secondHand;
	    private BigDecimal secondHandGst;
	    private BigDecimal secondHandDep;

	    private BigDecimal otherParts;
	    private BigDecimal otherGst;
	    private BigDecimal otherDep;

	    private BigDecimal totalParts;

	    private BigDecimal gst18Percent;
	    private BigDecimal tax18Percent;
	    private BigDecimal dep18Percent;

	    private BigDecimal gst28Percent;
	    private BigDecimal tax28Percent;
	    private BigDecimal dep28Percent;

	    private BigDecimal gst0Percent;
	    private BigDecimal tax0Percent;
	    private BigDecimal dep0Percent;

	    private BigDecimal gst5Percent;
	    private BigDecimal tax5Percent;
	    private BigDecimal dep5Percent;
    private String companyGenId;
    private Long insurenceGenId;
    private Long id;
    
    private Boolean majorAssemble;

    private Boolean isSubRow;

    private Integer parentSNo;

    private Integer subRowIndex;
    
    private BigDecimal exDep;
    private BigDecimal exAllowed;
    private BigDecimal exTax;
	public BigDecimal getsNo() {
		return sNo;
	}
	public void setsNo(BigDecimal sNo) {
		this.sNo = sNo;
	}
    
    
	
    

	    
    
    
}
