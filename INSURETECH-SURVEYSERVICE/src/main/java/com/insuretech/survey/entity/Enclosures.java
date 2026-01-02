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
@Table(name = "enclosures_details", schema = "insuredb")
@NamedQuery(name = "Enclosures.findAll", query = "SELECT a FROM Enclosures a")
public class Enclosures {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sr;
	
	@Column(nullable=false)
	private Long conclusionId;
	
	private String description;
	private Timestamp createdDtm;
	private Timestamp updatedDtm;

}
