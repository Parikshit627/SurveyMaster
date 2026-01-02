package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "part_assessment_pro", schema = "insuredb")
public class PartAssessmentPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "s_no")
    private BigDecimal sNo;

    @Column(name = "hsn")
    private String hsn;

    @Column(name = "parts_name")
    private String partsName;

    @Column(name = "type")
    private String type;

    @Column(name = "dep")
    private String dep;

    @Column(name = "estimated")
    private BigDecimal estimated;

    @Column(name = "qe_qa")
    private String qeQa;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "allowed")
    private BigDecimal allowed;

    @Column(name = "gst_rate")
    private String gstRate;

    @Column(name = "gst")
    private BigDecimal gst;

    @Column(name = "bill_sr")
    private String billSr;

    @Column(name = "with_tax")
    private BigDecimal withTax;

    @Column(name = "dep_amount")
    private BigDecimal depAmount;

    @Column(name = "assessed")
    private BigDecimal assessed;

    @Column(name = "imt")
    private String imt;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "tax_paid")
    private BigDecimal taxPaid;

    @Column(name = "metal_parts")
    private BigDecimal metalParts;

    @Column(name = "metal_gst")
    private BigDecimal metalGst;

    @Column(name = "metal_dep")
    private BigDecimal metalDep;

    @Column(name = "plastic_parts")
    private BigDecimal plasticParts;

    @Column(name = "plastic_gst")
    private BigDecimal plasticGst;

    @Column(name = "plastic_dep")
    private BigDecimal plasticDep;

    @Column(name = "glass_parts")
    private BigDecimal glassParts;

    @Column(name = "glass_gst")
    private BigDecimal glassGst;

    @Column(name = "second_hand")
    private BigDecimal secondHand;

    @Column(name = "second_hand_gst")
    private BigDecimal secondHandGst;

    @Column(name = "second_hand_dep")
    private BigDecimal secondHandDep;

    @Column(name = "other_parts")
    private BigDecimal otherParts;

    @Column(name = "other_gst")
    private BigDecimal otherGst;

    @Column(name = "other_dep")
    private BigDecimal otherDep;

//    @Column(name = "total_parts")
    private BigDecimal totalParts;

    @Column(name = "gst18percent")
    private BigDecimal gst18Percent;

    @Column(name = "tax18percent")
    private BigDecimal tax18Percent;

    @Column(name = "dep18percent")
    private BigDecimal dep18Percent;

    @Column(name = "gst28percent")
    private BigDecimal gst28Percent;

    @Column(name = "tax28percent")
    private BigDecimal tax28Percent;

    @Column(name = "dep28percent")
    private BigDecimal dep28Percent;

    @Column(name = "gst0percent")
    private BigDecimal gst0Percent;

    @Column(name = "tax0percent")
    private BigDecimal tax0Percent;

    @Column(name = "dep0percent")
    private BigDecimal dep0Percent;

    @Column(name = "gst5percent")
    private BigDecimal gst5Percent;

    @Column(name = "tax5percent")
    private BigDecimal tax5Percent;

    @Column(name = "dep5percent")
    private BigDecimal dep5Percent;

//    @Column(name = "company_gen_id")
    private String companyGenId;

//    @Column(name = "insurence_gen_id")
    private Long insurenceGenId;

//    @Column(name = "created_dtm")
    private Timestamp createdDtm;

//    @Column(name = "updated_dtm")
    private Timestamp updatedDtm;
    
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
