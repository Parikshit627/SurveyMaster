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
@Table(name = "email_history_details", schema = "insuredb")
@NamedQuery(name = "EmailHistory.findAll", query = "SELECT a FROM EmailHistory a")
public class EmailHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long emailGenId;
	
	@Column(length = 300)
    private String templateName;
	
	@Column(length = 300)
	private String emailTo;
	
	@Column(length = 300)
	private String cc;
	
	@Column(length = 500)
	private String subject;
	
	@Column(length = 2000)
	private String body;
	
	@Column(length = 10)
	private String status;
	
	@Column(length = 50)
	private String referenceNo;
	
	@Column(length = 50)
	private String companyGenId;

	private Timestamp createdDtm;
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;

}
