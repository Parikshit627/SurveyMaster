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
@Table(name = "notes_details", schema = "insuredb")
@NamedQuery(name = "Notes.findAll", query = "SELECT a FROM Notes a")
public class Notes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long sr;
	
	@Column(nullable=false)
	private Long conclusionId;
	
	private String description;
	private Timestamp createdDtm;
	private Timestamp updatedDtm;
	
	
}
