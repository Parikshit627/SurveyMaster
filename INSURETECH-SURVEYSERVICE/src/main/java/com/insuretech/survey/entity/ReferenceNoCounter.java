package com.insuretech.survey.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reference_no_counter", schema = "insuredb")
@NamedQuery(name = "ReferenceNoCounter.findAll", query = "SELECT a FROM ReferenceNoCounter a")
public class ReferenceNoCounter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long counterGenId;
	
	private String regCode;
	private Integer counter;
	private String regAbbreviation;

    private Timestamp createdDtm;
	private Timestamp updatedDtm;
	private String createdBy;
	private String updatedBy;
	
//	private String companyAbbrivation;

	
}