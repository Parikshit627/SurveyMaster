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
@Table(name = "ai_damages", schema = "insuredb")
@NamedQuery(name = "AiDamages.findAll", query = "SELECT a FROM AiDamages a")
public class AiDamages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long aiDamagesGenId;
	
	private long aiComponentGenId;

	@Column(length = 300)
	private String damages;

	private Timestamp createdDtm=new Timestamp(System.currentTimeMillis());
	private Timestamp updatedDtm;

	private String createdBy;
	private String updatedBy;
	
	
}
