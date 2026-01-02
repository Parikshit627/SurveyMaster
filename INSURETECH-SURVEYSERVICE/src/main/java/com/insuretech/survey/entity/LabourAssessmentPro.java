


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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "labour_assessment_pro", schema = "insuredb")
@NamedQuery(name = "LabourAssessmentPro.findAll", query = "SELECT a FROM LabourAssessmentPro a")
public class LabourAssessmentPro {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	   @Column(name = "s_no")
	    private BigDecimal sNo;   // ✅ changed from int → BigDecimal

	    private String hsn;

	    private String partsName;

	    private String remarks;

	    private BigDecimal estimated;

	    private BigDecimal replace;

	    private BigDecimal repair;   // ✅ added

	    private BigDecimal paintEstimate;

	    private BigDecimal less;

	    private BigDecimal allowed;

	    private String comment;

	    private BigDecimal labGst;

	    private BigDecimal estimateGst;

	    private BigDecimal replaceGst;

	    private BigDecimal repairGst;

	    private BigDecimal paintEstimateGst;

	    private BigDecimal allowedPaintGst;

	    private BigDecimal allowedLabourT;    // ✅ added

	    private BigDecimal labourGstT;        // ✅ added

	    private BigDecimal paint75Labour;     // ✅ added

	    private BigDecimal gstPaint75Labour;  // ✅ added

	    private BigDecimal paint25Part;       // ✅ added

	    private BigDecimal gstPaint25Part;    // ✅ added

	    private String companyGenId;

	    private Long insurenceGenId;  // ✅ keep String (matches JSON)

	    private Timestamp createdDtm;

	    private Timestamp updatedDtm;

		public BigDecimal getsNo() {
			return sNo;
		}

		public void setsNo(BigDecimal sNo) {
			this.sNo = sNo;
		}
	    
	    
}



  
