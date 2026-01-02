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
import lombok.Data;

@Data
@Entity
@Table(name = "ai_component_details", schema = "insuredb")
@NamedQuery(name = "AiComponentDetails.findAll", query = "SELECT a FROM AiComponentDetails a")
public class AiComponentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long aiComponentGenId;
	
	private long insurenceGenId;
	@Column(length = 50)
	private String companyGenId;
	@Column(length = 50)
	private String referenceNo;
	@Column(length = 50)
	private String vechileNo;
	
	@Column(length = 50)
	private String name;

	private Integer classId;

	private BigDecimal labour;
	private BigDecimal paint;
	private BigDecimal price;

	private Timestamp createdDtm=new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;

}
