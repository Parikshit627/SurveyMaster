package com.insuretech.survey.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "invoice_no_counter", schema = "insuredb")
@NamedQuery(name = "InvoiceNoCounter.findAll", query = "SELECT a FROM InvoiceNoCounter a")
public class InvoiceNoCounter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long invoiceCounterGenId;
	
	@Column(length=50,unique=true)
	private String invoiceNo;
	
	@Column(length=50)
	private Long counter;
	
	@Column(length=10)
	private String regCode;
	
//	@Column(length=10)
//    private String regAbbreviation;
//	
	@Column(length=30)
	private String companyGenId;
	
	@Column(length=6)
	private Timestamp createdDtm;
	
	@Column(length=6)
	private Timestamp updatedDtm;
	
	@Column(length=50)
	private String createdBy;
	
	@Column(length=50)
	private String updatedBy;
	
}