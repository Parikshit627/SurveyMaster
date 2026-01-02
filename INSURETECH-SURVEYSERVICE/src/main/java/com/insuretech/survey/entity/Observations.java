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
@Table(name = "observations_details", schema = "insuredb")
@NamedQuery(name = "Observations.findAll", query = "SELECT a FROM Observations a")
public class Observations {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sr;
	
	@Column(nullable=false)
	private Long conclusionId;
	
	private String description;
	private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
