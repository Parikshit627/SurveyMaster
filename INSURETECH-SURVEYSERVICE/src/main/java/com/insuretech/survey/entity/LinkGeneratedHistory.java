package com.insuretech.survey.entity;

import java.io.Serializable;
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
@Table(name = "link_generated_history", schema = "insuredb")
@NamedQuery(name = "LinkGeneratedHistory.findAll", query = "SELECT a FROM LinkGeneratedHistory a")
public class LinkGeneratedHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long linkGeneratedHistoryId;
	
	@Column(name="insurance_claim_id")
	private Long insuranceClaimId;	
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "link_name")
	private String linkName;
	
	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "company_gen_id")
	private String companyGenId;
	
	@Column(name = "link_expired")
	private Boolean linkExpired;
	
	@Column(name = "reg_code")
	private String regCode;

	

}
