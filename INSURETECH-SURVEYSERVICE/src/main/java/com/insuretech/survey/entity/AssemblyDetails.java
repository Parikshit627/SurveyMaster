package com.insuretech.survey.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
@Table(name = "assembly_details", schema = "insuredb")
@NamedQuery(name = "AssemblyDetails.findAll", query = "SELECT a FROM AssemblyDetails a")
public class AssemblyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sr;

	private String companyGenId;
	private Long insurenceGenId;
	private String workedBySurveyName;
	private String workedByUserId;

//    private String assemblyName;
//    private String repairReplace;
//    private Integer repairEstimated;
//    private Integer repairAssessed;
//    private Integer repairBilled;
//    private Integer refitEstimated;
//    private Integer refitAssessed;
//    private Integer refitBilled;
//    private Integer paintEstimated;
//    private Integer paintAssessed;
//    private Integer paintBilled;
//    private String gst;
//    private String workshopBillNo;
//    private String partInvoiceNo;
//    private String labourInvoiceNo;
//    private String paintInvoiceNo;
//    private String remarks;
//    private String hsnCode;
//    private String addonDesc;

	private BigDecimal allowed;
	private String assemblyName;
	private BigDecimal assessed;
	private String billSr;
	private BigDecimal depreciated;
	private BigDecimal estimated;
	private String gst;
	private String hsnCode;
	private Character partType;
	private String remarks;
	private String depreciation;
	private BigDecimal withTaxwithTax;
	private Long orderSr;
	

	private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
